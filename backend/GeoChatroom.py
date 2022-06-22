from Message import Message
from Chatroom import Chatroom

class GeoChatroom(Chatroom):
    def __init__(self, id, latitude, longitude, radius):
        super().__init__(id) 
        self.latitude = latitude
        self.longitude = longitude
        self.radius = radius

    def getType(self):
        return "geo"
        
    def toDict(self):
        return {
            "chatId": self.id,
            "latitude": self.latitude,
            "longitude": self.longitude,
            "radius": self.radius,
            "messages": len(self.messages),
            "type": self.getType()
        }