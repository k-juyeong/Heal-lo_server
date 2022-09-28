import makeElements from "../module/create-elememt.js";
import sweetalert from "../module/swal.js";

//DOM
const $reviewLists = document.querySelector('.review-lists');
const $orderBySelect = document.querySelector('.order-by-selector');
const $newImageWrap = document.querySelector('.facility-footer .new-image-wrap');

//페이지 첫 로딩 상태 값
let isFirst = true;

//운동시설 번호 전역변수
const fcno = document.querySelector('.facility').dataset.fcno;

// 페이지네이션 설정
let currentPage = 1;      //  현재 페이지

//초기 페이지 세팅
getReviewList();

//알림창 객체
const swal = new sweetalert();

//작성버튼 클릭이벤트
document.querySelector('.review-write').addEventListener('click', e => {
    if (!document.getElementById('loginOn')) {
        swal.checkLogin(redirectLogin);
    } else {
        location.href = `/reviews/${fcno}/add`;
    }
});

//리뷰 정렬기준 선택 이벤트
$orderBySelect.addEventListener('change',() => {
    currentPage = 1;
    getReviewList()
});

//즐겨찾기 클릭 이벤트
document.querySelector('.favorite-icon').addEventListener('click', (e) => {
    e.preventDefault();
    replaceBookmark(fcno,e.target)
});

/** 함수 **/

//즐겨찾기 업데이트
function replaceBookmark(fcno,target) {
    fetch(`/bookmarks/${fcno}`, {
        method: 'PATCH',        //http method
        headers: {             //http header
            'Accept': 'application/json'
        }
    })
        .then(response => response.json())
        .then(jsonData => {
            if (jsonData.header.code == '00') {
                if (jsonData.data.status) {
                    target.style.color = 'var(--color-main-header)';
                    swal.redirectBookmarkList()
                } else {
                    target.style.color = `black`;
                }

            } else if (jsonData.header.code == '03') {
                swal.checkLogin(redirectLogin)

            } else {
                throw new Error(jsonData.data);

            }
        })
        .catch(err => console.log(err));
}

//로그인으로 이동
function redirectLogin() {
    location.href = `/members/login?requestURI=${window.location.pathname}`;
}

//운동시설 별점세팅
function getFacilityScore() {
    fetch(`/facilities/${fcno}/score`,{
        method : 'GET'
    })
        .then(response => response.json())
        .then(jsonData => {
            if(jsonData.header.code != '00') throw new Error(jsonData.data.message);
            //리뷰 평균 수정
            document.querySelector('.review-header__main .text-score')
                .textContent = jsonData.data.fcScore;
            document.querySelector('.review-header__main .inner-star')
                .style.width = jsonData.data.fcScore * 20 + '%'
        })
        .catch(error => console.log(error))
}

//리뷰리스트 기본 메세지
function createDefault(msg) {
    return (
        `
        <div class="empty-message">
            <p>${msg}</p>
        </div>
        `
    )
}

//리뷰 생성
function createList(data) {

    //리뷰 미리보기 생성
    let preContents = data.rvcontents;
    let isMoreview = false;

    //라인 검사
    if (data.rvline > 5) {
        let findIdx = 0;
        for (let i = 0; i < 5; i++) {
            findIdx = data.rvcontents.indexOf('\n', i + findIdx);
        }
        preContents = data.rvcontents.substr(0, findIdx);
        isMoreview = true;

    //길이 검사
    } else if(data.rvcontents.length > 200) {
        preContents = data.rvcontents.substr(0, 200);
        isMoreview = true;
    }

    console.log(data)

    const reviewCard =
        makeElements('div',{class : 'review-card', id : `${data.rvno}`},
            makeElements('div',{class : 'review-card__info'},
                makeElements('div',{class : 'rating-score review-card__star'},
                    makeElements('div',{class : 'review-text-score'},`${data.rvscore}점`),
                    makeElements('div',{class : 'outer-star'},'★★★★★',
                        makeElements('span',{class : 'inner-star'},'★★★★★'))),
                makeElements('span',{class : 'info-header'},
                    makeElements('p',{class : 'user-name'},data.memnickname),
                    data.login ? makeElements('div',{class : 'review-btn-wrap'},
                        makeElements('button',{class : 'review-update btn-review'},'수정'),
                        makeElements('button',{class : 'review-delete btn-review'},'삭제')) : ''),
                makeElements('span',{class : 'date'},data.rvcdate),
                makeElements('div',{class : 'preview-contents'},preContents,
                    isMoreview ? makeElements('div',{class : 'btn-moreview'},'더보기') : ''),
                makeElements('div',{class : 'preview-wrap'})));

    //별점 설정
    reviewCard.querySelector('.inner-star').style.width = data.rvscore*20 + '%';

    //더보기 이벤트
    reviewCard.querySelector('.btn-moreview')?.addEventListener('click',(e) => {
        const contents = reviewCard.querySelector('.preview-contents');
        contents.classList.toggle('moreview-on');

        if(contents.classList.contains('moreview-on')) {
            contents.textContent = data.rvcontents;
        }
    });

    //이미지 미리보기 생성
    data.imageFiles
        ?.map(ele => createImageDiv(ele))
        .forEach(ele => reviewCard.querySelector('.preview-wrap').appendChild(ele));

    //리뷰 수정버튼 이벤트
    reviewCard.querySelector('.review-update')
        ?.addEventListener('click',() => toUpdatePage(data.rvno));

    //리뷰 삭제버튼 이벤트
    reviewCard.querySelector('.review-delete')
        ?.addEventListener('click', () => swal.checkDeleteAlert(() => deleteReview(data.rvno)));

    return reviewCard;
}

