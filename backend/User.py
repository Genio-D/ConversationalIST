from datetime import datetime

class User:
    def __init__(self, id, password):
        self.id = id
        self.password = password
        #stores joined chatrooms
        self.joinedChatrooms = set()

    def getJoinedChatrooms(self):
        return self.joinedChatrooms

    def joinChat(self, chatroom):
        self.joinedChatrooms.add(chatroom)

    def leaveChat(self, chatroom):
        self.joinedChatrooms.remove(chatroom)
