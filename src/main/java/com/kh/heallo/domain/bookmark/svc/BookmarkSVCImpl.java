package com.kh.heallo.domain.bookmark.svc;

import com.kh.heallo.domain.bookmark.dao.BookmarkDAO;
import com.kh.heallo.domain.bookmark.Bookmark;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * 즐겨찾기추가
     *
     * @param fcno  운동시설번호
     * @param memno 회원번호
     * @return 즐겨찾기 번호
     */
    @Override
    public Long addBookmark(Long memno, Long fcno) {
        Long bmno = bookmarkDAO.addBookmark(memno, fcno);

        return bmno;
    }

    /**
     * 즐겨찾기 삭제
     * @param bmno 즐겨찾기번호
     */
    @Override
    public Integer deleteBookmark(Long bmno) {
        Integer resultCount = bookmarkDAO.deleteBookmark(bmno);

        return resultCount;
    }
}
