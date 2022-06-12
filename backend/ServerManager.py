import json
from User import User
from Chatroom import Chatroom
from PublicChatroom import PublicChatroom
from GeoChatroom import GeoChatroom
from Message import Message

class ServerManager:

    def __init__(self, greetingMessage):
        self.greetingMessage = greetingMessage
        self.users = []
        self.chatrooms = ["0-27", "A1"]
        self.chatroomIdCounter = 0

    def greeting(self):
        response = {'text' : self.greetingMessage}
        return json.dumps(response)

    def addUser(self, id):
        if id not in [user.id for user in self.users]:
            self.users.append(User(id))
            return True
        return False

    def createPublicChatroom(self, username):
        newId = self.getNextId()
        newChat = PublicChatroom(newId)
        newChat.addMember(username)
        self.chatrooms.append(newChat)
        self.findUser(username).joinChat(newId)
        return True

    def createGeoChatroom(self, username, latitude, longitude, radius):
        newId = self.getNextId()
        self.chatrooms.append(GeoChatroom(newId, latitude, longitude, radius))
        self.findUser(username).joinChat(newId)
        return True

    def postMessage(self, username, chatroomId, messageType, content):
        message = Message(username, messageType, content)
        chat = self.findChat(chatroomId)
        chat.postMessage(message)
        return chat.members

    def getNextId(self):
        self.chatroomIdCounter += 1
        return str(self.chatroomIdCounter) 

    def getPublicIds(self):
        return [chat.id for chat in self.chatrooms if type(chat) is PublicChatroom]

    def getJoinedChatrooms(self, username):
        user = self.findUser(username)
        chatrooms = [self.findChat(chatId) for chatId in user.joinedChatrooms]
        chatInfo = {}
        for chat in chatrooms:
            chatInfo[chat.id] = len(chat.messages)
        return chatInfo

    def getChatroomMessages(self, chatId, messagesToRetrieveList):
        messageList = []
        print(messagesToRetrieveList)
        chat = self.findChat(chatId)
        for messageNumber in messagesToRetrieveList:
            messageList.append(chat.messages[messageNumber].toDict())
        return messageList

    def joinRoom(self, username, chatId):
        user = self.findUser(username)
        chat = self.findChat(chatId)
        user.joinChat(chatId)
        chat.addMember(username)
        return True

    def findUser(self, username):
        for user in self.users:
            if user.id == username:
                return user
        return None

    def findChat(self, chatId):
        for chat in self.chatrooms:
            if chat.id == chatId:
                return chat
        return None