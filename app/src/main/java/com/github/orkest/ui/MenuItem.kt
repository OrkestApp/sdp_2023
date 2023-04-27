package com.github.orkest.ui

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val id: String,
    val title: String,
    val contentDescription: String? = null, // for people with visual impairments
    val icon: ImageVector
)