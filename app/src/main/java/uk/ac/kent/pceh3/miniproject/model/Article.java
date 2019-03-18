package uk.ac.kent.pceh3.miniproject.model;

import com.google.gson.annotations.Expose;


/**
 * Created by pceh3 on 18/03/2019.
 */

public class Article {
    @Expose
    private String title;
    @Expose
    private String dateTime;
    @Expose
    private String articleUrl;
    @Expose
    private String imageUrl;
//    @Expose
//    private ArrayList<Content> content;
//    @Expose
//    private ArrayList<Categories> categories;

    @Expose
    private String content[];
    @Expose
    private String categories[];

    public String[] getContent() {
        return content;
    }

    public void setContent(String[] content) {
        this.content = content;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
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
