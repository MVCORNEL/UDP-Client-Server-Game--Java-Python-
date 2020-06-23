import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {
    private static DatagramSocket mServerSocket;
    private static DatagramPacket mPacket;
    private static InetAddress mClientIPAddress;
    private static int mClientPort;
    private static String messagesList[];

    public static void main(String[] args) throws IOException {
        replyToTheClient(waitingForPackets(startServer()));
    }
    //initialization of the server
    private static boolean startServer() {
        try {
            mServerSocket = new DatagramSocket(9876);
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println(" Error occurred in server initialization!");
            System.exit(1);
            return false;
        }
        System.out.println("\n Server has been successfully initialized !");
        return true;
    }
    //server waits for a sequence of three distinct UDP packets containing strings S1. S2 and S3.
    private static boolean waitingForPackets(boolean serverIsInitialized) {
        //Server stores the messages into a array
        messagesList = new String[3];
        byte[] receiveBuffer;
        System.out.println(" Server is waiting for packets...");
        if (serverIsInitialized) {
            for (int i = 0; i < messagesList.length; i++) {
                receiveBuffer = new byte[2048];
                mPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                try {
                    mServerSocket.receive(mPacket);
                    messagesList[i] = new String(receiveBuffer).trim();
                    System.out.println(" Server successfully received string message \"S" + (i + 1) + "\" with the value of : " + messagesList[i]);
                    mClientPort = mPacket.getPort();
                    mClientIPAddress = mPacket.getAddress();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    //computes R by concatenating S1, S2 and S3, adding two characters ’#’ as separators;
    private static String proccessR(String[] messagesList) {
        String R = messagesList[0] + "#" + messagesList[1] + "#" + messagesList[2];
        System.out.println(" Server computes R by concatenating string packets S1#S2#S3 received from the client with the result of : " + R);
        return R;
    }
    //computes N as the sum of the lengths of S1 S2 and S3
    private static String calculateN(String[] messagesList) {
        int lentghSum = 0;
        for (int i = 0; i < messagesList.length; i++) {
            lentghSum += messagesList[i].length();
        }
        System.out.println(" Server computes N by summing the lengths of the received string packets S1 S2 and S3 with the result of : " + lentghSum);
        return String.valueOf(lentghSum);
    }
    private static void replyToTheClient(boolean dataSuccessfullyReceived) {
        if (dataSuccessfullyReceived) {
            sendPackage(proccessR(messagesList));
            sendPackage(calculateN(messagesList));
            System.out.println(" Server sends N and R back to the client");
            replyToTheClient(waitingForPackets(true));
        }
    }
    private static void sendPackage(String string) {
        byte sendBuffer[] = string.getBytes();
        mPacket = new DatagramPacket(sendBuffer, sendBuffer.length, mClientIPAddress, mClientPort);
        try {
            mServerSocket.send(mPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




