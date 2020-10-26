package com.martynov.diplom_adn.data


data class AutorIdeaRequest(
    val id: Long? = 0,
    val username: String? = null,
    val attachment: AttachmentModel? = null,
    val readOnlyIdea: Boolean = false,
    val userType: UserType = UserType.NORMAL
)