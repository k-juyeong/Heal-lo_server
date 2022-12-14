import {categoryLoca_lv1, categoryLoca_lv2, category_fctype} from "../module/category.js";
import makeElements from "../module/create-elememt.js";
import MapUtile from "../module/naver-map-api.js";
import sweetalert from "../module/swal.js";

//DOM
const $cgListsByLoca1 = document.querySelector('.search__cg-wrap .search__cg-loca1');
const $cgListsByLoca2 = document.querySelector('.search__cg-wrap .search__cg-loca2');
const $cgListsByFacility = document.querySelector('.search__cg-wrap .search__cg-fa');
const $searchedLists = document.querySelector('.searched-list-wrap .searched-lists');
const $openList = document.querySelector('.open-list');
const $acWrap = document.querySelector('.auto-complete-wrap')
const $textInput = document.getElementById('textSearchInput');

//검색 임계상태
let requstStatus = 'false';

//첫 페이지 상태
let firstLoad = true;

//페이지네이션 설정
let currentPage = 1;          //  현재 페이지

//검색 조건 저장
let selectedCgLocaSave = {level1 : '', level2 : ''};
let selectedTypeCgSave = ''; 
let searchedTextSave = '';
let pagingRequestPram = '';

//네이버 지도 생성
const mapUtil = createMap();

//알림창 객체
const swal = new sweetalert();

//지역 카테고리 대분류 랜더링
RenderingUlTagLv1($cgListsByLoca1,categoryLoca_lv1);

//운동 카테고리 랜더링
RenderingUlTagLv1($cgListsByFacility,category_fctype);

//중분류 외 클릭 이벤트
document.body.addEventListener('click', ({target}) => {
  if (target.closest('.search__cg-wrap .search__cg-loca2')) return;
  else deleteLv2();
},true);

//검색어 입력 이벤트
$textInput.addEventListener('input', () => {
  searchedTextSave = $textInput.value.trim('');
  autoComplete(createRequestPram())
});

$textInput.addEventListener('focusin', () => {
  searchedTextSave = $textInput.value.trim('');
  autoComplete(createRequestPram())
});

//검색 자동완성창 외 클릭 이벤트
document.body.addEventListener('click', ({target}) => {
  if(target.closest('.auto-complete-wrap') || target.closest('.text-input__body')) return;
  else $acWrap.innerHTML = '';
},true);

// 지역 카테고리 대분류 클릭 이벤트
$cgListsByLoca1.addEventListener('click', ({target,currentTarget}) => {
  if(target.tagName != 'P') return;

  //중분류 삭제
  deleteLv2()

  //중분류 생성
  RenderingUlTagLv2($cgListsByLoca2,categoryLoca_lv2[`${target.id}`], target.id);

  //첫 로딩이면?
  if (firstLoad) {
    $cgListsByLoca2.querySelectorAll('p')[0].click();
    firstLoad = false;
  }
});

//지역 카테고리 중분류 클릭 이벤트
$cgListsByLoca2.addEventListener('click',({target,currentTarget}) => {
  if(target.tagName != 'P') return;

  //값 저장
  selectedCgLocaSave.level1 = target.classList[0];
  selectedCgLocaSave.level2 = target.textContent;

  //선택되어있는 대분류 카테고리
  const selectedLv1 = $cgListsByLoca1.querySelector(`#${selectedCgLocaSave.level1}`);

  //기존 lv2 카테고리 스타일 제거
  [...$cgListsByLoca2.querySelectorAll('li p')].forEach(ele => ele.style.backgroundColor = '#ffffff99');

  //기존 lv1 카테고리 스타일 제거
  [...$cgListsByLoca1.querySelectorAll('p')].forEach(ele => ele.style.backgroundColor = '#ffffff99');

  //lv1,lv2 지역카테고리 스타일 적용
  selectedLv1.style.backgroundColor = 'var(--color-others-header)';
  target.style.backgroundColor = 'var(--color-others-header)';

  //중분류 삭제
  deleteLv2();

  //툴팁생성
  createTooltip(selectedLv1);
});

