from inputparser import CatInputParser
import fileError, sys
from statisticscats import StatisticsFromCats

#Main program for running
def run(file_name_article, file_name_category):
    try:
        cat_input_parser = CatInputParser(file_name_article, file_name_category)
        cat_input_parser.parse_all()
        return cat_input_parser

    except fileError as f:
        print f.error


def get_stats(cat_input_parser):
    return StatisticsFromCats(cat_input_parser._article_xml_output,
                              cat_input_parser._category_link_sql_output_file)


def run_stats(cat_input_parser):
    statitics = StatisticsFromCats(cat_input_parser._article_xml_output,
                                   cat_input_parser._category_link_sql_output_file)
    statitics.do_job()
    return statitics


if __name__ == '__main__':
    reload(sys)
    sys.setdefaultencoding("utf-8")
    out_stats = run_stats(run(sys.argv[1], sys.argv[2]))






