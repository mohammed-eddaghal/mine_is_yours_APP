package com.ensias.mine_is_yoursapp.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context _context;


    public static final String SESSION_USERSESSION = "UserLoginSession"; // Name Of the session
    public static final String SESSION_REMEMBERME = "rememberMe"; // Name Of the session
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_EMAIL = "name";
    public static final String KEY_PASSWORD = "email";
    public static final String KEY_SESSION_EMAIL = "name";
    public static final String KEY_SESSION_PASSWORD = "email";
    public static final String IsRememberMe="isRememberMe";
    private static final String USER_FIRSTNAME = "firstName";
    private static final String USER_LASTNAME = "lastName";
    private static final String USER_ID = "";
    private static final String USER_IMAGE = "";
    private static final String USER_LANGITUDE = "";
    private static final String USER_LANTITUDE = "";

    public SessionManager(Context context,String sessionName){
        this._context = context;
        userSession = _context.getSharedPreferences(sessionName, Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public void createUserSession(User user){
        editor.putString(USER_LASTNAME,user.getLastName());
        editor.putString(USER_FIRSTNAME,user.getFirstName());
        editor.putString(USER_ID,user.getId());
        editor.putString(USER_IMAGE,user.getImage());
        //editor.putFloat(USER_LANGITUDE,user.getLangitude());
        //editor.putFloat(USER_LANTITUDE,user.getLantitude());
    }
    public void createSession(String email,String password){
        editor.putBoolean(IsRememberMe,true);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_PASSWORD,password);
        editor.commit();
    }
    public void createRememberMeSession(String email,String password){
        editor.putBoolean(IsRememberMe,true);
        editor.putString(KEY_SESSION_EMAIL,email);
        editor.putString(KEY_SESSION_PASSWORD,password);
        editor.commit();
    }

    public HashMap<String,String> getRememberMeSessionDetails(){
        HashMap<String , String> userInformation = new HashMap<>();
        userInformation.put(KEY_SESSION_EMAIL,userSession.getString(KEY_SESSION_EMAIL,null));
        userInformation.put(KEY_SESSION_PASSWORD,userSession.getString(KEY_SESSION_PASSWORD,null));
        return  userInformation;
    }

    public boolean checkLogin(){
        return userSession.getBoolean(IS_LOGIN, false);
    }

    public boolean checkRememberMe(){
        return userSession.getBoolean(IsRememberMe, false);
    }
}