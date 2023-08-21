package com.manish.newsapp.ui.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manish.newsapp.data.util.Constants.Companion.QUERY_PAGE_SIZE
import com.manish.newsapp.data.util.Resource
import com.manish.newsapp.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment() {
    private var _newsBinding: FragmentNewsBinding? = null
    private val binding get() = _newsBinding!!

    private val viewModel: NewsViewModel by viewModels()
    private lateinit var newsListAdapter: NewsListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _newsBinding = FragmentNewsBinding.inflate(inflater, container, false)

        newsListAdapter = NewsListAdapter { article ->
            val action =
                NewsFragmentDirections.actionNavigationDetailToArticleWebViewFragment(article.url)
            findNavController().navigate(action)
        }
        initList();
       dataObserver();
        return binding.root
    }

    private fun dataObserver() {
        viewModel.articles.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsListAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.page == totalPages
                        if (isLastPage) {
                            binding.recyclerView.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { msg ->
                        Log.e("NewsFragment", "Data can't loaded -> $msg")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }

    }

    private fun initList() {
        binding.recyclerView.apply {
            adapter = newsListAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            addOnScrollListener(this@NewsFragment.listScrollListener)
        }

    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private val listScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getAllArticles()
                isScrolling = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _newsBinding = null
    }

    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

}