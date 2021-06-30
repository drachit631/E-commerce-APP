package com.internship.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    SubCategoryAdapter subCategoryAdapter;
    Bundle bundle;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        bundle=getIntent().getExtras();
        id=bundle.getInt("id");
        id=id+1;


        recyclerView=findViewById(R.id.subCategoryRecyclerView);
        toolbar=findViewById(R.id.subCategory_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Categories for '"+ bundle.getString("categoryName")+"'");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubCategoryActivity.this, MainActivity.class));
                finish();
            }
        });
        RecyclerViewGrid();
    }

    private void RecyclerViewGrid() {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        //new fetching_data().execute();
        SubCategoryProcess();
    }

    private void SubCategoryProcess() {
        ProgressDialog pd=new ProgressDialog(SubCategoryActivity.this);
        pd.setMessage("Please Wait");
        pd.setCancelable(false);
        pd.show();

        Call<List<CategoriesResponseModel>> call = ApiController.getInstance().getapi().getSubCategories(id);

        call.enqueue(new Callback<List<CategoriesResponseModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesResponseModel>> call , Response<List<CategoriesResponseModel>> response) {
                List<CategoriesResponseModel> data = response.body();
                subCategoryAdapter=new SubCategoryAdapter(data);
                recyclerView.setAdapter(subCategoryAdapter);
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<List<CategoriesResponseModel>> call , Throwable t) {
                t.printStackTrace();
                //Toast.makeText(SubCategoryActivity.this , t.toString() , Toast.LENGTH_LONG).show();
            }
        });
    }

    /*private class fetching_data extends AsyncTask<String, String, String> {

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd =new ProgressDialog(SubCategoryActivity.this);
            pd.setMessage("Please Wait..");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("id", String.valueOf(id));
            return new MakeServiceCall().MakeServiceCall(ConstantUrl.URL + "sub_categories.php", MakeServiceCall.POST,hashMap);
            }

        @Override
        protected void onPostExecute(String s) {
            pd.dismiss();
            super.onPostExecute(s);
            try {
                JSONObject object=new JSONObject(s);
                if (object.getBoolean("Status")==true){
                    JSONArray jsonArray=object.getJSONArray("Response");
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        categoryList list = new categoryList();
                        list.setSubCategoryName(jsonObject.getString("sub_cat_name"));
                        list.setSubCategoryImage(jsonObject.getString("sub_cat_images"));
                        subCategoryLists.add(list);
                    }
                    subCategoryAdapter= new SubCategoryAdapter(SubCategoryActivity.this,subCategoryLists);
                    recyclerView.setAdapter(subCategoryAdapter);
                }else if (object.getBoolean("Status")== false){
                    Toast.makeText(SubCategoryActivity.this , object.getString("Message") , Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/

    private class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.subCategoryHolder> {

        List<CategoriesResponseModel> data;
        public SubCategoryAdapter(List<CategoriesResponseModel> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public SubCategoryAdapter.subCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_sub_category , parent , false);
            return new subCategoryHolder(view);
        }

        private class subCategoryHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;
            public subCategoryHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.subCategory_iv);
                textView = itemView.findViewById(R.id.subCategory_tv);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull SubCategoryAdapter.subCategoryHolder holder , int position) {
            Picasso.get().load(data.get(position).getSub_cat_images()).placeholder(R.drawable.ic_shopping).into(holder.imageView);
            holder.textView.setText(data.get(position).getSub_cat_name());
        }

        @Override
        public int getItemCount() { return data.size(); }
    }
}