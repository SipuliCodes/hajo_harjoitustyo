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

    public NetworkServiceListener(int serverPort) {
        this.serverPort = serverPort;
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
                System.out.println("Connection established!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
