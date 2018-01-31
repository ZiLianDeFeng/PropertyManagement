package com.it.hgad.logisticsmanager.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.activity.JobDetailActivity;
import com.it.hgad.logisticsmanager.util.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import me.maxwin.view.XListView;


/**
 * Created by hasee on 2016/8/9.
 */
public class RepairManageFragment extends BaseFragment implements View.OnClickListener{
    private static final int REPAIR = 110;
    private XListView lv;
    private String type = "1";
    private List<String> data = new ArrayList<>();
    private int page;
    private View mView;
//    private PopupWindow popupWindow;

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {

        if (data != null) {
            data.clear();
        }
        for (int i = 0; i < 5; i++) {
            data.add("");
        }
        mView = inflater.inflate(R.layout.fragment_main_state, null);
        initData();
        return mView;

    }


    protected void initData() {
        lv = (XListView) mView.findViewById(R.id.lv_last_order);
        lv.setPullLoadEnable(true);
        lv.setXListViewListener(mIXListViewListener);
        lv.setAdapter(MaintenanceAdapter);
        lv.setEmptyView(mView.findViewById(R.id.tv_empty));
        lv.setOnItemClickListener(mOnItemClickListener);

    }

    @Override
    protected void initView() {

    }

    private XListView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getContext(),JobDetailActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
//        if (requestCode == IS_CANCEL) {
//            String orderid = data.getStringExtra("orderid");
//            for (int i = 0; i < this.data.size(); i++) {
//                if (orderid.equals(orderListBeans.getOrderId())) {
//                    orderList.remove(orderListBeans);
//                    orderAdapter.notifyDataSetChanged();
//                }
//            }
//        }
    }

    private boolean afterLoadMore;
    private int mCurrentIndex;
    private XListView.IXListViewListener mIXListViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
//            int pageNum = orderList.size();

//            data.clear();
            for (int i = 0; i < 5; i++) {
                data.add("");
            }
//            request.setPageNum(pageNum);
//            sendRequest(request, OrderListResponse.class);
//            lv.setRefreshTime(CommonUtils.getFormatTime());
            lv.stopRefresh();

        }

        @Override
        public void onLoadMore() {
//            CommonUtils.showToast(getContext(), "没有历史数据了");
//            map = mOrderListRequest.getParams();
//            String currentPage = map.get("page");
//            map.put("page",(Integer.parseInt(currentPage)+1)+"");
//            mCurrentIndex = MaintenanceAdapter.getCount();
//            int currentPage = Integer.parseInt(page);
//            String nextPage = (currentPage + 1) + "";
//            page += 5;
//            mOrderListRequest.setPage(page);

//            sendRequest(mOrderListRequest, OrderListResponse.class);
            for (int i = 0; i < 5; i++) {
                data.add("");
            }
            MaintenanceAdapter.notifyDataSetChanged();
            lv.stopLoadMore();
            afterLoadMore = true;
        }
    };
    //    List<OrderListBean> list = new ArrayList<>();
    private BaseAdapter MaintenanceAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return data.size()==0?0:data.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_main_manage);
//            TextView opration = holder.getTv(R.id.tv_operation);
//            opration.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    initPopupWindow();
//                    Intent intent = new Intent(getContext(),MaintenanceActivity.class);
//                    startActivityForResult(intent, REPAIR);
//                }
//            });
            return holder.convertView;
        }
    };

//    private void initPopupWindow() {
//        View contentView = View.inflate(getActivity(), R.layout.popup_opration, null);
//        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff"))); //设置背景
//        popupWindow.setFocusable(true); //设置获取焦点
//        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
//        popupWindow.setOutsideTouchable(true);
//        TextView sort_number = (TextView) contentView.findViewById(R.id.sort_number);
//        sort_number.setOnClickListener(this);
//        TextView sort_type = (TextView) contentView.findViewById(R.id.sort_type);
//        sort_type.setOnClickListener(this);
//        TextView sort_time = (TextView) contentView.findViewById(R.id.sort_time);
//        sort_time.setOnClickListener(this);
//        TextView sort_state = (TextView) contentView.findViewById(R.id.sort_state);
//        sort_state.setOnClickListener(this);
//    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public <Res extends BaseReponse> void onSuccessResult(BaseRequest request, Res response) {

    }
}
