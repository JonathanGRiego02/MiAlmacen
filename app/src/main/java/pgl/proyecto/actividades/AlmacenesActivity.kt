package pgl.proyecto.actividades

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
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

        AlertDialog.Builder(this)
            .setTitle("Agregar Almacén")
            .setView(dialogBinding.root)
            .setPositiveButton("Agregar") { dialog, _ ->
                val nombre = nombreEditText.text.toString()
                val direccion = direccionEditText.text.toString()

                if (nombre.isNotEmpty() && direccion.isNotEmpty()) {
                    val newAlmacen = Almacen(0, nombre, direccion)
                    dbManager.addAlmacen(newAlmacen)
                    updateNavigationView()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
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
                val bundle = Bundle()
                bundle.putInt("idAlmacen", almacen.idAlmacen)
                navController.navigate(R.id.nav_almacen_objetos, bundle)
                drawerLayout.closeDrawers()
                true
            }
        }
    }
}