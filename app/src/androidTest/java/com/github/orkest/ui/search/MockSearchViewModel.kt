package com.github.orkest.ui.search

import com.github.orkest.data.User
import java.util.concurrent.CompletableFuture

class MockSearchViewModel : SearchViewModel() {

    val TEST_SEARCH = "TEST"

    override fun searchUserInDatabase(user :String) : CompletableFuture<MutableList<User>> {
        val future = CompletableFuture<MutableList<User>>()
        if (user != TEST_SEARCH) future.complete(mutableListOf())
        else
            future.complete(mutableListOf(User("test"),
                User("test2"),
                User("test3"),
                User("test4")))
        return future
    }
}