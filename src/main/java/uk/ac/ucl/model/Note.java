package uk.ac.ucl.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class Note {
    private String id;
    private String title;
    private List<NoteElement> elements;
    private String createdAt;
    private String updatedAt;
    private List<String> categories;

    @JsonCreator
    public Note(@JsonProperty("id")String id, @JsonProperty("title")String title, @JsonProperty("elements")List<NoteElement> elements, @JsonProperty("createdAt")String createdAt, @JsonProperty("updatedAt")String updatedAt, @JsonProperty("categories")List<String> categories) {
        this.id = id;
        this.title = title;
        this.elements = elements;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.categories = categories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<NoteElement> getElements() {
        return elements;
    }

    public void setElements(List<NoteElement> elements) {
        this.elements = elements;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String findPreview() {
        AtomicReference<String> result = new AtomicReference<>();
        Optional<NoteElement> firstText = getElements().stream().filter(element -> element.getType().equals("text")).findFirst();
        firstText.ifPresentOrElse(
                element-> {
                    result.set(element.getContent().substring(0, Math.min(70, element.getContent().length()))+ "...");
                },
                () -> result.set("Preview Unavailable"));
        return result.get();
    }

    public String findAllContent() {
        String content = new String();
        content = content.concat(getTitle());
        List<NoteElement> elements = getElements();
        if (!elements.isEmpty()) {
            for (NoteElement element : elements) {
                if (element.getType().equals("text")) {
                    content = content.concat(element.getContent());
                }
            }
        }
        return content;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> newCategories) {
        this.categories = newCategories;
    }
}