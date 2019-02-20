package com.example.jidekareem.movieapp;


import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.jidekareem.movieapp.details.DetailActivity;
import com.example.jidekareem.movieapp.mainList.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class DetailTest {


    @Rule
    public ActivityTestRule<DetailActivity> activityActivityTestRule = new ActivityTestRule<DetailActivity>(DetailActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent(InstrumentationRegistry.getContext(), DetailActivity.class);
            intent.putExtra( "MOVIE_ID_EXTRA","297802" );

            return intent;
        }
    };

//297802
//    Intent intent = new Intent(this, DetailActivity.class);
//        intent.putExtra(DetailActivity.MOVIE_ID_EXTRA, mId);
//    startActivity(intent);

    @Test
    public void TestAutoComplete(){
        onView(withId(R.id.trailer_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }


}
