package com.martynov.diplom_adn.model

import com.martynov.diplom_adn.data.AttachmentModel
import com.martynov.diplom_adn.data.AutorIdeaRequest

data class IdeaModel (
    val id: Long = 0,
    val autor: AutorIdeaRequest,
    val date: Long = 0,
    val ideaText:String,
    val attachment: AttachmentModel? = null
)