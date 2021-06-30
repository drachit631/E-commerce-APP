package com.internship.app;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText email_edit,password_edit;
    TextInputLayout email_text,password_text;
    MaterialButton login_button;
    TextView ca_textview;
    public static SharedPreferences sharedPreferences;
    String email, password;


    public static final Pattern passwordPattern = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,15}");

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       sharedPreferences=getSharedPreferences(ConstantUrl.PREF, Context.MODE_PRIVATE);
        email= sharedPreferences.getString(ConstantUrl.EMAIL, null);
        password=sharedPreferences.getString(ConstantUrl.PASSWORD,null);

        email_edit=findViewById(R.id.email_edit_text);
        password_edit=findViewById(R.id.password_edit_text);
        login_button=findViewById(R.id.login_button);
        ca_textview=findViewById(R.id.ca_textview);
        email_text=findViewById(R.id.email_text_input);
        password_text=findViewById(R.id.password_text_input);

        email_text.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!Patterns.EMAIL_ADDRESS.matcher(email_text.getEditText().getText().toString()).matches()) {
                    email_text.setError("Invalid Email Address");
                }else{
                    email_text.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        password_text.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!passwordPattern.matcher(password_text.getEditText().getText().toString().trim()).matches()) {
                    password_text.setError("Invalid Password");
                }else{
                    password_text.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        login_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email_text.getEditText().getText().toString().trim().isEmpty() &&
                        password_text.getEditText().getText().toString().trim().isEmpty()){
                    email_text.setError("Field can't be empty");
                    password_text.setError("Field can't be empty");

                }else{
                        LoginProcess();
                }
            }
        });

        ca_textview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void LoginProcess() {
        ProgressDialog pd =new ProgressDialog(LoginActivity.this);
        pd.setMessage("Please Wait");
        pd.setCancelable(false);
        pd.show();

        Call<LoginResponseModel> call = ApiController.getInstance().getapi().login(
                email_text.getEditText().getText().toString(),
                password_text.getEditText().getText().toString()
        );

        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call , Response<LoginResponseModel> response) {
                pd.dismiss();
                LoginResponseModel logindata=response.body();
                if (response.isSuccessful()) {
                    if (logindata.getStatus() == true) {
                        for (int i = 0; i < logindata.getResponse().toArray().length; i++) {
                            startActivity(new Intent(LoginActivity.this , MainActivity.class));
                            sharedPreferences.edit().putString(ConstantUrl.ID , String.valueOf(logindata.getResponse().get(i).getId())).commit();
                            sharedPreferences.edit().putString(ConstantUrl.NAME , logindata.getResponse().get(i).getName()).commit();
                            sharedPreferences.edit().putString(ConstantUrl.EMAIL , logindata.getResponse().get(i).getEmail()).commit();
                            sharedPreferences.edit().putString(ConstantUrl.CONTACT , logindata.getResponse().get(i).getContact()).commit();
                            sharedPreferences.edit().putString(ConstantUrl.PASSWORD , logindata.getResponse().get(i).getPassword()).commit();
                            sharedPreferences.edit().putString(ConstantUrl.GENDER , logindata.getResponse().get(i).getGender()).commit();
                            sharedPreferences.edit().putString(ConstantUrl.STATUS , logindata.getResponse().get(i).getStatus()).commit();
                            sharedPreferences.edit().putString(ConstantUrl.CREATED_DATE , logindata.getResponse().get(i).getCreated_date()).commit();

                            Toast.makeText(LoginActivity.this , logindata.getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this , logindata.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this , "Unexpected Error" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call , Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(email!=null && password!=null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finishAffinity();
        }
    }

    /*private class loginData extends AsyncTask<String, String ,String> {

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd =new ProgressDialog(LoginActivity.this);
            pd.setMessage("Please Wait..");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("email",email_text.getEditText().getText().toString());
            hashMap.put("password",password_text.getEditText().getText().toString());
            return new MakeServiceCall().MakeServiceCall(ConstantUrl.URL+"login.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object=new JSONObject(s);
                if (object.getBoolean("Status")==true){
                    Toast.makeText(LoginActivity.this , object.getString("Message") , Toast.LENGTH_SHORT).show();
                    JSONArray jsonArray=object.getJSONArray("Response");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject= jsonArray.getJSONObject(i);
                        sharedPreferences.edit().putString(ConstantUrl.ID,jsonObject.getString("id")).commit();
                        sharedPreferences.edit().putString(ConstantUrl.NAME,jsonObject.getString("name")).commit();
                        sharedPreferences.edit().putString(ConstantUrl.EMAIL,jsonObject.getString("email")).commit();
                        sharedPreferences.edit().putString(ConstantUrl.CONTACT,jsonObject.getString("contact")).commit();
                        sharedPreferences.edit().putString(ConstantUrl.PASSWORD,jsonObject.getString("password")).commit();
                        sharedPreferences.edit().putString(ConstantUrl.GENDER,jsonObject.getString("gender")).commit();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                }else {
                    Toast.makeText(LoginActivity.this , object.getString("Message") , Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/
}