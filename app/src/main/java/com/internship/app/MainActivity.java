package com.internship.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    ProgressDialog  progressDialog;
    AlertDialog.Builder builder;
    public static MaterialSearchView searchView;
    TextView menu_name,menu_email;
    String email, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginActivity.sharedPreferences=getSharedPreferences(ConstantUrl.PREF, Context.MODE_PRIVATE);
        email= LoginActivity.sharedPreferences.getString(ConstantUrl.EMAIL,null);
        name=LoginActivity.sharedPreferences.getString(ConstantUrl.NAME,null);

        searchView=findViewById(R.id.search_view);

        toolbar = findViewById(R.id.home_toolbar);
        toolbar.setTitle("E-Commerce App");
        setSupportActionBar(toolbar);

        navigationView=findViewById(R.id.nav_view);

        View headerView=navigationView.getHeaderView(0);
        menu_name=headerView.findViewById(R.id.menu_name);
        menu_email=headerView.findViewById(R.id.menu_email);

        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout=findViewById(R.id.drawer_layout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(MainActivity.this, drawerLayout,toolbar,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
            toolbar.setTitle("Home Page");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.top_menu, menu);

        MenuItem item=menu.findItem(R.id.nav_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_cart:
                Toast.makeText(this , "Cart" , Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_profile :
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                finishAffinity();
                break;

            case R.id.nav_update_profile:
                startActivity(new Intent(MainActivity.this,UpdateActivity.class));
                finishAffinity();
                break;

            case R.id.nav_about:
                Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_chat :
                Toast.makeText(this, "Chat ", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_delete:
                if(new ConnectionDetector(MainActivity.this).isConnectingToInternet()){
                    deleteAccountProcess();
                }
                else{
                    new ConnectionDetector(MainActivity.this).connectiondetect();
                }
                break;

            case R.id.nav_logout:
                LoginActivity.sharedPreferences.edit().clear().commit();
               progressDialog=new ProgressDialog(this) ;
               progressDialog.setMessage("Please Wait");
               progressDialog.show();

               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       startActivity(new Intent(MainActivity.this, LoginActivity.class));
                       finishAffinity();
                   }
               }, 1000);
            break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void deleteAccountProcess() {
        ProgressDialog pd =new ProgressDialog(MainActivity.this);
        pd.setMessage("Please Wait..");
        pd.setCancelable(false);
        pd.show();

        Call<SignUpResponseModel> call = ApiController.getInstance().getapi().delete(
                LoginActivity.sharedPreferences.getString(ConstantUrl.ID,null)
        );
        call.enqueue(new Callback<SignUpResponseModel>() {
            @Override
            public void onResponse(Call<SignUpResponseModel> call , Response<SignUpResponseModel> response) {
                pd.dismiss();
                SignUpResponseModel deleteData=response.body();
                if (response.isSuccessful()) {
                    if (deleteData.getStatus() == true) {
                        LoginActivity.sharedPreferences.edit().clear().commit();
                        builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Internship App");
                        builder.setMessage(deleteData.getMessage());
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok" , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog , int which) {
                                startActivity(new Intent(MainActivity.this , LoginActivity.class));
                                finishAffinity();
                            }
                        });
                        builder.show();
                    } else {
                        builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("E-Commerce APP");
                        builder.setMessage(deleteData.getMessage());
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok" , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog , int which) {
                                startActivity(new Intent(MainActivity.this , LoginActivity.class));
                                finishAffinity();
                            }
                        });
                        builder.show();
                    }
                }else{
                    Toast.makeText(MainActivity.this , "Unexpected Error" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignUpResponseModel> call , Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()){
            searchView.closeSearch();
        } else if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setIcon(R.mipmap.ic_launcher_frost_foreground);
            builder.setTitle("E-Commerce APP");
            builder.setMessage("Are you sure want to exit?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(email!=null || name!=null){
            menu_name.setText(name);
            menu_email.setText(email);
        }else{
            menu_name.setText("Failed to fetch data");
            menu_email.setText("Failed to fetch data");
        }
    }

    /*private class deleteAccount extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap=new HashMap();
            hashMap.put("id",LoginActivity.sharedPreferences.getString(ConstantUrl.ID,null));
            return new MakeServiceCall().MakeServiceCall(ConstantUrl.URL+"delete.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object=new JSONObject(s);
                if (object.getBoolean("Status")==true){
                    LoginActivity.sharedPreferences.edit().clear().commit();
                    builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Internship App");
                    builder.setMessage(object.getString("Message"));
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                            finishAffinity();
                        }
                    });
                    builder.show();
                }else{
                    builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("E-Commerce APP");
                    builder.setMessage(object.getString("Message"));
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                            finishAffinity();
                        }
                    });
                    builder.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }*/
}