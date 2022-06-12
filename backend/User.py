from datetime import datetime

class User:
    def __init__(self, id):
        self.id = id
        #stores joined chatrooms
        self.joinedChatrooms = []

    def joinChat(self, chatroom):
        self.joinedChatrooms.append(chatroom)

    def leaveChat(self, chatroom):
        self.joinedChatrooms.remove(chatroom)
