import makeElements from "../module/create-elememt.js";

// 리뷰 테스트 데이터
// const reviews = {
//     status : '200',
//     msg : "정상",
//     item : {
//         totalcount : 112,
//         items :
//         [
//             {
//                 rvno : 1,
//                 rvcontents : `Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernatur.
//                 Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernatur.
//                 Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernatur.
//                 Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernatur.
//                 Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernatur.
//                 Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernatur.
//                 Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernatur.
//                 Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernatur.
//                 Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernatur.
//                 Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernatur.
//                 Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernatur.`,
//                 rvscore : 1.5,
//                 rvcdate : '2022/08/30',
//                 rvudate : '2022/08/30',
//                 fcno : 1,
//                 memno : 2,
//                 memninkname : 'abc1234',
//                 imgs : [
//                     'https://images.unsplash.com/photo-1637666062717-1c6bcfa4a4df?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Nzd8fGd5bXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60',
//                     'https://images.unsplash.com/photo-1637666218229-1fe0a9419267?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8ODh8fGd5bXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                 ]
//             },
//             {
//                 rvno : 4,
//                 rvcontents : `    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur`,
//                 rvscore : 2.5,
//                 rvcdate : '2022/08/30',
//                 rvudate : '2022/08/30',
//                 fcno : 1,
//                 memno : 3,
//                 memninkname : 'abc1235',
//                 imgs : [
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                     'https://images.unsplash.com/photo-1630703178161-1e2f9beddbf8?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OTF8fGd5bXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg'
//                 ]
//             },
//             {
//                 rvno : 4,
//                 rvcontents : `    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur`,
//                 rvscore : 2.5,
//                 rvcdate : '2022/08/30',
//                 rvudate : '2022/08/30',
//                 fcno : 1,
//                 memno : 3,
//                 memninkname : 'abc1235',
//                 imgs : [
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                     'https://images.unsplash.com/photo-1630703178161-1e2f9beddbf8?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OTF8fGd5bXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg'
//                 ]
//             },
//             {
//                 rvno : 4,
//                 rvcontents : `    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur`,
//                 rvscore : 2.5,
//                 rvcdate : '2022/08/30',
//                 rvudate : '2022/08/30',
//                 fcno : 1,
//                 memno : 3,
//                 memninkname : 'abc1235',
//                 imgs : [
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                     'https://images.unsplash.com/photo-1630703178161-1e2f9beddbf8?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OTF8fGd5bXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg'
//                 ]
//             },
//             {
//                 rvno : 4,
//                 rvcontents : `    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur`,
//                 rvscore : 2.5,
//                 rvcdate : '2022/08/30',
//                 rvudate : '2022/08/30',
//                 fcno : 1,
//                 memno : 3,
//                 memninkname : 'abc1235',
//                 imgs : [
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                     'https://images.unsplash.com/photo-1630703178161-1e2f9beddbf8?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OTF8fGd5bXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg'
//                 ]
//             },
//             {
//                 rvno : 4,
//                 rvcontents : `    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur`,
//                 rvscore : 2.5,
//                 rvcdate : '2022/08/30',
//                 rvudate : '2022/08/30',
//                 fcno : 1,
//                 memno : 3,
//                 memninkname : 'abc1235',
//                 imgs : [
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                     'https://images.unsplash.com/photo-1630703178161-1e2f9beddbf8?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OTF8fGd5bXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg'
//                 ]
//             },
//             {
//                 rvno : 4,
//                 rvcontents : `    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur
//                 aspernaturipsum dolor sit amet consectetur adipisicing elit. Doloremque praesentium consectetur`,
//                 rvscore : 2.5,
//                 rvcdate : '2022/08/30',
//                 rvudate : '2022/08/30',
//                 fcno : 1,
//                 memno : 3,
//                 memninkname : 'abc1235',
//                 imgs : [
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                     'https://images.unsplash.com/photo-1630703178161-1e2f9beddbf8?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OTF8fGd5bXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg',
//                     'https://cdn.pixabay.com/photo/2014/11/11/15/24/gym-526995_960_720.jpg'
//                 ]
//             },
//             {
//                 rvno : 1,
//                 rvcontents : '시설이 좋아요3',
//                 rvscore : 4.5,
//                 rvcdate : '2022/08/30',
//                 rvudate : '2022/08/30',
//                 fcno : 1,
//                 memno : 4,
//                 memninkname : 'abc1236',
//                 imgs: null
//             },
//             {
//                 rvno : 1,
//                 rvcontents : '시설이 좋아요4',
//                 rvscore : 3,
//                 rvcdate : '2022/08/30',
//                 rvudate : '2022/08/30',
//                 fcno : 1,
//                 memno : 5,
//                 memninkname : 'abc1237',
//                 imgs : [
//                     'https://cdn.pixabay.com/photo/2013/03/09/14/38/gym-91849_960_720.jpg',
//                 ]
//             },
//             {
//                 rvno : 1,
//                 rvcontents : '시설이 좋아요5',
//                 rvscore : 4.5,
//                 rvcdate : '2022/08/30',
//                 rvudate : '2022/08/30',
//                 fcno : 1,
//                 memno : 8,
//                 memninkname : 'abc1238'
//             },
//         ]
//     }
// }

