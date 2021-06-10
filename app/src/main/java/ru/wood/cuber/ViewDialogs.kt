package ru.wood.cuber

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment


object ViewDialog {
    fun showDialogOfLength(
        context: Context,
        positiveAction: () -> Unit,
        commonAction: () -> Unit,
        checkBoxAction: () -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.length_dialog)
        var commonLength = false

        val checkBox =dialog.findViewById<CheckBox>(R.id.checkbox)
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            commonLength=isChecked
        }

        val ok=dialog.findViewById<TextView>(R.id.ok)
        ok.setOnClickListener {
            positiveAction()
            if (commonLength){
                checkBoxAction()
            }
            commonAction()
            dialog.dismiss()
        }

        val cancel=dialog.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            commonAction()
            dialog.dismiss()
        }
        dialog.apply {
            show()
            setCanceledOnTouchOutside(true)
        }
    }

    fun showCreateCalculationDialog(
        context: Context,
        msg: String,
        positiveAction: (name: String) -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.create_calculates_dialog)

        val editText =dialog.findViewById<EditText>(R.id.name)
        var name: String?=null

        val message =dialog.findViewById<TextView>(R.id.message)
        message.text=msg

        val ok=dialog.findViewById<TextView>(R.id.ok)
        ok.setOnClickListener {
            name=editText.text.toString()

            if (name==null || name==""){
                Loger.log("$name Введите имя")
                Toast.makeText(context, "Введите имя", Toast.LENGTH_SHORT).show()
            }else {
                Loger.log("$name")
                positiveAction(name!!)
                dialog.dismiss()
            }
        }

        val cancel=dialog.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.apply {
            show()
            setCanceledOnTouchOutside(true)
        }
    }
}

class SimpleDialogFragment(
    val message: String?,
    val positiveAction: () -> Unit,
    val negativeAction: () -> Unit
):DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(message)
                    //.setMessage("вопрос")
                    .setNegativeButton("Отмена"){ dialog, id ->  dialog.cancel(); negativeAction()
                    }
                    .setPositiveButton("Да") { dialog, id -> dialog.cancel();  positiveAction();
                    }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class SimpleDialogFragment2(
    val message: String?,
    val positiveFunction: () -> Unit,
    val negativeFunction: () -> Unit
):DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(message)
                //.setMessage("вопрос")
                .setNegativeButton("Отмена"){ dialog, id ->  dialog.cancel(); negativeFunction()
                }
                .setPositiveButton("Да") { dialog, id -> dialog.cancel();  positiveFunction()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}