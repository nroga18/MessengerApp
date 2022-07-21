package ge.nrogava.messengerapp.database

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase



class FirebaseRepository {
   val db=Firebase.database
   val firebaseAuth : FirebaseAuth =Firebase.auth
   val dbRef=db.getReference("chats")
   val peopleRef=db.getReference("people")
   var fireBaseUser : FirebaseUser?
   var person : Person?
   private val mail = "@freeuni.edu.ge"


   init {
      person = null
      fireBaseUser = Firebase.auth.currentUser
      if (fireBaseUser != null) {

      }
   }
   fun fireBaseSignUp(
      nickname: String, password: String, occupation: String, context: Context, completion: (success: Boolean) -> Unit
   ): Boolean {
      return when {
         nickname.isBlank() -> {
            Toast.makeText(context, "Please enter your nickname", Toast.LENGTH_SHORT)
               .show()
            completion(false)
            false
         }
         occupation.isBlank() -> {
            Toast.makeText(context, "Please enter your occupation", Toast.LENGTH_SHORT)
               .show()
            completion(false)
            false
         }
         password.isBlank() || password.length < 6 -> {
            Toast.makeText(
               context,
               "Please enter your Password that is at least 6 characters long",
               Toast.LENGTH_LONG
            ).show()
            completion(false)
            false
         }
         else -> {
            firebaseAuth.createUserWithEmailAndPassword("$nickname$mail", password)
               .addOnCompleteListener { task ->
                  if (task.isSuccessful) {
                     fireBaseUser = Firebase.auth.currentUser

                     saveUserToDatabase(nickname, occupation)
                     person = Person(nickname, occupation)
                     completion(true)
                  } else {
                     Toast.makeText(
                        context,
                        "Error: Failed to register user\n ${task.exception?.localizedMessage}",
                        Toast.LENGTH_LONG
                     ).show()
                     completion(false)
                  }
               }
            return true
         }
      }
   }
   fun login(nickname: String, password: String, context: Context, completion: (success: Boolean) -> Unit) {
      firebaseAuth.signInWithEmailAndPassword("$nickname$mail", password)
         .addOnCompleteListener { task ->
            if (task.isSuccessful) {
               fireBaseUser = Firebase.auth.currentUser
               findUserByUID(fireBaseUser!!.uid)
               completion(true)
            }
         }
         .addOnFailureListener {
            Toast.makeText(
               context,
               "Failed to log in: ${it.localizedMessage}",
               Toast.LENGTH_LONG
            ).show()
            completion(false)
         }
   }
   fun saveUserToDatabase(nickname: String, occupation: String) {
      val userId = FirebaseAuth.getInstance().uid?:""
      val ref = FirebaseDatabase.getInstance().getReference("/users/$userId")

      val user = Person(nickname,occupation)
      ref.setValue(user).addOnSuccessListener {  }
   }
   fun findUserByUID(uid: String){
      val db = Firebase.database.reference
      val uidRef = db.child("users").child(uid)
      val valueEventListener = object : ValueEventListener {
         override fun onDataChange(dataSnapshot: DataSnapshot) {
            person = dataSnapshot.getValue(Person::class.java)
         }

         override fun onCancelled(databaseError: DatabaseError) {
            Log.d("d", databaseError.message) //Don't ignore errors!
         }
      }
      uidRef.addListenerForSingleValueEvent(valueEventListener)
   }

   fun getNameFromMail(mailAddress: String): String {
      return mailAddress.substring(0, mailAddress.indexOf('@'))
   }


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