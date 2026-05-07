package com.example.soymusicreviewapp

import dagger.hilt.android.HiltAndroidApp
import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
@HiltAndroidApp
class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Firebase.firestore.useEmulator("10.0.2.2", 8085)
            //Firebase.firestore.useEmulator("10.0.2.2", 8080)
            Firebase.auth.useEmulator("10.0.2.2", 9099)
        }
    }
}