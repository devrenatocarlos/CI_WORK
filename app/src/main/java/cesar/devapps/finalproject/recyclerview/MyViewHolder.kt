package cesar.devapps.finalproject.recyclerview

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cesar.devapps.finalproject.R
import kotlinx.android.synthetic.main.main_line_view.view.*

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var description: TextView = itemView.item_description
    var weekday: TextView = itemView.item_weekday  //Verficar se o cast funcionou
    var delete: ImageButton = itemView.findViewById(R.id.button_delete)
    var update: ImageButton = itemView.findViewById(R.id.button_update)
}
