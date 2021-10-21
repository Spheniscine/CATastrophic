package com.example.catastrophic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.example.catastrophic.ui.fragment.GridFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    private var scaleGestureDetector: ScaleGestureDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, GridFragment.newInstance(), GridFragment::class.simpleName)
                .commit()
        }

        scaleGestureDetector = ScaleGestureDetector(this,
            object: ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    Timber.d("onScale")
                    mainViewModel.updateScaleFactor(detector.scaleFactor)
                    return false
                }
            })

        mainViewModel.scaleFactorLd.observe(this) { scaleFactor ->
            val view = findViewById<View>(R.id.container)
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Timber.d("onTouchEvent")
        scaleGestureDetector?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }
}