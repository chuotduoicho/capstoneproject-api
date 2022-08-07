package com.jovinn.capstoneproject.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> {
    List<T> content;
    String message;
    int page;
    int size;
    long totalElements;
    int totalPages;
    boolean last;

    public PageResponse(List<T> content, int page, int size, long totalElements, int totalPages, boolean last) {
        setContent(content);
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    public PageResponse(List<T> content, String message, int page, int size, long totalElements, int totalPages, boolean last) {
        setContent(content);
        this.message = message;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    public List<T> getContent() {
        return content == null ? null : new ArrayList<>(content);
    }

    public final void setContent(List<T> content) {
        if (content == null) {
            this.content = null;
        } else {
            this.content = Collections.unmodifiableList(content);
        }
    }

    public boolean isLast() {
        return last;
    }
}