//운동 카테고리 클릭 이벤트
$cgListsByFacility.addEventListener('click',({target}) => {
  if(target.tagName != 'P') return;

  //값 저장
  selectedTypeCgSave = target.id;

  //그전 카테고리 스타일 제거
  [...$cgListsByFacility.querySelectorAll('li p')].forEach(ele => ele.style.backgroundColor = '#ffffff99');

  //카테고리 스타일 적용
  target.style.backgroundColor = 'var(--color-others-header)';
})

// 목록 on/off 버튼 클릭 이벤트 (목록 on/off 이벤트)
$openList.addEventListener('click',() => {
  actionOpenMenu();
})

// 검색버튼 클릭이벤트
document.querySelector('.btn-search').addEventListener('click', () => {

  //임계상태 변경
  requstStatus = false;

  //요청 파라미터 생성
  const requestPram = createRequestPram();

  //검색
  pagingRequestPram = requestPram;
  currentPage = 1;
  search(requestPram)
});

//내 위치 클릭 이벤트 (내 위치 이벤트)
document.querySelector('.my-location').addEventListener('click', () => {
  if (!navigator.geolocation) {
    alert('내 위치정보를 지원하지 않는 브라우저입니다.');
    return;
  }

  //위치 불러오기
  navigator.geolocation.getCurrentPosition(
      position => mapUtil.makeMyMarker(position.coords.latitude, position.coords.longitude),
      positionError => alert('위치를 불러오는 중 오류가 발생했습니다.')
  );
});

//input 검색버튼 클릭 이벤트
document.querySelector('.text-input__body').addEventListener('submit',(e) => {
  e.preventDefault();

  //임계상태 변경
  requstStatus = false;

  //요청 파라미터 생성
  const requestPram = createRequestPram();

  //검색
  pagingRequestPram = requestPram;
  currentPage = 1;
  search(requestPram);
})

//초기 카테고리 상태
initDefaultCategory();

//메인페이지에서 검색 이벤트
if ($textInput.value != '') {
  searchedTextSave = $textInput.value;
  search();
}

/** 함수 **/

//카테고리 기본 선택
function initDefaultCategory() {
  $cgListsByLoca1.querySelectorAll('p')[0].click();
  $cgListsByFacility.querySelectorAll('p')[0].click();
}

//지도 생성
function createMap() {
  const mapOptions = {
    zoom: 13,
    minZoom: 7,
    zoomControl: true, //줌 컨트롤의 표시 여부
    zoomControlOptions: { //줌 컨트롤의 옵션
      position: naver.maps.Position.TOP_RIGHT
    },
    mapTypeControl: true,
    logoControl: false,
    disableKineticPan: false
  };
  const map = new naver.maps.Map('map', mapOptions);
  return new MapUtile(map);
}

//대분류 카테고리 랜더링
function RenderingUlTagLv1(ulTag,arr) {
  arr.forEach(ele => {
    ulTag.appendChild(
      makeElements('li',null,
        makeElements('div',{class : 'location-tooltip'},
            makeElements('i',null)),
        makeElements('p',{id : ele.value},ele.text)
        )
    );
  })
}

//중분류 카테고리 랜더링
function RenderingUlTagLv2(ulTag,arr,parentLv) {
  arr.forEach((ele) => {
    const pTag = document.createElement('p');
    pTag.textContent = ele;
    pTag.setAttribute('class',parentLv);
    const liTag = document.createElement('li');
    liTag.appendChild(pTag);
    ulTag.appendChild(liTag);

    //선택되어있는 카테고리 스타일 적용
    if(selectedCgLocaSave.level1 == parentLv && selectedCgLocaSave.level2 == ele) {
      pTag.style.backgroundColor = 'var(--color-others-header)';
    }
  })

  createLv2();
}

//중분류 카테고리 숨기기
function deleteLv2() {
  $cgListsByLoca2.style.top = 0;
  $cgListsByLoca2.style.visibility ='hidden';
  $cgListsByLoca2.innerHTML = '';
}

//중분류 카테고리 생성
function createLv2() {
  $cgListsByLoca2.style.top = $cgListsByLoca1.children[0].offsetTop + $cgListsByLoca1.offsetHeight + 'px';
  $cgListsByLoca2.style.visibility = 'visible';
}

