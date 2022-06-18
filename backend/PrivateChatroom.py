from Message import Message
from Chatroom import Chatroom

class PrivateChatroom(Chatroom):

    def getType(self):
        return "private"