/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Searching;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

/**
 *
 * @author resources
 */
public class Search {

    public static ArrayList searchQuery(String QUERY) throws IOException, InterruptedException {
        ArrayList<String> results = new ArrayList<>();
        ArrayList<String> dirPath = new ArrayList<>();
        File[] dir = new File("D:\\Websites").listFiles();
        for (File name : dir) {
            if (name.isDirectory()) {
                dirPath.add(name.getCanonicalPath());
            }
        }
        System.out.println("Length:" + dirPath.size());
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < dirPath.size(); i++) {
            Thread t1 = new Thread(dirPath.get(i)) {
                @Override
                public void run() {
                    try {
                        webcrawler.Search searcher;
                        String str = Thread.currentThread().toString();
                        str = str.substring(str.indexOf("["), str.indexOf(","));
                        str = str.replace("[", "");
                        searcher = new webcrawler.Search(str + "\\INDEX");

                        TopDocs hits = searcher.search(QUERY);

                        // System.out.println(hits.totalHits + " document found. Time: "+(endTime-startTime));
                        for (ScoreDoc soreDoc : hits.scoreDocs) {
                            Document doc = searcher.getDocument(soreDoc);
                            String temp=doc.get(webcrawler.LuceneConstants.FILE_PATH);
                            System.out.println("link: " + temp);
                            results.add(temp);
                        }
                        searcher.close();
                        //  searcher = new SearchIndex(indexDir1);
                        //startTime=System.currentTimeMillis();
                        //  hits = searcher.search(searchQuery);

                    } catch (Exception ex) {
                        Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            t1.start();
            t1.join();

        }
        long endTime = System.currentTimeMillis();

        System.out.println(" document found. Time: " + (endTime - startTime)+"ms");
        return results;
    }
    //public static void() 
}
