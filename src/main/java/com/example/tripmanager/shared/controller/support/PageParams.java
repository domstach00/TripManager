package com.example.tripmanager.shared.controller.support;

import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class PageParams {
    public static final int MAX_PAGE_SIZE = 1000;
    public static final String SORT_ORDER_DESCENDING = "desc";

    /**
     * Zero based page index (0...N)
     * */
    @Min(0)
    int page = 0;

    /**
     * Size of page to be returned
     * */
    @Min(1)
    int pageSize = 25;

    /**
     * Field to sort on, use comma to separate multiple fields
     * */
    String sortBy;

    /**
     * Direction to sort, using comma to separate multiple fields
     * 'desc' or 'asc'
     * */
    String sortOrder;

    public PageParams() {
    }

    public PageParams(int page, int pageSize) {
        setPage(page);
        setPageSize(pageSize);
    }

    public PageParams(int page, int pageSize, String sortBy, String sortOrder) {
        this(page, pageSize);
        setSortBy(sortBy);
        setSortOrder(sortOrder);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Pageable asPageable() {
        return toPageable(page, pageSize, sortBy, sortOrder);
    }

    public static Pageable toPageable(int page, int pageSize, String sortBy, String sortOrder) {
        return PageRequest.of(page, Math.min(pageSize, MAX_PAGE_SIZE), buildSort(sortBy, sortOrder));
    }

    public static Sort buildSort(String sortBy, String sortOrder) {
        if (sortBy == null || sortBy.isBlank()) {
            return Sort.unsorted();
        }

        final List<Sort.Order> orderList = new ArrayList<>();
        String[] sortByArray = sortBy.trim().split("\\s*[,;:]+\\s*");
        String[] sortOrderArray = sortOrder == null
                ? new String[0]
                : sortOrder.trim().split("\\s*[,;:]+\\s*");
        String currentSortingOrder = "asc";

        for (int i = 0; i < sortByArray.length; i++) {
            String fieldName = sortByArray[i];
            if (i < sortOrderArray.length) {
                currentSortingOrder = sortOrderArray[i];
            }
            orderList.add(mapToOrder(currentSortingOrder, fieldName));
        }
        return Sort.by(orderList);
    }

    private static Sort.Order mapToOrder(String sortOrder, String columnName) {
        if (sortOrder.trim().equalsIgnoreCase(SORT_ORDER_DESCENDING)) {
            return Sort.Order.desc(columnName);
        }
        return Sort.Order.asc(columnName);
    }
}
