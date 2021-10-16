package com.example.catastrophic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.catastrophic.ui.main.GridFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, GridFragment.newInstance())
                .commitNow()
        }
    }
}