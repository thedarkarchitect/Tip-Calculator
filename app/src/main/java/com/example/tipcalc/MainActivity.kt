package com.example.tipcalc

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
//default tip
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    //member variables to the class
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)

        //setting the initial tip before the eventlistner
        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        //initial tip decription
        updateTipDescription(INITIAL_TIP_PERCENT)
        //To change Tip percent using seekbar we create a listener in the seekbar
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
//            the overriden methods give us functionality to tap into the event listener manipulation
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //log takes two strings and let will help us see what number the seekbar is at when moved
//                Log.i(TAG, "onProgressChange $progress")
                tvTipPercentLabel.text = "$progress%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        //check for amount entered in the base
        etBaseAmount.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                //check if what you entering is being seen
//                Log.i(TAG, "afterChanged $s")
                computeTipAndTotal()
            }

        })

    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent){
            in 0..9 -> "Poor"
            in 10 .. 1 -> "Acceptable"
            in 15 .. 19 -> "Good"
            in 20 .. 24 -> "Great"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription
        //update color based on the tipPercent
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,//this gives a fraction to be used to express the color
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        )
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        //backspacing on an empty base crashes the app this ssolved by
        if(etBaseAmount.text.isEmpty()){
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }


        //get the value of the base and tip percent
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress

        //compute the tip and total
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount

        //Update the UI
        //format to get only 2 decimals
        tvTipAmount.text = "%.2f".format(tipAmount)//changes the tip amount to a string
        tvTotalAmount.text = "%.2f".format(totalAmount)//changes the total amount to a string
    }
}