//dom 접근
const $reviewLists = document.querySelector('.review-lists');
const $modal = document.getElementById('modal');
const $reviewCnt = document.querySelector('.review-cnt');

//다용도 전역변수
let isFirstRander = true;

// 페이지네이션 설정
const limitPage = 5;      //  페이지 최대 생성 수
const onePageNum = 10;    //  1페이지에 최대 목록 수
let currentPage = 1;          //  현재 페이지

//초기 페이지 셋팅
reviewListRequest();

//비동기 통신 함수
function reviewListRequest() {
    // 비통기 통신

    const pagination = createPagination(Math.ceil(reviews.item.totalcount / onePageNum));
    $reviewLists.appendChild(pagination);

    $reviewCnt.textContent = reviews.item.totalcount;
}

//리뷰 랜더링 함수
function reviewListRander(data) {
    const previewContents = data.rvcontents.substr(0,200) + '...';

    const isMoreview = data.rvcontents.length > 200;

    const reviewCard = 
    makeElements('div',{class : 'review-card', id : `${data.rvno}`},
        makeElements('div',{class : 'review-btn-wrap'},
            makeElements('button',{class : 'review-update btn-review'},'수정'),
            makeElements('button',{class : 'review-delete btn-review'},'삭제')),
        makeElements('div',{class : 'review-card__info'},
            makeElements('div',{class : 'rating-score review-card__star'},
                makeElements('div',{class : 'outer-star'},'★★★★★',
                    makeElements('span',{class : 'inner-star'},'★★★★★'))),
            makeElements('span',{class : 'user-name'},data.memninkname),
            makeElements('span',{class : 'date'},data.rvcdate),
            makeElements('p',{class : 'preview-contents'},isMoreview ? previewContents : data.rvcontents,
            isMoreview ? makeElements('div',{class : 'btn-moreview'},'더보기') : ''),
            makeElements('div',{class : 'preview-wrap'})));

    reviewCard.querySelector('.inner-star').style.width = data.rvscore*20 + '%';

    reviewCard.querySelector('.btn-moreview')?.addEventListener('click',(e) => {
        const contents = reviewCard.querySelector('.preview-contents');
        contents.classList.toggle('moreview-on');

        if(contents.classList.contains('moreview-on')) {
            contents.textContent = data.rvcontents;
        } else {
            contents.textContent = previewContents;
        }
    });

    data.imgs?.forEach(ele => {
        const img =  document.createElement('img');
        img.setAttribute('src',ele);
        img.setAttribute('data-bs-toggle','modal');
        img.setAttribute('data-bs-target','#modal');
        img.style.cursor = 'pointer';
        reviewCard.querySelector('.preview-wrap').appendChild(img);

        img.addEventListener('click',e => {
            $modal.querySelector('img').src = e.target.src;
        })
    });

//     <div class="preview-wrap">
//     <img src="https://media.istockphoto.com/id/1197662246/ko/%EC%82%AC%EC%A7%84/%EC%B2%B4%EC%9C%A1%EA%B4%80%EC%97%90%EC%84%9C-%EC%95%84%EB%A0%B9%EC%9D%98-%ED%96%89.webp?s=612x612&w=is&k=20&c=VSGgwQ-A6cZt5lwifHK1JIuimStPTr1bjqfjKQWUz1I=" alt="">
//     <img src="https://media.istockphoto.com/id/1197662246/ko/%EC%82%AC%EC%A7%84/%EC%B2%B4%EC%9C%A1%EA%B4%80%EC%97%90%EC%84%9C-%EC%95%84%EB%A0%B9%EC%9D%98-%ED%96%89.webp?s=612x612&w=is&k=20&c=VSGgwQ-A6cZt5lwifHK1JIuimStPTr1bjqfjKQWUz1I=" alt="">
//     <img src="https://media.istockphoto.com/id/1197662246/ko/%EC%82%AC%EC%A7%84/%EC%B2%B4%EC%9C%A1%EA%B4%80%EC%97%90%EC%84%9C-%EC%95%84%EB%A0%B9%EC%9D%98-%ED%96%89.webp?s=612x612&w=is&k=20&c=VSGgwQ-A6cZt5lwifHK1JIuimStPTr1bjqfjKQWUz1I=" alt="">
//     <img src="https://media.istockphoto.com/id/1197662246/ko/%EC%82%AC%EC%A7%84/%EC%B2%B4%EC%9C%A1%EA%B4%80%EC%97%90%EC%84%9C-%EC%95%84%EB%A0%B9%EC%9D%98-%ED%96%89.webp?s=612x612&w=is&k=20&c=VSGgwQ-A6cZt5lwifHK1JIuimStPTr1bjqfjKQWUz1I=" alt="">
//     <img src="https://media.istockphoto.com/id/1197662246/ko/%EC%82%AC%EC%A7%84/%EC%B2%B4%EC%9C%A1%EA%B4%80%EC%97%90%EC%84%9C-%EC%95%84%EB%A0%B9%EC%9D%98-%ED%96%89.webp?s=612x612&w=is&k=20&c=VSGgwQ-A6cZt5lwifHK1JIuimStPTr1bjqfjKQWUz1I=" alt="">
// </div>
        //     <div class="review-btn-wrap">
        //     <button class="review-update btn-review">수정</button>
        //     <button class="review-delete btn-review">삭제</button>
        // </div>
    return reviewCard;
}

