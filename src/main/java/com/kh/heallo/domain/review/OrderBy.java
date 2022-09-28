package com.kh.heallo.domain.review;

public enum OrderBy {
    RV_DATE_ASC("rvcdate asc"), RV_DATE_DESC("rvcdate desc"), RV_SCORE_DSC("rvscore desc"),
    FC_NAME_ASC("fcname asc"), FC_SCORE_DESC("fcscore desc");

    private String orderBy;


    OrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderBy() {
        return orderBy;
    }
}
