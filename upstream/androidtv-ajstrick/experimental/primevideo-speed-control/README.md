# Prime Video ATV — playback-speed control (SHELVED)

**Status:** shelved 2026-07-01. Not part of the build. Kept for reference and
possible reactivation against a future Prime Video build.

## What it was
An experimental patch to add remote fast-forward / rewind speed cycling
(1x→1.5x→2x→4x→8x→16x) to the ATV player, intended to blow through ad segments
that survive the SkipAds + DNS layers. It hooks the media3 `ExoPlayer` the app
builds inside `MediaPipelineBackendEngine` and calls `setPlaybackSpeed()`.

Files:
- `EnableSpeedPatch.kt` — bytecode patch (3 hooks: capture player, clear player, intercept onKey)
- `Fingerprints.kt` — fingerprints for `MediaPipelineBackendEngine` init/release + `IgniteRenderer$EventHandler.onKey`
- `SpeedControlPatch.java` — extension (the actual speed logic)

## Why it was shelved (tested on-device 2026-07-01, Onn 4K Plus, app v6.23.23)
The patch compiled, applied (all fingerprints resolved), the class survived R8,
and the app ran stably — but the feature is **inert on this build**:

1. **The native engine doesn't support playback speed.** media3's
   `ExoPlayer.setPlaybackSpeed()` is bridged to a native MPB-v3 property. The
   app's *own* attempts to set it fail at runtime:
   - `setProperty("playbackSpeed","1")` → `_av_mpb_v3_instance_set_property failed with error code: 50040`
   - `get_property(playbackSpeed)` → `property not recognized` (logged continuously)
   - The engine's advertised MPBv3 capabilities list contains no speed/trick-play entry.

   So even a perfectly wired hook forwards speed into a native call the engine
   rejects. This is the same sealed native stack that caps ad suppression.

2. **Hook 1 also didn't fire** (`setCurrentPlayer` never logged even though
   `Initialising ExoPlayer` did). The `player`-field/register match in
   `EnableSpeedPatch.kt` likely needs correcting — but this is moot until (1)
   is solved.

## Reactivation checklist (if a future build changes the pipeline)
1. Move the 3 files back:
   - `EnableSpeedPatch.kt`, `Fingerprints.kt` → `patches/src/main/kotlin/ajstrick81/morphe/patches/primevideo/speed/`
   - `SpeedControlPatch.java` → `extensions/extension/src/main/java/ajstrick81/morphe/extension/primevideo/speed/`
2. Re-add the R8 `-keep` rule for `SpeedControlPatch` (setCurrentPlayer /
   clearCurrentPlayer / onKeyEvent) to `extensions/proguard-rules.pro`.
3. First confirm the native layer accepts `playbackSpeed` (logcat: no
   `50040` / `property not recognized`), otherwise it's still a dead end.
4. Fix Hook 1 so `setCurrentPlayer` actually fires (verify via the
   `PVSpeedControl: captured player=` log on playback start).

See project memory `primevideo-native-speed-deadend` and the
`Ad_Suppression_Methodology` doc for the full findings.
