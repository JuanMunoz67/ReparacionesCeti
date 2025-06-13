package com.example.reparacionesceti.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reparacionesceti.R
import com.example.reparacionesceti.databinding.ItemReporteBinding
import com.example.reparacionesceti.model.entities.Reporte
import com.google.android.material.chip.Chip

class ReporteAdapter(
    private var reportes: List<Reporte> , private val reporteClickedListener: (Reporte)->Unit
) : RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder>() {

    // ViewHolder
    //class ReporteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        //val tvUbicacion: TextView = itemView.findViewById(R.id.tvUbicacion)
        //val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        //val chEstado: Chip = itemView.findViewById(R.id.chEstado)

    //}

    class ReporteViewHolder (private val binding: ItemReporteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reporte: Reporte) {
            binding.tvTitulo.text = reporte.titulo
            binding.tvUbicacion.text = "Ubicación: ${reporte.ubicacion}"
            binding.tvFecha.text = "Fecha: ${reporte.fecha}"
            binding.chEstado.text = "${reporte.estado}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReporteViewHolder {
       // val view = LayoutInflater.from(parent.context)
         //   .inflate(R.layout.item_reporte, parent, false)
        //return ReporteViewHolder(view)

        val binding = ItemReporteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReporteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReporteViewHolder, position: Int) {
        val reporte = reportes[position]
        holder.bind(reporte)
        holder.itemView.setOnClickListener { reporteClickedListener(reporte) }
    }

    /*override fun onBindViewHolder(holder: ReporteViewHolder, position: Int) {
        val reporte = reportes[position]
        holder.tvTitulo.text = reporte.titulo
        holder.tvUbicacion.text = "Ubicación: ${reporte.ubicacion}"
        holder.tvFecha.text = "Fecha: ${reporte.fecha}"
        holder.chEstado.text = "${reporte.estado}"
        holder.chEstado.chipBackgroundColor = when (reporte.estado.lowercase()) {
            "pendiente" -> holder.itemView.context.getColorStateList(R.color.red_urgent)
            "en proceeso" -> holder.itemView.context.getColorStateList(R.color.orange_warning)
            "resuelto" -> holder.itemView.context.getColorStateList(R.color.green_success)
            else -> holder.itemView.context.getColorStateList(R.color.gray_background)
        }
        holder.chEstado.chipIcon = when (reporte.estado.lowercase()) {
            "pendiente" -> holder.itemView.context.getDrawable(R.drawable.baseline_pending_actions_24)
            "en proceso" -> holder.itemView.context.getDrawable(R.drawable.baseline_home_repair_service_24)
            "resuelto" -> holder.itemView.context.getDrawable(R.drawable.baseline_thumb_up_24)
            else -> null
        }

        holder.itemView.setOnClickListener { reporteClickedListener(reporte) }
    }*/

    override fun getItemCount(): Int = reportes.size

    // Método para actualizar la lista
    fun actualizarLista(nuevaLista: List<Reporte>) {
        reportes = nuevaLista
        notifyDataSetChanged()
    }
}
