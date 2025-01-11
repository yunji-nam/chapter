package com.example.chapter.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class CustomPageImpl<T> extends PageImpl<T> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CustomPageImpl(@JsonProperty("content") List<T> content,
                          @JsonProperty("number") int pageNo,
                          @JsonProperty("size") int size,
                          @JsonProperty("totalElements") long total) {
        super(content, PageRequest.of(pageNo, size), total);
    }

    public CustomPageImpl(Page<T> page) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
    }
}
