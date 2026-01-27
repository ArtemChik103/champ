package com.example.lol.Projects

import java.util.UUID

/**
 * Data class representing a project.
 */
data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val recipient: String = "",
    val descriptionSource: String = "",
    val category: String = "",
    val imageUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
