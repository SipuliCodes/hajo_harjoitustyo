package fi.utu.tech.telephonegame.network;

import fi.utu.tech.telephonegame.Message;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class to handle communication between nodes
 *
 * Implements runnable to be able to run on a separate Thread
 * enabling having multiple connections at the same time
 */
public class NetworkServiceCommunicationHandler implements Runnable {
    private final Socket socket;
    private final NetworkService networkService;

    private final BlockingQueue<Serializable> messageQueue = new LinkedBlockingQueue<>();

    public NetworkServiceCommunicationHandler(Socket socket, NetworkService networkService) {
        this.socket = socket;
        this.networkService = networkService;
    }

    /**
     * Method for NetworkService to add messages to queue for sending
     *
     * @param message message to add to messageQueue
     */
    public void sendMessage(Serializable message) {
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            System.out.println("Message " + message + " could not be added to queue");
        }
    }

    /**
     * Creates a new Thread for reading incoming Objects from the socket
     *
     * @param ois The ObjectInputStream that the Thread reads
     */
    private void readThread(ObjectInputStream ois) {
        new Thread(() -> {
            try {
                Object obj;
                while( (obj = ois.readObject()) != null){
                    if (obj instanceof Message) {
                        Message receivedMessage = (Message) obj;
                        networkService.addReceivedMessage(receivedMessage);
                    } else {
                        System.out.println("Received object is not of expected type.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("Class was not found: " + e.getMessage() );
            }
        }).start();
    }

    /**
     * Creates a new Thread used for writing Objects to the Socket
     *
     * @param oos The ObjectOutputStream that the Thread writes to
     */
    private void writeThread(ObjectOutputStream oos) {
        new Thread(() -> {
            try {
                Serializable nextMessage = messageQueue.take();
                oos.writeObject(nextMessage);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("Message could not be sent");
            }
        }).start();
    }

    public void run() {
        try {
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            ObjectOutputStream oos = new ObjectOutputStream(os);
            ObjectInputStream ois = new ObjectInputStream(is);

            readThread(ois);
            writeThread(oos);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
