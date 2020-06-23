import socketserver
import socket

HOST="localhost"
PORT=1888
messageList=[]



def calculateN(messageList):
    stringsLength=0
    for message in messageList:
        stringsLength+=len(message)
    return stringsLength

def computeR(messageList):
    return messageList[0]+"#"+messageList[1]+"#"+messageList[2]


#initialize HANDLER
class MyUDPHandler(socketserver.DatagramRequestHandler):
    def handle(self):
        data = self.request[0].decode()
        print("Received msg: %s "% data)
        #used to add the messages to the list
        messageList.append(data)
        #when the list has 3 elements
        if (len(messageList)==3):
           n=str(calculateN(messageList))
           R=computeR(messageList)
           print("n = %s R = %s "%(n,R))
           #send n
           self.request[1].sendto(n.encode(),self.client_address)
           #send R
           self.request[1].sendto(R.encode(),self.client_address)
           messageList.clear()






if __name__ == '__main__':
    server = socketserver.UDPServer((HOST, PORT), MyUDPHandler)
    server.serve_forever()





