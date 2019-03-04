package com.husar.email

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.widget.Button
import android.content.Intent
import android.net.Uri



class MainActivity : AppCompatActivity() {

    private lateinit var name: TextInputEditText
    private lateinit var subject: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var text: TextInputEditText

    private lateinit var nameWrapper: TextInputLayout
    private lateinit var subjectWrapper: TextInputLayout
    private lateinit var emailWrapper: TextInputLayout
    private lateinit var textWrapper: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // wrappers
        nameWrapper = findViewById(R.id.input_layout_name)
        subjectWrapper = findViewById(R.id.input_layout_subject)
        emailWrapper = findViewById(R.id.input_layout_email)
        textWrapper = findViewById(R.id.input_layout_text)

        // Buttons
        val buttonReset = findViewById<Button>(R.id.button_reset)
        val buttonSend = findViewById<Button>(R.id.button_send)

        // Inputs
        name = findViewById(R.id.input_name)
        subject = findViewById(R.id.input_subject)
        email = findViewById(R.id.input_email)
        text = findViewById(R.id.input_text)

        // Reset inputs
        buttonReset.setOnClickListener {
            name.text.clear()
            subject.text.clear()
            email.text.clear()
            text.text.clear()
        }

        // Send email
        buttonSend.setOnClickListener {
            val nameContent = name.text.toString().trim()
            val subjectContent = subject.text.toString().trim()
            val emailContent = email.text.toString().trim()
            val textContent = text.text.toString().trim()

            if (validate(nameContent, subjectContent, emailContent, textContent)) {
                val message = createMessage(nameContent, textContent)
                sendEmail(subjectContent, emailContent, message)
            }
        }
    }
    private fun validate(name: String, subject: String, email: String, text: String): Boolean {
        nameWrapper.setErrorEnabled(false)
        subjectWrapper.setErrorEnabled(false)
        emailWrapper.setErrorEnabled(false)
        textWrapper.setErrorEnabled(false)

        if (name.isEmpty()) {
            nameWrapper.error = "Name can not be empty"
            return false
        }else if (!name.first().isUpperCase()) {
            nameWrapper.error = "Name must start from uppercase"
            return false
        }else if (subject.isEmpty()) {
            subjectWrapper.error = "Subject can not be empty"
            return false
        }else if (email.isEmpty()) {
            emailWrapper.error = "Email can not be empty"
            return false
        } else if (!validateEmail(email)) {
            println(validateEmail(email))
            emailWrapper.error = "Incorrect email"
            return false
        } else if (text.isEmpty()) {
            textWrapper.error = "Message can not be empty"
        }
        return true
    }

    private fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun sendEmail(subject: String, email: String, text: String) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, text)

        startActivity(Intent.createChooser(emailIntent, "Send mail..."))
    }

    private fun createMessage(name: String, text: String): String {
        return "Hi my name is $name.\n\nI have got following message for you:\n\n$text"
    }
}
