package com.home.cmoneytestdemo.second.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.home.cmoneytestdemo.common.util.JsonUtil
import com.home.cmoneytestdemo.databinding.ActivitySecondRecyclerViewItemBinding

class SecondAdapter : RecyclerView.Adapter<SecondAdapter.ViewHolder>() {

    private var jsonUtil: JsonUtil? = null
    var setOnItemClickListener: ((title: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ActivitySecondRecyclerViewItemBinding
            .inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(jsonUtil!!.index(position) , setOnItemClickListener)
    }

    override fun getItemCount(): Int {
        return jsonUtil!!.count()
    }

    fun setResponse(response: String?) {
        val jsonUtil = JsonUtil(response)
        this.jsonUtil = jsonUtil
    }

    class ViewHolder(binding: ActivitySecondRecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val rootConstraintLayout: ConstraintLayout = binding.constraintLayoutRoot
        private val idTextView: TextView = binding.textViewId
        private val titleTextView: TextView = binding.textViewTitle
        private val thumbnailImageView: ImageView = binding.imageViewThumbnail

        fun bindData(
            jsonUtil: JsonUtil?, clickItemListener: ((title: String) -> Unit)?
        ) {
            jsonUtil?.apply {
                val id = key("id").intValue().toString()
                idTextView.text = id
                val title = key("title").stringValue()
                titleTextView.text = title
                val url = key("thumbnailUrl").stringValue()
                // not quite sure why you need to build another wheel now
                // but also dealing with caching and lifecycle
                // also consider how to reduce memory usage
                // personally recommend using Glide Picasso Coil
                thumbnailImageView.load(url)

                rootConstraintLayout.post {
                    val layoutParams = itemView.layoutParams
                    layoutParams.width = rootConstraintLayout.width
                    layoutParams.height = rootConstraintLayout.width
                    rootConstraintLayout.layoutParams = layoutParams
                }
                rootConstraintLayout.setOnClickListener {
                    clickItemListener!!.invoke(title)
                }
            }
        }
    }
}