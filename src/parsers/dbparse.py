import re
import sys
import csv
import unicodecsv


def parse(path, mode):
    try:
        if mode == 'full':
            f = open('../data/categories_' + path + '.ttl', "r")
        elif mode == 'sample':
            f = open('../data/sample_categories_' + path + '.ttl', "r")
    except IOError:
        print("File doesn't exist!")
        exit(1)

    re.compile('<http://>')      # main delimiter in .ttl files
    entityarr = []
    with open("../data/" + mode + "_out_" + path + ".csv", "wb+") as output:
        spamwriter = unicodecsv.writer(output, delimiter=';', quotechar='|', quoting=csv.QUOTE_MINIMAL, encoding='utf-8')
        previous = None
        for line in f:
            if re.match('^#.*$', line):         # skipping beginning and ending
                continue
            res = line.split()
            rd = res[0].split('/')              # delimit by / inside first <http://> entity
            cd = res[2].split(':')              # delimit by : inside third <http://> entity
            resource = rd[len(rd)-1][:-1]       # string before last / inside first <http://> entity
            category = cd[len(cd)-1][:-1]       # string before last : inside third <http://> entity
            current = resource
            if previous == current or not previous:
                entityarr.append(category)
                previous = current
            else:
                spamwriter.writerow([previous] + entityarr)
                previous = None
                entityarr = []

if __name__ == "__main__":
    d = dict()
    parse(sys.argv[1], sys.argv[2])         # parsing method(language choose[sk,en,de],file mode[sample,full])