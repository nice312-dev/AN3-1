package ru.netology.Activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.R
import ru.netology.Utils
import ru.netology.adapter.OnInteractionListener
import ru.netology.adapter.PostAdapter
import ru.netology.databinding.FragmentPostBinding
import ru.netology.dto.Post
import ru.netology.viewModel.PostViewModel

class PostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
            ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentPostBinding.inflate(inflater, container, false)


        val id = arguments?.getLong("id") ?: 0
        viewModel.data.observe(viewLifecycleOwner) {
            val post = it.find { post -> post.id == id } ?: return@observe
            binding.contentTv.text = post.content
            binding.likeIb.text = Utils.valueUpgrade(post.numberOfLike)
            binding.videoIb.text = post.contentVideo
            binding.viewIb.text = Utils.valueUpgrade(post.numberOfView)
            binding.likeIb.isChecked = post.likedByMe
            binding.publishedTv.text = post.published
            binding.shareIb.text = Utils.valueUpgrade(post.numberOfShare)

            if (post.contentVideo == "" || !post.contentVideo.startsWith("https")) {
                binding.viewIb.visibility = View.GONE
            } else {
                binding.videoIb.visibility = View.VISIBLE
            }

            binding.likeIb.setOnClickListener {
                viewModel.likeById(post.id)
            }
            binding.shareIb.setOnClickListener {
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
            binding.menuIb.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.Remove -> {
                                viewModel.removeById(post.id)
                                findNavController().navigateUp()
                                true
                            }
                            R.id.Edit -> {
                                viewModel.edit(post)
                                val bundle = Bundle()
                                bundle.putString("text", post.content)
                                bundle.putString("video", post.contentVideo)
                                findNavController().navigate(R.id.action_postFragment_to_editPostFragment, bundle)

                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }

        return binding.root
    }
}