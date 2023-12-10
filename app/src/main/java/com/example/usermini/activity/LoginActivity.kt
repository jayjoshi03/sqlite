package com.example.usermini.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.usermini.database.DatabaseHelper
import com.example.usermini.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        manageClick()
    }

    private fun manageClick() {
        binding.apply {
            signupBtn.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            loginBtn.setOnClickListener {
                val email = editEmail.text.toString().trim()
                val password = editPassword.text.toString().trim()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    login(email, password)
                } else Toast.makeText(
                    this@LoginActivity,
                    "One or Both filed empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun login(email: String, password: String) {
        val db = DatabaseHelper(this)
        val loginSuccessful = db.getUser(email, password)
        if (loginSuccessful) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USER", email)
            startActivity(intent)
            finish()
        } else Toast.makeText(this, "Not Match Id and Password!!", Toast.LENGTH_SHORT).show()
    }
}