//툴팁생성
function createTooltip(selectedLv1) {
  [...document.querySelectorAll('.location-tooltip')].forEach(ele => ele.style.opacity = '0');
  const tooltip = selectedLv1.previousElementSibling;
  tooltip.style.opacity = '1';
  tooltip.querySelector('i').textContent = selectedCgLocaSave.level2;
  tooltip.style.width = `${selectedCgLocaSave.level2.length * 14}px`;
}

//메뉴 오픈
function actionOpenMenu() {
  const $mapUtil = document.querySelector('.map-util');
  $mapUtil.classList.toggle('open');
  $searchedLists.classList.toggle('open-lists');

  setTimeout(() => {
    const beforeIcon = $mapUtil.querySelector('.open-list i');
    beforeIcon.remove();
    const afterIcon = document.createElement('i');
      if($mapUtil.classList.contains('open')) {
        afterIcon.setAttribute('class','fa-solid fa-arrow-up-short-wide openbar-icon');
      } else {
        afterIcon.setAttribute('class','fa-solid fa-arrow-down-short-wide openbar-icon');
      }
    $openList.appendChild(afterIcon);
  },300)
}

//상호명 자동완성
function autoComplete(requestPram) {

  let queryPram = `?fcaddr=${requestPram.loca}`;
  queryPram += `&fctype=${requestPram.type}`;
  queryPram += `&fcname=${requestPram.text}`;

  fetch('/facilities/auto-complete' + queryPram, {
    method: 'GET'
  })
      .then(response => response.json())
      .then(jsonData => {
        console.log(jsonData)
        $acWrap.innerHTML = '';
        if (jsonData.header.code == '00') {
          jsonData.data.autoComplete.map(ele => {
            const resultTag = makeElements('li', {class: 'list'},
                makeElements('div',{class : 'name'},`${ele.fcname}`),
                makeElements('div',{class : 'count'},`reviews ${ele.rvtotal}`));

            resultTag.addEventListener('click', () => {
              $textInput.value = ele.fcname
              searchedTextSave = ele.fcname;
              $acWrap.innerHTML = '';
            });

            return resultTag;
          }).forEach(ele => $acWrap.appendChild(ele));

        } else new Error(jsonData.data)
      })
      .catch(err => console.log(err));
}

//운동시설 항목 생성
function createList(itemData) {

  console.log(itemData)

  const listWrap =
      makeElements('div', {class: 'searched-lists__item'},
          makeElements('div', {class: 'item-img'},
              makeElements('img', {src: `${itemData.fcimg}`})),
          makeElements('div', {class: 'item-content'},
              makeElements('div', {class: 'item-content__top'},
                  makeElements('a', {href: '#'},
                      makeElements('h3', {class: 'item-title'}, itemData.fcname)),
                  makeElements('div', {class: 'icon-wrap'},
                      makeElements('span',{class : 'fa-stack fa-lg favorite-icon'},
                          makeElements('i',{class : 'fa fa-square fa-stack-2x'}),
                          makeElements('i',{class : 'fa-solid fa-heart fa-stack-1x contents-icon'})),
                      makeElements('span', {class: 'fa-stack fa-lg move-map-icon'},
                          makeElements('i',{class : 'fa fa-square fa-stack-2x'}),
                          makeElements('i',{class : 'fa-solid fa-map-location-dot fa-stack-1x contents-icon'})),
                      makeElements('p',{class : 'review-total'},`${itemData.rvtotal} reviews`),
                      makeElements('div',{class : 'rating-score'},
                          makeElements('div',{class : 'outer-star'},'★★★★★',
                              makeElements('span',{class : 'inner-star'},'★★★★★'))))),
              makeElements('div', {class: 'item-content__body'},
                  makeElements('p', {class: 'item-sub item-tel'}, itemData.fctel),
                  makeElements('p', {class: 'item-sub item-addr'},itemData.fcaddr))));

  //별점 설정
  listWrap.querySelector('.inner-star').style.width = itemData.fcscore*20 + '%';

  //맵 유틸기능(지도이동)
  listWrap.querySelector('.move-map-icon')
      .addEventListener('click',() => {
    mapUtil.moveMap(itemData.fclat,itemData.fclng)
  })

  //운동시설 제목 클릭 이벤트
  listWrap.querySelector('.item-title')
      .addEventListener('click',() => {
    location.href = `/facilities/${itemData.fcno}`;
  })

  //즐겨찾기 여부에따라 스타일 추가
  if (itemData.bookmarking) {
    listWrap.querySelector('.contents-icon').style.color = 'var(--color-main-header)';
  }

  //즐겨찾기 추가/삭제 이벤트
  listWrap.querySelector('.favorite-icon')
      .addEventListener('click', (e) => replaceBookmark(itemData.fcno, e.target));

  return listWrap;
}

