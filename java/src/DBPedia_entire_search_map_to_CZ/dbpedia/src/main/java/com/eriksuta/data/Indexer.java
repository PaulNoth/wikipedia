package com.eriksuta.data;

import com.eriksuta.data.index.IndexAlgorithm;
import com.eriksuta.data.index.LinkIndexAlgorithm;
import com.eriksuta.data.index.PropertyIndexAlgorithm;
import com.eriksuta.data.index.SimpleIndexAlgorithm;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.*;
import java.util.Date;

/**
 *  @author Erik Suta
 * */
public class Indexer {

    private static final Logger LOGGER = Logger.getLogger(Indexer.class);

    public static final String INDEX_DIR = "src/main/webapp/parsed/index";

    public static final String DATA_DIR = "src/main/webapp/parsed/";
    public static final String ARTICLE_CATEGORIES_EN_URIS_SK = DATA_DIR + "article_categories_en_uris_sk.txt";
    public static final String ARTICLE_CATEGORIES_SK = DATA_DIR + "article_categories_sk.txt";
    public static final String EXTERNAL_LINKS_SK = DATA_DIR + "external_links_sk.txt";
    public static final String FREEBASE_LINKS_SK = DATA_DIR + "freebase_links_sk.txt";
    public static final String INFOBOX_PROPERTIES_SK = DATA_DIR + "infobox_properties_sk.txt";
    public static final String INTER_LANGUAGE_LINKS = DATA_DIR + "interlanguage_links_sk.txt";
    public static final String LABELS_SK = DATA_DIR + "labels_sk.txt";
    public static final String LONG_ABSTRACTS_SK = DATA_DIR + "long_abstracts_sk.txt";
    public static final String OUT_DEGREE_SK = DATA_DIR + "out_degree_sk.txt";
    public static final String PAGE_IDS_SK = DATA_DIR + "page_ids_sk.txt";
    public static final String PAGE_LENGTH_SK = DATA_DIR + "page_length_sk.txt";
    public static final String PAGE_LINKS_SK = DATA_DIR + "page_links_sk.txt";
    public static final String PAGE_LINKS_UNREDIRECTED_SK = DATA_DIR + "page_links_unredirected_sk.txt";
    public static final String REDIRECTS_SK = DATA_DIR + "redirects_sk.txt";
    public static final String REDIRECTS_TRANSITIVE_SK = DATA_DIR + "redirects_transitive_sk.txt";
    public static final String REVISION_IDS_SK = DATA_DIR + "revision_ids_sk.txt";
    public static final String REVISION_URIS_SK = DATA_DIR + "revision_uris_sk.txt";
    public static final String SHORT_ABSTRACTS_SK = DATA_DIR + "short_abstracts_sk.txt";
    public static final String WIKIPEDIA_LINKS_SK = DATA_DIR + "wikipedia_links_sk.txt";
    public static final String SKOS_CATEGORIES_EN_URIS_SK = DATA_DIR + "skos_categories_en_uris_sk.txt";
    public static final String SKOS_CATEGORIES_SK = DATA_DIR + "skos_categories_sk.txt";

    public static final String EXTERNAL_LINKS_CZ = DATA_DIR + "external_links_cs.txt";
    public static final String FREEBASE_LINKS_CZ = DATA_DIR + "freebase_links_cs.txt";
    public static final String INTER_LANGUAGE_LINKS_CZ = DATA_DIR + "interlanguage_links_cs.txt";
    public static final String PAGE_LINKS_CZ = DATA_DIR + "page_links_cs.txt";
    public static final String PAGE_LINKS_UNREDIRECTED_CZ = DATA_DIR + "page_links_unredirected_cs.txt";
    public static final String WIKIPEDIA_LINKS_CZ = DATA_DIR + "wikipedia_links_cs.txt";
    public static final String REDIRECTS_CZ = DATA_DIR + "redirects_cs.txt";
    public static final String REDIRECTS_TRANSITIVE_CZ = DATA_DIR + "redirects_transitive_cs.txt";

    private Directory directory;
    private Analyzer analyzer;
    private static IndexWriter indexWriter;

    /**
     *  Constructor - performs necessary initialization
     * */
    public Indexer(){
        initIndexer();
    }

