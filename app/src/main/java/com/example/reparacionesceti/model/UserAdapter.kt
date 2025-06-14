package com.example.reparacionesceti.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.reparacionesceti.R
import com.example.reparacionesceti.databinding.ItemReporteBinding
import com.example.reparacionesceti.databinding.ItemUserBinding
import com.example.reparacionesceti.model.entities.Reporte
import com.example.reparacionesceti.model.entities.User
import com.google.android.material.chip.Chip

class UserAdapter(
    private var users: List<User> , private val userClickedListener: (User)->Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder (private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.imageViewUserProfile.setImageResource(
                when (user.role){
                    "admin" -> R.drawable.baseline_edit_notifications_24
                    "estudiante" -> R.drawable.ic_person
                    "tecnico" -> R.drawable.baseline_home_repair_service_24
                    else -> R.drawable.baseline_add_a_photo_24
                }
            )

            binding.chipUserRole.text =
                when (user.role) {
                    "admin" -> "Admin"
                    "estudiante" -> "Estudiante"
                    "tecnico" -> "Técnico"
                    else -> "ERROR"
                }

            binding.chipUserRole.chipBackgroundColor =
                when (user.role) {
                    "admin" -> binding.root.context.getColorStateList(R.color.blue_secondary)
                    "estudiante" -> binding.root.context.getColorStateList(R.color.green_secondary)
                    "tecnico" -> binding.root.context.getColorStateList(R.color.orange_secondary)
                    else -> binding.root.context.getColorStateList(R.color.gray_background)
                }

            binding.textViewUserName.text = user.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)

        holder.itemView.setOnClickListener { userClickedListener(user) }
    }

    override fun getItemCount(): Int = users.size

    // Método para actualizar la lista
    fun actualizarLista(nuevaLista: List<User>) {
        users = nuevaLista
        notifyDataSetChanged()
    }
}
