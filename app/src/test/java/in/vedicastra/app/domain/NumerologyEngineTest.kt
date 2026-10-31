package com.vedicastra.app.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class NumerologyEngineTest {
    @Test fun reducesToSingleDigit() {
        assertEquals(6, NumerologyEngine.reduce(123))
    }
    @Test fun birthNumberUsesDayOfMonth() {
        assertEquals(5, NumerologyEngine.calculate(LocalDate.of(1984, 2, 23), "", NumberMethod.INDIAN_TRADITIONAL).birthNumber)
    }
    @Test fun destinyNumberUsesAllDateDigits() {
        assertEquals(8, NumerologyEngine.calculate(LocalDate.of(1984, 2, 2), "", NumberMethod.INDIAN_TRADITIONAL).destinyNumber)
    }
    @Test fun nameNumberIsComputedForLatinName() {
        val report = NumerologyEngine.calculate(LocalDate.of(2000, 1, 1), "ALOK", NumberMethod.CHALDEAN)
        assertEquals(4, report.nameNumber)
    }
}
