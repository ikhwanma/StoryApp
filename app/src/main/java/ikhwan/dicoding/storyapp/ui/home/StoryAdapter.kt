package ikhwan.dicoding.storyapp.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ikhwan.dicoding.storyapp.R
import ikhwan.dicoding.storyapp.databinding.ItemStoryBinding
import ikhwan.dicoding.storyapp.model.Story

class StoryAdapter : PagingDataAdapter<Story, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    var onItemClick: ((Story) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)!!
        holder.bind(data)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemStoryBinding.bind(itemView)
        fun bind(data: Story) {
            with(binding) {
                tvMovie.text = data.name
                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .into(imgStory)
                root.setOnClickListener {
                    onItemClick?.invoke(data)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}