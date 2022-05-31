from datetime import datetime

class User:
    def __init__(self, id):
        self.id = id
        #stores chatroom id's along with number of last read message
        self.joinedChatrooms = {}

    def joinChat(self, chatroomId):
        self.joinedChatrooms[chatroomId] = 0

    def leaveChat(self, chatroomId):
        del self.joinedChatrooms[chatroomId]
