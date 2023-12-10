package com.example.usermini.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.usermini.database.DatabaseHelper
import com.example.usermini.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var email: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        email = intent.getStringExtra("USER")!!
        loadData()
        manageClick()
    }

    private fun loadData() {
        binding.apply {
            val userData = DatabaseHelper(this@MainActivity).getUserByEmail(this@MainActivity, email!!)
            textName.text = userData!!.name
            textEmail.text = email
            textBirthDay.text = userData.birthday
            textGender.text = userData.gender
        }
    }

    private fun manageClick() {
        binding.apply {
            //Logout Button Click
//            logoutBtn.setOnClickListener {
//                viewModel.logout(false)
//                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
//            }

            //Update Button Click
            updateBtn.setOnClickListener {
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                intent.putExtra("UPDATE", true)
                intent.putExtra("USER", email)
                startActivity(intent)
            }
        }
    }

}