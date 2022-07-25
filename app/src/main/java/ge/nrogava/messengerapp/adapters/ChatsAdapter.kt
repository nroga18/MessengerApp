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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import ge.nrogava.messengerapp.R
import ge.nrogava.messengerapp.activities.ChatActivity
import ge.nrogava.messengerapp.activities.Listener

import ge.nrogava.messengerapp.database.Chat
import ge.nrogava.messengerapp.database.FirebaseRepository
import ge.nrogava.messengerapp.database.Person
import ge.nrogava.messengerapp.databinding.ConversationListViewBinding
import ge.nrogava.messengerapp.util.calculateTime
import java.text.SimpleDateFormat

class ChatsAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val chatItems = mutableListOf<Chat>()

    private val rep=FirebaseRepository




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatsViewHolder(parent)
    }

    inner class ChatsViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.conversation_list_view,parent, false))

     {

        private val binding= ConversationListViewBinding.bind(itemView)




        fun onBind(chat: Chat) {

            binding.nickname.text=chat.user
            if(chat.recentMessage.length>30) {
                binding.lastMessage.text=chat.recentMessage.substring(0,25)+"..."
            } else {
                binding.lastMessage.text=chat.recentMessage
            }

            FirebaseRepository.peopleRef.orderByChild("nickname").equalTo(chat.user).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val persons: List<Person> = snapshot.children.map {

                            dataSnapshot ->

                        dataSnapshot.getValue(Person::class.java)!!
                    }

                    if(persons.count() > 0){
                        if(persons[0].url.length>1) {
                            Picasso.get().load(persons[0].url).into(binding.profileImage)
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
            binding.time.text= calculateTime(chat.time*1000)
          //  binding.time.text= SimpleDateFormat("HH:mm").format(chat.time*1000)
            Log.d("RV","In Adapter")
            Log.d("RV","Time: " + chat.time)

        }


     }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatActivity :: class.java)
            intent.putExtra("nickname", chatItems[position].user)
            intent.putExtra("Source", "Home");
            Log.d("DecideIntent","2")
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

