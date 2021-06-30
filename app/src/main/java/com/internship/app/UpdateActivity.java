package com.internship.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity {

    TextInputLayout updateName,updatePhone,updateCreatepass,updateConfirmpass;
    MaterialButton updateButton;
    ProgressDialog pd;
    ImageView iv;
    SharedPreferences sharedPreferences;
    public static final Pattern namePattern = Pattern.compile("^[a-zA-Z\\s]*$");
    public static final Pattern passwordPattern = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,15}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        updateName=findViewById(R.id.updateName);
        updatePhone=findViewById(R.id.updatePhonenumer);
        updateCreatepass=findViewById(R.id.updatePassword);
        updateConfirmpass=findViewById(R.id.updateConfirmPassword);
        updateButton=findViewById(R.id.updateButton);
        sharedPreferences= getSharedPreferences(ConstantUrl.PREF, MODE_PRIVATE);
        getData();
        iv=findViewById(R.id.update_backButton);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateActivity.this, MainActivity.class));
                finish();
            }
        });

        updateName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!namePattern.matcher(updateName.getEditText().getText().toString()).matches()){
                    updateName.setError("Invalid Name Format");
                }else{
                    updateName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        updatePhone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!Patterns.PHONE.matcher(updatePhone.getEditText().getText().toString()).matches()){
                    updatePhone.setError("Invalid Phone Format");
                }else{
                    updatePhone.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        updateCreatepass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!passwordPattern.matcher(updateCreatepass.getEditText().getText().toString().trim()).matches()){
                    updateCreatepass.setError("Invalid Password Format");
                }else{
                    updateCreatepass.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        updateConfirmpass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!updateConfirmpass.getEditText().getText().toString().equals(updateCreatepass.getEditText().getText().toString())){
                    updateConfirmpass.setError("Password Couldn't match");
                }else{
                    updateConfirmpass.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(updateName.getEditText().getText().toString().isEmpty() ||
                        updatePhone.getEditText().getText().toString().isEmpty() ||
                        updateCreatepass.getEditText().getText().toString().isEmpty() ||
                        updateConfirmpass.getEditText().getText().toString().isEmpty()) {
                    updatePhone.setError("Field can't be empty");
                    updateCreatepass.setError("Field can't be empty");
                    updateConfirmpass.setError("Field can't be empty");
                    updateName.setError("Field can't be empty");
                }else{
                    updatePhone.setError(null);
                    updateCreatepass.setError(null);
                    updateConfirmpass.setError(null);
                    updateName.setError(null);

                    if (new ConnectionDetector(UpdateActivity.this).isConnectingToInternet()) {
                        UpdateProcess();
                    } else {
                        new ConnectionDetector(UpdateActivity.this).connectiondetect();
                    }
                    /*progressDialog=new ProgressDialog(SignUpActivity.this) ;
                    progressDialog.setMessage("Creating Account");
                    progressDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            finishAffinity();

                        }
                    }, 1000);*/
                }
            }
        });
    }

    private void UpdateProcess() {
        ProgressDialog pd =new ProgressDialog(UpdateActivity.this);
        pd.setMessage("Please Wait..");
        pd.setCancelable(false);
        pd.show();

        Call<SignUpResponseModel> call = ApiController.getInstance().getapi().update(
                LoginActivity.sharedPreferences.getString(ConstantUrl.ID,null),
                LoginActivity.sharedPreferences.getString(ConstantUrl.EMAIL,null),
                updateName.getEditText().getText().toString(),
                updatePhone.getEditText().getText().toString(),
                updateCreatepass.getEditText().getText().toString()
        );

        call.enqueue(new Callback<SignUpResponseModel>() {
            @Override
            public void onResponse(Call<SignUpResponseModel> call , Response<SignUpResponseModel> response) {
                pd.dismiss();
                SignUpResponseModel updateData=response.body();
                if (response.isSuccessful()){
                    if (updateData.getStatus() == true) {
                        LoginActivity.sharedPreferences.edit().putString(ConstantUrl.NAME , updateName.getEditText().getText().toString()).commit();
                        LoginActivity.sharedPreferences.edit().putString(ConstantUrl.CONTACT , updatePhone.getEditText().getText().toString()).commit();
                        LoginActivity.sharedPreferences.edit().putString(ConstantUrl.PASSWORD , updateCreatepass.getEditText().getText().toString()).commit();
                        Toast.makeText(UpdateActivity.this , updateData.getMessage() , Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(UpdateActivity.this , updateData.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(UpdateActivity.this , "Unexpected Error" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignUpResponseModel> call , Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UpdateActivity.this,MainActivity.class));
        finishAffinity();
        super.onBackPressed();
    }

    /*private class updateData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= new ProgressDialog(UpdateActivity.this);
            pd.setMessage("Please Wait..");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("email",LoginActivity.sharedPreferences.getString(ConstantUrl.EMAIL,null));
            hashMap.put("id",LoginActivity.sharedPreferences.getString(ConstantUrl.ID,null));
            hashMap.put("name",updateName.getEditText().getText().toString());
            hashMap.put("contact",updatePhone.getEditText().getText().toString());
            hashMap.put("password",updateCreatepass.getEditText().getText().toString());
            return new MakeServiceCall().MakeServiceCall(ConstantUrl.URL + "update.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object=new JSONObject(s);
                if(object.getBoolean("Status") == true) {
                    LoginActivity.sharedPreferences.edit().putString(ConstantUrl.NAME, updateName.getEditText().getText().toString()).commit();
                    LoginActivity.sharedPreferences.edit().putString(ConstantUrl.CONTACT, updatePhone.getEditText().getText().toString()).commit();
                    LoginActivity.sharedPreferences.edit().putString(ConstantUrl.PASSWORD, updateCreatepass.getEditText().getText().toString()).commit();
                    Toast.makeText(UpdateActivity.this , object.getString("Message") , Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(UpdateActivity.this , object.getString("Message") , Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/
    private void getData(){
        updateName.getEditText().setText(LoginActivity.sharedPreferences.getString(ConstantUrl.NAME, null));
        updatePhone.getEditText().setText(LoginActivity.sharedPreferences.getString(ConstantUrl.CONTACT, null));
        updateCreatepass.getEditText().setText(LoginActivity.sharedPreferences.getString(ConstantUrl.PASSWORD, null));
        updateConfirmpass.getEditText().setText(LoginActivity.sharedPreferences.getString(ConstantUrl.PASSWORD, null));
    }
}