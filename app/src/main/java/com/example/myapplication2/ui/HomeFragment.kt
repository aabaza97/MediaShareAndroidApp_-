import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication2.adapters.ContentCardAdapter
import com.example.myapplication2.databinding.FragmentHomeBinding
import com.example.myapplication2.model.ContentCard
import com.example.myapplication2.model.HomeViewModel
import com.example.myapplication2.service.media.util.MediaType
import com.example.myapplication2.ui.HomeActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var cardAdapter: ContentCardAdapter
    private lateinit var homeViewModel: HomeViewModel

    override
    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Initialize homeViewModel
        setupViewModelWithListeners()

        return binding.root
    }

    private fun setupViewModelWithListeners() {
        homeViewModel = ViewModelProvider(
            requireActivity()
        )[HomeViewModel::class.java]

        // Observe the uploads
        homeViewModel.uploads.observe(viewLifecycleOwner) { uploads ->
            Log.d("HomeFragment", "Received uploads: $uploads")
            uploads?.let {
                cardAdapter.updateContentCards(
                    it.media.map { media ->
                        ContentCard(media)
                    }
                )
            }
        }
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter
        cardAdapter = ContentCardAdapter(emptyList())

        binding.contentRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cardAdapter
        }
    }

    override
    fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
