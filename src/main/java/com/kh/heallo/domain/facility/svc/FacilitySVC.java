package com.kh.heallo.domain.facility.svc;

import com.kh.heallo.domain.facility.Criteria;
import com.kh.heallo.domain.facility.Facility;

import java.util.List;

public interface FacilitySVC {

    /**
     * 공공데이터 연동
     */
    Integer connect();

    /**
     * 운동시설 조건검색
     * @param criteria 검색조건
     * @return 운동시설리스트
     */
    List<Facility> search(Criteria criteria);

    /**
     * 운동시설 조건검색 totalCount
     * @param criteria 검색조건
     * @return 결과 수
     */
    Integer getTotalCount(Criteria criteria);

    /**
     * 운동시설 평균평점 수정
     * @param fcno 운동시설번호
     * @return  결과 수
     */
    Integer updateToScore(Long fcno);

    /**
     * 운동시설 상세검색
     * @param fcno 운동시설번호
     * @return  운동시설
     */
    Facility findByFcno(Long fcno);

}
