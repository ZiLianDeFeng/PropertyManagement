package cn.itcast.ecshop.act;

import android.support.v7.app.AppCompatActivity;

import cn.itcast.ecshop.bean.response.ErrorResponseInfo;
import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.net.Callback;
import cn.itcast.ecshop.net.NetUtil;

public abstract class BaseActivity extends AppCompatActivity implements Callback<BaseReponse> {


    public void sendRequest(BaseRequest request, Class<? extends BaseReponse> responseClass) {

        NetUtil.sendRequest(request, responseClass, this);
    }

    public abstract void onSuccessResult(BaseRequest request, BaseReponse response);


    @Override
    public void onSuccess(BaseRequest request, BaseReponse response) {

        onSuccessResult(request, response);
    }


    /*

        所有的错误吗处理都在这个地方
     */
    @Override
    public void onOther(BaseRequest request, ErrorResponseInfo errorResponseInfo) {
        if (errorResponseInfo != null) {
//            CommonUtils.showToast(BaseActivity.this, "网络不稳定！");
            //没有登录，跳转到登录界面

//            if ("1533".equals(errorResponseInfo.error_code)) {
//
//                Toast.makeText(this, "需要去登录", Toast.LENGTH_SHORT).show();
//
//            }
//            switch (errorResponseInfo.error_code) {
//                case "1530":
//                    CommonUtils.showToast(this, "用户名不存在或密码错误");
//                    break;
//                case "1531":
//                    CommonUtils.showToast(this, "userid请求头为空");
//                    break;
//                case "1532":
//                    CommonUtils.showToast(this, "该用户名已经被注册过了");
//                    break;
//                case "1533":
//                    CommonUtils.showToast(this, "需要登录");
//                    break;
//                case "1534":
//                    CommonUtils.showToast(this, "请求参数错误或缺失");
//                    break;
//                case "1535":
//                    CommonUtils.showToast(this, "当前商品已经添加过收藏");
//                    break;
//                case "1536":
//                    CommonUtils.showToast(this, "商品已经添加收藏失败");
//                    break;
//                case "1537":
//                    CommonUtils.showToast(this, "取消订单失败");
//                    break;
//                case "1538":
//                    CommonUtils.showToast(this, "该详情在盘古开天辟地时不小心丢失了哦！");
//                    break;
//                default:
//                    break;
//            }
        }
    }

    @Override
    public void onError(BaseRequest request, Exception e) {

    }
}

