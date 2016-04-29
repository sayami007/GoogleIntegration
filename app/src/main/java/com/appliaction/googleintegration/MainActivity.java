package com.appliaction.googleintegration;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SignInButton signInButton;
    Button signOut;
    GoogleApiClient aps;
    GoogleSignInOptions gso;
    int RC_SIGN_IN = 100;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        aps = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signOut=(Button)findViewById(R.id.signOut);
        signOut.setOnClickListener(this);
        signInButton.setScopes(gso.getScopeArray());
        iv = (ImageView) findViewById(R.id.image);
        signInButton.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.signOut:
                signOut();
                break;

        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(aps).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(getApplicationContext(), ""+status, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void signIn() {
        Intent s = Auth.GoogleSignInApi.getSignInIntent(aps);
        startActivityForResult(s, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            try {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            } catch (Exception err) {

            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) throws IOException {
        Log.v("tag", "handleSignInResult:" + result.isSuccess());
        Bitmap m=null;
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.v("name", acct.getDisplayName() + " " + acct.getPhotoUrl() + "" + acct.getId());
            try{
                Picasso.with(getApplicationContext()).load(acct.getPhotoUrl()).into(iv);
            }catch (Exception err){
                Toast.makeText(getApplicationContext(),err.getMessage(),Toast.LENGTH_SHORT).show();
            }
        } else {
            // Signed out, show unauthenticated UI.

        }
    }
}

