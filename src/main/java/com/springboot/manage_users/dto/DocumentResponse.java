package com.springboot.manage_users.dto;

public class DocumentResponse {
    private String id;
    private String name;
    private String type;
    private String size;
    private String url;

    public DocumentResponse(String id, String name, String type, String size, String url) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
        this.url = url;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getname() { return name; }
    public void setname(String name) { this.name = name; }

    public String gettype() { return type; }
    public void settype(String type) { this.type = type; }

    public String getsize() { return size; }
    public void setsize(String size) { this.size = size; }

    public String geturl() { return url; }
    public void seturl(String url) { this.url = url; }
}

