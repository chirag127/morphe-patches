# 👋🧩 Morphe Patches template

Template repository for Morphe Patches.

## ❓ About

Patches for apps I like.

TODO: Update this about section with a brief introduction/summary about this repo and what it offers.

## 🩹 Patches list

<!-- PATCHES_START EXPANDED -->
> **[v1.2.0](https://github.com/chirag127/morphe-patches/releases/tag/v1.2.0)**&nbsp;&nbsp;•&nbsp;&nbsp;`main`&nbsp;&nbsp;•&nbsp;&nbsp;4 patches total
<details open>
<summary>📦 com.google.android.apps.pixel.creativeassistant&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Pixel Studio unlock (Poco F7)](#pixel-studio-unlock-poco-f7) | Bypass in-APK Pixel-only gates on Poco F7. Uses narrowed fingerprints to avoid lifecycle-callback match. Server-side Play Integrity NOT bypassed. |  |

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
