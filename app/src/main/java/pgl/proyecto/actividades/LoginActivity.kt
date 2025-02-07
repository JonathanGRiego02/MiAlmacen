package pgl.proyecto.actividades

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
                val intent = Intent(this, AlmacenesActivity::class.java)
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

        AlertDialog.Builder(this)
            .setTitle("Registrarse")
            .setView(dialogBinding.root)
            .setPositiveButton("Registrar") { dialog, _ ->
                val nombre = nombreEditText.text.toString()
                val email = emailEditText.text.toString()
                val password = passwdEditText.text.toString()

                var isValid = true

                if (nombre.isEmpty()) {
                    nombreLayout.error = "El nombre es obligatorio"
                    isValid = false
                } else {
                    nombreLayout.error = null
                }

                if (email.isEmpty()) {
                    emailLayout.error = "El email es obligatorio"
                    isValid = false
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailLayout.error = "Formato de email inválido"
                    isValid = false
                } else {
                    emailLayout.error = null
                }

                if (password.isEmpty()) {
                    passwdLayout.error = "La contraseña es obligatoria"
                    isValid = false
                } else {
                    passwdLayout.error = null
                }

                if (isValid) {
                    val success = dbManager.addUser(nombre, email, password)
                    if (success) {
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
