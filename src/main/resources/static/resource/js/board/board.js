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
        const url = `/boards/1/${$searchType.value}/${$keyword.value}?bdcg=${category}`;
        location.href = url;
    }