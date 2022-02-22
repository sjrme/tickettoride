package model;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import results.game.EndGameResult;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;

/**
 * Created by sjrme on 7/26/17.
 */
public class StartedGameTest {
    HashMap<Integer, Route> routeMap = new HashMap<>();
    private int playerOneRoute = 0;
    private int playerTwoRoute = 1;
    private int playerThreeRoute =2;
    private int playerFourRoute = 3;

    private int playerOnePoints = 0;
    private int playerTwoPoints = 0;
    private int playerThreePoints = 0;
    private int playerFourPoints = 0;

    private int numOfPassesTest1 = 0;
    private static StartedGame startedGame;
    @BeforeClass
    public static void setUpGame() {
        UnstartedGame unstartedGame = new UnstartedGame();

        unstartedGame.addPlayer("Player1");
        unstartedGame.addPlayer("Player2");
        unstartedGame.addPlayer("Player3");
        unstartedGame.addPlayer("Player4");

        startedGame = new StartedGame(unstartedGame);
        startedGame.preGameSetup(unstartedGame.getUsernamesInGame());
    }

    /**
     * Calculates the first time a player cannot claim a route because they do not have enough cars.
     */
    @Test
    public void calculateFirstFailure() {
        boolean end = false;

        int totalClaimed1 = 45;
        int totalClaimed2 = 45;
        int totalClaimed3 = 45;
        int totalClaimed4 = 45;
        int numOfRoundsFinished = 0;
        String playerFailure = null;
        while (!end) {
            if (Route.getRouteByID(playerOneRoute).getLength() < totalClaimed1) {
                totalClaimed1 -= Route.getRouteByID(playerOneRoute).getLength();
            } else {
                playerFailure = "Player1";
                break;
            }
            if (Route.getRouteByID(playerTwoRoute).getLength() < totalClaimed2) {
                totalClaimed2 -= Route.getRouteByID(playerTwoRoute).getLength();
            } else {
                playerFailure = "Player2";
                break;
            }
            if (Route.getRouteByID(playerThreeRoute).getLength() < totalClaimed3) {
                totalClaimed3 -= Route.getRouteByID(playerThreeRoute).getLength();
            }else {
                playerFailure = "Player3";
                break;
            }
            if (Route.getRouteByID(playerFourRoute).getLength() < totalClaimed4) {
                totalClaimed4 -= Route.getRouteByID(playerFourRoute).getLength();
            }else {
                playerFailure = "Player4";
                break;
            }
            playerOneRoute += 4;
            playerTwoRoute += 4;
            playerThreeRoute += 4;
            playerFourRoute += 4;
            numOfRoundsFinished++;
        }
        System.out.println(numOfRoundsFinished + " " + playerFailure);
        System.out.println("Player 1 cars:  " + totalClaimed1);
        System.out.println("Player 2 cars:  " + totalClaimed2);
        System.out.println("Player 3 cars:  " + totalClaimed3);
        System.out.println("Player 4 cars:  " + totalClaimed4);
        System.out.println("Player 4 route:  " + playerFourRoute);
        assertFalse(end);
    }

    @Test
    public void claimRouteTest() {
        try {
            startedGame.returnDestCard("Player1", 30);
            startedGame.returnDestCard("Player2", 30);
            startedGame.returnDestCard("Player3", 30);
            startedGame.returnDestCard("Player4", 30);

            while(startedGame.getEndGameResult() == null) {
                roundOfRoutes();
            }

            EndGameResult result = (EndGameResult)startedGame.getEndGameResult();
            //Right points
            assertEquals(49, (int)result.getPointsFromRoutes().get(0));
            assertEquals(42, (int) result.getPointsFromRoutes().get(1));
            assertEquals(47, (int) result.getPointsFromRoutes().get(2));
            assertEquals(81, (int) result.getPointsFromRoutes().get(3));

            //Right ContRoutes
            printEndResults(result);

        } catch(GamePlayException ge) {
            System.out.println(ge.getMessage());
        }

    }

