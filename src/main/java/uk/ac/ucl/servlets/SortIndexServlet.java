package uk.ac.ucl.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;
import uk.ac.ucl.model.Note;


import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// The servlet invoked to perform a search.
// The url http://localhost:8080/runsearch.html is mapped to calling doPost on the servlet object.
// The servlet object is created automatically, you just provide the class.
@WebServlet("/sortNotes.html")
public class SortIndexServlet extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Model model = ModelFactory.getModel();
        String sortBy = request.getParameter("sortBy");
        String filterBy = request.getParameter("filterBy");
        List<Note> notes = model.getNotes();

        if (filterBy != null && !filterBy.equals("all")) {
            notes = notes.stream().filter(note -> note.getCategories().contains(filterBy)).collect(Collectors.toList());
            request.setAttribute("selectedFilter", filterBy);
        } else {
            request.setAttribute("selectedFilter", "all");
        }

        if (sortBy != null) {
            switch(sortBy) {
                case "title":
                    notes.sort(Comparator.comparing(Note::getTitle));
                    break;
                case "dateCreated":
                    notes.sort(Comparator.comparing(Note::getCreatedAt));
                    break;
                case "dateUpdated":
                    notes.sort(Comparator.comparing(Note::getUpdatedAt).reversed());
                    break;
                default:
                    break;
            }
            request.setAttribute("selectedSort", sortBy);
        } else {
            request.setAttribute("selectedSort", "dateCreated");
        }


        request.setAttribute("notes", notes);
        request.setAttribute("allCategories", model.getAllCategories());

        // Invoke the JSP page.
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/index.jsp");
        dispatch.forward(request, response);
    }
}
