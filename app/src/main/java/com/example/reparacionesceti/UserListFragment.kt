package com.example.reparacionesceti

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reparacionesceti.databinding.FragmentListaReportesBinding
import com.example.reparacionesceti.databinding.FragmentUserListBinding
import com.example.reparacionesceti.model.AppDatabase
import com.example.reparacionesceti.model.ReporteAdapter
import com.example.reparacionesceti.model.UserAdapter
import com.example.reparacionesceti.model.entities.Reporte
import com.example.reparacionesceti.model.entities.User
import kotlinx.coroutines.launch

class UserListFragment : Fragment() {
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private lateinit var userAdapter: UserAdapter

    private var users: List<User> = emptyList()
    private lateinit var db: AppDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch { loadUsers() }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch { loadUsers() }
    }

    private suspend fun loadUsers() {
        db = AppDatabase.getDatabase(requireContext())
        users = db.userDao().getAll()

        binding.recyclerUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerUsers.adapter = UserAdapter(users) { user ->
            val intent = Intent(requireContext(), RegisterActivity::class.java)
            intent.putExtra("userId", user.id)
            startActivity(intent)
        }
    }
}