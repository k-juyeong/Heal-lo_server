package com.kh.heallo.domain.bookmark.dao;

import com.kh.heallo.domain.bookmark.Bookmark;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class BookmarkDAOImplTest {

    @Autowired
    private BookmarkDAO bookmarkDAO;
    private static Long bmno;

    @Order(1)
    @Test
    @DisplayName("즐겨찾기 추가")
    void addBookmark() {
        bmno = bookmarkDAO.addBookmark(1L, 1L);

        assertThat(bmno).isNotNull();
    }

    @Order(2)
    @Test
    @DisplayName("회원의 즐겨찾기 전체 조회")
    void findBookmarkListByMemno() {
        List<Bookmark> bookmarkListByMemno = bookmarkDAO.findBookmarkListByMemno(1L);

        assertThat(bookmarkListByMemno.size()).isEqualTo(1);
    }


    @Order(3)
    @Test
    @DisplayName("즐겨찾기 삭제")
    void deleteBookmark() {
        List<Bookmark> bookmarkListByMemno = bookmarkDAO.findBookmarkListByMemno(1L);
        for (Bookmark bookmark : bookmarkListByMemno) {
            bookmarkDAO.deleteBookmark(bookmark.getBmno());
        }
        List<Bookmark> bookmarkListByMemno2 = bookmarkDAO.findBookmarkListByMemno(1L);

        assertThat(bookmarkListByMemno2.size()).isEqualTo(0);
    }

}