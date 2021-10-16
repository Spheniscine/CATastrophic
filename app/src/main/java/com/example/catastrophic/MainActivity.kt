package com.example.catastrophic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.catastrophic.ui.fragment.GridFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

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