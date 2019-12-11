package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONObject;

import java.lang.ref.WeakReference;


public class Login extends AppCompatActivity {

    public static Activity login;

    String password_db;
    MySQLiteHelper helper;
    SQLiteDatabase database;
    String userEmail;

    private static String OAUTH_CLIENT_ID = "2fdXl1490ye5EcJ20jMO";
    private static String OAUTH_CLIENT_SECRET = "M5E8JCYdwJ";
    private static String OAUTH_CLIENT_NAME = "파워닭 닭가슴살 쇼핑몰";

    private static OAuthLogin naverLoginInstance;
    private OAuthLoginButton naverLogInButton;
    private static Context context;


    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        login = Login.this;

        init();
        init_View();
    }
    private void init(){
        context = this;
        naverLoginInstance = OAuthLogin.getInstance();
        naverLoginInstance.init(this,OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
    }
    private void init_View(){
        naverLogInButton = findViewById(R.id.buttonNaverLogin);


        //로그인 핸들러
        OAuthLoginHandler naverLoginHandler  = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {//로그인 성공

                    //DB에서 해당 이메일을 가지고 있는 회원을 검색한다. 검색해서 있으면
                    //1. 로그인 시켜준다 UserInfo에 회원정보 저장
                    //2. 로그인이 완료되었으니까 메인으로 이동시켜준다.

                    String accessToken = naverLoginInstance.getAccessToken(context);
                    String refreshToken = naverLoginInstance.getRefreshToken(context);
                    long expiresAt = naverLoginInstance.getExpiresAt(context);
                    String tokenType = naverLoginInstance.getTokenType(context);

                    new RequestApiTask().execute();

                    Log.i("로그인 - 접근토근 : ",accessToken);
                    Log.i("로그인 - 갱신토근 : ", refreshToken);
                    Log.i("로그인 - 만료시간 : ",expiresAt+"");
                    Log.i("로그인 - 토근타입 : ",tokenType);
                } else {//로그인 실패
                    String errorCode = naverLoginInstance.getLastErrorCode(context).getCode();
                    String errorDesc = naverLoginInstance.getLastErrorDesc(context);
                    Log.i("로그인 - errorCode : ",errorCode);
                    Log.i("로그인 - errorDesc : ", errorDesc);
                    Toast.makeText(context, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                }
            }

        };
        naverLogInButton.setOAuthLoginHandler(naverLoginHandler);

    }
    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/me";
            String at = naverLoginInstance.getAccessToken(context);

            return naverLoginInstance.requestApi(context, at, url);
        }

        protected void onPostExecute(String content) {
            try{
                JSONObject jsonObject = new JSONObject(content);
                JSONObject response = jsonObject.getJSONObject("response");
                userEmail = response.getString("email");

                Log.i("로그인 - 이메일 : ",userEmail);

                helper = new MySQLiteHelper(getApplicationContext());
                database = helper.getReadableDatabase();
                //우선 첫번쨰로 UI에서 입력된 회원정보가 DB에 있는지 검사한다.
                cursor = database.rawQuery("SELECT * FROM UserInfo WHERE email = ?", new String[]{userEmail});

                if (cursor != null) {
                    cursor.moveToNext();
                    UserInfo.setUserId(cursor.getString(0));
                    UserInfo.setUserPassword(cursor.getString(1));
                    UserInfo.setUserName(cursor.getString(2));
                    UserInfo.setUserEmail(cursor.getString(3));
                    UserInfo.setUserAddress(cursor.getString(4));
                    UserInfo.setPoint(cursor.getInt(5));
                    UserInfo.setUserPhone(cursor.getString(6));
                    UserInfo.isLogin = true;

                    //네아로 연동으로 로그인에 성공했다.
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));//네아로 연동에 성공하면 메인으로 보내준다.
                    return;
                }else{
                    //Toast.makeText(getApplicationContext(), "회원님의 이메일과 네이버 이메일이 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Popup_Naver_Login.class));
                    //네아로 연동에 실패하면 경고창을 띄워준다.
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    public void login(View view) throws SQLiteException{
        //로그인을 성공하면 로그인을 성공했다는 상태변수를 true로 바꿔주고 아니면 다시 로그인 창으로 돌려준다.
        int is_Id_Exists;
        EditText userId = findViewById(R.id.userId);
        EditText userPassword = findViewById(R.id.userPassword);

        String userIdStr = userId.getText().toString();
        String userPasswordStr = userPassword.getText().toString();

        helper = new MySQLiteHelper(this);
        database = helper.getReadableDatabase();
        //우선 첫번쨰로 UI에서 입력된 회원정보가 DB에 있는지 검사한다.
        cursor = database.rawQuery("SELECT COUNT(password) FROM UserInfo WHERE id = ?", new String[]{userIdStr});

        if(userIdStr.equals("admin") && userPasswordStr.equals("123")){
            //만약 관리자가 로그인을 했다면
            UserInfo.setUserId("admin");
            finish();
        }else if(cursor != null) {
            cursor.moveToNext();
            is_Id_Exists = cursor.getInt(0);
            if (is_Id_Exists == 0) {
                Log.i("Login.java - ", "아이디가 없음");
                Toast.makeText(this, "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Log.i("Login.java - ", "아이디가 있음");
                cursor = database.rawQuery("SELECT * FROM UserInfo WHERE id = ?", new String[]{userIdStr});
                if (cursor != null) {
                    cursor.moveToNext();
                    password_db = cursor.getString(1);
                    Log.i("Login - 비밀번호 : ",password_db);
                    UserInfo.setUserId(cursor.getString(0));
                    UserInfo.setUserPassword(cursor.getString(1));
                    UserInfo.setUserName(cursor.getString(2));
                    UserInfo.setUserEmail(cursor.getString(3));
                    UserInfo.setUserAddress(cursor.getString(4));
                    UserInfo.setPoint(cursor.getInt(5));
                    UserInfo.setUserPhone(cursor.getString(6));

                    Log.i("DB속의 비밀번호", password_db);
                }

                if (password_db.equals(userPasswordStr)) {//DB속의 비밀번호가 UI에서 입력된 비밀번호와 일치한다면
                    UserInfo.isLogin = true;
                    Log.i("로그인 성공여부", "로그인에 성공했다");
                    finish();
                } else {//일치하지 않는다면
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void insertUser(View view){
        startActivity(new Intent(Login.this, UserInsert.class));
    }
    public void searchIdPassword(View view){
        Intent intent = new Intent(getApplicationContext(), SearchIdPassword.class);
        startActivity(intent);
    }

    }
