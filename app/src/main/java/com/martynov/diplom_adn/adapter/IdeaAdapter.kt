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
import com.martynov.diplom_adn.data.UserType
import com.martynov.diplom_adn.model.IdeaModel
import kotlinx.android.synthetic.main.iteam_idea.view.*
import splitties.toast.toast

class IdeaAdapter(val listIdea: MutableList<IdeaModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var likeBtnClickListener: OnLikeBtnClickListener? = null
    var disLikeBtnClickListener: OnDisLikeBtnClickListener? = null
    var viewingBtnClickListener: OnViewingBtnClickListener? = null
    var autorBtnClickListener: OnAutorBtnClickListener? = null
    var linkBtnClickListener: OnLinkBtnClickListener? = null


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

    interface OnLikeBtnClickListener {
        fun onLikeBtnClicked(item: IdeaModel, position: Int)
    }

    interface OnDisLikeBtnClickListener {
        fun onDisLikeBtnClicked(item: IdeaModel, position: Int)
    }

    interface OnViewingBtnClickListener {
        fun onViewingBtnClicked(iteam: IdeaModel)
    }

    interface OnAutorBtnClickListener {
        fun onAutorBtnClicked(iteam: IdeaModel)
    }

    fun newRecentIdea(list: List<IdeaModel>) {
        this.listIdea.clear()
        this.listIdea.addAll(list)
    }

    interface OnLinkBtnClickListener {
        fun onLinkBtnClickListener(iteam: IdeaModel)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        when (holder) {
            is IdeaViewHolder ->
                if (!payloads.isEmpty()) {
                    when (payloads[0]) {
                        Payload.LIKE_CHANGE -> {
                            holder.bindLike(listIdea[position])
                        }
                        Payload.DISLIKE_CHANGE -> {
                            holder.binddisLike(listIdea[position])
                        }

                    }

                } else {
                    super.onBindViewHolder(holder, position, payloads)
                }
        }


    }

    class IdeaViewHolder(val adapter: IdeaAdapter, view: View) : RecyclerView.ViewHolder(view) {
        init {
            with(itemView) {

                imageLike.setOnClickListener {
                    val currentPosition = adapterPosition
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        val item = adapter.listIdea[currentPosition]
                        if (item.likeActionPerforming) {
                            context.toast(context.getString(R.string.like_in_progress))
                        }
                        adapter.likeBtnClickListener?.onLikeBtnClicked(
                            item,
                            currentPosition
                        )
                    }
                }
                imageDisLike.setOnClickListener {
                    val currentPosition = adapterPosition
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        val item = adapter.listIdea[currentPosition]
                        if (item.likeActionPerforming) {
                            context.toast(context.getString(R.string.like_in_progress))
                        }
                        adapter.disLikeBtnClickListener?.onDisLikeBtnClicked(
                            item,
                            currentPosition
                        )
                    }
                }
                imageViewing.setOnClickListener {
                    val currentPosition = adapterPosition
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        val item = adapter.listIdea[currentPosition]
                        adapter.viewingBtnClickListener?.onViewingBtnClicked(item)
                    }

                }
                imageAutor.setOnClickListener {
                    val currentPosition = adapterPosition
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        val item = adapter.listIdea[currentPosition]
                        adapter.autorBtnClickListener?.onAutorBtnClicked(item)
                    }
                }
                imageLink.setOnClickListener {
                    val currentPosition = adapterPosition
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        val item = adapter.listIdea[currentPosition]
                        adapter.linkBtnClickListener?.onLinkBtnClickListener(item)
                    }
                }
            }

        }

        fun bind(idea: IdeaModel) {
            with(itemView) {
                nameAutor.text = idea.autor.username
                when (idea.autor.userType) {
                    UserType.HATER -> {
                        textBadge.text = context.getString(R.string.hater)
                        textBadge.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                    }
                    UserType.PROMOTER -> {
                        textBadge.text = context.getString(R.string.promoter)
                        textBadge.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
                    }
                }
                when (idea.autor.attachment?.mediaType) {
                    AttachmentType.IMAGE -> loadImageAutor(
                        imageAutor,
                        idea.autor.attachment.urlUser
                    )
                }
                textDate.text = convertDateToString(idea.date)
                textIdea.text = idea.ideaText
                textLike.text = idea.like.toString()
                textDisLike.text = idea.disLike.toString()
                when {
                    idea.url != "" -> imageLink.setImageResource(R.drawable.ic_active_link)
                }

                when {
                    idea.likeActionPerforming -> imageLike.setImageResource(R.drawable.like_wating)
                    idea.isLike -> {
                        imageLike.setImageResource(R.drawable.like_active)
                        textLike.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
                    }

                    else -> {
                        imageLike.setImageResource(R.drawable.like_no_active)
                        textLike.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
                    }
                }
                when {
                    idea.DisLikeActionPerforming -> imageDisLike.setImageResource(R.drawable.dislike_wating)
                    idea.isDisLike -> {
                        imageDisLike.setImageResource(R.drawable.dislike_active)
                        textDisLike.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                    }
                    else -> {
                        imageDisLike.setImageResource(R.drawable.dislike_no_active)
                        textDisLike.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorBlack
                            )
                        )
                    }
                }
                if (idea.attachment != null) {
                    when (idea.attachment.mediaType) {
                        AttachmentType.IMAGE -> loadImage(imageIdea, idea.attachment.url)
                    }
                } else {
                    imageIdea.setImageResource(0)
                }


            }

        }

        fun bindLike(idea: IdeaModel) {
            with(itemView) {
                imageLike.setImageResource(R.drawable.like_active)
                textLike.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
                textLike.text = idea.like.toString()

            }
        }

        fun binddisLike(idea: IdeaModel) {
            with(itemView) {
                imageDisLike.setImageResource(R.drawable.dislike_active)
                textDisLike.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                textDisLike.text = idea.disLike.toString()
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
}