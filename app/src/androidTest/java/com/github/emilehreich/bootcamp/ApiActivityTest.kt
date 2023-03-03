//package com.github.emilehreich.bootcamp
//
//import android.content.Intent
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.core.app.launchActivity
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.action.ViewActions.click
//import androidx.test.espresso.assertion.ViewAssertions.matches
//import androidx.test.espresso.matcher.ViewMatchers.*
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.google.gson.Gson
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ActivityComponent
//import dagger.hilt.android.testing.CustomTestApplication
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import okhttp3.mockwebserver.MockResponse
//import okhttp3.mockwebserver.MockWebServer
//import org.junit.After
//import org.junit.Assert.*
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import javax.inject.Inject
//
//
////@RunWith(AndroidJUnit4::class)
////@HiltAndroidTest
////class ApiActivityTest {
////
////    @Rule
////    @JvmField
////    var activityScenarioRule = ActivityScenarioRule(ApiActivity::class.java)
////
////
////}
//
//
//@RunWith(AndroidJUnit4::class)
//@HiltAndroidTest
//class ApiActivityTest {
//
//    @get:Rule(order = 0)
//    var hiltRule = HiltAndroidRule(this)
//
//    @Module
//    @InstallIn(ActivityComponent::class)
//    object BoredApiObjectTest {
//
//        @Provides
//        fun providesActivity() : BoredApi{
//            return Retrofit.Builder()
//                .baseUrl("http://127.0.0.1:8080")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build().create(BoredApi::class.java)
//        }
//    }
//
//
//    private var mockWebServer: MockWebServer = MockWebServer()
//
//    @Before
//    fun setUp() {
//        hiltRule.inject()
//        mockWebServer.start(8080)
//    }
//
//    @After
//    fun tearDown() {
//        mockWebServer.shutdown()
//    }
//    @Test
//    fun testActivityRequest() {
//        // Set up a mocked response
//        val mockedResponse = BoredActivity("Test Activity", "test", 1, 0.5)
//        val mockedResponseJson = Gson().toJson(mockedResponse)
//        mockWebServer.enqueue(
//            MockResponse()
//            .setResponseCode(200)
//            .setBody(mockedResponseJson))
//
//        // Launch the ApiActivity
//        val intent = Intent(ApplicationProvider.getApplicationContext(), ApiActivity::class.java)
//        val scenario = launchActivity<ApiActivity>(intent)
//
//        // Wait for the view to be displayed
//        onView(withId(R.id.Activity)).check(matches(isDisplayed()))
//
//        // Perform the activity request by clicking the button
//        onView(withId(R.id.boredButton)).perform(click())
//
//        // Check that the mocked response is displayed in the view
//        onView(withId(R.id.Activity)).check(matches(withText("Activity: Test Activity")))
//        onView(withId(R.id.Type)).check(matches(withText("Type: test")))
//        onView(withId(R.id.Participants)).check(matches(withText("Participants: 1")))
//        onView(withId(R.id.Price)).check(matches(withText("Price: 0.5")))
//    }
//}
