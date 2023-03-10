package com.example.petswalking;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class AccountFragment extends Fragment implements View.OnClickListener {

    private AccountViewModel mViewModel;
    private TextView tv_signup, tv_forget_password;
    private Button btn_login;
    private EditText et_username, et_password;
    private DBOpenHelper DB;

    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    private ImageView google_login;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        tv_signup = view.findViewById(R.id.tv_signup);
        tv_signup.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_signup.setOnClickListener(this);

        DB = new DBOpenHelper(getActivity());

        tv_forget_password = view.findViewById(R.id.tv_forget_password);
        tv_forget_password.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_forget_password.setOnClickListener(this);

        btn_login = view.findViewById(R.id.btn_login);
        et_username = view.findViewById(R.id.et_username);
        et_password = view.findViewById(R.id.et_password);

        btn_login.setOnClickListener(this);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions);

        google_login = view.findViewById(R.id.google_login);
        google_login.setOnClickListener(this);
        
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_signup:
                Intent intent = new Intent(getActivity(), SignUpActivity.class);
                startActivity(intent);

                break;

            case R.id.btn_login:
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    Toast.makeText(this.getContext(), "All fields Required!", Toast.LENGTH_LONG).show();
                }else {
                    Boolean checkUsernamePassword = DB.checkUsernamePassword(username, password);

                    if (checkUsernamePassword == true && checkUsernamePassword != null){
                        Toast.makeText(this.getContext(), "Login Successfully!", Toast.LENGTH_LONG).show();
                        intent = new Intent(requireActivity(), MainActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(this.getContext(), "Login Failed!", Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case R.id.tv_forget_password:
                intent = new Intent(getActivity(), ForgetPassword.class);
                startActivity(intent);
                break;

            case R.id.google_login:
                signin();
                break;

            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);

                Toast.makeText(this.getContext(), "Login Successfully!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
            } catch (ApiException e) {
                Toast.makeText(this.getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void signin() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }
}






















