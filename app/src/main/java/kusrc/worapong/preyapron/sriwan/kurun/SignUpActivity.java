package kusrc.worapong.preyapron.sriwan.kurun;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    //Explicit
    private EditText nameEditText, surnameEditText, idSTUEditText,
            userEditText, passwordEditText;
    private Spinner yearSpinner;
    private RadioGroup avataRadiogroup;
    private RadioButton avata1RadioButton, avata2RadioButton,
            avata3RadioButton, avata4RadioButton, avata5RadioButton;

    private String nameString, surnameString, idSTUString,
            yearString, userString, passwordString, avataString = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Bind Widget
        bindWidget();

        //Create Year Spinner
        createYearSpinner();

        //Radio Controller
        radioController();

    } //main method

    private void radioController() {

        avataRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.radioButton:
                        avataString = "0";
                        break;
                    case R.id.radioButton2:
                        avataString = "1";
                        break;
                    case R.id.radioButton3:
                        avataString = "2";
                        break;
                    case R.id.radioButton4:
                        avataString = "3";
                        break;
                    case R.id.radioButton5:
                        avataString = "4";
                        break;
                }

            }   // onChecked
        });

    }   // radioController

    private void createYearSpinner() {

        final String[] yearStrings = {"ปี 1", "ปี 2", "ปี 3", "ปี 4", "มากกว่า ปี 4", "อาจารย์"};

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, yearStrings);
        yearSpinner.setAdapter(stringArrayAdapter);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                yearString = yearStrings[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                yearString = yearStrings[0];
            }
        });

    }   // createYearSpinner

    public void clickSignUpSign(View view) {

        nameString = nameEditText.getText().toString().trim();
        surnameString = surnameEditText.getText().toString().trim();
        idSTUString = idSTUEditText.getText().toString().trim();
        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        //Check Space
        if (checkSpace()) {
            //Have Space
            MyAlertDialog myAlertDialog = new MyAlertDialog();
            myAlertDialog.myDialog(SignUpActivity.this,
                    "มีช่องว่าง",
                    "กรุณากรอก ทุกช่องคะ");

        } else {
            //No Space
            confirmData();

        }

    }   // clickSignUp

    private void confirmData() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(findAvata(avataString));
        builder.setTitle(nameString + " " + surnameString);
        builder.setMessage("โปรดตรวจข้อมูล" + "\n" +
        getResources().getString(R.string.ID_Student) + " " + idSTUString + "\n" +
        getResources().getString(R.string.Year) + " " + yearString + "\n" +
        getResources().getString(R.string.User) + " " + userString + "\n" +
        getResources().getString(R.string.Password) + " " + passwordString);

        builder.setPositiveButton("บันทึก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                updateDataToMySQL();
                dialogInterface.dismiss();

            }   // onClick
        });

        builder.setNegativeButton("แก้ไข", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        builder.show();




    }   // confirmData

    private void updateDataToMySQL() {

        //Connected Http
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("isAdd", "true"));
            nameValuePairs.add(new BasicNameValuePair("Name", nameString));
            nameValuePairs.add(new BasicNameValuePair("Surname", surnameString));
            nameValuePairs.add(new BasicNameValuePair("ID_Student", idSTUString));
            nameValuePairs.add(new BasicNameValuePair("Year", yearString));
            nameValuePairs.add(new BasicNameValuePair("User", userString));
            nameValuePairs.add(new BasicNameValuePair("Password", passwordString));
            nameValuePairs.add(new BasicNameValuePair("Avata", avataString));

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://swiftcodingthai.com/keng/php_add_user_master.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            httpClient.execute(httpPost);

            Toast.makeText(SignUpActivity.this,
                    "บันทึกข้อมูลเรียบร้อย ขอบคุณคะ", Toast.LENGTH_SHORT).show();
            finish();

        } catch (Exception e) {
            Toast.makeText(SignUpActivity.this,
                    "ไม่สามารถเชื่อมต่อ Server ได้", Toast.LENGTH_SHORT).show();
        }



    }   // updateToMySQL

    private int findAvata(String avataString) {

        FindAvata findAvata = new FindAvata();
        int intAvata = findAvata.myFindAvata(avataString);

        return intAvata;
    }

    private boolean checkSpace() {

        boolean bolStatus = nameString.equals("") ||
                surnameString.equals("") ||
                idSTUString.equals("") ||
                userString.equals("") ||
                passwordString.equals("");

        return bolStatus;
    }

    private void bindWidget() {

        nameEditText = (EditText) findViewById(R.id.editText3);
        surnameEditText = (EditText) findViewById(R.id.editText4);
        idSTUEditText = (EditText) findViewById(R.id.editText5);
        userEditText = (EditText) findViewById(R.id.editText7);
        passwordEditText = (EditText) findViewById(R.id.editText8);
        yearSpinner = (Spinner) findViewById(R.id.spinner);
        avataRadiogroup = (RadioGroup) findViewById(R.id.ragAvata);
        avata1RadioButton = (RadioButton) findViewById(R.id.radioButton);
        avata2RadioButton = (RadioButton) findViewById(R.id.radioButton2);
        avata3RadioButton = (RadioButton) findViewById(R.id.radioButton3);
        avata4RadioButton = (RadioButton) findViewById(R.id.radioButton4);
        avata5RadioButton = (RadioButton) findViewById(R.id.radioButton5);


    } //bindWidget
} //Main class