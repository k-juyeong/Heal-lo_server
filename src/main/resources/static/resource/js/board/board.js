      const $writeBtn = document.getElementById('writeBtn');
       $writeBtn?.addEventListener('click', (evt) => {
             location.href = '/boards/add';
       });


      const $keyword  = document.getElementById('keyword');
      const $searchBtn = document.getElementById('searchBtn');
      const $searchType = document.getElementById('searchType');


    //검색 버튼 클릭시
    $searchBtn?.addEventListener('click', search_f);

    //키워드 입력필드에 엔터키 눌렀을때 검색
    $keyword?.addEventListener('keydown', e=>{
        if(e.key === 'Enter') {
            search_f(e);
        }
    });

    function search_f(e){
        //검색어입력 유무체크
        if($keyword.value.trim().length === 0){
           alert('검색어를 입력하세요');
           $keyword.focus();$keyword.select(); //커서이동
           return false;
        }
        const url = `/boards/list/1/${$searchType.value}/${$keyword.value}?category=${container.dataset.code}`;
        location.href = url;
    }

//   function timeForToday(value) {
//           const today = new Date();
//           const timeValue = new Date(value);
//
//           const betweenTime = Math.floor((today.getTime() - timeValue.getTime()) / 1000 / 60);
//           if (betweenTime < 1) return '방금전';
//           if (betweenTime < 60) {
//               return `${betweenTime}분전`;
//           }
//
//           const betweenTimeHour = Math.floor(betweenTime / 60);
//           if (betweenTimeHour < 24) {
//               return `${betweenTimeHour}시간전`;
//           }
//
//           const betweenTimeDay = Math.floor(betweenTime / 60 / 24);
//           if (betweenTimeDay < 365) {
//               return `${betweenTimeDay}일전`;
//           }
//
//           return `${Math.floor(betweenTimeDay / 365)}년전`;
//    }



    //수정이동
    const $updateBtn = document.querySelector('.updatebtn');
    $updateBtn.addEventListener('click', (evt) => {
      if (confirm('수정하시겠습니까?')) {
        const bdno = document.getElementById('bdno').textContent;
        const url = `/boards/${bdno}/edit`;
        location.href = url;
      }
    });

    //삭제
    const $deleteBtn = document.querySelector('.deletebtn');
    $deleteBtn.addEventListener('click', (evt) => {
      if (confirm('삭제하시겠습니까?')) {
        const bdno = document.getElementById('bdno').textContent;
        const url = `/boards/${bdno}/del`;
        location.href = url;
      }
    });

    //목록
//    const $listBtn = document.querySelector('.listbtn');
//    $listBtn.addEventListener('click', (evt) => {
//        console.log("diffMemno");
//
////    const url = `/boards/list?category=${container.dataset.code}`;
//      const url = `/boards/list?category=${bdcg}`;
//      location.href = url;
//    });


//     const $listBtn2 = document.querySelector('.listbtn2');
//     $listBtn2.addEventListener('click', (evt) => {
//     console.log("diffMemno");
//       const url = `/boards/list?category=${container.dataset.code}`;
//       location.href = url;
//     });