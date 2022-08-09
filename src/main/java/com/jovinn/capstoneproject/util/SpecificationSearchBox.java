package com.jovinn.capstoneproject.util;

import com.jovinn.capstoneproject.dto.client.boxsearch.BoxSearchRequest;
import com.jovinn.capstoneproject.enumerable.BoxServiceStatus;
import com.jovinn.capstoneproject.model.Box;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationSearchBox {
    public static Specification<Box> getAllActiveBoxWithCategory(BoxSearchRequest request) {
        if(request.getSearchKeyWord() != null) {
            return getActiveBox().and(getBoxByCategory(request)).and(getByKeyWord(request));
        } else {
            return getActiveBox().and(getBoxByCategory(request));
        }
    }

    public static Specification<Box> getAllActiveBoxWithSubCategory(BoxSearchRequest request) {
        if(request.getSearchKeyWord() != null) {
            return getActiveBox().and(getBoxBySubCategory(request)).and(getByKeyWord(request));
        } else {
            return getActiveBox().and(getBoxBySubCategory(request));
        }
    }

    public static Specification<Box> getPrice(BoxSearchRequest request) {
        return (root, query, builder)
                -> builder.between(root.get("fromPrice"), request.getMinPrice(), request.getMaxPrice());
    }

    private static Specification<Box> getBoxByCategory(BoxSearchRequest request) {
        return (root, query, builder)
                -> builder.in(root.get("subCategory").get("category").get("id")).value(request.getCategoryId());
    }

    private static Specification<Box> getBoxBySubCategory(BoxSearchRequest request) {
        return (root, query, builder)
                -> builder.in(root.get("subCategory").get("id")).value(request.getSubCategoryId());
    }

    private static Specification<Box> getActiveBox() {
        return (root, query, builder)
                -> builder.and(root.get("status").in(BoxServiceStatus.ACTIVE));
    }

    private static Specification<Box> getByKeyWord(BoxSearchRequest request) {
        return (root, query, builder) -> builder.like(root.get("title"), "%" + request.getSearchKeyWord() + "%");
    }
}
