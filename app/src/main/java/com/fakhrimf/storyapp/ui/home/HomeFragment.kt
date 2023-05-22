package com.fakhrimf.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fakhrimf.storyapp.AddStoryActivity
import com.fakhrimf.storyapp.LoginActivity
import com.fakhrimf.storyapp.databinding.FragmentHomeBinding
import com.fakhrimf.storyapp.utils.boilerplate.MyFragment

class HomeFragment : MyFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun init() {
        val key = getLoginKey()
        binding.spinKit.visibility = View.VISIBLE
        binding.llShadow.visibility = View.VISIBLE
        if (!key.isNullOrEmpty()) {
            viewModel.getStories(key).observe(viewLifecycleOwner) {
                if (it.error!!) {
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                } else {
                    binding.rvHome.adapter = HomeRecyclerAdapter(it.listStory!!, requireContext())
                    binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
                    binding.spinKit.visibility = View.GONE
                    binding.llShadow.visibility = View.GONE
                }
            }
        } else {
            toastyError("Please login again") //TODO ubah jadi string value
        }
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(requireContext(), AddStoryActivity::class.java))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onResume() {
        super.onResume()
        init()
    }

}