import Tkinter

#showing indefinte or definete progressbar
class ProgressBar:
    def __init__(self):
        self._root = Tkinter.Tk()
        self._root.resizable(False, False)
        self._root.title('Parsing input files...')
        self._canvas = Tkinter.Canvas(self._root, width=500, height=30)
        self._canvas.grid()
        self._lastpos = -0.01
        self._color = 'red'

    def start(self):
        self._root.deiconify()
        self._root.focus_set()

    def stop(self):
        self._root.withdraw()
        self._root.destroy()

    def reset_title(self):
        self._root.title('Finding unique categories....')

    def reset(self):
        self._lastpos = 0
        self._color = 'blue'
        self.reset_title()

    def update(self):
        if self._lastpos > 0.98:
            self._lastpos = 0.95
        elif self._lastpos > 0.9:
            self._lastpos += 0.001
        elif self._lastpos > 0.7:
            self._lastpos += 0.002
        elif not self._lastpos < 0.6:
            self._lastpos += 0.004
        elif self._lastpos > 0.2:
            self._lastpos += 0.005
        else:
            self._lastpos += 0.01

        self._canvas.delete(Tkinter.ALL)
        self._canvas.create_rectangle(0, 0, 500 * self._lastpos, 30, fill=self._color)
        self._root.update()
        self._root.focus_set()

    def update_exact(self, ratio):
        self._canvas.delete(Tkinter.ALL)
        self._canvas.create_rectangle(0, 0, 500 * ratio, 30, fill=self._color)
        self._root.update()
        self._root.focus_set()