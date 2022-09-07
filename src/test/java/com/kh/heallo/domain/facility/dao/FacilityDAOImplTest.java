package com.kh.heallo.domain.facility.dao;

import com.kh.heallo.domain.facility.Bookmark;
import com.kh.heallo.domain.facility.Criteria;
import com.kh.heallo.domain.facility.Facility;
import lombok.extern.slf4j.Slf4j;
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
    private static Long connectedFcno;

    @Order(1)
    @Test
    @DisplayName("운동시설 등록")
    void connect() {
        Facility facility = new Facility("TEST", "당구장업", ".", "010-1111",
                35.121, 121.121, "울산광역시 X", "15151", "정상운영", "#");

        boolean connected = facilityDAO.isConnected(facility);
        connectedFcno = facilityDAO.add(facility);
        facility.setFcstatus("폐업");
        Integer resultCount = facilityDAO.update(facility);

        assertThat(connected).isFalse();
        assertThat(connectedFcno).isNotNull();
        assertThat(resultCount).isGreaterThan(0);
    }

    @Order(2)
    @Test
    @DisplayName("운동시설 상세조회")
    void findByFcno() {
        Facility foundFacility = facilityDAO.findByFcno(connectedFcno);

        assertThat(foundFacility.getFcno()).isEqualTo(connectedFcno);
    }

    @Order(3)
    @Test
    @DisplayName("운동시설 조건검색")
    void search() {
        Criteria criteria = new Criteria("울산광역시%","%당구장업%","%TEST%",1,10);
        List<Facility> searchedList = facilityDAO.search(criteria);
        Facility foundFacility = facilityDAO.findByFcno(connectedFcno);

        assertThat(searchedList).contains(foundFacility);
        assertThat(searchedList.size()).isGreaterThan(0);
    }

    @Order(4)
    @Test
    @DisplayName("운동시설 조건검색 결과 수")
    void getTotalCount() {
        Criteria criteria = new Criteria("울산광역시%","%당구장업%","%%",1,10);
        Integer totalCount = facilityDAO.getTotalCount(criteria);

        assertThat(totalCount).isGreaterThan(0);
    }

    @Order(5)
    @Test
    @DisplayName("즐겨찾기 추가")
    void addBookmark() {
        Long cnt1 = facilityDAO.addBookmark(1L, 1L);
        Long cnt2 = facilityDAO.addBookmark(1L, 2L);
        Long cnt3 = facilityDAO.addBookmark(1L, 3L);

        assertThat(cnt1).isNotNull();
        assertThat(cnt2).isNotNull();
        assertThat(cnt3).isNotNull();
    }

    @Order(6)
    @Test
    @DisplayName("회원의 즐겨찾기 전체 조회")
    void findBookmarkListByMemno() {
        List<Bookmark> bookmarkListByMemno = facilityDAO.findBookmarkListByMemno(1L);

        assertThat(bookmarkListByMemno.size()).isEqualTo(3);
    }

    @Order(7)
    @Test
    @DisplayName("즐겨찾기 삭제")
    void deleteBookmark() {
        List<Bookmark> bookmarkListByMemno = facilityDAO.findBookmarkListByMemno(1L);
        Integer resultCount = 0;
        for (Bookmark bookmark : bookmarkListByMemno) {
            resultCount += facilityDAO.deleteBookmark(bookmark.getBmno());
        }

        List<Bookmark> bookmarkListByMemno2 = facilityDAO.findBookmarkListByMemno(1L);

        assertThat(resultCount).isEqualTo(3);
        assertThat(bookmarkListByMemno2.size()).isEqualTo(0);
    }

    @Order(8)
    @Test
    @DisplayName("운동시설 평균평점 업데이트")
    void updateScore() {
        Integer resultCount = facilityDAO.updateScore(connectedFcno);

        assertThat(resultCount).isEqualTo(1);
    }

    @Order(9)
    @Test
    @DisplayName("운동시설 삭제")
    void delete() {
        Integer resultCount = facilityDAO.delete(connectedFcno);
        Facility foundFacility = facilityDAO.findByFcno(connectedFcno);

        assertThat(resultCount).isGreaterThan(0);
        assertThat(foundFacility).isNull();
    }

}