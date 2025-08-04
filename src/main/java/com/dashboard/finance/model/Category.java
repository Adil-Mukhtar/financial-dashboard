package com.dashboard.finance.model;

public class Category {
    private Long id;
    private String name;
    private String type; // INCOME or EXPENSE
    private String color;

    // Constructors
    public Category() {}

    public Category(String name, String type, String color) {
        this.name = name;
        this.type = type;
        this.color = color;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}