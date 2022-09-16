package com.example.digitalreceipts.ui.custom.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.digitalreceipts.R

class ProgressHUD(context: Context?, theme: Int) : Dialog(context!!, theme) {

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        val imageView = findViewById<View>(R.id.spinnerImageView) as ImageView
        val spinner = imageView.background as AnimationDrawable
        spinner.start()
    }

    fun setMessage(message: CharSequence?) {
        if (message != null && message.isNotEmpty()) {
            val txt = findViewById<View>(R.id.message) as TextView

            txt.visibility = View.VISIBLE
            txt.text = message
            txt.invalidate()
        }
    }

    companion object {
        fun show(
            context: Context?, message: CharSequence?, indeterminate: Boolean, cancelable: Boolean,
        ): ProgressHUD {
            val dialog = ProgressHUD(context!!, R.style.ProgressHUD)
            dialog.setTitle("")
            dialog.setContentView(R.layout.progress_hud)

            val txt = dialog.findViewById<View>(R.id.message) as TextView

            if (message.isNullOrEmpty()) {
                txt.visibility = View.GONE
            } else {
                txt.text = message
            }

            dialog.setCancelable(cancelable)
            //dialog.setOnCancelListener(cancelListener)
            dialog.window!!.attributes.gravity = Gravity.CENTER

            val lp = dialog.window!!.attributes
            lp.dimAmount = 0.2f

            dialog.window!!.attributes = lp
            //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            dialog.show()

            return dialog
        }
    }
}