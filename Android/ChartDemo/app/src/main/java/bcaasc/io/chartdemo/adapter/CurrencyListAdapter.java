package bcaasc.io.chartdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import bcaasc.io.chartdemo.R;
import bcaasc.io.chartdemo.bean.FilterCurrencyListBean;
import bcaasc.io.chartdemo.listener.ItemSelectorListener;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/7
 * <p>
 * 幣種列表適配器
 */
public class CurrencyListAdapter extends RecyclerView.Adapter<CurrencyListAdapter.ViewHolder> {


    private Context context;
    private List<FilterCurrencyListBean> filterCurrencyListBeans;
    private ItemSelectorListener itemSelectorListener;

    public CurrencyListAdapter(Context context, List<FilterCurrencyListBean> filterCurrencyListBeans) {
        super();
        this.context = context;
        this.filterCurrencyListBeans = filterCurrencyListBeans;
    }

    public void setItemSelectorListener(ItemSelectorListener itemSelectorListener) {
        this.itemSelectorListener = itemSelectorListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_currency_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        FilterCurrencyListBean filterCurrencyListBean = filterCurrencyListBeans.get(position);
        if (filterCurrencyListBean != null) {
            holder.tvCurrencyName.setText("CoinName:"+filterCurrencyListBean.getCurrencyName());
            holder.tvCurrencyValue.setText("價值："+String.valueOf(filterCurrencyListBean.getCurrencyValue()));
            holder.tvCurrencyMarketValue.setText("市值："+String.valueOf(filterCurrencyListBean.getCurrencyMarketTotalValue()));
        }
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemSelectorListener != null) {
                    itemSelectorListener.onItemSelectorListener(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterCurrencyListBeans == null ? 0 : filterCurrencyListBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCurrencyName;
        LinearLayout llItem;
        TextView tvCurrencyValue;
        TextView tvCurrencyMarketValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCurrencyName = itemView.findViewById(R.id.tv_currency_name);
            llItem = itemView.findViewById(R.id.ll_item);
            tvCurrencyValue = itemView.findViewById(R.id.tv_currency_value);
            tvCurrencyMarketValue = itemView.findViewById(R.id.tv_currency_market_value);
        }
    }
}
