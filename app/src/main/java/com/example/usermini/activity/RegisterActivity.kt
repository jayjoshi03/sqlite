package com.example.usermini.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.usermini.database.DatabaseHelper
import com.example.usermini.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var isUpdate = false
    var name: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isUpdate = intent.getBooleanExtra("UPDATE", false)
        name = intent.getStringExtra("USER")!!
        postInitViews()
        loadData()
        manageClick()
    }

    private fun postInitViews() {
        binding.apply {
            if (isUpdate) {
                signupBtn.visibility = View.GONE
                passwordLayout.visibility = View.GONE
                cPasswordLayout.visibility = View.GONE
            } else
                updateBtn.visibility = View.GONE
        }
    }

    private fun loadData() {
        binding.apply {
            val userData = DatabaseHelper(this@RegisterActivity).getUserByEmail(this@RegisterActivity, name!!)
            if (isUpdate) {
                editName.setText(userData!!.name)
                editEmail.setText(userData.email)
                editBirthDay.setText(userData.birthday)
                checkMale.isChecked = userData.gender == "Male"
                checkFemale.isChecked = userData.gender == "Female"
            }
        }
    }

    private fun manageClick() {
        binding.apply {
            signupBtn.setOnClickListener {
                registerUser()
            }

            updateBtn.setOnClickListener {
                updateUser()
                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                intent.putExtra("USER",name)
                startActivity(intent)
            }
        }
    }

    private fun registerUser() {
        binding.apply {
            val name = editName.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()
            val cPassword = editConfirmPassword.text.toString().trim()
            val birth = editBirthDay.text.toString().trim()
            val gender = if (checkMale.isChecked) "Male" else "Female"

            if (email.isNotEmpty() && cPassword.isNotEmpty()) {
                if (password == cPassword) {
                    val db = DatabaseHelper(this@RegisterActivity)
                    db.addUser(name, cPassword, email, gender, birth)
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                } else Toast.makeText(
                    this@RegisterActivity,
                    "Password not match!!",
                    Toast.LENGTH_SHORT
                ).show()
            } else Toast.makeText(
                this@RegisterActivity,
                "email and Password empty!!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateUser() {
        binding.apply {
            val db = DatabaseHelper(this@RegisterActivity)
            val name = editName.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val birth = editBirthDay.text.toString().trim()
            val gender = if (checkMale.isChecked) "Male" else "Female"
            if (email.isNotEmpty()) {
                db.updateUser(name, email, gender, birth)
            }
        }
    }
}