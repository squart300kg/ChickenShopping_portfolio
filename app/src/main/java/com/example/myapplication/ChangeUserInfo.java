package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeUserInfo extends AppCompatActivity {

    EditText id;
    EditText currentPassword;
    EditText changePassword;
    EditText changeRePassword;
    EditText name;
    EditText phone;
    EditText email;
    EditText address;

    MySQLiteHelper helper;
    SQLiteDatabase database;
    Cursor cursor;

    static Activity changeUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);

        changeUserInfo = ChangeUserInfo.this;

        currentPassword = findViewById(R.id.userCurrentPassword);
        changePassword = findViewById(R.id.userChangePassword);
        changeRePassword = findViewById(R.id.userChangeRePassword);
        name = findViewById(R.id.changeName);
        phone = findViewById(R.id.changePhone);
        email = findViewById(R.id.changeEmail);
        address = findViewById(R.id.changeAddress);

        helper = new MySQLiteHelper(this);
        database = helper.getReadableDatabase();

        //처음 회원정보 변경창에 들어오면 기존의 정보들(비밀번호 제외)를 창에다가 적어준다.
        cursor = database.rawQuery("SELECT * FROM UserInfo WHERE id = ?", new String[]{UserInfo.getUserId()});
        if (cursor != null) {
            cursor.moveToNext();
            Log.i("회원 정보 변경 - id : ",cursor.getString(0));
            Log.i("회원 정보 변경 - pw : ",cursor.getString(1));
            Log.i("회원 정보 변경 - name : ",cursor.getString(2));

            name.setText(cursor.getString(2));
            email.setText(cursor.getString(3));
            address.setText(cursor.getString(4));
            phone.setText(cursor.getString(6));


        }

    }

    public void userInfoChangeStep(View view) {
        //현재 비밀번호르 검사한다.
        //현재 비밀번호가 일치한다면
        //새 비밀번호와 새 비밀번호 확인이 일치하는지 확인한다.
        //적혀진 정보들을 갱신한다.

//        String id2 = id.getText().toString();

        //회원정보를 다 입력하고 '회원정보 변경'버튼을 누른다면 진짜 변경할건지 마지막으로 물어본다.
        Intent intent = new Intent(this, Popup_goToMain2.class);


        String currentPassword2 = currentPassword.getText().toString();
        String changePassword2 = changePassword.getText().toString();
        String changeRePassword2 = changeRePassword.getText().toString();
        String name2 = name.getText().toString();
        String phone2 = phone.getText().toString();
        String email2 = email.getText().toString();
        String address2 = address.getText().toString();

        //미니창에서 OK를 눌렀을 때, 회원정보를 갱신하기 위해 다음 페이지로 데이터들을 넘긴다.
        intent.putExtra("currentPassword2",currentPassword2);
        intent.putExtra("changePassword2",changePassword2);
        intent.putExtra("changeRePassword2",changeRePassword2);
        intent.putExtra("name2",name2);
        intent.putExtra("phone2",phone2);
        intent.putExtra("email2",email2);
        intent.putExtra("address2",address2);


        String password_db;
        helper = new MySQLiteHelper(this);
        database = helper.getReadableDatabase();

        //처음 회원정보 변경창에 들어오면 기존의 정보들(비밀번호 제외)를 창에다가 적어준다.
        cursor = database.rawQuery("SELECT password FROM UserInfo WHERE id = ?", new String[]{UserInfo.getUserId()});
        if (cursor != null) {
            cursor.moveToNext();
//            Log.i("회원 정보 변경 - id : ",cursor.getString(0));
//            Log.i("회원 정보 변경 - pw : ",cursor.getString(1));
//            Log.i("회원 정보 변경 - name : ",cursor.getString(2));

            password_db = cursor.getString(0);
            Log.i("회원 정보 변경 - db_pw : ",password_db);

            if(password_db.equals(currentPassword2)){
                //DB속 비밀번호와 사용자가 액티비티에서 입력한 비밀번호가 같다면
                //새 비밀번호와 새 비밀번호 확인이 일치하는지 확인한다.
                if(changePassword2.equals(changeRePassword2)){
                    //새 비밀번호와 새 비밀번호 확인이 일치한다.
                    //적혀진 정보들을 갱신한다.

                    startActivity(intent);//마지막으로 정말 변경할것인지 물어보는 미니창이다.

                    //회원정보 입력 폼을 제대로 입력했다면 회원정보를 변경하시겠습니까? 미니창을 띄워준다.
                    //미니창에서 ok를 입력받으면 지금 액티비티를 종료시켜주고
                    //cancel을 입력받으면 원래대로 해준다.


                    //Toast.makeText(this, "회원 정보 변경을 완료하였습니다!", Toast.LENGTH_SHORT).show();
                }else{
                    //새 비밀번호화 새 비밀번호 확인이 일치하지 않는다.
                    Toast.makeText(this, "비밀번호와 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "현재 비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
