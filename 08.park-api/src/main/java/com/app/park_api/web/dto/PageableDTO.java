package com.app.park_api.web.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PageableDTO {
    private List<?> content = new ArrayList<>();
    private boolean first;
    private boolean last;
    @JsonProperty("page")
    private int number;
    private int size;
    @JsonProperty("page_elements")
    private int numberOfElements;
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("total_elements")
    private int totalElements;
}
