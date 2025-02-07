package pgl.proyecto.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pgl.proyecto.R
import pgl.proyecto.actividades.modelos.Objeto
import pgl.proyecto.databinding.ItemObjetoBinding

class ObjetoAdapter(private val objetos: List<Objeto>) : RecyclerView.Adapter<ObjetoAdapter.ObjetoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjetoViewHolder {
        val binding = ItemObjetoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ObjetoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ObjetoViewHolder, position: Int) {
        val objeto = objetos[position]
        holder.bind(objeto)
    }

    override fun getItemCount(): Int = objetos.size

    class ObjetoViewHolder(private val binding: ItemObjetoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(objeto: Objeto) {
            binding.textNombre.text = objeto.nombre
            binding.textPeso.text = "Peso: ${objeto.peso}"
            binding.textAncho.text = "Ancho: ${objeto.ancho}"
            binding.textLargo.text = "Largo: ${objeto.largo}"
        }
    }
}