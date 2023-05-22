package com.fakhrimf.storyapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.fakhrimf.storyapp.HomeActivity
import com.fakhrimf.storyapp.R
import com.fakhrimf.storyapp.RegisterActivity
import com.fakhrimf.storyapp.databinding.FragmentLoginBinding
import com.fakhrimf.storyapp.model.UserModel
import com.fakhrimf.storyapp.utils.APP_IMAGE
import com.fakhrimf.storyapp.utils.APP_NAME
import com.fakhrimf.storyapp.utils.boilerplate.MyFragment

class LoginFragment : MyFragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun init() {
        if (!getLoginKey().isNullOrEmpty()) {
            startActivity(Intent(requireContext(), HomeActivity::class.java))
            requireActivity().finish()
        }
        binding.btnSignIn.setOnClickListener {
            if (binding.etUsername.error.isNullOrEmpty() && binding.etPassword.error.isNullOrEmpty() && binding.etUsername.text.toString()
                    .isNotEmpty() && binding.etUsername.text.toString().isNotEmpty()
            ) {
                binding.spinKit.visibility = View.VISIBLE
                binding.llShadow.visibility = View.VISIBLE
                viewModel.login(
                    UserModel(
                        "",
                        binding.etUsername.text.toString(),
                        binding.etPassword.text.toString(),
                    ), prefs
                ).observe(
                    viewLifecycleOwner
                ) {
                    binding.spinKit.visibility = View.GONE
                    binding.llShadow.visibility = View.GONE
                    if (it == null) {
                        toastyError(getString(R.string.wrong_password_or_email))
                    } else {
                        startActivity(Intent(requireContext(), HomeActivity::class.java))
                        requireActivity().finish()
                    }
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(requireContext(), RegisterActivity::class.java)
            val p1 = Pair.create(binding.ivIconApp as View, APP_IMAGE)
            val p2 = Pair.create(binding.tvStoryApp as View, APP_NAME)
            val opt = ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                p1,
                p2,
            )
            startActivity(intent, opt.toBundle())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTransitionName(binding.ivIconApp, APP_IMAGE)
        init()
    }

}