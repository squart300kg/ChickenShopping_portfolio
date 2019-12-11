package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;

public class SearchIdPassword extends AppCompatActivity {

    MySQLiteHelper helper;
    SQLiteDatabase database;
    Cursor cursor;

    public static Activity searchIdPassword;//나중에 추후에 이 창을 닫으려고 스태틱으로 선언했다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchidpassword);
        searchIdPassword = SearchIdPassword.this;//추후에 이 액티비티를 종료하기 위해 선언했다.
    }
    public void onClickSearch(View view){
        LinearLayout searchId = findViewById(R.id.searchId);
        LinearLayout searchPassword = findViewById(R.id.searchPassword);

        switch(view.getId()){
            case R.id.searchidbutton:
                searchPassword.setVisibility(View.GONE);
                searchId.setVisibility(View.VISIBLE);
                break;
            case R.id.searchpasswordbutton:
                searchId.setVisibility(View.GONE);
                searchPassword.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void search(View view){
        int is_Id_Exists = 0;
        EditText userName = findViewById(R.id.userName);
        EditText userEmail = findViewById(R.id.userEmail);


        String userNameStr = userName.getText().toString();
        String userEmailStr = userEmail.getText().toString();
        String userIdStr;
        //유저의 이름을 기운으로 정보를 찾는다.
        helper = new MySQLiteHelper(this);
        database = helper.getReadableDatabase();

        //해당 정보(아이디, 이메일)를 가진 회원의 아이디를 추출하는 쿼리문이다.
        cursor = database.rawQuery("SELECT id FROM UserInfo WHERE name = ? AND email = ?", new String[]{userNameStr, userEmailStr});
        if(cursor != null && cursor.moveToFirst()){

            userIdStr = cursor.getString(0);
            Log.i("아이디 찾기 : ",userIdStr);
            Intent intent = new Intent(this, Popup_IdSearch.class);
            intent.putExtra("id",userIdStr);
            startActivity(intent);
        }else{
            Log.i("아이디 찾기 : ", "아이디가 없음");
            Toast.makeText(this, "입력하신 정보가 올바르지 않습니다!", Toast.LENGTH_SHORT).show();
        }
    }

    public void passwordSearch(View view) {
        String userPassword;//해쉬 알고리즘으로 변환할 사용자의 비밀번호이다.

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());


        EditText passwordUserId = findViewById(R.id.passwordUserId);
        EditText passwordUserName = findViewById(R.id.passwordUserName);
        EditText passwordUserEmail = findViewById(R.id.passwordUserEmail);

        String userId = passwordUserId.getText().toString();
        String userName = passwordUserName.getText().toString();
        String userEmail = passwordUserEmail.getText().toString();

        helper = new MySQLiteHelper(this);
        database = helper.getReadableDatabase();
        //입력받은 유저의 아이디, 이름, 이메일을 검사한다.

        cursor = database.rawQuery("SELECT password FROM UserInfo WHERE id = ? AND name = ? AND email = ?", new String[]{userId, userName, userEmail});
        if(cursor != null && cursor.moveToFirst()){
            //액티비티에서 입력받은 3개의 정보에 일치하는 비밀번호가 있다면!

            //회원님의 이메일로 변경된 비밀번호를 전송했다는 미니창을 띄워준다.
            startActivity(new Intent(this, Popup_goToMain.class));//확인창을 누르면 로그인창으로 이동한다.
 
            Log.i("비번 찾기 : ",cursor.getString(0));
            userPassword = cursor.getString(0);
            //비번찾기해서 값을 올바르게 가져 왔다. 이제 이 값을 해쉬값으로 바꿔주고 DB에 다시 저장시킨다
            //이와 동시에 이메일로 해당 비밀번호를 보내준다.

            //========================SHA256 해쉬 알고리즘이다.
            //===========사용자의 비밀번호를 암호화한다.
            try{
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(userPassword.getBytes("UTF-8"));
                StringBuffer hexString = new StringBuffer();

                for (int i = 0; i < hash.length; i++) {
                    String hex = Integer.toHexString(0xff & hash[i]);
                    if(hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
                }
                userPassword = hexString.toString();//사용자에게 알려질, 사용자의 이메일에 보내질, DB에 저장될
                                                    //암호화된 비밀번호이다.
//                System.out.println(hexString.toString());
            } catch(Exception ex){
                throw new RuntimeException(ex);
            }


            try {
                GMailSender gMailSender = new GMailSender("a01039329810@gmail.com", "356vkf7593.");
                //GMailSender.sendMail(제목, 본문내용, 받는사람);
                gMailSender.sendMail("[파워닭 닭가슴살 쇼핑몰]비밀번호 찾기 서비스", "회원님의 비밀번호는 ["+userPassword+"]입니다! \n회원정보를 변경 후 이용해 주세요. 좋은 하루 되세요^^", userEmail);
                Toast.makeText(getApplicationContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
                //회원님의 이메일로 비밀번호를 전송해 드렸습니다. 를 띄워주는 미니 창
            } catch (SendFailedException e) {
                Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();

            } catch (MessagingException e) {
                Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
            
            TextView result = findViewById(R.id.passwordSearchResult);
            result.setVisibility(View.VISIBLE);

            //이메일로 비밀번호를 전송했으면 이제 비밀번호를 저장해야 한다.
            database = helper.getWritableDatabase();
            database.execSQL("UPDATE UserInfo SET password = '"+userPassword+"' WHERE id = '"+userId+"'");


        }else{
            Log.i("아이디 찾기 : ", "아이디가 없음");
            Toast.makeText(this, "입력하신 정보가 올바르지 않습니다!", Toast.LENGTH_SHORT).show();
        }
    }
}
