import socket
import sys

# Port and server number
HOST = "localhost"
PORT = 1888
server = (HOST, PORT)
receivedList = []
# initialize the socket--> kind of channel to send the data
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)


# Storing the data into an stringss array
def storeWords():
    list = []
    for i in range(3):
        message = input("Enter message:")
        list.append(message)
    return list


# message from string to bytes as the sendto function takes bytes wize object not string as parameter
def sendMessages(list):
    for messageByte in list:
        sock.sendto(messageByte.encode(), server)


# get the length of the input input strings S1 S2 S3
def calculateL(concatenatedString):
    return len(concatenatedString)


# concatenate words
def concatenateC(list):
    concatenatedWords = ""
    for word in list:
        concatenatedWords += word
    return concatenatedWords


def receiveData(sock):
    # Add the received data to a list
    # first element of the list is the n
    # second element is R
    receivedList = [0, ""]
    elementsFound = 0
    # if we received something will be different of "" --> wait just for 2 elements after break the loop
    while elementsFound < 2:
        receivedMessage = sock.recv(1024).decode()
        if (receivedMessage != ""):
            elementsFound += 1
            # parsing the received string if the string contains the cahracter # is the R if not is
            if "#" in receivedMessage:
                # The R element was found was found
                receivedList[1] = receivedMessage
            else:
                receivedList[0] = int(receivedMessage)
    print("Received messages n= %d and R= %s " % (receivedList[0], receivedList[1]))
    sock.close()
    return receivedList


def compareData(l, C, receivedMsgList):
    n = receivedMsgList[0]
    R = receivedMsgList[1]
    # take # out of R
    R = R.replace("#", "")
    nEqualsL = False
    cEqualsR = False
    # Comparing the recieved vs sent data
    if (n == l):
        nEqualsL = True
    if (C == R):
        cEqualsR = True

    print("  n = l " + str(nEqualsL) + "  C = R  " + str(cEqualsR))


list = storeWords()
sendMessages(list)
C = concatenateC(list)
l = calculateL(C)
print("The value of l= %d and C= %s " % (l, C))
receivedList = receiveData(sock)
compareData(l, C, receivedList)
