package com.devfutech.souvenir.ui.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.devfutech.souvenir.BuildConfig
import com.devfutech.souvenir.R
import com.devfutech.souvenir.databinding.FragmentInfoBinding
import com.devfutech.souvenir.ui.MainActivity
import com.devfutech.souvenir.utils.toogleActionbar
import com.devfutech.souvenir.utils.visible
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        (activity as AppCompatActivity).toogleActionbar(
            show = true, back = false, title = resources.getString(
                R.string.info
            )
        )
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.nav_view).visible()
        binding.tvVersion.text = resources.getString(R.string.versi,BuildConfig.VERSION_NAME)
    }

}