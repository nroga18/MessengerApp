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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

//import ge.nrogava.messengerapp.util.toast



object FirebaseRepository {
   val db=Firebase.database
   val firebaseAuth : FirebaseAuth =Firebase.auth
   val dbRef=db.getReference("chats")
   val peopleRef=db.getReference("users")
   val messagesRef=db.getReference("user-messages")
   var fireBaseUser : FirebaseUser?
   var person : Person?
   private val mail = "@freeuni.edu.ge"


   init {

      fireBaseUser = Firebase.auth.currentUser

      person = null

      if (fireBaseUser != null) {
         person=getAndSetupCurrentUser()
         Log.d("User", fireBaseUser!!.uid.toString())
         Log.d("CurrentUserLog4", person.toString())

      }
   }

   fun currentUserPicture(): String {
      val uid= Firebase.auth.currentUser?.uid
      var url:String = ""
      if (uid != null) {
         peopleRef.child(uid).child("url").get().addOnSuccessListener {
               data -> url=data.value as String

            Log.d("PictureData2", url)

            Log.d("PictureData", data.value.toString())
         }
         Log.d("PictureData3", url)
         return url
      }

      Log.d("Picture1", url)
      return url
   }

   fun saveImageInRealtimeDatabase(url : String) {
      val uid= Firebase.auth.currentUser?.uid
      if (uid != null) {
         peopleRef.child(uid).child("url").setValue(url)
      }


   }

    fun updateCurrentUser(newNickname : String, newOccupation : String) {
      peopleRef.child(fireBaseUser!!.uid).child("occupation").setValue(newOccupation)
      peopleRef.child(fireBaseUser!!.uid).child("nickname").setValue(newNickname)
       Log.d("CurrentUserLog4", person.toString())
       fireBaseUser = Firebase.auth.currentUser


       person = null

       if (fireBaseUser != null) {
           fireBaseUser!!.updateEmail(newNickname+mail)
            person=getAndSetupCurrentUser()
          Log.d("User", fireBaseUser!!.uid.toString())
          Log.d("CurrentUserLog4", person.toString())

       }
   }

     fun getAndSetupCurrentUser():Person {

       val localPerson=Person("placeholder1","placeholder2", "")

          peopleRef.child(fireBaseUser!!.uid).child("occupation").get().addOnSuccessListener {

             data -> localPerson.occupation = data.value as String
          }
          peopleRef.child(fireBaseUser!!.uid).child("nickname").get().addOnSuccessListener {

             data -> localPerson.nickname = data.value as String
          }

       return localPerson

   }

