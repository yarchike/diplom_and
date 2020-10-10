package com.martynov.diplom_adn

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.martynov.diplom_adn.adapter.IdeaAdapter
import com.martynov.diplom_adn.model.IdeaModel
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast

class FeedActivity : AppCompatActivity() {
    private var dialog : ProgressDialog? = null
    var adapter = IdeaAdapter(ArrayList<IdeaModel>())
    var iteams = ArrayList<IdeaModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        fab.setOnClickListener{
            start<CreateIdeaActivity>()
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
         dialog = ProgressDialog(this@FeedActivity).apply{
             setMessage(getString(R.string.please_wait))
             setTitle(getString(R.string.loading_ideas))
             setCancelable(false)
             setProgressBarIndeterminate(true)
             show()
         }
            val result = App.repository.getIdea()
            dialog?.dismiss()
            if(result.isSuccessful){
                with(container){
                    iteams = result.body() as ArrayList<IdeaModel>
                    layoutManager = LinearLayoutManager(this@FeedActivity)
                    adapter = IdeaAdapter(iteams as MutableList<IdeaModel>)
                }
            }else{
                toast(R.string.error_occured)
            }

        }
    }
}