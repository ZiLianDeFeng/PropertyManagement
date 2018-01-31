package cn.itcast.ecshop.fragment;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import cn.itcast.ecshop.bean.response.ErrorResponseInfo;
import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.net.Callback;
import cn.itcast.ecshop.net.NetUtil;

public abstract class BaseFragment extends Fragment implements Callback<BaseReponse> {

    protected Context mContext;
    private View mRootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    public void sendRequest(BaseRequest request, Class<? extends BaseReponse> responseClass) {

        NetUtil.sendRequest(request, responseClass, this);
    }

    public abstract <Res extends BaseReponse> void onSuccessResult(BaseRequest request, Res response);

    @Override
    public void onSuccess(BaseRequest request, BaseReponse response) {

        onSuccessResult(request, response);
    }

    @Override
    public void onOther(BaseRequest request, ErrorResponseInfo errorResponseInfo) {
//        if (errorResponseInfo != null) {
////            CommonUtils.showToast(getContext(), errorResponseInfo.error);
//            //没有登录，跳转到登录界面
//            switch (errorResponseInfo.error_code) {
//                case "1530":
//                    CommonUtils.showToast(getContext(), "用户名不存在或密码错误");
//                    break;
//                case "1531":
//                    CommonUtils.showToast(getContext(), "userid请求头为空");
//                    break;
//                case "1532":
//                    CommonUtils.showToast(getContext(), "该用户名已经被注册过了");
//                    break;
//                case "1533":
//                    CommonUtils.showToast(getContext(), "需要登录");
//                    break;
//                case "1534":
//                    CommonUtils.showToast(getContext(), "请求参数错误或缺失");
//                    break;
//                case "1535":
//                    CommonUtils.showToast(getContext(), "当前商品已经添加过收藏");
//                    break;
//                case "1536":
//                    CommonUtils.showToast(getContext(), "商品已经添加收藏失败");
//                    break;
//                case "1537":
//                    CommonUtils.showToast(getContext(), "取消订单失败");
//                    break;
//                case "1538":
//                    CommonUtils.showToast(getContext(), "该详情在盘古开天辟地时不小心丢失了哦！");
//                    getActivity().finish();
//                    break;
//                default:
//                    break;
//            }
//        }
    }

    @Override
    public void onError(BaseRequest request, Exception e) {

    }

}
