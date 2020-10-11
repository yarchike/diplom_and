package com.martynov.diplom_adn

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.martynov.diplom_adn.adapter.IdeaAdapter
import com.martynov.diplom_adn.model.IdeaModel
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast

class FeedActivity : AppCompatActivity(), IdeaAdapter.OnLikeBtnClickListener, IdeaAdapter.OnDisLikeBtnClickListener {
    private var dialog: ProgressDialog? = null
    var adapter = IdeaAdapter(ArrayList<IdeaModel>())
    var iteams = ArrayList<IdeaModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        fab.setOnClickListener {
            start<CreateIdeaActivity>()
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            dialog = ProgressDialog(this@FeedActivity).apply {
                setMessage(getString(R.string.please_wait))
                setTitle(getString(R.string.loading_ideas))
                setCancelable(false)
                setProgressBarIndeterminate(true)
                show()
            }
            try {
                val result = App.repository.getIdea()
                dialog?.dismiss()
                if (result.isSuccessful) {
                    with(container) {
                        iteams = result.body() as ArrayList<IdeaModel>
                        layoutManager = LinearLayoutManager(this@FeedActivity)
                        adapter = IdeaAdapter(iteams as MutableList<IdeaModel>).apply {
                            likeBtnClickListener = this@FeedActivity
                            disLikeBtnClickListener = this@FeedActivity
                        }
                    }
                } else {
                    toast(R.string.error_occured)
                }
            } catch (e: Exception) {
                dialog?.dismiss()
                toast(R.string.falien_connect)
            }

        }
    }

    override fun onLikeBtnClicked(item: IdeaModel, position: Int) {
        toast("Лайк")
        lifecycleScope.launch {
            with(container) {
                if (item.isLike) {
                    splitties.toast.toast(context.getString(R.string.like_is))
                } else {
                    item.likeActionPerforming = true
                    adapter?.notifyItemChanged(position)
                    val response = App.repository.like(item.id)
                    item.likeActionPerforming = false
                    if (response.isSuccessful) {
                        item.updateIdea(response.body()!!)
                    }
                }
                adapter?.notifyItemChanged(position)
            }
        }
    }

    override fun onDisLikeBtnClicked(item: IdeaModel, position: Int) {
        toast("Дизлайк")
        lifecycleScope.launch {
            with(container) {
                if (item.isDisLike) {
                    splitties.toast.toast(context.getString(R.string.dis_like_is))
                } else {
                    item.DisLikeActionPerforming = true
                    adapter?.notifyItemChanged(position)
                    val response = App.repository.disLike(item.id)
                    item.DisLikeActionPerforming = false
                    if (response.isSuccessful) {
                        item.updateIdea(response.body()!!)
                    }
                }
                adapter?.notifyItemChanged(position)
            }
        }
    }
}