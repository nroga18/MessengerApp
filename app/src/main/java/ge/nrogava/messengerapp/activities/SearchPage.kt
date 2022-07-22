package ge.nrogava.messengerapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import ge.nrogava.messengerapp.R
import ge.nrogava.messengerapp.adapters.PeopleAdapter
import ge.nrogava.messengerapp.databinding.ActivitySearchPageBinding
import ge.nrogava.messengerapp.util.afterTextChangedDelayed
import ge.nrogava.messengerapp.util.toast
import ge.nrogava.messengerapp.views.PeopleViewModel

class SearchPage : AppCompatActivity() {

    lateinit var backButton : ImageButton
    lateinit var viewModel: PeopleViewModel
    lateinit var peopleAdapter: PeopleAdapter
    lateinit var progressBar: ProgressBar
    lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySearchPageBinding= DataBindingUtil.setContentView(this,
            R.layout.activity_search_page
        )
        toast("Loading... Please Wait.")
        recyclerViewInit(binding)
        viewInitializations()
        toast("Loading Successful")
    }

    private fun recyclerViewInit(binding:ActivitySearchPageBinding) {

        //lazy loading yet to be implemented.
        viewModel = ViewModelProvider(this)[PeopleViewModel::class.java]
        binding.lifecycleOwner=this
        binding.viewModel=viewModel

        peopleAdapter=PeopleAdapter()
        binding.searchRecyclerView.adapter=peopleAdapter

        viewModel.getAllPeople()

        viewModel.peopleLiveData.observe(this) {
            peopleItems -> peopleAdapter.setItems(peopleItems )
           //     chatItems -> peopleAdapter.setItems(chatItems)
            Log.d("AmountPeopleItems",peopleItems.toString())
        }
        Log.d("AmountVM", viewModel.getAllPeople().toString())

    }



    private fun viewInitializations() {
        backButtonInit()
        searchBarInit()
    }

    private fun searchBarInit() {
        searchEditText=findViewById(R.id.search_edit_text)
        progressBar=findViewById(R.id.progress_bar_search)
        progressBar.visibility= View.INVISIBLE

        searchEditText.afterTextChangedDelayed {
            val searchText=searchEditText.text.toString()
            var sizeForNotification:Int = 0
            if(searchText.length>=3) {
                viewModel.getPeople(searchText)
                viewModel.peopleLiveData.observe(this) { peopleItems ->
                    peopleAdapter.setItems(peopleItems)
                    sizeForNotification=peopleItems.size
                    Log.d("RV", "In Activity")
                }
                Log.d("PeopleRV", sizeForNotification.toString())
                if(sizeForNotification==0) {
                    toast("Nickname couldn't be found.")
                    //is one update behind when calculating size of list. should be corrected.
                }
                sizeForNotification=0
            }
        }

    }

    private fun backButtonInit() {
        backButton=findViewById(R.id.back_button)
        backButton.setOnClickListener {
            startActivity(Intent(this, HomePage::class.java))
            finish()
        }
    }
}