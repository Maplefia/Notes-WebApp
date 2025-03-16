<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="uk.ac.ucl.model.Note" %>
<%@ page import="uk.ac.ucl.model.NoteElement" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>

<html>
<head>
  <title>An Open Note</title>
  <link href="./noteOpened.css" rel="stylesheet">
</head>
<body>
<% Boolean editMode = (Boolean) request.getAttribute("editing"); %>
<form action="/openEditNote.html" method="POST">
  <div class="Navbar">
    <div><a href="/">Home</a></div>
      <button type="submit" name="deleteNote" value="true">Delete</button>
      <div><% if (editMode == false) { %>
      <button type="submit" name="editing" value="true">Edit</button>
      <% } else { %>
      <button type="submit" name="saving" value="true">Save</button> <% } %>
    </div>
  </div>
  <div class="noteWrapper">
  <% Note note = (Note) request.getAttribute("noteContent");
    if (note == null) {%>
  <p>No note was found!</p>
  <%} else {
      List<NoteElement> elements = note.getElements();
      String id = note.getId();
      String title = note.getTitle();
      if (editMode == false) { %>
        <h2 class="noteTitleBar"><%=title%></h2>
      <% } else { %>
        <textarea name="title"><%= title %></textarea>
      <% } %>
      <p class="noteTitleBar">Updated At: <%=note.getUpdatedAt()%></p>
      <% List<String> allCategories = (List<String>) request.getAttribute("allCategories");
         List<String> selectedCategories = note.getCategories();
         if (editMode) { %>
           <p class="noteTitleBar">Categories:</p>
           <div class="CategoryWrapper">
           <% for (String category : allCategories) { %>
             <div class="CategoryPicker">
               <label for="<%=category%>"><%=category%></label>
               <input type="checkbox" name="category" value="<%=category%>" <%if (!selectedCategories.isEmpty() && selectedCategories.contains(category)) {
               out.print(" checked"); } %>/>
             </div>
           <% } %>
           </div> <%
         } else { %>
         <p class="noteTitleBar">Categories: <%=selectedCategories%></p>
      <% } %>
      <hr>
      <input type="hidden" name="noteId" value="<%=id%>"/>
      <div class="noteContents">
        <% if (elements != null) {
          for (NoteElement element : elements) {
            String type = element.getType();
            String content = element.getContent();
            int orderIndex = element.getOrderIndex();
        %>
        <input type="hidden" name="elementId" value="<%=orderIndex%>"/>
        <div class="noteElement">
          <% if (editMode == false) {
            if (type.equals("text")) { %>
              <div class="noteText">
                <pre><%=content%></pre>
              </div>
            <% } else if (type.equals("image")) { %>
              <div class ="noteImage">
                <% if (element.checkValidImage(content)) { %>
                  <img src="<%=content%>"/>
                <% } else { %>
                  <p><%=content%> (Image link is invalid or goes against source CORS policy)</p>
                <% } %>
              </div>
            <% } else if (type.equals("url")) { %>
              <div class ="noteUrl">
                <% if (element.checkValidUrl(content)) { %>
                  <a href="<%=content%>" target="_blank" rel="noopener noreferrer"><%=content%></a>
                <% } else { %>
                  <p><%=content%> (This URL may have errors)</p>
                <% } %>
              </div>
            <% }
         } else {
          if (type.equals("text")) { %>
            <div class="editElementWrapper">
              <textarea name="content_<%=orderIndex%>" oninput="resizeTextAreas(this)"><%= content %></textarea>
              <button type="submit" name="delete_<%=orderIndex%>" value="true" class="deleteElement">Delete element</button>
            </div>
          <% } else if (type.equals("image")) { %>
            <div class="editElementWrapper">
              <textarea name="content_<%=orderIndex%>" oninput="resizeTextAreas(this)"><%= content %></textarea>
              <button type="submit" name="delete_<%=orderIndex%>" value="true" class="deleteElement">Delete element</button>
           </div>
          <% } else if (type.equals("url")) { %>
            <div class="editElementWrapper">
              <textarea name="content_<%=orderIndex%>" oninput="resizeTextAreas(this)"><%= content %></textarea>
              <button type="submit" name="delete_<%=orderIndex%>" value="true" class="deleteElement">Delete element</button>
            </div>
          <% }
        } %>
      </div>
    <%}}}%>
    <div class="Buffer"></div>
    </div>
    <div class="addElements">
      <button type="submit" name="add" value="text">Add Text</button>
      <button type="submit" name="add" value="image">Add Image</button>
      <button type="submit" name="add" value="url">Add Url</button>
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

