/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static webcrawler.WebCrawler.indexPath;

/**
 *
 * @author Garvit Patel
 */
public class WebCrawler extends Thread {

    static String path;
    static Thread mainThread;
    public static TreeMap<String, String> tm = new TreeMap<>();
    public static final String Url[] = new String[4];
    static int i = 0;
    public static ArrayList<String> indexPath = new ArrayList<>();

    public WebCrawler() {
        Url[0] = "http://172.16.12.33/index.html";
        Url[1] = "http://172.16.12.35/index.html";
        Url[2] = "http://172.16.12.26/index.html";
        Url[3] = "http://172.16.12.30/index.html";
    }

    public static void makeDirectory(String websiteName) {

        websiteName = websiteName.substring(websiteName.indexOf("/"), websiteName.lastIndexOf("/"));
        websiteName = websiteName.replace("/", "");

        File dir = new File("D:\\Websites\\" + websiteName);

        // attempt to create the directory here
        boolean successful = dir.mkdir();
        if (successful) {
            path = dir.getPath();
            // creating the directory succeeded
            System.out.println("directory was created successfully");
        } else {
            // creating the directory failed
            System.out.println("failed trying to create the directory");
        }
    }

    public static void threadCall() throws InterruptedException {
        WebCrawler wc = new WebCrawler();
        for (i = 0; i < 4; i++) {

            Thread t1 = new Thread(wc, Url[i]);
            makeDirectory(Url[i]);
            t1.start();
            t1.join();
        }
    }

    @Override
    public void run() {
        try {

            Spider spider = new Spider();
            String url = Thread.currentThread().toString();
            url = url.substring(url.indexOf("http"), url.indexOf(","));
            System.out.println("..........." + url);

            spider.search(url, "");
        } catch (IOException ex) {
            Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        threadCall();

        System.out.println("......................." + tm + "........................");
        makeIndex();
        Indexing index = new Indexing();
        index.applyIndexing();
        // spider.search("http://172.16.12.33/index.html", "JAVA");

    }

    public static void makeIndex() {
        for (String temp : Url) {
            System.out.println(temp);
            temp = temp.substring(temp.indexOf("/"), temp.lastIndexOf("/"));
            temp = temp.replace("/", "");
            temp = "D:\\Websites\\" + temp + "\\INDEX";
            //System.out.println(websiteName.get(i));
            indexPath.add(temp);
            File dir = new File(temp);

            // attempt to create the directory here
            boolean successful = dir.mkdir();
            if (successful) {
                path = dir.getPath();
                // creating the directory succeeded
                System.out.println("directory was created successfully");
            } else {
                // creating the directory failed
                System.out.println("failed trying to create the directory");
            }
        }
    }

}

class Indexing {

    public void applyIndexing() {
        System.out.println("ok");
        for (String indexPath1 : indexPath) {
            System.out.println("ok-" + indexPath1);
            Thread t1 = new Thread(indexPath1) {
                @Override
                public void run() {
                    String str = Thread.currentThread().toString();
                    str = str.substring(str.indexOf("["), str.indexOf(","));
                    str = str.replace("[", "");
                    try {
                        index(str);
                    } catch (Exception ex) {
                        Logger.getLogger(Indexing.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("Current Indexing on:" + str);
                }
            };
            t1.start();
            // System.out.println("OKKKKKKKKKKKK");
            //t1.join();
        }
    }

    public void index(String str) throws Exception {
        Indexer indexer;
        String link;
        indexer = new Indexer(str);
        int numIndexed;
        long startTime = System.currentTimeMillis();
        //System.out.println(".............HIIIIII I AM:"+str);
        String dataDir = str.substring(0, str.lastIndexOf("\\"));
        numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
        long endTime = System.currentTimeMillis();
        indexer.close();
        System.out.println(numIndexed + " FileIndexed, Time Taken: " + (endTime - startTime) + " ms");

    }

}
