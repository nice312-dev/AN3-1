package ru.netology.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.db.AppDb
import ru.netology.dto.Post
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryInMemoryImpl
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryInMemoryImpl(
            AppDb.getInstance(application).postDao
    )
    val data = repository.getAll()
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeBuId(id)

    private val formatter = DateTimeFormatter.ofPattern("dd MMMM в HH:mm ")
    val currentDate = Instant.now().atZone(ZoneId.systemDefault())


    private val empty = Post(
            id = 0,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "",
            published = formatter.format(currentDate),
            likedByMe = false,
            numberOfLike = 0,
            numberOfShare = 0,
            numberOfView = 0,
            contentVideo = ""

    )

    val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun changeContent(content: String, contentVideo: String) {
        edited.value?.let {
            val text = content.trim()
            val url = contentVideo.trim()
            if (it.content == text && it.contentVideo == url) {
                return
            }
            edited.value =
                    it.copy(content = text, contentVideo = url)
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun openPost(post: Post) {
        edited.value = post
    }

    fun cancelChange() {
        edited.value = edited.value
    }

}