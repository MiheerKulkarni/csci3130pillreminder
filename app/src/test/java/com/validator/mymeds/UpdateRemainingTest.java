package com.validator.mymeds;
import com.validator.mymeds.model.Medication;

import org.junit.Test;
import static org.junit.Assert.*;

public class UpdateRemainingTest {
    @Test
    public void isNumericTest() {
        Medication med = new Medication();
        med.setRemainingDoses("10");
        assertTrue(med.isNumeric(med.getRemainingDoses()));

        med.setRemainingDoses("Hello World");
        assertFalse(med.isNumeric(med.getRemainingDoses()));
    }

    @Test
    public void decrementDoseTest() {
        Medication med = new Medication();
        med.setRemainingDoses("10");
        assertEquals(med.decrementDose(med.getRemainingDoses()), "9");

        med.setRemainingDoses("9");
        assertEquals(med.decrementDose(med.getRemainingDoses()), "8");

        med.setRemainingDoses("1");
        assertEquals(med.decrementDose(med.getRemainingDoses()), "0");

        med.setRemainingDoses("0");
        assertEquals(med.decrementDose(med.getRemainingDoses()), null);

        med.setRemainingDoses("Hello World");
        assertEquals(med.decrementDose(med.getRemainingDoses()), null);
    }

}
