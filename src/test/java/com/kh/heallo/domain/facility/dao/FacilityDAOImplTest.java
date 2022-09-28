package com.kh.heallo.domain.facility.dao;

import com.kh.heallo.domain.facility.AutoComplete;
import com.kh.heallo.domain.facility.FacilityCriteria;
import com.kh.heallo.domain.facility.Facility;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FacilityDAOImplTest {

    @Autowired
    private FacilityDAO facilityDAO;
    private static Facility facility;

    @Order(1)
    @Test
    @DisplayName("false 운동시설 등록")
    void add() {
        ArrayList<Facility> facilities = new ArrayList<>();
        facility = new Facility(
                "TEST", "당구장업", ".", "010-1111",
                35.121, 121.121, "울산광역시 X",
                "15151", "정상운영", "#",0);
        facilities.add(facility);
        int[] results = facilityDAO.add(facilities);

        FacilityCriteria criteria = new FacilityCriteria(
                "울산광역시%","%당구장업%","%TEST%",1,1,10
        );
        List<Facility> searchedList = facilityDAO.search(criteria);
        log.info("add searchedList {}", searchedList);

        facility.setFcno(searchedList.get(0).getFcno());

        assertThat(results[0]).isEqualTo(1);
    }

    @Order(2)
    @Test
    @DisplayName("운동시설 상세조회")
    void findByFcno() {
        Facility foundFacility = facilityDAO.findByFcno(facility.getFcno());

        assertThat(foundFacility.getFcno()).isEqualTo(facility.getFcno());
        assertThat(foundFacility.getFcname()).isEqualTo(facility.getFcname());
        assertThat(foundFacility.getFcaddr()).isEqualTo(facility.getFcaddr());
    }

    @Order(3)
    @Test
    @DisplayName("운동시설 중복체크")
    void contains() {
        boolean connected = facilityDAO.contains(facility);

        assertThat(connected).isTrue();
    }

    @Order(4)
    @Test
    @DisplayName("true 운동시설 업데이트")
    void update() {
        facility.setFcstatus("폐업");
        ArrayList<Facility> facilities = new ArrayList<>();
        facilities.add(facility);
        int[] results = facilityDAO.update(facilities);
        Facility foundFacility = facilityDAO.findByFcno(facility.getFcno());

        assertThat(results[0]).isEqualTo(1);
        assertThat(foundFacility.getFcstatus()).isEqualTo("폐업");
    }

    @Order(5)
    @Test
    @DisplayName("운동시설 평균평점 업데이트")
    void updateScore() {
        Integer resultCount = facilityDAO.updateScore(facility.getFcno());

        assertThat(resultCount).isEqualTo(1);
    }

    @Order(6)
    @Test
    @DisplayName("운동시설 조건검색")
    void search() {
        FacilityCriteria criteria = new FacilityCriteria(
                "울산광역시%","%당구장업%","%TEST%",1,1,10
        );
        List<Facility> searchedList = facilityDAO.search(criteria);

        assertThat(searchedList).contains(facility);
    }

    @Order(7)
    @Test
    @DisplayName("운동시설 조건검색 결과 수")
    void getTotalCount() {
        FacilityCriteria criteria = new FacilityCriteria(
                "울산광역시%","%당구장업%","%TEST%",1,1,10
        );
        Integer totalCount = facilityDAO.getTotalCount(criteria);

        assertThat(totalCount).isEqualTo(1);
    }

    @Order(8)
    @Test
    @DisplayName("운동시설 삭제")
    void delete() {
        Integer resultCount = facilityDAO.delete(facility.getFcno());

        assertThrows(DataAccessException.class, () -> {
            Facility foundFacility = facilityDAO.findByFcno(facility.getFcno());
        });
    }

    @Order(9)
    @Test
    @DisplayName("자동완성 테스트")
    void autoComplete() {
        FacilityCriteria facilityCriteria = new FacilityCriteria();
        facilityCriteria.setFcaddr("경기도%");
        facilityCriteria.setFctype("%당구장업%");
        facilityCriteria.setFcname("나이스%");
        int row = 5;
        List<AutoComplete> autoCompleteData = facilityDAO.autoComplete(facilityCriteria, row);

        autoCompleteData.stream().forEach(s -> assertThat(s.getFcname().contains("나이스")).isTrue());

    }

}