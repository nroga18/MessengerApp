package ge.nrogava.messengerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.nrogava.messengerapp.adapters.ChatsAdapter
import ge.nrogava.messengerapp.databinding.ActivityHomepageBinding
import ge.nrogava.messengerapp.views.ChatsViewModel

class HomePage : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityHomepageBinding = DataBindingUtil.setContentView(this, R.layout.activity_homepage)
        viewInitializations()

        val viewModel: ChatsViewModel = ViewModelProvider(this)[ChatsViewModel::class.java]
        binding.lifecycleOwner=this
        binding.viewModel=viewModel


        val chatsAdapter=ChatsAdapter()
        binding.conversationsRecyclerView.adapter=chatsAdapter
        viewModel.getAllChats()
        viewModel.chatsLiveData.observe(this) {

            chatItems -> chatsAdapter.setItems(chatItems)
            Log.d("RV","In Activity")
        }


    }


    private fun recyclerViewInit() {

    }

    private fun viewInitializations() {
        bottomNavInit()
    }

    private fun bottomNavInit() {
        bottomNav=findViewById(R.id.bottomAppNav)
        bottomNav.background=null
    }
}