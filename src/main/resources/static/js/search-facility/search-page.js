import {categoryLoca_lv1, categoryLoca_lv2, category_fctype} from "../module/category.js";
import makeElements from "../module/create-elememt.js";
import MapUtile from "../module/naver-map-api.js";

// dom접근
const $cgListsByLoca1 = document.querySelector('.search__cg-wrap .search__cg-loca1');
const $cgListsByLoca2 = document.querySelector('.search__cg-wrap .search__cg-loca2');
const $cgListsByFacility = document.querySelector('.search__cg-wrap .search__cg-fa');
const $searchForm = document.querySelector('.text-input__body');
const $inputByText = document.getElementById('textSearchInput');
const $searchedLists = document.querySelector('.searched-list-wrap .searched-lists');
const $resultCount = document.querySelector('.result-count');
const $mapUtil = document.querySelector('.map-util');
const $openList = document.querySelector('.open-list');
const $myLocation = document.querySelector('.my-location');

// 다용도 전역변수
let selectedLog = {before : '', now : ''};
const cgRemveColor = '#ffffff99';
const cgAddColor = 'var(--color-others-header)';
let requstStatus = 'false'; 

// 페이지네이션 설정
const limitPage = 5;      //  페이지 최대 생성 수
const onePageNum = 10;    //  1페이지에 최대 목록 수
let currentPage = 1;          //  현재 페이지

//검색 조건 저장
let selectedCgLocaSave = {level1 : '', level2 : ''};
let selectedTypeCgSave = ''; 
let searchedTextSave = ''; 

// 네이버 지도 생성
const mapOptions = {
  center: new naver.maps.LatLng(35.5352, 129.3109),
  zoom: 8
};
const map = new naver.maps.Map('map', mapOptions);
const mapUtil = new MapUtile(map);

// 지역 카테고리 대분류 랜더링 
RenderingUlTagLv1($cgListsByLoca1,categoryLoca_lv1);

// 운동 카테고리 랜더링
RenderingUlTagLv1($cgListsByFacility,category_fctype);

// 지역 카테고리 대분류 클릭 이벤트
$cgListsByLoca1.addEventListener('click', ({target,currentTarget}) => {
  if(target.tagName != 'P') return;
  selectedLog.now = target;
  
  //카테고리 생성 로직
  $cgListsByLoca2.innerHTML = '';
  RenderingUlTagLv2($cgListsByLoca2,categoryLoca_lv2[`${target.id}`], target.id);
  $cgListsByLoca2.style.top = $cgListsByLoca1.children[0].offsetTop + $cgListsByLoca1.offsetHeight + 'px';
  $cgListsByLoca2.style.visibility = 'visible';

  //캡쳐링 단계(중분류 삭제)
  document.body.addEventListener('click', ({target}) => {
    if(target.closest('.search__cg-wrap .search__cg-loca2')) {
      return;
    }
    $cgListsByLoca2.style.top = 0;
    $cgListsByLoca2.style.visibility ='hidden';
    $cgListsByLoca2.innerHTML = '';
  },true)
});

// 지역 카테고리 중분류 클릭 이벤트
$cgListsByLoca2.addEventListener('click',({target,currentTarget}) => {
  if(target.tagName != 'P') {
    return;
  }
  const li_lv2 = currentTarget.querySelectorAll('li p');
  [...li_lv2].forEach(ele => {
    ele.style.backgroundColor = cgRemveColor;
  })
  // 객체 저장
  selectedCgLocaSave.level1 = target.classList[0];
  selectedCgLocaSave.level2 = target.textContent;
  target.style.backgroundColor = cgAddColor;
  //선택되어 있는 lv1 지역카테고리 스타일 적용
  if(selectedLog.now != '') {
    selectedLog.now.style.backgroundColor = cgAddColor;
  }
  //그전 카테고리의 스타일 제거 후 선택되어 있는 카테고리 스타일 적용 
  if(selectedLog.before != '') {
    selectedLog.before.style.backgroundColor = cgRemveColor;
    selectedLog.now.style.backgroundColor = cgAddColor;
  }
  //선택되어 있는 카테고리 요소 저장
  selectedLog.before = selectedLog.now;
});

