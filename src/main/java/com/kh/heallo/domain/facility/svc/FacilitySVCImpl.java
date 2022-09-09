package com.kh.heallo.domain.facility.svc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.heallo.domain.facility.Criteria;
import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.facility.FacilityJson;
import com.kh.heallo.domain.facility.dao.FacilityDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacilitySVCImpl implements FacilitySVC{

    private static final int NUM_OF_ROWS = 10000;
    private final FacilityDAO facilityDAO;
    private final ObjectMapper objectMapper;


    /**
     * 공공데이터 연동
     */
    @Override
    public Integer connect() {
        String[] exampleImages = new String[]{
                "https://images.unsplash.com/photo-1571902943202-507ec2618e8f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8Z3ltfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=400&q=60",
                "https://images.unsplash.com/photo-1540497077202-7c8a3999166f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTN8fGd5bXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=400&q=60",
                "https://images.unsplash.com/photo-1542766788-a2f588f447ee?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NDN8fGd5bXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60",
                "https://images.unsplash.com/photo-1637430308606-86576d8fef3c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NDl8fGd5bXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60",
                "https://images.unsplash.com/photo-1637666062717-1c6bcfa4a4df?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Nzd8fGd5bXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60",
                "https://images.unsplash.com/photo-1637666218229-1fe0a9419267?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8ODh8fGd5bXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60",
                "https://images.unsplash.com/photo-1630703178161-1e2f9beddbf8?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OTF8fGd5bXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60",
                "https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg",
                "https://cdn.pixabay.com/photo/2013/03/09/14/38/gym-91849_960_720.jpg",
                "https://cdn.pixabay.com/photo/2020/04/03/20/49/gym-5000169_960_720.jpg"
        };
        Integer totalCount = Integer.parseInt(String.valueOf(publicConnectApi(1, 2).getResponse().getBody().getTotalCount()));
        Integer resultCount = 0;
        for (int i = 0; i < totalCount / NUM_OF_ROWS; i++) {
            resultCount = 0;
            FacilityJson jsonObjectToFacility = publicConnectApi(i+1, NUM_OF_ROWS);

            for (FacilityJson.Response.Body.Items.Item item : jsonObjectToFacility.getResponse().getBody().getItems().getItem()) {
                if (
                        item.getFaciStat().equals("폐업") ||
                                item.getFaciPointX().equals("-") ||
                                item.getFaciPointY().equals("-") ||
                                item.getFaciPointX().equals("0") ||
                                item.getFaciPointY().equals("0") ||
                                item.getFcobNm().equals("-") ||
                                item.getFaciRoadAddr1().equals("-") ||
                                item.getFaciTel().equals("-")
                ) continue;

                int idx = (int) (Math.random() * 10);
                Facility facility = new Facility(
                        item.getFaciNm(),
                        item.getFcobNm(),
                        item.getFaciHomepage(),
                        item.getFaciTel(),
                        Double.valueOf(item.getFaciPointY()),
                        Double.valueOf(item.getFaciPointX()),
                        item.getFaciRoadAddr1(),
                        item.getFaciRoadPost(),
                        item.getFaciStat(),
                        exampleImages[idx]
                );

                if(!facilityDAO.contains(facility)) facilityDAO.add(facility);
                else facilityDAO.update(facility);

                resultCount++;
            }
        }

        return resultCount;
    }

    //공공데이터 연동 메서드
    private FacilityJson publicConnectApi(Integer pageNo, Integer numOfRows) {
        StringBuilder sb = null;
        try {
            StringBuilder urlBuilder = new StringBuilder("http://www.kspo.or.kr/openapi/service/sportsNewFacilInfoService/getNewFacilInfoList"); /*URL*/

            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=gLqQXaEl7S%2F1SKAHgfQg6i%2FhHE0PHZOxx66vvYlVs0EAXuisC6z57aBdJSdkX2BwZ03megcKkrzvP%2Bd5cx4lNg%3D%3D"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(String.valueOf(pageNo), "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(String.valueOf(numOfRows), "UTF-8")); /*한 페이지 결과 수*/

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "application/json");
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
        } catch (IOException e) {
            log.info("IOException {}", e.getMessage());
        }
        FacilityJson responseJson = getResponseJson(sb);

        return responseJson;
    }

    //JSON파싱 메서드
    private FacilityJson getResponseJson(StringBuilder sb) {
        FacilityJson responseJson = null;
        try {
            responseJson = objectMapper.readValue(sb.toString(), FacilityJson.class);
        } catch (JsonProcessingException e) {
            log.info("JsonProcessingException {}",e.getMessage());
        }

        return responseJson;
    }

    /**
     * 운동시설 조건검색
     * @param criteria 검색조건
     * @return 운동시설리스트
     */
    @Override
    public List<Facility> search(Criteria criteria) {
        criteria.setFcaddr(criteria.getFcaddr()+"%");
        criteria.setFcname("%"+criteria.getFcname()+"%");
        criteria.setFctype("%"+criteria.getFctype()+"%");
        List<Facility> searchedList = facilityDAO.search(criteria);

        return searchedList;
    }

    /**
     * 운동시설 조건검색 결과 수
     * @param criteria 검색조건
     * @return 결과 수
     */
    @Override
    public Integer getTotalCount(Criteria criteria) {
        criteria.setFcaddr(criteria.getFcaddr()+"%");
        criteria.setFcname("%"+criteria.getFcname()+"%");
        criteria.setFctype("%"+criteria.getFctype()+"%");
        Integer totalCount = facilityDAO.getTotalCount(criteria);

        return totalCount;
    }

    /**
     * 운동시설 평균평점 수정
     * @param fcno 운동시설번호
     * @return 결과 수
     */
    @Override
    public Integer updateToScore(Long fcno) {
        Integer resultCount = facilityDAO.updateScore(fcno);

        return resultCount;
    }

    /**
     * 운동시설 상세검색
     * @param fcno 운동시설번호
     * @return 운동시설
     */
    @Override
    public Facility findByFcno(Long fcno) {
        Facility foundFacility = facilityDAO.findByFcno(fcno);

        return foundFacility;
    }
}
