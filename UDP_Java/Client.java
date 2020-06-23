import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static DatagramSocket clientSocket;
    private static InetAddress mIPAddress;
    private static DatagramPacket mPacket;
    private static Scanner mScanner;
    private static boolean serverStatus;

    public static void main(String[] args) {
        //If the sever is successfully initialized, store the 3 prompt messages into a list
        serverStatus = initiliazeClient();
        String[] sentMessages = storePromptMessages(serverStatus);
        //Assign the corresponding values to l(initial length) and C(initial S1+S2+S3)
        int l = calculateLength(sentMessages);
        String C = concatenateStrings(sentMessages);
        //Sends the messages stored into the array to the Server as UDP packets,
        //if the packets are successfully sent recieves from the server two packets, and store them into an array
        String[] receivedMessages = receivingData(sendPackets(sentMessages));
        //client processes the received UDP packets and carries out the required comparisons, also displau R and n
        proccessReceivedData(receivedMessages, C, l);
    }

    //initialization on the client socket
    private static boolean initiliazeClient() {
        try {
            clientSocket = new DatagramSocket();
            mIPAddress = InetAddress.getLocalHost();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
            System.out.println(" Error occurred in server initialization");
            return false;
        }
        System.out.println("\n Client has been successfully initialized !");
        return true;
    }

    // uses the Scanner class to prompt the user with 3 string messages
    private static String[] storePromptMessages(boolean serverIsInitialized) {
        String sentMessagesList[];
        if (serverIsInitialized) {
            sentMessagesList = new String[3];
            mScanner = new Scanner(System.in);
            for (int i = 0; i <= 2; i++) {
                System.out.print("    Please insert the message S" + (i + 1) + " : ");
                sentMessagesList[i] = mScanner.nextLine().trim();
            }
            return sentMessagesList;
        }
        return null;
    }

    // calculates the sum ℓ of the length of the three strings which were sent to the Server
    private static int calculateLength(String[] s) {
        if (s != null) {
            System.out.println(" Client has successfully calculated the sum of the lengths of the three string with the value : l = " + s.length);
            return s.length;
        }
        return 0;
    }

    //calculates the the concatenation of S1, S2 and S3
    private static String concatenateStrings(String[] list) {
        String newString = "";
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                newString += list[i];
            }
            System.out.println(" Client has successfully concatenation of S1, S2 and S3, that is C with the value : C = " + newString);
        }
        return newString;
    }

    // sends S1, S2 and S3 to the server in three distinct UDP packets;
    private static boolean sendPackets(String[] list) {
        if (list != null) {
            byte[] sendingBuffer;
            for (int i = 0; i < list.length; i++) {
                sendingBuffer = list[i].getBytes();
                mPacket = new DatagramPacket(sendingBuffer, sendingBuffer.length, mIPAddress, 9876);
                try {
                    clientSocket.send(mPacket);
                    System.out.println(" Client has successfully sent to the server the UDP packet (S" + i + ") with the string value of : " + list[i]);
                } catch (IOException e) {
                    System.out.println(" Error occurred, client failed to send the UDP packet (S" + i + ") : with the string value of : " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    //client waits for two distinct UDP packets from string R and an integer n from the server and store them in an array
    private static String[] receivingData(boolean successfullySent) {
        if (successfullySent) {
            //Client expects exactly 2 UDP packets
            String returnedMessagesList[] = new String[2];
            for (int i = 0; i < 2; i++) {
                byte[] receiveBuffer = new byte[1024];
                mPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                try {
                    clientSocket.receive(mPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                returnedMessagesList[i] = new String(receiveBuffer).trim();
                System.out.println(" Client has successfully received a packet from the server with the string value of : " + returnedMessagesList[i]);
            }
            return returnedMessagesList;
        }
        return null;
    }

    //client process the data after removing all characters ’#’ from R, checks that the result is the same as the concatenation
    // checks if the received n is the correct sum of the lengths of S1, S2 and S3;
    // displays the results of the checks.
    private static void proccessReceivedData(String[] list, String C, int l) {
        if (list != null && list.length == 2) {
            int n = 0;
            String R = null;
            //Removing the # from the string the string
            for (int i = 0; i < list.length; i++) {
                //this condition is here in case the packets sending order is different from the expected one
                //the received message is the the concatenated String so will include at least one # symbol
                if (list[i].contains("#")) {
                    R = list[i].replace("#", "");
                    System.out.println(" Client has succesfully removed all # symbols from R = " + list[i] + " obtaining the new String result of : R = " + R);
                }
                //Client stores the n value received from the server into a integer variable
                else {
                    n = Integer.valueOf(list[i]);
                }
            }
            //Finally the client carries out the comparison between variables
            System.out.println(" Client has successfully carried out the comparison between strings : C = R ? " + (C.equals(R) ? " YES " : " NO "));
            System.out.println(" Client has successfully carried out the comparison between lengths : n = l ? " + (n == l ? " YES " : " NO "));
            System.out.println(" R = " + R + "\n n = " + n);
        }
        resendMessage();

    }

    private static void resendMessage() {
        System.out.print("    Do you want to senD another three UDP packets ? Y/N : ");
        mScanner = new Scanner(System.in);
        String Go = mScanner.nextLine();

        switch (Go) {
            case "Y":
                System.out.println();
                String[] sentMessages = storePromptMessages(serverStatus);
                //Assign the corresponding values to l(initial length) and C(initial S1+S2+S3)
                int l = calculateLength(sentMessages);
                String C = concatenateStrings(sentMessages);
                //Sends the messages stored into the array to the Server as UDP packets,
                //if the packets are successfully sent recieves from the server two packets, and store them into an array
                String[] receivedMessages = receivingData(sendPackets(sentMessages));
                //client processes the received UDP packets and carries out the required comparisons, also displau R and n
                proccessReceivedData(receivedMessages, C, l);
                break;
            case "N":
                System.out.println(" Client socket closed \n");
                clientSocket.close();
                mScanner.close();
                break;
            default:
                System.out.println(" Invalid request \n");
                resendMessage();
        }

    }


}
