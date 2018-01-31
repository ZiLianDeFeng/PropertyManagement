package com.it.hgad.logisticsmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.LearnType;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.RvViewHolder> {
    private List<LearnType> list;
    private Context context;
    private RvItemClickListener mItemClickListener;

    public CardAdapter(List<LearnType> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, ((LearnType) v.getTag()));
                }
            }
        });
        return new RvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int position) {
        LearnType learnType = list.get(position);
        Picasso.with(context).load(learnType.getImg()).into(holder.iv_backgroud);
        holder.tv_title.setText(learnType.getTypeName());
        holder.itemView.setTag(learnType);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class RvViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_backgroud;
        private TextView tv_title;

        public RvViewHolder(View itemView) {
            super(itemView);
            iv_backgroud = (ImageView) itemView.findViewById(R.id.picture);
            tv_title = (TextView) itemView.findViewById(R.id.name);
        }

    }

    public void setOnItemClickListener(RvItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public interface RvItemClickListener {
        public void onItemClick(View view, LearnType learn);
    }
}
