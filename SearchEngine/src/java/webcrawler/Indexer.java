/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webcrawler;

/**
 *
 * @author resources
 */
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class Indexer
{
    private IndexWriter writer;
    
    public Indexer(String indexDirectoryPath) throws Exception
    {
        Directory indexDirectory;
        indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
        writer =new IndexWriter(indexDirectory, new StandardAnalyzer(Version.LUCENE_36),true,IndexWriter.MaxFieldLength.UNLIMITED);
    }
    public void close() throws Exception
    {
        writer.close();
    }
    
    public Document getDocument (File file,String link) throws IOException
    {
        Document document = new Document();
        Field contentField = new Field (LuceneConstants.CONTENTS,new FileReader(file));
        Field filePathField = new Field (LuceneConstants.FILE_PATH,link,Field.Store.YES,Field.Index.NOT_ANALYZED);
        
        document.add(contentField);
     
        document.add(filePathField);
        
        return document;
        
    }
    
    public void indexFile(File file,String link) throws Exception
    {
        System.out.println("Indexing "+file.getCanonicalPath());
        Document document = getDocument(file,link);
        writer.addDocument(document);
        file.delete();
    }
    
    public int createIndex (String dataDirPath ,FileFilter filter) throws Exception
    {
       
        File[] files = new File(dataDirPath).listFiles();
        String link;
        for (File file : files)
        {
            if (!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead() && filter.accept(file))
            {
                link=dataDirPath;
               // link=link.substring(link.indexOf("\\")+1);
                link=link.substring(link.lastIndexOf("\\")+1);
               // System.out.println("MY Strrrrrrrrrrrrriiiiingggggggg:"+link);
               // link=link.replace("\\", "/");
                link="http://"+link;
                link=link+"/"+file.getName();
                link=link.substring(0,link.lastIndexOf("."));
                link=link+".html";
                System.out.println("I Am Linkkkkkkkkkkkk:"+link);
                indexFile(file,link);
            }
        }
        return writer.numDocs();
    }
}
