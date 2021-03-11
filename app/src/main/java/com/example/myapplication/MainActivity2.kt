package com.example.myapplication

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView


class MainActivity2 : AppCompatActivity() {
    private var value: Int? = 0
    private lateinit var textView: TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.value = intent.getIntExtra("value", 0)
            .let { getSquare(it) }

        setContentView(R.layout.activity_main2)
        this.textView = this.findViewById(R.id.textView2)
        this.updateText()
        Log.i(TAG, "onCreate()")
    }

    fun getSquare(x: Int): Int {
        return x * x
    }

    fun updateText() {
        this.textView.text = this.value.toString()
    }
    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume()")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart()")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop()")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause()")
    }
    companion object {
        private const val TAG = "Activity2"
    }
}