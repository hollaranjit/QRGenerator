package com.example.qrcodegenerator.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qrcodegenerator.R
import com.example.qrcodegenerator.model.User

class UserAdapter(mContext: Context, mUsers:List<User>): RecyclerView.Adapter<UserAdapter.ViewHolder?>()
{

    private  val mContext    : Context = mContext
    private  val mUsers      : List<User> = mUsers

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {

        var userNameTxt : TextView
        var userZucitectIDTxt : TextView
        var userDeviceIDTxt : TextView
        var userDateOfIssue : TextView


        init {

            userNameTxt = itemView.findViewById(R.id.fbUsename)
            userZucitectIDTxt = itemView.findViewById(R.id.fbZucitechId)
            userDeviceIDTxt = itemView.findViewById(R.id.fbDeviceId)
            userDateOfIssue = itemView.findViewById(R.id.fbdateOfIssue)

        }


    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view:View = LayoutInflater.from(mContext).inflate(R.layout.content_employee_status,viewGroup,false)

        return UserAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: User = mUsers[position]
        holder.userNameTxt.text = user!!.getUserName()
        holder.userZucitectIDTxt.text = user!!.getZucitechID()
        holder.userDeviceIDTxt.text = user!!.getDeviceID()
        holder.userDateOfIssue.text = user!!.getDateOfIssue()



        if(user!!.getDateOfIssue().isNullOrBlank())
            Log.i("zucitechID","emty string")
        else
            Log.i("zucitechID",user!!.getDateOfIssue().toString())


    }

    override fun getItemCount(): Int {
        return mUsers.size
    }
}