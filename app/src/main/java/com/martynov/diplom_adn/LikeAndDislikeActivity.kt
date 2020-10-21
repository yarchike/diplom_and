package com.martynov.diplom_adn

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.martynov.diplom_adn.adapter.LikeAndDisLikeAdapter
import com.martynov.diplom_adn.data.LikeAndDislike
import com.martynov.diplom_adn.model.IdeaModel
import kotlinx.android.synthetic.main.activity_like_and_dislike.*
import kotlinx.android.synthetic.main.iteam_like_dislike.view.*
import kotlinx.android.synthetic.main.tollbar.*
import splitties.toast.toast
import java.io.File

class LikeAndDislikeActivity : AppCompatActivity() {
    private var dialog: ProgressDialog? = null
    var adapter = LikeAndDisLikeAdapter(ArrayList<LikeAndDislike>())
    var iteams = ArrayList<LikeAndDislike>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like_and_dislike)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)


    }

    override fun onStart() {
        super.onStart()
        val bundle = intent.extras
        val stringOutJson = bundle?.getString("likeAndDislike")
        val type = object : TypeToken<List<LikeAndDislike>>() {}.type
        val likeAndDislike: List<LikeAndDislike> = Gson().fromJson<List<LikeAndDislike>>(stringOutJson, type)
        with(recyclerView){
            iteams = likeAndDislike as ArrayList<LikeAndDislike>
            layoutManager =  LinearLayoutManager(this@LikeAndDislikeActivity)
            adapter = LikeAndDisLikeAdapter(iteams as MutableList<LikeAndDislike>)
        }

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
}