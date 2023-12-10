package com.example.usermini.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.usermini.model.User

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "UserManager.db"
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_GENDER = "gender"
        private const val COLUMN_BIRTHDAY = "birthday"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLES =
            ("CREATE TABLE $TABLE_USERS ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "$COLUMN_USERNAME TEXT, $COLUMN_PASSWORD TEXT, $COLUMN_EMAIL TEXT, $COLUMN_GENDER TEXT, $COLUMN_BIRTHDAY TEXT)")
        db!!.execSQL(CREATE_TABLES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun addUser(
        username: String,
        password: String,
        email: String,
        gender: String,
        birthDay: String
    ): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USERNAME, username)
        values.put(COLUMN_PASSWORD, password)
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_GENDER, gender)
        values.put(COLUMN_BIRTHDAY, birthDay)
        val success = db.insert(TABLE_USERS, null, values)
        db.close()
        return success != -1L
    }

    // Function to get all users from the database
    @SuppressLint("Range")
    fun getAllUsers(context: Context): ArrayList<User> {
        val usersList = ArrayList<User>()
        val dbHelper = DatabaseHelper(context)
        val db: SQLiteDatabase = dbHelper.readableDatabase

        val selectQuery = "SELECT * FROM $TABLE_USERS"

        val cursor: Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                val username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME))
                val password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
                val email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))
                val gender = cursor.getString(cursor.getColumnIndex(COLUMN_GENDER))
                val birthday = cursor.getString(cursor.getColumnIndex(COLUMN_BIRTHDAY))
                val user = User(id, username, password, email, gender, birthday)
                usersList.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return usersList
    }

    @SuppressLint("Range")
    fun getUserByEmail(context: Context, email: String): User? {
        val dbHelper = DatabaseHelper(context)
        val db: SQLiteDatabase = dbHelper.readableDatabase

        val selectQuery = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor: Cursor = db.rawQuery(selectQuery, selectionArgs)
        var user: User? = null

        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
            val password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
            val username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME))
            val mEmail = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))
            val gender = cursor.getString(cursor.getColumnIndex(COLUMN_GENDER))
            val birthday = cursor.getString(cursor.getColumnIndex(COLUMN_BIRTHDAY))
            user = User(id, username, password, mEmail, gender, birthday)
        }

        cursor.close()
        db.close()
        return user
    }

    fun getUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        val result = cursor.count > 0
        cursor.close()
        db.close()
        return result
    }

    fun updateUser(
        username: String,
        newEmail: String,
        gender: String,
        birthDay: String
    ): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USERNAME, username)
        values.put(COLUMN_EMAIL, newEmail)
        values.put(COLUMN_GENDER, gender)
        values.put(COLUMN_BIRTHDAY, birthDay)
        val success = db.update(TABLE_USERS, values, "$COLUMN_USERNAME = ?", arrayOf(username))
        db.close()
        return success != -1
    }

}