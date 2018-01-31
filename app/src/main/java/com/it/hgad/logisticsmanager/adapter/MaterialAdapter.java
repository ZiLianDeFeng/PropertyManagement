package com.it.hgad.logisticsmanager.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.Material;
import com.it.hgad.logisticsmanager.util.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/12/29.
 */
public class MaterialAdapter extends BaseAdapter {
    private List<Material> data;
    private boolean isEdit;

    public MaterialAdapter(List<Material> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size() == 0 ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_material);
        final ImageView mIvAdd = holder.getIv(R.id.iv_pd_add);
        final ImageView mIvCut = holder.getIv(R.id.iv_pd_cut);
        final TextView mTvCount = holder.getTv(R.id.tv_product_count);
        final TextView tv_count = holder.getTv(R.id.tv_count);
        LinearLayout ll_count = holder.getLl(R.id.ll_count);
        CheckBox mCb = holder.getCb(R.id.cb);
        if (isEdit){
//            ll_count.setVisibility(View.VISIBLE);
            mCb.setVisibility(View.VISIBLE);
//            tv_count.setVisibility(View.INVISIBLE);
        }else {
//            ll_count.setVisibility(View.INVISIBLE);
            mCb.setVisibility(View.INVISIBLE);
//            tv_count.setVisibility(View.VISIBLE);
        }
        TextView tv_name = holder.getTv(R.id.tv_name);
        TextView tv_type = holder.getTv(R.id.tv_type);
        final Material material = data.get(position);
//        material.setChecked(false);
        tv_name.setText(material.getName());
        tv_type.setText(material.getType());
        mTvCount.setText(material.getNumber()+"");
        tv_count.setText(material.getNumber()+"");
        mCb.setChecked(material.isChecked());
        mCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                material.setChecked(isChecked);
            }
        });
        mIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = material.getNumber() + 1;
                mIvCut.setEnabled(true);
                mIvCut.setBackgroundColor(Color.WHITE);
//                if (num >= 11) {
//                    mIvAdd.setEnabled(false);
//                    mIvAdd.setBackgroundColor(Color.DKGRAY);
//                } else {

                    material.setNumber(num);
                    mTvCount.setText(material.getNumber()+"");
                    tv_count.setText(material.getNumber()+"");
//                }
            }
        });
        mIvCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = material.getNumber() - 1;
                mIvAdd.setEnabled(true);
                mIvAdd.setBackgroundColor(Color.WHITE);
                if (num <= 0) {
                    mIvCut.setEnabled(false);
                    mIvCut.setBackgroundColor(Color.DKGRAY);
                } else {
//                    mDetailBean.setProductNum(num);
//                    mProductPayBean.setProductNum(num);
                    material.setNumber(num);
                    mTvCount.setText(material.getNumber()+"");
                    tv_count.setText(material.getNumber()+"");

                }
            }
        });
        return holder.convertView;
    }
    public void setCompile(){
        isEdit = !isEdit;
        notifyDataSetChanged();
    }
}
