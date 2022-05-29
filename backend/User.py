from datetime import datetime

class User:
    def __init__(self, id):
        self.id = id
        #stores chatroom id's along with datetime of last read message
        self.joinedChatrooms = {}

    def joinChat(self, chatroomId):
        self.joinedChatrooms[chatroomId] = datetime.min

    def leaveChat(self, chatroomId):
        del self.joinedChatrooms[chatroomId]
