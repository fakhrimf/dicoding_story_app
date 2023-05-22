package com.fakhrimf.storyapp.ui.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.fakhrimf.storyapp.LoginActivity
import com.fakhrimf.storyapp.R
import com.fakhrimf.storyapp.databinding.FragmentSplashBinding
import com.fakhrimf.storyapp.utils.BACKGROUND_FADE_IN_DURATION
import com.fakhrimf.storyapp.utils.TITLE_FADE_IN_DURATION
import com.fakhrimf.storyapp.utils.boilerplate.MyFragment

class SplashFragment : MyFragment() {

    private lateinit var viewModel: SplashViewModel
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    private val mRunnable = Runnable {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }

    private val mHandler = Handler(Looper.getMainLooper())

    private fun splash() {
        binding.main.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate().apply {
                alpha(1f)
                duration = BACKGROUND_FADE_IN_DURATION
            }
        }
        binding.titleSplash.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate().apply {
                alpha(1f)
                duration = TITLE_FADE_IN_DURATION
                setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        mHandler.postDelayed(mRunnable, TITLE_FADE_IN_DURATION)
                    }
                })
            }
        }
    }

    @Deprecated("No Longer Supported")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val w = requireActivity().window
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        w.statusBarColor = ContextCompat.getColor(requireActivity(), android.R.color.transparent)
        splash()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SplashFragment()
    }

    override fun onDestroy() {
        mHandler.removeCallbacks(mRunnable)
        _binding = null
        super.onDestroy()
    }
}