//운동 카테고리 클릭 이벤트
$cgListsByFacility.addEventListener('click',({target,currentTarget}) => {
  if(target.tagName != 'P') return;
  const li_lv2 = currentTarget.querySelectorAll('li p');
  [...li_lv2].forEach(ele =>ele.style.backgroundColor = cgRemveColor);
  target.style.backgroundColor = cgAddColor;
  selectedTypeCgSave = target.id;
})

//목록on/off 버튼 클릭 이벤트 (목록 on/off 이벤트)
$openList.addEventListener('click',() => {
  actionOpenMenu();
})

//내 위치 클릭 이벤트 (내 위치 이벤트)
$myLocation.addEventListener('click', () => {
  if (!navigator.geolocation) {
    alert('내 위치정보를 지원하지 않는 브라우저입니다.');
    return;
  }

  //위치 불러오기
  navigator.geolocation.getCurrentPosition(
      position => mapUtil.makeMyMarker(position.coords.latitude, position.coords.longitude),
      positionError => alert('위치를 불러오는 중 오류가 생겼습니다.')
  );
});

// 검색 이벤트 (데이터 통신이 이루어지는 이벤트)
$searchForm.addEventListener('submit',(e) => {
  e.preventDefault();
  const ispossible = selectedCgLocaSave.level1 != '' && selectedTypeCgSave != '';
  if(!requstStatus) {
    alert("검색어를 조회하고 있습니다");
    return;
  } else if (!ispossible) {
    alert("카테고리를 선택해주세요");
    return;
  }
  searchedTextSave = $inputByText.value.trim('');
  $inputByText.value = '';

  //검색시작
  search();
})

//대분류 카테고리 ul태그 랜더링 함수 (ul > li > p)
function RenderingUlTagLv1(ulTag,arr) {
  arr.forEach(ele => {
    ulTag.appendChild(
      makeElements('li',null,
        makeElements('p',{id : ele.value},ele.text))
    );
  })
}

//중분류 카테고리 ul태그 랜더링 함수 (ul > li > p)
function RenderingUlTagLv2(ulTag,arr,parentLv) {
  arr.forEach((ele,idx) => {
    const pTag = document.createElement('p');
    pTag.textContent = ele;
    pTag.setAttribute('class',parentLv);
    const liTag = document.createElement('li');
    liTag.appendChild(pTag);
    ulTag.appendChild(liTag);

    if(idx == arr.length-1) {
      // close버튼 설정
      const closeLiTag = document.createElement('li');
      closeLiTag.setAttribute('class','loca-close-li')      
      const closeBtn = document.createElement('i');
      closeBtn.setAttribute('class','btn-close-loca fa-solid fa-circle-xmark');
      closeLiTag.appendChild(closeBtn);
      ulTag.appendChild(closeLiTag);

      closeBtn.addEventListener('click', () => {
        ulTag.querySelectorAll('li').forEach(ele => ele.remove());
        $cgListsByLoca2.style.top = 0;
        ulTag.style.visibility = 'hidden';  
      })
    }

    if(selectedCgLocaSave.level1 == parentLv && selectedCgLocaSave.level2 == ele) {
      pTag.style.backgroundColor = cgAddColor;
    }
  })
}

// 메뉴 오픈 함수
function actionOpenMenu() {
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

//운동시설 항목 생성 함수
function createList(itemData) {
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
                          makeElements('i',{class : 'fa-solid fa-map-location-dot fa-stack-1x contents-icon'})))),
              makeElements('div', {class: 'item-content__body'},
                  makeElements('p', {class: 'item-sub item-tel'}, itemData.fctel),
                  makeElements('p', {class: 'item-sub item-addr'},itemData.fcaddr))));

  //맵 유틸기능(지도이동)
  listWrap.querySelector('.move-map-icon').addEventListener('click',() => {
    mapUtil.moveMap(itemData.fclat,itemData.fclng)
  })

  //운동시설 제목 클릭 이벤트
  listWrap.querySelector('.item-title').addEventListener('click',() => {
    location.href = `/facilities/${itemData.fcno}`;
  })

  return listWrap;
}

