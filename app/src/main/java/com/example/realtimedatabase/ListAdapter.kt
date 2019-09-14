package com.example.realtimedatabase

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.layout_update.view.*

class ListAdapter (val con: Context,val layoutResId: Int,val list: List<Users> )
    : ArrayAdapter<Users>(con, layoutResId, list) {

    override fun getView(postition: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater = LayoutInflater.from(con)
        val view = layoutInflater.inflate(layoutResId, null)

        val textNama = view.findViewById<TextView>(R.id.tv_nama)
        val textEmail = view.findViewById<TextView>(R.id.tv_email)
        val bt_Update = view.findViewById<Button>(R.id.bt_update)
        val bt_Delete = view.findViewById<Button>(R.id.bt_delete)

        val user = list[postition]
        textNama.text = user.nama
        textEmail.text = user.email

        bt_Update.setOnClickListener{
            showUpdateDialog(user)
        }

        bt_Delete.setOnClickListener{
            deleteInfo(user)
        }

        return view
    }
    private fun deleteInfo(user: Users){
        val progressDialog = ProgressDialog(con)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Deleting.....")
        progressDialog.show()
        val db = FirebaseDatabase.getInstance().getReference("USERS")
        db.child(user.id).removeValue().addOnCompleteListener{
            Toast.makeText(con,"DELETED",Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }

    }
    private fun showUpdateDialog(user: Users){
        val builder = AlertDialog.Builder(con)
        builder.setTitle("Update")
        val inflater = LayoutInflater.from(con)
        val view = inflater.inflate(R.layout.layout_update,null)

        view.et_nama_update.setText(user.nama)
        view.et_email_update.setText(user.email)

        builder.setView(view)
        builder.setPositiveButton("Update"){dialogInterface, i ->
            val dbUsers = FirebaseDatabase.getInstance().getReference("USERS")
            val nama = view.et_nama_update.text.toString().trim()
            val email = view.et_email_update.text.toString().trim()

            when {
                nama.isEmpty() -> {
                    view.et_nama_update.error = "Nama tidak boleh kosong"
                    return@setPositiveButton
                }


                email.isEmpty() -> {
                    return@setPositiveButton
                    view.et_email_update.error = "Email tidak boleh kosong"
                }
                else -> {
                    val user = Users(user.id,nama,email)
                    dbUsers.child(user.id).setValue(user).addOnCompleteListener{
                        Toast.makeText(con,"Updated",Toast.LENGTH_SHORT).show()
                    }
                }


            }
        }
        builder.setNegativeButton("cancel"){dialogInterface, i ->

        }

        val alert = builder.create()
        alert.show()
    }

}