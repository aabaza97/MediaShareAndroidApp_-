package com.example.myapplication2.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication2.databinding.ContentCardItemBinding
import com.example.myapplication2.model.ContentCard

class ContentCardAdapter(
    private val contentCards: List<ContentCard>
) : RecyclerView.Adapter<ContentCardAdapter.ContentCardViewHolder>() {

    private var content = mutableListOf<ContentCard>()

    inner class ContentCardViewHolder(
        private val binding: ContentCardItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contentCard: ContentCard) {
            binding.titleTextView.text = contentCard.title

            // replace localhost in mediaURL with the actual IP address
            val mediaUrl = contentCard.mediaUrl.replace("localhost", "10.0.2.2")

            // Load image or video thumbnail
            Glide.with(binding.root.context)
                .load(mediaUrl)
                .into(binding.mediaImageView)

            // Like button toggle
            binding.likeButton.isChecked = contentCard.isLiked
            Log.d("ContentCardAdapter", "isLiked: ${contentCard.isLiked}")
            binding.likeButton.setOnClickListener {
                if (contentCard.isLiked) {
                    contentCard.disLike(contentCard.id)
                } else {
                    contentCard.like(contentCard.id)
                }

                contentCard.isLiked = !contentCard.isLiked
                binding.likeButton.isChecked = contentCard.isLiked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentCardViewHolder {
        val binding = ContentCardItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContentCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContentCardViewHolder, position: Int) {
        holder.bind(content[position])
    }

    override fun getItemCount() = content.size

    fun updateContentCards(newContentCards: List<ContentCard>) {
        // Calculate differences using DiffUtil
        val diffResult = DiffUtil.calculateDiff(MyDiffCallback(content, newContentCards))

        // Update the internal list
        content = newContentCards.toMutableList()

        // Dispatch updates to RecyclerView
        diffResult.dispatchUpdatesTo(this)
    }

    class MyDiffCallback(private val oldList: List<ContentCard>, private val newList: List<ContentCard>) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
