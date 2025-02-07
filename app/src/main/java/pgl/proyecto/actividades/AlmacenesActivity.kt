package pgl.proyecto.actividades

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import pgl.proyecto.R
import pgl.proyecto.actividades.modelos.Almacen
import pgl.proyecto.controladores.DBManager
import pgl.proyecto.databinding.ActivityAlmacenesBinding
import pgl.proyecto.databinding.AddAlmacenDialogBinding

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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.almacenes, menu)
        menu.findItem(R.id.action_addAlmacen).setOnMenuItemClickListener {
            showAddAlmacenDialog()
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

        // Set the click listener for the positive button
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