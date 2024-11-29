import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.myapplication2.databinding.FragmentProfileBinding
import com.example.myapplication2.ui.LoginActivity

class ProfileFragment : Fragment() {
    // associated type list item 
    enum class SettingsItem(val title: String){
        EDIT_PROFILE("Edit Profile"),
        CHANGE_PASSWORD("Change Password"),
        NOTIFICATIONS("Notifications"),
        PRIVACY_SETTINGS("Privacy Settings"),
        LOGOUT("Logout");
    }
    
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding

    private lateinit var settingsAdapter: ArrayAdapter<String>
    private val settingsList = SettingsItem.entries.map { it.title }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root ?: throw RuntimeException("FragmentProfileBinding is null")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create an ArrayAdapter to populate the ListView
        settingsAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            settingsList
        )

        // Set the adapter to the ListView
        binding?.settingsList?.adapter = settingsAdapter

        // Add click listeners for settings
        binding?.settingsList?.setOnItemClickListener { _, _, position, _ ->
            when (settingsList[position]) {
                SettingsItem.EDIT_PROFILE.title -> navigateToEditProfile()
                SettingsItem.CHANGE_PASSWORD.title -> navigateToChangePassword()
                SettingsItem.NOTIFICATIONS.title -> navigateToNotificationSettings()
                SettingsItem.PRIVACY_SETTINGS.title -> navigateToPrivacySettings()
                SettingsItem.LOGOUT.title -> performLogout()
            }
        }
    }

    private fun navigateToEditProfile() {
        // Implement navigation or show edit profile dialog
    }

    private fun navigateToChangePassword() {
        // Implement password change logic
    }

    private fun navigateToNotificationSettings() {
        // Implement notification settings
    }

    private fun navigateToPrivacySettings() {
        // Implement privacy settings
    }

    private fun performLogout() {
        // Implement logout logic
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}