# 👋🧩 Morphe Patches template

Template repository for Morphe Patches.

## ❓ About

Patches for apps I like.

TODO: Update this about section with a brief introduction/summary about this repo and what it offers.

## 🩹 Patches list

<!-- PATCHES_START EXPANDED -->
> **[v1.1.0](https://github.com/chirag127/morphe-patches/releases/tag/v1.1.0)**&nbsp;&nbsp;•&nbsp;&nbsp;`main`&nbsp;&nbsp;•&nbsp;&nbsp;4 patches total
<details open>
<summary>📦 com.google.android.apps.pixel.creativeassistant&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Pixel Studio unlock (Poco F7) — EXPERIMENTAL, off by default](#pixel-studio-unlock-poco-f7-experimental-off-by-default) | ⚠️ Known to cause SuperNotCalledException on MainActivity in v1.0.x. Off-by-default in v1.1.0 pending fingerprint rewrite from disassembled APK. Do not enable until v1.2+. |  |

</details>

<details open>
<summary>🌐 Universal&nbsp;&nbsp;•&nbsp;&nbsp;3 patches</summary>
<br>

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Force hasSystemFeature true](#force-hassystemfeature-true) | Return true for Pixel-experience feature queries (fail-soft on fingerprint miss). |  |
| [Spoof Pixel build fingerprint check](#spoof-pixel-build-fingerprint-check) | In-APK Pixel-model checks return true (fail-soft on fingerprint miss). |  |
| [Strip root detection](#strip-root-detection) | Force common root-detection methods to return false (fail-soft on fingerprint miss). |  |

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
