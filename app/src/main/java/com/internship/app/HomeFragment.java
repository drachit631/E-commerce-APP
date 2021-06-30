package com.internship.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    View view;
    ImageSlider imageSlider;
    RecyclerView recyclerView_category;
    CategoryAdapter categoryAdapter;
    Bundle bundle;
    TextView homeShop;
    ProgressDialog pd;

    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home,container,false);
        homeShop=view.findViewById(R.id.home_shop);
        imageSlider=view.findViewById(R.id.image_slider);
        recyclerView_category=view.findViewById(R.id.categoryRecyclerView);

           /* MainActivity.searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                     if (newText.trim().equalsIgnoreCase("")) {
                        categoryAdapter.filter("");
                    } else {
                        categoryAdapter.filter(newText);
                    }
                    return false;
                }
            });*/

        ArrayList<SlideModel> images=new ArrayList<>();
        images.add(new SlideModel(R.drawable.deals,ScaleTypes.FIT));
        images.add(new SlideModel(R.drawable.deals1,ScaleTypes.FIT));
        images.add(new SlideModel(R.drawable.deals2,ScaleTypes.FIT));
        images.add(new SlideModel(R.drawable.deals3,ScaleTypes.FIT));

        imageSlider.setImageList(images);
        RecyclerViewGrid();
        return view;
    }

    private void RecyclerViewGrid() {
        recyclerView_category.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView_category.setItemAnimator(new DefaultItemAnimator());
        recyclerView_category.setNestedScrollingEnabled(false);

       CategoryProcess();
        // new fetching_data().execute();
    }

    private void CategoryProcess() {
        pd=new ProgressDialog(getActivity());
        pd.setMessage("Please Wait");
        pd.setCancelable(false);
        pd.show();
        Call<List<CategoriesResponseModel>> call = ApiController.getInstance().getapi().getCategories();

        call.enqueue(new Callback<List<CategoriesResponseModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesResponseModel>> call , Response<List<CategoriesResponseModel>> response) {
                pd.dismiss();
                List<CategoriesResponseModel> data=response.body();
                CategoryAdapter adapter=new CategoryAdapter(data);
                recyclerView_category.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<CategoriesResponseModel>> call , Throwable t) {
                Toast.makeText(getActivity(),t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

  /* private class fetching_data extends AsyncTask<String, String, String> {

        ProgressDialog pd;
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pd= new ProgressDialog(getActivity());
           pd.setMessage("Please Wait..");
           pd.setCancelable(false);
           pd.show();
       }

       @Override
        protected String doInBackground(String... strings) {
            return new MakeServiceCall().MakeServiceCall(ConstantUrl.URL+"categories.php", MakeServiceCall.POST,new HashMap<>());
        }

        @Override
        protected void onPostExecute(String s) {
           super.onPostExecute(s);
           pd.dismiss();
            try {
                JSONObject object=new JSONObject(s);
                if (object.getBoolean("Status")==true){
                    JSONArray jsonArray=object.getJSONArray("Response");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject= jsonArray.getJSONObject(i);
                            categoryList list = new categoryList();
                            list.setCategoryName(jsonObject.getString("cat_name"));
                            list.setCategoryImage(jsonObject.getString("cat_images"));
                            arraycategorylist.add(list);
                    }
                    categoryAdapter = new CategoryAdapter(getContext(), arraycategorylist);
                    recyclerView_category.setAdapter(categoryAdapter);
                }else {
                    Toast.makeText(getContext() , object.getString("Message") , Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/


    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.categoryHolder>{

        List<CategoriesResponseModel> data;
        public CategoryAdapter(List<CategoriesResponseModel> data) {
            this.data = data;
        }

        /*public CategoryAdapter(Context context , ArrayList<categoryList> arraylist) {
            this.context = context;
            this.arrayCategoryList= arraylist;
            searchList =new ArrayList<>();
            searchList.addAll(arraylist);
        }*/

        @NonNull
        @Override
        public CategoryAdapter.categoryHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_category , parent , false);
            return new categoryHolder(view);
        }

           /* public void filter(String s) {
                s = s.toLowerCase(Locale.getDefault());
                data.clear();
                if (s.length()==0){
                    data.addAll(searchList);
                }else {
                    for (CategoryModel cat : searchList) {
                        if (cat.getCategoryName().toLowerCase(Locale.getDefault()).contains(s)) {
                            data.add(cat);
                        }
                    }
                }
            notifyDataSetChanged();
        }*/

        public class categoryHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;
            public categoryHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.category_iv);
                textView = itemView.findViewById(R.id.category_tv);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull  HomeFragment.CategoryAdapter.categoryHolder holder, int position) {
            Picasso.get().load(data.get(position).getCat_images()).placeholder(R.drawable.ic_shopping).into(holder.imageView);
            holder.textView.setText(data.get(position).getCat_name());

          holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bundle= new Bundle();
                    bundle.putInt("id", position);
                    bundle.putString("categoryName", holder.textView.getText().toString());
                    Intent intent=new Intent(getActivity(),SubCategoryActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
          });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
