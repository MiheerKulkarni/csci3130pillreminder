package com.validator.mymeds;

import com.validator.mymeds.model.Medication;

import org.junit.Test;

import static org.junit.Assert.*;

public class AddTimeTest {


    @Test
    public void formatTimeHour(){
        String time = "2:30";
        String expectedTime = "02:30";

        Medication medication = new Medication();
        medication.setDosageTime(time);

        assertTrue(medication.getDosageTime().equals(expectedTime));

    }
    @Test
    public void formatTimeMinute(){
        String time = "12:0";
        String expectedTime = "12:00";

        Medication medication = new Medication();
        medication.setDosageTime(time);

        assertTrue(medication.getDosageTime().equals(expectedTime));

    }
}
