package com.martynov.diplom_adn.model

import com.martynov.diplom_adn.data.AttachmentModel
import com.martynov.diplom_adn.data.AutorIdeaRequest
import com.martynov.diplom_adn.data.LikeAndDislike


data class IdeaModel(
    val id: Long = 0,
    val autor: AutorIdeaRequest,
    val date: Long = 0,
    val ideaText: String,
    val attachment: AttachmentModel? = null,
    var like: Long,
    var disLike: Long,
    var likeActionPerforming: Boolean = false,
    var DisLikeActionPerforming: Boolean = false,
    var ideaIsLike: ArrayList<LikeAndDislike> = ArrayList(),
    var isLike: Boolean = false,
    var isDisLike:Boolean = false,
    val url:String =""
)  {
    fun updateIdea(updatedModel: IdeaModel) {
        if (id != updatedModel.id) throw IllegalAccessException("Ids are different")
        like = updatedModel.like
        isLike = updatedModel.isLike
        disLike = updatedModel.disLike
        ideaIsLike = updatedModel.ideaIsLike
        isDisLike = updatedModel.isDisLike
    }
}
