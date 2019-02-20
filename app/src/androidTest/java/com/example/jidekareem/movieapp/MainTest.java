package com.example.jidekareem.movieapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.jidekareem.movieapp.mainList.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainTest {



    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);


    @Test
    public void TestAutoComplete(){
        onView(withId(R.id.main_recycle)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

}
