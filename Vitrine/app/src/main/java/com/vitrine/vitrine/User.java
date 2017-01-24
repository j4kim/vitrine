package com.vitrine.vitrine;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Objet utilisateur
 */

public class User implements Parcelable{
    private String name;
    private String email;
    private String token;

    /**
     * Standard constructor
     * @param jsonResponse JSON string to decode
     * @throws JSONException
     */
    public User(String jsonResponse) throws JSONException {
        JSONObject mainObject = new JSONObject(jsonResponse);
        name = mainObject.getString("username");
        email = mainObject.getString("email");
        token = mainObject.getString("token");
    }

    /**
     * Constructor from a Parcel (unserialize constructor)
     * @param in Parcel to create user from
     */
    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        token = in.readString();
    }

    /**
     * Parcelable method
     */
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(token);
    }

    // Convert the user to a json string used for preferences
    public String toJson(){
        return "{\"username\":\""+name+"\",\"email\":\""+email+"\",\"token\":\""+token+"\"}";
    }

    /*
     * GETTERS
     */
    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }
}
