package ru.netology.Activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.AndroidUtils
import ru.netology.R
import ru.netology.adapter.OnInteractionListener
import ru.netology.adapter.PostAdapter
import ru.netology.databinding.FragmentFeedBinding
import ru.netology.dto.Post
import ru.netology.viewModel.PostViewModel

class FeedFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentFeedBinding.inflate(inflater, container, false)


        val adapter = PostAdapter(object : OnInteractionListener {

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.shooser_intent_post))
                startActivity(shareIntent)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                val bundle = Bundle()
                bundle.putString("text", post.content)
                bundle.putString("video", post.contentVideo)
                findNavController().navigate(R.id.action_feedFragment_to_editPostFragment, bundle)
            }

            override fun onCancelEdit(post: Post) {
                viewModel.cancelChange()

            }

            override fun onOpenPost(post: Post) {
                viewModel.openPost(post)
                val bundle = Bundle()
                bundle.putLong("id", post.id)
//
                findNavController().navigate(R.id.action_feedFragment_to_postFragment, bundle)
            }

        })
        binding.listPost.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { post ->
            adapter.submitList(post)

        }
        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id == 0L) {
                return@observe
            }
        }

        binding.addPostView.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        return binding.root
    }

}



