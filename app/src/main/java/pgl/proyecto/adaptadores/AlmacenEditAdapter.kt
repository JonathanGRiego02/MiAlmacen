package pgl.proyecto.adaptadores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pgl.proyecto.actividades.modelos.Almacen
import pgl.proyecto.databinding.ItemAlmacenEditBinding

class AlmacenEditAdapter(
    private val almacenes: List<Almacen>,
    private val onDeleteClick: (Almacen) -> Unit
) : RecyclerView.Adapter<AlmacenEditAdapter.AlmacenViewHolder>() {

    inner class AlmacenViewHolder(val binding: ItemAlmacenEditBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(almacen: Almacen) {
            binding.almacenNameTextView.text = almacen.nombre
            binding.deleteButton.setOnClickListener {
                onDeleteClick(almacen)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlmacenViewHolder {
        val binding = ItemAlmacenEditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlmacenViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlmacenViewHolder, position: Int) {
        holder.bind(almacenes[position])
    }

    override fun getItemCount(): Int = almacenes.size
}