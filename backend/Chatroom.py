from Message import Message

class Chatroom:
    def __init__(self, id):
        self.id = id
        self.messages = []

    def postMessage(self, message):
        self.messages.append(message)
