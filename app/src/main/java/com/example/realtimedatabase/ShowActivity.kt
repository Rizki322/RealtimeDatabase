package com.example.realtimedatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_show.*

class ShowActivity : AppCompatActivity() {

    lateinit var ref : DatabaseReference
    lateinit var list: MutableList<Users>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)


        ref = FirebaseDatabase.getInstance().getReference("USERS")
        list = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError){

                Toast.makeText(this@ShowActivity,"Koneksi Gagal",Toast.LENGTH_SHORT).show()
            }
            override fun onDataChange(p0: DataSnapshot){
                if (p0.exists()){
                    list.clear()
                    for(h in p0.children){
                        val user = h.getValue(Users::class.java)
                        list.add(user!!)
                    }
                    val adapter = ListAdapter(this@ShowActivity,R.layout.item_user,list)
                    listView.adapter=adapter


                }
            }

        })
    }
}
