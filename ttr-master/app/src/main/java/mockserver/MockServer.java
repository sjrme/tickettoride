package mockserver;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import clientfacade.ClientFacade;
import commands.Command;
import commands.game.ChatCommand;
import commands.game.ClaimRouteCommand;
import commands.game.DrawThreeDestCardsCommand;
import commands.game.DrawTrainCardFromDeckCommand;
import commands.game.DrawTrainCardFromFaceUpCommand;
import commands.game.ReturnDestCardsCommand;
import commands.game.ReturnFirstDestCardCommand;
import commands.game.StartGameCommand;
import commands.menu.CreateGameCommand;
import commands.menu.JoinGameCommand;
import commands.menu.LeaveGameCommand;
import commands.menu.LoginCommand;
import commands.menu.RegisterCommand;
import model.Game;
import model.Route;
import model.RunningGame;
import model.TrainCard;
import model.UnstartedGame;
import utils.Utils;

public class MockServer {
    private static List<UnstartedGame> unstartedGameList = new ArrayList<>();
    private static List<RunningGame> runningGameList = new ArrayList<>();
    private static Map<String, String> loginMap = new HashMap<>();
    private static Game game = Game.getGameInstance();

    static{
        loginMap.put("username", "password");
        UnstartedGame mockGame = new UnstartedGame("mockGame", 2);
        mockGame.addPlayer("Timmy");
        unstartedGameList.add(mockGame);
    }

