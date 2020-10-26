package com.martynov.diplom_adn

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.martynov.diplom_adn.adapter.IdeaAdapter
import com.martynov.diplom_adn.data.AutorIdeaRequest
import com.martynov.diplom_adn.model.IdeaModel
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.tollbar.*
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast

class FeedActivity : AppCompatActivity(), IdeaAdapter.OnLikeBtnClickListener,
    IdeaAdapter.OnDisLikeBtnClickListener, IdeaAdapter.OnViewingBtnClickListener,
    IdeaAdapter.OnAutorBtnClickListener, IdeaAdapter.OnLinkBtnClickListener {
    private var dialog: ProgressDialog? = null
    var adapter = IdeaAdapter(ArrayList<IdeaModel>())
    var iteams = ArrayList<IdeaModel>()
    var me = AutorIdeaRequest()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        val recyclerView = findViewById<RecyclerView>(R.id.container)
        title = getString(R.string.ideas)
        container.addOnChildAttachStateChangeListener(object :
            RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {

            }

            override fun onChildViewDetachedFromWindow(view: View) {
                if (recyclerView.getChildAdapterPosition(view) == 0) {
                    lifecycleScope.launch {
                        try {
                            val result = App.repository.getIdeaCount(iteams[iteams.size - 1].id)
                            if (result.isSuccessful) {
                                val rezultIteam = result.body() as ArrayList<IdeaModel>
                                iteams.addAll(rezultIteam)
                                adapter.newRecentIdea(iteams)
                                with(container) {
                                    adapter?.notifyItemRangeInserted(
                                        iteams.size,
                                        rezultIteam.size
                                    )
                                }
                            } else {
                                toast(R.string.error_occured)
                            }
                        } catch (e: Exception) {
                            toast(R.string.falien_connect)
                        }

                    }

                }

            }

        })
        setSupportActionBar(toolbar)
        fab.setOnClickListener {
            if (me.readOnlyIdea) {
                toast(getString(R.string.read_only))
            } else {
                start<CreateIdeaActivity>()
            }
        }
        swipeContainer.setOnRefreshListener {
            refreshData()
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
                val result = App.repository.getIdeaCount(-1)
                val resultme = App.repository.getMe()
                if (resultme.isSuccessful) {
                    me = resultme.body()!!
                }
                dialog?.dismiss()
                val id = intent.getStringExtra("id")?.toLong()
                if (result.isSuccessful) {
                    with(container) {
                        if (id != null) {
                            iteams.clear()
                            val iteam = App.repository.getIdeaId(id).body()
                            if (iteam != null) {
                                iteams.add(iteam)
                            } else {
                                iteams =
                                    result.body() as ArrayList<com.martynov.diplom_adn.model.IdeaModel>
                            }
                        } else {
                            iteams =
                                result.body() as ArrayList<com.martynov.diplom_adn.model.IdeaModel>
                        }
                        layoutManager = LinearLayoutManager(this@FeedActivity)
                        adapter = IdeaAdapter(iteams as MutableList<IdeaModel>).apply {
                            likeBtnClickListener = this@FeedActivity
                            disLikeBtnClickListener = this@FeedActivity
                            viewingBtnClickListener = this@FeedActivity
                            autorBtnClickListener = this@FeedActivity
                            linkBtnClickListener = this@FeedActivity
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
        lifecycleScope.launch {
            with(container) {
                if (item.isLike || item.isDisLike) {
                    splitties.toast.toast(context.getString(R.string.already_voted_for_this_idea))
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
        lifecycleScope.launch {
            with(container) {
                if (item.isDisLike || item.isLike) {
                    splitties.toast.toast(context.getString(R.string.already_voted_for_this_idea))
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

    override fun onViewingBtnClicked(iteam: IdeaModel) {
        if (iteam.ideaIsLike.size > 0) {
            //val stringInJSON = Gson().toJson(iteam.ideaIsLike)
            val intent = Intent(this@FeedActivity, LikeAndDislikeActivity::class.java)

            intent.putExtra("likeAndDislike", iteam.id.toString())
            startActivity(intent)
        } else {
            toast(getString(R.string.not_like_dislike))
        }
    }

    override fun onAutorBtnClicked(iteam: IdeaModel) {
        val index = iteams.indexOfFirst { it.id == iteam.id }
        val idea = iteams[index]
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        var ideaAutorList = ArrayList<IdeaModel>()
        for (ideaAutor in iteams) {
            if (idea.autor == ideaAutor.autor) {
                ideaAutorList.add(ideaAutor)
            }
        }

        with(container) {
            layoutManager = LinearLayoutManager(this@FeedActivity)
            adapter = IdeaAdapter(ideaAutorList as MutableList<IdeaModel>).apply {
                likeBtnClickListener = this@FeedActivity
                disLikeBtnClickListener = this@FeedActivity
                viewingBtnClickListener = this@FeedActivity
                autorBtnClickListener = this@FeedActivity
                linkBtnClickListener = this@FeedActivity

            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this@FeedActivity, UserSettingsActivity::class.java)
                startActivity(intent)
            }
            android.R.id.home -> {
                refreshData()
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                supportActionBar!!.setDisplayShowHomeEnabled(false)
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refreshData() {
        lifecycleScope.launch {
            try {
                val result = App.repository.getIdeaCount(-1)
                val resultme = App.repository.getMe()
                if (resultme.isSuccessful) {
                    me = resultme.body()!!
                }

                swipeContainer.isRefreshing = false
                if (result.isSuccessful) {
                    with(container) {
                        iteams = result.body() as ArrayList<IdeaModel>
                        layoutManager = LinearLayoutManager(this@FeedActivity)
                        adapter = IdeaAdapter(iteams as MutableList<IdeaModel>).apply {
                            likeBtnClickListener = this@FeedActivity
                            disLikeBtnClickListener = this@FeedActivity
                            viewingBtnClickListener = this@FeedActivity
                            autorBtnClickListener = this@FeedActivity
                            linkBtnClickListener = this@FeedActivity

                        }
                    }
                } else {
                    toast(R.string.error_occured)
                }

            } catch (e: Exception) {
                swipeContainer.isRefreshing = false
                toast(R.string.falien_connect)
            }
        }
    }

    override fun onLinkBtnClickListener(iteam: IdeaModel) {
        if (iteam.url != "") {
            val address: Uri = Uri.parse(iteam.url)
            val openLink = Intent(Intent.ACTION_VIEW, address)
            startActivity(Intent.createChooser(openLink, "Browser"))
        } else {
            toast(getString(R.string.no_link))
        }

    }

}