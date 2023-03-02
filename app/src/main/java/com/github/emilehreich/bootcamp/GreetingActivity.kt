package com.github.emilehreich.bootcamp

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

class GreetingActivity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    lateinit var drawerLayout : DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greeting)

        // greet with name entered in MainActivity
        val greet = findViewById<TextView>(R.id.greetingName)
        val name = intent.getStringExtra("name")

        greet.text = "Hello $name"


        drawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.navigationView)

        // displays the "hamburger menu" button on the top left
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // fragments that will be displayed when clicking on respective options in Navigation Drawer
        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()
        val musicPlaylistFragment = MusicPlaylistFragment()

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> replaceFragment(homeFragment)
                R.id.nav_profile -> replaceFragment(profileFragment)
                R.id.nav_music_playlist -> replaceFragment(musicPlaylistFragment)
            }
            true
        }

    }

    /**
     * replace current displayed Fragment with the one passed as an argument
     */
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flMainFragments, fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
    }

    // this function is needed in order for the ActionBarDrawerToggle button to display menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}