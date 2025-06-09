package com.example.reparacionesceti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reparacionesceti.model.AppDatabase
import com.example.reparacionesceti.model.ReporteAdapter

class MisReportesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var db: AppDatabase
    private lateinit var adapter: ReporteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_mis_reportes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerReportes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ReporteAdapter(emptyList())
        recyclerView.adapter = adapter

        db = AppDatabase.getDatabase(requireContext())

        // Usamos LiveData para observar los reportes en tiempo real
        db.reporteDao().observarTodos().observe(viewLifecycleOwner) { lista ->
            adapter.actualizarLista(lista)
        }
    }
}