    //THe first time a player cannot claim a route, because they do not have enough cars, is actually the
    //final turn of the game, as calculated above. At this time, player 4 has 2 cars, so he claims a
    //route 99, which has a size of 1.
    private void roundOfRoutes() {
        try {
            //Test is calculated for 11 passes; no one claims routes the final round.
            if(numOfPassesTest1 == 11) {
                return;
            }
            startedGame.claimRoute("Player1", playerOneRoute, new ArrayList<>(trainCards(playerOneRoute)));
            playerOnePoints += Route.getRouteByID(playerOneRoute).getPointValue();

            startedGame.claimRoute("Player2", playerTwoRoute, new ArrayList<>(trainCards(playerTwoRoute)));
            playerTwoPoints += Route.getRouteByID(playerTwoRoute).getPointValue();

            startedGame.claimRoute("Player3", playerThreeRoute, new ArrayList<>(trainCards(playerThreeRoute)));
            playerThreePoints += Route.getRouteByID(playerThreeRoute).getPointValue();

            startedGame.claimRoute("Player4", playerFourRoute, new ArrayList<>(trainCards(playerFourRoute)));
            playerFourPoints += Route.getRouteByID(playerFourRoute).getPointValue();

            playerOneRoute += 4;
            playerTwoRoute += 4;
            playerThreeRoute += 4;
            playerFourRoute += 4;
            numOfPassesTest1++;
        } catch(GamePlayException ge) {
            System.out.println(ge.getMessage());
        }
    }

    private void printEndResults(EndGameResult result) {
        List<DestCard> destCards1 = startedGame.getAllPlayers().get("Player1").getDestCards();
        List<DestCard> destCards2 = startedGame.getAllPlayers().get("Player2").getDestCards();
        List<DestCard> destCards3 = startedGame.getAllPlayers().get("Player3").getDestCards();
        List<DestCard> destCards4 = startedGame.getAllPlayers().get("Player4").getDestCards();

        List<Integer> pointsFromRoutes = result.getPointsFromRoutes();
        List<Integer> pointsFromDestCards = result.getDestCardPtsAdded();
        List<Integer> pointsSubtFromDestCards = result.getDestCardPtsSubtracted();
        List<String>  longestRouteHolder = result.getOwnsLongestRoute();
        List<Integer> totalPoints = result.getTotalPoints();

        List<Player.ContinuousRoute> playerOneRoutes = startedGame.getAllPlayers().get("Player1").getAllContRoutes();
        List<Player.ContinuousRoute> playerTwoRoutes = startedGame.getAllPlayers().get("Player2").getAllContRoutes();
        List<Player.ContinuousRoute> playerThreeRoutes = startedGame.getAllPlayers().get("Player3").getAllContRoutes();
        List<Player.ContinuousRoute> playerFourRoutes = startedGame.getAllPlayers().get("Player4").getAllContRoutes();

        assertEquals(6, playerOneRoutes.size());
        assertEquals(5, playerTwoRoutes.size());
        assertEquals(7, playerThreeRoutes.size());
        assertEquals(6, playerFourRoutes.size());

        System.out.println("\nPlayer 1 has: ");
        for (int a  = 0; a < destCards1.size(); a++) {
            DestCard destCard = destCards1.get(a);
            System.out.println("  " + destCard.toString() + ", points: " + destCard.getPointValue());
        }
        for (int a = 0; a < playerOneRoutes.size(); a++) {
            for (City city : playerOneRoutes.get(a).cities) {
                System.out.print(city.getPrettyName() + ", ");
            }
            System.out.println("Size is: "  + playerOneRoutes.get(a).size);
        }
        System.out.println("Points from routes: "  + pointsFromRoutes.get(0));
        System.out.println("Points added: "  + pointsFromDestCards.get(0));
        System.out.println("Points subtracted: "  + pointsSubtFromDestCards.get(0));
        if(longestRouteHolder.contains("Player1")){
            System.out.println("Largest cont route? "
                    + startedGame.getAllPlayers().get("Player1").getLargestContRouteSize() + ", Yes.");
        }else {
            System.out.println("Largest cont route? " +
                    + startedGame.getAllPlayers().get("Player1").getLargestContRouteSize() + ", No.");
        }
        System.out.println("Total Points: " + totalPoints.get(0));

        System.out.println("\nPlayer 2 has: ");
        for (int a  = 0; a < destCards2.size(); a++) {
            DestCard destCard = destCards2.get(a);
            System.out.println("  " + destCard.toString() + ", points: " + destCard.getPointValue());
        }
        for (int a = 0; a < playerTwoRoutes.size(); a++) {
            for (City city : playerTwoRoutes.get(a).cities) {
                System.out.print(city.getPrettyName() + ", ");
            }
            System.out.println("Size is: "  + playerTwoRoutes.get(a).size);
        }
        System.out.println("Points from routes: "  + pointsFromRoutes.get(1));
        System.out.println("Points added: "  + pointsFromDestCards.get(1));
        System.out.println("Points subtracted: "  + pointsSubtFromDestCards.get(1));
        if(longestRouteHolder.contains("Player2")){
            System.out.println("Largest cont route? "
                    + startedGame.getAllPlayers().get("Player2").getLargestContRouteSize() + ", Yes.");
        }else {
            System.out.println("Largest cont route? " +
                    + startedGame.getAllPlayers().get("Player2").getLargestContRouteSize() + ", No.");
        }
        System.out.println("Total Points: " + totalPoints.get(1));

        System.out.println("\nPlayer 3 has: ");
        for (int a  = 0; a < destCards3.size(); a++) {
            DestCard destCard = destCards3.get(a);
            System.out.println("  " + destCard.toString() + ", points: " + destCard.getPointValue());
        }
        for (int a = 0; a < playerThreeRoutes.size(); a++) {
            for (City city : playerThreeRoutes.get(a).cities) {
                System.out.print(city.getPrettyName() + ", ");
            }
            System.out.println("Size is: "  + playerThreeRoutes.get(a).size);
        }
        System.out.println("Points from routes: "  + pointsFromRoutes.get(2));
        System.out.println("Points added: "  + pointsFromDestCards.get(2));
        System.out.println("Points subtracted: "  + pointsSubtFromDestCards.get(2));
        if(longestRouteHolder.contains("Player3")){
            System.out.println("Largest cont route? "
                    + startedGame.getAllPlayers().get("Player3").getLargestContRouteSize() + ", Yes.");
        }else {
            System.out.println("Largest cont route? " +
                    + startedGame.getAllPlayers().get("Player3").getLargestContRouteSize() + ", No.");
        }
        System.out.println("Total Points: " + totalPoints.get(2));

        System.out.println("\nPlayer 4 has: ");
        for (int a  = 0; a < destCards4.size(); a++) {
            DestCard destCard = destCards4.get(a);
            System.out.println("  " + destCard.toString() + ", points: " + destCard.getPointValue());
        }
        for (int a = 0; a < playerFourRoutes.size(); a++) {
            for (City city : playerFourRoutes.get(a).cities) {
                System.out.print(city.getPrettyName() + ", ");
            }
            System.out.println("Size is: "  + playerFourRoutes.get(a).size);
        }
        System.out.println("Points from routes: "  + pointsFromRoutes.get(3));
        System.out.println("Points added: "  + pointsFromDestCards.get(3));
        System.out.println("Points subtracted: "  + pointsSubtFromDestCards.get(3));
        if(longestRouteHolder.contains("Player4")){
            System.out.println("Largest cont route? "
                    + startedGame.getAllPlayers().get("Player4").getLargestContRouteSize() + ", Yes.");
        }else {
            System.out.println("Largest cont route? " +
                    + startedGame.getAllPlayers().get("Player4").getLargestContRouteSize() + ", No.");
        }
        System.out.println("Total Points: " + totalPoints.get(3));
    }

