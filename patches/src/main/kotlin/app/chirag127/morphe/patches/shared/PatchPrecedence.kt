package app.chirag127.morphe.patches.shared

/**
 * Patch precedence registry.
 *
 * Purpose: when multiple on-top variants (Piko / Brosssh / IcySymmetra / Adobo /
 * BufferK / Morphe-YouTube) claim the same target package + fingerprint, this
 * map decides which patch wins. The key is a stable identifier (e.g. the
 * qualified patch class name or its declared `name`), the value is the
 * winning override chain.
 *
 * Empty by default — conflicts are resolved lazily as they surface during
 * matrix builds. Populate with entries like:
 *
 *   "youtube.hide-ads" to listOf("piko", "morphe-youtube")
 *
 * meaning: prefer Piko's hide-ads; fall back to Morphe's own hide-ads if
 * Piko's fingerprint fails at build time.
 */
object PatchPrecedence {
    val overrides: Map<String, List<String>> = emptyMap()
}
