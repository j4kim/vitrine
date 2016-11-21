package com.vitrine.vitrine;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by loris.ceschin on 20/11/2016.
 */

public class Vitrine implements Parcelable {

    private int id;
    private String name;
    private double radius;
    private double latitude;
    private double longitude;
    private ArrayList<String> pictures;

    public Vitrine(String jsonResponse) throws JSONException {
        JSONObject mainObject = new JSONObject(jsonResponse);
        id = mainObject.getInt("id");
        name = mainObject.getString("name");
        radius = mainObject.getDouble("radius");
        latitude = mainObject.getDouble("latitude");
        longitude = mainObject.getDouble("longitude");
        pictures = new ArrayList<>();
    }

    /*
     * Functions
     */
    public void addPicture(String path)
    {
        pictures.add(path);
    }


    /*
     * Getters and setters
     */

    public int getId() { return id; }

    public String getName() {
        return name;
    }

    public double getRadius() {
        return radius;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }



    /*
     *  Parcelable implementation
     */

    protected Vitrine(Parcel in) {
        id = in.readInt();
        name = in.readString();
        radius = in.readDouble();
        latitude = in.readDouble();
        longitude = in.readDouble();
        pictures = in.readArrayList(null);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(radius);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeList(pictures);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Vitrine> CREATOR = new Creator<Vitrine>() {
        @Override
        public Vitrine createFromParcel(Parcel in) {
            return new Vitrine(in);
        }

        @Override
        public Vitrine[] newArray(int size) {
            return new Vitrine[size];
        }
    };

}
