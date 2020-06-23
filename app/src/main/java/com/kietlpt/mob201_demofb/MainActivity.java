package com.kietlpt.mob201_demofb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;


import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    Button btnShareDialog;
    ImageView imgShare;
    CallbackManager callbackManager;
    ProfilePictureView profilePictureView;
    Bitmap image;
    SharePhoto photo;




            ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Code lay KeyHash chay 1 lan
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo("com.kietlpt.mob201_demofb", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
        tv=findViewById(R.id.tv);
        imgShare=findViewById(R.id.imgShare);
        btnShareDialog=findViewById(R.id.btnShareDialog);
        profilePictureView = (ProfilePictureView) findViewById(R.id.friendProfilePicture);
        shareDialog = new ShareDialog(this);
        btnShareDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get Drawable from imageView
                Drawable drawable = imgShare.getDrawable();
                Bitmap bmp = null;
                if (drawable instanceof BitmapDrawable){
                    bmp = ((BitmapDrawable) imgShare.getDrawable()).getBitmap();
                    Log.d("t","co bitmap");
                }else Log.d("t","bitmap null");

                photo = new SharePhoto.Builder()
                        .setCaption("Photo Test")
                        .setBitmap(bmp)
                        .build();
                // only share image
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                // multishare
//                ShareContent shareContent = new ShareMediaContent.Builder()
//                        .addMedium(photo)
//                        .build();

                shareDialog.show(MainActivity.this,content);
            }
        });
        //Share link with ShareButton
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();



        ShareButton shareButton = findViewById(R.id.btnShare);
        shareButton.setShareContent(content);

        // Login

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Toast.makeText(MainActivity.this, "dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                        String userId=loginResult.getAccessToken().getUserId();

                        // lay avatar
                        profilePictureView.setProfileId(userId);
                        xulysaukhilogin(loginResult);


                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }



                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(MainActivity.this, "Loi", Toast.LENGTH_SHORT).show();
                    }
                });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void xulysaukhilogin(LoginResult loginResult){


        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("LoginActivity", response.toString());

                        // Application code
                        try {
                            tv.setText("Hi "+object.getString("name")+"!");

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "loi"+ e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email ");
        request.setParameters(parameters);
        request.executeAsync();



    }


}
