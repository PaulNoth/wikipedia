#Custom class for throwing file error
class FileError(Exception):
    def __init__(self, path):
        self.path = path
        self.error = 'File not found: ' + path

    def __str__(self):
        return repr(self.path)