package cesar.devapps.finalproject.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cesar.devapps.finalproject.R
import cesar.devapps.finalproject.repository.UserDb
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {

    val TAG = RegisterActivity::class.java.simpleName
    //metodo para exibir os dados inseridos nos campos de textos


    fun showToast(text: String) {
        //this@activity serve oara referenciar a própria activity
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    fun clearEditTexts() {
        edit_name.setText("")
        edit_cpf.setText("")
        edit_login.setText("")
        edit_password.setText("")
    }

    fun handleInsertions() {
        button_confirm.setOnClickListener {
            try {

                val regexpEmail = Pattern.compile("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])") //your regexp here
                val matcherEmail = regexpEmail.matcher(edit_name.text.toString())

                val regexpCPF = Pattern.compile("\\d") //your regexp here
                val matcherCPF = regexpCPF.matcher(edit_cpf.text.toString())

                val regexpLogin = Pattern.compile("\\w") //your regexp here
                val matcherLogin = regexpLogin.matcher(edit_login.text.toString())

                val regexpPwd = Pattern.compile("[A-Z].+") //your regexp here
                val matcherPwd = regexpPwd.matcher(edit_password.text.toString())



                if (!matcherEmail.find())
                    showToast("Email com formato inválido")
                else if(!matcherCPF.find())
                    showToast("CPF com formato inválido")
                else if(!matcherLogin.find())
                    showToast("Login com formato inválido")
                else if(!matcherPwd.find())
                    showToast("Senha com formato inválido")
                else{
                    val db = UserDb(this@RegisterActivity)
                    db.insertData(
                        edit_name.text.toString(),
                        edit_cpf.text.toString(),
                        edit_login.text.toString(),
                        edit_password.text.toString()
                    )
                    val intent = Intent(this, LoginActivity::class.java)
                    // intent.putExtra("loginOk",edit_login.text.toString())
                    showToast("${edit_login.text.toString()} Registrado com sucesso")
                    Log.d(TAG, "inseriu ${edit_name.text.toString()} no banco de dados")
                    startActivity(intent)
                    clearEditTexts()
                }
            }catch(e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "Erro ao inserir dados no banco")
            }
        }

        val buttonCancel = findViewById<Button>(R.id.button_cancel)
        buttonCancel.setOnClickListener {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        handleInsertions()

    }

}
