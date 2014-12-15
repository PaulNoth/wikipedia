from __future__ import print_function
from lxml import etree as etree
from xmlutils import string_element_tree, fast_iter
import lucene
from lucene import SimpleFSDirectory, System, File, Document, Field, StandardAnalyzer, IndexWriter, Version, \
    IndexSearcher, QueryParser, IndexWriterConfig, IndexReader, RAMDirectory

# getting info from parsed data like categories and how much are they different
class StatisticsFromCats:
    def __init__(self, pages_xml, cats_xml):
        self._pages = pages_xml
        self._cats = cats_xml
        self._out_xml = None
        self._unique = 0
        self._total_cats = 0
        self._total = 0
        self._total_art = 0
        self._titles = []
        self._analyzer = None
        self._dir = None
        self._writer = None
        self._th=None
        self._dick=None

    # openning our parsed data
    def do_job(self):
         with open('temp_output', 'w') as output:
             print('<?xml version="1.0" ?>\n<root>', file=output)
             with open(self._pages) as page:
                 tree_articles = etree.iterparse(page, tag='data', remove_comments=True, remove_blank_text=True)
                 with open(self._cats) as cats:
                     print('working on it')


                     tree_link = etree.iterparse(cats, tag='category_trunc', remove_blank_text=True)
                     dict_link = {}
                     self._dick = {}
                     full_name_dict_link = {}
                     fast_iter(tree_link, self.gethash, dict_link, full_name_dict_link)
                     self.indexFile()
                     fast_iter(tree_articles, self.findcats, output, dict_link, full_name_dict_link, self._dick)
                     print("<total>" + str(self._total_cats) + "</total>", file=output)
                     print("<unique>" + str(self._unique) + "</unique>", file=output)
                     print("<avg>" + str(self._unique / self._total_art) + "</avg>", file=output)
                     print("<onetimeunique>" + str(len(self._dick)) + "</onetimeunique>", file=output)

                     print("</root>", file=output)
                     self._writer.close()

         # with open('temp_output','r') as output:
         #    output = etree.iterparse(output,tag='data',remove_comments=True)
         #    fast_iter(output,self.createIndexing)


    #getting total of cats
    def get_total(self):
        return str(self._total_cats)

    #geting number of unique
    def get_unique(self):
        return  str(self._unique)

     #geting number of unique
    def get_avg(self):
        return str(self._unique / self._total_art)

     #geting number of unique
    def get_one_time_unique(self):
        return str(len(self._dick))


    #search function
    def find_spec_cat(self, title):
        title = title.replace('\n', '')
        return self.query(title)

    #finding categories in ssql based on id form xml
    def findcats(self, elem, output, dict, dict_all, dict_unique):
        id_atr = elem.get('article_id')
        self._total_art += 1
        unique = 0
        content='XML\n'
        try:
            found = dict[id_atr]
        except KeyError:
            found = []
        self._total += 1
        data = etree.Element('data')
        data.set('article_id', id_atr)
        data.set('title', elem.findtext('title'))
        self._titles.append(elem.findtext('title'))
        for catinarticle in elem.findall('category'):
            self._total_cats += 1
            if catinarticle is not None and catinarticle.text is not None:
                try:
                    content+=catinarticle.text+"\n"
                    trimCat = catinarticle.text.replace(' ', '').replace('\'', '')
                    if trimCat not in found:
                        unique += 1
                        dict_unique[trimCat] = 1
                except etree.XPathEvalError:
                    print(catinarticle.text.replace(' ', '').replace('\'', ''))
            sub = etree.SubElement(data, 'category')
            sub.text = catinarticle.text

        content+='\nSQL\n'
        if found:
            for text in dict_all[id_atr]:
                subdat = etree.SubElement(data, 'categoryDat')
                subdat.text = text
                content+=text+"\n"
            del dict[id_atr]
            del dict_all[id_atr]
        print(etree.tostring(data, encoding='utf-8'), file=output)
        self._unique += unique
        self.addElement(elem.findtext('title'),content)


    def gethash(self, elem, dict, full_name_dict_link):
        id_atr = elem.get('article_id')
        try:
            ret = dict[id_atr]
            retfull = dict[id_atr]
        except KeyError:
            ret = []
            retfull = []
        ret.append(elem.text)
        dict[id_atr] = ret
        full_name_dict_link[id_atr] = retfull
        self._total += 1

    def createIndexing(self,elem):
        title=elem.get('title')
        content='XML\n'
        for catinarticle in elem.findall('category'):
            if catinarticle.text is not None:
                content+=catinarticle.text+'\n'
        content+='\nSQL\n'
        for catinsql in elem.findall('categoryDat'):
            if catinsql.text is not None:
                content+=catinsql.text+'\n'
        self.addElement(title,content)

    def indexFile(self):
        self._th=lucene.initVM()
        self._analyzer = StandardAnalyzer(Version.LUCENE_36)
        self._dir = RAMDirectory()
        self._writer = IndexWriter(self._dir, self._analyzer, True, IndexWriter.MaxFieldLength(25000))



    def addElement(self,title,content):
        self._total_art += 1
        doc = Document()
        doc.add(Field("title", title, Field.Store.YES, Field.Index.ANALYZED))
        doc.add(Field("content", content, Field.Store.YES, Field.Index.ANALYZED))
        self._writer.addDocument(doc)



    def query(self,title):
        self._th.attachCurrentThread()
        searcher = IndexSearcher(self._dir)
        query=QueryParser(Version.LUCENE_30, "title", self._analyzer).parse(title)
        total_hits = searcher.search(query, 10)
        for hit in total_hits.scoreDocs:
            doc = (searcher.doc(hit.doc))
            return doc.get("title")+"\n"+doc.get("content")+"--------------------------------"
        return "None"
