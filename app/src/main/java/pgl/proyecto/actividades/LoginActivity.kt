package pgl.proyecto.actividades

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import pgl.proyecto.controladores.DBManager
import pgl.proyecto.databinding.ActivityLoginBinding
import pgl.proyecto.databinding.RegisterDialogBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val dbManager = DBManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailTextField.text.toString()
            val password = binding.passwdTextField.text.toString()
            val dbManager = DBManager(this)

            if (dbManager.checkUserCredentials(email, password)) {
                val userName = dbManager.getUserNameByEmail(email) // Assuming this method exists
                val intent = Intent(this, AlmacenesActivity::class.java)
                intent.putExtra("userName", userName)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerButton.setOnClickListener {
            showRegisterDialog()
        }
    }

    private fun showRegisterDialog() {
        val dialogBinding = RegisterDialogBinding.inflate(layoutInflater)
        val nombreEditText = dialogBinding.nombreEditText
        val emailEditText = dialogBinding.emailEditText
        val passwdEditText = dialogBinding.passwdEditText
        val nombreLayout = dialogBinding.nombreTextInputLayout
        val emailLayout = dialogBinding.emailTextInputLayout
        val passwdLayout = dialogBinding.passwdTextInputLayout

        val dialog = AlertDialog.Builder(this)
            .setTitle("Registrarse")
            .setView(dialogBinding.root)
            .setPositiveButton("Registrar", null) // Set the positive button
            .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
            .create()

        dialog.show()

        // Set the click listener for the positive button
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            val isValid = validateFields(
                nombreEditText,
                emailEditText,
                passwdEditText,
                nombreLayout,
                emailLayout,
                passwdLayout
            )

            if (isValid) {
                val success = dbManager.addUser(
                    nombreEditText.text.toString().trim(),
                    emailEditText.text.toString().trim(),
                    passwdEditText.text.toString().trim()
                )
                if (success) {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateFields(
        nombreEditText: TextInputEditText,
        emailEditText: TextInputEditText,
        passwdEditText: TextInputEditText,
        nombreLayout: TextInputLayout,
        emailLayout: TextInputLayout,
        passwdLayout: TextInputLayout
    ): Boolean {
        val nombre = nombreEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwdEditText.text.toString().trim()

        var isValid = true

        Log.d("debuggin", "Esto se está ejecutando")

        if (TextUtils.isEmpty(nombre)) {
            nombreLayout.error = "El nombre es obligatorio"
            isValid = false
        } else {
            nombreLayout.error = null
        }

        if (TextUtils.isEmpty(email)) {
            emailLayout.error = "El email es obligatorio"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.error = "Formato de email inválido"
            isValid = false
        } else {
            emailLayout.error = null
        }

        if (TextUtils.isEmpty(password)) {
            passwdLayout.error = "La contraseña es obligatoria"
            isValid = false
        } else {
            passwdLayout.error = null
        }

        return isValid
    }


}




