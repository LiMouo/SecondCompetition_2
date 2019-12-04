package limou.com.ETCHome;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import limou.com.NetworkHome.OKHttpJson;
import limou.com.SQLiteCatalog.SQLiteBillMaster;
import limou.com.ToolsHome.SecondTitleTools;
import limou.com.secondcompetition.R;
import okhttp3.MediaType;

public class ETCActivity extends AppCompatActivity {

    private SQLiteBillMaster billMaster;
    private SQLiteDatabase db;
    private Spinner spinner;
    private static final String TAG = "ETCActivity";
    private int count = 1;
    private TextView show_money;
    private Button btn_query;
    private EditText in_money;
    private Button btn_inmoney;
    private Handler handler = new Handler();
    private  String  money;
    private SimpleDateFormat formatter;
    private ETCGson etcGson;
    private ETCGson etcGson1 = new ETCGson();
    String regEx ="^([1-9][0-9]{0,1}|100)$";
    String regEx1 = "[1-9][0-9][0-9]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etc);
        InitView();
        setToolbar(); /*设置查询车辆*/
        queryMoney(); /*查询余额*/
        getDate(); /*获取时间*/
    }



    private void setToolbar() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, list);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                count = (position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        show_money = findViewById(R.id.show_money);
        in_money = findViewById(R.id.in_money);
        btn_query = findViewById(R.id.btn_query);
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryMoney();
            }
        });
        btn_inmoney = findViewById(R.id.btn_inmoney);
        btn_inmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (in_money.getText().toString().matches(regEx)) {
                    updateMoney();
                }else if (in_money.getText().toString().matches(regEx1)) {
                    Toast.makeText(ETCActivity.this, "充值金额不能大于 100 ，请重新输入", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ETCActivity.this, "充值金额不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateMoney() {
            final String url = "http://192.168.3.5:8088/transportservice/action/SetCarAccountRecharge.do";
            final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            final JSONObject json = new JSONObject();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        json.put("CarId", count);
                        json.put("Money", in_money.getText().toString());
                        json.put("UserName", "user1");
                        OKHttpJson.SendOKHttp(url, mediaType,json);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Gson gson = new Gson();
                                    etcGson = gson.fromJson(OKHttpJson.JsonObjectRead().toString(), ETCGson.class);
                                    if (etcGson.getERRMSG().equals("成功")) {
                                        Toast.makeText(ETCActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
                                        queryMoney();
                                        ContentValues values = new ContentValues();
                                        values.put("car_id", count);
                                        values.put("money", in_money.getText().toString());
                                        values.put("user", "user1");
                                        values.put("datetime", getDate());
                                        db.insert("bill", null, values);
                                        values.clear();
                                        db.close();
                                    } else {
                                        Toast.makeText(ETCActivity.this, "充值失败 请检查充值金额 或 网络", Toast.LENGTH_SHORT).show();
                                    }
                                    if (OKHttpJson.JsonObjectRead().get("status").toString().equals("500")) {
                                        Toast.makeText(ETCActivity.this, "请求失败" + OKHttpJson.JsonObjectRead().get("status").toString(), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    try {
                                        Toast.makeText(ETCActivity.this, "请求失败  " + OKHttpJson.JsonObjectRead().get("status").toString(), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(ETCActivity.this, "Error 充值失败 请检查车是否连接到服务器网络", Toast.LENGTH_SHORT).show();
                                    show_money.setText("");
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
    }

    private void queryMoney() {
        final String url = "http://192.168.3.5:8088/transportservice/action/GetCarAccountBalance.do";
        final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final JSONObject json = new JSONObject();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    json.put("CarId", String.valueOf(count));
                    json.put("UserName", "user1");
                    OKHttpJson.SendOKHttp(url,mediaType,json);
                    Gson gson = new Gson();
                    etcGson1 = gson.fromJson(OKHttpJson.JsonObjectRead().toString(), ETCGson.class);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                if (etcGson1.getERRMSG().equals("成功")){
                                    money = String.valueOf(etcGson1.getBalance());
                                    show_money.setText(money);
                                    Toast.makeText(ETCActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                                    in_money.setText("");
                                }else {
                                    Toast.makeText(ETCActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void InitView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SecondTitleTools.setTitle("我的账户");
        SecondTitleTools.MenuCreate();
        billMaster = new SQLiteBillMaster(this, "Bill.db");
        db = billMaster.getWritableDatabase();
    }
    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String str = format.format(date);
        return str;
    }
}
