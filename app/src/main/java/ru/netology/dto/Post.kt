package ru.netology.dto

data class Post(
   val id: Long,
   val author: String,
   val content: String,
   val contentVideo: String = "",
   val published: String,
   val likedByMe: Boolean = false,
   val numberOfLike: Int,
   val numberOfView: Int,
   val numberOfShare: Int
)