package com.fakhrimf.storyapp.utils.boilerplate

import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fakhrimf.storyapp.utils.LOGIN_KEY
import com.fakhrimf.storyapp.utils.PREF
import es.dmoral.toasty.Toasty

open class MyFragment : Fragment() {
    fun showToast(message: String, long: Boolean) {
        if (long) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    val prefs by lazy {
        requireContext().getSharedPreferences(PREF, MODE_PRIVATE)
    }

    fun storeLoginKey(key: String) {
        prefs.edit().putString(LOGIN_KEY, key)
            .apply()
    }

    fun getLoginKey() : String? {
        return prefs.getString(LOGIN_KEY, "")
    }

    fun toastySuccess(message: String) {
        Toasty.success(requireContext(), message, Toast.LENGTH_LONG, true).show()
    }

    fun toastyWarning(message: String) {
        Toasty.warning(requireContext(), message, Toast.LENGTH_LONG, true).show()
    }

    fun toastyError(message: String) {
        Toasty.error(requireContext(), message, Toast.LENGTH_LONG, true).show()
    }
}