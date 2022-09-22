package com.kh.heallo.domain.review;

public enum OrderBy {
    DATE_ASC("rvcdate asc"), DATE_DESC("rvcdate desc"), SCORE_DSC("rvscore desc");

    private String orderBy;

    OrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderBy() {
        return orderBy;
    }
}
