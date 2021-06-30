package com.internship.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    TextInputLayout registerName,registerEmail,registerPhone,registerCreatepass,registerConfirmpass;
    MaterialButton registerButton;
    RadioGroup gender;
    String gen;
    ProgressDialog pd;
   /* Spinner citySpinner,stateSpinner;
    ArrayList<String>cityList;
    ArrayList<String> stateList;
    ArrayAdapter<String>stateAdapter;
    ArrayAdapter cityAdapter;*/

    ImageView iv;
    public static final Pattern namePattern = Pattern.compile("^[a-zA-Z\\s]*$");
    public static final Pattern passwordPattern = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,15}");
            /*"^(?=.*[0-9])"+
            "(?=.*[a-z])"+             // at least 1 small alphbabet
            "(?=.*[A-Z])"+            // at least 1 capital alphbabet
            "(?=.*[!@#$%*+_])" +     // at least 1 special character
            "(?=\\S+$)" +           // no white spaces
            ".{4,}" +              // at least 4 characters
            "$");*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        registerName=findViewById(R.id.registerName);
        registerEmail=findViewById(R.id.registerEmailAddress);
        registerPhone=findViewById(R.id.registerPhonenumer);
        registerCreatepass=findViewById(R.id.registerPassword);
        registerConfirmpass=findViewById(R.id.registerConfirmPassword);
        registerButton=findViewById(R.id.registerButton);
        gender=findViewById(R.id.radio_grp);
       /* stateSpinner=findViewById(R.id.stateSpinner);
        citySpinner=findViewById(R.id.citySpinner);*/

        iv=findViewById(R.id.backButton);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        /*stateList=new ArrayList<>();
        stateList.add("Gujarat");
        stateList.add("Maharastra");
        stateList.add("Rajasthan");

        stateAdapter= new ArrayAdapter(SignUpActivity.this, android.R.layout.simple_list_item_1,stateList);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        stateSpinner.setAdapter(stateAdapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String state = stateList.get(position);
                Toast.makeText(SignUpActivity.this, state, Toast.LENGTH_SHORT).show();
                cityData(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        registerName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!namePattern.matcher(registerName.getEditText().getText().toString()).matches()){
                    registerName.setError("Invalid Name Format");
                }else{
                    registerName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        registerEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!Patterns.EMAIL_ADDRESS.matcher(registerEmail.getEditText().getText().toString()).matches()){
                    registerEmail.setError("Invalid Email");
                }else{
                    registerEmail.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        registerPhone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!Patterns.PHONE.matcher(registerPhone.getEditText().getText().toString()).matches()){
                    registerPhone.setError("Invalid Phone Format");
                }else{
                    registerPhone.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        registerCreatepass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!passwordPattern.matcher(registerCreatepass.getEditText().getText().toString().trim()).matches()){
                    registerCreatepass.setError("Invalid Password Format");
                }else{
                    registerCreatepass.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

       registerConfirmpass.getEditText().addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (!registerConfirmpass.getEditText().getText().toString().equals(registerCreatepass.getEditText().getText().toString())){
                   registerConfirmpass.setError("Password Couldn't match");
               }else{
                   registerConfirmpass.setError(null);
               }
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.male){
                    gen="male";
                }
                if(checkedId==R.id.female){
                    gen="female";
                }
                //int id = gender.getCheckedRadioButtonId();
                //RadioButton radioButton=findViewById(id);
                Toast.makeText(SignUpActivity.this, gen, Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(registerName.getEditText().getText().toString().isEmpty() ||
                        registerEmail.getEditText().getText().toString().isEmpty() ||
                        registerPhone.getEditText().getText().toString().isEmpty() ||
                        registerCreatepass.getEditText().getText().toString().isEmpty() ||
                        registerConfirmpass.getEditText().getText().toString().isEmpty() ||
                        gen == null) {
                    registerEmail.setError("Field can't be empty");
                    registerPhone.setError("Field can't be empty");
                    registerCreatepass.setError("Field can't be empty");
                    registerConfirmpass.setError("Field can't be empty");
                    registerName.setError("Field can't be empty");
                }else{
                    registerEmail.setError(null);
                    registerPhone.setError(null);
                    registerCreatepass.setError(null);
                    registerConfirmpass.setError(null);
                    registerName.setError(null);

                    SignUpProcess();
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

    private void SignUpProcess() {
        ProgressDialog pd =new ProgressDialog(SignUpActivity.this);
        pd.setMessage("Please Wait..");
        pd.setCancelable(false);
        pd.show();

        Call<SignUpResponseModel> call = ApiController.getInstance().getapi().signUp(
                registerName.getEditText().getText().toString(),
                registerEmail.getEditText().getText().toString(),
                registerPhone.getEditText().getText().toString(),
                registerCreatepass.getEditText().getText().toString(),
                gen
        );

        call.enqueue(new Callback<SignUpResponseModel>() {
            @Override
            public void onResponse(Call<SignUpResponseModel> call , Response<SignUpResponseModel> response) {
                pd.dismiss();
                SignUpResponseModel signUpData = response.body();
                if (response.isSuccessful()) {
                    if (signUpData.getStatus() == true) {
                        startActivity(new Intent(SignUpActivity.this , LoginActivity.class));
                        Toast.makeText(SignUpActivity.this , signUpData.getMessage() , Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SignUpActivity.this , signUpData.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignUpActivity.this , "Unexpected Error" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignUpResponseModel> call , Throwable t) {
                t.printStackTrace();
            }
        });
    }
   /* private class signUpData extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= new ProgressDialog(SignUpActivity.this);
            pd.setMessage("Please Wait..");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("name",registerName.getEditText().getText().toString());
            hashMap.put("email",registerEmail.getEditText().getText().toString());
            hashMap.put("contact",registerPhone.getEditText().getText().toString());
            hashMap.put("password",registerCreatepass.getEditText().getText().toString());
            hashMap.put("gender",gen);
            return new MakeServiceCall().MakeServiceCall(ConstantUrl.URL + "signUp.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object=new JSONObject(s);
                if(object.getBoolean("Status") == true) {
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    Toast.makeText(SignUpActivity.this , object.getString("Message") , Toast.LENGTH_LONG).show();
                } else if(object.getBoolean("Status")==false){
                    Toast.makeText(SignUpActivity.this , object.getString("Message") , Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/

   /* private void cityData(int position) {

        cityList=new ArrayList<>();

        if (position==0) {
            cityList.add("Ahmedabad");
            cityList.add("Gandhinagar");
            cityList.add("Surat");
            cityList.add("Rajkot");
            cityList.add("Bhavnagar");
        }

        if (position==1){
            cityList.add("Mumbai");
            cityList.add("Nagpur");
            cityList.add("Pune");
            cityList.add("Lonavala");
        }

        if (position==2){
            cityList.add("Kota");
            cityList.add("Jaipur");
            cityList.add("MT Abu");
            cityList.add("Dungarpur");
        }
        cityAdapter= new ArrayAdapter(SignUpActivity.this, android.R.layout.simple_list_item_1,cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        citySpinner.setAdapter(cityAdapter);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String city = stateList.get(position);
                Toast.makeText(SignUpActivity.this, city , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/

}