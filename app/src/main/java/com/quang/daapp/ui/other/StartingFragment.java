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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quang.daapp.R;
import com.quang.daapp.data.service.AccountService;
import com.quang.daapp.ui.dialog.MessageDialogFragment;
import com.quang.daapp.ultis.DialogManager;
import com.quang.daapp.ultis.NetworkClient;
import com.quang.daapp.ultis.AuthTokenManager;

import java.net.ConnectException;
import java.net.SocketTimeoutException;


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
        final String token = AuthTokenManager.getToken(getContext());
        navController = Navigation.findNavController(view);

        if(token == null || token.isEmpty()) {
            navController.navigate(R.id.loginFragment);

        }else {
            NetworkClient.getInstance().getRetrofitInstance().create(AccountService.class).check(token,token).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Log.e("Status", response.code() + "");
                    if(response.code() == 200) {

                        if(response.body()) {
                            navController.navigate(R.id.expertActivity);
                            getActivity().finish();
                        }else {
                            //Move customer
                            navController.navigate(R.id.authActivity);
                            getActivity().finish();
                        }

                    }else if(response.code() == 202) {
                        navController.navigate(R.id.loginFragment);
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    if(t instanceof ConnectException) {
                        MessageDialogFragment dialogTimeOut = new MessageDialogFragment(
                                "Can not connect to server, please check your connection"
                                , R.color.colorDanger, R.drawable.ic_error,
                                () -> navController.navigate(R.id.startingFragment)
                        );
                        DialogManager.getInstance().showDialog(dialogTimeOut,true);
                    }
                }
            });
        }

    }
}
