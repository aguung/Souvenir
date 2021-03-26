package com.devfutech.souvenir.ui.search

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devfutech.souvenir.R
import com.devfutech.souvenir.adapter.CategoryAdapter
import com.devfutech.souvenir.adapter.SouvenirListAdapter
import com.devfutech.souvenir.data.local.entity.Souvenir
import com.devfutech.souvenir.databinding.FragmentSearchBinding
import com.devfutech.souvenir.ui.MainActivity
import com.devfutech.souvenir.utils.toogleActionbar
import com.devfutech.souvenir.utils.visible
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest


@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : Fragment(), CategoryAdapter.OnItemClickListener{

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var souvenirListAdapter: SouvenirListAdapter
    private var type = "Semua"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAdapter()
        initViewModel()
        initSearch()
    }

    private fun initView() {
        (activity as AppCompatActivity).toogleActionbar(
            show = true, back = false, title = resources.getString(
                R.string.cari
            )
        )
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.nav_view).visible()
    }

    private fun initAdapter() {
        categoryAdapter = CategoryAdapter(this)
        souvenirListAdapter = SouvenirListAdapter(this@SearchFragment::onItemSouvenirClick)
        binding.apply {
            rvCategory.apply {
                adapter = categoryAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
            }
            rvDataSouvenir.apply {
                adapter = souvenirListAdapter
                layoutManager = LinearLayoutManager(activity)
                setHasFixedSize(true)
            }
        }
    }

    private fun initViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.category.collectLatest {
                val list: MutableList<Souvenir> = it.toMutableList()
                list.add(
                    Souvenir(
                        souvenirAddress = null,
                        souvenirCategory = "Semua",
                        souvenirCode = "S000",
                        souvenirDescription = null,
                        souvenirImageUrl = null,
                        souvenirLatLong = null,
                        souvenirName = null,
                        souvenirOpenClosed = null,
                        souvenirTelp = null
                    )
                )
                categoryAdapter.submitList(list.reversed())
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.souvenirFlow.collectLatest {
                souvenirListAdapter.submitList(it)
                binding.rvDataSouvenir.smoothScrollToPosition(0)
            }
        }
    }

    private fun initSearch() {
        binding.input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updatedSouvenirFromInput()
                true
            } else {
                false
            }
        }
        binding.input.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updatedSouvenirFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updatedSouvenirFromInput() {
        binding.input.text.trim().toString().also {
            if (viewModel.shouldShowSearch(subsearch = it)) {
                hideSoftKeyboard(binding.input)
                viewModel.showSearch(type = type, search = "%${it}%")
            }
        }
    }

    private fun hideSoftKeyboard(view: View) {
        val imm: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onItemCategoryClick(items: Souvenir) {
        type = items.souvenirCategory!!
        if (viewModel.shouldShowType(type = type)) {
            viewModel.showSearch(type = type, search = "%${binding.input.text}%")
        }
    }


    private fun onItemSouvenirClick(items: Souvenir?) {
        findNavController().navigate(
            SearchFragmentDirections.actionNavigationSearchToDetailFragment(
                souvenir = items
            )
        )
    }

}