    public void doCommand(Command command) {
        ClientFacade clientFacade = new ClientFacade();
        switch (command.getType()) {
            case Utils.LOGIN_TYPE: {
                LoginCommand myCommand = (LoginCommand) command;
                if (loginMap.containsKey(myCommand.getUsername())) {
                    if (myCommand.getPassword().equals(loginMap.get(myCommand.getUsername()))) {
                        clientFacade.loginUser(myCommand.getUsername(), myCommand.getPassword(), null);
                    }
                } else
                    clientFacade.postMessage("fake login rejected");
                break;
            }
            case Utils.REGISTER_TYPE: {
                RegisterCommand myCommand = (RegisterCommand) command;
                if (!loginMap.containsKey(myCommand.getUsername())) {
                    loginMap.put(myCommand.getUsername(), myCommand.getPassword());
                    clientFacade.registerUser(command.getUsername(), "password", "fake register successful", null);
                }
                else
                    clientFacade.postMessage("fake register already exists");
                break;
            }
            case Utils.POLL_TYPE: {
                clientFacade.updateSingleUserGameList(command.getUsername(), unstartedGameList, runningGameList);
                break;
            }
            case Utils.CREATE_TYPE: {
                CreateGameCommand mycommand = (CreateGameCommand) command;
                UnstartedGame mygame = new UnstartedGame(mycommand.getGameName(), 2);
                mygame.addPlayer("Timmy");
                for (int i = 0; i < unstartedGameList.size(); i++) {
                    if (unstartedGameList.get(i).getGameName().equals(mycommand.getGameName())) {
                        clientFacade.postMessage("game already exists");
                        return;
                    }
                }
                for (int i = 0; i < runningGameList.size(); i++) {
                    if (runningGameList.get(i).getGameName().equals(mycommand.getGameName())) {
                        clientFacade.postMessage("game already exists");
                        return;
                    }
                }
                unstartedGameList.add(mygame);
                clientFacade.updateSingleUserGameList(command.getUsername(), unstartedGameList, runningGameList);
                break;
            }
            case Utils.JOIN_TYPE: {
                JoinGameCommand mycommand = (JoinGameCommand)command;
                for (int i = 0; i < unstartedGameList.size(); i++){
                    if (mycommand.getGameName().equals(unstartedGameList.get(i).getGameName())){
                        unstartedGameList.get(i).addPlayer(mycommand.getUsername());
                        clientFacade.joinGame(mycommand.getUsername(), unstartedGameList.get(i).getGameName());
                        clientFacade.updateSingleUserGameList(command.getUsername(), unstartedGameList, runningGameList);
                        return;
                    }
                }
                clientFacade.postMessage("could not join game");
                break;
            }
            case Utils.LEAVE_TYPE: {
                LeaveGameCommand mycommand = (LeaveGameCommand) command;
                for (int i = 0; i < unstartedGameList.size(); i++) {
                    if (mycommand.getGameName().equals(unstartedGameList.get(i).getGameName())) {
                        clientFacade.leaveGame(mycommand.getUsername(), unstartedGameList.get(i).getGameName());
                        unstartedGameList.get(i).removePlayer(mycommand.getUsername());
                        unstartedGameList.remove(i);
                        clientFacade.updateSingleUserGameList(command.getUsername(), unstartedGameList, runningGameList);
                        return;
                    }
                }
                clientFacade.postMessage("could not leave game");
                break;
            }
            case Utils.START_TYPE: {
                StartGameCommand mycommand = (StartGameCommand) command;
                for (int i = 0; i < unstartedGameList.size(); i++) {
                    if (mycommand.getGameName().equals(unstartedGameList.get(i).getGameName())) {
                        UnstartedGame myGame = unstartedGameList.get(0);
                        List<String> playerNames = new ArrayList<>();
                        for (String username : myGame.getUsernamesInGame()){
                            playerNames.add(username);
                        }
                        List<Integer> destCards = new ArrayList<>();
                        Random rand = new Random();
                        int c1 = rand.nextInt(29) + 1;
                        int c2 = rand.nextInt(29) + 1;
                        int c3 = rand.nextInt(29) + 1;
                        destCards.add(c1);
                        destCards.add(c2);
                        destCards.add(c3);
                        List<Integer> trainCards = new ArrayList<>();
                        int tc1 = rand.nextInt(9);
                        int tc2 = rand.nextInt(9);
                        int tc3 = rand.nextInt(9);
                        trainCards.add(tc1);
                        trainCards.add(tc2);
                        trainCards.add(tc3);
                        List<Integer> faceUpCards = new ArrayList<>();
                        int fc1 = rand.nextInt(9);
                        int fc2 = rand.nextInt(9);
                        int fc3 = rand.nextInt(9);
                        int fc4 = rand.nextInt(9);
                        int fc5 = rand.nextInt(9);
                        faceUpCards.add(fc1);
                        faceUpCards.add(fc2);
                        faceUpCards.add(fc3);
                        faceUpCards.add(fc4);
                        faceUpCards.add(fc4);
                        faceUpCards.add(fc5);
                        clientFacade.startGame(mycommand.getUsername(), mycommand.getGameName(), playerNames, destCards, trainCards, faceUpCards);
                    }
                }
                break;
            }
            case Utils.REJOIN_TYPE:
                break;
            case Utils.MESSAGE_TYPE: {
                ChatCommand mycommand = (ChatCommand)command;
                clientFacade.addChat(mycommand.getUsername(), mycommand.getMessage());
                break;
            }
            case Utils.DRAW_DEST_CARDS_TYPE: {
                DrawThreeDestCardsCommand mycommand = (DrawThreeDestCardsCommand) command;
                List<Integer> destCards = new ArrayList<>();
                Random rand = new Random();
                int c1 = rand.nextInt(29) + 1;
                int c2 = rand.nextInt(29) + 1;
                int c3 = rand.nextInt(29) + 1;
                destCards.add(c1);
                destCards.add(c2);
                destCards.add(c3);
                clientFacade.drawDestCards(mycommand.getUsername(), destCards);
                break;
            }
            case Utils.RETURN_FIRST_DEST_CARD_TYPE: {
                ReturnFirstDestCardCommand mycommand = (ReturnFirstDestCardCommand)command;
                clientFacade.returnFirstDestCards(mycommand.getUsername(), mycommand.getDestCard());
                break;
            }
            case Utils.RETURN_DEST_CARDS_TYPE: {
                ReturnDestCardsCommand mycommand = (ReturnDestCardsCommand)command;
                clientFacade.returnDestCards(mycommand.getUsername(), mycommand.getDestCard());
                break;
            }
            case Utils.DRAW_TRAIN_CARD_DECK_TYPE: {
                DrawTrainCardFromDeckCommand mycommand = (DrawTrainCardFromDeckCommand)command;
                Random rand = new Random();
                int card = rand.nextInt(9);
                clientFacade.drawTrainCardDeck(mycommand.getUsername(), card);
                break;
            }
            case Utils.DRAW_TRAIN_CARD_FACEUP_TYPE: {
                DrawTrainCardFromFaceUpCommand mycommand = (DrawTrainCardFromFaceUpCommand)command;
                List<Integer> faceUpCards = game.getFaceUpCards();
                TrainCard myTrainCard = TrainCard.getTrainCard(faceUpCards.get(mycommand.getIndex()));
                clientFacade.drawTrainCardFaceUp(mycommand.getUsername(), TrainCard.getTrainCardKey(myTrainCard));
                Random rand = new Random();
                int card = rand.nextInt(9);
                faceUpCards.set(mycommand.getIndex(), card);
                List<Boolean> faceUpDifferences = new ArrayList<>();
                for (int i = 0; i < 5; i++)
                    faceUpDifferences.add(true);
                clientFacade.replaceFaceUpCards(faceUpCards, faceUpDifferences);
                break;
            }
            case Utils.CLAIM_ROUTE_TYPE: {
                ClaimRouteCommand mycommand = (ClaimRouteCommand)command;
                Route myRoute = Route.getRouteByID(mycommand.getRouteID());
                TrainCard myTrainCardType = myRoute.getOriginalColor();
                int routeSize = myRoute.getLength();

                    clientFacade.claimRoute(mycommand.getUsername(), mycommand.getRouteID());
                break;
            }
            case Utils.CHAT_TYPE: {
                ChatCommand mycommand = (ChatCommand)command;
                clientFacade.addChat(mycommand.getUsername(), mycommand.getMessage());
                break;
            }
            default:
                break;
        }

    }
}
