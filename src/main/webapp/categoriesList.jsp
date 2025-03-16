<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="uk.ac.ucl.model.Note" %>
<%@ page import="uk.ac.ucl.model.NoteElement" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="uk.ac.ucl.model.Index" %>

<html>
<head>
  <title>Categories List</title>
  <link href="./noteOpened.css" rel="stylesheet">
</head>
<body>
<% Boolean editMode = (Boolean) request.getAttribute("editCategories"); %>
<form action="/editCategories.html" method="POST">
  <div class="Navbar">
    <div><a href="/">Home</a></div>
      <div><% if (editMode == false) { %>
      <button type="submit" name="editingCategories" value="true">Edit</button>
      <% } else { %>
      <button type="submit" name="savingCategories" value="true">Save</button> <% } %>
    </div>
  </div>
  <div class="noteWrapper">
  <% List<Index> allIndexes = (List<Index>)request.getAttribute("allIndexes");
    if (allIndexes == null || allIndexes.subList(1,allIndexes.size()).isEmpty()) {%>
  <p>No categories so far!</p>
  <% } else { %>
      <h2 class="noteTitleBar">Categories</h2>
      <hr>
      <div class="noteContents">
        <% for (Index index : allIndexes.subList(1,allIndexes.size())) {
          String category = index.getName();
          String id = index.getId();
        %>
        <div class="noteElement">
          <% if (!editMode) { %>
              <div class="noteText">
                <pre><%=category%></pre>
              </div>
          <% } else { %>
            <div class="editElementWrapper">
              <textarea name="edit_<%=id%>" oninput="resizeTextAreas(this)"><%= category %></textarea>
              <button type="submit" name="delete_<%=id%>" value="true" class="deleteElement">Delete Category</button>
            </div>
          <% } %>
        </div>
        <%} }%>
    </div>
    <div class="Buffer"></div>
    <div class="addElements">
      <button type="submit" name="addCategory" value="true">Add Category</button>
    </div>
    </div>
  </form>
</div>
<script>
 function resizeTextAreas(textarea) {
    textarea.style.height = 'auto';
    textarea.style.height = Math.max(((textarea.scrollHeight / window.innerHeight) * 100),2) + 'vh';
 }
 window.onload = function() {
   var textareas = document.querySelectorAll("textarea");
   textareas.forEach(textarea => resizeTextAreas(textarea));
 }
</script>
</body>
</html>

