package ge.nrogava.messengerapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ge.nrogava.messengerapp.R
import ge.nrogava.messengerapp.adapters.ChatsAdapter
import ge.nrogava.messengerapp.database.Chat
import ge.nrogava.messengerapp.database.FirebaseRepository
import ge.nrogava.messengerapp.databinding.ActivityHomepageBinding
import ge.nrogava.messengerapp.util.toast
import ge.nrogava.messengerapp.views.ChatsViewModel

class HomePage : AppCompatActivity() , Listener{


    lateinit var bottomNav : BottomNavigationView
    lateinit var fab:FloatingActionButton
    lateinit var search:EditText
    lateinit var viewModel:ChatsViewModel
    lateinit var chatsAdapter:ChatsAdapter
    lateinit var progressBar: ProgressBar
    private var chatList: ArrayList<Chat> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityHomepageBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_homepage
        )


        toast("Loading... Please Wait.")

        //cant seem to get progressbar working.

        recyclerViewInit(binding)
        viewInitializations()
        toast("Loading Successful")


    }

    //onResume should be implemented - chat may have been added and progressBar should update.


    private fun recyclerViewInit(binding:ActivityHomepageBinding) {


        viewModel = ViewModelProvider(this)[ChatsViewModel::class.java]
        binding.lifecycleOwner=this
        binding.viewModel=viewModel

        chatsAdapter=ChatsAdapter(this)
        binding.conversationsRecyclerView.adapter=chatsAdapter
        viewModel.getAllChats()

        viewModel.chatsLiveData.observe(this) {
                chatItems -> chatsAdapter.setItems(chatItems)
            Log.d("RV","In Activity")
        }


    }

    private fun viewInitializations() {
        searchBarInit()
        bottomNavInit()
        searchFabInit()
       // progressBarInit()

    }

/*    private fun progressBarInit() {

        progressBar=findViewById(R.id.progress_bar)
        toast("Loading... Please Wait.")
        progressBar.visibility= View.GONE

    }


 */

    private fun searchBarInit() {
        search=findViewById(R.id.search_edit_text)
        search.addTextChangedListener {
            val searchText=search.text.toString()
            viewModel.searchChats(searchText)
            viewModel.chatsLiveData.observe(this) {
                    chatItems -> chatsAdapter.setItems(chatItems)
            }

        }

    }

    private fun searchFabInit() {
        fab=findViewById(R.id.search_fab)
        fab.setOnClickListener {
            startActivity(Intent(this, SearchPage::class.java))
            finish()
        }
    }




    private fun bottomNavInit() {
        bottomNav=findViewById(R.id.bottomAppNav)
        bottomNav.background=null
        bottomNav.setOnItemReselectedListener {

            if(it.itemId != R.id.home) {

                startActivity(Intent(this, ProfilePage::class.java))
                finish()
            }
        }

        bottomNav.selectedItemId= R.id.settings

    }
    override fun onClick(position: Int) {
        val intentForChatActivity = Intent(this, ChatActivity::class.java)
        intentForChatActivity.putExtra("chat", chatList[position].user)
        startActivity(intentForChatActivity)
    }
}