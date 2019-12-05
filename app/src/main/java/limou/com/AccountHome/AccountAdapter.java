package limou.com.AccountHome;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import limou.com.secondcompetition.R;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountAdapterHolder> {

    private Context mContext;
    public List<Map<String, String>> listdata;
    public static List<String> car_id;
    public static List<String> car_plate;
    public static Map<String,String> map;
    public static onCheck onCheck;
    public static onClick onClick;
    private String TAG = "AccountActivity";

    public AccountAdapter(Context mContext, List<Map<String, String>> list,onClick onClick,onCheck onCheck) {
        this.mContext = mContext;
        this.listdata = list;
        this.onCheck = onCheck;
        this.onClick = onClick;
        car_id = new ArrayList<>();
        car_plate = new ArrayList<>();
    }

    @NonNull
    @Override
    public AccountAdapter.AccountAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountAdapterHolder(LayoutInflater.from(mContext).inflate(R.layout.account_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccountAdapterHolder holder, final int position) {
        map = listdata.get(position);
        Log.e(TAG, "list: " + map.toString());
        holder.item_carId.setText(map.get("item_carId"));
        holder.item_plate.setText(map.get("item_plate"));
        holder.item_carName.setText(map.get("item_carName"));
        holder.item_carMoney.setText(map.get("item_carMoney"));

        holder.item_carCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCheck.onCheck(buttonView,isChecked,position);
            }
        });
        holder.item_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClick(position,v);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    class AccountAdapterHolder extends RecyclerView.ViewHolder{
        private TextView item_carId,item_plate,item_carName,item_carMoney;
        private ImageView item_carIog;
        private CheckBox item_carCheckBox;
        private Button item_btn_submit;
        private LinearLayout lout;
        public AccountAdapterHolder(@NonNull View itemView) {
            super(itemView);
            item_carId = itemView.findViewById(R.id.item_carId); /*车辆ID*/
            item_carIog = itemView.findViewById(R.id.item_carIog); /*车主图片*/
            item_plate = itemView.findViewById(R.id.item_plate); /*车牌号*/
            item_carName = itemView.findViewById(R.id.item_carName); /*车主信息*/
            item_carMoney = itemView.findViewById(R.id.item_carMoney); /*车主余额*/
            item_carCheckBox = itemView.findViewById(R.id.item_carCheckBox); /*充值多选*/
            item_btn_submit = itemView.findViewById(R.id.item_btn_submit); /*提交*/
            lout = itemView.findViewById(R.id.lout); /*提交*/
        }
    }

    public interface onClick{
        void onClick(int position,View v);
    }
    public interface onCheck{
        void onCheck(CompoundButton buttonView,boolean isChecked,int position);
    }
}
