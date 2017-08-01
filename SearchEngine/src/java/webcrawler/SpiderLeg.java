package webcrawler;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderLeg {

    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT
            = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links = new LinkedList<>();
    private Document htmlDocument;

    /**
     * This performs all the work. It makes an HTTP request, checks the
     * response, and then gathers up all the links on the page. Perform a
     * searchForWord after the successful crawl
     *
     * @param url - The URL to visit
     * @return whether or not the crawl was successful
     * @throws java.lang.InterruptedException
     */
    public boolean crawl(String url) throws InterruptedException {
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            String temp = url;
            String path = url;

            path = path.substring(path.indexOf("/"), path.lastIndexOf("/"));
            path = path.replace("/", "");
            System.out.println("path:" + path);
            temp = temp.substring(temp.lastIndexOf("/"), temp.lastIndexOf("."));
            temp = temp.replace("/", "_");
            temp = temp.replace("_", "");
            //System.out.println("..................." + temp);
            temp = "D:\\Websites\\" + path + "\\" + temp + ".txt";
            System.out.println("Temp:" + temp);
            WebCrawler.tm.put(url, temp);
            this.htmlDocument = htmlDocument;
            if (connection.response().statusCode() == 200) // 200 is the HTTP OK status code
            // indicating that everything is great.
            {
                //System.out.println("\n**Visiting** Received web page at " + url);
            }
            if (!connection.response().contentType().contains("text/html")) {
                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }
            Elements linksOnPage = htmlDocument.select("a");

            //System.out.println("Found (" + linksOnPage.size() + ") links");
            for (Element link : linksOnPage) {
                String link1 = link.attr("href");
                Thread t1 = new Thread() {
                    @Override
                    public void run() {

                        Spider spider = new Spider();
                        try {

                            spider.search(link1, "");
                        } catch (IOException ex) {
                            Logger.getLogger(SpiderLeg.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SpiderLeg.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                };
                t1.start();
                t1.join();
                System.out.println(link);
                this.links.add(link.absUrl("href"));
            }
            return true;
        } catch (IOException ioe) {
            // We were not successful in our HTTP request
            return false;
        }
    }

    /**
     * Performs a search on the body of on the HTML document that is retrieved.
     * This method should only be called after a successful crawl.
     *
     * @param searchWord - The word or string to look for
     * @return whether or not the word was found
     */
    public boolean searchForWord(String searchWord, String urlString) throws IOException {
        // Defensive coding. This method should only be used after a successful crawl.
        if (this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return false;
        }
        String path = urlString;

        path = path.substring(path.indexOf("/"), path.lastIndexOf("/"));
        path = path.replace("/", "");
        System.out.println("path:" + path);
        System.out.println("Searching for the word " + searchWord + "...");
        String bodyText = this.htmlDocument.body().text();
        String originalUrl = urlString;
        urlString = urlString.substring(urlString.lastIndexOf("/"), urlString.lastIndexOf("."));
        urlString = urlString.replace("/", "");
        //System.out.println("..................." + urlString);
        urlString = "D:\\Websites\\"+path+"\\"+ urlString + ".txt";
        File file = new File(urlString);

        // creates the file
        file.createNewFile();

        // creates a FileWriter Object
        FileWriter writer = new FileWriter(file);

        // Writes the content to the file
        writer.write(bodyText);
        writer.flush();
        writer.close();
        // Creates a FileReader Object
        FileReader fr = new FileReader(file);
        char[] a = new char[50];
        fr.read(a);   // reads the content to the array

        for (char c : a) {
            System.out.print(c);   // prints the characters one by one
        }
        fr.close();

        //System.out.println(bodyText);
        return bodyText.toLowerCase().contains(searchWord.toLowerCase());
    }

    public List<String> getLinks() {
        return this.links;
    }

}
