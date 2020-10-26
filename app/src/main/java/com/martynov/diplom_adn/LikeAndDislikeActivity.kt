package com.martynov.diplom_adn

import android.app.ProgressDialog
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.martynov.diplom_adn.adapter.LikeAndDisLikeAdapter
import com.martynov.diplom_adn.data.LikeAndDislike
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.activity_like_and_dislike.*
import kotlinx.android.synthetic.main.activity_like_and_dislike.swipeContainer
import kotlinx.android.synthetic.main.tollbar.*
import kotlinx.coroutines.launch
import splitties.toast.toast

class LikeAndDislikeActivity : AppCompatActivity() {
    private var dialog: ProgressDialog? = null
    var adapter = LikeAndDisLikeAdapter(ArrayList<LikeAndDislike>())
    var iteams = ArrayList<LikeAndDislike>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like_and_dislike)
        setSupportActionBar(toolbar)
        supportActionBar!!.setSubtitle(R.string.likes_and_dislikes)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        swipeContainer.setOnRefreshListener {
            load()
            swipeContainer.isRefreshing = false
        }


    }

    override fun onStart() {
        super.onStart()
        load()


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun load() {
        val bundle = intent.extras
        val id = bundle?.getString("likeAndDislike")?.toLong()
        lifecycleScope.launch {
            try {
                with(recyclerView) {
                    if (id != null) {
                        val result = App.repository.getIdeaId(id)
                        iteams = result.body()?.ideaIsLike as ArrayList<LikeAndDislike>
                        layoutManager = LinearLayoutManager(this@LikeAndDislikeActivity)
                        adapter = LikeAndDisLikeAdapter(iteams as MutableList<LikeAndDislike>)
                    } else {
                        toast(R.string.falien_connect)
                    }

                }
            } catch (e: Exception) {
                toast(R.string.falien_connect)
            }
        }

    }
}