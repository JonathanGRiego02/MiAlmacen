package pgl.proyecto.actividades

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import pgl.proyecto.R
import pgl.proyecto.actividades.modelos.Almacen
import pgl.proyecto.actividades.modelos.Objeto
import pgl.proyecto.actividades.ui.AlmacenObjetosFragment
import pgl.proyecto.adaptadores.AlmacenEditAdapter
import pgl.proyecto.controladores.DBManager
import pgl.proyecto.databinding.ActivityAlmacenesBinding
import pgl.proyecto.databinding.AddAlmacenDialogBinding
import pgl.proyecto.databinding.AddObjetoDialogBinding
import pgl.proyecto.databinding.EditAlmacenesDialogBinding

class AlmacenesActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAlmacenesBinding
    private lateinit var dbManager: DBManager
    private lateinit var almacenes: List<Almacen>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAlmacenesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarAlmacenes.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_almacenes)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_almacen_objetos
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Get the user's name from the intent
        val userName = intent.getStringExtra("userName")
        val headerView = navView.getHeaderView(0)
        val welcomeTextView: TextView = headerView.findViewById(R.id.welcomeTextView)
        welcomeTextView.text = "Bienvenido $userName!"

        dbManager = DBManager(this)
        almacenes = dbManager.getAlmacenes()
        val menu = navView.menu
        almacenes.forEach { almacen ->
            val menuItem = menu.add(R.id.group_almacenes, Menu.NONE, Menu.NONE, almacen.nombre)
            menuItem.setIcon(R.drawable.ic_almacen)
            menuItem.setOnMenuItemClickListener {
                val bundle = Bundle()
                bundle.putInt("idAlmacen", almacen.idAlmacen)
                navController.navigate(R.id.nav_almacen_objetos, bundle)
                drawerLayout.closeDrawers()
                true
            }
        }

        if (almacenes.isNotEmpty()) {
            val bundle = Bundle()
            bundle.putInt("idAlmacen", almacenes[0].idAlmacen)
            navController.navigate(R.id.nav_almacen_objetos, bundle)
        }

        // Set click listener for the "Add Object" button
        binding.appBarAlmacenes.addObjeto.setOnClickListener {
            showAddObjetoDialog()
        }

        val editButton: Button = findViewById(R.id.editAlmacenesButton)
        editButton.setOnClickListener {
            showEditAlmacenesDialog()
        }

        val aboutButton: Button = findViewById(R.id.aboutButton)
        aboutButton.setOnClickListener {
            showAboutDialog()
        }

        val logoutButton: Button = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logout()
        }


    }

    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Información de la app")
            .setMessage("App Version: 1.0\nDevelopers: Jonathan Gutiérrez Riego \nAplicación para la gestión de almacenes")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showEditAlmacenesDialog() {
        val dialogBinding = EditAlmacenesDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Editar Almacenes")
            .setView(dialogBinding.root)
            .setPositiveButton("Cerrar", null)
            .create()

        dialog.show()

        val adapter = AlmacenEditAdapter(almacenes) { almacen ->
            showDeleteConfirmationDialog(almacen, dialog)
        }
        dialogBinding.almacenesRecyclerView.layoutManager = LinearLayoutManager(this)
        dialogBinding.almacenesRecyclerView.adapter = adapter
    }

    private fun showDeleteConfirmationDialog(almacen: Almacen, editDialog: AlertDialog) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Estás seguro de que quieres eliminar el almacén ${almacen.nombre}?")
            .setPositiveButton("Eliminar") { dialog, _ ->
                dbManager.deleteAlmacen(almacen.idAlmacen)
                dialog.dismiss()
                editDialog.dismiss()
                recreate()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showAddObjetoDialog() {
        val dialogBinding = AddObjetoDialogBinding.inflate(layoutInflater)
        val nombreEditText = dialogBinding.nombreEditText
        val pesoEditText = dialogBinding.pesoEditText
        val anchoEditText = dialogBinding.anchoEditText
        val largoEditText = dialogBinding.largoEditText

        val dialog = AlertDialog.Builder(this)
            .setTitle("Agregar Objeto")
            .setView(dialogBinding.root)
            .setPositiveButton("Agregar", null)
            .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            val nombre = nombreEditText.text.toString().trim()
            val peso = pesoEditText.text.toString().trim().toDoubleOrNull()
            val ancho = anchoEditText.text.toString().trim().toDoubleOrNull()
            val largo = largoEditText.text.toString().trim().toDoubleOrNull()

            if (nombre.isNotEmpty() && peso != null && ancho != null && largo != null) {
                val currentFragment = getCurrentFragment()
                val idAlmacen = currentFragment?.arguments?.getInt("idAlmacen", -1) ?: -1
                if (idAlmacen != -1) {
                    val newObjeto = Objeto(0, nombre, peso, ancho, largo, idAlmacen)
                    val success = dbManager.addObjeto(newObjeto)
                    if (success) {
                        if (currentFragment != null) {
                            currentFragment.updateRecyclerView()
                        } else {
                            Toast.makeText(this, "Error al actualizar la lista de objetos", Toast.LENGTH_SHORT).show()
                        }
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Error al agregar el objeto", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "ID de almacén no válido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // Conseguir el fragmento actual para saber en que almacen estoy
    private fun getCurrentFragment(): AlmacenObjetosFragment? {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_almacenes) as? NavHostFragment
        return navHostFragment?.childFragmentManager?.fragments?.firstOrNull() as? AlmacenObjetosFragment
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.almacenes, menu)
        menu.findItem(R.id.action_addAlmacen).setOnMenuItemClickListener {
            showAddAlmacenDialog()
            true
        }
        menu.findItem(R.id.action_settings).setOnMenuItemClickListener {
            Log.d("AlmacenesActivity", "Se ha ejecutado el bloque de configuración")
            val intent = Intent(this, AjustesActivity::class.java)
            startActivity(intent)
            true
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_almacenes)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showAddAlmacenDialog() {
        val dialogBinding = AddAlmacenDialogBinding.inflate(layoutInflater)
        val nombreEditText = dialogBinding.nombreEditText
        val direccionEditText = dialogBinding.direccionEditText
        val nombreLayout = dialogBinding.nombreTextInputLayout
        val direccionLayout = dialogBinding.direccionTextInputLayout

        val dialog = AlertDialog.Builder(this)
            .setTitle("Agregar Almacén")
            .setView(dialogBinding.root)
            .setPositiveButton("Agregar", null) // Set the positive button
            .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            val isValid = validateAlmacenFields(
                nombreEditText,
                direccionEditText,
                nombreLayout,
                direccionLayout
            )

            if (isValid) {
                val newAlmacen = Almacen(
                    0,
                    nombreEditText.text.toString().trim(),
                    direccionEditText.text.toString().trim()
                )
                dbManager.addAlmacen(newAlmacen)
                updateNavigationView()
                dialog.dismiss()
            }
        }
    }

    private fun validateAlmacenFields(
        nombreEditText: TextInputEditText,
        direccionEditText: TextInputEditText,
        nombreLayout: TextInputLayout,
        direccionLayout: TextInputLayout
    ): Boolean {
        val nombre = nombreEditText.text.toString().trim()
        val direccion = direccionEditText.text.toString().trim()

        var isValid = true

        if (TextUtils.isEmpty(nombre)) {
            nombreLayout.error = "El nombre es obligatorio"
            isValid = false
        } else {
            nombreLayout.error = null
        }

        if (TextUtils.isEmpty(direccion)) {
            direccionLayout.error = "La dirección es obligatoria"
            isValid = false
        } else {
            direccionLayout.error = null
        }

        return isValid
    }

    private fun updateNavigationView() {
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_almacenes)
        val drawerLayout: DrawerLayout = binding.drawerLayout

        val menu = navView.menu
        menu.removeGroup(R.id.group_almacenes)
        almacenes = dbManager.getAlmacenes()
        almacenes.forEach { almacen ->
            val menuItem = menu.add(R.id.group_almacenes, Menu.NONE, Menu.NONE, almacen.nombre)
            menuItem.setIcon(R.drawable.ic_almacen)
            menuItem.setOnMenuItemClickListener {
                drawerLayout.closeDrawers()
                true
            }

        }
    }

}