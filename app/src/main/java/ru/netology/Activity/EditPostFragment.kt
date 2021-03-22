package ru.netology.Activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.AndroidUtils
import ru.netology.R
import ru.netology.databinding.FragmentEditPostBinding
import ru.netology.viewModel.PostViewModel

class EditPostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
            ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentEditPostBinding.inflate(inflater, container, false)


        binding.cancelIv.setOnClickListener {

            with(binding.contentEt) {
                viewModel.cancelChange()
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
                findNavController().navigateUp()
            }
        }

        binding.contentEt.setText(arguments?.getString("text"))
        binding.contentVideoEt.setText(arguments?.getString("video"))




        binding.contentVideoEt.requestFocus()
        binding.contentEt.requestFocus()
        binding.saveIv.setOnClickListener {
            val contentText = binding.contentEt.text.toString()
            val contentVideo = binding.contentVideoEt.text.toString()
            viewModel.changeContent(contentText, contentVideo)
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigate(R.id.action_editPostFragment_to_feedFragment)
        }

        return binding.root
    }

}

