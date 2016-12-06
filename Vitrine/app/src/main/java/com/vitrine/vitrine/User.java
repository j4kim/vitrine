package com.vitrine.vitrine;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by loris.ceschin on 20/11/2016.
 */

public class User implements Parcelable{
    private String name;
    private String email;
    private String token;

    public User(String jsonResponse) throws JSONException {
        JSONObject mainObject = new JSONObject(jsonResponse);
        name = mainObject.getString("username");
        email = mainObject.getString("email");
        token = mainObject.getString("token");
    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        token = in.readString();
    }

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

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

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

    public String toJson(){
        // {"username":"loris","email":"loris@loris.com","token":"asdfasdfasdfasdfhasdfjhsaidfhinsofdnsiodofnaosdfinoasdifn"}
        return "{\"username\":\""+name+"\",\"email\":\""+email+"\",\"token\":\""+token+"\"}";
    }
}
