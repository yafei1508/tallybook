package com.lyf.tallybook.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyf.tallybook.R;

import java.util.Date;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.reViewHolder> {
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    private OnItemClickListener mItemClickListener;
    private Context context;
    private List<Record> list;
    private String[] categorys;
    static class reViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView record_time;
        TextView record_detail;
        TextView record_category;
        TextView record_money;
        public reViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            record_time = itemView.findViewById(R.id.record_time);
            record_detail = itemView.findViewById(R.id.record_detail);
            record_category = itemView.findViewById(R.id.record_category);
            record_money = itemView.findViewById(R.id.record_money);
        }
    }
    @NonNull
    @Override
    public reViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.i("adapter", "holder");
        View v = LayoutInflater.from(context).inflate(R.layout.record_item, parent, false);
        return new reViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull reViewHolder holder, int position) {
        Record r = list.get(position);
        if(r.isSign()) {
            holder.record_money.setText("收支：- " + r.getMoney().toString() + " ￥");
        }
        else {
            holder.record_money.setText("收支：+ " + r.getMoney().toString() + " ￥");
        }
        holder.record_category.setText("类别：" + categorys[r.getCategory()]);
        holder.record_time.setText("时间：" + String.valueOf(r.getAddTime().getYear() + 1900) + "-" + r.getAddTime().getMonth() + "-" + r.getAddTime().getDay());
        holder.record_detail.setText("描述：" + r.getDetail());
        Log.i("adapter", "bind holder");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public RecordAdapter(List<Record> list, Context context) {
        this.list = list;
        this.context = context;
        categorys = this.context.getResources().getStringArray(R.array.category);
        Log.i("adapter", "construct" + list.size());
    }
}


