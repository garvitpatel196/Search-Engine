/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author resources
 */
public class QuerySearch extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/HTML");
            PrintWriter out = response.getWriter();
            ArrayList<String> results = new ArrayList<>();
            String query = request.getParameter("search");
            Searching.Search search = new Searching.Search();
            results = search.searchQuery(query);

            out.println("<html>");
            out.println("<head>");
            out.println("<title>" + "Search Result");
            out.println("</title>");
            out.println("<meta charset=\"UTF-8\">\n"
                    + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            out.println("<link rel=\"stylesheet\" href=\"materialize.min.css\">\n"
                    + "<link href=\"http://fonts.googleapis.com/icon?family=Material+Icons\" rel=\"stylesheet\">\n"
                    + "<script src=\"materialize.min.js\"></script>\n"
                    + "<script type=\"text/javascript\" src=\"jquery-3.2.1.min.js\"></script>");
            out.println("</head>");
            out.println("<body style='background-color:#fafafa;'>");
            out.println("<div class='container'>");
            out.println("<div class='row'>");
            out.println("<h1>Search Results");
            out.println("<h1>");
            out.println("</div>");
            if (results.size() == 0) {
                out.println("<h4>No Results Found</h4>");
            } else {
                for (int i = 0; i < results.size(); i++) {
                    out.println("<div class='row'>");
                    out.println("<div class=\"col s12 m7\">\n"
                            + "    <div class=\"card horizontal\">\n"
                            + "      <div class=\"card-stacked\">\n"
                            + "        <div class=\"card-content\">\n");

                    out.println("<a href='" + results.get(i) + "'>" + results.get(i) + "</a><br>");

                    out.println("</div>\n"
                            + "      </div>\n"
                            + "    </div>\n"
                            + "  </div>");

                    out.println("</div>");
                }
            }
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

        } catch (InterruptedException ex) {
            Logger.getLogger(QuerySearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
