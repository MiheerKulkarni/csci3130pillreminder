package com.validator.mymeds;
import org.junit.Test;
import static org.junit.Assert.*;
public class testSendNotification {
    @Test
    public void testTime() {
        String dosageTime = "17:03";
        String currentTIme = SendNotification.alertuser();
        if (currentTIme.equals(dosageTime)) {
            assertTrue("dosageTIme = CurrentTIme", dosageTime.equals(currentTIme));
        } else {
            assertTrue("dosagetime not equal current time", dosageTime != currentTIme);
        }

    }
}
