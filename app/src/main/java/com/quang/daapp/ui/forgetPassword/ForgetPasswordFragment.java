package com.quang.daapp.ui.forgetPassword;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.quang.daapp.R;
import com.quang.daapp.ui.dialog.LoaderDialogFragment;
import com.quang.daapp.ui.dialog.MessageDialogFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetPasswordFragment extends Fragment {

    private ForgetPasswordViewModel viewModel;

    public ForgetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel =
                ViewModelProviders.of(this).get(ForgetPasswordViewModel.class);

        return inflater.inflate(R.layout.fragment_forget_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText edtEmail = view.findViewById(R.id.edtEmail);

        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        view.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPasswordFormState formState = viewModel.formState(edtEmail.getText().toString());
                if(!formState.isDataValid()) {
                    if(formState.getEmailError() != null) edtEmail.setError(getString(formState.getEmailError()));
                    return;
                }
                LoaderDialogFragment loader = new LoaderDialogFragment();
                loader.show(getParentFragmentManager(),getTag());
                viewModel.forgetPassword(edtEmail.getText().toString());
                viewModel.getForgetPasswordResult().observe(getViewLifecycleOwner(), new Observer<Number>() {
                    @Override
                    public void onChanged(Number number) {
                        loader.dismiss();
                        if(number == null) return;
                        if(number.intValue() == 200) {
                            MessageDialogFragment dialogFragment = new MessageDialogFragment(getString(R.string.forget_password_success),
                                    R.color.colorSuccess, R.drawable.ic_success
                                    , () -> Navigation.findNavController(view).popBackStack()
                            );
                            dialogFragment.show(getParentFragmentManager(),getTag());
                        }

                        else if (number.intValue() == 202) {
                            MessageDialogFragment dialogFragment = new MessageDialogFragment(getString(R.string.forget_password_no_email),
                                    R.color.colorWarning, R.drawable.ic_warning
                            );
                            dialogFragment.show(getParentFragmentManager(),getTag());
                        }

                        else if (number.intValue() == 400) {
                            MessageDialogFragment dialogFragment = new MessageDialogFragment(getString(R.string.mes_error_400),
                                    R.color.colorDanger, R.drawable.ic_error
                            );
                            dialogFragment.show(getParentFragmentManager(),getTag());
                        }
                    }
                });


            }
        });
    }
}
