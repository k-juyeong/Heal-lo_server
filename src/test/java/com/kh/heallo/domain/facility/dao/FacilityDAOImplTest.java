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
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FacilityDAOImplTest {

    @Autowired
    private FacilityDAO facilityDAO;

    @Order(1)
    @Test
    @DisplayName("운동시설 상세조회")
    void findByFcno() {
        Facility foundFacility = facilityDAO.findByFcno(399L);

        assertThat(foundFacility).isNotNull();
    }

    @Order(2)
    @Test
    @DisplayName("운동시설 조건검색")
    void search() {
        Criteria criteria = new Criteria("울산광역시%","%당구장업%","%%",1,10);
        List<Facility> searchedList = facilityDAO.search(criteria);
        Facility foundFacility = facilityDAO.findByFcno(399L);

        assertThat(searchedList).contains(foundFacility);
    }

    @Order(3)
    @Test
    @DisplayName("운동시설 조건검색 결과 수")
    void getTotalCount() {
        Criteria criteria = new Criteria("울산광역시%","%당구장업%","%%",1,10);
        int totalCount = facilityDAO.getTotalCount(criteria);

        assertThat(totalCount).isGreaterThan(0);
    }

    @Order(4)
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

    @Order(5)
    @Test
    @DisplayName("회원의 즐겨찾기 전체 조회")
    void findBookmarkListByMemno() {
        List<Bookmark> bookmarkListByMemno = facilityDAO.findBookmarkListByMemno(1L);

        assertThat(bookmarkListByMemno.size()).isEqualTo(3);
    }

    @Order(6)
    @Test
    @DisplayName("즐겨찾기 삭제")
    void deleteBookmark() {
        List<Bookmark> bookmarkListByMemno = facilityDAO.findBookmarkListByMemno(1L);
        int resultCount = 0;
        for (Bookmark bookmark : bookmarkListByMemno) {
            resultCount += facilityDAO.deleteBookmark(bookmark.getBmno());
        }

        assertThat(resultCount).isEqualTo(3);
    }

    @Order(7)
    @Test
    @DisplayName("운동시설 평균평점 업데이트")
    void updateScore() {
        int resultCount = facilityDAO.updateScore(399L);

        assertThat(resultCount).isEqualTo(1);
    }

}