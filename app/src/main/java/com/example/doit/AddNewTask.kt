package com.example.doit

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.penguincoders.doit.Model.ToDoModel
import net.penguincoders.doit.Utils.DatabaseHandler

class AddNewTask : BottomSheetDialogFragment() {

    private lateinit var newTaskText: EditText
    private lateinit var newTaskSaveButton: Button
    private lateinit var db: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.new_task, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newTaskText = requireView().findViewById(R.id.newTaskText)
        newTaskSaveButton = requireView().findViewById(R.id.newTaskButton)

        var isUpdate = false

        arguments?.let { bundle ->
            isUpdate = true
            val task = bundle.getString("task")
            newTaskText.setText(task)
            task?.let {
                if (it.length > 0)
                    newTaskSaveButton.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimaryDark
                        )
                    )
            }
        }

        db = DatabaseHandler(requireActivity())
        db.openDatabase()

        newTaskText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    newTaskSaveButton.isEnabled = false
                    newTaskSaveButton.setTextColor(Color.GRAY)
                } else {
                    newTaskSaveButton.isEnabled = true
                    newTaskSaveButton.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimaryDark
                        )
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        val finalIsUpdate = isUpdate
        newTaskSaveButton.setOnClickListener {
            val text = newTaskText.text.toString()
            if (finalIsUpdate) {
                arguments?.getInt("id")?.let { id ->
                    db.updateTask(id, text)
                }
            } else {
                val task = ToDoModel().apply {
                    this.task = text
                    this.status = 0
                }
                db.insertTask(task)
            }
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity = requireActivity()
        if (activity is DialogCloseListener) {
            (activity as DialogCloseListener).handleDialogClose(dialog)
        }
    }

    interface DialogCloseListener {
        fun handleDialogClose(dialog: DialogInterface)
    }

    companion object {
        const val TAG = "ActionBottomDialog"

        fun newInstance(): AddNewTask {
            return AddNewTask()
        }
    }
}
