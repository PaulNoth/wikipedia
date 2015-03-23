import xml.dom.minidom as md
from lxml import etree
# getting string from xml element
def string_element_tree(elem):
    return md.parseString(etree.tostring(elem)).toprettyxml(indent=" ")


#function for faster and memorry less consuming iterations over xml
def fast_iter(context, func, *args, **kwargs):
    """
    http://lxml.de/parsing.html#modifying-the-tree
    Based on Liza Daly's fast_iter
    http://www.ibm.com/developerworks/xml/library/x-hiperfparse/
    See also http://effbot.org/zone/element-iterparse.htm
    """
    for event, elem in context:
        func(elem, *args, **kwargs)
        elem.clear()
        for ancestor in elem.xpath('ancestor-or-self::*'):
            while ancestor.getprevious() is not None:
                del ancestor.getparent()[0]
    del context