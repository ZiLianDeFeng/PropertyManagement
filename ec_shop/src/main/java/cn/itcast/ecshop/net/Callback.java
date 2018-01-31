package cn.itcast.ecshop.net;

import cn.itcast.ecshop.bean.response.ErrorResponseInfo;

/**
 * Created by Administrator on 2016/7/24.
 *
 *      成功，其他，错误
 */
public interface Callback<Res> {

    public abstract void onSuccess(BaseRequest request, Res response);

    public abstract void onOther(BaseRequest request, ErrorResponseInfo errorResponseInfo);

    public abstract void onError(BaseRequest request, Exception e);

}
