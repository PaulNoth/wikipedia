import re
import sys

skarr = {"Trnava": ["Trnava", "Cities_and_towns_in_Slovakia", "Trnava_Region"], "Virginia_Woolf": ["1882_births", "1941_deaths", "English_writers", "Suicides", "British_feminists"], "Vyacheslav_Molotov": ["1890_births", "1986_deaths", "Soviet_politicians", "Heads_of_government_of_the_Soviet_Union", "Soviet_people_of_World_War_II"]}
enarr = {"Hour": ["Orders_of_magnitude_(time)", "Units_of_time"], "Intel_80286": ["1982_introductions", "Intel_x86_microprocessors"], "Relay_league": ["History_of_telecommunications", "Telegraphy"]}
dearr = {"Widawa": ["Fluss_in_Europa", "Fluss_in_Polen", "Flusssystem_Oder", "Geographie_(Woiwodschaft_Niederschlesien)"], "Bratsche":["Bratsche", "Streichinstrument", "Chordophon"], "Margret": "Weiblicher_Vorname"}


def check(lang, source, array):
    if lang == 'sk':
        if skarr[source] <= array:
            print("SK OK")
            exit(0)
    elif lang == 'en':
        if enarr[source] <= array:
            print("EN OK")
            exit(0)
    elif lang == 'de':
        if dearr[source] <= array:
            print("DE OK")
            exit(0)

def test(lang):
    try:
        f = open("../data/sample_categories_" + lang + ".ttl", "r")
    except IOError:
        print("File doesn't exist!")
        exit(1)

    re.compile('<http://>')                         # main delimiter in .ttl files
    for line in f:
        if re.match('^#.*$', line):
            continue
        res = line.split()
        rd = res[0].split('/')                      # delimit by / inside first <http://> entity
        cd = res[2].split(':')                      # delimit by : inside third <http://> entity
        resource = rd[len(rd)-1][:-1]               # string before last / inside first <http://> entity
        category = cd[len(cd)-1][:-1]               # string before last : inside third <http://> entity

        if lang == 'sk':
            if resource == "Trnava" or resource == "Virginia_Woolf" or resource == "Vyacheslav_Molotov":    # test condition
                if d.get(resource) is None:
                    d[resource] = [category]
                else:
                    d[resource].append(category)
                check(lang, resource, d.get(resource))

        if lang == 'en':
            if resource == "Hour" or resource == "Intel_80286" or resource == "Relay_league":    # test condition
                if d.get(resource) is None:
                    d[resource] = [category]
                else:
                    d[resource].append(category)
                check(lang, resource, d.get(resource))

        if lang == 'de':
            if resource == "Widawa" or resource == "Bratsche" or resource == "Margret":    # test condition
                if d.get(resource) is None:
                    d[resource] = [category]
                else:
                    d[resource].append(category)
                check(lang, resource, d.get(resource))
    print("ERR")

if __name__ == "__main__":
    d = dict()
    d.clear()
    test(sys.argv[1])                               # testing method(language choose[sk,en,de])