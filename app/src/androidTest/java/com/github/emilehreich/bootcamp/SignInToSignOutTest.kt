package com.github.emilehreich.bootcamp

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import org.junit.Rule
import org.junit.Test
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class SignInToSignOutTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SignIn::class.java)

    @Test
    fun testSignIn() {
        Intents.init()

        // Click the sign-in button
        onView(withId(R.id.btn_sign_in)).perform(click())

        // Wait for the email selection dialog to appear
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val emailSelector = By.textContains("evgueni.rousselot@gmail.com")
        val emailSelectionDialog = device.wait(Until.hasObject(emailSelector), 5000)

        // Check if the email selection dialog is showing
        if (emailSelectionDialog != null) {
            // Click on the email address that you want to sign in with
            val email = device.findObject(emailSelector)

            if(email!=null) {
                email.click()
            }
            else{
                return
            }


            // Wait for the app to transition to the MainActivity
            Thread.sleep(5000)
        }

        // Verify that the sign-in activity is finished and the main activity is launched
        intended(hasComponent(hasClassName(MainActivity::class.java.name)))

        onView(withId(R.id.btn_sign_out)).perform(click())

        Thread.sleep(5000)

        intended(hasComponent(hasClassName(SignIn::class.java.name)))

        Intents.release()
    }



}
