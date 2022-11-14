package ru.kekulta.kotlingrid



import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit



private const val KEY_CURRENT_POSITION = "com.google.samples.gridtopager.key.currentPosition"

class MainActivity : AppCompatActivity() {

    companion object {
        var currentPosition = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION, 0)
            return
        }


         supportFragmentManager.commit {
            add(R.id.fragment_container, GridFragment(), GridFragment::class.java.simpleName)
         }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_POSITION, currentPosition)
    }
}