package com.example.tripmanager.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public abstract class AbstractController {

    protected Pageable buildPageable(int pageSize, int pageNumber, String sort) {
        return PageRequest.of(pageNumber, pageSize);
    }
}
