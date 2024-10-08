package com.example.chapter.entity;

public enum Category {
    FICTION("소설"),
    HISTORY("역사"),
    SCIENCE("과학"),
    PHILOSOPHY("철학"),
    BUSINESS("경제/경영"),
    POETRY("시"),
    TRAVEL("여행");

    private final String categoryName;

    Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
