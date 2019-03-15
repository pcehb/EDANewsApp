package uk.ac.kent.pceh3.miniproject.model;

import com.google.gson.annotations.Expose;

/**
 * Created by pceh3 on 13/03/2019.
 */

public class Articles {
    @Expose
    private String description;
    @Expose
    private String title;
    @Expose
    private String dateTime;
    @Expose
    private String articleUrl;
    @Expose
    private String imageUrl;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
