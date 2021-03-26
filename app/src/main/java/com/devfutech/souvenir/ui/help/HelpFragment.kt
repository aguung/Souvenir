package com.devfutech.souvenir.ui.help

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.devfutech.souvenir.R
import com.devfutech.souvenir.databinding.FragmentHelpBinding
import com.devfutech.souvenir.ui.MainActivity
import com.devfutech.souvenir.utils.toogleActionbar
import com.devfutech.souvenir.utils.visible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpFragment : Fragment() {

    private lateinit var binding: FragmentHelpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHelpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAction()
    }

    private fun initView() {
        (activity as AppCompatActivity).toogleActionbar(
            show = true, back = false, title = resources.getString(
                R.string.bantuan
            )
        )
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.nav_view).visible()
    }

    private fun initAction() {
        binding.apply {
            btnCustomer.setOnClickListener {
                openWhatsApp()
            }
        }
    }

    private fun openWhatsApp() {
        val contact = "+6285156168684"
        val message = "Halo, Oleh-oleh khas Ponorogo."
        val url = "https://api.whatsapp.com/send?phone=$contact&text=${message}"
        println("Hasil app : ${isAppInstalled()}")
        if (isAppInstalled()) {
            val pm = requireContext().packageManager
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            val i = Intent(Intent.ACTION_VIEW)
            i.type = "text/plain"
            val text = "YOUR TEXT HERE"
            i.putExtra(Intent.EXTRA_TEXT, text)
            i.data = Uri.parse(url)
            startActivity(i)
        } else {
            Snackbar.make(binding.root,resources.getString(R.string.no_whatsapp) , Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun isAppInstalled(): Boolean {
        val pm: PackageManager = requireActivity().packageManager
        return try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}