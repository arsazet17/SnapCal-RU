package com.ghostwan.snapcal.domain.model

/** Only scale quantities with an unambiguous number and the same unit. */
object IngredientQuantity {
    private val pattern = Regex("^\\s*([0-9]+(?:[.,][0-9]+)?)\\s*([\\p{L}.]*)\\s*$")

    private fun parse(text: String): Pair<Double, String>? {
        val match = pattern.matchEntire(text) ?: return null
        val amount = match.groupValues[1].replace(',', '.').toDoubleOrNull() ?: return null
        if (!amount.isFinite() || amount <= 0.0 || amount > 100000.0) return null
        val unit = when (val raw = match.groupValues[2].lowercase()) {
            "g", "гр", "гр.", "г" -> "г"
            "kg", "кг" -> "кг"
            "ml", "мл" -> "мл"
            "l", "л" -> "л"
            "шт", "шт." -> "шт"
            else -> raw
        }
        return amount to unit
    }

    fun isValid(text: String): Boolean = parse(text) != null

    fun ratio(original: String, updated: String): Double? {
        val old = parse(original) ?: return null
        val new = parse(updated) ?: return null
        return if (old.second == new.second) new.first / old.first else null
    }
}
