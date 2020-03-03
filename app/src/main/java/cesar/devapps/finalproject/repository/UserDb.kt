package cesar.devapps.finalproject.repository

//criar operaçoes do banco com a helper class baseadad no sqliteHelper
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


open class UserDb (context: Context ) : SQLiteOpenHelper(context,
    DATABASE_NAME,null,1){
    val TAG = UserDb::class.java.simpleName
    // objeto complementar
    companion object{

       const val DATABASE_NAME = "advanced_android.db"

       const val TABLE_USER = "USER_TB"
       const val USER_ID = "USER_ID"
       const val USER_NAME = "USER_NAME"
       const val USER_CPF = "USER_CPF"
       const val USER_LOGIN = "USER_LOGIN"
       const val USER_PASSWORD = "USER_PASSWORD"
       const val USER_TIMESTAMP = "USER_TIMESTAMP"

       const val TABLE_ACTIVITY = "TABLE_ACTIVITY"
       const val ACTIVITY_ID = "ACTIVITY_ID"
       const val ACTIVITY_DESCRIPTION = "ACTIVITY_DESCRIPTION"
       const val ACTIVITY_WEEKDAY = "ACTIVITY_WEEKDAY"
       const val ACTIVITY_FK_USER_ID = "ACTIVITY_FK_USER_ID"

     }

    //seu metodo onCreaten. Chamado quando o banco de dados é criado pela primeira vez. Aqui será criada as tabelas e sua população inicial
    override fun onCreate(db:SQLiteDatabase?){
        val CREATE_TABLE_USER =
            "CREATE TABLE if not exists " + TABLE_USER + " (" +
                    "$USER_ID integer PRIMARY KEY autoincrement," +
                    "$USER_NAME text," +
                    "$USER_CPF text,"+
                    "$USER_LOGIN text,"+
                    "$USER_PASSWORD text,"+
                    "$USER_TIMESTAMP text"+
                    ");"

        Log.d(TAG, "Creating: $CREATE_TABLE_USER")
        db?.execSQL(CREATE_TABLE_USER)

       val CREATE_TABLE_ACTIVITY =
        "CREATE TABLE if not exists " + TABLE_ACTIVITY + " (" +
                "$ACTIVITY_ID integer PRIMARY KEY autoincrement," +
                "$ACTIVITY_DESCRIPTION text,"+
                "$ACTIVITY_WEEKDAY text," +
                "$ACTIVITY_FK_USER_ID integer,"+
                "FOREIGN KEY ($ACTIVITY_FK_USER_ID) REFERENCES $TABLE_USER($USER_ID)" +
                ");"

        Log.d(TAG, "Creating: $CREATE_TABLE_ACTIVITY")
        db?.execSQL(CREATE_TABLE_ACTIVITY)

    }

    override fun onUpgrade(db:SQLiteDatabase?, oldVersion: Int, newVersion: Int){
        //exclui tabela e em seguida a recria
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ACTIVITY")
        onCreate(db)
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true);
    }

    fun insertUserData(name: String, cpf: String, login: String, password: String) {
        val contentValues = ContentValues()
        contentValues.put(USER_NAME,name)
        contentValues.put(USER_CPF,cpf)
        contentValues.put(USER_LOGIN,login)
        contentValues.put(USER_PASSWORD,password)
        contentValues.put(USER_TIMESTAMP,System.currentTimeMillis())
        writableDatabase.insert(TABLE_USER, null, contentValues);
    }

    fun insertData(name: String, cpf: String, login: String, password: String){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val timeStamp = current.format(formatter).toString()
        contentValues.put(USER_NAME,name)
        contentValues.put(USER_CPF,cpf)
        contentValues.put(USER_LOGIN,login)
        contentValues.put(USER_PASSWORD,password)
        contentValues.put(USER_TIMESTAMP,timeStamp)
        db.insert(TABLE_USER,null,contentValues)
    }

    fun insertDataActivvity(description: String, weekday: String, fk_user_id: Int){
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(ACTIVITY_DESCRIPTION,description)
        contentValues.put(ACTIVITY_WEEKDAY,weekday)
        contentValues.put(ACTIVITY_FK_USER_ID,fk_user_id)


        db.insert(TABLE_ACTIVITY,null,contentValues)
    }

    fun updateUserData(id: Int,name: String,cpf: String,login: String, password: String, timestamp: String) {
        val contentValues = ContentValues()
        contentValues.put(USER_ID,id)
        contentValues.put(USER_NAME,name)
        contentValues.put(USER_CPF,cpf)
        contentValues.put(USER_LOGIN,login)
        contentValues.put(USER_PASSWORD,password)
        contentValues.put(USER_TIMESTAMP,System.currentTimeMillis())
        getWritableDatabase().update(TABLE_USER, contentValues, "$USER_ID=${id}", null)
    }

    //atualizar atividades de um usuários
    fun updateData(id: String,name: String,cpf: String,login: String, password: String, timestamp: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(USER_ID,id)
        contentValues.put(USER_NAME,name)
        contentValues.put(USER_CPF,cpf)
        contentValues.put(USER_LOGIN,login)
        contentValues.put(USER_PASSWORD,password)
        contentValues.put(USER_TIMESTAMP,timestamp)
        db.update(TABLE_USER,contentValues, "USER_ID = ?", arrayOf(id))
        return true
    }

    fun updateActivity(id: String,name: String,description: String,weekday: String, fk_user_id: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(ACTIVITY_DESCRIPTION,description)
        contentValues.put(ACTIVITY_WEEKDAY,weekday)
        contentValues.put(ACTIVITY_FK_USER_ID,fk_user_id)
        db.update(TABLE_USER,contentValues, "USER_ID = ?", arrayOf(id))
        return true
    }

    fun removeUserTable(id: Int): Int {
        return writableDatabase.delete(TABLE_USER, "$USER_ID=${id}", null)
    }
    //deletar uma linha da tabela
    fun deleteData(id: String): Int{
        val db = this.writableDatabase
        return db.delete(TABLE_USER,"USER_ID = ?",arrayOf(id))
    }

    fun getUserData(id: Int) : Cursor {
        return getReadableDatabase()
            .query(
                TABLE_USER, arrayOf(
                    USER_ID,
                    USER_NAME,
                    USER_CPF,
                    USER_LOGIN,
                    USER_PASSWORD,
                    USER_TIMESTAMP
                ), "$USER_ID=${id}", null, null, null, null)
    }

    fun getUserAllData(): Cursor{
        return readableDatabase
            .query(
                TABLE_USER, arrayOf(
                    USER_ID,
                    USER_NAME,
                    USER_CPF,
                    USER_LOGIN,
                    USER_PASSWORD,
                    USER_TIMESTAMP
                ), null, null, null, null, null)
    }

    // retorna o cursor contendo os dados
    val allDataUser: Cursor
        get(){
            val db = this.writableDatabase
            val res = db.rawQuery("SELECT * FROM $TABLE_USER",null)
            return res
        }

    val allDataUserActivity: Cursor
        get(){
            val db = this.writableDatabase
            val res = db.rawQuery("SELECT * FROM $TABLE_ACTIVITY",null)
            return res
        }
}