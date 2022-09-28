package com.kh.heallo.domain.bookmark.svc;

import com.kh.heallo.domain.bookmark.dao.BookmarkDAO;
import com.kh.heallo.domain.bookmark.Bookmark;
import com.kh.heallo.domain.facility.Facility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BookmarkSVCImpl implements BookmarkSVC{

    private final BookmarkDAO bookmarkDAO;

    /**
     * 로그인 계정 즐겨찾기 목록 조회
     * @param memno 회원번호
     * @return 즐겨찾기 리스트
     */
    @Override
    public List<Bookmark> findBookmarkListByMemno(Long memno) {
        List<Bookmark> bookmarkListByMemno = bookmarkDAO.findBookmarkListByMemno(memno);
        return bookmarkListByMemno;
    }

    /**
     * @param memno 회원번호
     * @param fcno  운동시설번호
     * @return 상태값
     */
    @Override
    public Boolean replace(Long memno, Long fcno) {
        boolean status = false;

        // 1) 즐겨찾기가 체크되어있는지 확인
        Optional<Bookmark> optionalBookmark = bookmarkDAO.CheckingBookmark(memno, fcno);

        // 2) 즐겨찾기 추가 or 삭제
        if (optionalBookmark.isEmpty()) {
            bookmarkDAO.addBookmark(memno, fcno);
            status = true;
        } else {
            bookmarkDAO.deleteBookmark(optionalBookmark.get().getBmno());
        }

        return status;
    }

    /**
     * 즐겨찾기 페이지 리스트
     *
     * @return
     */
    @Override
    public List<Facility> bookmarkPageList(String order) {
        return bookmarkDAO.bookmarkPageList(order);
    }
}
