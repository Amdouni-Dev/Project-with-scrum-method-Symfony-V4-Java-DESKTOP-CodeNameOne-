package com.espritx.client.entities;

public class ForumPost {

    private int id;
    private static String slug;
    private static String body;

    public ForumPost(String slug, String body) {
        this.slug = slug;
        this.body = body;
    }

    public ForumPost(int id, String slug, String body) {
        this.id = id;
        this.slug = slug;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public static String getSlug() {
        return slug;
    }

    public static String getBody() {
        return body;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ForumPost{" +
                "slug='" + slug + '\'' +
                ", body='" + body + '\'' +
                '}';
    }


}
