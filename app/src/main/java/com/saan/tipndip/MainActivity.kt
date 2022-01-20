package com.saan.tipndip

import android.animation.ArgbEvaluator
import android.nfc.Tag
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
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip : SeekBar
    private lateinit var tvTipPercent: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercent = findViewById(R.id.tvTipPercent)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercent.text = "$INITIAL_TIP_PERCENT%"
        updateTipDesc(INITIAL_TIP_PERCENT)
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChange $p1")
                tvTipPercent.text = "$p1%"
                computeTipAndTotal()
                updateTipDesc(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }

        })
    }

    private fun updateTipDesc(tipPercent: Int) {
        val tipDescription = when (tipPercent){
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Amazing"
            else -> "Extraordinary"
        }

        tvTipDescription.text = tipDescription

        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.lowestTip),
            ContextCompat.getColor(this, R.color.greatestTip)
        ) as Int

        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        //Validating input
        if(etBaseAmount.text.isEmpty()){
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }

        //Grabbing necessary vars
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress

        //Calculating tip
        var tipAmount = baseAmount * tipPercent / 100
        var totalAmount = baseAmount + tipAmount

        //Updating UI
        tvTipAmount.text = "$" + "%.2f".format(tipAmount)
        tvTotalAmount.text = "$" + "%.2f".format(totalAmount)


    }
}