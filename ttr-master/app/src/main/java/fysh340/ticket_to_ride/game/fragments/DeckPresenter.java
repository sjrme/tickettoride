package fysh340.ticket_to_ride.game.fragments;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fysh340.ticket_to_ride.R;
import fysh340.ticket_to_ride.game.MasterGamePresenter;
import fysh340.ticket_to_ride.game.fragments.gameplaystate.ClientState;
import fysh340.ticket_to_ride.game.fragments.gameplaystate.MyTurnState;
import fysh340.ticket_to_ride.game.fragments.mapsupport.MapRoute;
import interfaces.Observer;
import model.Game;
import model.Route;
import model.TrainCard;

import static model.TrainCard.WILD;

/**
 * <h1>Deck Presenter Fragment</h1>
 * Creates a deckView, which shows face-up and face-down deck cards, as well as the destination
 * card deck and selected route information.
 *
 * @author Nathan Finch
 * @since 7-22-17
 */
public class DeckPresenter extends Fragment implements Observer {
    //Data Members

    //Constant labels for the mFaceDownCards and mDestinationDeck TestViews
    //private static final String TRAIN_DECK = "Train Card Deck\n";
    //private static final String DEST_DECK = "Dest. Cards\n";

    //Model object is referenced when it notifies this object of an update
    private Game mGame = Game.getGameInstance();

    //The interactive parts of the view
    private ImageView[] mFaceUpCards;
    private TextView mSelectedRoute;
    private TextView mFaceDownCards;
    private TextView mDestinationDeck;
    private int FLIP_DURATION = 200;

    //Constructors

    /**
     * <h1>DeckPresenter</h1>
     * Constructs a default Android fragment, which will be used as part of the MasterGamePresenter
     */
    public DeckPresenter() {
        // Required empty public constructor
    }

