package pgl.proyecto.actividades.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import pgl.proyecto.R
import pgl.proyecto.actividades.modelos.Objeto
import pgl.proyecto.adaptadores.ObjetoAdapter
import pgl.proyecto.controladores.DBManager
import pgl.proyecto.databinding.FragmentAlmacenObjetosBinding

class AlmacenObjetosFragment : Fragment() {

    private var _binding: FragmentAlmacenObjetosBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbManager: DBManager
    private lateinit var objetos: List<Objeto>
    private lateinit var adapter: ObjetoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlmacenObjetosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val idAlmacen = arguments?.getInt("idAlmacen") ?: return root
        dbManager = DBManager(requireContext())
        objetos = dbManager.getObjetosByAlmacen(idAlmacen)

        // Show or hide the empty state message
        if (objetos.isEmpty()) {
            binding.emptyTextView.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.emptyTextView.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }

        // Configure the RecyclerView
        adapter = ObjetoAdapter(objetos) { objeto ->
            showDeleteMenu(objeto)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        return root
    }

    private fun showDeleteMenu(objeto: Objeto) {
        val popupMenu = PopupMenu(requireContext(), binding.recyclerView)
        popupMenu.menuInflater.inflate(R.menu.context_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_delete -> {
                    dbManager.deleteObjeto(objeto.idObjeto)
                    updateRecyclerView()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    fun updateRecyclerView() {
        val idAlmacen = arguments?.getInt("idAlmacen") ?: return
        objetos = dbManager.getObjetosByAlmacen(idAlmacen)
        adapter = ObjetoAdapter(objetos) { objeto ->
            showDeleteMenu(objeto)
        }
        binding.recyclerView.adapter = adapter

        // Show or hide the empty state message
        if (objetos.isEmpty()) {
            binding.emptyTextView.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.emptyTextView.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}