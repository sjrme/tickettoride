package model;

import java.util.HashSet;
import java.util.Set;

import interfaces.Observable;
import interfaces.Observer;

/**
 *
 * @author Shun Sambongi
 * @version 1.0
 * @since 7/25/17
 */
public class ServerError implements Observable {

    private String mMessage;
    private Set<Observer> mObservers = new HashSet<>();
    private boolean exists=false;

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getMessage() {
        exists=false;
        return mMessage;
    }

    public void setMessage(String message) {
        exists=true;
        mMessage = message;
        notifyObserver();
    }

    @Override
    public void register(Observer o) {
        mObservers.add(o);
    }

    @Override
    public void unregister(Observer o) {
        mObservers.remove(o);
    }

    @Override
    public void notifyObserver() {
        for (Observer observer : mObservers) {
            observer.update();
        }
    }
}
