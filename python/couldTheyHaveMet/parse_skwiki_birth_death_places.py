 #!/usr/bin/python3 

import xml.etree.ElementTree as etree
import re
import unicodedata

##check whether input file exists
def check_path(value):
    svalue = value.strip()
    if not os.path.isfile(svalue):
         raise ArgumentTypeError("%s is an invalid file path" % svalue)
    return svalue


if __name__ == '__main__':     
            from argparse import ArgumentParser,ArgumentTypeError
            import string	
            import datetime
            import os
            import socket
            parser = ArgumentParser()
            parser.add_argument("-f", "--file", dest="input_filename",required=True,
                               help="read input from FILE", metavar="FILE", type=check_path)

            args = parser.parse_args()
            entry=''
            title=''
            ##<!--This is a comment-->
            htmlcomments = re.compile('\<![ \r\n\t]*(--([^\-]|[\r\n]|-[^\-])*--[ \r\n\t]*)\>')
            #{{dnv|1964|9|15}}
            dateb1 = re.compile('\{\{[\w ]+\|[0-9 ]*\|[0-9]+\|[0-9]*[|df]*\}\}')
            #{{dúv|1971|11|14|1914|2|12}}
            dated1 = re.compile('\{\{[\w ]+\|[0-9]*\|[0-9]+\|[0-9]+\|[0-9]+\|[0-9]+\|[0-9]+[|]*\}\}')
            #[[935]]
            date2 = re.compile('\[\[[0-9]+\]\]')
            #1958
            date3 = re.compile('[0-9]+[ 0-9]*$')
            #1. storočie
            date4 = re.compile('[\[]*[0-9]+.stor[.ocie]*[\]]*')
            #14. 11. 1971
            date5 = re.compile('[\[]*[0-9]+\.[ ]*[0-9]+\.[ ]*[0-9]+[\]]*')

            print("Celé meno;Dátum narodenia;Dátum úmrtia;Miesto narodenia/úmrtia")
            ##dictionary that translates month names to numbers and trims other words
            tdict = {' r.': '',
'okolo': '',
'po': '',
'asi': '',
'cca': '',
'pred': '',
' kr.': ' B.C.', 
'medzi': '',
'priblizne': '',
'alebo': '',
'neznamy': '',
'roku': '',

'januar': '1.',
'februar': '2.',
'marec': '3.',
'april': '4.',
'maj': '5.',
'jun': '6.',
'jul': '7.',
'august': '8.',
'september': '9.',
'oktober': '10.',
'november': '11.',
'december': '12.',
'/': ' ',
'-': ' ',
'. ': '.',
'nezname': '',
'<br >': '',
' a ': ' '}
            #input is read as xml-structured file
            for event, elem in etree.iterparse(open(args.input_filename, "rb"), events=('start', 'end')): 
              if event == 'end':
                if 'title' in elem.tag:
                  title=elem.text.strip() #store page title to replace {{PAGENAME}} reference in Infobox, usually not needed

                if 'text' in elem.tag and elem.text and 'Infobox' not in title and 'Wikipédia' not in title:
                  birth=''
                  death=''
                  name=''
                  places=set()
                  for line in elem.text.split('\n'):
                   #remove trailing whitespaces and other characters
                    stripped=' '.join(line.strip(' |\t\n\r-,').split())
                    ##translate slovak characters to ascii 
                    norm=unicodedata.normalize('NFKD', stripped.lower()).encode('ascii','ignore').decode('ascii')
                    #remove all whitespaces in string
                    normtrim=norm.replace(" ","").replace("\n","").replace("\r","").replace(" ","")
                    if "meno=" in normtrim and not name:
                       name=htmlcomments.sub('', stripped.split('=')[1]).strip(" '\r\n\t").replace('{{PAGENAME}}',title).replace('?','').replace('<center>','').split('<')[0].strip(' \r\n\t[]')
                    elif not birth and ("datum" in  norm and "narodenia=" in  normtrim) or "narodenie=" in normtrim:
                        value=norm.split('=')[1].strip() 
                        if value:
                          var=htmlcomments.sub('', value).strip().replace('?','') #remove HTML comments
                          for k, v in tdict.items():
                           var = var.replace(k, v)   #use dictionary to translate month names
                          var=var.strip()
                          struct_time=''
                          if "B.C." in var:
                            birth='??' +var  # mark B.C. dates as errors
                          elif dateb1.search(var):
                            birth=dateb1.search(var).group(0).strip('{}').split('|')[1:4]
                            birth[0]="{0:04d}".format(int(birth[0]))
                            try:
                               struct_time = datetime.datetime.strptime(' '.join(birth), "%Y %m %d")
                            except:
                               struct_time = datetime.datetime.strptime(birth[0], "%Y") #day of some dates is >31 or month >21. parse only year
                          elif date2.search(var):
                            birth=date2.search(var).group(0).strip('[]')
                            struct_time = datetime.datetime.strptime("{0:04d}".format(int(birth)), "%Y") # {0:04d} to print year in four digit format
                          elif date3.match(var):
                            struct_time = datetime.datetime.strptime("{0:04d}".format(int(date3.match(var).group(0).split(' ')[0])), "%Y")
                          elif date4.match(var):
                            century=int(date4.match(var).group(0).split('.')[0].strip('[]'))
                            if century>1:
                              struct_time = datetime.datetime.strptime("{0:04d}".format((century-1)*100), "%Y")
                            else:
                              struct_time = datetime.datetime.strptime("{0:04d}".format(1), "%Y") # first century fix. set birth to year 1
                          elif date5.match(var):
                            birth=date5.search(var).group(0).replace(' ','').strip('[] ').split('.')
                            birth[2]="{0:04d}".format(int(birth[2]))
                            struct_time = datetime.datetime.strptime(' '.join(birth), "%d %m %Y")
                          elif var:
                             birth='??' +var
                          if struct_time:
                            birth=(str(struct_time.day) +'.'+ str(struct_time.month) +'.'+ str(struct_time.year)) #store parsed date in dot-delimited format
                             
                    elif ("datum" in norm and "umrtia=" in  normtrim) or "umrtie=" in normtrim:
                        value=norm.split('=')[1].strip()
                       
                        if value != '':
                          var=htmlcomments.sub('', value).strip().replace('?','') #remove HTML comments
                          for k, v in tdict.items():
                           var = var.replace(k, v)   #use dictionary to translate month names
                          var=var.strip()
                          struct_time=''
                          if "B.C." in var:
                            death='??' +var  # mark B.C. dates as errors
                          elif dated1.search(var):
                            birth=dated1.search(var).group(0).strip('{}').split('|')[4:7]
                            birth[0]="{0:04d}".format(int(birth[0]))  # {0:04d} to print year in four digit format
                            try:
                               struct_time = datetime.datetime.strptime(' '.join(birth), "%Y %m %d")
                            except:
                              struct_time = datetime.datetime.strptime(birth[0], "%Y") #day of some dates is >31 or month >21. parse only year
                            birth=(str(struct_time.day) +'.'+ str(struct_time.month) +'.'+ str(struct_time.year)) # birth date can be extracted from this format
                            death=dated1.search(var).group(0).split('|')[1:4]
                            death[0]="{0:04d}".format(int(death[0]))  
                            death=' '.join(death)
                            struct_time = datetime.datetime.strptime(death, "%Y %m %d")
                          elif date2.search(var):
                            death=date2.search(var).group(0).strip('[]')
                            struct_time = datetime.datetime.strptime("{0:04d}".format(int(death)), "%Y").replace(month=12, day=31) # exact day is unknown. set to last day of the year
                          elif date3.match(var):
                            struct_time= datetime.datetime.strptime("{0:04d}".format(int(date3.match(var).group(0).split(' ')[0])), "%Y").replace(month=12, day=31) # exact day is unknown. set to last day of the year
                          elif date4.match(var):
                            struct_time = datetime.datetime.strptime("{0:04d}".format((int(date4.match(var).group(0).split('.')[0].strip('[]'))*100)-1), "%Y").replace(month=12, day=31)
                          elif date5.match(var):
                            death=date5.search(var).group(0).strip('[] ').split('.')
                            death[2]="{0:04d}".format(int(death[2]))
                            struct_time = datetime.datetime.strptime(' '.join(death), "%d %m %Y")
                          elif var:
                             death='??' +var
                          if struct_time:
                             death=(str(struct_time.day) +'.'+ str(struct_time.month) +'.'+ str(struct_time.year))
                             
                    elif stripped.startswith('}}'):
                      break #end of Infobox reached
                    if "miesto" in stripped.lower().split('=')[0] and '=' in stripped:
                          word= stripped.split('=')[1]
                       	  #split string to substrings using special characters 
                       	  chars = ['{','}','[',']','|','(',')',';']
                       	  for char in chars:
                       	  	if char in word:
                       	  		word=word.replace(char,';')  #replace chars by semicolon
                       	  for place in word.split(';'):
                       	     if place and place[0].isupper() and "Súbor:" not in place: #string that starts with uppercase is treated as location
                       	         places.add(place.strip())                       	       
                  if birth or death:
                     if not name:
                        name=title
                     print(name + ';' + birth + ';' + death + ';' + '|'.join(list(places))) #print output
                elem.clear()    #clear current element from memory. vital for large files
