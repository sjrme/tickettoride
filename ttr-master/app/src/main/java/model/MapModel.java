package model;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

import interfaces.Observable;
import interfaces.Observer;

/**
 * Created by Rachael on 7/25/2017.
 */

public class MapModel implements Observable {
    private static MapModel myMap;
    private MapModel(){}
    public static MapModel getMapInstance(){
        if (myMap == null)
            myMap = new MapModel();
        return myMap;
    }
    int color;
    int lastRoute;
    public void claimRoute(int color,int lastRoute)
    {
        this.color=color;
        this.lastRoute=lastRoute;
        notifyObserver();
    }
    public int getColor() {
        return color;
    }

    public int getLastRoute() {
        return lastRoute;
    }


    private ArrayList<Observer> observers = new ArrayList<>();
    @Override
    public void register(Observer o) {
        observers.add(o);
    }

    @Override
    public void unregister(Observer deleteObserver) {
        int observerIndex = observers.indexOf(deleteObserver);
        if(observerIndex>=0) {
            observers.remove(observerIndex);
        }
    }

    @Override
    public void notifyObserver() {
        Handler uiHandler = new Handler(Looper.getMainLooper()); //gets the UI thread
        Runnable runnable = new Runnable() { //
            @Override
            public void run() {
                for (int i = 0; i < observers.size(); i++){
                    observers.get(i).update();
                }
            }
        };
        uiHandler.post(runnable); //do the run() method in previously declared runnable on UI thread
    }
}
