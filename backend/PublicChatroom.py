from Message import Message
from Chatroom import Chatroom

class PublicChatroom(Chatroom):

    def getType(self):
        return "public"