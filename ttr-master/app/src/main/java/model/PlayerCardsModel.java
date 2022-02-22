package model;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

import interfaces.Observable;
import interfaces.Observer;

/**
 * Created by Rachael on 7/22/2017.
 */

public class PlayerCardsModel implements Observable {
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
