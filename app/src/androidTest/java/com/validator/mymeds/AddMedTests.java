package com.validator.mymeds;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AddMedTests {


    @Rule
    public ActivityTestRule<AddMedView> mActivityRule = new ActivityTestRule<>(AddMedView.class);


    @Test
    public void missingField(){

        String name = "MedName";
        int dosage = 3;
        String expectedText = "Missing fields: Description ";


        onView(withId(R.id.medName)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.dosageAmount)).perform(typeText(String.valueOf(dosage)), closeSoftKeyboard());
        onView(withId(R.id.addMedButton)).perform(click());
        onView(withId(R.id.requiredFields)).check(matches(withText(expectedText)));
    }
    @Test
    public void notMissingField(){

        String name = "MedName";
        int dosage = 3;
        String desciprtion = "A description";
        String expectedText = "Required Fields: Name, Dosage, Description";


        onView(withId(R.id.medName)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.dosageAmount)).perform(typeText(String.valueOf(dosage)), closeSoftKeyboard());
        onView(withId(R.id.description)).perform(typeText(desciprtion), closeSoftKeyboard());
        onView(withId(R.id.addMedButton)).perform(click());
        onView(withId(R.id.requiredFields)).check(matches(withText(expectedText)));
    }


}
