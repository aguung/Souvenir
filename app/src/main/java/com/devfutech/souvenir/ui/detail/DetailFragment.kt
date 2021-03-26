package com.devfutech.souvenir.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.devfutech.souvenir.R
import com.devfutech.souvenir.databinding.FragmentDetailBinding
import com.devfutech.souvenir.ui.MainActivity
import com.devfutech.souvenir.utils.gone
import com.devfutech.souvenir.utils.toast
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
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAction()
    }

    private fun initView() {
        (activity as AppCompatActivity).toogleActionbar(
            show = false,
            back = false,
            title = args.souvenir?.souvenirName
        )
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.nav_view).visible()
        binding.apply {
            tvNama.text = args.souvenir?.souvenirName
            tvAlamat.text = args.souvenir?.souvenirAddress
            tvDekripsi.text = args.souvenir?.souvenirDescription
            tvOpen.text = if (args.souvenir?.souvenirOpenClosed == "") {
                "-"
            } else {
                args.souvenir?.souvenirOpenClosed
            }
            tvTelp.text = if (args.souvenir?.souvenirTelp == "") {
                "-"
            } else {
                args.souvenir?.souvenirTelp
            }
            imageSouvenir.load(args.souvenir?.souvenirImageUrl) {
                crossfade(true)
                error(R.drawable.icon)
            }
        }
    }

    private fun initAction() {
        binding.apply {
            btnPetunjuk.setOnClickListener {
                if (args.souvenir?.souvenirLatLong == null || args.souvenir?.souvenirLatLong == "") {
                    requireContext().toast("Koordinat toko tidak tersedia")
                } else {
                    findNavController().navigate(
                        DetailFragmentDirections.actionDetailFragmentToMapFragment(
                            souvenir = args.souvenir
                        )
                    )
                }
            }
        }
    }
}