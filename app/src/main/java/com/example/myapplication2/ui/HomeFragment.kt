import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication2.adapters.ContentCardAdapter
import com.example.myapplication2.databinding.FragmentHomeBinding
import com.example.myapplication2.model.ContentCard

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var cardAdapter: ContentCardAdapter

    override
    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sampleCards = listOf(
            ContentCard("Nature Landscape", "https://example.com/image1.jpg", false),
            ContentCard("City Skyline", "https://example.com/image2.mp4", false)
        )

        cardAdapter = ContentCardAdapter(sampleCards)
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
