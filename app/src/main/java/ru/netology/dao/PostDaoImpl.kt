package ru.netology.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.dto.Post
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {

    private val formatter = DateTimeFormatter.ofPattern("dd MMMM в HH:mm ")
    val currentDate = Instant.now().atZone(ZoneId.systemDefault())

    companion object {
        val DDL = """
        CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
            ${PostColumns.COLUMN_LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_LIKES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIEWS} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARE} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIDEO} INTEGER NOT NULL DEFAULT 0,
        );
        """.trimIndent()
    }

    object PostColumns {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_LIKED_BY_ME = "likedByMe"
        const val COLUMN_LIKES = "likes"
        const val COLUMN_VIEWS = "views"
        const val COLUMN_SHARE = "share"
        const val COLUMN_VIDEO = "video"
        val ALL_COLUMNS = arrayOf(
                COLUMN_ID,
                COLUMN_AUTHOR,
                COLUMN_CONTENT,
                COLUMN_PUBLISHED,
                COLUMN_LIKED_BY_ME,
                COLUMN_LIKES,
                COLUMN_SHARE,
                COLUMN_VIDEO,
                COLUMN_VIEWS
        )
    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
                PostColumns.TABLE,
                PostColumns.ALL_COLUMNS,
                null,
                null,
                null,
                null,
                "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            if (post.id != 0L) {
                put(PostColumns.COLUMN_ID, post.id)
            }
            // TODO: remove hardcoded values
            put(PostColumns.COLUMN_AUTHOR, "Нетология. Университет интернет-профессий будущего")
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_CONTENT, post.contentVideo)
            put(PostColumns.COLUMN_PUBLISHED, formatter.format(currentDate))
        }
        val id = db.replace(PostColumns.TABLE, null, values)
        db.query(
                PostColumns.TABLE,
                PostColumns.ALL_COLUMNS,
                "${PostColumns.COLUMN_ID} = ?",
                arrayOf(id.toString()),
                null,
                null,
                null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun likeById(id: Long) {
        db.execSQL(
                """
           UPDATE posts SET
               likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
                PostColumns.TABLE,
                "${PostColumns.COLUMN_ID} = ?",
                arrayOf(id.toString())
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
                """
           UPDATE posts SET
               share = share THEN +1 END
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                    id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                    author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                    content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                    published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
                    likedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                    numberOfLike = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
                    numberOfView = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_VIEWS)),
                    numberOfShare = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARE)),
                    contentVideo = getString(getColumnIndexOrThrow(PostColumns.COLUMN_VIDEO)),
            )
        }
    }
}