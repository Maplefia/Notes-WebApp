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
import java.util.*;

@WebServlet("/openEditNote.html")
public class UpdateNoteServlet extends HttpServlet
{
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    Model model = ModelFactory.getModel();
    String noteId = request.getParameter("noteId");
    String addedElement = request.getParameter("add");
    boolean editingMode = Boolean.parseBoolean(request.getParameter("editing"));
    boolean savingMode = Boolean.parseBoolean(request.getParameter("saving"));
    boolean deleteNote = Boolean.parseBoolean(request.getParameter("deleteNote"));

    if (deleteNote) {
      List<Note> notes = model.deleteNote(noteId);
      request.setAttribute("notes", notes);
      request.setAttribute("selectedSort", "dateCreated");
      request.setAttribute("selectedFilter", "all");
      request.setAttribute("allCategories", model.getAllCategories());

      ServletContext context = getServletContext();
      RequestDispatcher dispatch = context.getRequestDispatcher("/index.jsp");
      dispatch.forward(request, response);
    } else {
      Note note = model.getNoteContent(noteId);
      List<NoteElement> elements = note.getElements();
      String[] categoriesSelected = request.getParameterValues("category");
      List<String> categoriesSelectedList = categoriesSelected != null ? Arrays.asList(categoriesSelected) : new ArrayList<>();

      if (editingMode) {
        request.setAttribute("editing", true);
      }
      if (addedElement != null) {
        request.setAttribute("editing", true);
        note = model.addElement(noteId, addedElement);
      }
      if (elements != null && !elements.isEmpty()) {
        request.setAttribute("editing", true);
        for (NoteElement element : elements) {
          int elementId = element.getOrderIndex();
          if (Boolean.parseBoolean(request.getParameter("delete_" + elementId))) {
            note = model.deleteElement(noteId, elementId);
          }
          if (savingMode && (request.getParameter("content_" + elementId) != null)) {
            note = model.editElement(noteId, elementId, request.getParameter("content_" + elementId), request.getParameter("title"), categoriesSelectedList);
            model.updateNoteCategories(noteId, categoriesSelectedList);
          }
        }
      }
      if (savingMode) {
        request.setAttribute("editing", false);
      }

      request.setAttribute("categoriesSelected", note.getCategories());
      request.setAttribute("allCategories", model.getAllCategories());
      request.setAttribute("noteContent", note);
      ServletContext context = getServletContext();
      RequestDispatcher dispatch = context.getRequestDispatcher("/noteOpened.jsp");
      dispatch.forward(request, response);
    }
  }
}
