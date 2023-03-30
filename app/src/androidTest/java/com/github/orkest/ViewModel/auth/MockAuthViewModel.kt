package com.github.orkest.ViewModel.auth

import java.util.concurrent.CompletableFuture

class MockAuthViewModel : AuthViewModel() {

    companion object {
        val EXISTING_USER = "exists"
        val VALID_USER = "valid"
        val INVALID_PAIR = "invalid pair"
        const val EMPTY_USER = ""
        const val NO_CONNECTION = "No wifi"
    }


    @Override
    override fun createUser(): CompletableFuture<Boolean>{
        val future = CompletableFuture<Boolean>()
        val username = getUsername().text

        if (username == EXISTING_USER)
             future.complete(false)

        if (username == VALID_USER)
            future.complete((true))

        if (username == EMPTY_USER)
            future.completeExceptionally(Exception("Username cannot be empty!"))

        if (username == NO_CONNECTION)
            future.completeExceptionally(
                Exception("Sorry, something went wrong ... " +
                        "Please check your Internet connection"))

        return future
    }

    @Override
    override fun signInUser(): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        val username = getUsername().text

        if (username == EXISTING_USER)
            future.complete(true)

        if (username == INVALID_PAIR)
            future.complete((false))

        if (username == EMPTY_USER)
            future.completeExceptionally(Exception("Username cannot be empty!"))

        if (username == NO_CONNECTION)
            future.completeExceptionally(
                Exception("Sorry, something went wrong ... " +
                        "Please check your Internet connection"))

        return future
    }

}