package ru.wood.cuber.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import ru.wood.cuber.MainActivity
import ru.wood.cuber.base

open class BaseFragment : Fragment() {
    fun showSplashScreen(activity: Activity) =base(activity) {
        this.showSplashScreen()
    }

    fun hideSplashScreen(activity: Activity) =base(activity)  {
        Thread.sleep(2000)
        this.hideSplashScreen()
    }

    inline fun base(activity: Activity,block: MainActivity.() -> Unit) {
        activity.base(block)
    }
}