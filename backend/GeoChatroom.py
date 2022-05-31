from Message import Message
from Chatroom import Chatroom

class GeoChatroom(Chatroom):
    def __init__(self, id, latitude, longitude, radius):
        super().__init__(id) 
        self.latitude = latitude
        self.longitude = longitude
        self.radius = radius
