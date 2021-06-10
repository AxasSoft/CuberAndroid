package ru.wood.cuber

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil.setContentView
import androidx.databinding.ObservableField
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import dagger.hilt.android.AndroidEntryPoint
import org.apache.poi.hpsf.Util
import ru.wood.cuber.databinding.ActivityMainBinding
import ru.wood.cuber.utill.Utill
//Ramires
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val observableField =ObservableField<Boolean> ()
    lateinit var splashScreen: RelativeLayout
    var navController: NavController? = null
    private lateinit var appBarConfiguration: AppBarConfiguration

    fun initLengths (){
        for (x in 60 downTo 30){
            val number1digits:Double = x/10.toDouble()
            Utill.LENGTHS.add(number1digits)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.activity=this

        initLengths()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            title = ""
        }

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        splashScreen=binding.splashScreen
        //light тема
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        for (x in 16..51 ){
            Utill.DIAMETERS?.add(x)
        }
    }

    fun showSplashScreen() {
        observableField.set(true)
    }

    fun hideSplashScreen() {
        observableField.set(false)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val v = currentFocus
        val ret = super.dispatchTouchEvent(event)
        if (v is EditText) {
            val w = currentFocus
            val scrcoords = IntArray(2)
            w!!.getLocationOnScreen(scrcoords)
            val x = event.rawX + w.left - scrcoords[0]
            val y = event.rawY + w.top - scrcoords[1]
            if (event.action == MotionEvent.ACTION_UP && (x < w.left || x >= w.right || y < w.top || y > w.bottom)) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(window.currentFocus!!.windowToken, 0)
            }
        }
        return ret
    }
}

inline fun Activity?.base(block: MainActivity.() -> Unit) {
    (this as? MainActivity)?.let(block)
}
