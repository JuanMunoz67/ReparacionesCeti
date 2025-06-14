package com.example.reparacionesceti

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.reparacionesceti.databinding.FragmentListaReportesBinding
import com.example.reparacionesceti.databinding.FragmentProfileBinding
import com.example.reparacionesceti.databinding.FragmentUserListBinding
import com.example.reparacionesceti.model.entities.User
import com.example.reparacionesceti.preferences.Preferences
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip


class ProfileFragment : Fragment() {

    private lateinit var imageViewProfilePicture: ImageView
    private lateinit var textViewProfileName: TextView
    private lateinit var textViewProfileEmail: TextView
    private lateinit var chipProfileRole: Chip
    private lateinit var buttonEditProfile: MaterialButton
    private lateinit var buttonLogout: MaterialButton

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = Preferences.currentUser!!
        init()
    }

    override fun onResume() {
        super.onResume()
        user = Preferences.currentUser!!
        init()
    }

    private fun init() {
        imageViewProfilePicture = binding.imageViewProfilePicture
        textViewProfileName = binding.textViewProfileName
        textViewProfileEmail = binding.textViewProfileEmail
        chipProfileRole = binding.chipProfileRole
        buttonEditProfile = binding.buttonEditProfile
        buttonLogout = binding.buttonLogout

        imageViewProfilePicture.setImageResource(
            when (user.role) {
                "admin" -> R.drawable.baseline_edit_notifications_24
                "estudiante" -> R.drawable.ic_person
                "tecnico" -> R.drawable.baseline_home_repair_service_24
                else -> -1
            }
        )

        textViewProfileName.text = user.name
        textViewProfileEmail.text = user.email
        chipProfileRole.text = user.role

        chipProfileRole.chipBackgroundColor = when (user.role) {
            "admin" -> ContextCompat.getColorStateList(requireContext(), R.color.blue_secondary)
            "estudiante" -> ContextCompat.getColorStateList(requireContext(), R.color.green_success)
            "tecnico" -> ContextCompat.getColorStateList(requireContext(), R.color.orange_warning)
            else -> ContextCompat.getColorStateList(requireContext(), R.color.red_urgent)
        }

        /*
        chipProfileRole.chipIcon = when (user.role) {
            "admin" -> ContextCompat.getDrawable(requireContext(), R.drawable.baseline_edit_notifications_24)
            "estudiante" -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_person)
            "tecnico" -> ContextCompat.getDrawable(requireContext(), R.drawable.baseline_home_repair_service_24)
            else -> ContextCompat.getDrawable(requireContext(), R.drawable.baseline_bug_report_24)
        }*/

        buttonEditProfile.setOnClickListener { goToEditProfile() }
        buttonLogout.setOnClickListener { logOut() }
    }

    private fun goToEditProfile() {
        val intent = Intent(context, RegisterActivity::class.java)
        intent.putExtra("userId", user.id)
        startActivity(intent)
    }

    private fun logOut() {
        Preferences(requireContext().getSharedPreferences(Preferences.USER_SESSION, 0)).deleteUserSession()
        Preferences(requireContext().getSharedPreferences(Preferences.USER_SESSION, 0)).clearData()
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}