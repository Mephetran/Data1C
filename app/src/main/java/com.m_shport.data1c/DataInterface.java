package com.m_shport.data1c;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface DataInterface {

    @GET()
    Call<ListJson> getData(@Url String url);

}
