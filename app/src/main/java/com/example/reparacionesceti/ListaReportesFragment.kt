package com.example.reparacionesceti

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reparacionesceti.databinding.FragmentListaReportesBinding
import com.example.reparacionesceti.model.AppDatabase
import com.example.reparacionesceti.model.entities.Reporte
import com.example.reparacionesceti.model.ReporteAdapter
import kotlinx.coroutines.launch

class ListaReportesFragment : Fragment() {

    private var _binding: FragmentListaReportesBinding? = null
    private val binding get() = _binding!!

    private lateinit var reporteAdapter: ReporteAdapter // Lo crearemos luego

    private var reportes: List<Reporte> = emptyList()
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaReportesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*reporteAdapter = ReporteAdapter(emptyList())


        binding.recyclerReportes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reporteAdapter
        }*/

        db = AppDatabase.getDatabase(requireContext())
        lifecycleScope.launch {
            cargarReportes()

            binding.recyclerReportes.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerReportes.adapter = ReporteAdapter(reportes) { reporte ->
                val intent = Intent(requireContext(), CrearReporteActivity::class.java)
                intent.putExtra("reporteId", reporte.id)
                startActivity(intent)
            }
        }




        binding.fabAgregar.setOnClickListener {
            //findNavController().navigate(R.id.action_listaReportesFragment_to_formularioReporteFragment)
            val intent = Intent(requireContext(), CrearReporteActivity::class.java)
            startActivity(intent)
        }

        // Cargar datos para mostrar en la lista


    }

    private suspend fun cargarReportes() {
        /*val listaPrueba = listOf(
            Reporte(
                id = 1,
                titulo = "Problema con PC",
                ubicacion = "Laboratorio 1",
                descripcion = "No enciende la computadora",
                notas = "",
                estado = "Pendiente",
                imagenUri = null,
                fecha = System.currentTimeMillis() - 86400000L // 1 día atrás
            ),
            Reporte(
                id = 2,
                titulo = "Fuga de agua",
                ubicacion = "Baños Planta Baja",
                descripcion = "Goteras en baño",
                notas = "",
                estado = "En proceso",
                imagenUri = null,
                fecha = System.currentTimeMillis() - 43200000L // 12 horas atrás
            ),
            Reporte(
                id = 3,
                titulo = "Aire acondicionado",
                ubicacion = "Aula 204",
                descripcion = "No enfría bien",
                notas = "",
                estado = "Resuelto",
                imagenUri = null,
                fecha = System.currentTimeMillis() - 3600000L // 1 hora atrás
            )
        )*/
        reportes = db.reporteDao().obtenerTodos()
        //reporteAdapter.actualizarLista(reportes)

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
