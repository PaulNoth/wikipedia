# class for showing gui
from __future__ import division
import Tkinter, tkFileDialog, Tkconstants, ttk
from time import sleep
import thread
import sys
import main
from progressbar import ProgressBar

# initializing main frame with imputs and start button
class TkMain(Tkinter.Frame):
    def __init__(self, root):
        Tkinter.Frame.__init__(self, root)
        self._filenames = 'data/sample_categories_input-enwiki-latest-categorylinks.sql'
        self._filenamex = 'data/sample_categories_input-enwiki-latest-pages.xml'
        self._finalxml = None
        self._run = False
        self._cats = None

        button_opt = {'fill': Tkconstants.BOTH, 'padx': 15, 'pady': 15}
        Tkinter.Button(self, text='Latest Articles Xml', command=self.ask_open_filename_x).pack(**button_opt)
        Tkinter.Button(self, text='Latest Categories Sql', command=self.ask_open_filename_s).pack(**button_opt)
        Tkinter.Button(self, text='Start', command=lambda: self.start_by_click(root)).pack(**button_opt)

        self.fileX_opt = options = {}
        options['defaultextension'] = ['.txt', '.xml', 'bz2']
        options['filetypes'] = [('all files', '.*'), ('text files', '.txt'), ('xml files', '.xml'),
                                ('bz files', '.bz2')]
        options['initialfile'] = 'sample_categories_input-enwiki-latest-pages.xml'
        options['parent'] = root
        options['title'] = 'Open Latest Articles Xml'

        self.fileS_opt = options = {}
        options['defaultextension'] = ['.txt', '.sql', '.bz2']
        options['filetypes'] = [('all files', '.*'), ('text files', '.txt'), ('sql files', '.sql'),
                                ('bz files', '.bz2')]
        options['initialfile'] = 'sample_categories_input-enwiki-latest-categorylinks.sql'
        options['parent'] = root
        options['title'] = 'Open Latest Categories Sql'

    #function for opening files
    def ask_open_filename_x(self):
        self._filenamex = tkFileDialog.askopenfilename(**self.fileX_opt)

    def ask_open_filename_s(self):
        self._filenames = tkFileDialog.askopenfilename(**self.fileS_opt)

    #starting parsing and analyzing
    def start_by_click(self, master):
        pb = ProgressBar()
        pb.start()
        self._run = True
        thread.start_new_thread(self.run_parse, ())

        while self._run:
            pb.update()
            sleep(1)

        self._run = True
        pb.reset()

        tot = self._cats._total
        self._finalxml = main.get_stats(self._cats)
        thread.start_new_thread(self.run_stats, ())

        while self._run:
            pb.update_exact(self._finalxml._total / tot)
            sleep(1)

        pb.stop()
        w = Tkinter.Label(master, text="Total Categories: " + self._finalxml.get_total())
        w.pack()
        w = Tkinter.Label(master, text="Categories not in database: " + self._finalxml.get_unique())
        w.pack()
        w = Tkinter.Label(master, text="Avg not in database categories per Article: " + self._finalxml.get_avg())
        w.pack()
        w = Tkinter.Label(master, text="Unique Categories: " + self._finalxml.get_one_time_unique())
        w.pack()

        scrollbar = Tkinter.Scrollbar(master)
        scrollbar.pack(side=Tkconstants.RIGHT, fill=Tkconstants.Y)

        text = Tkinter.Text(master, wrap=Tkconstants.WORD, yscrollcommand=scrollbar.set)
        text.pack(fill=Tkconstants.BOTH, expand=1, side=Tkinter.BOTTOM)

        scrollbar.config(command=text.yview)

        textsearch = Tkinter.Text(master, height=1, width=1, wrap=Tkconstants.WORD)
        textsearch.pack(fill=Tkconstants.BOTH, expand=1, side=Tkinter.BOTTOM)
        textsearch.bind("<Return>", lambda x: self.search(textsearch, text))

    #search over data
    def search(self, search, result):
        result.delete('1.0', Tkconstants.END)
        result.insert(Tkconstants.END, self._finalxml.find_spec_cat(search.get('1.0', Tkconstants.END)))
        search.delete('1.0', Tkconstants.END)
        return 'break'

    #parse
    def run_parse(self):
        self._cats = main.run(self._filenamex, self._filenames)
        self._run = False

    #analyze
    def run_stats(self):
        self._finalxml.do_job()
        self._run = False


reload(sys)
sys.setdefaultencoding("utf-8")
top = Tkinter.Tk()
top.wm_title('CATEGORY PARSER')
TkMain(top).pack()
top.mainloop()