    private List<Integer> trainCards(int routeId) {
        int routeSize = Route.getRouteByID(routeId).getLength();
        List<Integer> allCards = new ArrayList<>();
        for (int a = 0; a < routeSize; a++) {
            allCards.add(8);
        }
        return allCards;
    }


    //Test is set for TWENTY-FOUR TRAIN CARS PER PERSON.
    @Test
    public void destinationCardTest() {

        StartedGame startedGame = destCardTestSetup();
        startedGame.getAllPlayers().get("Player1").customNumOfCars(24);
        startedGame.getAllPlayers().get("Player2").customNumOfCars(24);

        try{
            startedGame.returnDestCard("Player1", 30);
            startedGame.returnDestCard("Player2", 30);

            for (int a = 0; a < 4; a++) {
                startedGame.drawThreeDestCards("Player1");
                startedGame.returnDestCard("Player1", 30);
                startedGame.drawThreeDestCards("Player2");
                startedGame.returnDestCard("Player2", 30);
            }

            startedGame.claimRoute("Player1", 79, new ArrayList<>(Arrays.asList(8, 8)));
            startedGame.claimRoute("Player2", 11, new ArrayList<Integer>(Arrays.asList(8,8,8,8)));

            startedGame.claimRoute("Player1", 92, new ArrayList<>(Arrays.asList(8, 8)));
            startedGame.claimRoute("Player2", 51, new ArrayList<Integer>(Arrays.asList(8,8,8)));

            startedGame.claimRoute("Player1", 5, new ArrayList<>(Arrays.asList(8, 8)));
            startedGame.claimRoute("Player2", 36, new ArrayList<Integer>(Arrays.asList(8,8,8)));

            startedGame.claimRoute("Player1", 9, new ArrayList<>(Arrays.asList(8, 8)));
            startedGame.claimRoute("Player2", 35, new ArrayList<Integer>(Arrays.asList(8,8,8,8,8)));

            startedGame.claimRoute("Player1", 7, new ArrayList<>(Arrays.asList(8, 8)));
            startedGame.claimRoute("Player2", 12, new ArrayList<Integer>(Arrays.asList(8,8,8,8)));

            startedGame.claimRoute("Player1", 33, new ArrayList<>(Arrays.asList(8, 8, 8, 8)));
            startedGame.claimRoute("Player2", 13, new ArrayList<Integer>(Arrays.asList(8,8,8)));

            startedGame.claimRoute("Player1", 47, new ArrayList<>(Arrays.asList(8, 8,8,8,8)));
            startedGame.claimRoute("Player2", 99, new ArrayList<Integer>(Arrays.asList(8)));

            EndGameResult result = (EndGameResult)startedGame.getEndGameResult();
            assertEquals(27, (int)result.getPointsFromRoutes().get(0));
            assertEquals(37, (int)result.getPointsFromRoutes().get(1));
            printSecondResults(result, startedGame);
        } catch (GamePlayException ge) {
            System.out.println(ge.getMessage());
        }
    }

