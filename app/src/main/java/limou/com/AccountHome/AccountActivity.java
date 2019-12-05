package limou.com.AccountHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import limou.com.ETCHome.ETCActivity;
import limou.com.NetworkHome.OKHttpJson;
import limou.com.NetworkHome.OkHttpData;
import limou.com.ToolsHome.MyDialog;
import limou.com.ToolsHome.SecondTitleTools;
import limou.com.secondcompetition.R;

public class AccountActivity extends AppCompatActivity {
    private static String getUrl = "http://192.168.3.5:8088/transportservice/action/GetCarAccountBalance.do";
    private static String PutUrl = "http://192.168.3.5:8088/transportservice/action/SetCarAccountRecharge.do";
    private static String[] car_id = {"1", "2", "3", "4"};
    private static String[] car_plate = {"辽A10001", "渝A10002", "川A10003", "古A10004"};
    private static String[] car_name = {"张三", "李四", "高亮", "三国"};
    private static Integer[] item_carIog = {R.drawable.car_1, R.drawable.car_2, R.drawable.car_3, R.drawable.car_4};
    private static Handler handler = new Handler();
    private AccountAdapter adapter;
    private RecyclerView account_recycle;

    private List<Map<String, String>> listdata;
    private Map<String, String> map, map_2;//map各项数据，map_2Json请求数据
    private MyDialog myDialog;
    private Button btn_save, btn_cancel;
    private String TAG = "AccountActivity";
    private EditText ed_money;
    private List<String> list_car_id;
    private int position_n1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        InitData();
        setRecycler();
    }

    /*private void InitData() {
        listdata = new ArrayList<>();
        for (int i = 0; i < car_id.length; i++) {
            map = new HashMap<>();
            map.put("item_carId", car_id[i]);
            map.put("item_carIog", String.valueOf(item_carIog[i]));
            map.put("item_plate", car_plate[i]);
            map.put("item_carName", car_name[i]);
            listdata.add(map);
            Log.d(TAG, "listdata 数据：" + listdata.get(i));
        }
    }*/

    private void InitData() {
        listdata = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < car_id.length; i++) {
                    map_2 = new HashMap<>();
                    map_2.put("CarId", car_id[i]);
                    map_2.put("UserName", "user1");
                    JSONObject json = new JSONObject(map_2);
                    OkHttpData.sendConnect(getUrl, json.toString());
                    try {
                        Log.e(TAG, "JSon数据" + OkHttpData.JsonObjectRead().toString());
                        map = new HashMap<>();
                        map.put("item_carId", car_id[i]);
                        map.put("item_carIog", String.valueOf(item_carIog[i]));
                        map.put("item_plate", car_plate[i]);
                        map.put("item_carName", car_name[i]);
                        map.put("item_carMoney", OkHttpData.JsonObjectRead().getString("Balance"));
                        listdata.add(map);
                        Log.d(TAG, "listdata 数据" + listdata.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private void setRecycler() {

        adapter = new AccountAdapter(this, listdata, new AccountAdapter.onClick() {
            @Override
            public void onClick(final int position, View v) {
                OnClickMoney(position);
            }
        }, new AccountAdapter.onCheck() {
            @Override
            public void onCheck(CompoundButton buttonView, boolean isChecked, int position) {
                if (isChecked){

                }else {

                }
            }
        });
        account_recycle.setAdapter(adapter);
    }

    private void InitView() {
        setContentView(R.layout.activity_account);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        myDialog = new MyDialog(this, 0.8, 1.0, R.layout.accountmanager_dialog);
        myDialog.setCancelable(false);//用户不可点击外围取消掉弹窗

        SecondTitleTools.setTitle("账户管理");
        SecondTitleTools.MenuCreate();
        SecondTitleTools.btn_Record.setVisibility(View.VISIBLE);
        SecondTitleTools.btn_inMoney.setVisibility(View.VISIBLE);
        SecondTitleTools.btn_Record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //充值记录
            }
        });
        SecondTitleTools.btn_inMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //充值按钮
                list_car_id.add(car_id[position_n1]);
                OnClickMoney(position_n1);
            }
        });

        account_recycle = findViewById(R.id.account_recycler);
        account_recycle.setLayoutManager(new GridLayoutManager(this, 1));
        account_recycle.addItemDecoration(new MyDecoration());

        list_car_id = new ArrayList<>();
    }


    private class MyDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = 1;
        }
    }

    private void OnClickMoney(final int position) {
        Log.d(TAG, "OnClickMoney: " + position);
        myDialog.Do(new MyDialog.DoSomeThing() {
            @Override
            public void Do(Dialog v) {
                btn_save = myDialog.findViewById(R.id.btn_save);
                btn_cancel = myDialog.findViewById(R.id.btn_cancel);
                ed_money = myDialog.findViewById(R.id.e_money);
                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ed_money.getText().toString().equals("")) {
                            Toast.makeText(AccountActivity.this, "充值金额不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            int money = Integer.parseInt(ed_money.getText().toString());
                            updateMoney(position, money);
                            Log.e(TAG, "用户输入的是" + money);
                            ed_money.setText("");//用户再次点击时初始化内容
                            myDialog.dismiss();
                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ed_money.setText("");//用户再次点击时初始化内容
                        myDialog.dismiss();
                    }
                });
            }
        }).show();
    }

    private void updateMoney(final int position_n1, final int money) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <list_car_id.size() ; i++) {
                map_2 = new HashMap<>();
                map_2.put("CarId", car_id[position_n1]);
                map_2.put("Money", String.valueOf(money));
                map_2.put("UserName", "user1");
                JSONObject json = new JSONObject(map_2);
                OkHttpData.sendConnect(PutUrl, json.toString());
                }
                try {
                    if (OkHttpData.JsonObjectRead().getString("ERRMSG").equals("失败")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AccountActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                InitData();
                                setRecycler();
                                /*adapter.notifyDataSetChanged();*/
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
