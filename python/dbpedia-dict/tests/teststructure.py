import re
import sys


def check(fr):
    f = open(fr, "r")
    flag = 1
    count = 0
    for line in f:
        if re.match('^#.*$', line):
            count += 1
            if flag:
                print("Beginning found")
                flag = 0
            else:
                print("Ending found")
                print("[OK] Let's use the file!")
        elif not re.match(r'^<http://.*>\s+<http://.*>\s+<http://.*>\s+\.$', line):
            print("[ERR] File's not valid. Mistake on line number:")
            print(count)
            exit(0)
        count += 1
    f.close()

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print "File not provided"
        exit(1)
    check(sys.argv[1])