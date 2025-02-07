package pgl.proyecto.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pgl.proyecto.R
import pgl.proyecto.actividades.modelos.Objeto
import pgl.proyecto.databinding.ItemObjetoBinding

class ObjetoAdapter(
    private val objetos: List<Objeto>,
    private val onLongClick: (Objeto) -> Unit
) : RecyclerView.Adapter<ObjetoAdapter.ObjetoViewHolder>() {

    inner class ObjetoViewHolder(val binding: ItemObjetoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(objeto: Objeto) {
            binding.textNombre.text = objeto.nombre
            binding.root.setOnLongClickListener {
                onLongClick(objeto)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjetoViewHolder {
        val binding = ItemObjetoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ObjetoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ObjetoViewHolder, position: Int) {
        holder.bind(objetos[position])
    }

    override fun getItemCount(): Int = objetos.size
}