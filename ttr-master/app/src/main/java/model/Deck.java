package model;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

import interfaces.Observable;
import interfaces.Observer;

/**
 *  <h1>DeckPresenter Model</h1>
 *  Models the details that are presented as available (face up) deck cards, as well as
 *  destination cards available when the player chooses to draw.
 *
 *  @author     Nathan Finch
 *  @since      7-22-17
 */
public class Deck implements Observable{
    private static class LazyDeckHelper {
        private static final Deck DECK_INSTANCE = new Deck();
    }
    
    public static Deck getInstance() {
        return LazyDeckHelper.DECK_INSTANCE;
    }
    
    private List<Integer> mFaceUpCards;
    private List<Boolean> mFaceUpDifferences;
    private List<Integer> mDestinationCards;
    
    private List<Observer> mObservers;
    
    private boolean iHaveDifferentTrainDeckSize;
    private boolean newTrainCardDeckSize;
    
    private int trainCardDeckSize;
    private int destinationCardDeckSize;
    
    private Deck() {
        mFaceUpCards = new ArrayList<>();
        mFaceUpDifferences = new ArrayList<>();
        mDestinationCards = new ArrayList<>();
        mObservers = new ArrayList<>();
        
        iHaveDifferentTrainDeckSize = false;
        newTrainCardDeckSize = false;
        
        trainCardDeckSize = 110;
        destinationCardDeckSize = 30;
    }
    
    public void setAvailableDestCards(List<Integer> newCards) {
        mDestinationCards = newCards;
    }
    
    public void setAvailableFaceUpCards(List<Integer> newCards) {
        mFaceUpCards = newCards;
    }
    
    public List<Integer> getFaceUpCards() {
        return mFaceUpCards;
    }

    public void setFaceUpDifferences(List<Boolean> faceUpDifferences){
        mFaceUpDifferences = faceUpDifferences;
    }
    public List<Boolean> getFaceUpDifferences() {
        return mFaceUpDifferences;
    }
    
    public List<Integer> getDestinationCards() {
        return mDestinationCards;
    }
    
    @Override
    public void register(Observer o) {
        mObservers.add(o);
    }
    
    @Override
    public void unregister(Observer o) {
        int observerIndex = mObservers.indexOf(o);
        if(observerIndex >= 0) {
            mObservers.remove(observerIndex);
        }
    }
    
    @Override
    public void notifyObserver() {
        Handler uiHandler = new Handler(Looper.getMainLooper());
        
        Runnable runnable = new Runnable() { //
            @Override
            public void run() {
                for (int i = 0; i < mObservers.size(); i++){
                    mObservers.get(i).update();
                }
            }
        };
        
        uiHandler.post(runnable);
    }
    
    public boolean iHaveDifferentTrainDeckSize() {
        return iHaveDifferentTrainDeckSize;
    }
    public void iHaveDifferentTrainDeckSize(boolean hasChanged) {
        iHaveDifferentTrainDeckSize = hasChanged;
    }
    
    public void setTrainCardDeckSize(int newSize) {
        trainCardDeckSize = newSize;
        if (trainCardDeckSize < 0){
            trainCardDeckSize = 0;
        }
    }
    public int getTrainCardDeckSize() {
        return trainCardDeckSize;
    }
    
    //Begin trainCardDeckSize flag
    public boolean trainCardDeckSizeHasChanged() {
        return newTrainCardDeckSize;
    }
    public void trainCardDeckSizeHasChanged(boolean hasChanged) {
        newTrainCardDeckSize = hasChanged;
    }
    //end trainCardDeckSize flags
    
    //Begin destinationCardDeckSize flag
    private boolean newDestinationCardDeckSize = false;
    public boolean destinationCardDeckSizeHasChanged() {
        return newDestinationCardDeckSize;
    }
    public void destinationCardDeckSizeHasChanged(boolean hasChanged) {
        newDestinationCardDeckSize = hasChanged;
    }
    //end destinationCardDeckSize flags
    
    public void setDestinationCardDeckSize(int newSize) {
        destinationCardDeckSize = newSize;
        if (destinationCardDeckSize < 0){
            destinationCardDeckSize = 0;
        }
    }
    public int getDestinationCardDeckSize() {
        return destinationCardDeckSize;
    }
    
}
