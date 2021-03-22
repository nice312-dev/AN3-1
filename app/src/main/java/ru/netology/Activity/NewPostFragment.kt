package ru.netology.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.AndroidUtils
import ru.netology.databinding.FragmentNewPostBinding
import ru.netology.viewModel.PostViewModel

class NewPostFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(
            ownerProducer = ::requireParentFragment
    )


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentNewPostBinding.inflate(inflater, container, false)


        binding.cancelIv.setOnClickListener {
            with(binding.contentEt) {
                viewModel.cancelChange()
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
                findNavController().navigateUp()
            }
        }
        binding.addIv.setOnClickListener {
            if (binding.contentVideoEt.isVisible) {
                binding.contentVideoEt.visibility = View.GONE
            } else {
                binding.contentVideoEt.visibility = VISIBLE
            }
        }

        binding.contentEt.requestFocus()
        binding.contentVideoEt.requestFocus()

        binding.saveIv.setOnClickListener {
            val intent = Intent()
            if (TextUtils.isEmpty(binding.contentEt.text)) {
                activity?.setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val contentText = binding.contentEt.text.toString()
                val contentVideo = binding.contentVideoEt.text.toString()

                viewModel.changeContent(contentText, contentVideo)
                viewModel.save()
                AndroidUtils.hideKeyboard(requireView())
            }
            findNavController().navigateUp()
        }

        return binding.root
    }
}
