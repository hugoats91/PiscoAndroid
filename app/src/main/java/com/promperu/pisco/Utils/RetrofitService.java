package com.promperu.pisco.Utils;

import com.promperu.pisco.LocalService.AppDatabase;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConstantList.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();


    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    private static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + AppDatabase.INSTANCE.userDao().getEntityUser().getToken())
                        .build();
                return chain.proceed(newRequest);
            }).addInterceptor(new HttpLoggingInterceptor()).build();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(AppConstantList.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client);

    private static Retrofit retrofitAuthorization = builder.build();

    public static <S> S createServiceAuthorization(Class<S> serviceClass) {
        return retrofitAuthorization.create(serviceClass);
    }

}