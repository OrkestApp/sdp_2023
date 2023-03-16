package com.github.orkest.ViewModel.auth

import java.util.concurrent.CompletableFuture

class MockAuthViewModel : AuthViewModel() {

    companion object {
        val EXISTING_USER = "exists"
        val VALID_USER = "valid"
    }


    @Override
    override fun createUser(): CompletableFuture<Boolean>{
        val future = CompletableFuture<Boolean>()
        val username = getUsername().text

        if (username == EXISTING_USER)
             future.complete(false)

        if (username == VALID_USER)
            future.complete((true))

        return future
    }

}