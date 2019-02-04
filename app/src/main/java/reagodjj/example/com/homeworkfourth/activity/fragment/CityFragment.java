package reagodjj.example.com.homeworkfourth.activity.fragment;

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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import reagodjj.example.com.homeworkfourth.R;

public class CityFragment extends Fragment {
    public static final String CITY = "city";
    private GridView gvCity;
    private ListView lvCity;
    private List<Map<String, String>> cityList;
    private String[] cityNameList = {"A", "安庆", "安宁", "鞍山", "安阳", "B", "白山", "白银", "保定", "宝鸡", "C"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initGridView();
        initListView();
    }

    private void initGridView() {
        gvCity = Objects.requireNonNull(getView()).findViewById(R.id.gv_city);

        String[] buttonList = getResources().getStringArray(R.array.grid_city);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                R.layout.grid_button_item, buttonList);

        gvCity.setAdapter(arrayAdapter);
        gvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Button, ImageButton and CheckBox have there focus. We should set focusable false
                Button btCity = (Button) gvCity.getChildAt(position);
                Log.i("RealgodJJ", btCity.getText().toString());
                //TODO:两个fragment传输数据
            }
        });
    }

    private void initListView() {
        lvCity = getView().findViewById(R.id.lv_city);
        cityList = new ArrayList<>();
        for (String cityName : cityNameList) {
            Map<String, String> cityMap = new HashMap<>();
            cityMap.put("city", cityName);
            cityList.add(cityMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), cityList,
                R.layout.city_list_item, new String[]{"city"}, new int[]{R.id.tv_city});
        lvCity.setAdapter(simpleAdapter);
    }
}
