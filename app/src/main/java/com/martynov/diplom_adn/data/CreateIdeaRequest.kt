package com.martynov.diplom_adn.data


data class CreateIdeaRequest(
    val id: Long = 0,
    val date: Long,
    val ideaText: String,
    val attachment: AttachmentModel? = null,
    val like: Long,
    val disLike: Long,
    val url: String = ""
)