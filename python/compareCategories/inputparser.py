from __future__ import print_function
import re, os, bz2, magic
from lxml import etree
from lxml import html
import multiprocessing
from fileError import FileError
from xmlutils import fast_iter

# Class for pasting inputs
class CatInputParser:
    # getting names of files
    def __init__(self, article_xml_input, category_links_sql_input):
        if os.path.isfile(article_xml_input):
            self._article_xml_input_file = article_xml_input
            self._article_xml_output = 'output_enwiki_articles.xml'
        else:
            raise FileError(article_xml_input)

        if os.path.isfile(category_links_sql_input):
            self._category_links_sql_input_file = category_links_sql_input
            self._category_link_sql_output_file = 'output_category_links.xml'
        else:
            raise FileError(category_links_sql_input)
        self._total = 0
        self._total_articles = 0

    # parsing article xml
    def parse_xml_article(self, q):
        if 'bzip2' in (magic.from_file(self._article_xml_input_file).lower()):
            input_article_latest_xml = bz2.BZ2File(self._article_xml_input_file, 'r')
        else:
            input_article_latest_xml = open(self._article_xml_input_file)

        with open(self._article_xml_output, 'w') as output:
            print('<?xml version="1.0" ?>\n<root>', file=output)

            with input_article_latest_xml:
                tree = etree.iterparse(input_article_latest_xml, tag='{*}page', remove_comments=True)
                fast_iter(tree, self.process_element, output)

            q.put(self._total_articles)
            print("</root>", file=output)

    # parsing sql categories
    def parse_sql_categories(self, q):
        total_links = 1
        with open(self._category_links_sql_input_file) as input_categories_sql:

            with open(self._category_link_sql_output_file, 'w') as output:
                print('<?xml version="1.0" ?>\n<root>', file=output)

                regex = re.compile('\(([0-9]*?),\'(.*?)\',[^\)]*')
                for line in input_categories_sql:
                    for match in re.findall(regex, line):
                        cat = etree.Element('category_trunc')
                        cat.set('article_id', match[0])
                        cat.text = (match[1].replace('_', ' ').replace(' ', '').replace('\'', '')).decode('utf-8')
                        catf = etree.Element('category')
                        catf.set('article_id', match[0])
                        catf.text = (match[1].replace('_', ' ')).decode('utf-8')
                        print(etree.tostring(cat, encoding='utf-8'), file=output)
                        print(etree.tostring(catf, encoding='utf-8'), file=output)
                        total_links += 1

                q.put(total_links)
                print("</root>", file=output)


    # each parser is in own process
    def parse_all(self):
        que = multiprocessing.Queue()
        jobs = []
        jobs.append(multiprocessing.Process(target=self.parse_sql_categories, args=(que,)))
        jobs.append(multiprocessing.Process(target=self.parse_xml_article, args=(que,)))

        for j in jobs:
            j.start()
            print('started job')

        for j in jobs:
            j.join()
            self._total += que.get()
            print('stopped job')

    #iterator over xml
    def process_element(self, elem, output):
        cat_pa = 'Category'
        ref_pa = '{*}revision/{*}text'
        data = etree.Element('data')
        data.set('article_id', elem.findtext('{*}id'))
        name = etree.SubElement(data, 'title')
        name.text = elem.findtext('{*}title')
        self._total_articles += 1
        for cat in elem.findall(ref_pa):
            if cat.text is not None and cat.text:
                try:
                    parse_text = html.fromstring(cat.text).text_content()
                except etree.ParserError:
                    parse_text = cat.text
                except ValueError:
                    parse_text = ""
                for match in re.findall(r'\[\[' + cat_pa + ':(.*?)\]\]', parse_text):
                    match = match.replace(u'\xa0', ' ').split('|', 1)[0]
                    cat = etree.SubElement(data, 'category')
                    cat.text = match

        print(etree.tostring(data, encoding='utf-8'), file=output)


