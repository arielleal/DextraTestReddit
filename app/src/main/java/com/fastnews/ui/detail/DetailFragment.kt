package com.fastnews.ui.detail

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.transition.TransitionInflater
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.fastnews.KEY_POST
import com.fastnews.PREFIX_HTTP
import com.fastnews.R
import com.fastnews.TEXT_PLAIN
import com.fastnews.mechanism.TimeElapsed
import com.fastnews.mechanism.VerifyNetworkInfo
import com.fastnews.service.model.CommentData
import com.fastnews.service.model.PostData
import com.fastnews.ui.web.CustomTabsWeb
import com.fastnews.viewmodel.CommentViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_detail_post.*
import kotlinx.android.synthetic.main.include_detail_post_header.*
import kotlinx.android.synthetic.main.include_detail_post_thumbnail.*
import kotlinx.android.synthetic.main.include_detail_post_title.*
import kotlinx.android.synthetic.main.include_item_timeline_ic_score.*
import kotlinx.android.synthetic.main.include_item_timeline_ic_share.*
import kotlinx.android.synthetic.main.include_item_timeline_timeleft.*


class DetailFragment : Fragment() {

    private var post: PostData? = null

    private val commentViewModel: CommentViewModel by lazy {
        ViewModelProvider(this).get(CommentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.arguments.let {
            post = it?.getParcelable(KEY_POST)
        }
        return inflater.inflate(R.layout.fragment_detail_post, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buildActionBar()
        populateUi()
    }

    private fun buildActionBar() {
        val activity = activity as AppCompatActivity

        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    private fun populateUi() {
        populateAuthor()
        populateTimeLeftValue()
        populateTitle()
        populateThumbnail()
        buildOnClickCardDetail()
        buildOnClickShare()
        populateScore()
        verifyConnectionState()
    }

    private fun verifyConnectionState() {
        context.let {
            if (VerifyNetworkInfo.isConnected(it!!)) {
                hideNoConnectionState()
                showStateProgress()
                fetchComments()
            } else {
                hideStateProgress()
                showNoConnectionState()

                state_without_conn_detail_post.setOnClickListener {
                    verifyConnectionState()
                }
            }
        }
    }

    private fun fetchComments() {
            post.let {
                commentViewModel.getComments(postId = post!!.id).observe(
                    viewLifecycleOwner,
                    Observer<List<CommentData>> { comments ->
                        comments.let {
                            populateComments(comments)
                            hideStateProgress()
                            showComments()
                        }
                    })
            }
    }

    private fun populateComments(comments: List<CommentData>) {
        if (isAdded) {
            activity?.runOnUiThread {
                detail_post_comments.removeAllViews()

                for (comment in comments) {
                    val itemReview = CommentItem.newInstance(requireActivity(), comment)
                    detail_post_comments.addView(itemReview)
                }
            }
        }
    }

    private fun showComments() {
        detail_post_comments.visibility = View.VISIBLE
    }

    private fun hideStateProgress() {
        state_progress_detail_post_comments.visibility = View.GONE
    }

    private fun showStateProgress() {
        state_progress_detail_post_comments.visibility = View.VISIBLE
    }

    private fun showNoConnectionState() {
        state_without_conn_detail_post.visibility = View.VISIBLE
    }

    private fun hideNoConnectionState() {
        state_without_conn_detail_post.visibility = View.GONE
    }

    private fun populateAuthor() {
        post?.author.let {
            item_timeline_author.text = it

            (activity as AppCompatActivity).supportActionBar?.title = it
        }
    }

    private fun populateTimeLeftValue() {
        post?.created_utc.let {
            val elapsed = TimeElapsed.getTimeElapsed(it!!.toLong())
            item_timeline_timeleft.text = elapsed
        }
    }

    private fun populateTitle() {
        post?.title.let {
            item_detail_post_title.text = it
        }
    }

    private fun populateThumbnail() {

        post?.preview?.images?.first()?.let {

            if (!TextUtils.isEmpty(it.source.url) && it.source.url.startsWith(PREFIX_HTTP)) {
                Glide.with(item_detail_post_thumbnail.context)
                    .load(HtmlCompat.fromHtml(it.source.url, HtmlCompat.FROM_HTML_MODE_LEGACY).toString())
                    .placeholder(R.drawable.ic_placeholder)
                    .fitCenter()
                    .error(ColorDrawable(Color.DKGRAY))
                    .into(item_detail_post_thumbnail)
                item_detail_post_thumbnail.visibility = View.VISIBLE
            } else {
                item_detail_post_thumbnail.visibility = View.GONE
            }
        }?: kotlin.run { item_detail_post_thumbnail.visibility = View.GONE }
    }

    private fun buildOnClickCardDetail() {
        card_detail_post.setOnClickListener {
            if (!post?.url.isNullOrEmpty()) {
                context.let {
                    val customTabsWeb = CustomTabsWeb(requireContext(), post?.url!!)
                    customTabsWeb.openUrlWithCustomTabs()
                }
            } else {
                Snackbar.make(
                    item_detail_post_thumbnail,
                    R.string.error_detail_post_url,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun  buildOnClickShare() {
        val url = post?.url
        item_timeline_bt_share.setOnClickListener {

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, url)
                type = TEXT_PLAIN
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        }
    }

    private fun populateScore() {
        post?.score.let {
            item_timeline_bt_score_text.text = it.toString()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }
}