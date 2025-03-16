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
import uk.ac.ucl.model.NoteElement;

import java.io.IOException;

@WebServlet("/showNoteContent.html")
public class NoteContentServlet extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String noteId = request.getParameter("id");
        Model model = ModelFactory.getModel();
        Note note = model.getNoteContent(noteId);

        request.setAttribute("noteContent", note);
        request.setAttribute("editing", false);
        request.setAttribute("categoriesSelected", null);
        request.setAttribute("allCategories", model.getAllCategories());
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/noteOpened.jsp");
        dispatch.forward(request, response);
    }

}
