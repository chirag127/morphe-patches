# Pluto TV — patch / install / debug runbook (handoff)

This is a handoff note for a **local Claude Code session** running on the Windows
machine that has `adb` access to the Onn TV. The prior work was done in a cloud
session that could not reach the device; everything below is committed so a local
session can continue directly. Device under test: **`192.168.12.209:5555`**.

## State of play (what's already built and verified)

Branch: `claude/pluto-tv-framework-8x9kjt`.

Two patches exist for Pluto (`tv.pluto.android`, validated against
`5.66.0-leanback`, versionCode 510400248):

1. **`Skip ads`** — `patches/.../pluto/ads/{Fingerprints,SkipAdsPatch}.kt`
   Three hooks, all confirmed against the real dex:
   - `BeaconTracker.fire(String,List)V` → `return-void` (kills SSAI tracking beacons)
   - `PauseAdsImageBinder.showPauseAdImageAfterInactivity(Bitmap,List)V` → `return-void` (kills pause-ad overlay)
   - `ClickableAdsBinder$Companion.bind(...)Disposable` → returns `Disposables.disposed()` (kills interactive clickable-ad overlays)
2. **`Override certificate pinning`** — `patches/.../pluto/misc/security/CertificatePinningPatch.kt`
   Edits Pluto's shipped `network_security_config.xml` to add `<certificates src="user" overridePins="true">`
   to a new `<base-config>` **and** to the existing `pluto.tv` `<domain-config>` (which was system-only).
   Enables an HTTPS-filtering proxy (AdGuard) to MITM the HLS manifest.

**Verified by static analysis, NOT yet built or run on a device.** This runbook's
first execution is the first real test.

### Key architectural finding (why these are the hooks)

Pluto uses **server-side ad stitching (SSAI)** — the ad video is baked into the
HLS manifest. The client-side ad-break timeline only drives beacons + UI + seek-
blocking, so **bytecode cannot remove the in-stream ad video** (live TV especially).
The `Skip ads` patch removes the ad *experience* (overlays, beacons). Removing the
ad *video* requires the manifest-rewrite path, which is what the cert patch unlocks:
with AdGuard able to see the `.m3u8`, an SCTE-35 `#EXT-X-CUE-OUT`/`CUE-IN` `$replace`
rule can neuter ad segments while keeping the break slate (the "FoxSports trick").
Pluto's whole stack is JVM (OkHttp/Volley/media3, no native libcurl) and shows no
active pinning, so unlike Prime Video this MITM should actually succeed.

## Step 0 — one-time setup

```bash
cd testing
./scripts/setup.sh                          # fetch morphe-cli.jar + APKEditor.jar, check java/adb
cp config/device.env.example config/device.env
# edit device.env:  DEVICE_IP="192.168.12.209"   DEVICE_PORT="5555"
# drop the Pluto .apkm into testing/apks/ (e.g. apks/pluto.apkm)
```
`pluto` → `tv.pluto.android` is already registered in `config/apps.conf`.
Building the bundle needs a GitHub token for the Morphe registry
(`GITHUB_ACTOR`/`GITHUB_TOKEN`, or `gpr.user`/`gpr.key` in `~/.gradle/gradle.properties`).

## Step 1 — build, patch, install, debug (the loop)

```bash
cd testing
./scripts/build.sh
./scripts/patch.sh pluto apks/pluto.apkm -e "Skip ads" -e "Override certificate pinning"
./scripts/deploy.sh pluto
./scripts/logs.sh pluto 'Ad|Beacon|Stitcher|Clickable|Pinn|SSL|Trust|CertificatePinner'
```

## Step 2 — what to look for (this is the debug signal)

- **App launches, no crash.** A `VerifyError` or immediate crash means a hook's
  bytecode edit is bad — check which of the three fired last.
- **Hooks 1–3 effective:** no pause-ad image on pause; no interactive/clickable ad
  overlay during breaks. (In-stream ad *video* will still play — expected; that's
  the SSAI residue the manifest-rewrite step targets.)
- **Cert patch effective:** with AdGuard's CA installed on the TV (user cert) and
  HTTPS filtering on, AdGuard's log should now show **decrypted `*.pluto.tv`
  requests, including the `.m3u8` manifest**. If any host throws
  `SSLPeerUnverified` / `CertificatePinner` in logcat, that host pins
  programmatically (none expected) and needs a targeted bytecode nop.

## Step 3 — capture for the next iteration

Once AdGuard can see the manifest, during a **mid-break (ad slate showing)** grab the
**media playlist `.m3u8`** — the one carrying `#EXT-X-CUE-OUT` / `#EXT-X-CUE-IN` or
`#EXT-X-DATERANGE` (SCTE-35) markers and the ad segment URIs. That manifest is what a
`$replace` rule is written against to strip the ad creatives while keeping the slate.

Save it to `testing/out/pluto-break.m3u8` (gitignored) and share it for rule authoring.

## Notes

- The Onn TV shows an "Allow debugging?" prompt on first `adb connect` — accept it.
- `deploy.sh` uninstalls the stock app first (signatures differ), wiping Pluto's
  local data — fine for a test box.
- This runbook and the `testing/` harness were brought onto the Pluto branch so a
  single checkout has both the patches and the tooling.
