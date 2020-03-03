package cesar.devapps.finalproject.views

import android.content.Intent
import cesar.devapps.finalproject.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import cesar.devapps.finalproject.recyclerview.MyAdapter
import cesar.devapps.finalproject.recyclerview.User
import cesar.devapps.finalproject.repository.UserDb
import kotlinx.android.synthetic.main.recycler_view_data.*


class RecyclerViewActivity : AppCompatActivity() {
    private lateinit var adapter: MyAdapter
    val TAG = RecyclerViewActivity::class.java.simpleName
    internal var db = UserDb(this)
    fun showToast(text: String){
        //this@activity serve oara referenciar a própria activity
        Toast.makeText(this,text, Toast.LENGTH_LONG).show()
    }
    fun handleInsertions(){
        floatingActionButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle("Nova atividade")
            val dialogLayout = inflater.inflate(R.layout.edit_text, null)

            val editTextDescription = dialogLayout.findViewById<EditText>(R.id.editTextDescription)
            val weekday = dialogLayout.findViewById<Spinner>(R.id.item_weekday)

            Log.d(TAG,editTextDescription.text.toString()+"\n"+weekday)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") {
                dialogInterface, i ->
                val user = User(
                editTextDescription.text.toString(), weekday.selectedItem.toString()
            )
                adapter.addUserActivity(user,db,intent.getStringExtra("user_id"))
                printUserActivities()

            }
            builder.show()
        }
    }

    fun updateActivity(){
        adapter.updateUserActivity(db)
    }

    fun printUserActivities(){
        val res = UserDb(this@RecyclerViewActivity).allDataUserActivity
        try{
            if(res.count==0){
                Log.d(TAG, "Tabela de atividades do usuário vazia")
                showToast("nehuma atividade cadastrada")

            }else {
                val buffer = StringBuffer()
                while (res.moveToNext()) {
                    buffer.append("ACTIVITY_ID :" + res.getString(0) + "\n")
                    buffer.append("ACTIVITY_DESCRIPTION :" + res.getString(1) + "\n")
                    buffer.append("ACTIVITY_WEEKDAY :" + res.getString(2) + "\n")
                    buffer.append("ACTIVITY_FK_USER :" + res.getString(3) + "\n")


                }
                Log.d(TAG, buffer.toString())
            }
        }catch(e: Exception) {
            e.printStackTrace()

            Log.d(TAG,e.message.toString())
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_view_data)
        recycler_view_data.layoutManager = LinearLayoutManager(this)
        adapter =
            MyAdapter(mutableListOf<User>())
        recycler_view_data.adapter = adapter

        login_data.text = "Bem-vindo " + "${intent.getStringExtra("loginOk")}"

        //loadActivities()
        handleInsertions()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.recycler_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       when(item.itemId){
           R.id.item1 -> {val intent = Intent(this, Distraction::class.java)
               startActivity(intent)
                return true}

           R.id.item2 -> {val intent = Intent(this, MapsActivity::class.java)
               startActivity(intent)
               return true}
       }
        return super.onOptionsItemSelected(item)
    }
}
