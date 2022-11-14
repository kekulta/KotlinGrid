package ru.kekulta.kotlingrid


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import ru.kekulta.kotlingrid.fragment.GridFragment


private const val KEY_CURRENT_POSITION = "com.google.samples.gridtopager.key.currentPosition"

/**
 * Grid to pager app's main activity.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Holds the current image position to be shared between the grid and the pager fragments. This
     * position updated when a grid item is clicked, or when paging the pager.
     *
     * In this demo app, the position always points to an image index at the {ImageData} class.
     */
    companion object {
        var currentPosition = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION, 0)
            // Return here to prevent adding additional GridFragments when changing orientation.
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