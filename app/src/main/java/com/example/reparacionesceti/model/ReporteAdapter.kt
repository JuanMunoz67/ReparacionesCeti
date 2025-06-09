package com.example.reparacionesceti.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reparacionesceti.R
import com.example.reparacionesceti.model.entities.Reporte

class ReporteAdapter(
    private var reportes: List<Reporte>
) : RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder>() {

    // ViewHolder
    class ReporteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val tvUbicacion: TextView = itemView.findViewById(R.id.tvUbicacion)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReporteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reporte, parent, false)
        return ReporteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReporteViewHolder, position: Int) {
        val reporte = reportes[position]
        holder.tvTitulo.text = reporte.titulo
        holder.tvUbicacion.text = "Ubicación: ${reporte.ubicacion}"
        holder.tvEstado.text = "Estado: ${reporte.estado}"
        holder.tvFecha.text = "Fecha: ${reporte.fecha}"
    }

    override fun getItemCount(): Int = reportes.size

    // Método para actualizar la lista
    fun actualizarLista(nuevaLista: List<Reporte>) {
        reportes = nuevaLista
        notifyDataSetChanged()
    }
}
