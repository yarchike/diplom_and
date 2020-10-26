package com.martynov.diplom_adn.data


enum class TypeLikeDisLike {
    LIKE, DISLIKE
}

data class LikeAndDislike(val autor: AutorIdeaRequest, val date: Long, val type: TypeLikeDisLike)