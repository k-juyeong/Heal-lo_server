package com.kh.heallo.domain.review.dao;


import com.kh.heallo.domain.review.Criteria;
import com.kh.heallo.domain.review.Review;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReviewDAOImplTest {

    @Autowired
    private ReviewDAO reviewDAO;
    private static Long rvno;

    @Order(1)
    @Test
    void add() {
        Review review = new Review();
        review.setRvcontents("시설이 너무 좋습니다 적극 추천드립니다");
        review.setRvscore(4.5);
        rvno = reviewDAO.add(1L, 1L, review);

        assertThat(rvno).isNotNull();
    }

    @Order(2)
    @Test
    void findByRvno() {
        System.out.println("rvno = " + rvno);

        Review foundReview = reviewDAO.findByRvno(rvno);

        assertThat(foundReview.getRvno()).isEqualTo(rvno);
    }

    @Order(3)
    @Test
    void getTotalCount() {
        Integer totalCount = reviewDAO.getTotalCount(1L);

        assertThat(totalCount).isGreaterThan(0);
    }

    @Order(4)
    @Test
    void update() {
        Review review = new Review();
        review.setRvcontents("시설이 너무 좋습니다 적극 추천드립니다 수정!");
        review.setRvscore(2.5);
        Integer resultCount = reviewDAO.update(rvno, review);

        Review foundReview = reviewDAO.findByRvno(rvno);

        assertThat(resultCount).isEqualTo(1);
        assertThat(foundReview.getRvcontents()).isEqualTo(review.getRvcontents());
        assertThat(foundReview.getRvscore()).isEqualTo(review.getRvscore());
    }

    @Order(5)
    @Test
    void findListByFcno() {
        Criteria criteria = new Criteria();
        criteria.setPageNo(1);
        criteria.setNumOfRow(5);
        List<Review> reviewList = reviewDAO.findListByFcno(1L, criteria);

        assertThat(reviewList.size()).isGreaterThan(0);
    }

    @Order(6)
    @Test
    void delete() {
        Integer resultCount = reviewDAO.delete(rvno);
        Review foundReview = reviewDAO.findByRvno(rvno);

        assertThat(foundReview).isNull();
        assertThat(resultCount).isGreaterThan(0);
    }
}