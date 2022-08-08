package com.jovinn.capstoneproject.dto.client.boxsearch;

import com.jovinn.capstoneproject.dto.PageResponse;

import java.util.List;

public class ListBoxSearchResponse extends PageResponse<BoxSearchResponse> {
    public ListBoxSearchResponse(List<BoxSearchResponse> content, String message,
                                 int page, int size, long totalElements, int totalPages, boolean last) {
        super(content, message, page, size, totalElements, totalPages, last);
    }
}
