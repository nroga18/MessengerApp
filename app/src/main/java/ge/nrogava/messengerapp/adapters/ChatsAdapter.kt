package ge.nrogava.messengerapp.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.nrogava.messengerapp.R
import ge.nrogava.messengerapp.activities.ChatActivity
import ge.nrogava.messengerapp.activities.Listener

import ge.nrogava.messengerapp.database.Chat
import ge.nrogava.messengerapp.databinding.ConversationListViewBinding

class ChatsAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val chatItems = mutableListOf<Chat>()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatsViewHolder(parent)
    }

    inner class ChatsViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.conversation_list_view,parent, false))

     {

        private val binding= ConversationListViewBinding.bind(itemView)



        fun onBind(chat: Chat) {

            binding.nickname.text=chat.user
            binding.lastMessage.text=chat.recentMessage
            binding.time.text=chat.time
            Log.d("RV","In Adapter")
            Log.d("RV","Time: " + chat.time)

        }

//         override fun onClick(p0: View?) {
//             listener.onClick(adapterPosition)
//         }

     }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatActivity :: class.java)
            intent.putExtra("nickname", chatItems[position].user)
            intent.putExtra("Source", "Home");
            context.startActivity(intent)
            (context as Activity).finish()
        }



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

