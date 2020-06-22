package com.quang.daapp.ultis;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quang.daapp.R;
import com.quang.daapp.data.model.Customer;
import com.quang.daapp.ui.dialog.MessageDialogFragment;
import com.quang.daapp.ui.other.CustomerActivity;
import com.quang.daapp.ui.other.ExpertActivity;
import com.quang.daapp.ui.other.UnAuthActivity;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkClient {

    private static NetworkClient instance;
    public static final String BASE_URL = "http://192.168.137.1:8080";

    private static final String IMG_URL = "/request/image?imgName=";

    public static NetworkClient getInstance(){
        if(instance == null) {
            instance = new NetworkClient();
        }
        return  instance;
    }

    public void init(Context context, AppCompatActivity activity, NavController navController) {
        this.navController = navController;
        this.token = AuthTokenManager.getToken(context);
        this.context = context;
        this.activity = activity;
        this.retrofit = null;
    }

    private  Retrofit retrofit;
    private NavController navController;
    private  Gson gson;
    private  String token;
    private Context context;
    private AppCompatActivity activity;

    public  Gson getGson() {
        if(gson == null) {
             gson = new GsonBuilder()
                    .setLenient()
                    .setDateFormat("yyyy-MM-dd")
                    .create();
        }
        return  gson;
    }



    public  Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                                          @Override
                                          public Response intercept(Interceptor.Chain chain) throws IOException {
                                              Request original = chain.request();

                                              Request request = original.newBuilder()
                                                      .header("authorization", token)
                                                      .method(original.method(), original.body())
                                                      .build();

                                              Response response = chain.proceed(request);

                                              switch (response.code()) {
                                                  case 400: {
                                                      MessageDialogFragment dialog400 = new MessageDialogFragment(
                                                              context.getString(R.string.mes_error_400), R.color.colorDanger, R.drawable.ic_error

                                                      );
                                                      dialog400.show(activity.getSupportFragmentManager(),"400");

                                                      break;
                                                  }
                                                  case 401: {
                                                      AuthTokenManager.removeToken(context);
                                                      MessageDialogFragment dialog401 = new MessageDialogFragment(
                                                              context.getString(R.string.mes_error_401), R.color.colorDanger, R.drawable.ic_error
                                                              , () -> {

                                                                  if(activity instanceof  CustomerActivity) {
                                                                        navController.navigate(R.id.unAuthActivity);
                                                                  }else if(activity instanceof ExpertActivity) {
                                                                        navController.navigate(R.id.unAuthActivity2);
                                                                  }else if(activity instanceof UnAuthActivity) {
                                                                        navController.navigate(R.id.loginFragment);
                                                                  }
                                                              }
                                                      );
                                                      dialog401.show(activity.getSupportFragmentManager(),"401");

                                                      break;
                                                  }
                                                  case 403: {
                                                      MessageDialogFragment dialog403 = new MessageDialogFragment(
                                                              context.getString(R.string.mes_error_403), R.color.colorDanger, R.drawable.ic_error
                                                      );
                                                      dialog403.show(activity.getSupportFragmentManager(),"403");
                                                      break;
                                                  }

                                              }


                                              return  response;
                                          }
                                      });


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
                    .build();
        }
        return retrofit;
    }

    public static String getImageUrl(String imageName) {
        return BASE_URL + IMG_URL + imageName;
    }





}
