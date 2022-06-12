from Message import Message

class Chatroom:
    def __init__(self, id):
        self.id = id
        self.messages = []
        self.members = []

    def postMessage(self, message):
        self.messages.append(message)

    def addMember(self, user):
        self.members.append(user)

    def removeMember(self, user):
        self.members.remove(user)