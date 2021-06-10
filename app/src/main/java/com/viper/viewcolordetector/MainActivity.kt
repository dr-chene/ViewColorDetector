package com.viper.viewcolordetector

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.viper.viewcolordetector.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        DetectClient.Builder(binding.root).activityInfo(this).delay(10000).build().apply {
            lifecycle.addObserver(this)
        }
    }
}