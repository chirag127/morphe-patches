# 👋🧩 Morphe Patches template

Template repository for Morphe Patches.

## ❓ About

Patches for apps I like.

TODO: Update this about section with a brief introduction/summary about this repo and what it offers.

## 🩹 Patches list

<!-- PATCHES_START EXPANDED -->
> **[v1.3.1](https://github.com/chirag127/morphe-patches/releases/tag/v1.3.1)**&nbsp;&nbsp;•&nbsp;&nbsp;`main`&nbsp;&nbsp;•&nbsp;&nbsp;24 patches total
<details open>
<summary>📦 com.google.android.apps.pixel.creativeassistant&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Pixel Studio unlock (Poco F7)](#pixel-studio-unlock-poco-f7) | Bypass in-APK Pixel-only gates on Poco F7. Uses narrowed fingerprints to avoid lifecycle-callback match. Server-side Play Integrity NOT bypassed. |  |

</details>

<details open>
<summary>📦 com.truecaller&nbsp;&nbsp;•&nbsp;&nbsp;20 patches</summary>
<br>

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Truecaller — GMS sign-in bypass](#truecaller-gms-sign-in-bypass) | Forces SMS-based OTP verification instead of GMS SmsRetriever, fixing sign-in on re-signed APKs. |  |
| [Truecaller — Google Drive sync fix (STUB)](#truecaller-google-drive-sync-fix-stub) | STUB (requires MicroG-RE): restore Google Drive backup / sync on re-signed APKs. Fingerprints not yet discovered. |  |
| [Truecaller — Premium unlock](#truecaller-premium-unlock) | Unlocks premium features (GOLD tier + all features + block server revocation + block expired-notification worker). |  |
| [Truecaller — all patches](#truecaller-all-patches) | Enables every atomic Truecaller patch above. Toggle individual patches to opt out. |  |
| [Truecaller — disable contact upload (STUB)](#truecaller-disable-contact-upload-stub) | STUB: prevent local contact book from being uploaded to Truecaller servers. Fingerprints not yet discovered. |  |
| [Truecaller — disable telemetry](#truecaller-disable-telemetry) | Disables AppStartTracker and other telemetry points. |  |
| [Truecaller — disable update check](#truecaller-disable-update-check) | Disables the in-app update check / update nag. |  |
| [Truecaller — hide Assistant tab](#truecaller-hide-assistant-tab) | Hides the Assistant tab from the bottom navigation bar. |  |
| [Truecaller — hide Family Protection button](#truecaller-hide-family-protection-button) | Hides the Family Protection button from the bottom navigation bar. |  |
| [Truecaller — hide Get Premium banners](#truecaller-hide-get-premium-banners) | Immediately finishes the full-screen paywall activity so promotional banners never render. |  |
| [Truecaller — hide Premium from settings](#truecaller-hide-premium-from-settings) | Hides the Premium options from the settings and user details pages. |  |
| [Truecaller — hide Premium tab](#truecaller-hide-premium-tab) | Hides the Premium tab from the bottom navigation bar. |  |
| [Truecaller — hide Premium upgrade UI](#truecaller-hide-premium-upgrade-ui) | Kills paywall activity onCreate as a safety-net when Premium unlock is enabled. |  |
| [Truecaller — hide Scams tab](#truecaller-hide-scams-tab) | Hides the Scams tab from the bottom navigation bar. |  |
| [Truecaller — mock Premium badge only](#truecaller-mock-premium-badge-only) | Paints the Premium badge in UI without unlocking features (cosmetic-only variant of Premium unlock). |  |
| [Truecaller — region unlock (STUB)](#truecaller-region-unlock-stub) | STUB: unlock region-locked features. Fingerprints not yet discovered. |  |
| [Truecaller — remove Premium UI (nav drawer + tab)](#truecaller-remove-premium-ui-nav-drawer-tab) | Hides bottom-nav Premium tab + PremiumNavDrawerItemView (sets GONE in onAttachedToWindow). |  |
| [Truecaller — remove ads](#truecaller-remove-ads) | No-ops after-call ad update sites (Lcom/truecaller/acs/ui/baz;->Rh and Ltw1/f;->Th). |  |
| [Truecaller — strip outbound SMS watermark (STUB)](#truecaller-strip-outbound-sms-watermark-stub) | STUB: remove the "Sent via Truecaller" trailer from outbound SMS. Fingerprints not yet discovered. |  |
| [Truecaller — strip third-party analytics + ad SDKs](#truecaller-strip-third-party-analytics-ad-sdks) | No-ops third-party telemetry, ad-network, and tracking SDK init calls (Clarity, CleverTap, AppsFlyer, Moloco, HMS, Freshchat, InMobi, Appnext, Vungle, Fyber, Mintegral, IronSource, HyBid, MLKit, Presence stream). |  |

</details>

<details open>
<summary>🌐 Universal&nbsp;&nbsp;•&nbsp;&nbsp;3 patches</summary>
<br>

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Force hasSystemFeature true (narrow)](#force-hassystemfeature-true-narrow) | Return true from public static boolean methods calling PackageManager.hasSystemFeature with a String arg. Narrow-scoped to avoid lifecycle callbacks. |  |
| [Spoof Pixel model check (narrow)](#spoof-pixel-model-check-narrow) | In-APK utility methods checking Pixel model return true. Narrow-scoped to avoid lifecycle callbacks. |  |
| [Strip root detection (narrow)](#strip-root-detection-narrow) | Force public static boolean methods referencing "Magisk" to return false. Narrow-scoped. |  |

</details>

<!-- PATCHES_END -->

#### How to use these patches

Click here to add these patches to Morphe: https://morphe.software/add-source?github=chirag127/morphe-patches

Or manually add this repository url as a patch source in Morphe: https://github.com/chirag127/morphe-patches

### 🛠️ Building

To build UserXYZ Patches,
you can follow the [Morphe documentation](https://github.com/MorpheApp/morphe-documentation).

## 📜 License

UserXYZ Patches are licensed under the [GNU General Public License v3.0](LICENSE)
