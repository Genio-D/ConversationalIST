from Message import Message
from Chatroom import Chatroom

class PublicChatroom(Chatroom):

    def getType(self):
        return "public"

    def toDict(self):
        return {
            "chatId": self.id,
            "messages": len(self.messages),
            "type": self.getType()
        }