package com.kh.heallo.domain.facility.dao;

import com.kh.heallo.domain.facility.Criteria;
import com.kh.heallo.domain.facility.Facility;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FacilityDAOImplTest {

    @Autowired
    private FacilityDAO facilityDAO;
    private static Facility defaultBean;

    @Order(1)
    @Test
    @DisplayName("false 운동시설 등록")
    void add() {
        defaultBean = new Facility("TEST", "당구장업", ".", "010-1111",
                                    35.121, 121.121, "울산광역시 X", "15151", "정상운영", "#");
        Long connectedFcno = facilityDAO.add(defaultBean);
        defaultBean.setFcno(connectedFcno);

        assertThat(connectedFcno).isNotNull();
    }

    @Order(2)
    @Test
    @DisplayName("운동시설 상세조회")
    void findByFcno() {
        Facility foundFacility = facilityDAO.findByFcno(defaultBean.getFcno());

        assertThat(foundFacility.getFcno()).isEqualTo(defaultBean.getFcno());
        assertThat(foundFacility.getFcname()).isEqualTo(defaultBean.getFcname());
        assertThat(foundFacility.getFcaddr()).isEqualTo(defaultBean.getFcaddr());
    }

    @Order(3)
    @Test
    @DisplayName("운동시설 중복체크")
    void contains() {
        boolean connected = facilityDAO.contains(defaultBean);

        assertThat(connected).isTrue();
    }

    @Order(4)
    @Test
    @DisplayName("true 운동시설 업데이트")
    void update() {
        defaultBean.setFcstatus("폐업");
        Integer resultCount = facilityDAO.update(defaultBean);
        Facility foundFacility = facilityDAO.findByFcno(defaultBean.getFcno());

        assertThat(foundFacility.getFcstatus()).isEqualTo("폐업");
    }

    @Order(5)
    @Test
    @DisplayName("운동시설 평균평점 업데이트")
    void updateScore() {
        Integer resultCount = facilityDAO.updateScore(defaultBean.getFcno());

        assertThat(resultCount).isEqualTo(1);
    }

    @Order(6)
    @Test
    @DisplayName("운동시설 조건검색")
    void search() {
        Criteria criteria = new Criteria("울산광역시%","%당구장업%","%TEST%",1,10);
        List<Facility> searchedList = facilityDAO.search(criteria);

        assertThat(searchedList).contains(defaultBean);
    }

    @Order(7)
    @Test
    @DisplayName("운동시설 조건검색 결과 수")
    void getTotalCount() {
        Criteria criteria = new Criteria("울산광역시%","%당구장업%","%TEST%",1,10);
        Integer totalCount = facilityDAO.getTotalCount(criteria);

        assertThat(totalCount).isEqualTo(1);
    }

    @Order(8)
    @Test
    @DisplayName("운동시설 삭제")
    void delete() {
        Integer resultCount = facilityDAO.delete(defaultBean.getFcno());
        Facility foundFacility = facilityDAO.findByFcno(defaultBean.getFcno());

        assertThat(foundFacility).isNull();
    }

}