package ge.nrogava.messengerapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.nrogava.messengerapp.R
import ge.nrogava.messengerapp.database.Message
import java.text.SimpleDateFormat

class MessagesAdapter (val messages: ArrayList<Message>): RecyclerView.Adapter<MessageRecyclerViewViewHolder>() {


    override fun getItemViewType(ind: Int): Int {
        if(messages[ind].sentOrReceived){
            return 1
        }
        return 0
    }
    override fun getItemCount(): Int {
        return messages.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageRecyclerViewViewHolder {

        return if(viewType == 1){
            val v = LayoutInflater.from(parent.context).inflate(R.layout.msg_sent, parent, false)
            MessageRecyclerViewViewHolder(v)
        }else{
            val v = LayoutInflater.from(parent.context).inflate(R.layout.msg_received, parent, false)
            MessageRecyclerViewViewHolder(v)
        }
    }
    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: MessageRecyclerViewViewHolder, ind: Int) {
        holder.message.text = messages[ind].message
        holder.time.text = SimpleDateFormat("hh:mm").format(messages[ind].time)
    }
}

class MessageRecyclerViewViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val message: TextView = itemView.findViewById(R.id.txt_msg)
    val time: TextView = itemView.findViewById(R.id.txt_time)
}