//검색 요청 파라미터 생성
function createRequestPram() {
  let locationValue = '';
  const lv1 = selectedCgLocaSave.level1;
  const lv2 = selectedCgLocaSave.level2;
  locationValue = lv2 == '전체' ? lv1 : `${lv1} ${lv2}`;

  const pram = {
    text : searchedTextSave,
    type : selectedTypeCgSave,
    loca : locationValue,
    pageNo : currentPage,
  };

  return pram;
}

//검색 기본 메세지
function createDefault(msg) {
  return (`<div class="default">${msg}</div>`)
}

//검색
function search(requestPram) {
  const queryPram = `?fcaddr=${requestPram.loca}&fctype=${requestPram.type}&fcname=${requestPram.text}&pageNo=${requestPram.pageNo}`;

  fetch('/facilities/list' + queryPram, {
    method: 'GET'
  })
      .then(response => response.json())
      .then(jsonData => {
        const $resultCount = document.querySelector('.result-count');

        //임계상태 변경
        requstStatus = true;

        //기존 목록, 페이징 삭제
        $searchedLists.innerHTML = '';

        //목록 활성화
        if (!$searchedLists.classList.contains('open-lists')) {
          actionOpenMenu();
        }

        if (jsonData.header.code == '00') {

          //총 검색 결과 표시
          $resultCount.textContent = jsonData.data.pagination.totalRec;

          //목록 생성
          jsonData.data.facilities.forEach(ele => $searchedLists.appendChild(createList(ele)));

          //페이지네이션 생성
          const paginationWrap = createPagination(jsonData.data.pagination);
          $searchedLists.appendChild(paginationWrap);

          //운동시설 마커 생성
          mapUtil.makeMarkers(jsonData.data.facilities);

        } else if (jsonData.header.code == '02') {

          //총 검색 결과 표시
          $resultCount.textContent = '0';

          //기본 메세지 생성
          $searchedLists.innerHTML = createDefault('검색 결과가 없습니다.');

          //맵 초기상태로 변경
          mapUtil.defaultSet();

          return;
        }
      })
      .catch(error => console.log(error))
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

  //페이지네이션 객체
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
      pagingRequestPram.pageNo = currentPage
      search(pagingRequestPram);
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
    link.addEventListener('click', ({target}) => {

      //현재 페이지 저장
      currentPage = parseInt(target.textContent);
      pagingRequestPram.pageNo = currentPage

      //검색
      search(pagingRequestPram);
    });
  }

  //다음버튼 생성
  if(pageInfo.next) {

    //페이지 태그 생성
    const {page,link} = createPagTag('다음');
    pagination.appendChild(page);

    //다음버튼 클릭 이벤트
    link.addEventListener('click', e => {
      currentPage = pageInfo.endPage + 1;
      pagingRequestPram.pageNo = currentPage
      search(pagingRequestPram);
    })
  }

  return paginationWrap;
}

//로그인 이동
function redirectLogin() {
  location.href = `/members/login?requestURI=${window.location.pathname}`;
}

//즐겨찾기 업데이트
function replaceBookmark(fcno, target) {
  fetch(`/bookmarks/${fcno}`, {
    method:'PATCH',        //http method
    headers:{             //http header
      'Accept':'application/json'
    }
  })
      .then(response => response.json())
      .then(jsonData => {
        if (jsonData.header.code == '00') {
          target.style.color =
              jsonData.data.status ? 'var(--color-main-header)' : `white`;

        } else if (jsonData.header.code == '03') {
          swal.checkLogin(redirectLogin);

        } else {
          throw new Error(jsonData.data);

        }
      })
      .catch(err => console.log(err));
}
