package com.cloupix.blindr.business;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by alonsousa on 15/12/15.
 *
 */
public class Sector implements Parcelable {

    public static final int MATH_GENERATED_READINGS = 0;
    public static final int MAPPED_READINGS = 1;
    public static final int ALL_READINGS = 2;

    private long sectorId;
    private int listN;
    private int matrixX;
    private int matrixY;
    private double latitude;
    private double longitude;
    private long mapId;
    private double locationProbability;

    ArrayList<Reading> readings;
    ArrayList<WifiAP> wifiAPs;



    public Sector() {
        this.readings = new ArrayList<>();
        this.wifiAPs = new ArrayList<>();
    }

    public Sector(long sectorId){
        this.sectorId = sectorId;
        this.readings = new ArrayList<>();
        this.wifiAPs = new ArrayList<>();
    }

    public Sector(int listN, int matrixX, int matrixY, double latitude, double longitude, long mapId) {
        this.listN = listN;
        this.matrixX = matrixX;
        this.matrixY = matrixY;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mapId = mapId;
        this.readings = new ArrayList<>();
        this.wifiAPs = new ArrayList<>();
    }

    public Sector(long sectorId, int listN, int matrixX, int matrixY, double latitude, double longitude, long mapId) {
        this.sectorId = sectorId;
        this.listN = listN;
        this.matrixX = matrixX;
        this.matrixY = matrixY;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mapId = mapId;
        this.readings = new ArrayList<>();
        this.wifiAPs = new ArrayList<>();
    }



    public long getSectorId() {
        return sectorId;
    }

    public void setSectorId(long sectorId) {
        this.sectorId = sectorId;
    }

    public int getListN() {
        return listN;
    }

    public void setListN(int listN) {
        this.listN = listN;
    }

    public int getMatrixX() {
        return matrixX;
    }

    public void setMatrixX(int matrixX) {
        this.matrixX = matrixX;
    }

    public int getMatrixY() {
        return matrixY;
    }

    public void setMatrixY(int matrixY) {
        this.matrixY = matrixY;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public ArrayList<Reading> getReadings() {
        return readings;
    }

    public ArrayList<Reading> getReadings(int readingType) {
        switch (readingType){
            case MATH_GENERATED_READINGS:
                return getMathGeneratedReadings();
            case MAPPED_READINGS:
                return getMappedReadings();
            case ALL_READINGS:
                return readings;
            default:
                return readings;
        }
    }

    public void setReadings(ArrayList<Reading> readings) {
        this.readings = readings;
    }

    public ArrayList<WifiAP> getWifiAPs() {
        return wifiAPs;
    }

    public void setWifiAPs(ArrayList<WifiAP> wifiAPs) {
        this.wifiAPs = wifiAPs;
    }

    public boolean hasReadings(){
        if(this.readings == null)
            return false;
        return !this.readings.isEmpty();
    }

    public boolean hasNonDeletedReadings() {
        if(!hasReadings())
            return false;
        for(Reading reading : readings)
            if(!reading.isDeleteDBEntity())
                return true;
        return false;
    }

    public ArrayList<WifiAP> getNonDeletedWifiAPs() {
        ArrayList<WifiAP> nonDeletedWifiAPs = new ArrayList<>();
        for(WifiAP wifiAP : wifiAPs){
            if(wifiAP instanceof WifiAPView){
                if(!((WifiAPView) wifiAP).isDeleteDBEntity())
                    nonDeletedWifiAPs.add(wifiAP);
            }
        }
        return nonDeletedWifiAPs;
    }

    @Override
    public String toString() {
        return this.matrixX + "x" + this.matrixY;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(sectorId);
        dest.writeInt(listN);
        dest.writeInt(matrixX);
        dest.writeInt(matrixY);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeLong(mapId);
        dest.writeList(readings);
        dest.writeList(wifiAPs);
    }

    protected Sector(Parcel in) {
        sectorId = in.readLong();
        listN = in.readInt();
        matrixX = in.readInt();
        matrixY = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        mapId = in.readLong();
        readings = in.readArrayList(getClass().getClassLoader());
        wifiAPs = in.readArrayList(getClass().getClassLoader());
    }

    public static final Creator<Sector> CREATOR = new Creator<Sector>() {
        @Override
        public Sector createFromParcel(Parcel in) {
            return new Sector(in);
        }

        @Override
        public Sector[] newArray(int size) {
            return new Sector[size];
        }
    };

    public double getLocationProbability() {
        return locationProbability;
    }

    public void setLocationProbability(double locationProbability) {
        this.locationProbability = locationProbability;
    }

    public boolean hasLatLong() {
        //TODO Esto no está del todo bien, poner un valor -1 a los lat long que no tengan valor que comprobar con eso
        return latitude!=0 && longitude!=0;
    }

    private ArrayList<Reading> getMathGeneratedReadings() {
        ArrayList<Reading> mathGeneratedReadings = new ArrayList<>();
        for(Reading reading : readings)
            if(reading.isMathGenerated())
                mathGeneratedReadings.add(reading);
        return mathGeneratedReadings;
    }

    private ArrayList<Reading> getMappedReadings() {
        ArrayList<Reading> mathGeneratedReadings = new ArrayList<>();
        for(Reading reading : readings)
            if(!reading.isMathGenerated())
                mathGeneratedReadings.add(reading);
        return mathGeneratedReadings;
    }

    public void deleteReadings(int typeOfReadings) {
        switch (typeOfReadings){
            case ALL_READINGS:
                for(Reading reading : readings)
                    reading.setDeleteDBEntity(true);
                break;
            case MAPPED_READINGS:
                for(Reading reading : readings)
                    if(!reading.isMathGenerated())
                        reading.setDeleteDBEntity(true);
                break;
            case MATH_GENERATED_READINGS:
                for(Reading reading : readings)
                    if(reading.isMathGenerated())
                        reading.setDeleteDBEntity(true);
                break;
        }
    }

    public ArrayList<Reading> getNonDeletedReadings(int readingType) {
        ArrayList<Reading> resultReadings;
        switch (readingType){
            case MATH_GENERATED_READINGS:
                resultReadings = getMathGeneratedReadings();
                break;
            case MAPPED_READINGS:
                resultReadings = getMappedReadings();
                break;
            case ALL_READINGS:
                resultReadings = readings;
                break;
            default:
                resultReadings = readings;
        }
        for(Iterator<Reading> it = resultReadings.iterator(); it.hasNext(); ){
            Reading reading = it.next();
            if(reading.isDeleteDBEntity())
                it.remove();
        }
        return resultReadings;
    }
}
