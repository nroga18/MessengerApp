package ge.nrogava.messengerapp.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import ge.nrogava.messengerapp.R
import ge.nrogava.messengerapp.database.Chat
import ge.nrogava.messengerapp.database.FirebaseRepository
import ge.nrogava.messengerapp.database.Person
import ge.nrogava.messengerapp.databinding.ConversationListViewBinding
import ge.nrogava.messengerapp.databinding.SearchListViewBinding

class PeopleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val rep = FirebaseRepository
    private val peopleItems = mutableListOf<Person>()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PeopleViewHolder(parent)
    }

    inner class PeopleViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.search_list_view,parent, false)
    ) {

        private val binding= SearchListViewBinding.bind(itemView)

        fun onBind(person: Person) {
            Log.d("Amount",person.nickname)
            binding.nickname.text=person.nickname
            binding.occupation.text=person.occupation
            Log.d("Picasso",person.toString())
            Log.d("Picasso",person.url)
            if(person.url.isNotEmpty()) {
                Picasso.get().load(person.url).into(binding.profileImage)
            } else {
                binding.profileImage.setImageResource(R.drawable.avatar_image_placeholder)
                Log.d("PicassoNull",person.url)
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PeopleViewHolder).onBind(peopleItems[position])

    }

    override fun getItemCount(): Int {

        return peopleItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(peopleItems: List<Person>) {
        this.peopleItems.clear()
        this.peopleItems.addAll(peopleItems)

        notifyDataSetChanged()
    }
}