package com.jovinn.capstoneproject.util;

import com.jovinn.capstoneproject.exception.JovinnException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import static com.jovinn.capstoneproject.util.WebConstant.MAX_PAGE_SIZE;

public class Pagination {
    public static Pageable paginationCommon(int page, int size, String sortBy, String sortDir) {
        validatePageNumberAndSize(page, size);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }

    public static void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new JovinnException(HttpStatus.BAD_REQUEST, "Vị trí trang không được nhỏ hơn 1");
        }
        if (size < 0) {
            throw new JovinnException(HttpStatus.BAD_REQUEST, "Size trang không được nhỏ hơn 0");
        }
        if (size > WebConstant.MAX_PAGE_SIZE) {
            throw new JovinnException(HttpStatus.BAD_REQUEST, "Số lượng phần tử cần nhỏ hơn " + MAX_PAGE_SIZE);
        }
    }
}