//페이지네이션 생성 함수
function createPagination(totalPage) {

    //이전 페이지네이션 삭제
    $reviewLists.querySelector('.pagination-wrap')?.remove();

    //페이지네이션 wrap생성
    const paginationLis = [];
    const paginationWrap = document.createElement('li');
    const pagination = document.createElement('ul');
    paginationWrap.setAttribute('class', 'pagination-wrap');
    paginationWrap.setAttribute('aria-label', 'Page navigation example');
    pagination.setAttribute('class', 'pagination pagination-sm');
    paginationWrap.appendChild(pagination);

    //페이지 생성
    let pageLv = Math.ceil(currentPage/limitPage);
    let startIdx = currentPage;
    let lastIdx = currentPage + limitPage;

    for (startIdx; startIdx < lastIdx && startIdx <= totalPage; startIdx++) {
        const page = document.createElement('li');
        page.setAttribute('class', 'page-item');
        const a = document.createElement('a');
        a.setAttribute('class', 'page-link shadow-none');
        a.setAttribute('href','#reviewContainer');
        a.textContent = startIdx;

        // 페이지 클릭 이벤트
        a.addEventListener('click', ({target}) => {
            currentPage = parseInt(target.textContent);  //현재 페이지 저장

            // 이전 목록들 초기화
            [...$reviewLists.children].filter(ele => ele.classList.contains('review-card'))
            .forEach(ele => ele.remove());

            //새 목록 생성   
            reviews.item.items.forEach(ele => $reviewLists.prepend(reviewListRander(ele)));

            //클릭 표시
            [...$reviewLists.querySelectorAll('a')].forEach(ele => ele.classList.remove('on'));
            target.classList.add('on');
        });
        page.appendChild(a);
        paginationLis.push(page);

        isFirstRander ? a.href = '#' : '';
        if(startIdx == currentPage) {
            a.click();
            isFirstRander = false;
        }
    }

    //이전 페이지 생성
    if(currentPage - limitPage > 0) {
        const page = document.createElement('li');
        page.setAttribute('class', 'page-item');
        const a = document.createElement('a');
        a.setAttribute('class', 'page-link shadow-none');
        a.textContent = '이전';

        page.appendChild(a);
        paginationLis.unshift(page);

        a.addEventListener('click', e => {

            currentPage = limitPage*pageLv - (limitPage*2-1);
            const paginationWrap = createPagination(totalPage);
            $reviewLists.appendChild(paginationWrap);
        })
    }

    //다음 페이지 생성
    if(pageLv*limitPage < totalPage) {
        const page = document.createElement('li');
        page.setAttribute('class', 'page-item');
        const a = document.createElement('a');
        a.setAttribute('class', 'page-link shadow-none');
        a.textContent = '다음';

        page.appendChild(a);
        paginationLis.push(page);

        a.addEventListener('click', e => {

            currentPage = limitPage*pageLv + 1;
            const paginationWrap = createPagination(totalPage);
            $reviewLists.appendChild(paginationWrap);
        })
    }

    paginationLis.forEach(ele => pagination.appendChild(ele))
    return paginationWrap;
}