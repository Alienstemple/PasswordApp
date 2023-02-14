package com.example.passwordapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.passwordapp.databinding.ActivityMainBinding
import com.example.passwordapp.databinding.ActivityPreferencesBinding

class Preferences : AppCompatActivity() {
    private lateinit var mainBinding: ActivityPreferencesBinding

    private val APP_PREF_1 = "appPrefOne"
    private val APP_PREF_2 = "appPrefTwo"
    private val preferences1: SharedPreferences = getSharedPreferences(APP_PREF_1, Context.MODE_PRIVATE)
    private val preferences2: SharedPreferences = getSharedPreferences(APP_PREF_2, Context.MODE_PRIVATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityPreferencesBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

//        getSharedPreferences()
    }
}