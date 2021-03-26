package com.devfutech.souvenir.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.devfutech.souvenir.R
import com.devfutech.souvenir.databinding.FragmentMapBinding
import com.devfutech.souvenir.ui.MainActivity
import com.devfutech.souvenir.utils.toogleActionbar
import com.devfutech.souvenir.utils.visible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : Fragment() {

    private lateinit var binding:FragmentMapBinding
    private val args:MapFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView(){
        (activity as AppCompatActivity).toogleActionbar(show = true, back = false, title = resources.getString(R.string.peta))
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.nav_view).visible()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private val callback = OnMapReadyCallback { googleMap ->
            val location = args.souvenir?.souvenirLatLong?.split(',')!!
            val map = LatLng(location[0].toDouble(), location[1].toDouble())
            googleMap.addMarker(MarkerOptions().position(map).title(args.souvenir?.souvenirName))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(map,16f))
    }
}