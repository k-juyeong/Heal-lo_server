package com.kh.heallo.domain.facility.svc;

import com.kh.heallo.domain.facility.AutoComplete;
import com.kh.heallo.domain.facility.FacilityCriteria;
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
    List<Facility> search(FacilityCriteria criteria);

    /**
     * 상호명 검색 자동완성
     * @param criteria 검색조건
     * @param row 자동완성 레코드 수
     * @return 상호명 리스트
     */
    List<AutoComplete> autoComplete(FacilityCriteria criteria, Integer row);

    /**
     * 운동시설 조건검색 totalCount
     * @param criteria 검색조건
     * @return 결과 수
     */
    Integer getTotalCount(FacilityCriteria criteria);

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