//검색 함수
function search() {
  requstStatus = false;

  const text = searchedTextSave;
  const type = selectedTypeCgSave;
  const loca = selectedCgLocaSave.level2 == '전체' ? selectedCgLocaSave.level1 : `${selectedCgLocaSave.level1} ${selectedCgLocaSave.level2}`;

  // 검색 결과 수 조회
  const xhr = new XMLHttpRequest();
  const url = '/facilities/total';
  const queryPram = `?fcaddr=${loca}&fctype=${type}&fcname=${text}`;
  console.log(queryPram);
  xhr.open('GET',url + queryPram);
  xhr.send();

  xhr.addEventListener('readystatechange', () => {
    requstStatus = true;
    currentPage = 1;

    if (xhr.readyState == XMLHttpRequest.DONE) {
      if (xhr.status == 0 || (xhr.status >= 200 && xhr.status < 400)) {
        const jsonData = JSON.parse(xhr.responseText);

        //검색결과 체크
        if (jsonData.data.totalCount == 0) {
          alert('검색결과가 없습니다.');
          requstStatus = true;
          return;
        }

        //기존 목록 삭제
        [...$searchedLists.children].filter(ele => ele.tagName == 'LI')
        .forEach(ele => ele.remove());

        //총 검색 결과 표시
        $resultCount.textContent = jsonData.data.totalCount;

        //페이지네이션 생성
        const totalPage = Math.ceil(jsonData.data.totalCount / onePageNum);
        const paginationWrap = createPagination(totalPage);
        $searchedLists.appendChild(paginationWrap);

        //목록 on
        if (!$searchedLists.classList.contains('open-lists')) {
          actionOpenMenu();
        }
      } else {
        console.log("오류");
      }
    }
  });
}

//페이지네이션 생성 함수
function createPagination(totalPage) {

  //이전 페이지네이션 삭제
  $searchedLists.querySelector('.pagination-wrap')?.remove();

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
    a.textContent = startIdx;

    // 페이지 클릭 이벤트
    a.addEventListener('click', ({target}) => {
      requstStatus = false;
      currentPage = parseInt(target.textContent);  //현재 페이지 저장

      const loca = selectedCgLocaSave.level2 == '전체' ? selectedCgLocaSave.level1 : `${selectedCgLocaSave.level1} ${selectedCgLocaSave.level2}`;
      const type = selectedTypeCgSave;
      const text = searchedTextSave;
      const pageNO = currentPage;
      const numOfRow = onePageNum;

      //검색 결과 조회
      const xhr = new XMLHttpRequest();
      const url = '/facilities/list';
      const queryPram = `?fcaddr=${loca}&fctype=${type}&fcname=${text}&pageNo=${pageNO}&numOfRow=${numOfRow}`;
      console.log(queryPram);
      xhr.open('GET',url + queryPram);
      xhr.send();

      xhr.addEventListener('readystatechange', () => {
        if (xhr.readyState == XMLHttpRequest.DONE) {
          if (xhr.status == 0 || (xhr.status >= 200 && xhr.status < 400)) {
            requstStatus = true;
            const jsonData = JSON.parse(xhr.responseText);

            //이전 목록들 초기화
            [...$searchedLists.children].filter(ele => ele.classList.contains('searched-lists__item'))
            .forEach(ele => ele.remove());

            //새 목록 생성
            jsonData.data.facilities.forEach(ele => $searchedLists.prepend(createList(ele)));
            $searchedLists.scrollTop = 0;

            //운동시설 마커 생성
            mapUtil.makeMarkers(jsonData.data.facilities);

            //클릭 표시
            [...$searchedLists.querySelectorAll('a')].forEach(ele => ele.classList.remove('on'));
            target.classList.add('on');

          } else {
            console.log("오류");
          }
        }
      });

    });
    page.appendChild(a);
    paginationLis.push(page);
    a.textContent == currentPage && a.click();
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
      console.log(currentPage)

      currentPage = limitPage*pageLv - (limitPage*2-1);
      const paginationWrap = createPagination(totalPage);
      $searchedLists.appendChild(paginationWrap);
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
      console.log(limitPage)

      currentPage = limitPage*pageLv + 1;
      const paginationWrap = createPagination(totalPage);
      $searchedLists.appendChild(paginationWrap);
    })
  }

  paginationLis.forEach(ele => pagination.appendChild(ele))
  return paginationWrap;
}
