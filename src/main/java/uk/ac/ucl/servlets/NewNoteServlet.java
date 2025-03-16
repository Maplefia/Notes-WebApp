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
import java.util.List;

// The servlet invoked to perform a search.
// The url http://localhost:8080/runsearch.html is mapped to calling doPost on the servlet object.
// The servlet object is created automatically, you just provide the class.
@WebServlet("/newNote.html")
public class NewNoteServlet extends HttpServlet
{
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    Model model = ModelFactory.getModel();
    boolean newNote = Boolean.parseBoolean(request.getParameter("newNote"));
    request.setAttribute("editing", false);

    if (newNote) {
      Note note = model.newNote();
      request.setAttribute("noteContent", note);

      // Invoke the JSP page.
      ServletContext context = getServletContext();
      RequestDispatcher dispatch = context.getRequestDispatcher("/noteOpened.jsp");
      dispatch.forward(request, response);
    }
  }
}
