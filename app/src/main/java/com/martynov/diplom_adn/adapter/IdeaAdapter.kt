package com.martynov.diplom_adn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.martynov.diplom_adn.R
import com.martynov.diplom_adn.convertDateToString
import com.martynov.diplom_adn.data.AttachmentType
import com.martynov.diplom_adn.model.IdeaModel
import kotlinx.android.synthetic.main.iteam_idea.view.*

class IdeaAdapter(val listIdea: MutableList<IdeaModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val ideaView =
            LayoutInflater.from(parent.context).inflate(R.layout.iteam_idea, parent, false)
        return IdeaViewHolder(this, ideaView)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ideaIndex = position
        when (holder) {
            is IdeaViewHolder -> holder.bind(listIdea[ideaIndex])
        }
    }


    override fun getItemCount(): Int {
        return listIdea.size
    }

}

class IdeaViewHolder(val adapter: IdeaAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {

    }

    fun bind(idea: IdeaModel) {
        with(itemView) {
            nameAutor.text = idea.autor.username
            when (idea.autor.attachment?.mediaType) {
                AttachmentType.IMAGE -> loadImageAutor(imageAutor, idea.autor.attachment.urlUser)
            }
            textDate.text = convertDateToString(idea.date)
            textIdea.text = idea.ideaText

            when (idea.attachment?.mediaType) {
                AttachmentType.IMAGE -> loadImage(imageIdea, idea.attachment.url)
            }


        }

    }

    private fun loadImage(photoImg: ImageView, imageUrl: String) {
        Glide.with(photoImg.context)
            .load(imageUrl)
            .into(photoImg)
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