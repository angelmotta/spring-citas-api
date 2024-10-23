package com.angelinux.citasapi.appointment.domain;

import java.util.List;

public record PaginatedResponse<T>(
    List<T> data,
    int currentPage,
    int totalPages,
    long totalItems
) { }
