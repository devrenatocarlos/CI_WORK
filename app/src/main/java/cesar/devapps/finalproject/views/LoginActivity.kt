package cesar.devapps.finalproject.views
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cesar.devapps.finalproject.R
import cesar.devapps.finalproject.repository.UserDb
import kotlinx.android.synthetic.main.activity_login.*

import java.util.regex.Pattern

class LoginActivity : AppCompatActivity(){
    val TAG = LoginActivity::class.java.simpleName

    fun showToast(text: String){
        Toast.makeText(this,text, Toast.LENGTH_LONG).show()
    }

    fun clearEditTexts(){
        edit_login.setText("")
        edit_password.setText("")
    }

    fun loginValidation(loginValue: String,passwordValue: String,intent: Intent):Boolean {
        var  isLoginOk = false
        val res = UserDb(this@LoginActivity).allDataUser
        try{
            if(res.count==0){
                Log.d(TAG, "Tabela usuário vazio")
                showToast("nehum usuário cadastrado")
                return isLoginOk
            }else {
               val buffer = StringBuffer()
               Log.d(TAG,buffer.toString())
                while (res.moveToNext()) {
                    buffer.append("USER_ID :" + res.getString(0) + "\n")
                    buffer.append("USER_NAME :" + res.getString(1) + "\n")
                    buffer.append("USER_CPF :" + res.getString(2) + "\n")
                    buffer.append("USER_LOGIN :" + res.getString(3) + "\n")
                    buffer.append("USER_PASSWORD :" + res.getString(4) + "\n")
                    buffer.append("USER_TIMESTAMP :" + res.getString(5) + "\n")

                    //Log.d(TAG, buffer.toString())
                    Log.d(TAG,"login/senha:${res.getString(3)+ res.getString(4)}")
                    isLoginOk =
                        (loginValue.equals(res.getString(3)) && passwordValue.equals(
                            res.getString(4)
                        ))
                    if (isLoginOk) {
                        intent.putExtra("user_id",res.getString(0))

                        break
                    }
                }

                Log.d(TAG,buffer.toString())
            }
        }catch(e: Exception) {
            e.printStackTrace()

            Log.d(TAG,e.message.toString())
        }

        println("valor de isLoginOk "+ isLoginOk)
        return isLoginOk
    }

    fun handleRead(intent: Intent){
        button_confirm.setOnClickListener {
           // Log.d(TAG,"retorno de login validation ${loginValidation(edit_login.text.toString(),edit_password.text.toString())}")
            val regexpLogin = Pattern.compile("\\w") //your regexp here
            val matcherLogin = regexpLogin.matcher(edit_login.text.toString())

            val regexpPwd = Pattern.compile("[A-Z].+") //your regexp here
            val matcherPwd = regexpPwd.matcher(edit_password.text.toString())

            if(!matcherLogin.find())
                showToast("Login com formato inválido")
            else if(!matcherPwd.find())
                showToast("Senha com formato inválido")
                else {
                if (loginValidation(
                        edit_login.text.toString(),
                        edit_password.text.toString(),
                        intent
                    )
                ) {

                    intent.putExtra("loginOk", edit_login.text.toString())
                    clearEditTexts()
                    startActivity(intent)
                } else {
                    showToast("Login ou senha inválido")
                    clearEditTexts()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val intent = Intent(this, RecyclerViewActivity::class.java)
        handleRead(intent)

        button_cancel.setOnClickListener {
            finish()
        }
    }
}
