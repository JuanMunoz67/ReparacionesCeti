package com.example.reparacionesceti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reparacionesceti.databinding.FragmentListaReportesBinding
import com.example.reparacionesceti.model.entities.Reporte
import com.example.reparacionesceti.model.ReporteAdapter

class ListaReportesFragment : Fragment() {

    private var _binding: FragmentListaReportesBinding? = null
    private val binding get() = _binding!!

    private lateinit var reporteAdapter: ReporteAdapter // Lo crearemos luego

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaReportesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reporteAdapter = ReporteAdapter(emptyList())


        binding.recyclerReportes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reporteAdapter
        }

        binding.fabAgregar.setOnClickListener {
            findNavController().navigate(R.id.action_listaReportesFragment_to_formularioReporteFragment)
        }

        // Cargar datos para mostrar en la lista
        cargarReportes()
    }

    private fun cargarReportes() {
        val listaPrueba = listOf(
            Reporte(
                id = 1,
                titulo = "Problema con PC",
                ubicacion = "Laboratorio 1",
                descripcion = "No enciende la computadora",
                estado = "Pendiente",
                imagenUri = null,
                fecha = System.currentTimeMillis() - 86400000L // 1 día atrás
            ),
            Reporte(
                id = 2,
                titulo = "Fuga de agua",
                ubicacion = "Baños Planta Baja",
                descripcion = "Goteras en baño",
                estado = "En proceso",
                imagenUri = null,
                fecha = System.currentTimeMillis() - 43200000L // 12 horas atrás
            ),
            Reporte(
                id = 3,
                titulo = "Aire acondicionado",
                ubicacion = "Aula 204",
                descripcion = "No enfría bien",
                estado = "Resuelto",
                imagenUri = null,
                fecha = System.currentTimeMillis() - 3600000L // 1 hora atrás
            )
        )
        reporteAdapter.actualizarLista(listaPrueba)
    }

    //codigo real para guardar los reportes en la bd

    /*private fun cargarReportes() {
    lifecycleScope.launch(Dispatchers.IO) {
        val lista = db.reporteDao().getAllReportes()
        launch(Dispatchers.Main) {
            reporteAdapter.actualizarLista(lista)
        }
    }*/



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
