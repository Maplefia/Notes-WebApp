package uk.ac.ucl.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Index;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;
import uk.ac.ucl.model.Note;

import java.io.IOException;
import java.util.List;

// The servlet invoked to perform a search.
// The url http://localhost:8080/runsearch.html is mapped to calling doPost on the servlet object.
// The servlet object is created automatically, you just provide the class.
@WebServlet("/editCategories.html")
public class EditCategoriesServlet extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Model model = ModelFactory.getModel();
        boolean editCategories = Boolean.parseBoolean(request.getParameter("editCategories"));
        boolean savingCategories = Boolean.parseBoolean(request.getParameter("savingCategories"));
        boolean addCategory = Boolean.parseBoolean(request.getParameter("addCategory"));
        List<Index> allIndexes= model.getIndexes();

        if (addCategory) {
            request.setAttribute("editCategories", true);
            model.addCategory();
        }
        if (allIndexes != null && !allIndexes.isEmpty()) {
            request.setAttribute("editCategories", true);
            for (Index index : allIndexes) {
                String indexId = index.getId();
                if (Boolean.parseBoolean(request.getParameter("delete_" + indexId))) {
                    model.deleteCategory(indexId);
                }
                if (savingCategories && (request.getParameter("edit_" + indexId) != null)) {
                    model.editCategory(indexId,request.getParameter("edit_" + indexId));
                }
            }
        }
        if (savingCategories) {
            request.setAttribute("editCategories", false);
        }
        request.setAttribute("allIndexes", model.getIndexes());

        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/categoriesList.jsp");
        dispatch.forward(request, response);
    }
}
