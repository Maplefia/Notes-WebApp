package uk.ac.ucl.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class NoteElement {
    private String type;
    private String content;
    private int orderIndex;

    @JsonCreator
    public NoteElement(@JsonProperty("type")String type, @JsonProperty("content")String content, @JsonProperty("orderIndex")int orderIndex) {
        this.type = type;
        this.content = content;
        this.orderIndex = orderIndex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public boolean checkValidUrl(String urlString) {
        try {
            URL url = new URI(urlString).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(1000);
            connection.connect();
            return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkValidImage(String imageString) {
        try {
            URI uri = new URI(imageString);

            if (uri.isAbsolute()) {
                URL url = uri.toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(2000);
                connection.setReadTimeout(2000);

                int responseCode = connection.getResponseCode();
                String contentType = connection.getContentType();

                if (responseCode == HttpURLConnection.HTTP_OK && contentType != null && contentType.startsWith("image/")) {
                    return true;
                }
            } else {
                throw new URISyntaxException(imageString, "URI not absolute");
            }
        } catch (IOException | URISyntaxException | IllegalArgumentException e) {
            return false;
        }
        return false;
    }
}
