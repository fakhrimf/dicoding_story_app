package com.fakhrimf.storyapp.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.fakhrimf.storyapp.StoryDetailActivity
import com.fakhrimf.storyapp.databinding.StoryItemBinding
import com.fakhrimf.storyapp.model.PostModel
import com.fakhrimf.storyapp.utils.STORY_INTENT
import com.squareup.picasso.Picasso
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import com.fakhrimf.storyapp.utils.APP_IMAGE
import com.fakhrimf.storyapp.utils.STORY_IMAGE

class HomeRecyclerAdapter(val posts: ArrayList<PostModel>, val context: Context) : RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding_ = binding
    }

    override fun toString(): String {
        return ""
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            StoryItemBinding.inflate (
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bind = holder.binding_
        val post = posts[position]
        ViewCompat.setTransitionName(bind.ivStory, STORY_IMAGE)
        Picasso.get().load(post.photoUrl).into(bind.ivStory)
        bind.tvJudul.text = post.name
        bind.tvIsi.text = post.description
        bind.cvStory.setOnClickListener {
            val intent = Intent(context, StoryDetailActivity::class.java)
            val p1 = Pair.create(bind.ivStory as View, STORY_INTENT)
            val opt = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context as Activity,
                p1
            )
            intent.putExtra(STORY_INTENT, post)
            context.startActivity(intent, opt.toBundle())
        }
    }
}