//리뷰 검색
function getReviewList() {
    const queryPram = `?pageNo=${currentPage}&orderBy=${$orderBySelect.value}`;
    fetch(`/reviews/${fcno}/list` + queryPram, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(jsonData => {
            const $resultCount = document.querySelector('.review-cnt')

            //기존노드 삭제
            $reviewLists.innerHTML = '';

            //최신 업로드 이미지 노드 삭제
            $newImageWrap.innerHTML = '';

            //운동시설 별점 세팅
            getFacilityScore();

            //최신 업로드 이미지 5개 설정
            setNewReviewImage();

            if (jsonData.header.code == '00') {

                //총 검색 결과 표시
                $resultCount.textContent = jsonData.data.pagination.totalRec;

                //목록 생성
                jsonData.data.reviews.forEach(ele => $reviewLists.appendChild(createList(ele)));

                //페이지네이션 생성
                const paginationWrap = createPagination(jsonData.data.pagination);
                $reviewLists.appendChild(paginationWrap);

            } else if (jsonData.header.code == '02') {

                $resultCount.textContent = '0';
                $reviewLists.innerHTML = createDefault('리뷰가 없습니다. 리뷰를 달아주세요!');
                return;
            }
        })
        .catch(error => console.log(error));
}

//최근 등록된 이미지 조회
function setNewReviewImage() {
    fetch(`/reviews/${fcno}/new-images`, {
        method : "GET"
    })
        .then(response => response.json())
        .then(jsonData => {
            if (jsonData.header.code == '00') {
                jsonData.data.images
                    .map(ele => createImageDiv(ele))
                    .forEach(ele => $newImageWrap.appendChild(ele));

            } else if (jsonData.header.code == '02') {
                $newImageWrap.innerHTML = createDefault('최근에 올라온 운동시설 사진이 없습니다.')
            }
        })
        .catch(err => console.log(err))
}

//리뷰 삭제
function deleteReview(rvno) {
    fetch(`/reviews/${rvno}`, {
        method: `DELETE`
    })
        .then(response => response.json())
        .then(jsonData => {
            getReviewList()
        })
        .catch(error => console.log(error));
}

//리뷰 수정페이지
function toUpdatePage(rvno) {
    window.location = `/reviews/${rvno}/edit`;
}

//모달창 이미지 변경
function createImageDiv(data) {
    const image =
    makeElements('div', {class: 'upload-img'},
        makeElements('img', {src: `/images/${data.code}/${data.ufsname}`}));
    image.setAttribute('data-bs-toggle', 'modal');
    image.setAttribute('data-bs-target', '#modal');

    image.addEventListener('click', e => {

        document.getElementById('modal')
            .querySelector('img').src = e.target.src;
    });

    return image;
}

//페이지 태그 생성
function createPagTag(text) {
    const page = document.createElement('li');
    page.setAttribute('class', 'page-item');
    const a = document.createElement('a');
    a.setAttribute('class', 'page-link shadow-none');
    a.textContent = text;
    page.appendChild(a);

    return {page : page, link : a};
}

//페이지네이션 생성
function createPagination(pageInfo) {

    //페이지네이션 wrap생성
    const paginationWrap = document.createElement('li');
    const pagination = document.createElement('ul');
    paginationWrap.setAttribute('class', 'pagination-wrap');
    paginationWrap.setAttribute('aria-label', 'Page navigation example');
    pagination.setAttribute('class', 'pagination pagination-sm');
    paginationWrap.appendChild(pagination);

    //페이지 설정
    let startIdx = pageInfo.startPage;
    let lastIdx = pageInfo.endPage;

    //이전버튼 생성
    if(pageInfo.prev) {

        //페이지 태그 생성
        const {page,link} = createPagTag('이전');
        pagination.appendChild(page);

        //이전버튼 클릭 이벤트
        link.addEventListener('click', e => {
            currentPage = pageInfo.startPage - pageInfo.page_COUNT_PER_PAGE;
            getReviewList();
        })
    }

    //페이지 생성
    for (startIdx; startIdx <= lastIdx; startIdx++) {

        //페이지 태그 생성
        const {page,link} = createPagTag(startIdx);
        pagination.appendChild(page);

        //페이지 클릭 표시
        if (startIdx == currentPage) {
            link.classList.add('on');
        }

        // 페이지 클릭 이벤트
        link.addEventListener('click', (e) => {
            e.preventDefault();

            //링크 설정
            e.target.textContent == 1 && isFirst ? location.href = '#' : location.href = '#review';

            //페이지 첫 로딩 상태 값
            isFirst = false;

            //현재 페이지 저장
            currentPage = parseInt(e.target.textContent);

            //페이징 검색
            getReviewList();

        });
    }

    if(pageInfo.next) {

        //페이지 태그 생성
        const {page,link} = createPagTag('다음');
        pagination.appendChild(page);

        //다음버튼 클릭 이벤트
        link.addEventListener('click', e => {
            currentPage = pageInfo.endPage + 1;
            getReviewList();
        })
    }

    return paginationWrap;
}