package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sencosignapp.R;

import java.util.ArrayList;

import model.CustomerDetailModel;

public class CustomerDetailAdapter extends RecyclerView.Adapter<CustomerDetailAdapter.CustomerDetailViewHolder> {

    private ArrayList<CustomerDetailModel> dataList;
    private ItemClickListener mClickListener;

    public CustomerDetailAdapter(ArrayList<CustomerDetailModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public CustomerDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.home_itemview, parent, false);
        return new CustomerDetailViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CustomerDetailViewHolder holder, int position) {
        holder.txtInvoiveNo.setText(dataList.get(position).getInvoiceNum());
        holder.txtCustomerName.setText(dataList.get(position).getCustomerName());
        holder.custAcct.setText(dataList.get(position).getCustomerAcct());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class CustomerDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtInvoiveNo, txtCustomerName,custAcct;
        CardView cardView;
        CustomerDetailViewHolder(View itemView) {
            super(itemView);
            txtInvoiveNo = (TextView) itemView.findViewById(R.id.invoiceValue);
            txtCustomerName = (TextView) itemView.findViewById(R.id.customerName);
            custAcct = (TextView)itemView.findViewById(R.id.custAcct);
            cardView = (CardView) itemView.findViewById(R.id.cv1);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null){
                mClickListener.onItemClick(v, getAdapterPosition());
            }


        }
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