    /**
     * <h1>update</h1>
     * Updates any one of the face-up cards, train card and destination card decks, as well as
     * the route currently selected, depending on whether the model notifies this object through
     * the update method and shows a changed state
     *
     * @pre none
     * @post Updated model data will be reflected in the deckView
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void update() {
        //Check for updated face-up cards
        if (mGame.iHaveDifferentFaceUpCards()) {
            mGame.iHaveDifferentFaceUpCards(false);
            repopulateFaceUpCards();
        }

        //Check for new selected route
        if (mGame.routeIDHasChanged()) {
            Route currentlySelected = Route.getRouteByID(mGame.getCurrentlySelectedRouteID());
            String routeText = "Claim " + currentlySelected.getStartCity().getPrettyName() + " to "
                    + currentlySelected.getEndCity().getPrettyName() + " Route";
            mSelectedRoute.setText(routeText);
            mSelectedRoute.setEnabled(true);
        }

        //Check if destination cards are being drawn
        if (mGame.iHavePossibleDestCards()) {
            mGame.iHavePossibleDestCards(false);
            ((MasterGamePresenter) getActivity()).switchDeckFragment();
        }

        //Check deck sizes
        String deckDescription;
        if (mGame.iHaveDifferentTrainDeckSize()) {
            mGame.iHaveDifferentTrainDeckSize(false);
            deckDescription = String.valueOf(mGame.getTrainCardDeckSize());
            //if (!mFaceDownCards.getText().equals("0")) //as long as there are cards in the deck, keep flipping
            flipView(mFaceDownCards.getBackground(), mFaceDownCards, 100, deckDescription);
        }
        if (mGame.iHaveDifferentDestDeckSize()) {
            mGame.iHaveDifferentDestDeckSize(false);
            deckDescription = String.valueOf(mGame.getDestCardDeckSize());
            flipView(mDestinationDeck.getBackground(), mDestinationDeck, 0, deckDescription);
        }

    }

    /**
     * <h1>onCreate</h1>
     * Sets up the fragment prior to creating a view
     *
     * @param savedInstanceState Any data to be restored
     * @pre none
     * @post The fragment will be created, ready for a view
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get ready for updates from the model
        mGame.register(this);
    }

    /**
     * <h1>onCreateView</h1>
     * Inflates the fragment_deck layout to create a deckView, which is then populated with
     * TextViews representing the face-up cards, train card and destination card decks, as well as
     * the currently selected route. OnClickListeners are established here for each, with each
     * calling a ServerProxy method, which will ultimately update the Game model object, update
     * this object to make updates to the deckView.
     *
     * @param inflater           The utility to convert the layout to a View
     * @param container          Allows children views to be a part of this one where
     *                           necessary
     * @param savedInstanceState Any data to be restored
     * @return deckView            The view being created here, once it is set up
     * @pre The fragment is created
     * @pre All needed layouts are available
     * @pre repopulateFaceUpCards method sets text and color for face-up cards per model
     * @post deckView will contain accurate data, based on the current model data
     * @post deckView TextViews' clicks will udpate the model through the ServerProxy object
     * or switch to the DestCardPresenter in the case of the mDestinationDeck TextView
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Get the view
        View deckView = inflater.inflate(R.layout.fragment_deck, container, false);

        //Set all the face-up cards
        mFaceUpCards = new ImageView[5];
        mFaceUpCards[0] = (ImageView) deckView.findViewById(R.id.first);
        mFaceUpCards[1] = (ImageView) deckView.findViewById(R.id.second);
        mFaceUpCards[2] = (ImageView) deckView.findViewById(R.id.third);
        mFaceUpCards[3] = (ImageView) deckView.findViewById(R.id.fourth);
        mFaceUpCards[4] = (ImageView) deckView.findViewById(R.id.fifth);

        List<Boolean> initializeFaceUpTrue = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            initializeFaceUpTrue.add(true);
        mGame.setFaceUpDifferences(initializeFaceUpTrue);
        repopulateFaceUpCards();

        //Make the face-up cards clickable
        for (int i = 0; i < mFaceUpCards.length; i++) {
            final int index = i;
            mFaceUpCards[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClientState.INSTANCE.getState().drawTrainCardFaceUp(mGame.getMyself().getMyUsername(),
                            mGame.getMyGameName(), index);
                }
            });
        }

        //Set the face-down card deck and make it clickable
        mFaceDownCards = (TextView) deckView.findViewById(R.id.trainDeck);
        //String deckDescription = TRAIN_DECK + mGame.getTrainCardDeckSize();
        String deckDescription = String.valueOf(mGame.getTrainCardDeckSize());
        mFaceDownCards.setText(deckDescription);

        mFaceDownCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientState.INSTANCE.getState().drawTrainCardFromDeck(mGame.getMyself().getMyUsername(), mGame.getMyGameName());
            }
        });

        //Set the destination card deck and make it clickable
        mDestinationDeck = (TextView) deckView.findViewById(R.id.destinationDeck);
        //deckDescription = DEST_DECK + mGame.getDestCardDeckSize();
        deckDescription = String.valueOf(mGame.getDestCardDeckSize());
        mDestinationDeck.setText(deckDescription);

        mDestinationDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientState.INSTANCE.getState().drawThreeDestCards(mGame.getMyself().getMyUsername(), mGame.getMyGameName());
            }
        });

        //Set the selected route are and make it clickable
        mSelectedRoute = (TextView) deckView.findViewById(R.id.selectedRoute);
        mSelectedRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedRoute.setText("");
                mSelectedRoute.setEnabled(false);

                ClientState.INSTANCE.getState().claimRoute(mGame.getMyself().getMyUsername(), mGame.getMyGameName(),
                                    mGame.getCurrentlySelectedRouteID(), (AppCompatActivity) getActivity());
            }
        });

        return deckView;
    }

    /**
     * <h1>Repopulate Face-up Cards</h1>
     * Sets the TextView text and color to represent the associated face-up card
     *
     * @pre mFaceUpCards[0:5] contains active TextViews
     * @post Each TextView in mFaceUpCards will accurately represent the model's face up cards
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void repopulateFaceUpCards() {
        //Get the new face up train cards
        mGame.iHaveDifferentTrainDeckSize(false);
        List<Integer> cardsByID = mGame.getFaceUpCards();
        List<Boolean> faceUpDifferences = mGame.getFaceUpDifferences();
        int numClickableCards = mFaceUpCards.length;
        int cardFlipDelay = 0;  //delay to flip a face up card. this is only relevant when > 1 face up cards are changing.
        int deckFlipDelay = 100; //delay to flip the train card deck. this is only relevant when > 1 face up cards are changing.
        int NEXT_FLIP_DELAY = 100; //delay between each face up card flip. setting this to 0 will make all face up card flipping immediate
        int numCardsFlipped = 0; //number of times to flip the train card deck.
        int cardIndex;

        for (cardIndex = 0; cardIndex < cardsByID.size(); cardIndex++) {
            boolean cardIsDifferent = faceUpDifferences.get(cardIndex);
            if (!cardIsDifferent)
                continue;

            TrainCard nextCard = TrainCard.getTrainCard(cardsByID.get(cardIndex));
            final Drawable drawable;
            final ImageView imageView = mFaceUpCards[cardIndex]; //get the original card image ready to flip

            //get the drawable depending on the type of train card it is
            switch (nextCard) {
                case RED:
                    drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.red, null);
                    break;
                case BLUE:
                    drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.blue, null);
                    break;
                case GREEN:
                    drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.green, null);
                    break;
                case YELLOW:
                    drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.yellow, null);
                    break;
                case BLACK:
                    drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.black, null);
                    break;
                case PURPLE:
                    drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.purple, null);
                    break;
                case ORANGE:
                    drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.orange, null);
                    break;
                case WHITE:
                    drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.white, null);
                    break;
                case WILD:
                    drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.wild, null);
                    break;
                default:
                    drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.tickettoride, null);
                    break;
            }

            flipView(drawable, imageView, cardFlipDelay, null); //flip the changed face up card
            cardFlipDelay += NEXT_FLIP_DELAY; //delay the next face up card flip
            numCardsFlipped++;  //a face up card has flipped, so flip the deck one more time

            mFaceUpCards[cardIndex].setEnabled(true);
        }

        //if the number of the face up cards has been reduced (we have < 5 train cards face up), set it to default image and remove clicks on it
        int cardTemp = cardIndex;
        while (cardTemp < numClickableCards) {
            final Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.tickettoride, null);
            final ImageView imageView = mFaceUpCards[cardTemp];

            flipView(drawable, imageView, cardFlipDelay, null);
            mFaceUpCards[cardTemp].setEnabled(false);
            cardTemp++;
        }

        boolean temporarilyStopFaceUpClicks = false;
        if (numCardsFlipped > 1) {
            temporarilyStopFaceUpClicks = true;
        }

        //for each face up card that was flipped, flip the deck once and decrement the number on it
        while (numCardsFlipped-- > 0 && mFaceDownCards != null){
            if (mGame.getTrainCardDeckSize() == 0)
                break;

            mFaceUpCards[cardIndex - numCardsFlipped - 1].setEnabled(true);
            String flipCounter = String.valueOf(mGame.getTrainCardDeckSize() + numCardsFlipped);

            flipView(mFaceDownCards.getBackground(), mFaceDownCards, deckFlipDelay, flipCounter);
            deckFlipDelay += FLIP_DURATION;
        }

        /*
        If flipping more than one card, temporarily disable clicks on face up cards. Re enable after the
        last deck flip animation to preserve the deck size correctly.
         */
        if (temporarilyStopFaceUpClicks){
            for (ImageView imageView : mFaceUpCards){
                imageView.setEnabled(false);
            }
            Handler waitForAnimationEnd = new Handler();
            final int cardsToEnable = cardIndex;
            final Runnable enableTrainCardClicks = new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < cardsToEnable; i++)
                        mFaceUpCards[i].setEnabled(true);
                }
            };
            waitForAnimationEnd.postDelayed(enableTrainCardClicks, deckFlipDelay - FLIP_DURATION);
        }
    }

    //flip a TextView or ImageView 180 degrees on the y axis while changing it's text or image respectively
    private void flipView(final Drawable drawable, final View view, int flipDelay, final String newText){
        //flip from 0 to 90 first
        final ObjectAnimator animStage1 = (ObjectAnimator) AnimatorInflater.loadAnimator(getActivity(), R.animator.flip_stage_1);
        //then flip from -90 to 0
        final ObjectAnimator animStage2 = (ObjectAnimator) AnimatorInflater.loadAnimator(getActivity(), R.animator.flip_stage_2);
        animStage1.setTarget(view);
        animStage2.setTarget(view);
        animStage1.setDuration(FLIP_DURATION / 2);
        animStage2.setDuration(FLIP_DURATION / 2);
        animStage1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /*
                After completing part one of the animation (it is now at 90 degrees and is completely invisible),
                change the image or the text and then rotate back into visibility.
                 */
                if (view instanceof ImageView)
                    ((ImageView) view).setImageDrawable(drawable);
                if (view instanceof TextView && newText != null)
                    ((TextView) view).setText(String.valueOf(newText));
                animStage2.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        if (drawable != null) { //if the new drawable is null, do not perform this animation (used when first starting the activity)
            animStage1.setStartDelay(flipDelay);
            animStage1.start();
        }
    }
}