package com.kh.heallo.domain.facility.dao;

import com.kh.heallo.domain.facility.AutoComplete;
import com.kh.heallo.domain.facility.FacilityCriteria;
import com.kh.heallo.domain.facility.Facility;
import java.util.List;

public interface FacilityDAO {

    /**
     * 운동시설 중복체크
     * @param facility 운동시설
     * @return true,false
     */
    boolean contains(Facility facility);

    /**
     * 운동시설 등록
     * @param facilityList 운동시설
     * @return 시퀀스 번호
     */
    int[] add(List<Facility> facilityList);

    /**
     * 운동시설 업데이트
     * @param facilityList 운동시설
     * @return 결과 수
     */
    int[] update(List<Facility> facilityList);

    /**
     * 운동시설 삭제
     * @return 결과 수
     */
    Integer delete(Long fcno);

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
    Integer updateScore(Long fcno);

    /**
     * 운동시설 상세검색
     * @param fcno 운동시설번호
     * @return  운동시설
     */
    Facility findByFcno(Long fcno);

}
