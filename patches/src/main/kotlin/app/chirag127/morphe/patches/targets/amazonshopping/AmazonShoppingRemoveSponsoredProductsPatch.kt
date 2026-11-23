/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * STUB - fingerprint discovery needed via disassemble.yml.
 *
 * Target: in.amazon.mShop.android.shopping
 * Patch: AmazonShoppingRemoveSponsoredProducts
 *
 * TODO: run disassemble.yml on this package, grep smali for the attack
 * surface, author real fingerprint, replace stub execute body.
 */

package app.chirag127.morphe.patches.targets.amazonshopping

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_AMAZON_SHOPPING

@Suppress("unused")
val amazonShoppingRemoveSponsoredProductsPatch = bytecodePatch(
    name = "AmazonShoppingRemoveSponsoredProducts (STUB)",
    description = "STUB placeholder. Real behavior authored after disassemble.yml runs on in.amazon.mShop.android.shopping.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_AMAZON_SHOPPING)

    execute {
        // TODO: implement.
    }
}
