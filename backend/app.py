from time import sleep
from flask import Flask, request
from ServerManager import ServerManager
import json
import threading
import socket

app = Flask(__name__)
serverManager = ServerManager("Hi")
clientSockets = {}

#TODO: Error handling? JSON validation?

def startServerSocket():
    print("Starting server socket")
    ServerSocket = socket.socket()
    ServerSocket.bind(('127.0.0.1', 5001))
    ServerSocket.listen()
    print("Server socket started")
    while True:
        acceptClient(ServerSocket)

def acceptClient(serverSocket):
    Client, address = serverSocket.accept()
    print("New client connected")
    threading.Thread(target=handleClient, args=(Client,)).start()

def handleClient(client):
    print("waiting for this dude's message")
    data = client.recv(2048)
    username = data.decode('utf-8').strip()
    print("created socket for user: " + username)
    clientSockets[username] = client
    #client.sendall(str.encode("gotcha, thanks " + username))
    client.sendall(b"gotcha, thanks dude\n")
    client.sendall(b"gotcha, thanks dude2\n")
    client.sendall(b"gotcha, thanks dude3\n")

threading.Thread(target=startServerSocket).start()

@app.route("/")
def hello_world():
    return serverManager.greeting()

"""
EXAMPLE REQUEST
{"username": "value1"}
EXAMPLE RESPONSE
{}
"""
@app.route("/addUser/<username>", methods=["POST"])
def addUser(username):
    try:
        serverManager.addUser(username)
        return makeOkResponse()
    except ValueError as e:
        return makeErrorResponse(str(e))

"""
EXAMPLE REQUEST
{
	"username": "value1",
    "chatroomId": "1",
    "messageType": "this",
    "content": "supercool"
}
EXAMPLE RESPONSE
{}
"""
@app.route("/postMessage", methods=["POST"])
def postMessage():
    payload = request.get_json()
    username = payload['username']
    chatroomId = payload['chatroomId']
    messageType = payload['messageType']
    content = payload['content']
    memberList = serverManager.postMessage(username, chatroomId, messageType, content)
    print(memberList)
    print(clientSockets)
    notifyMembers(memberList)
    return makeOkResponse()

"""
EXAMPLE REQUEST
{}
EXAMPLE RESPONSE
{}
"""
@app.route("/createPublicChatroom/<chatId>/<username>", methods=["POST"])
def createPublicChatroom(chatId, username):
    try:
        serverManager.createPublicChatroom(chatId, username)
    except ValueError as e:
        return makeErrorResponse(str(e))
    return makeOkResponse()

"""
EXAMPLE REQUEST
{
	"username": "value1",
    "latitude": 123.123,
    "longitude": 321.321,
    "radius": 1000
}
EXAMPLE RESPONSE
{}
"""
@app.route("/createGeoChatroom/<chatId>/<username>/<latitude>/<longitude>/<radius>", methods=["POST"])
def createGeoChatroom(chatId, username, latitude, longitude, radius):
    latitude = float(latitude)
    longitude = float(longitude)
    radius = int(radius)
    try:
        serverManager.createGeoChatroom(chatId, username, latitude, longitude, radius)
    except ValueError as e:
        return makeErrorResponse(str(e))
    return makeOkResponse()

"""
EXAMPLE REQUEST
{}
EXAMPLE RESPONSE
{"list": ["1","2"]}
"""
@app.route("/getPublicChatrooms", methods=["GET"])
def getPublicChatrooms():
    print("GET /getPublicChatrooms")
    idList = serverManager.getPublicIds()
    return makeOkResponse({'list' : idList})

"""
EXAMPLE REQUEST
{}
EXAMPLE RESPONSE
{"chatrooms": {"1": 54, "2": 86}}
"""
@app.route("/getJoinedChatrooms/<username>", methods=["GET"])
def getJoinedChatrooms(username):
    chatInfo = serverManager.getJoinedChatrooms(username)
    return makeOkResponse({"chatrooms": chatInfo})


"""
EXAMPLE REQUEST
{}
EXAMPLE RESPONSE
{
    "message":
        {
            "author": "value1",
            "content": "supercool",
            "timestamp": "2022-05-31T21:20:21.951677",
            "type": "this"
        }
}
"""
@app.route("/chatrooms/<chatId>/<int:messageNumber>", methods=["GET"])
def getMessage(chatId, messageNumber):
    chatInfo = serverManager.getChatroomMessage(chatId, messageNumber)
    return makeOkResponse({"message": chatInfo})

"""
EXAMPLE REQUEST
{
	"chatroomId":"1",
    "list":[0, 1, 2]
}
EXAMPLE RESPONSE
{
    "list": [
        {
            "author": "value1",
            "content": "supercool",
            "timestamp": "2022-05-31T21:20:21.951677",
            "type": "this"
        },
        {
            "author": "value1",
            "content": "supercool2",
            "timestamp": "2022-05-31T21:20:26.019176",
            "type": "this"
        }
    ]
}

@app.route("/getChatroomMessages", methods=["GET"])
def getChatroomMessages():
    payload = request.get_json()
    chatroomId = payload['chatroomId']
    messagesToRetrieveList = payload['list']
    messageList = serverManager.getChatroomMessages(chatroomId, messagesToRetrieveList)
    return makeOkResponse({'list' : messageList})
"""
"""
EXAMPLE REQUEST
{}
EXAMPLE RESPONSE
{}
"""
@app.route("/joinRoom/<chatId>/<username>", methods=["POST"])
def joinRoom(chatId, username):
    serverManager.joinRoom(username, chatId)
    return makeOkResponse()

def makeErrorResponse(message):
    return {'ERROR' : message, 'OK' : None}

def makeOkResponse(message="BIGSUCESS"):
    return {'OK' : message, 'ERROR' : None}

def notifyMembers(memberList):
    for username in memberList:
        if username in clientSockets:
            clientSockets[username].sendall(b"its time to update man\n")
