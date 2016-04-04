package kusrc.worapong.preyapron.sriwan.kurun;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by masterUNG on 3/8/16 AD.
 */
public class MyManage {

    //Explicit
    private MyOpenHelper myOpenHelper;
    private SQLiteDatabase writeSqLiteDatabase, readSqLiteDatabase;

    public static final String user_table = "userTABLE";
    public static final String column_id = "_id";
    public static final String column_Name = "Name";
    public static final String column_Surname = "Surname";
    public static final String column_ID_Student = "ID_Student";
    public static final String column_Year = "Year";
    public static final String column_User = "User";
    public static final String column_Password = "Password";
    public static final String column_Avata = "Avata";

    public MyManage(Context context) {

        //Create & Connected Database
        myOpenHelper = new MyOpenHelper(context);
        writeSqLiteDatabase = myOpenHelper.getWritableDatabase();
        readSqLiteDatabase = myOpenHelper.getReadableDatabase();

    }   // Constructor

    public String[] searchUser(String strUser) {

        try {

            String[] resultStrings = null;



        } catch (Exception e) {
            return null;
        }

        return new String[0];
    }


    public long addUser(String strName,
                        String strSurname,
                        String strIDstudent,
                        String strYear,
                        String strUser,
                        String strPassword,
                        String strAvata) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(column_Name, strName);
        contentValues.put(column_Surname, strSurname);
        contentValues.put(column_ID_Student, strIDstudent);
        contentValues.put(column_Year, strYear);
        contentValues.put(column_User, strUser);
        contentValues.put(column_Password, strPassword);
        contentValues.put(column_Avata, strAvata);

        return writeSqLiteDatabase.insert(user_table, null, contentValues);
    }


}   // Main Class