   fun signOut() {

      fireBaseUser = null
      person = null
      firebaseAuth.signOut()
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
                     person = Person(nickname, occupation, fireBaseUser?.uid?:"")
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

      val user = Person(nickname,occupation, userId)
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


   fun getAllChats1(liveData : MutableLiveData<List<Chat>>) {
      dbRef.addValueEventListener(object : ValueEventListener {
         override fun onDataChange(snapshot: DataSnapshot) {
            Log.i("Firebase",snapshot.value.toString())
            val chats : List<Chat> = snapshot.children.map {

                  dataSnapshot ->  dataSnapshot.getValue(Chat::class.java)!!

            }
//            var userChats  = mutableSetOf<Chat>()
//            for (c in chats){
//               if(c.user == person?.nickname){
//                  userChats.add(c)
//               }
//            }
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
   fun getAllChats(liveData : MutableLiveData<List<Chat>>) {

      messagesRef.child(fireBaseUser?.uid?:"").addValueEventListener(object : ValueEventListener {

         override fun onDataChange(snapshot: DataSnapshot) {
            Log.i("Firebase",snapshot.value.toString())

            var userMesages = HashMap<String, MutableList<Message>>()
            var hashMap = snapshot.value as HashMap<*, *>
            for (receiverUid in hashMap.keys){
               var receiverMessages = hashMap[receiverUid] as HashMap<*, *>
               var rUid = receiverUid as String
               userMesages[rUid] = mutableListOf<Message>()
               for(messageKey in receiverMessages.keys){
                  var m = receiverMessages[messageKey] as HashMap<*,*>
                  var sendOrReceived = m["sentOrReceived"] as Boolean
                  var key = m["key"] as String
                  var fromId = m["fromId"] as String
                  var toId = m["toId"] as String
                  var toIdNickname = m["toIdNickname"] as String
                  var fromIdNickname = m["fromIdNickname"] as String
                  var message = m["message"] as String
                  var time = m["time"] as Long
                  var w = Message(sendOrReceived, key, fromId, toId,toIdNickname,fromIdNickname, message, time)
                  userMesages[rUid]?.add(w)
               }
            }
            var userChats  = mutableListOf<Chat>()
            for (k in userMesages.keys){
               var lastMessage = userMesages[k]?.get((userMesages[k]?.size ?: 0) -1)
               var user = ""

                  if(fireBaseUser!!.uid == lastMessage!!.fromId){
                     user = lastMessage!!.toIdNickname
                  }else{
                     user = lastMessage!!.fromIdNickname
                  }
                  getNicknameByUid(user)


               var time = lastMessage?.time
               var recentMessage = lastMessage?.message
               var chat = Chat(user, time?:1 , recentMessage?:"")
               userChats.add(chat)
            }
            liveData.postValue(Collections.unmodifiableList(userChats))

         }

         override fun onCancelled(error: DatabaseError) {
            //
         }
      })

   }
   fun getNicknameByUid(id : String) {
      val uidRef = peopleRef.child(id)

      val valueEventListener = object : ValueEventListener {
         override fun onDataChange(dataSnapshot: DataSnapshot) {
            val user = dataSnapshot.getValue(Person::class.java)
            //return user.nickname
         }

         override fun onCancelled(databaseError: DatabaseError) {
         }
      }
      uidRef.addListenerForSingleValueEvent(valueEventListener)
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
            Log.i("SearchUsers",snapshot.value.toString())
            val persons : List<Person> = snapshot.children.map {

                  dataSnapshot ->
               dataSnapshot.getValue(Person::class.java)!!

            }
            liveData.postValue(persons)
            Log.d("Amount", persons.size.toString())
         }

         override fun onCancelled(error: DatabaseError) {
            //
         }
      })


   }

    fun searchMessages(_messagesLiveData: MutableLiveData<List<Message>>, person: String) {

    }

   fun getAllMessages(_messagesLiveData: MutableLiveData<List<Message>>) {

   }

   fun displayChatmessages(messages: ArrayList<Message>, user: Person, any: Any) {
      var receiverUid = fireBaseUser?.uid?:""
      var senderUid = user.uid

//      messagesRef.orderByChild("nickname").equalTo(nickname)
//         .addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//               Log.i("Firebase", snapshot.value.toString())
//               val persons : List<Person> = snapshot.children.map {
//
//                     dataSnapshot ->
//                  dataSnapshot.getValue(Person::class.java)!!
//
//               }
//               if(persons.count() > 0){
//                  receiver = persons[0]
//                  completion(receiver)
//               }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//               //
//            }
//         })
//      return receiver
   }

   fun getOccupationByNickname(nickname: String): CharSequence? {
      return ""
   }

   fun sendMessageFromChat(toId: String, messageText: String, receiverNickname : String) {
      var fromId = fireBaseUser?.uid?:""
      val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
      val ref1 = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
      val message = Message(true, ref.key!!, fromId, toId,receiverNickname, person!!.nickname, messageText)
      ref.setValue(message)
      ref1.setValue(message)
   }

   fun getUserByNickname(nickname: String, completion: (Person) -> Unit,): Person {
      var receiver = Person()
      peopleRef.orderByChild("nickname").equalTo(nickname)
         .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               Log.i("Firebase", snapshot.value.toString())
               val persons : List<Person> = snapshot.children.map {

                     dataSnapshot ->
                  dataSnapshot.getValue(Person::class.java)!!

               }
               if(persons.count() > 0){
                  receiver = persons[0]
                  completion(receiver)
               }
            }

            override fun onCancelled(error: DatabaseError) {
               //
            }
         })
      return receiver
   }

   fun listenForMessages(toId : String, completion: (Message) -> Unit) {
      var fromId = fireBaseUser?.uid?:""
      val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
      ref.addChildEventListener(object : ChildEventListener{
         override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            var m = snapshot.getValue(Message::class.java)
            if(m != null){
               m.sentOrReceived = m.fromId == fromId
               completion(m)
            }
         }

         override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
         }

         override fun onChildRemoved(snapshot: DataSnapshot) {
            TODO("Not yet implemented")
         }

         override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
         }

         override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
         }

      })
   }


}