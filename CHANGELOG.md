## [1.6.0](https://github.com/chirag127/morphe-patches/compare/v1.5.1...v1.6.0) (2026-07-09)

### 🐛 Bug Fixes

* **constants:** add underscored aliases for authored patch imports ([4eb8630](https://github.com/chirag127/morphe-patches/commit/4eb8630374fa1be3b83ffa1ee764a77cd4406807))

### ✨ New Features

* 21-app STUB fanout partial (26 files) ([a48e524](https://github.com/chirag127/morphe-patches/commit/a48e524f7c87be388492c3b4213a1170f68de6a2))

## [1.5.1](https://github.com/chirag127/morphe-patches/compare/v1.5.0...v1.5.1) (2026-07-09)

### 🔧 Improvements

* **ci:** Gradle + APK caching + expand verify matrix to 27 targets ([860c01e](https://github.com/chirag127/morphe-patches/commit/860c01e64f23451d4468d8f1f2888a4f3774566c))

## [1.5.0](https://github.com/chirag127/morphe-patches/compare/v1.4.0...v1.5.0) (2026-07-09)

### 🐛 Bug Fixes

* **github-comment-surface:** nested /* in doc comment closed outer block ([3277965](https://github.com/chirag127/morphe-patches/commit/327796575126373fc7a649996fcc720ea7e1014f))
* **verify:** correct morphe-cli fetch — versioned asset name ([c8bbe3e](https://github.com/chirag127/morphe-patches/commit/c8bbe3ebddedc8348a857e24cecc316b73376ac1))

### ✨ New Features

* 22-app fanout — 6 subtrees + on-top + 16 fresh-RE + auto-build pipeline ([f5f01bf](https://github.com/chirag127/morphe-patches/commit/f5f01bfad9873df1703d89ffbb5c45f876032fb2))
* **github-app:** initial water-patch bundle for com.github.android ([249b743](https://github.com/chirag127/morphe-patches/commit/249b7432816650d8134e4ff24838a896e0a171ac))
* **googlenews:** initial Google News ad-strip patches (STUB, default=false) ([af9b01b](https://github.com/chirag127/morphe-patches/commit/af9b01bf3516ce738687e869d713506ac25a2f1e))

## [1.4.0](https://github.com/chirag127/morphe-patches/compare/v1.3.1...v1.4.0) (2026-07-08)

### ✨ New Features

* **pixel:** 3 Pixel target patches + matrix verify ([9355f18](https://github.com/chirag127/morphe-patches/commit/9355f188fd43f4bd20d927d2f6ea67a1ab6e5aeb))

## [1.3.1](https://github.com/chirag127/morphe-patches/compare/v1.3.0...v1.3.1) (2026-07-08)

### 🐛 Bug Fixes

* **verify:** pass GITHUB_TOKEN to Gradle build step ([638f5fb](https://github.com/chirag127/morphe-patches/commit/638f5fbf221c5dec4673a4073b491b27b68a9ac2))

## [1.3.0](https://github.com/chirag127/morphe-patches/compare/v1.2.0...v1.3.0) (2026-07-08)

### 🐛 Bug Fixes

* **truecaller:** 3 compile errors from v1.3.0 attempt ([c888352](https://github.com/chirag127/morphe-patches/commit/c88835200294497a83f53be6acf82f32a7a937ab)), closes [#1](https://github.com/chirag127/morphe-patches/issues/1)

### ✨ New Features

* **truecaller:** add Truecaller patch suite (com.truecaller) ([6537cf2](https://github.com/chirag127/morphe-patches/commit/6537cf2894a700a4ee805726ddf481a6de85287a))

## [1.2.0](https://github.com/chirag127/morphe-patches/compare/v1.1.0...v1.2.0) (2026-07-08)

### ✨ New Features

* **universal:** narrow fingerprints — PUBLIC STATIC boolean shape ([ee98f5b](https://github.com/chirag127/morphe-patches/commit/ee98f5b4b57e0ced9f80f04f44e387dfeb98237c))

## [1.1.0](https://github.com/chirag127/morphe-patches/compare/v1.0.2...v1.1.0) (2026-07-08)

### ✨ New Features

* **verify:** auto emulator smoke-test before release ([e65cb7e](https://github.com/chirag127/morphe-patches/commit/e65cb7e21a719e3f77ccc075b46d71f4d47f57a7))

## [1.0.2](https://github.com/chirag127/morphe-patches/compare/v1.0.1...v1.0.2) (2026-07-08)

### 🐛 Bug Fixes

* **pixelstudio:** disable target patch — v1.0.1 caused SuperNotCalledException ([d14f8c2](https://github.com/chirag127/morphe-patches/commit/d14f8c2c6572da9fe3d62bce76a619621cf9a117))

## [1.0.1](https://github.com/chirag127/morphe-patches/compare/v1.0.0...v1.0.1) (2026-07-08)

### 🐛 Bug Fixes

* **universal:** fail-soft fingerprint miss (catch PatchException per patch) ([fcbabd5](https://github.com/chirag127/morphe-patches/commit/fcbabd5a82023d5699e6fc3443f567012825cc37))

## 1.0.0 (2026-07-08)

### 🐛 Bug Fixes

* correct compatibleWith() overload types ([f412143](https://github.com/chirag127/morphe-patches/commit/f4121434226adf616d144812eb2c4a3a76bf253a))
* mark gradlew executable ([be6504a](https://github.com/chirag127/morphe-patches/commit/be6504aba68e191ded1db587a181f1e9bc95f6fd))

### ✨ New Features

* initial device-detection removal patches (Pixel Studio target) ([4a41ca7](https://github.com/chirag127/morphe-patches/commit/4a41ca705fcc7cb9df4a254588d6afbb73ad1d1d))
