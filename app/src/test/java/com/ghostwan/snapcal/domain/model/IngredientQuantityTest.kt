package com.ghostwan.snapcal.domain.model

import org.junit.Assert.*
import org.junit.Test

class IngredientQuantityTest {
    @Test fun russianDecimalAndEquivalentGramUnits() {
        assertEquals(2.0, IngredientQuantity.ratio("100 г", "200g")!!, 0.00001)
        assertEquals(0.5, IngredientQuantity.ratio("150 г", "75,0 гр.")!!, 0.00001)
        assertTrue(IngredientQuantity.isValid("0,5 кг"))
    }

    @Test fun invalidWeightsDoNotCorruptCalories() {
        listOf("", "-100 г", "0 г", "NaN", "Infinity", "100001 г", "1.2.3 г").forEach {
            assertFalse(it, IngredientQuantity.isValid(it))
        }
    }

    @Test fun differentUnitsAndAmbiguousQuantitiesRequireManualCorrection() {
        assertNull(IngredientQuantity.ratio("150 г", "0,2 кг"))
        assertNull(IngredientQuantity.ratio("150 г", "200 мл"))
        assertNull(IngredientQuantity.ratio("2 ложки (30 мл)", "60 мл"))
        assertNull(IngredientQuantity.ratio("100 г", ""))
    }
}
