package com.example.tfg_friendpoint.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.ui.dialog.DatePickerFragment

class RegisterActivity : AppCompatActivity() {
    lateinit var etFechaNacimiento : EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        etFechaNacimiento = findViewById<EditText>(R.id.et_fechaNacimiento)
        etFechaNacimiento.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {

        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }
}