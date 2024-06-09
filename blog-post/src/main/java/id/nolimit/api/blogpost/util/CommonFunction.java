package id.nolimit.api.blogpost.util;

import org.springframework.data.domain.Sort;

public class CommonFunction {

    private static final String ASC = "asc";
    private static final String DESC = "desc";

    private CommonFunction() {
        throw new IllegalStateException("CommonFunction class");
    }

    public static Sort.Direction getSortDirection(String sortDirection) {
        if (ASC.equalsIgnoreCase(sortDirection)) {
            return Sort.Direction.ASC;
        } else if (DESC.equalsIgnoreCase(sortDirection)) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }


}
