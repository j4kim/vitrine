package com.vitrine.vitrine;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Objet vitrine
 */

public class Vitrine implements Parcelable {

    private int id;
    private String name;
    private double radius;
    private double latitude;
    private double longitude;
    private ArrayList<String> pictures;
    private int color;

    /**
     * Standard constructor
     * @param name name
     * @param radius radius
     * @param latitude latitude
     * @param longitude longitude
     * @param color color
     */
    public Vitrine(String name, int radius, double latitude, double longitude, int color){
        this.name = name;
        this.radius = radius;
        this.latitude = latitude;
        this.longitude = longitude;
        this.color = color;
        this.pictures = new ArrayList<>();
    }

    /**
     * Constructor from JSON String
     * @param jsonResponse JSON String
     * @throws JSONException
     */
    public Vitrine(String jsonResponse) throws JSONException {
        JSONObject mainObject = new JSONObject(jsonResponse);
        id = mainObject.getInt("id");
        name = mainObject.getString("name");
        radius = mainObject.getDouble("radius");
        latitude = mainObject.getDouble("latitude");
        longitude = mainObject.getDouble("longitude");
        try{
            color = Color.parseColor(mainObject.getString("color"));
        }catch (IllegalArgumentException e){
            color = Color.BLUE;
        }
        pictures = new ArrayList<>();

    }

    /*
     * Functions
     */

    /**
     * Add a path to the pictures array
     * @param path path of the picture
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

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    public int getColor() { return color; }

    public ArrayList<String> getPictures() { return pictures; }

    public String getPictureAtIndex(int index)
    {
        try {
            index = index % pictures.size();
            return pictures.get(index);
        }
        catch (ArithmeticException e)
        {
            return "";
        }
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
