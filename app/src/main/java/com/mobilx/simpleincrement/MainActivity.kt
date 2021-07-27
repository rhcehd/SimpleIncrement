package com.mobilx.simpleincrement

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.mobilx.simpleincrement.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val INCREMENT_LEVEL_1 = 1
        const val INCREMENT_LEVEL_2 = 2
        const val INCREMENT_LEVEL_3 = 3
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val incrementHandler = object: Handler(Looper.getMainLooper()) {
        var incrementValue = 0
        var incrementCount = 0
        var buttonPressed = false
            set(value) {
                if(value != field) {
                    field = value
                    if(!field) {
                        incrementCount = 0
                    } else {
                        Message.obtain(this, INCREMENT_LEVEL_1).sendToTarget()
                    }
                }
            }
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if(!buttonPressed) {
                return
            }
            post {
                binding.incrementText.text = "$incrementValue"
            }
            incrementValue++
            when(msg.what) {
                INCREMENT_LEVEL_1 -> {
                    if(incrementCount >= 3) {
                        postDelayed({ Message.obtain(this, INCREMENT_LEVEL_2).sendToTarget() }, 1000)
                        incrementCount = 0
                    } else {
                        postDelayed({ Message.obtain(this, INCREMENT_LEVEL_1).sendToTarget() }, 1000)
                        incrementCount++
                    }
                }
                INCREMENT_LEVEL_2 -> {
                    if(incrementCount >= 5) {
                        postDelayed({ Message.obtain(this, INCREMENT_LEVEL_3).sendToTarget() }, 500)
                        incrementCount = 0
                    } else {
                        postDelayed({ Message.obtain(this, INCREMENT_LEVEL_2).sendToTarget() }, 500)
                        incrementCount++
                    }
                }
                INCREMENT_LEVEL_3 -> {
                    postDelayed({ Message.obtain(this, INCREMENT_LEVEL_3).sendToTarget() }, 100)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)*/

        /*binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
        binding.incrementText.text = "${incrementHandler.incrementValue}"
        binding.fab.setOnTouchListener { _, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    incrementHandler.buttonPressed = true
                }
                MotionEvent.ACTION_UP -> {
                    incrementHandler.buttonPressed = false
                }
            }
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}