from Message import Message

class Chatroom:
    def __init__(self, id):
        self.id = id
        self.messages = []
        self.members = set()

    def getMessages(self):
        return self.messages

    def postMessage(self, message):
        self.messages.append(message)

    def addMember(self, user):
        self.members.add(user)

    def removeMember(self, user):
        self.members.remove(user)