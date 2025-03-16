package uk.ac.ucl.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Index {
    private String indexName;
    private List<String> noteIds;
    private String id;

    @JsonCreator
    public Index(@JsonProperty("name")String indexName, @JsonProperty("notes")List<String> noteIds, @JsonProperty("id")String id) {
        this.indexName = indexName;
        this.noteIds = noteIds;
        this.id = id;
    }

    public String getName() {
        return indexName;
    }
    public void setName(String newName) {
        this.indexName = newName;
    }

    public List<String> getNoteIds() {
        return noteIds;
    }
    public void setNoteIds(List<String> newContents) {
        this.noteIds = newContents;
    }

    public String getId() {
        return id;
    }

    public void setId(String newId) {
        this.id = newId;
    }
}
