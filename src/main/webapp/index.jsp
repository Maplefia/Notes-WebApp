<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="uk.ac.ucl.model.Note" %>

<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Notes</title>
  <link href="./mainIndex.css" rel="stylesheet">
</head>
<body>
<div class="Navbar">
  <div class="NavbarLeft">
    <a href="/">Home</a>
    <form method="POST" action="/newNote.html">
      <button type="submit" name="newNote" value="true">New Note</button>
    </form>
    <form method="POST" action="/editCategories.html">
      <button type="submit" name="editCategories" value="true">Edit Categories</button>
    </form>
  </div>
  <div class="NavbarRight">
    <div class="Dropdown">
      <form action="/sortNotes.html" method="POST">
        <select name="filterBy">
          <% List<String> allCategories = (List<String>) request.getAttribute("allCategories");
             String selectedFilter = (String) request.getAttribute("selectedFilter");
             String selectedSort = (String) request.getAttribute("selectedSort"); %>
            <option value="all" <%= "all".equals(selectedFilter) ? "selected" : ""%>>All Notes</option>
            <% for (String category: allCategories) { %>
            <option value="<%=category%>" <%= category.equals(selectedFilter) ? "selected" : ""%>><%=category%></option>
          <% } %>
        </select>
        <select name="sortBy">
          <option value="dateCreated" <%= "dateCreated".equals(selectedSort) ? "selected" : "" %>>Date Created</option>
          <option value="dateUpdated" <%= "dateUpdated".equals(selectedSort) ? "selected" : "" %>>Date Updated</option>
          <option value="title" <%= "title".equals(selectedSort) ? "selected" : "" %>>Title</option>
        </select>
        <button type="submit" class="navbarButton">Filter</button>
      </form>
    </div>
    <div class="SearchBar">
      <form method="POST" action="/searchNotes.html">
        <input type="text" name="searchQuery" placeholder="Search..."/>
      </form>
    </div>
  </div>
</div>
  <div class="NotesIndexContainer">
    <div class="NotesIndexWrapper">
      <% List<Note> notes = (List<Note>) request.getAttribute("notes");
        if (notes.isEmpty()) { %>
          <div><p>Seems a bit empty here...</p></div>
        <% } else {
          for (Note note : notes) {
            String title = note.getTitle();
            String updatedAt = note.getUpdatedAt();
            String id = note.getId();
            String preview = note.findPreview();
            List<String> categories = note.getCategories();
            String categoriesString = String.join(",", categories);
          %>
            <div class="NotesIndexElement" onclick="document.getElementById('noteForm<%=id %>').submit()">
              <h3><%=title%></h3>
              <p><%=preview%></p>
              <p>Last Updated at: <%=updatedAt%></p>
              <% if (!categories.isEmpty()) { %>
                <p>Categories: <%=categoriesString%></p>
              <% } %>
            </div>
            <form method="GET" action="/showNoteContent.html" id="noteForm<%=id %>">
              <input type="hidden" name="id" value="<%= id%>"/>
            </form>
          <% }
        }
      %>
    </div>
  </div>
</div>
</body>
</html>
