package com.example.digitalreceipts.ui.custom.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.digitalreceipts.R

/**
 * Essa classe é responsável por criar, gerenciar e exibir os popups de loading,
 * que são na verdade dialogs customizados.
 */
class ProgressHUD(context: Context?, theme: Int) : Dialog(context!!, theme) {

    /**
     * Esse método é chamado sob qualquer alteração de foco que o dialog sofrer.
     * Ele basicamente vincula os itens de layout aos objetos e inicializa a animação
     * do spinner, quase como um inicializador da classe.
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        val imageView = findViewById<View>(R.id.spinnerImageView) as ImageView
        val spinner = imageView.background as AnimationDrawable
        spinner.start()
    }

    /**
     * Esse método é responsável por receber todas as informações necessárias
     * e exibir o dialog de progresso.
     */
    companion object {
        fun show(
            context: Context?,
            message: CharSequence?,
            cancelable: Boolean,
            spinnerGone: Boolean
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

            val spinner = dialog.findViewById<View>(R.id.spinnerImageView) as ImageView
            spinner.visibility = if (spinnerGone) View.GONE else View.VISIBLE

            dialog.setCancelable(cancelable)
            dialog.window!!.attributes.gravity = Gravity.CENTER

            // Controle de opacidade
            val lp = dialog.window!!.attributes
            lp.dimAmount = 0.2f

            dialog.window!!.attributes = lp
            dialog.show()

            return dialog
        }
    }
}