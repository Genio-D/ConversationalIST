import json
from datetime import datetime

class Message:
    def __init__(self, username, messageType, content):
        self.type = messageType
        self.content = content
        self.author = username
        self.time = datetime.now()

    def toJson(self):
        return json.dumps({'type':self.type,
            'content':self.content,
            'author':self.author,
            'timestamp':self.time.isoformat()
        })

    def toDict(self):
        return {'type':self.type,
            'content':self.content,
            'author':self.author,
            'timestamp':self.time.isoformat()
        }