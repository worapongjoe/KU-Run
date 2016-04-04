package kusrc.worapong.preyapron.sriwan.kurun;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private MyManage myManage;
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Widget
        bindWidget();

        //Request Database
        myManage = new MyManage(this);

        //Tester Add Value
        //testAddValue();

        //Delete All SQlite
        deleteAllSQLite();

        //Synchronize JSON to SQLite
        synJSONtoSQLite();

    }   // Main Method

    private void bindWidget() {
        userEditText = (EditText) findViewById(R.id.editText2);
        passwordEditText = (EditText) findViewById(R.id.editText);
    }

    public void clickSignInMain(View view) {

        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        //Check Space
        if (userString.equals("") || passwordString.equals("")) {
            //Have Space
            MyAlertDialog myAlertDialog = new MyAlertDialog();
            myAlertDialog.myDialog(MainActivity.this,
                    "มีช่องว่าง", "กรุณากรอกทุกช่องคะ");

        } else {
            //No Space
            checkUser();
        }


    }   // clickSingIn

    private void checkUser() {

        try {

            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase
                    .rawQuery("SELECT * FROM userTABLE WHERE User = " + "'" + userString + "'", null);
            cursor.moveToFirst();
            String[] resultStrings = new String[cursor.getColumnCount()];
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                resultStrings[i] = cursor.getString(i);
            }   // for
            cursor.close();

            //Check Password
            if (passwordString.equals(resultStrings[6])) {
                //Password True
                Toast.makeText(this, "ยินดีต้อนรับ " + resultStrings[1], Toast.LENGTH_SHORT).show();

            } else {
                //Password False
                MyAlertDialog myAlertDialog = new MyAlertDialog();
                myAlertDialog.myDialog(this, "Password False",
                        "กรุณาพิมพ์ใหม่ Password ผิด");

            }



        } catch (Exception e) {
            MyAlertDialog myAlertDialog = new MyAlertDialog();
            myAlertDialog.myDialog(this, "ไม่มี User นี้",
                    "ไม่มี " + userString + " ในฐานข้อมูลของฉัน");
        }

    }   // checkUser


    @Override
    protected void onRestart() {
        super.onRestart();

        deleteAllSQLite();
        synJSONtoSQLite();

    }

    private void synJSONtoSQLite() {

        //Connected Http
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        int intTABLE = 0;
        String[] urlTABLEStrings = {"http://swiftcodingthai.com/keng/php_get_user_master.php"};

        while (intTABLE <= 0) {

            //1 Create InputStream
            InputStream inputStream = null;
            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(urlTABLEStrings[intTABLE]);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();

            } catch (Exception e) {
                Log.d("KuRun", "InputStream ==> " + e.toString());
            }

            //2 Create JSON String
            String strJSON = null;
            try {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String strLine = null;
                while ((strLine = bufferedReader.readLine()) != null) {
                    stringBuilder.append(strLine);
                }
                inputStream.close();
                strJSON = stringBuilder.toString();

            } catch (Exception e) {
                Log.d("KuRun", "JSON String ==> " + e.toString());
            }

            //3 Update SQLite
            try {

                JSONArray jsonArray = new JSONArray(strJSON);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    switch (intTABLE) {
                        case 0:

                            String strName = jsonObject.getString(MyManage.column_Name);
                            String strSurname = jsonObject.getString(MyManage.column_Surname);
                            String strIDstudent = jsonObject.getString(MyManage.column_ID_Student);
                            String strYear = jsonObject.getString(MyManage.column_Year);
                            String strUser = jsonObject.getString(MyManage.column_User);
                            String strPassword = jsonObject.getString(MyManage.column_Password);
                            String strAvata = jsonObject.getString(MyManage.column_Avata);

                            myManage.addUser(strName, strSurname, strIDstudent,
                                    strYear, strUser, strPassword, strAvata);

                            break;
                    }   // switch
                }   // for

            } catch (Exception e) {
                Log.d("KuRun", "UPdate ==> " + e.toString());
            }


            intTABLE += 1;
        }   // while


    }   // synJSON

    private void deleteAllSQLite() {
        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                MODE_PRIVATE, null);
        sqLiteDatabase.delete(MyManage.user_table, null, null);
    }

    private void testAddValue() {
        myManage.addUser("name", "sur", "id", "2016", "user", "12345", "3");
    }

    public void clickSignUpMain(View view) {
        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
    }
}   // Main Class