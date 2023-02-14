package com.example.passwordapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.passwordapp.databinding.ActivityMainBinding
import com.example.passwordapp.databinding.ActivityPreferencesBinding

class Preferences : AppCompatActivity() {
    lateinit var mainBinding: ActivityPreferencesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityPreferencesBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

//        getSharedPreferences()
    }
}