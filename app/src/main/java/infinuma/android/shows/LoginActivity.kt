package infinuma.android.shows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

//Scroll down for activity lifecycle logs observed from Logcat.

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d("LoginActivity", "onCreate called")
    }
    override fun onStart() {
        super.onStart()
        Log.d("LoginActivity", "onStart called")
    }
    override fun onResume() {
        super.onResume()
        Log.d("LoginActivity", "onResume called")
    }
    override fun onPause() {
        super.onPause()
        Log.w("LoginActivity", "onPause called")
    }
    override fun onStop() {
        super.onStop()
        Log.w("LoginActivity", "onStop called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.w("LoginActivity", "onDestroy called")
    }
    override fun onRestart() {
        super.onRestart()
        Log.i("LoginActivity", "onRestart called")
    }
}
/*
Logcat logs:
* 1. When the app is put on background and moved back to foreground:
onPause
onStop
onRestart
onStart
onResume    are called.
----------------
* 2. When the app is killed:
onPause
onStop
onDestroy   are called.
----------------
* 3. When the phone screen is locked and unlocked:
onPause
onStop
onRestart
onStart
onResume    are called, similar to when it's put on background and moved back to foreground.
----------------
* When the app is first opened:
onCreate
onStart
onResume    are called.
----------------
* When the app is reopened from the background:
onRestart
onStart
onResume    are called.
----------------
* When the app is put on background:
onPause
onStop      are called.
----------------
* When the app is closed/killed from the background:
onDestroy   is called.
*/