    public void initIndexer(){
        analyzer = new StandardAnalyzer();

        try {
            directory = FSDirectory.open(new File(INDEX_DIR));

            if(indexWriter == null){
                IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);
                indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
                indexWriter = new IndexWriter(directory, indexWriterConfig);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void performIndexing(){
        IndexAlgorithm algorithm;

        System.out.println("Starting indexing process, at: " + new Date());
        LOGGER.info("Starting indexing process, at: " + new Date());
        long startTime = System.currentTimeMillis();

        try {

            //Index Categories
            algorithm = new SimpleIndexAlgorithm(IndexLabelNames.ARTICLE_CATEGORY_LABEL, IndexLabelNames.ARTICLE_CATEGORY_CONTENT);
            createIndexes(new File(ARTICLE_CATEGORIES_SK), algorithm);
            createIndexes(new File(ARTICLE_CATEGORIES_EN_URIS_SK), algorithm);

            //External Links
            algorithm = new LinkIndexAlgorithm(IndexLabelNames.EXTERNAL_LINK_LABEL, IndexLabelNames.EXTERNAL_LINK_CONTENT);
            createIndexes(new File(EXTERNAL_LINKS_SK), algorithm);

            //Freebase Links
            algorithm = new LinkIndexAlgorithm(IndexLabelNames.FREEBASE_LINK_LABEL, IndexLabelNames.FREEBASE_LINK_CONTENT);
            createIndexes(new File(FREEBASE_LINKS_SK), algorithm);

            //Interlanguage Links
            algorithm = new LinkIndexAlgorithm(IndexLabelNames.INTER_LANGUAGE_LINKS_LABEL, IndexLabelNames.INTER_LANGUAGE_LINKS_CONTENT);
            createIndexes(new File(INTER_LANGUAGE_LINKS), algorithm);

            //Page Links
            algorithm = new LinkIndexAlgorithm(IndexLabelNames.PAGE_LINKS_LABEL, IndexLabelNames.PAGE_LINKS_CONTENT);
            createIndexes(new File(PAGE_LINKS_SK), algorithm);

            //Page Links - Unredirected
            algorithm = new LinkIndexAlgorithm(IndexLabelNames.PAGE_LINKS_UNREDIRECTED_LABEL, IndexLabelNames.PAGE_LINKS_UNREDIRECTED_CONTENT);
            createIndexes(new File(PAGE_LINKS_UNREDIRECTED_SK), algorithm);

            //Wikipedia Links
            algorithm = new SimpleIndexAlgorithm(IndexLabelNames.WIKIPEDIA_LINKS_LABEL, IndexLabelNames.WIKIPEDIA_LINKS_CONTENT);
            createIndexes(new File(WIKIPEDIA_LINKS_SK), algorithm);

            //Infobox Properties
            algorithm = new PropertyIndexAlgorithm(IndexLabelNames.INFOBOX_PROPERTIES_LABEL, IndexLabelNames.INFOBOX_PROPERTIES_CONTENT);
            createIndexes(new File(INFOBOX_PROPERTIES_SK), algorithm);

            //Labels
            algorithm = new SimpleIndexAlgorithm(IndexLabelNames.LABEL_LABEL, IndexLabelNames.LABEL_CONTENT);
            createIndexes(new File(LABELS_SK), algorithm);

            //Long Abstracts
            algorithm = new SimpleIndexAlgorithm(IndexLabelNames.LONG_ABSTRACT_LABEL, IndexLabelNames.LONG_ABSTRACT_CONTENT);
            createIndexes(new File(LONG_ABSTRACTS_SK), algorithm);

            //Short Abstracts
            algorithm = new SimpleIndexAlgorithm(IndexLabelNames.SHORT_ABSTRACT_LABEL, IndexLabelNames.SHORT_ABSTRACT_CONTENT);
            createIndexes(new File(SHORT_ABSTRACTS_SK), algorithm);

            //Out Degree
            algorithm = new SimpleIndexAlgorithm(IndexLabelNames.OUT_DEGREE_LABEL, IndexLabelNames.OUT_DEGREE_CONTENT);
            createIndexes(new File(OUT_DEGREE_SK), algorithm);

            //Page IDs
            algorithm = new SimpleIndexAlgorithm(IndexLabelNames.PAGE_ID_LABEL, IndexLabelNames.PAGE_ID_CONTENT);
            createIndexes(new File(PAGE_IDS_SK), algorithm);

            //Page Length
            algorithm = new SimpleIndexAlgorithm(IndexLabelNames.PAGE_LENGTH_LABEL, IndexLabelNames.PAGE_LENGTH_CONTENT);
            createIndexes(new File(PAGE_LENGTH_SK), algorithm);

            //Redirects
            algorithm = new LinkIndexAlgorithm(IndexLabelNames.REDIRECTS_LABEL, IndexLabelNames.REDIRECTS_CONTENT);
            createIndexes(new File(REDIRECTS_SK), algorithm);

            //Redirects - Transitive
            algorithm = new LinkIndexAlgorithm(IndexLabelNames.REDIRECTS_TRANSITIVE_LABEL, IndexLabelNames.REDIRECTS_TRANSITIVE_CONTENT);
            createIndexes(new File(REDIRECTS_TRANSITIVE_SK), algorithm);

            //Revision IDs
            algorithm = new SimpleIndexAlgorithm(IndexLabelNames.REVISION_ID_LABEL, IndexLabelNames.REVISION_ID_CONTENT);
            createIndexes(new File(REVISION_IDS_SK), algorithm);

            //Revision URIs
            algorithm = new LinkIndexAlgorithm(IndexLabelNames.REVISION_URI_LABEL, IndexLabelNames.REVISION_URI_CONTENT);
            createIndexes(new File(REVISION_URIS_SK), algorithm);

            //Skos Categories
            algorithm = new SimpleIndexAlgorithm(IndexLabelNames.SKOS_CATEGORIES_LABEL, IndexLabelNames.SKOS_CATEGORIES_CONTENT);
            createIndexes(new File(SKOS_CATEGORIES_SK), algorithm);
            createIndexes(new File(SKOS_CATEGORIES_EN_URIS_SK), algorithm);

            //External Links - CZ
            algorithm = new LinkIndexAlgorithm(IndexLabelNames.EXTERNAL_LINK_LABEL_CZ, IndexLabelNames.EXTERNAL_LINK_CONTENT_CZ);
            createIndexes(new File(EXTERNAL_LINKS_CZ), algorithm);

            //Freebase Links - CZ
            algorithm = new LinkIndexAlgorithm(IndexLabelNames.FREEBASE_LINK_LABEL_CZ, IndexLabelNames.FREEBASE_LINK_CONTENT_CZ);
            createIndexes(new File(FREEBASE_LINKS_CZ), algorithm);

            //Interlanguage Links - CZ
            algorithm = new LinkIndexAlgorithm(IndexLabelNames.INTER_LANGUAGE_LINKS_LABEL_CZ, IndexLabelNames.INTER_LANGUAGE_LINKS_CONTENT_CZ);
            createIndexes(new File(INTER_LANGUAGE_LINKS_CZ), algorithm);

            //Page Links - CZ
            algorithm = new LinkIndexAlgorithm(IndexLabelNames.PAGE_LINKS_LABEL_CZ, IndexLabelNames.PAGE_LINKS_CONTENT_CZ);
            createIndexes(new File(PAGE_LINKS_CZ), algorithm);

            //Page Links - Unredirected - CZ
            algorithm = new LinkIndexAlgorithm(IndexLabelNames.PAGE_LINKS_UNREDIRECTED_LABEL_CZ, IndexLabelNames.PAGE_LINKS_UNREDIRECTED_CONTENT_CZ);
            createIndexes(new File(PAGE_LINKS_UNREDIRECTED_CZ), algorithm);

            //Wikipedia Links - CZ
            algorithm = new SimpleIndexAlgorithm(IndexLabelNames.WIKIPEDIA_LINKS_LABEL_CZ, IndexLabelNames.WIKIPEDIA_LINKS_CONTENT_CZ);
            createIndexes(new File(WIKIPEDIA_LINKS_CZ), algorithm);

            //Redirects - CZ
            algorithm = new LinkIndexAlgorithm(IndexLabelNames.REDIRECTS_LABEL_CZ, IndexLabelNames.REDIRECTS_CONTENT_CZ);
            createIndexes(new File(REDIRECTS_CZ), algorithm);

            //Redirects - Transitive - CZ
            algorithm = new LinkIndexAlgorithm(IndexLabelNames.REDIRECTS_TRANSITIVE_LABEL_CZ, IndexLabelNames.REDIRECTS_TRANSITIVE_CONTENT_CZ);
            createIndexes(new File(REDIRECTS_TRANSITIVE_CZ), algorithm);

            indexWriter.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Indexing process finished after: " + (endTime - startTime) + " milliseconds, resp.: " + (endTime - startTime)/1000.0 + " seconds.");
        LOGGER.info("Indexing process finished after: " + (endTime - startTime) + " milliseconds, resp.: " + (endTime - startTime)/1000.0 + " seconds.");
    }

    public void createIndexes(File file, IndexAlgorithm algorithm) throws IOException {
        System.out.println("Creating indexes for: " + file.getName());

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        while((line = bufferedReader.readLine()) != null){
            if(line.isEmpty()){
                continue;
            }

            algorithm.createSimpleIndex(line, indexWriter);
        }

        bufferedReader.close();
        fileReader.close();
    }

    public Directory getDirectory() {
        return directory;
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }
}
