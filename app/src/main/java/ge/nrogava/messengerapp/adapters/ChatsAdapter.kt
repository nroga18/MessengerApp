package ge.nrogava.messengerapp.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import ge.nrogava.messengerapp.R

import ge.nrogava.messengerapp.database.Chat
import ge.nrogava.messengerapp.databinding.ConversationListViewBinding

class ChatsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val chatItems = mutableListOf<Chat>()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatsViewHolder(parent)
    }

    inner class ChatsViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.conversation_list_view,parent, false)
    ) {

        private val binding= ConversationListViewBinding.bind(itemView)



        fun onBind(chat: Chat) {

            binding.nickname.text=chat.user
            binding.lastMessage.text=chat.recentMessage
            binding.time.text=chat.time
            Log.d("RV","In Adapter")
            Log.d("RV","Time: " + chat.time)

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ChatsViewHolder).onBind(chatItems[position])

    }

    override fun getItemCount(): Int {
        return chatItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(chatItems: List<Chat>) {
        this.chatItems.clear()
        this.chatItems.addAll(chatItems)

        notifyDataSetChanged()
    }

}

