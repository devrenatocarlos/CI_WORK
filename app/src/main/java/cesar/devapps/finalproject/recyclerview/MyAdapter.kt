package cesar.devapps.finalproject.recyclerview

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cesar.devapps.finalproject.R
import cesar.devapps.finalproject.R.layout.edit_text
import cesar.devapps.finalproject.repository.UserDb
import kotlinx.android.synthetic.main.edit_text.view.*

class MyAdapter (private  val users: MutableList<User>): RecyclerView.Adapter<MyViewHolder>(){
    val TAG = MyAdapter::class.java.simpleName


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_line_view,parent,false)


        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {

        return users.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = users[position]

        holder?.let {
            holder.description.text = user.description
            holder.weekday.text = user.weekday
            holder.delete.setOnClickListener {
                users.removeAt(position)
                notifyItemRemoved(position)
            }

        holder.update.setOnClickListener {
            val dialogLayout = LayoutInflater.from(holder.itemView.context).inflate(edit_text, null)
            val builder = AlertDialog.Builder(holder.itemView.context)

            ///dialogLayout.editTextDescription.text = user.

            builder.setView(dialogLayout)
            builder.setTitle("atualizar registro")
            builder.setPositiveButton("OK"){
                    dialogInterface: DialogInterface, i: Int ->
                var nameValue =  dialogLayout.editTextDescription.text.toString()
                var weekday = dialogLayout.item_weekday.toString()
                val user =
                    User(nameValue, weekday)
                users.set(position,user)
                notifyItemChanged(position)
            }
            builder.show()
            }
        }
    }

    fun addUserActivity(user: User,db :UserDb,user_id: String){
        users.add(user)

        notifyItemInserted(itemCount)
        db.insertDataActivvity(user.description,user.weekday,user_id.toInt())
        Log.w(TAG,"funcao addUserActivity ${user.description}")
    }

   fun updateUserActivity(db:UserDb){

   }
}




