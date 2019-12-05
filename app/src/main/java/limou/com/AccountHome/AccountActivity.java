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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import limou.com.ToolsHome.MyDialog;
import limou.com.ToolsHome.SecondTitleTools;
import limou.com.secondcompetition.R;

public class AccountActivity extends AppCompatActivity{
    private static String[] car_id = {"1", "2", "3", "4"};
    private static String[] car_plate = {"辽A10001", "渝A10002", "川A10003", "古A10004"};
    private static String[] car_name = {"张三", "李四", "高亮", "三国"};
    private static Integer[] item_carIog = {R.drawable.car_1, R.drawable.car_2, R.drawable.car_3, R.drawable.car_4};
    private static Handler handler = new Handler();
    private AccountAdapter adapter;
    private RecyclerView account_recycle;

    private List<Map<String, String>> listdata;
    private Map<String,String> map;
    private MyDialog myDialog;
    private Button btn_save,btn_cancel;
    private String TAG = "AccountActivity";
    private EditText ed_money;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        InitData();
        setRecycler();
    }

    private void InitData() {
        listdata = new ArrayList<>();
        for (int i =0;i<car_id.length;i++){
            map = new HashMap<>();
            map.put("item_carId", car_id[i]);
            map.put("item_carIog", String.valueOf(item_carIog[i]));
            map.put("item_plate", car_plate[i]);
            map.put("item_carName", car_name[i]);
            listdata.add(map);
        }
    }

    private void setRecycler() {
        account_recycle = findViewById(R.id.account_recycler);
        account_recycle.setLayoutManager(new GridLayoutManager(this,1));
        account_recycle.addItemDecoration(new MyDecoration());
        adapter = new AccountAdapter(this, listdata, new AccountAdapter.onClick() {
            @Override
            public void onClick(final int position) {
                Toast.makeText(AccountActivity.this, "这是" + position, Toast.LENGTH_SHORT).show();
                myDialog.Do(new MyDialog.DoSomeThing() {
                    @Override
                    public void Do(Dialog v) {
                        btn_save = myDialog.findViewById(R.id.btn_save);
                        btn_cancel = myDialog.findViewById(R.id.btn_cancel);
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialog.dismiss();
                            }
                        });
                        //充值按钮
                        btn_save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int position_i = position;
                                ed_money = myDialog.findViewById(R.id.e_money);
                                int money = Integer.parseInt(ed_money.getText().toString());
                                Log.e(TAG, "用户输入的是" + money +"\n车号是："+ position_i);
                                myDialog.dismiss();
                            }
                        });
                    }
                }).show();
            }
        }, new AccountAdapter.onCheck() {
            @Override
            public void onCheck(CompoundButton buttonView, boolean isChecked, int position) {

            }
        });
        account_recycle.setAdapter(adapter);
    }

    private void InitView() {
        setContentView(R.layout.activity_account);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        myDialog = new MyDialog(this,0.8,1.0,R.layout.accountmanager_dialog);


        SecondTitleTools.setTitle("账户管理");
        SecondTitleTools.MenuCreate();
        SecondTitleTools.btn_Record.setVisibility(View.VISIBLE);
        SecondTitleTools.btn_inMoney.setVisibility(View.VISIBLE);
        SecondTitleTools.btn_Record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AccountActivity.this, "1", Toast.LENGTH_SHORT).show();
            }
        });
        SecondTitleTools.btn_inMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.Do(new MyDialog.DoSomeThing() {
                    @Override
                    public void Do(Dialog v) {
                        btn_save = myDialog.findViewById(R.id.btn_save);
                        btn_cancel = myDialog.findViewById(R.id.btn_cancel);
                        ed_money = myDialog.findViewById(R.id.e_money);
                        btn_save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int money = Integer.parseInt(ed_money.getText().toString());
                                Log.e(TAG, "用户输入的是" + money );
                                myDialog.dismiss();
                            }
                        });
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialog.dismiss();
                            }
                        });
                    }
                }).show();
            }
        });


    }


    private class MyDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = 1;
        }
    }
}