    private void printSecondResults(EndGameResult result, StartedGame startedGame) {

        List<DestCard> destCards1 = startedGame.getAllPlayers().get("Player1").getDestCards();
        List<DestCard> destCards2 = startedGame.getAllPlayers().get("Player2").getDestCards();
        List<Player.ContinuousRoute> playerOneRoutes = startedGame.getAllPlayers().get("Player1").getAllContRoutes();
        List<Player.ContinuousRoute> playerTwoRoutes = startedGame.getAllPlayers().get("Player2").getAllContRoutes();

        List<Integer> pointsFromRoutes = result.getPointsFromRoutes();
        List<Integer> pointsFromDestCards = result.getDestCardPtsAdded();
        List<Integer> pointsSubtFromDestCards = result.getDestCardPtsSubtracted();
        List<String>  longestRouteHolder = result.getOwnsLongestRoute();
        List<Integer> totalPoints = result.getTotalPoints();

        System.out.println("\nPlayer 1 has: ");
        for (int a  = 0; a < destCards1.size(); a++) {
            DestCard destCard = destCards1.get(a);
            System.out.println("  " + destCard.toString() + ", points: " + destCard.getPointValue());
        }
        for (int a = 0; a < playerOneRoutes.size(); a++) {
            for (City city : playerOneRoutes.get(a).cities) {
                System.out.print(city.getPrettyName() + ", ");
            }
            System.out.println("Size is: "  + playerOneRoutes.get(a).size);
        }
        System.out.println("Points from routes: "  + pointsFromRoutes.get(0));
        System.out.println("Points added: "  + pointsFromDestCards.get(0));
        System.out.println("Points subtracted: "  + pointsSubtFromDestCards.get(0));
        if(longestRouteHolder.contains("Player1")){
            System.out.println("Largest cont route? "
                    + startedGame.getAllPlayers().get("Player1").getLargestContRouteSize() + ", Yes.");
        }else {
            System.out.println("Largest cont route? " +
                    + startedGame.getAllPlayers().get("Player1").getLargestContRouteSize() + ", No.");
        }
        System.out.println("Total Points: " + totalPoints.get(0));

        System.out.println("\nPlayer 2 has: ");
        for (int a  = 0; a < destCards2.size(); a++) {
            DestCard destCard = destCards2.get(a);
            System.out.println("  " + destCard.toString() + ", points: " + destCard.getPointValue());
        }
        for (int a = 0; a < playerTwoRoutes.size(); a++) {
            for (City city : playerTwoRoutes.get(a).cities) {
                System.out.print(city.getPrettyName() + ", ");
            }
            System.out.println("Size is: "  + playerTwoRoutes.get(a).size);
        }
        System.out.println("Points from routes: "  + pointsFromRoutes.get(1));
        System.out.println("Points added: "  + pointsFromDestCards.get(1));
        System.out.println("Points subtracted: "  + pointsSubtFromDestCards.get(1));
        if(longestRouteHolder.contains("Player2")){
            System.out.println("Largest cont route? "
                    + startedGame.getAllPlayers().get("Player2").getLargestContRouteSize() + ", Yes.");
        }else {
            System.out.println("Largest cont route? " +
                    + startedGame.getAllPlayers().get("Player2").getLargestContRouteSize() + ", No.");
        }
        System.out.println("Total Points: " + totalPoints.get(1));
    }

    private StartedGame destCardTestSetup() {
        UnstartedGame unstartedGame = new UnstartedGame("gameName", 2);
        unstartedGame.addPlayer("Player1");
        unstartedGame.addPlayer("Player2");
        StartedGame startedGame = new StartedGame(unstartedGame);
        startedGame.preGameSetup(unstartedGame.getUsernamesInGame());
        return startedGame;
    }



}
