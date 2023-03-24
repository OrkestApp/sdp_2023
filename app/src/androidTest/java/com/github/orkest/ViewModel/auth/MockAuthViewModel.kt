package com.github.orkest.ViewModel.auth

import java.util.concurrent.CompletableFuture

class MockAuthViewModel : AuthViewModel() {

    companion object {
        val EXISTING_USER = "exists"
        val VALID_USER = "valid"
        val NO_PERMISSIONS = "no_permissions"
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

    @Override
    override fun signInUser(): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        val username = getCurrentUsername().text

        if (username == EXISTING_USER)
            future.complete(true)

        if (username == NO_PERMISSIONS)
            future.complete((false))

        return future
    }

}