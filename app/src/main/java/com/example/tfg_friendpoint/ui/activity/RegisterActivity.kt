package com.example.tfg_friendpoint.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.ActivityRegisterBinding
import com.example.tfg_friendpoint.ui.dialog.DatePickerFragment

class RegisterActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
        setupDatePickerEditText()
        setupBtnContinuar()


    }

    private fun setupDatePickerEditText(){
        mBinding.etFechaNacimiento.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun setupBtnContinuar(){
        mBinding.btnContinuar.setOnClickListener {
            val registerActivity2 = Intent(this, RegisterActivity2::class.java).apply {
                putExtra("email", mBinding.etEmail?.text?.toString() ?: null)
                putExtra("contraseña", mBinding.etContrasena?.text?.toString() ?:null)
                putExtra("fechaNacimiento", mBinding.etFechaNacimiento?.text?.toString() ?:null)
                putExtra("nickname", mBinding.etNickName?.text?.toString() ?:null)
            }
            startActivity(registerActivity2)
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day: Int, month: Int, year: Int ->
            onDateSelected(
                day,
                month,
                year
            )
        }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        mBinding.etFechaNacimiento.setText("$day/$month/$year")
    }
}