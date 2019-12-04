package limou.com.AccountHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import limou.com.MyDecoration.MyDecoration;
import limou.com.ToolsHome.SecondTitleTools;
import limou.com.secondcompetition.R;

public class AccountActivity extends AppCompatActivity {
    private static String[] car_id = {"1", "2", "3", "4"};
    private static String[] car_plate = {"辽A10001", "渝A10002", "川A10003", "古A10004"};
    private static String[] car_name = {"张三", "李四", "高亮", "三国"};
    private static Integer[] item_carIog = {R.drawable.car_1, R.drawable.car_2, R.drawable.car_3, R.drawable.car_4};
    private static Handler handler = new Handler();
    private AccountAdapter adapter;
    private RecyclerView account_recycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        setRecycler();
    }

    private void setRecycler() {
        account_recycle = findViewById(R.id.account_recycler);
        account_recycle.setLayoutManager(new GridLayoutManager(this,1));
        account_recycle.addItemDecoration(new MyDecoration());
        adapter = new AccountAdapter(this);
        account_recycle.setAdapter(adapter);
    }

    private void InitView() {
        setContentView(R.layout.activity_account);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
                Toast.makeText(AccountActivity.this, "2", Toast.LENGTH_SHORT).show();
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
