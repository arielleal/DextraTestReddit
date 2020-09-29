package com.fastnews.ui.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.fastnews.EMPTY
import com.fastnews.KEY_POST
import com.fastnews.R
import com.fastnews.THUMBNAIL
import com.fastnews.mechanism.VerifyNetworkInfo
import com.fastnews.service.model.PostChildrenAfter
import com.fastnews.service.model.PostData
import com.fastnews.viewmodel.PostViewModel
import kotlinx.android.synthetic.main.fragment_timeline.*


class TimelineFragment : Fragment() {

    private val viewModel: PostViewModel by lazy {
        ViewModelProvider(this).get(PostViewModel::class.java)
    }

    private lateinit var adapter: TimelineAdapter
    private var after = EMPTY
    private var isLoading: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buildActionBar()
        buildTimeline()
        verifyConnectionState()
    }

    private fun verifyConnectionState() {
        context.let {
            if (VerifyNetworkInfo.isConnected(it!!)) {
                hideNoConnectionState()
                showProgress()
                fetchTimeline()
            } else {
                hideProgress()
                showNoConnectionState()

                state_without_conn_timeline.setOnClickListener {
                    verifyConnectionState()
                }
            }
        }
    }

    private fun buildActionBar() {
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(false) // disable the button
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false) // remove the left caret
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.app_name)
    }

    private fun buildTimeline() {
        adapter = TimelineAdapter { it, imageView ->
            onClickItem(it, imageView)
        }

        val layoutManager = LinearLayoutManager(context)
        timeline_rv.layoutManager = layoutManager
        timeline_rv.itemAnimator = DefaultItemAnimator()
        timeline_rv.adapter = adapter

        timeline_rv.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                fetchTimeline()
            }
        })
    }

    private fun fetchTimeline() {
        viewModel.getPosts(after, 20).observe(viewLifecycleOwner, Observer<PostChildrenAfter> { posts ->
            posts.let {
                after = it.after
                isLoading = false
                adapter.setData(it.listPostData)
                hideProgress()
                showPosts()
            }
        })
    }

    private fun showPosts() {
        timeline_rv.visibility = View.VISIBLE
    }

    private fun showProgress() {
        state_progress_timeline.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        state_progress_timeline.visibility = View.GONE
    }

    private fun showNoConnectionState() {
        state_without_conn_timeline.visibility = View.VISIBLE
    }

    private fun hideNoConnectionState() {
        state_without_conn_timeline.visibility = View.GONE
    }

    private fun onClickItem(postData: PostData, imageView: ImageView) {
        val extras = FragmentNavigatorExtras(
            imageView to THUMBNAIL
        )
        val bundle = Bundle()
        bundle.putParcelable(KEY_POST, postData)
        after = EMPTY
        findNavController().navigate(R.id.action_timeline_to_detail, bundle, null, extras)
    }
}