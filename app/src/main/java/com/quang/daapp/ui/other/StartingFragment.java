package com.quang.daapp.ui.other;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HTTP;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quang.daapp.R;
import com.quang.daapp.data.service.AccountService;
import com.quang.daapp.data.service.RetrofitClient;
import com.quang.daapp.ultis.AuthTokenManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartingFragment extends Fragment {

    NavController navController;

    public StartingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_starting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String token = AuthTokenManager.getToken(getContext());
        navController = Navigation.findNavController(view);
        if(token == null || token.isEmpty()) {
            navController.navigate(R.id.loginFragment);
        }
        RetrofitClient.getRetrofitInstance().create(AccountService.class).check(token).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.code() == 200) {

                    if(response.body()) {
                        //Move expert
                    }else {
                        //Move customer
                        navController.navigate(R.id.authActivity);
                        getActivity().finish();
                    }

                }else if(response.code() == 202) {
                    //Relog
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }
}
