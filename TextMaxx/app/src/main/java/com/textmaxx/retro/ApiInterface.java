package com.textmaxx.retro;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Url;


public interface ApiInterface {


    @PUT
    Call<RegisterResponse> getRegisteredData(@Url String url, @Header("authorization") String authorization, @Header("Accept") String Accept, @Header("Content-Type") String Contenttpe, @Body RegisterTask task);


    @PUT
    Call<RegisterResponse> changePas(@Url String url, @Header("authorization") String authorization, @Header("Accept") String Accept, @Header("Content-Type") String Contenttpe, @Body ModelChangePas task);

    @PUT
    Call<RegisterResponse>deleteAwait(@Url String url, @Header("authorization") String authorization, @Header("Accept") String Accept, @Header("Content-Type") String Contenttpe, @Body DeleteAwait task);


}
