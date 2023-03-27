package com.github.orkest

class Constants {
    companion object{
        private var _currentLoggedUser: String = ""
        var currentLoggedUser: String
            get() = _currentLoggedUser
            set(value) { _currentLoggedUser = value }
    }
}