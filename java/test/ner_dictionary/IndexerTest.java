package ner_dictionary;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import ner_dictionary.indexing.Indexer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests if the indexer can be instantiated correctly.
 *
 */
public class IndexerTest {

	@Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
	
	@Test
	public void testIndexer() throws IOException {
		File indexDirectory = testFolder.newFolder();
		String indexDirectoryName = indexDirectory.getPath();
		Indexer indexer = new Indexer(indexDirectoryName);
		assertNotNull(indexer.getIndexWriter());
	}

}
