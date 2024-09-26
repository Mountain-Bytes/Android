package com.example.bookswap;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sendbird.uikit.SendbirdUIKit;
import com.sendbird.uikit.adapter.SendbirdUIKitAdapter;
import com.sendbird.uikit.interfaces.UserInfo;
import com.sendbird.android.handler.InitResultHandler;
import com.sendbird.android.exception.SendbirdException;
import com.sendbird.uikit.model.configurations.UIKitConfig;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SendbirdUIKit.init(new SendbirdUIKitAdapter() {
            @NonNull
            @Override
            public String getAppId() {
                return "7D0C9372-4276-4A0D-A358-60C65A49E30E";
            }

            @Nullable
            @Override
            public String getAccessToken() {
                return "";
            }

            @NonNull
            @Override
            public UserInfo getUserInfo() {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                if (firebaseUser != null) {
                    return new UserInfo() {
                        @Override
                        public String getUserId() {
                            // Return Firebase User ID
                            return firebaseUser.getUid();
                        }

                        @Nullable
                        @Override
                        public String getNickname() {
                            // Return Firebase display name or fallback to User ID if no display name is set
                            return firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : firebaseUser.getUid();
                        }

                        @Nullable
                        @Override
                        public String getProfileUrl() {
                            // Return Firebase profile picture URL if available
                            return firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : "";
                        }
                    };
                } else {
                    // If FirebaseAuth.getCurrentUser() is null, return a default or error user info
                    return new UserInfo() {
                        @Override
                        public String getUserId() {
                            return "unknown_user";
                        }

                        @Nullable
                        @Override
                        public String getNickname() {
                            return "Guest";
                        }

                        @Nullable
                        @Override
                        public String getProfileUrl() {
                            return "";
                        }
                    };
                }
            }

            @NonNull
            @Override
            public InitResultHandler getInitResultHandler() {
                return new InitResultHandler() {
                    @Override
                    public void onMigrationStarted() {
                        // DB migration has started.
                    }

                    @Override
                    public void onInitFailed(SendbirdException e) {
                        // If DB migration fails, this method is called.
                    }

                    @Override
                    public void onInitSucceed() {
                        // If DB migration is successful, this method is called and you can proceed to the next step.
                        // In the sample app, the `LiveData` class notifies you on the initialization progress
                        // And observes the `MutableLiveData<InitState> initState` value in `SplashActivity()`.
                        // If successful, the `LoginActivity` screen
                        // Or the `HomeActivity` screen will show.
                    }
                };
            }
        }, this);

        UIKitConfig.getGroupChannelConfig().setEnableVoiceMessage(true);
    }
}