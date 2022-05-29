import json
from User import User
from PublicChatroom import PublicChatroom
from Message import Message

class ServerManager:

    def __init__(self, greetingMessage):
        self.greetingMessage = greetingMessage
        self.users = []
        self.publicChatrooms = []
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
        self.publicChatrooms.append(PublicChatroom(newId))
        self.findUser(username).joinChat(newId)
        return True

    def postMessage(self, username, chatroomId, messageType, content):
        message = Message(username, messageType, content)
        self.findChat(chatroomId).postMessage(message)
        return True

    def getNextId(self):
        self.chatroomIdCounter += 1
        return str(self.chatroomIdCounter) 

    def getPublicIds(self):
        return ",".join([chat.id for chat in self.publicChatrooms])

    def findUser(self, username):
        for user in self.users:
            if user.id == username:
                return user
        return None

    def findChat(self, chatId):
        for chat in self.publicChatrooms:
            if chat.id == chatId:
                return chat
        return None