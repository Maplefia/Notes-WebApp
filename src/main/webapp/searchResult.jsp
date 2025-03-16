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
  <div><a href="/">Back</a></div>
</div>
<div class="NotesIndexContainer">
  <% List<Note> notes = (List<Note>) request.getAttribute("resultSearch");
    if (notes.isEmpty()) { %>
      <div><h3>No matches found...</h3></div>
    <% } else {
      Integer matches = notes.size();
    %>
      <h3><%=matches%> matches found!</h3>
      <div class="NotesIndexWrapper">
        <% for (Note note : notes) {
          String title = note.getTitle();
          String updatedAt = note.getUpdatedAt();
          String id = note.getId();
          String preview = note.findPreview(); %>
          <div class="NotesIndexElement" onclick="document.getElementById('noteForm<%=id %>').submit()">
            <h3><%=title%></h3>
            <p><%=preview%></p>
            <p>Last Updated at: <%=updatedAt%></p>
          </div>
          <form method="GET" action="/showNoteContent.html" id="noteForm<%=id %>">
            <input type="hidden" name="id" value="<%= id%>"/>
          </form>
      <% } %>
      </div>
    <% }%>
</div>
</body>
</html>
