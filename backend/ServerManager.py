import json
from multiprocessing.sharedctypes import Value
from PrivateChatroom import PrivateChatroom
from User import User
from Chatroom import Chatroom
from PublicChatroom import PublicChatroom
from GeoChatroom import GeoChatroom
from Message import Message

class ServerManager:

    def __init__(self, greetingMessage):
        self.greetingMessage = greetingMessage
        self.users = []
        self.chatrooms = []

    def greeting(self):
        response = {'text' : self.greetingMessage}
        return json.dumps(response)

    def addUser(self, username, password):
        if username not in [user.id for user in self.users]:
            self.users.append(User(username, password))
            return True
        raise ValueError("User already exists")

    def login(self, username, password):
        user = self.findUser(username)
        if user.password != password:
            raise ValueError("incorrect username/password")

    def addChatroom(self, chatroom, user):
        if chatroom.id not in [chat.id for chat in self.chatrooms]:
            chatroom.addMember(user)
            user.joinChat(chatroom)
            self.chatrooms.append(chatroom)
        else:
            raise ValueError("chatroom name already exists")

    def createPublicChatroom(self, chatId, username):
        newChat = PublicChatroom(chatId)
        user = self.findUser(username)
        self.addChatroom(newChat, user)

    def createPrivateChatroom(self, chatId, username):
        newChat = PrivateChatroom(chatId)
        user = self.findUser(username)
        self.addChatroom(newChat, user)

    def createGeoChatroom(self, chatId, username, latitude, longitude, radius):
        newChat = GeoChatroom(chatId, latitude, longitude, radius)
        user = self.findUser(username)
        self.addChatroom(newChat, user)

    def postMessage(self, username, chatroomId, messageType, content):
        self.findUser(username)
        message = Message(username, messageType, content)
        chat = self.findChat(chatroomId)
        chat.postMessage(message)    
        memberList = [member.id for member in chat.members]
        messageNumber = len(chat.getMessages()) - 1
        return memberList, messageNumber 


    def getPublicChatrooms(self):
        return [chat.toDict() for chat in self.chatrooms if type(chat) is PublicChatroom]

    def getGeoChatrooms(self):
        return [chat.toDict() for chat in self.chatrooms if type(chat) is GeoChatroom]

    def getJoinedChatrooms(self, username):
        user = self.findUser(username)
        chatrooms = user.joinedChatrooms
        chatInfo = []
        for chat in chatrooms:
            chatInfo.append({"chatId":chat.id, "type":chat.getType(), "messages":len(chat.messages)})
        return chatInfo

    def getChatroomMessages(self, chatId, messagesToRetrieveList):
        messageList = []
        print(messagesToRetrieveList)
        chat = self.findChat(chatId)
        for messageNumber in messagesToRetrieveList:
            messageList.append(chat.messages[messageNumber].toDict())
        return messageList

    def getChatroomMessage(self, chatId, messageNumber):
        chat = self.findChat(chatId)
        return chat.messages[messageNumber].toDict()

    def joinRoom(self, username, chatId):
        user = self.findUser(username)
        chat = self.findChat(chatId)
        user.joinChat(chat)
        chat.addMember(user)
        return True

    def leaveRoom(self, username, chatId):
        user = self.findUser(username)
        chat = self.findChat(chatId)
        user.leaveChat(chat)
        chat.removeMember(user)

    def findUser(self, username):
        for user in self.users:
            if user.id == username:
                return user
        raise ValueError("user doesn't exist")

    def findChat(self, chatId):
        for chat in self.chatrooms:
            if chat.id == chatId:
                return chat
        raise ValueError("chat doesn't exist")