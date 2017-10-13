package com.example.admin1.facebooklogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin1 on 8/11/17.
 */

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";
    private CallbackManager callbackManager;
    private TextView textView, textHello, textEmail, textBirthday, textGender;
    private ImageView imageView;
    private LoginButton loginButton;
    private Button button;
    private String firstName, lastName, email, birthday, gender;
    private URL profilePicture;
    private String userId;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

//    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
//        @Override
//        public void onSuccess(LoginResult loginResult) {
//            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//                @Override
//                public void onCompleted(JSONObject object, GraphResponse response) {
//
//                    try {
//                        userId = object.getString("id");
//                        profilePicture = new URL("https://graph.facebook.com/" + userId + "/picture?width=500&height=500");
//                        if (object.has("first_name"))
//                            firstName = object.getString("first_name");
//                        if (object.has("last_name"))
//                            lastName = object.getString("last_name");
//                        if (object.has("email"))
//                            email = object.getString("email");
//                        if (object.has("birthday"))
//                            birthday = object.getString("birthday");
//                        if (object.has("gender"))
//                            gender = object.getString("gender");
//                        displayMessage(Profile.getCurrentProfile());
//                        displayDetail(email, birthday, gender);
//                        // displayMessage(firstName, lastName, profilePicture.toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            Bundle parameters = new Bundle();
//            parameters.putString("fields", "id, first_name, last_name, email, birthday, gender");
//            request.setParameters(parameters);
//            request.executeAsync();
//        }
//
//        @Override
//        public void onCancel() {
//            Log.d(TAG, "onCancel: ");
//        }
//
//        @Override
//        public void onError(FacebookException e) {
//            Log.d(TAG, "onError: " + e.toString());
//        }
//    };

    private void displayDetail(String email, String birthday, String gender) {
        if (email != null) {
            textEmail.setVisibility(View.VISIBLE);
            textEmail.setText(email);
        } else {
            textEmail.setVisibility(View.INVISIBLE);
        }
        if (birthday != null) {
            textBirthday.setVisibility(View.VISIBLE);
            textBirthday.setText(birthday);
        } else {
            textBirthday.setVisibility(View.INVISIBLE);
        }
        if (gender != null) {
            textGender.setVisibility(View.VISIBLE);
            textGender.setText(gender);
        } else {
            textGender.setVisibility(View.INVISIBLE);
        }
    }

    public MainFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
//
//            }
//        };
//
//        profileTracker = new ProfileTracker() {
//            @Override
//            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
//                displayMessage(newProfile);
//            }
//        };
//
//        accessTokenTracker.startTracking();
//        profileTracker.startTracking();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        textView = (TextView) view.findViewById(R.id.textView);
        textHello = (TextView) view.findViewById(R.id.text_hello);
        textEmail = (TextView) view.findViewById(R.id.email);
        textBirthday = (TextView) view.findViewById(R.id.birthday);
        textGender = (TextView) view.findViewById(R.id.gender);
        imageView = (ImageView) view.findViewById(R.id.profileImage);
        button = (Button) view.findViewById(R.id.button);
//        loginButton.setHeight(100);
//        loginButton.setTextColor(Color.WHITE);
//        loginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//        loginButton.setCompoundDrawablePadding(0);
//        loginButton.setReadPermissions("public_profile");
//        loginButton.setFragment(this);
//        loginButton.registerCallback(callbackManager, callback);

        //facebook login with custom button click
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button) {
              LoginManager.getInstance().logInWithReadPermissions(getActivity(),
                    Arrays.asList("public_profile"));

                }
            }
        });

        LoginManager.getInstance().registerCallback(
                callbackManager,
                new FacebookCallback < LoginResult > () {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // Handle success
                        graphAPICall(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        displayMessage("dhara","cancel",null);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "onError: "+exception.toString());
                        displayMessage("error","error",null);
                    }
                }
        );
    }
    private void graphAPICall(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        try {
                            userId = object.getString("id");
                            profilePicture = new URL("https://graph.facebook.com/" + userId + "/picture?width=500&height=500");
                            if (object.has("first_name"))
                                firstName = object.getString("first_name");
                            if (object.has("last_name"))
                                lastName = object.getString("last_name");
                            if (object.has("email"))
                                email = object.getString("email");
                            if (object.has("birthday"))
                                birthday = object.getString("birthday");
                            if (object.has("gender"))
                                gender = object.getString("gender");
                            displayMessage(Profile.getCurrentProfile());
                            displayDetail(email, birthday, gender);
                            // displayMessage(firstName, lastName, profilePicture.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, birthday, gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void displayMessage(String firstName, String lastName, String imageUrl) {
        if (firstName != null) {
            textView.setVisibility(View.VISIBLE);
            textHello.setVisibility(View.VISIBLE);
            String fullName = firstName + "" + lastName;
            textView.setText(fullName);
        }
        if (imageUrl != null) {
            Picasso.with(getActivity())
                    .load(imageUrl)
                    .into(imageView);
        }
    }

    private void displayMessage(Profile profile) {
        if (profile != null) {
            if (profile.getFirstName() != null) {
                textView.setVisibility(View.VISIBLE);
                textHello.setVisibility(View.VISIBLE);
                String fullName = profile.getFirstName() + "" + profile.getLastName();
                textView.setText(fullName);
            }
            if (profile.getProfilePictureUri(200, 200) != null) {
                imageView.setVisibility(View.VISIBLE);
                Picasso.with(getActivity())
                        .load(profile.getProfilePictureUri(200, 200))
                        .into(imageView);
            }
        } else {
            textView.setVisibility(View.INVISIBLE);
            textHello.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
        textEmail.setVisibility(View.INVISIBLE);
        textGender.setVisibility(View.INVISIBLE);
        textBirthday.setVisibility(View.INVISIBLE);

    }
}
