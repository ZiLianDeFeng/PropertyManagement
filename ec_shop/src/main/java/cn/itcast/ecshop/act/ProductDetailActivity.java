package cn.itcast.ecshop.act;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.itcast.ecshop.R;
import cn.itcast.ecshop.bean.request.FavoriteRequest;
import cn.itcast.ecshop.bean.request.ProdcuctDetailRequest;
import cn.itcast.ecshop.bean.response.FavorityResponse;
import cn.itcast.ecshop.bean.response.ProductDetailResponse;
import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;
import cn.itcast.ecshop.net.NetUtil;

public class ProductDetailActivity extends BaseActivity {

    private TextView textViewContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail1);
        textViewContent = ((TextView) findViewById(R.id.textview_content));
    }


    public void getProductDetailData(View view) {


        NetUtil.sendRequest(new ProdcuctDetailRequest("1"), ProductDetailResponse.class, this);
    }



    public void favoriteProduct(View view) {

        //从share Preference 获取userid,如果userid为空，跳转到登录

        NetUtil.sendRequest(new FavoriteRequest("1"), FavorityResponse.class, this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

        if (request instanceof ProdcuctDetailRequest) {

            ProductDetailResponse productdetailresponse  = (ProductDetailResponse) response;

            if (productdetailresponse!=null) {

                textViewContent.setText(productdetailresponse.toString());
            }
        } else if(request instanceof FavoriteRequest ) {

            FavorityResponse favorityresponse = (FavorityResponse) response;
            textViewContent.setText(favorityresponse.toString());
        }

    }
}
