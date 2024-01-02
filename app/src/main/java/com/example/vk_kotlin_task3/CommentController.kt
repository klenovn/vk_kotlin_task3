package com.example.vk_kotlin_task3

import io.bloco.faker.Faker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.Random

object CommentController {

    private val provider = CommentProvider()

//    val scope = CoroutineScope(Dispatchers.IO)
//    private fun createFakeComments(bound: Int) : MutableList<Comment> {
//        val faker = Faker()
//        val random = Random()
//        val numberOfComments = random.nextInt(bound)
//
//        var fakeComments = mutableListOf<Comment>()
//        for (i in 0 until numberOfComments) {
//            fakeComments.add(Comment(
//                id =i,
//                user = "${faker.name.firstName()}${faker.name.lastName()}",
//                content = faker.lorem.sentence(),
//                likeNumber = random.nextInt(20),
//                parentId = null
//            ))
//        }
//        fakeComments = fakeComments.sortedByParentId().toMutableList()
//
//        return fakeComments
//    }

    fun loadComments(callback: (result: List<Comment>?, error: Throwable?) -> Unit) {
        provider.createFakeComments(1, callback)
    }
}
