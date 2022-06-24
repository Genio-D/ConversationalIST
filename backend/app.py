from flask import Flask, request
from ServerManager import ServerManager
import threading
import socket

app = Flask(__name__)
serverManager = ServerManager("Hi")
clientSockets = {}

serverManager.addUser("user1", "password1")

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
#    client.sendall(str.encode("gotcha, thanks " + username + "\n"))

def notifyClients(memberList, chatId, messageNumber):
        for clientName, clientSocket in clientSockets.items():
            if clientName in memberList:
                try:
                    clientSocket.sendall(str.encode(f"{chatId}/{messageNumber}\n"))
                    print(f"notified {clientName} with -> {chatId}/{messageNumber}")
                except (ValueError, BrokenPipeError) as e:
                    print(e)


threading.Thread(target=startServerSocket).start()

@app.route("/")
def hello_world():
    return serverManager.greeting()

"""
EXAMPLE REQUEST
{
    "username": "value1",
    "password": "password1"
}
EXAMPLE RESPONSE
{}
"""
@app.route("/addUser", methods=["POST"])
def addUser():
    try:
        payload = request.get_json()
        username = payload['username']
        password = payload['password']
        serverManager.addUser(username, password)
        return makeOkResponse()
    except ValueError as e:
        return makeErrorResponse(str(e))

"""
EXAMPLE REQUEST
{
    "username": "value1",
    "password": "password1"
}
EXAMPLE RESPONSE
{}
"""
@app.route("/login", methods=["POST"])
def login():
    try:
        payload = request.get_json()
        username = payload['username']
        password = payload['password']
        serverManager.login(username, password)
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
    try:
        payload = request.get_json()
        username = payload['username']
        chatroomId = payload['chatroomId']
        messageType = payload['messageType']
        content = payload['content']
        memberList, messageNumber = serverManager.postMessage(username, chatroomId, messageType, content)
        print(f"memberList: {memberList}")
        print(f"messageNumber: {messageNumber}")
        notifyClients(memberList, chatroomId, messageNumber)
        return makeOkResponse()
    except ValueError as e:
        return makeErrorResponse(str(e))

"""
EXAMPLE REQUEST
{
    "chatId": "1",
    "username": "value1"
}
EXAMPLE RESPONSE
{}
"""
@app.route("/createPublicChatroom", methods=["POST"])
def createPublicChatroom():
    try:
        payload = request.get_json()
        chatId = payload['chatId']
        username = payload['username']
        serverManager.createPublicChatroom(chatId, username)
        return makeOkResponse()
    except ValueError as e:
        return makeErrorResponse(str(e))

"""
EXAMPLE REQUEST
{
    "chatId": "1",
    "username": "value1"
}
EXAMPLE RESPONSE
{}
"""
@app.route("/createPrivateChatroom", methods=["POST"])
def createPrivateChatroom():
    try:
        payload = request.get_json()
        chatId = payload['chatId']
        username = payload['username']
        serverManager.createPrivateChatroom(chatId, username)
        return makeOkResponse()
    except ValueError as e:
        return makeErrorResponse(str(e))


"""
EXAMPLE REQUEST
{
    "chatId": "1"
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
    chatId = payload['chatId']
    username = payload['username']
    latitude = payload['latitude']
    longitude = payload['longitude']
    radius = payload['radius']
    try:
        serverManager.createGeoChatroom(chatId, username, latitude, longitude, radius)
        return makeOkResponse()
    except ValueError as e:
        return makeErrorResponse(str(e))

"""
EXAMPLE REQUEST
{}
EXAMPLE RESPONSE
{"list": ["1","2"]}
"""
@app.route("/getPublicChatrooms", methods=["GET"])
def getPublicChatrooms():
    print("GET /getPublicChatrooms")
    idList = serverManager.getPublicChatrooms()
    return makeOkResponse({'list' : idList})

"""
EXAMPLE REQUEST
{}
EXAMPLE RESPONSE
{"list": ["1","2"]}
"""
@app.route("/getGeoChatrooms", methods=["GET"])
def getGeoChatrooms():
    print("GET /getGeoChatrooms")
    idList = serverManager.getGeoChatrooms()
    print(makeOkResponse({'list' : idList}))
    return makeOkResponse({'list' : idList})


"""
EXAMPLE REQUEST
{}
EXAMPLE RESPONSE
{
	"chatrooms": [{
		"chatId": "tagus",
		"type": "public",
		"messages": 54
	}, {
		"chatId": "park",
		"type": "private",
		"messages": 99
	}]
}

"""
@app.route("/getJoinedChatrooms/<username>", methods=["GET"])
def getJoinedChatrooms(username):
    try:
        chatInfo = serverManager.getJoinedChatrooms(username)
        print({"chatrooms": chatInfo})
        return makeOkResponse({"chatrooms": chatInfo})
    except ValueError as e:
        return makeErrorResponse(str(e))

"""
EXAMPLE REQUEST
{}
EXAMPLE RESPONSE
{
    "author": "value1",
    "content": "supercool",
    "timestamp": "2022-05-31T21:20:21.951677",
    "type": "this"
}
"""
@app.route("/chatrooms/<chatId>/<int:messageNumber>", methods=["GET"])
def getMessage(chatId, messageNumber):
    try:
        chatInfo = serverManager.getChatroomMessage(chatId, messageNumber)
        return makeOkResponse(chatInfo)
    except ValueError as e:
        return makeErrorResponse(str(e))

"""
EXAMPLE REQUEST
{
    "chatId": "1",
    "username": "value1"
}
EXAMPLE RESPONSE
{}
"""
@app.route("/joinRoom", methods=["POST"])
def joinRoom():
    try:
        payload = request.get_json()
        username = payload['username']
        chatId = payload['chatId']
        serverManager.joinRoom(username, chatId)
        return makeOkResponse()
    except ValueError as e:
        return makeErrorResponse(str(e))

"""
EXAMPLE REQUEST
{
    "chatId": "1",
    "username": "value1"
}
EXAMPLE RESPONSE
{}
"""
@app.route("/leaveRoom", methods=["POST"])
def leaveRoom():
    try:
        payload = request.get_json()
        username = payload['username']
        chatId = payload['chatId']
        serverManager.leaveRoom(username, chatId)
        return makeOkResponse()
    except ValueError as e:
        return makeErrorResponse(str(e))

def makeErrorResponse(errorMessage):
    return {'errorMessage' : errorMessage, 'error': True}

def makeOkResponse(response={}):
    response['errorMessage'] = None
    response['error'] = False
    return response