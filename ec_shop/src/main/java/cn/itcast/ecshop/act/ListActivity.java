package cn.itcast.ecshop.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.itcast.ecshop.R;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String LIST_FUNCTIONS[] = {"订单", "购物车", "商品详情"};

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mListView = ((ListView) findViewById(R.id.listview));

        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, LIST_FUNCTIONS));

        mListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent();
        if (position == 0) {

           // intent.setClass(this, OrderActivity.class);

        } else if(position == 1) {

           // intent.setClass(this, ShopDemoActivity.class);
        } else if(position == 2) {


            intent.setClass(this, ProductDetailActivity.class);
        }

        startActivity(intent);
    }
}
