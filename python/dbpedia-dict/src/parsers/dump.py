from elasticsearch import Elasticsearch
from elasticsearch.helpers import bulk
import elasticsearch
import csv
import sys
import os


def process_operations(es, operations):
    if len(operations):
        bulk(es, operations)


def dump(file_path, language_identifier):
    with open(file_path, "rb") as input_file:               # opening file and setup of es
        es = Elasticsearch(port=9200)
        es_index = 'index'
        es_type = 'd'
        reader = csv.reader(input_file, delimiter=';')
        operations = []
        max_documents = 1000                                # max count of documents
        for row in reader:
            doc_id = row[0]
            try:
                es.get(index=es_index, doc_type=es_type, id=doc_id)   # if index exists, update
                operations.append({
                    '_op_type': 'update',
                    '_index': 'index',
                    '_type': 'd',
                    '_id': row[0],
                    'doc': {
                        'entity': row[0],
                        'data': {
                            language_identifier: {
                                'categories': row[1:]
                            }
                        }
                    }
                })
            except elasticsearch.exceptions.NotFoundError:                 # if not, index is created
                operations.append({
                    '_op_type': 'index',
                    '_index': 'index',
                    '_type': 'd',
                    '_id': row[0],
                    'entity': row[0],
                    'data': {
                        language_identifier: {
                            'categories': row[1:]
                        }
                    }
                })

            if len(operations) > max_documents:             # if max is reached, operations are processed
                bulk(es, operations)
                operations = []
        process_operations(es, operations)                  # if we miss some operation

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print "File not provided"
        exit(1)
    if not os.path.exists(sys.argv[1]):
        print "File not exists"
        exit(1)
    if len(sys.argv) < 3:
        print "Language identifier not provided"
        exit(1)

    dump(sys.argv[1], sys.argv[2])