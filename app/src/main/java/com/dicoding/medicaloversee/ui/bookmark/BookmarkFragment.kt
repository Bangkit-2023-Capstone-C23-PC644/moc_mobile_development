package com.dicoding.medicaloversee.ui.bookmark

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.medicaloversee.adapter.FavoriteHospitalAdapter
import com.dicoding.medicaloversee.data.local.entity.HospitalEntity
import com.dicoding.medicaloversee.databinding.FragmentBookmarkBinding

class BookmarkFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var bookmarkViewModel: BookmarkViewModel
    private lateinit var favoriteAdapter: FavoriteHospitalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        favoriteAdapter = FavoriteHospitalAdapter()

        if (savedInstanceState != null) {
            @Suppress("DEPRECATION")
            val list = savedInstanceState.getParcelableArrayList<HospitalEntity>(EXTRA_STATE)
            if (list != null) {
                favoriteAdapter.submitList(list)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
    }

    private fun setupViewModel() {
        bookmarkViewModel = ViewModelProvider(
            this,
            BookmarkViewModelFactory(requireContext())
        )[BookmarkViewModel::class.java]
    }

    private fun setupRecyclerView() {

        bookmarkViewModel.getFavoriteHospitals().observe(viewLifecycleOwner) {
            favoriteAdapter.submitList(it)
            binding.rvFavorite.adapter = favoriteAdapter
            binding.ivNoFavorite.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = favoriteAdapter
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, ArrayList(favoriteAdapter.currentList))
    }

    companion object {
        const val EXTRA_STATE = "extra_state"
    }
}