from datetime import datetime

class User:
    def __init__(self, id):
        self.id = id
        #stores ids of joined chatrooms
        self.joinedChatrooms = []

    def joinChat(self, chatroomId):
        self.joinedChatrooms.append(chatroomId)

    def leaveChat(self, chatroomId):
        del self.joinedChatrooms[chatroomId]
