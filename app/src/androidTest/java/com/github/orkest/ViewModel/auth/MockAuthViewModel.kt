package com.github.orkest.ViewModel.auth

import java.util.concurrent.CompletableFuture

class MockAuthViewModel : AuthViewModel() {

    companion object {
        const val EXISTING_USER = "exists"
        const val VALID_USER = "valid"
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

}