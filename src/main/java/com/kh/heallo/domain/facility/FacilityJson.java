package com.kh.heallo.domain.facility;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class FacilityJson {

    private Response response;

    @Data
    public static class Response {
        private Header header;

        @Data
        public static class Header {
            private String resultCode;
            private String resultMsg;
        }

        private Body body;

        @Data
        public static class Body {
            private Items items;
            private String numOfRows;
            private String pageNo;
            private String totalCount;

            @Data
            public static class Items {
                List<Item> item;

                @JsonIgnoreProperties(ignoreUnknown = true)
                @Data
                public static class Item {
                    private String faciNm;
                    private String fcobNm;
                    private String faciTel;
                    private String faciHomepage;
                    private String faciPointY;
                    private String faciPointX;
                    private String faciRoadAddr1;
                    private String faciRoadPost;
                    private String faciStat;
                }
            }
        }
    }



}