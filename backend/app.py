from flask import Flask, request
from ServerManager import ServerManager
import json

app = Flask(__name__)
serverManager = ServerManager("Hi")

#TODO: Error handling? JSON validation?

@app.route("/")
def hello_world():
    return serverManager.greeting()

@app.route("/addUser", methods=["POST"])
def addUser():
    payload = request.get_json()
    username = payload['username']
    if(serverManager.addUser(username)):
        return {}
    else:
        return makeErrorResponse("Username already exists")

@app.route("/postMessage", methods=["POST"])
def postMessage():
    payload = request.get_json()
    username = payload['username']
    chatroomId = payload['chatroomId']
    messageType = payload['messageType']
    content = payload['content']
    serverManager.postMessage(username, chatroomId, messageType, content)
    return {}

@app.route("/createPublicChatroom", methods=["POST"])
def createPublicChatroom():
    payload = request.get_json()
    username = payload['username']
    serverManager.createPublicChatroom(username)
    return {}

@app.route("/getPublicChatrooms", methods=["GET"])
def getPublicChatrooms():
    idList = serverManager.getPublicIds()
    return {'list' : idList}

@app.route("/getJoinedChatrooms", methods=["GET"])
def getJoinedChatrooms():
    payload = request.get_json()
    username = payload['username']
    idList = serverManager.getJoinedIds(username)
    return {'list' : idList}

@app.route("/getChatroomMessages", methods=["GET"])
def getChatroomMessages():
    payload = request.get_json()
    chatroomId = payload['chatroomId']
    messagesToRetrieveList = payload['list']
    messageList = serverManager.getChatroomMessages(chatroomId, messagesToRetrieveList)
    return {'list' : messageList}

def makeErrorResponse(message):
    return {'ERROR' : message}
