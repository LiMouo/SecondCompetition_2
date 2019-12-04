package limou.com.AccountHome;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import limou.com.secondcompetition.R;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountAdapterHolder> {

    private Context mContext;

    public AccountAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AccountAdapter.AccountAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountAdapterHolder(LayoutInflater.from(mContext).inflate(R.layout.account_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccountAdapterHolder holder, int position) {
    }


    @Override
    public int getItemCount() {
        return 0;
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
}
