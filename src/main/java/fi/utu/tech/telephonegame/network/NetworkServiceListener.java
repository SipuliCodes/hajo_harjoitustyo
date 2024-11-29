package fi.utu.tech.telephonegame.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Does the listening for NetworkService
 *
 *  Implements runnable to be able to run on different Thread
 *  leaving main thread for NetworkService
 */
public class NetworkServiceListener implements Runnable{
    private final int serverPort;
    private final NetworkService networkService;

    public NetworkServiceListener(int serverPort, NetworkService networkService) {
        this.serverPort = serverPort;
        this.networkService = networkService;
    }

    /**
     * Creates a new ServerSocket with the given serverPort
     *
     * Listens for new connections indefinitely
     *
     * Server socket is automatically closed when method exists
     */
    public void run() {
        try(ServerSocket socket = new ServerSocket(serverPort)){
            while(true) {
                Socket s = socket.accept();
                NetworkServiceCommunicationHandler nsch = new NetworkServiceCommunicationHandler(s, this.networkService);
                networkService.addToCommunicationHandlers(nsch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
