package com.example.vk_kotlin_task3

import io.bloco.faker.Faker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Random

class  CommentProvider {
    val scope = CoroutineScope(Dispatchers.IO)


    fun createFakeComments(bound: Int, callback: (result: List<Comment>?, error: Throwable?) -> Unit) {
        scope.launch {
            delay(DELAY)
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

            withContext(Dispatchers.Main) {
                callback(fakeComments, null)
            }
        }
    }

    companion object {
        const val DELAY = 3000L
    }
}