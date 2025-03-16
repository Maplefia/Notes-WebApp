package uk.ac.ucl.model;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Model {
  private static final String FILEPATH = "data" + File.separator + "notes.json";
  private static final String INDEXPATH = "data"  + File.separator +"index.json";
  private final ObjectMapper objectMapper = new ObjectMapper();

  private List<Note> readNotesJSON() throws IOException {
    return objectMapper.readValue(new File(FILEPATH), new TypeReference<List<Note>>() {});
  }

  private List<Index> readIndexJSON() throws IOException {
    return objectMapper.readValue(new File(INDEXPATH), new TypeReference<List<Index>>() {});
  }

  private void writeNotesJSON(List<Note> notes) throws IOException {
    objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILEPATH), notes);
  }

  private void writeIndexJSON(List<Index> indexes) throws IOException {
    objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(INDEXPATH), indexes);
  }

  private static String getCurrentTimestamp() {
    return java.time.ZonedDateTime.now().toString();
  }

  public List<Note> getNotes() {
    List<Note> notes = new ArrayList<>();
    try {
      notes = readNotesJSON();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return notes;
  }

  public List<Index> getIndexes() {
    List<Index> indexes = new ArrayList<>();
    try {
      indexes = readIndexJSON();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return indexes;
  }

  public Integer getLocationInMainIndex(String id) throws IOException {
    AtomicReference<Integer> location = new AtomicReference<>(-1);
    List<Index> indexes = getIndexes();
    Optional<Index> indexOptional = indexes.stream().filter(index -> "all".equals(index.getName())).findFirst();

    indexOptional.ifPresentOrElse(
      index -> {
        location.set(index.getNoteIds().indexOf(id));
      },
      () -> {
        System.out.println("Index with name 'all' not found");
      }
    );
    return location.get();
  }

  public Note getNoteContent(String id) {
    List<Note> notes = new ArrayList<>();
    Integer location = -1;
    try {
      notes = readNotesJSON();
      location = getLocationInMainIndex(id);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return location != -1 ? notes.get(location): null;
  }

  public Note editElement(String noteId, Integer elementId, String content, String title, List<String> categories) {
    Note editedNote = null;
    Integer location;
    try {
      List<Note> notes = readNotesJSON();
      location = getLocationInMainIndex(noteId);
      editedNote = notes.get(location);
      if (title != null && !title.trim().isEmpty()) {
        editedNote.setTitle(title);
      }
      editedNote.setCategories(categories);

      Optional<NoteElement> editedElement = editedNote.getElements().stream().filter(element -> element.getOrderIndex() == elementId).findFirst();
      editedElement.ifPresentOrElse(
        element-> {
          if (content != null && !content.trim().isEmpty()) {
            element.setContent(content);
          }
        },
        () -> System.out.println("Element of this id missing..?")
      );
      editedNote.setUpdatedAt(getCurrentTimestamp());
      writeNotesJSON(notes);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return editedNote;
  }

  public Note deleteElement(String noteId, Integer elementId) {
    Note editedNote = null;
    Integer location;
    try {
      List<Note> notes = readNotesJSON();
      location = getLocationInMainIndex(noteId);
      editedNote = notes.get(location);
      List<NoteElement> elements = editedNote.getElements();

      Optional<NoteElement> editedElement = elements.stream().filter(element -> element.getOrderIndex() == elementId).findFirst();
      editedElement.ifPresentOrElse(
              elements::remove,
        () -> System.out.println("Element of this id missing..?")
      );
      editedNote.setUpdatedAt(getCurrentTimestamp());
      writeNotesJSON(notes);

    } catch (IOException e) {
      e.printStackTrace();
    }
    return editedNote;
  }

  public Note addElement(String noteId, String elementType) {
    Note editedNote = null;
    Integer location;
    try {
      List<Note> notes = readNotesJSON();
      location = getLocationInMainIndex(noteId);
      editedNote = notes.get(location);
      List<NoteElement> elements = editedNote.getElements();
      int newOrderIndex = elements.stream().mapToInt(NoteElement::getOrderIndex).max().orElse(0) + 1;

      NoteElement newElement = new NoteElement(elementType, "Replace me with new text...", newOrderIndex);
      elements.add(newElement);
      editedNote.setUpdatedAt(getCurrentTimestamp());
      writeNotesJSON(notes);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return editedNote;
  }

  public Note newNote() {
    Note newNote = null;
    AtomicReference<String> newId = new AtomicReference<>();
    try {
      List<Note> notes = readNotesJSON();
      List<Index> indexes = getIndexes();
      List<NoteElement> elements = new ArrayList<>();
      elements.add(new NoteElement("text", "Replace me with text...", 0));
      Optional<Index> indexOptional = indexes.stream().filter(index -> "all".equals(index.getName())).findFirst();
      indexOptional.ifPresentOrElse(
        index -> {
          newId.set(Integer.toString(index.getNoteIds().stream().mapToInt(Integer::parseInt).max().orElse(0) + 1));
          index.getNoteIds().add(newId.get());
        },
        () -> {
          System.out.println("Index with name 'all' not found");
        }
      );
      newNote = new Note(newId.get(), "Untitled Note", elements, getCurrentTimestamp(),getCurrentTimestamp(), new ArrayList<>());
      notes.add(newNote);
      writeNotesJSON(notes);
      writeIndexJSON(indexes);

    } catch (IOException e) {
      e.printStackTrace();
    }
    return newNote;
  }

  public List<Note> deleteNote(String id) {
    List<Note> notes = null;
    AtomicReference<Integer> location = new AtomicReference<>();
    try {
      notes = readNotesJSON();
      updateNoteCategories(id, new ArrayList<>());
      List<Index> indexes = getIndexes();
      Optional<Index> indexOptional = indexes.stream().filter(index -> "all".equals(index.getName())).findFirst();
      indexOptional.ifPresentOrElse(
        index -> {
          location.set(index.getNoteIds().indexOf(id));
          index.getNoteIds().remove(id);
        },
        () -> {
          System.out.println("Index with name 'all' not found");
        }
      );
      if (location.get() != null) {
        notes.remove((int) location.get());
      }
      writeNotesJSON(notes);
      writeIndexJSON(indexes);

    } catch (IOException e) {
      e.printStackTrace();
    }
    return notes;
  }

  public List<Note> searchFor(String searchQuery) {
    List<Note> allNotes = getNotes();

    Pattern caseInsensitive = Pattern.compile("(?i)" + Pattern.quote(searchQuery).replaceAll(" ", ".*"));
    Pattern exactMatch = Pattern.compile("^" + Pattern.quote(searchQuery) + "$");
    Pattern partialString = Pattern.compile(".*" + Pattern.quote(searchQuery) + ".*");

    Set<Note> matchedNotes = allNotes.stream()
            .filter(note -> caseInsensitive.matcher(note.findAllContent()).find() || exactMatch.matcher(note.findAllContent()).find() || partialString.matcher(note.findAllContent()).find())
            .collect(Collectors.toSet());
    return new ArrayList<>(matchedNotes);
  }

  public List<String> getAllCategories() {
    List<String> allCategories = new ArrayList<>();
    List<Index> indexes = getIndexes();
      allCategories =  indexes.stream()
              .map(Index::getName)
              .filter(name -> !"all".equals(name))
              .collect(Collectors.toList());
    return allCategories;
  }

  public void updateNoteCategories(String noteId, List<String> updatedCategories) {
    try {
      List<Index> indexes = getIndexes();
      for (Index index : indexes.subList(1,indexes.size())) {
        List<String> currentIds = index.getNoteIds();
        if (updatedCategories.contains(index.getName())) {
          if (!currentIds.contains(noteId)) {
            currentIds.add(noteId);
          }
        } else {
            currentIds.remove(noteId);
        }
        index.setNoteIds(currentIds);
      }
      writeIndexJSON(indexes);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void addCategory() {
    try {
      List<Index> indexes = getIndexes();
      String newId = Integer.toString(indexes.stream().mapToInt(index -> Integer.parseInt(index.getId())).max().orElse(0) + 1);
      Index newIndex = new Index("New Index", new ArrayList<>(), newId);
      System.out.println(indexes);
      indexes.add(newIndex);
      System.out.println(indexes);
      writeIndexJSON(indexes);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void editCategory(String indexId, String newName) {
    System.out.println("Received: "+ newName);
    try {
      List<Index> indexes = getIndexes();
      Optional<Index> editedIndex = indexes.stream().filter(index -> index.getId().equals(indexId)).findFirst();
      editedIndex.ifPresentOrElse(
        index -> {
          if (newName != null && !newName.trim().isEmpty()) {
            index.setName(newName);
          }
        },
        () -> System.out.println("Index of this id missing..?")
      );
      writeIndexJSON(indexes);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void deleteCategory(String indexId) {
    try {
      List<Index> indexes = objectMapper.readValue(new File(INDEXPATH), new TypeReference<List<Index>>() {
      });
      Optional<Index> editedIndex = indexes.stream().filter(index -> index.getId().equals(indexId)).findFirst();
      editedIndex.ifPresentOrElse(
      index ->  {
          List<Note> notes = getNotes();
          try {
            for (String noteId : index.getNoteIds()) {
              Note note = notes.get(getLocationInMainIndex(noteId));
              note.getCategories().remove(index.getName());
            }
            writeNotesJSON(notes);
          } catch (IOException e) {
            e.printStackTrace();
          }
          indexes.remove(index);
          },
        () -> System.out.println("Index of this id missing..?")
      );
      writeIndexJSON(indexes);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
