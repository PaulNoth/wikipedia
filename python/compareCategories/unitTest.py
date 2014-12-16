import unittest
import filecmp
import main
#unittest, Runs parser on sample files provided with this project. After parsing them, he chceks for the equality, if they are not equal, exception is raised.
#As a sample input files, the sql with categorylinks from wikipedia is provided, note , this program was made for en wiki, but simple change can make it compatible with any.
#As a second simple input is choosen articles xml. Outputs have form of xml structured files.
class OutputTasteCase(unittest.TestCase):
    def test_output(self):
        self.assertEqual(True, filecmp.cmp('data/sample_output_enwiki-latest-categorylinks.xml', 'output_category_links.xml'))
        self.assertEqual(True, filecmp.cmp('data/sample_output_enwiki-latest-pages.xml', 'output_enwiki_articles.xml'))


if __name__ == '__main__':
    filenames = 'data/sample_categories_input-enwiki-latest-categorylinks.sql'
    filenamex = 'data/sample_categories_input-enwiki-latest-pages.xml'
    cats = main.run(filenamex, filenames)
    main.run_stats(cats)
