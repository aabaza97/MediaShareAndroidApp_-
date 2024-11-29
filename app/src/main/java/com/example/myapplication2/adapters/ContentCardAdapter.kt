package com.example.myapplication2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication2.databinding.ContentCardItemBinding
import com.example.myapplication2.model.ContentCard

class ContentCardAdapter(
    private val contentCards: List<ContentCard>
) : RecyclerView.Adapter<ContentCardAdapter.ContentCardViewHolder>() {

    inner class ContentCardViewHolder(
        private val binding: ContentCardItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contentCard: ContentCard) {
            binding.titleTextView.text = contentCard.title

            // Load image or video thumbnail
            Glide.with(binding.root.context)
                .load(contentCard.mediaUrl)
                .into(binding.mediaImageView)

            // Like button toggle
            binding.likeButton.isChecked = contentCard.isLiked
            binding.likeButton.setOnClickListener {
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
        holder.bind(contentCards[position])
    }

    override fun getItemCount() = contentCards.size
}
