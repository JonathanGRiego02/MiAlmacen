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
        val dialogBinding = RegisterDialogBinding.inflate(LayoutInflater.from(this))

        val dialog = AlertDialog.Builder(this)
            .setTitle("Registrarse en la aplicación")
            .setView(dialogBinding.root)
            .setPositiveButton("Registrar") { _, _ ->
                val nombre = dialogBinding.nombreEditText.text.toString()
                val email = dialogBinding.emailEditText.text.toString()
                val password = dialogBinding.passwdEditText.text.toString()
                val dbManager = DBManager(this)
                if (nombre.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                    Toast.makeText(this, "Usuario registrado: $nombre", Toast.LENGTH_SHORT).show()
                    dbManager.addUser(nombre, email, password)
                } else {
                    Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }
}
