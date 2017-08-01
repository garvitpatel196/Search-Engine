/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package searching;

/**
 *
 * @author resources
 */

import java.io.File;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
public class SearchIndex 
{
    IndexSearcher indexSearcher;
    QueryParser queryParser;
    Query query;
    
    public SearchIndex (String indexDirectoryPath) throws Exception
    {
        Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
        indexSearcher = new IndexSearcher(indexDirectory);
        queryParser = new QueryParser(Version.LUCENE_36, webcrawler.LuceneConstants.CONTENTS, new StandardAnalyzer(Version.LUCENE_36));
    }
    
    public TopDocs search (String searchQuery) throws Exception
    {
        query= queryParser.parse(searchQuery);
        return indexSearcher.search(query, 200);
    }
    
    public Document getDocument(ScoreDoc scoreDoc) throws Exception
    {
        return indexSearcher.doc(scoreDoc.doc);
    }
    
    public void close() throws Exception
    {
        indexSearcher.close();
    }
}
