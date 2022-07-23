package ge.nrogava.messengerapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ge.nrogava.messengerapp.R
import ge.nrogava.messengerapp.adapters.MessagesAdapter
import ge.nrogava.messengerapp.database.FirebaseRepository
import ge.nrogava.messengerapp.database.Message

class ChatActivity : AppCompatActivity() {

    private val messages = arrayListOf<Message>()
    private val rep= FirebaseRepository
    lateinit var chatsView: RecyclerView
    lateinit var backBtnOnChat: ImageView
    lateinit var messageReceiverNickName: TextView
    lateinit var messageInputTxt: TextView
    lateinit var messageReceiverOccupation: TextView
    lateinit var nickname : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        nickname = intent.getStringExtra("chat")!!
        setMessageListOnChat(nickname)
        setChatBackButtonListener()
        setReceiverNameOnChat()
        setReceiverOccupationOnChat()
        setInputTextListener()
        setLinearLayoutManagerToChatsView()
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

    fun sendMessageFromChat(){
        val message = messageInputTxt.text.toString()
        messages.add(Message( true, message))
        updateMessagesInChat()
        rep.sendMessageFromChat(nickname, message)
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
    private fun setMessageListOnChat(user: String) {
        rep.displayChatmessages(messages, user, this::updateMessagesInChat)
        chatsView = findViewById(R.id.recycler_chat)
        chatsView.adapter = MessagesAdapter(messages)
    }

    private fun updateMessagesInChat(){
        chatsView.adapter?.notifyDataSetChanged()
        chatsView.scrollToPosition(messages.size - 1)
    }

}