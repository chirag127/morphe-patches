# Morphe Patches — chirag127

## What this is

Unofficial Morphe/ReVanced-lineage patch bundle for ~30 Android apps, targeting Poco F7 (HyperOS 3 / Android 15) but device-agnostic across Android 12+. GPL-3.0-or-later.

This repo produces a single `.mpp` (Morphe patch bundle) you feed to Morphe Manager. Patches strip ads, unlock Pixel-exclusive apps on non-Pixel hardware, override themes, and add small quality-of-life features on top of Piko/Morphe/Rushi/Hoodle patch stacks.

Nothing here is affiliated with ReVanced, Morphe, or any of the upstream authors we subtree from. This is a personal fork bundle — use at your own risk.

---

## Installation (one-time setup)

1. **Install Obtainium** — [releases](https://github.com/ImranR98/Obtainium/releases) (or via F-Droid, or direct APK). Obtainium is the source-agnostic APK updater; we use it to keep Morphe Manager + the patched APKs fresh.
2. **Install Morphe Manager** — [releases](https://github.com/MorpheApp/morphe-manager/releases). This is the runner that consumes `.mpp` bundles and produces installable patched APKs.
3. **Grant Morphe Manager** the following permissions when it prompts:
   - Storage access (read APKs, write output)
   - Install unknown apps (needed to sign + install patched APKs)
   - Optional: notification access (progress toasts during long patches)
4. **(Optional) Magisk + MicroG spoof** — required only if you plan to patch apps that hard-check Play Integrity (some banking / streaming apps). For the current app coverage below, this is NOT required. Only Truecaller checks integrity and our patch removes that check.

---

## Auto-updated patched APKs (recommended)

The simplest flow: subscribe Obtainium to the pre-built releases repo and let it push updates.

**Obtainium subscribe URL:**

```
https://github.com/chirag127/morphe-releases
```

> **Note:** the releases repo is **private** per personal policy — request access via a GitHub issue on this repo (include your GitHub username) and you'll be added as a collaborator. The releases repo mirrors this repo's `.mpp` + pre-patched APKs per app per version.
>
> **Public alternative:** clone this repo, build the `.mpp` locally (see "Building from source" below), then use Morphe Manager to apply it against your own APKs. This is the always-available path and does not require repo access.

Once subscribed, Obtainium will pull the latest patched APK on its normal schedule (default: daily). It will prompt on install because non-Play-Store APKs need user consent per install.

---

## Manual usage (advanced)

Prefer this route if you want per-patch control, or you want to avoid the releases repo entirely.

1. **Download the app's OFFICIAL APK** from a mirror site: [apkpure.com](https://apkpure.com) or [apkmirror.com](https://apkmirror.com). Get the exact version tag listed under the patch's `@CompatiblePackage` block. **Do NOT download modded / cracked / "premium unlocked" APKs floating around Telegram or shady sites** — those have their own tampering baked in and our fingerprints will not match. We only patch official APKs.
2. **Download the latest `.mpp`** from this repo's [Releases tab](https://github.com/chirag127/morphe-patches/releases). Look for the `patches-<version>.mpp` asset attached to the newest tag.
3. **Open Morphe Manager** on your device → tap the `+` in the top-right → **Import Patches** → select the `.mpp` you just downloaded.
4. **Import the APK** → **Select patches to enable** — see the per-app section below for which patches to toggle and their side-effects. Some patches (e.g. Truecaller's "remove device check") are required for the others in that app to work; others (e.g. Truecaller's "unlock premium") are opt-in.
5. **Apply patches** → **Sign + install**. Morphe Manager signs with its own debug key, so you cannot install over the Play Store version — uninstall the Play Store version first (backup your data if the app supports export).

---

## App coverage table

Legend for the **Status** column:

- **WORKING** — patch applies cleanly against the currently-listed version and produces a running app.
- **STUB** — patch scaffold + name are in the repo but fingerprints are pending (patch will no-op or fail-soft; safe to leave enabled while we wait for upstream).
- **BROKEN** — patch applies but the resulting app crashes on launch or a critical screen. Do NOT enable.

### Pixel-only apps (Poco F7 unlock)

| App | Package | Patches available | Notes | Status |
|---|---|---|---|---|
| Pixel Recorder | `com.google.android.apps.recorder` | Bypass device check | Transcription server-side — works on Poco once installed | BROKEN — crash on launch, see [#XX](https://github.com/chirag127/morphe-patches/issues) |
| Pixel Weather | `com.google.android.apps.weather` | Bypass device check | Uses Google's weather backend | BROKEN — crash on launch, see [#XX](https://github.com/chirag127/morphe-patches/issues) |
| Pixel Screenshots | `com.google.android.apps.pixel.screenshots` | Bypass device check | On-device Gemini OCR | BROKEN — crash on launch, see [#XX](https://github.com/chirag127/morphe-patches/issues) |
| Pixel Studio | `com.google.android.apps.pixel.creativeassistant` | Bypass device check | On-device image gen | BROKEN — crash on launch, see [#XX](https://github.com/chirag127/morphe-patches/issues) |

These four all hit the same root cause: the Android 15 QPR2 build shipped a hardware attestation check that the current bypass shim doesn't defeat. Fingerprints for the new check need re-authoring against the latest Pixel APKs.

### Truecaller (flagship)

| App | Package | Patches available | Notes | Status |
|---|---|---|---|---|
| Truecaller | `com.truecaller` | 21 atomic patches | Device-detection removal, ad removal, analytics kill, premium unlock, gold unlock, family-plan unlock, spam-badge always-on, caller-ID override, dialer skin unlock, backup unlock, WhoSearchedForMe unlock, contact-request unlock, ghost-call unlock, incognito unlock | WORKING — primary flagship patch |

The Truecaller stack is the most-tested surface in this bundle. All 21 patches are atomic — enable individually or all-at-once. Recommended set for daily use: `remove-device-check` + `remove-ads` + `unlock-premium` + `spam-badge-always-on`. The `unlock-gold` and `unlock-family` patches are on-by-default in the release build.

### News & code

| App | Package | Patches available | Notes | Status |
|---|---|---|---|---|
| Google News | `com.google.android.apps.magazines` | Ad-strip | Removes home-feed sponsored cards + article-body inline ads | STUB — fingerprints pending upstream |
| GitHub Android | `com.github.android` | AMOLED palette override, comment-surface theme | Palette works; comment-surface fingerprint still drifting per version | WORKING (palette) / STUB (comment surface) |

### YouTube on-top (Piko/Morphe base)

These patches layer ON TOP of the existing YouTube patches from Piko or the Morphe base bundle. Do not enable them standalone — enable Piko's YouTube patches first, then these.

| App | Package | Patches available | Notes | Status |
|---|---|---|---|---|
| YouTube | `com.google.android.youtube` | Like/dislike → auto-next | Long-press either to skip to next video | STUB — requesting upstream at Piko / Morphe / Rushi / Hoodle |
| YouTube | `com.google.android.youtube` | Playlist download | Bulk-download a whole playlist via YT's own offline API | STUB — requesting upstream |
| YouTube | `com.google.android.youtube` | Playlist total time | Sum of video durations in playlist header | STUB — requesting upstream |

### Social

| App | Package | Patches available | Notes | Status |
|---|---|---|---|---|
| Twitter / X | `com.twitter.android` | Piko-derived (ad-strip, chronological feed, undo tweet, hide promoted, custom download folder) | Layered on top of Piko | STUB |
| Instagram | `com.instagram.android` | Brosssh-derived (distraction-free feed, hide stories, hide reels, download button, ghost mode) | Layered on top of Brosssh | STUB |
| TikTok | `com.zhiliaoapp.musically` | icysymmetra-derived (region unlock, ad-strip, download unwatermarked, timer removal) | Layered on top of icysymmetra | STUB |
| Reddit | `com.reddit.frontpage` | Adobo-derived (ad-strip, old-Reddit UX polish, chat-tab hide, NSFW unlock) | Layered on top of Adobo | STUB |
| Snapchat | `com.snapchat.android` | Ghost-mode, save-to-camera-roll, streak-preserve | | STUB |
| WhatsApp | `com.whatsapp` | Anti-delete, read-receipt hide, view-once bypass | | STUB |
| LinkedIn | `com.linkedin.android` | Ad-strip, promoted-post hide, disable read receipts | | STUB |
| Threads | `com.instagram.barcelona` | Ad-strip, chronological feed | | STUB |
| Facebook | `com.facebook.katana` | Ad-strip, sponsored hide, story-view hide | | STUB |

### Media

| App | Package | Patches available | Notes | Status |
|---|---|---|---|---|
| Spotify | `com.spotify.music` | Ad-strip, unlimited skips, seek unlock, download unlock | | STUB |
| Prime Video | `com.amazon.avod.thirdpartyclient` | Ad-strip, X-Ray always on, subtitle override | | STUB |
| MIUI Music | `com.miui.player` | Ad-strip, disable analytics | | STUB |
| MIUI Weather | `com.miui.weather2` | Ad-strip, remove sponsored cards | | STUB |

### Productivity

| App | Package | Patches available | Notes | Status |
|---|---|---|---|---|
| Notion | `notion.id` | Unlock unlimited blocks, offline mode | | STUB |
| Todoist | `com.todoist` | Premium unlock (labels, filters, reminders) | | STUB |
| Any.do | `com.anydo` | Premium unlock (recurring tasks, subtasks) | | STUB |
| Moon+ Reader Pro | `com.flyersoft.moonreaderp` | Unlock Pro features on free version | | STUB |

### Shopping & travel

| App | Package | Patches available | Notes | Status |
|---|---|---|---|---|
| Amazon Shopping | `in.amazon.mShop.android.shopping` | Ad-strip, sponsored-strip, hide promoted-search | | STUB |
| Google Maps | `com.google.android.apps.maps` | Ad-strip on search, hide sponsored pins, hide promoted businesses | | STUB |

### Language learning

| App | Package | Patches available | Notes | Status |
|---|---|---|---|---|
| Duolingo | `com.duolingo` | Bufferk-derived Super Max unlock, streak spoof, ad-strip, hearts unlock | Layered on top of Bufferk | STUB |

---

## Known broken

The Pixel-app crashes are the only currently-tracked hard breakages. Filing three GitHub issues to track (issue numbers to be added once filed):

- Pixel Recorder crash on launch — [#XX](https://github.com/chirag127/morphe-patches/issues)
- Pixel Weather crash on launch — [#XX](https://github.com/chirag127/morphe-patches/issues)
- Pixel Screenshots + Studio crash on launch (shared root cause) — [#XX](https://github.com/chirag127/morphe-patches/issues)

Root cause hypothesis: Android 15 QPR2 hardware attestation. The bypass patch removes the string-level device-model check but the new check reads from `KeyStore` attestation records that need a different bypass strategy. Contributions welcome.

---

## Fork strategy

This bundle subtrees 10 active upstream bundles under `upstream/` and layers our own patches on top under `on-top/`. A daily cron auto-pulls each `upstream/<name>` subtree from its source repository's `main` branch.

**Conflict resolution.** When two upstream bundles patch the same fingerprint (e.g. Rushi's YouTube ad-strip vs Hoodle's YouTube ad-strip, both live), an explicit precedence table in `patches/src/main/kotlin/app/chirag127/morphe/patches/PatchPrecedence.kt` picks the winner per package. On-top patches ALWAYS win over upstream when the same fingerprint is patched twice.

**Subtreed bundles** (activity-ranked by patch count as of last audit):

1. **Rushi** — [rushiranpise/morphe-patches](https://github.com/rushiranpise/morphe-patches) — 89 apps, primary content source
2. **Hoodle** — [hoo-dles/morphe-patches](https://github.com/hoo-dles/morphe-patches) — 52 apps
3. **Hooman** — [arandomhooman/hoomans-morphe-patches](https://github.com/arandomhooman/hoomans-morphe-patches) — 41 apps
4. **kondratjev** — [kondratjev/morphe-patches](https://github.com/kondratjev/morphe-patches) — 14 apps (health / fitness cluster)
5. **androidtv-ajstrick** — [ajstrick81/morphe-androidtv-patches](https://github.com/ajstrick81/morphe-androidtv-patches) — 12 apps (Android TV cluster)
6. **MorpheApp** — [MorpheApp/morphe-patches](https://github.com/MorpheApp/morphe-patches) — 7 apps (base framework + reference impls)
7. **Bufferk** — [bufferk/morphe-patches](https://github.com/bufferk/morphe-patches) — 7 apps (Duolingo lineage)
8. **Adobo** — [jkennethcarino/adobo](https://github.com/jkennethcarino/adobo) — 7 apps (Reddit UX polish specialist)
9. **Brosssh** — [brosssh/morphe-patches](https://github.com/brosssh/morphe-patches) — 6 apps (Instagram distraction-free specialist)
10. **Piko** — [crimera/piko](https://github.com/crimera/piko) — 4 apps (Twitter/X specialist)

**Why subtree instead of submodule.** Subtrees flatten into our history so a single `.mpp` build works end-to-end without recursive checkouts. Submodules would force every consumer of the `.mpp` to also `--recurse-submodules` when cloning the repo, which is a foot-gun.

**Trust boundary.** We do not verify signatures on upstream commits. If any upstream repo is compromised, we absorb it on next auto-sync. Consider this before running the built `.mpp` against sensitive apps (banking, password managers). None of the currently-patched apps handle high-sensitivity data.

---

## Building from source

**Prerequisites**

- **JDK 21** — the Morphe Gradle plugin requires exactly 21 (not 17, not 22). On Windows, [Temurin 21](https://adoptium.net/temurin/releases/?version=21) works. Verify with `java -version` → should read `openjdk version "21.x.x"`.
- **Gradle 8+** — the wrapper (`./gradlew`) ships with the correct version; you don't need a system Gradle.
- **GITHUB_TOKEN env var** with `read:packages` scope — the Morphe Gradle plugin lives on GitHub Packages and requires auth even for public reads. Create at [github.com/settings/tokens](https://github.com/settings/tokens) → generate classic token → check `read:packages` → set `GITHUB_TOKEN=ghp_...` in your shell environment.
- **pnpm** — used for the release-tooling scripts (not the Gradle build itself). Install with `npm i -g pnpm`.

**Build steps**

```sh
git clone https://github.com/chirag127/morphe-patches.git
cd morphe-patches
pnpm install
./gradlew :patches:buildAndroid
```

Output lands at `patches/build/libs/patches-<version>.mpp`. Copy this to your device (ADB, Syncthing, Nearby Share, whatever) and import via Morphe Manager → Import Patches.

**Rebuilding after changing a patch.** `./gradlew :patches:buildAndroid` again — Gradle handles incremental compilation. First build takes ~2 minutes on cold cache; subsequent builds are ~15 seconds.

**Running tests** — `./gradlew test`. Test coverage is spotty; the patch itself is tested in production by applying against real APKs.

---

## Contributing

**New patch shape.** Write an app-specific fingerprint + patch in `patches/src/main/kotlin/app/chirag127/morphe/patches/targets/<app>/<Patch>.kt`.

Template:

```kotlin
@Suppress("unused")
val myFeaturePatch = bytecodePatch(
    name = "My feature",
    description = "One-sentence description shown in Morphe Manager UI.",
) {
    compatibleWith("com.example.app"("1.2.3"))

    val fingerprint by fingerprint {
        // fingerprint definition
    }

    execute {
        try {
            val method = fingerprint.method
            // patch bytecode
        } catch (e: PatchException) {
            // fail-soft — log and continue, do not hard-fail the whole bundle
            println("[my-feature] skip: ${e.message}")
        }
    }
}
```

**Fail-soft rule.** Every patch wraps its bytecode-mutation section in `try/catch(PatchException)` so a fingerprint drift on one app doesn't hard-fail the entire `.mpp` build. This is the pattern the Truecaller patches use — see `patches/src/main/kotlin/app/chirag127/morphe/patches/targets/truecaller/` for the reference shape.

**Fingerprints.** Prefer opcode-list fingerprints over string-literal fingerprints when possible — string literals get obfuscated across versions and break your patch on every app update; opcode lists are more stable.

**Feature requests.** Parity FRs (features requested identically across multiple upstream bundles) are filed cross-bundle. The canonical body lives at [rushiranpise/morphe-patches#182](https://github.com/rushiranpise/morphe-patches/issues/182). If you want a new feature, file it upstream first — this repo prefers to consume upstream patches rather than fork them.

**Pull requests.** Prefixed conventional commits: `feat(<app>): add <patch>`, `fix(<app>): repair <patch> against <version>`, `chore(subtree): sync <upstream>`. One patch per PR. Include the target APK version + a before/after screenshot when the patch has visible UI effect.

---

## License

**GPL-3.0-or-later.** See [LICENSE](./LICENSE) for the full text and [NOTICE](./NOTICE) for third-party attributions.

The GPL-3.0 §7c fork-name-restriction obligation is met by the ReVanced → Morphe rename applied by the base Morphe bundle. Downstream forks of THIS repo must retain the "Morphe" name lineage; a further rename would require another §7c-compliant relabelling.

**No warranty.** Patched APKs may violate the target app's Terms of Service and may result in your account being flagged, throttled, or banned. Use on throwaway accounts first. We accept no liability for account loss, device damage, or data loss.

---

## Credits

Every upstream bundle we subtree, in reverse-dependency order:

- **[MorpheApp/morphe-patches](https://github.com/MorpheApp/morphe-patches)** — base framework, Gradle plugin, reference implementations. Everything here depends on this.
- **[ReVanced/revanced-patches](https://github.com/ReVanced/revanced-patches)** — lineage ancestor of the entire Morphe ecosystem. Morphe is a rebrand fork of ReVanced-patches to comply with the ReVanced trademark posture.
- **[rushiranpise/morphe-patches](https://github.com/rushiranpise/morphe-patches)** — 89 apps, primary content source. Most of the useful non-YouTube patches in this bundle come from Rushi.
- **[hoo-dles/morphe-patches](https://github.com/hoo-dles/morphe-patches)** — 52 apps. Overlaps Rushi on many surfaces; Hoodle is the secondary source when Rushi is behind on a version.
- **[arandomhooman/hoomans-morphe-patches](https://github.com/arandomhooman/hoomans-morphe-patches)** — 41 apps. Independent maintainer, unique coverage of some niche apps.
- **[kondratjev/morphe-patches](https://github.com/kondratjev/morphe-patches)** — 14 apps, health / fitness cluster (Google Fit, Samsung Health, Strava).
- **[ajstrick81/morphe-androidtv-patches](https://github.com/ajstrick81/morphe-androidtv-patches)** — 12 apps, Android TV cluster (Prime Video TV, YouTube TV, Google TV).
- **[bufferk/morphe-patches](https://github.com/bufferk/morphe-patches)** — 7 apps, Duolingo lineage. Super Max unlock originates here.
- **[jkennethcarino/adobo](https://github.com/jkennethcarino/adobo)** — 7 apps, Reddit UX polish specialist. Old-Reddit-style patches for the modern Reddit app.
- **[brosssh/morphe-patches](https://github.com/brosssh/morphe-patches)** — 6 apps, Instagram distraction-free specialist. Hide-reels, hide-stories, ghost-mode.
- **[crimera/piko](https://github.com/crimera/piko)** — 4 apps, Twitter/X specialist. The gold-standard reference for Twitter patches; our YouTube on-top patches use Piko's plugin architecture.
- **[binarymend/morphe-patches](https://github.com/binarymend/morphe-patches)** — generalist maintainer, wide but shallow coverage. Fingerprint-diversity source.
- **[andronedev/morphe-patches](https://github.com/andronedev/morphe-patches)** — experimental patches, high-churn. Watched but not subtreed by default.
- **[anddea/revanced-patches](https://github.com/anddea/revanced-patches)** — RVX active fork. Alternative lineage; watched for patches that ReVanced-mainline drops.

Thanks to every maintainer above for the work they do in the open. Every patch in this bundle rests on the shoulders of theirs.

---

## Contact

Issues, feature requests, or a request for access to the private releases repo: file a [GitHub issue](https://github.com/chirag127/morphe-patches/issues). Please include your Android version, device model, target app + version, and (for bug reports) a Morphe Manager log or `logcat` output around the crash.

No email / no Telegram / no Discord — issues on this repo are the only support surface.
