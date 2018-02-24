package com.urbannightdev.cardiopp.helper;

import com.urbannightdev.cardiopp.helper.API.ApiCall;
import com.urbannightdev.cardiopp.helper.utils.RetrofitClientUtils;

/**
 * Created by ghifar on 24/02/18.
 */

public class RetrofitServices {
    public static ApiCall sendRequest(){
        return RetrofitClientUtils.client().create(ApiCall.class);
    }
}
