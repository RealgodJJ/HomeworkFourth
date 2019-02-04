package reagodjj.example.com.homeworkfourth.activity.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import reagodjj.example.com.homeworkfourth.R;
import reagodjj.example.com.homeworkfourth.activity.MainActivity;
import reagodjj.example.com.homeworkfourth.adapter.ListViewAdapter;
import reagodjj.example.com.homeworkfourth.entity.FoodInfo;
import reagodjj.example.com.homeworkfourth.entity.FoodResult;

public class ShoppingFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ImageView ivBack;
    private GridView gvStore;
    private ListView lvFoodItem;
    private List<FoodInfo> foodInfoList = new ArrayList<>();
    private CityFragment cityFragment = new CityFragment();
    private ListViewAdapter listViewAdapter;
    private int cardList[] = {R.drawable.fly1, R.drawable.car, R.drawable.autombile1,
            R.drawable.cake, R.drawable.food, R.drawable.watch, R.drawable.cp, R.drawable.phone};
    private static final String URL_STRING = "http://www.imooc.com/api/shopping?type=11";
    private static final String STATUS = "status";
    private static final String MESSAGE = "msg";
    private static final String DATA = "data";
    private static final String ID = "id";
    private static final String IMAGE = "img";
    private static final String NAME = "name";
    private static final String COUNT = "count";
    private static final String PRICE = "price";
    private static final String DESCRIPTION = "description";
    private static final String _ACTION = "action";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ivBack = Objects.requireNonNull(getView()).findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                Objects.requireNonNull(getActivity()).finish();
            }
        });

        gvStore = getView().findViewById(R.id.gv_store);
        String titleList[] = getResources().getStringArray(R.array.store_order);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                R.layout.grid_store_order_item, titleList);
        gvStore.setAdapter(arrayAdapter);
        gvStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvStore = (TextView) gvStore.getChildAt(position);
                tvStore.setTextColor(getResources().getColor(R.color.orange, null));
                Log.i("RealgodJJ", tvStore.getText().toString());
                for (int i = 0; i < 3; i++) {
                    if (i != position) {
                        TextView tvOthers = (TextView) gvStore.getChildAt(i);
                        tvOthers.setTextColor(getResources().getColor(R.color.grey, null));
                    }
                }
            }
        });

        lvFoodItem = getView().findViewById(R.id.lv_food_item);
        lvFoodItem.setOnItemClickListener(this);
        new RequestFoodAsyncTask().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @SuppressLint("StaticFieldLeak")
    public class RequestFoodAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return request(URL_STRING);
        }

        private String request(String urlString) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(30 * 1000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                int requestCode = httpURLConnection.getResponseCode();

                if (requestCode == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();

                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    return stringBuilder.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            FoodResult foodResult = new FoodResult();

            try {
                JSONObject jsonObject = new JSONObject(result);
                int status = jsonObject.getInt(STATUS);
                String message = jsonObject.getString(MESSAGE);
                foodResult.setStatus(status);
                foodResult.setMessage(message);

                if (status == 1 && message.equals("成功")) {
                    JSONArray jsonArray = jsonObject.getJSONArray(DATA);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject tempJSONObject = (JSONObject) jsonArray.get(i);
                        int id = tempJSONObject.getInt(ID);
                        String name = tempJSONObject.getString(NAME);
                        String image = tempJSONObject.getString(IMAGE);
                        int count = tempJSONObject.getInt(COUNT);
                        String price = tempJSONObject.getString(PRICE);
                        String description = tempJSONObject.getString(DESCRIPTION);
                        String action = tempJSONObject.getString(_ACTION);
                        //为食物列表添加元素
                        FoodInfo foodInfo = new FoodInfo(id, name, image, count, price, description, action);
                        foodInfoList.add(foodInfo);
                    }

                    listViewAdapter = new ListViewAdapter(getActivity(), foodInfoList);
                    lvFoodItem.setAdapter(listViewAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            listViewAdapter.notifyDataSetChanged();
        }
    }
}
