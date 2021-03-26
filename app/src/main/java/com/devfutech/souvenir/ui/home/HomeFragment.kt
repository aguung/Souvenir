package com.devfutech.souvenir.ui.home

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.devfutech.souvenir.R
import com.devfutech.souvenir.adapter.souvenir.SouvenirAdapter
import com.devfutech.souvenir.adapter.souvenir.SouvenirLoadStateAdapter
import com.devfutech.souvenir.data.local.entity.Souvenir
import com.devfutech.souvenir.databinding.FragmentHomeBinding
import com.devfutech.souvenir.ui.MainActivity
import com.devfutech.souvenir.utils.gone
import com.devfutech.souvenir.utils.toogleActionbar
import com.devfutech.souvenir.utils.visible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import java.util.*


@ExperimentalPagingApi
@AndroidEntryPoint
class HomeFragment : Fragment(),SouvenirAdapter.OnItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var souvenirAdapter: SouvenirAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAdapter()
        initViewModel()
    }

    private fun initView() {
        (activity as AppCompatActivity).toogleActionbar(
            show = true,
            back = false,
            title = resources.getString(R.string.app_name)
        )
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.nav_view).visible()
    }

    private fun initAdapter() {
        souvenirAdapter = SouvenirAdapter(this)
        binding.apply {
            rvSouvenir.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = souvenirAdapter.withLoadStateHeaderAndFooter(
                    header = SouvenirLoadStateAdapter { souvenirAdapter::retry },
                    footer = SouvenirLoadStateAdapter { souvenirAdapter::retry }
                )
                itemAnimator = DefaultItemAnimator()
            }

            btnRetry.setOnClickListener {
                souvenirAdapter.retry()
                lytError.gone()
                shimmerFrameLayout.visible()
            }
            souvenirAdapter.addLoadStateListener {
                shimmerFrameLayout.showShimmer(it.refresh is LoadState.Loading)
                if (it.refresh is LoadState.Error && souvenirAdapter.itemCount < 1) {
                    lytError.visible()
                    shimmerFrameLayout.gone()
                    tvMessage.text = (it.refresh as? LoadState.Error)?.error?.message
                } else if (it.source.refresh is LoadState.NotLoading && it.append.endOfPaginationReached) {
                    lytError.gone()
                    shimmerFrameLayout.gone()
                    rvSouvenir.visible()
                } else if (souvenirAdapter.itemCount > 0){
                    shimmerFrameLayout.gone()
                }else{
                    lytError.gone()
                    shimmerFrameLayout.visible()
                }
            }
        }
    }



    private fun initViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.souvenir.collectLatest {
                souvenirAdapter.submitData(it)
            }

            souvenirAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect {
                    binding.rvSouvenir.scrollToPosition(0)
                }
        }
    }

    override fun onItemClick(items: Souvenir?) {
        findNavController().navigate(
            HomeFragmentDirections.actionNavigationHomeToDetailFragment(
                souvenir = items
            )
        )
    }
}