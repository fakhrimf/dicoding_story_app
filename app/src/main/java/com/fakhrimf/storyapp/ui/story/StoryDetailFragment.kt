package com.fakhrimf.storyapp.ui.story

import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.fakhrimf.storyapp.R
import com.fakhrimf.storyapp.StoryDetailActivity
import com.fakhrimf.storyapp.databinding.FragmentStoryDetailBinding
import com.fakhrimf.storyapp.utils.STORY_IMAGE
import com.fakhrimf.storyapp.utils.boilerplate.MyFragment
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class StoryDetailFragment : MyFragment() {
    private var _binding: FragmentStoryDetailBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = StoryDetailFragment()
    }

    private lateinit var viewModel: StoryDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[StoryDetailViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun init() {
        val act = requireActivity() as StoryDetailActivity
        val story = act.getStory()
        Picasso.get().load(story.photoUrl).into(binding.ivStory, object : Callback {
            override fun onSuccess() {
                binding.ivStory.transitionName = STORY_IMAGE
                requireActivity().window.sharedElementEnterTransition =
                    TransitionInflater.from(requireContext()).inflateTransition(
                        R.transition.shared_element_transition
                    )
            }

            override fun onError(e: Exception?) {
                requireActivity().finish()
                toastyWarning("Picasso : ${e.toString()}")
            }
        })
        binding.tvDate.text = story.createdAt
        binding.tvIsi.text = story.description
        binding.tvUploader.text = story.name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

}