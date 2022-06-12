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
@app.route("/addUser", methods=["POST"])
def addUser():
    payload = request.get_json()
    username = payload['username']
    if(serverManager.addUser(username)):
        return {}
    else:
        return makeErrorResponse("Username already exists")

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
    return {}

"""
EXAMPLE REQUEST
{"username": "value1"}
EXAMPLE RESPONSE
{}
"""
@app.route("/createPublicChatroom", methods=["POST"])
def createPublicChatroom():
    payload = request.get_json()
    username = payload['username']
    serverManager.createPublicChatroom(username)
    return {}

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
@app.route("/createGeoChatroom", methods=["POST"])
def createGeoChatroom():
    payload = request.get_json()
    username = payload['username']
    latitude = payload['latitude']
    longitude = payload['longitude']
    radius = payload['radius']
    serverManager.createGeoChatroom(username, latitude, longitude, radius)
    return {}

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
    return {'list' : idList}

"""
EXAMPLE REQUEST
{"username": "value1"}
EXAMPLE RESPONSE
{"chatrooms": {"1": 54, "2": 86}}
"""
@app.route("/getJoinedChatrooms", methods=["GET"])
def getJoinedChatrooms():
    payload = request.get_json()
    username = payload['username']
    chatInfo = serverManager.getJoinedChatrooms(username)
    return {"chatrooms": chatInfo}

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
"""
@app.route("/getChatroomMessages", methods=["GET"])
def getChatroomMessages():
    payload = request.get_json()
    chatroomId = payload['chatroomId']
    messagesToRetrieveList = payload['list']
    messageList = serverManager.getChatroomMessages(chatroomId, messagesToRetrieveList)
    return {'list' : messageList}

"""
EXAMPLE REQUEST
{"username":"value1","chatroomId":"1"}
EXAMPLE RESPONSE
{}
"""
@app.route("/joinRoom", methods=["POST"])
def joinRoom():
    payload = request.get_json()
    username = payload['username']
    chatroomId = payload['chatroomId']
    serverManager.joinRoom(username, chatroomId)
    return {}

def makeErrorResponse(message):
    return {'ERROR' : message}

def makeOkResponse(message):
    return {'OK' : message}

def notifyMembers(memberList):
    for username in memberList:
        if username in clientSockets:
            clientSockets[username].sendall(b"its time to update man\n")
