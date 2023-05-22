package com.fakhrimf.storyapp.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.fakhrimf.storyapp.R
import com.fakhrimf.storyapp.databinding.FragmentRegisterBinding
import com.fakhrimf.storyapp.model.UserModel
import com.fakhrimf.storyapp.utils.boilerplate.MyFragment

class RegisterFragment : MyFragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun init() {
        binding.btnRegister.setOnClickListener {
            if (binding.etName.error.isNullOrEmpty() && binding.etUsername.error.isNullOrEmpty() && binding.etPassword.error.isNullOrEmpty() && binding.etName.text.toString()
                    .isNotEmpty() && binding.etPassword.text.toString()
                    .isNotEmpty() && binding.etUsername.text.toString().isNotEmpty()
            ) {
                binding.spinKit.visibility = View.VISIBLE
                viewModel.register(
                    UserModel(
                        binding.etName.text.toString(),
                        binding.etUsername.text.toString(),
                        binding.etPassword.text.toString(),
                    )
                ).observe(
                    viewLifecycleOwner
                ) {
                    binding.spinKit.visibility = View.GONE
                    if (it == null) {
                        toastyError(getString(R.string.something_went_wrong))
                    } else {
                        toastySuccess(it.message)
                        requireActivity().onBackPressed()
                        toastySuccess(getString(R.string.login_with_your_account))
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
}