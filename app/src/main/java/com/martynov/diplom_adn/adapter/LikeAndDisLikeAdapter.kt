package com.martynov.diplom_adn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.martynov.diplom_adn.R
import com.martynov.diplom_adn.convertDateToString
import com.martynov.diplom_adn.data.AttachmentType
import com.martynov.diplom_adn.data.LikeAndDislike
import com.martynov.diplom_adn.data.TypeLikeDisLike
import com.martynov.diplom_adn.data.UserType
import kotlinx.android.synthetic.main.iteam_like_dislike.view.*


class LikeAndDisLikeAdapter(val listLikeAndDislike: MutableList<LikeAndDislike>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val likeDislikeIteam =
            LayoutInflater.from(parent.context).inflate(R.layout.iteam_like_dislike, parent, false)
        return LikeDislikeHolder(this, likeDislikeIteam)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val likeDislikeIndex = position
        when (holder) {
            is LikeDislikeHolder -> holder.bind(listLikeAndDislike[likeDislikeIndex])
        }
    }

    override fun getItemCount(): Int {
        return listLikeAndDislike.size
    }


}

class LikeDislikeHolder(val adapter: LikeAndDisLikeAdapter, view: View) :
    RecyclerView.ViewHolder(view) {

    fun bind(likeDislike: LikeAndDislike) {
        with(itemView) {
            textAutor.text = likeDislike.autor.username
            textDate.text = convertDateToString(likeDislike.date)
            when (likeDislike.autor.attachment?.mediaType) {
                AttachmentType.IMAGE -> loadImageAutor(
                    imageAutor,
                    likeDislike.autor.attachment.urlUser
                )
            }
            when (likeDislike.type) {
                TypeLikeDisLike.LIKE -> {
                    imageLikdeDislike.setImageResource(R.drawable.like_active)
                }
                TypeLikeDisLike.DISLIKE -> {
                    imageLikdeDislike.setImageResource(R.drawable.dislike_active)
                }
            }
            when (likeDislike.autor.userType) {
                UserType.HATER -> {
                    textBadgeLD.text = context.getString(R.string.hater)
                    textBadgeLD.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }
                UserType.PROMOTER -> {
                    textBadgeLD.text = context.getString(R.string.promoter)
                    textBadgeLD.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
                }
            }
        }
    }

    private fun loadImageAutor(photoImg: ImageView, imageUrl: String) {
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.error_image)
        Glide.with(photoImg.context)
            .applyDefaultRequestOptions(requestOptions)
            .load(imageUrl)
            .into(photoImg)
    }

}