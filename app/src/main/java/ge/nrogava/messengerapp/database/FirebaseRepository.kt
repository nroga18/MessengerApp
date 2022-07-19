package ge.nrogava.messengerapp.database

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase



class FirebaseRepository {
   val db=Firebase.database
   val dbRef=db.getReference("chats")
   val peopleRef=db.getReference("people")

   fun getAllChats(liveData : MutableLiveData<List<Chat>>) {
      dbRef.addValueEventListener(object : ValueEventListener {
         override fun onDataChange(snapshot: DataSnapshot) {
            Log.i("Firebase",snapshot.value.toString())
            val chats : List<Chat> = snapshot.children.map {

                  dataSnapshot ->  dataSnapshot.getValue(Chat::class.java)!!

            }
            Log.i("CHATS",chats.toString())
            Log.d("RV","In Repository")
            Log.d("RV", chats.size.toString())
            liveData.postValue(chats)

         }

         override fun onCancelled(error: DatabaseError) {
            //
         }
      })


   }

   fun searchChats(liveData : MutableLiveData<List<Chat>>, person: String) {

      dbRef.orderByChild("user").startAt(person).endAt(person+"\uf8ff").addValueEventListener(object : ValueEventListener {
         override fun onDataChange(snapshot: DataSnapshot) {
            Log.i("Firebase",snapshot.value.toString())
            val chats : List<Chat> = snapshot.children.map {
                  dataSnapshot ->  dataSnapshot.getValue(Chat::class.java)!!
            }

            Log.d("RVSearch", chats.size.toString())
            liveData.postValue(chats)
         }

         override fun onCancelled(error: DatabaseError) {
            //
         }
      })

   }


   fun getPeople(liveData : MutableLiveData<List<Person>>, prefix: String) {

      peopleRef.orderByChild("nickname").startAt(prefix).endAt(prefix+"\uf8ff").addValueEventListener(object : ValueEventListener {
         override fun onDataChange(snapshot: DataSnapshot) {
            Log.i("Firebase",snapshot.value.toString())
            val persons : List<Person> = snapshot.children.map {
                  dataSnapshot ->  dataSnapshot.getValue(Person::class.java)!!
            }

            Log.d("RVSearch", persons.size.toString())
            liveData.postValue(persons)
         }

         override fun onCancelled(error: DatabaseError) {
            //
         }
      })

   }

   fun getAllPeople(liveData : MutableLiveData<List<Person>>) {
      peopleRef.addValueEventListener(object : ValueEventListener {
         override fun onDataChange(snapshot: DataSnapshot) {
            Log.i("Firebase",snapshot.value.toString())
            val persons : List<Person> = snapshot.children.map {

                  dataSnapshot ->  dataSnapshot.getValue(Person::class.java)!!

            }

            liveData.postValue(persons)

         }

         override fun onCancelled(error: DatabaseError) {
            //
         }
      })


   }












}