package com.example.vk_kotlin_task3

import android.os.Parcel
import android.os.Parcelable
import io.bloco.faker.Faker
import java.util.Random

data class Comment(val id: Int, val user: String?, val content: String?, var likeNumber: Int, val parentId: Int?, var is_liked: Boolean = false) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(user)
        parcel.writeString(content)
        parcel.writeInt(likeNumber)
        parcel.writeValue(parentId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Comment> {
        override fun createFromParcel(parcel: Parcel): Comment {
            return Comment(parcel)
        }

        override fun newArray(size: Int): Array<Comment?> {
            return arrayOfNulls(size)
        }
    }
}

public fun createFakeComments(bound: Int) : MutableList<Comment> {
    val faker = Faker()
    val random = Random()
    val numberOfComments = random.nextInt(bound)

    var fakeComments = mutableListOf<Comment>()
    for (i in 0 until numberOfComments) {
        fakeComments.add(Comment(
            id =i,
            user = "${faker.name.firstName()}${faker.name.lastName()}",
            content = faker.lorem.sentence(),
            likeNumber = random.nextInt(20),
            parentId = null
        ))
    }
    fakeComments = fakeComments.sortedByParentId().toMutableList()

    return fakeComments
}

public fun createFakeComment(id: Int) {
    val faker = Faker()
    val random = Random()
    val comment = Comment(
        id=id,
        user = "${faker.name.firstName()}${faker.name.lastName()}",
        content = faker.lorem.sentence(),
        likeNumber = random.nextInt(20),
        parentId = null
    )
}


public fun MutableList<Comment>.sortedByParentId() : List<Comment> {
    val sortedList = mutableListOf<Comment>()

    val topLevelComments = filter { it.parentId == null }
    topLevelComments.forEach { parentComment ->
        sortedList.add(parentComment)
        val children = filter { it.parentId == parentComment.id }
        sortedList.addAll(children)
    }

    val remainingComments = filter { it.parentId != null && !sortedList.contains(it) }
    remainingComments.forEach { comment ->
        sortedList.add(comment)
        val children = filter { it.parentId == comment.id }
        sortedList.addAll(children)
    }

    return sortedList
}