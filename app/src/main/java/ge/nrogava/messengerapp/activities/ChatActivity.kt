package ge.nrogava.messengerapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ge.nrogava.messengerapp.R
import ge.nrogava.messengerapp.adapters.MessagesAdapter
import ge.nrogava.messengerapp.database.FirebaseRepository
import ge.nrogava.messengerapp.database.Message
import ge.nrogava.messengerapp.database.Person

class ChatActivity : AppCompatActivity() {

    private val messages = arrayListOf<Message>()
    private val rep= FirebaseRepository
    lateinit var chatsView: RecyclerView
    lateinit var backBtnOnChat: ImageView
    lateinit var messageReceiverNickName: TextView
    lateinit var messageInputTxt: TextView
    lateinit var messageReceiverOccupation: TextView
    lateinit var nickname : String
    lateinit var receiver : Person
    lateinit var profileImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        receiver = Person("","","")
        profileImage=findViewById(R.id.profile_image_in_chat)

        //--------------------------------
        val source = intent.getStringExtra("Source")
        if(source=="Home") {
            nickname = intent.getStringExtra("nickname")!!
            rep.getUserByNickname(nickname, this::setToId)
        } else {
            val url=intent.getStringExtra("url")
            if(url!!.length>1) {
                Picasso.get().load(url).into(profileImage)
            }
            nickname = intent.getStringExtra("nicknameSearch")!!
            rep.getUserByNickname(nickname, this::setToId)
        }

        //--------------------------------

        setMessageListOnChat()
        setChatBackButtonListener()
        setReceiverNameOnChat()
        setReceiverOccupationOnChat()
        setInputTextListener()
        setLinearLayoutManagerToChatsView()


    }

    private fun listenForMessages() {
        rep.listenForMessages(receiver.uid, this::updateList)
    }
    private fun updateList(m : Message){
        messages.add(m)
        setMessageListOnChat()
    }
    private fun setToId(r : Person){
        receiver = r

        Log.d("IntentChatReceiver",receiver.nickname)

        listenForMessages()
    }
    private fun setLinearLayoutManagerToChatsView() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        chatsView.layoutManager = linearLayoutManager
    }

    private fun setInputTextListener() {
        messageInputTxt = findViewById<EditText>(R.id.txt_message)
        messageInputTxt.setOnClickListener {
            chatsView.scrollToPosition(messages.size - 1)
        }
    }

    fun sendMessageFromChat(view : View){
        val message = messageInputTxt.text.toString()
        updateMessagesInChat()
        Log.d("IntentChatUID",receiver.uid.toString())
        rep.sendMessageFromChat(receiver.uid, message, receiver.nickname)
        findViewById<EditText>(R.id.txt_message).text.clear()
    }
    private fun setReceiverOccupationOnChat() {
        messageReceiverOccupation = findViewById<TextView>(R.id.txt_receiver_occupation)
        messageReceiverOccupation.text = rep.getOccupationByNickname(nickname)
    }

    private fun setReceiverNameOnChat() {
        messageReceiverNickName = findViewById<TextView>(R.id.txt_msg_receiver)
        messageReceiverNickName.text = nickname
    }

    private fun setChatBackButtonListener() {
        backBtnOnChat = findViewById<ImageView>(R.id.btn_back)
        backBtnOnChat.setOnClickListener {
            startActivity(Intent(this, HomePage::class.java))
            finish()
        }
    }
    private fun setMessageListOnChat() {
        rep.displayChatmessages(messages, receiver, this::updateMessagesInChat)
        chatsView = findViewById(R.id.recycler_chat)
        chatsView.adapter = MessagesAdapter(messages)

    }

    private fun updateMessagesInChat(){
        chatsView.scrollToPosition(messages.size - 1)
        chatsView.adapter?.notifyDataSetChanged()

    }

}