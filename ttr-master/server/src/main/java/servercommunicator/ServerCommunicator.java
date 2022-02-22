package servercommunicator;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.logging.*;

import database.IDatabase;
import database.DatabaseFactory;
import handlers.ServerWebSocket;
import serverfacade.ServerFacade;
import utils.Utils;

public class ServerCommunicator{

    private static Logger logger;

    static{
        try{
            initLog();
        }catch (IOException e){
            System.out.println("Could not initialize log: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initLog() throws IOException{
        Level logLevel = Level.ALL;
        logger = Logger.getLogger(Utils.SERVER_LOG);
        logger.setLevel(logLevel);
        logger.setUseParentHandlers(false);

        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(logLevel);
        consoleHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord logRecord) {
                return logRecord.getLevel() + " " + logRecord.getMessage() + "\n";
            }
        });
        logger.addHandler(consoleHandler);
    }

    public static void main(String[] args) throws Exception{
        int port = 8080;


        try {
            String myDatabaseType = args[0];
            int commandsUntilSave = Integer.parseInt(args[1]);
            if (commandsUntilSave < 1)
                throw new NumberFormatException("Commands until save cannot be less than 1");

            //IDatabase myDatabase = DatabaseFactory.getDatabase(myDatabaseType, commandsUntilSave);
            IDatabase myDatabase = DatabaseFactory.getDatabase(myDatabaseType, 20);
            ServerFacade.setAndLoadDatabase(myDatabase);
            logger.info("Loaded the: " + myDatabaseType + " database");
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
            logger.log(Level.SEVERE, "Invalid command line arguments!", ex);
            logger.warning("Continuing without a real database attached");

            IDatabase myDatabase = DatabaseFactory.getDatabase("database", 20);
            ServerFacade.setAndLoadDatabase(myDatabase);
        }

        WebSocketHandler wsHandler = new WebSocketHandler()
        {
            @Override
            public void configure(WebSocketServletFactory factory)
            {
                factory.register(ServerWebSocket.class);
            }
        };

        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        ResourceHandler rh = new ResourceHandler();
        rh.setDirectoriesListed(true);
        rh.setResourceBase("./server/web");

        wsHandler.setHandler(rh);

        server.setHandler(wsHandler);
        server.start();

        //output ip addresses to logger
        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {
                String info = netint.getName();
                logger.info("Name: " + info);
                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();

                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    logger.info("InetAddress: " + inetAddress);
                }
            }
        }catch (SocketException ex){
            logger.severe("Socket exception in Network interface");
        }

        server.join();

    }

}


