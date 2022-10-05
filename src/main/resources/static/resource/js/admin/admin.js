manageMember();
memberAll();

// ** 회원 계정 관리 **
// 계정 or 게시물 관리 선택 시 구분
function changeCategory(cate) {
  const menu = document.querySelector('.menu-lists');
  const count = menu.classList.length;
  if(count === 1) {
    addClassList(cate);
  } else if(count >= 2) {
    for(let i=1; i<count; i++) {
      const classname = menu.classList[i];
      menu.classList.remove(classname);
    }
    addClassList(cate);
  }

  function addClassList(cate) {
    switch (cate) {
          case "member":
            menu.classList.add("member");
            break;
          case "board":
            menu.classList.add("board");
            break;
          case "reply":
            menu.classList.add("reply");
            break;
          case "review":
            menu.classList.add("review");
            break;
          case "notice":
            menu.classList.add("notice");
            break;
          default:
            break;
        }
  }
  console.log(menu);
}

// 회원 계정 관리 클릭
function manageMember(){
  document.getElementById('submenu').style.display = 'none';
  document.querySelector('.th__2').textContent = "아이디";
  document.querySelector('.th__3').textContent = "닉네임";
  document.querySelector('.th__4').textContent = "가입날짜";
  const member = "member";
  changeCategory(member);
  memberAll();
  changeOptionForMember();
}

// 회원 계정 검색 옵션
function changeOptionForMember() {
  document.querySelector('.searchType1').textContent = '닉네임';
  document.querySelector('.searchType2').textContent = '아이디';
  const type = document.querySelector('.searchType');
  const count = type.childElementCount;
  if (count === 3) {
    type.lastElementChild.remove();
  }
}

// 회원 전체 목록
function memberAll(){
  const url = `http://localhost:9080/admin/memberAll`;
  fetch(url, {
    method: 'GET',
    headers: {
      'Accept' : 'application/json'
    }
  }).then(res=>res.json())
    .then(res=>{
      console.log(res.data.memberAll);
      document.querySelector('#container .content .list-table tbody').innerHTML =
      res.data.memberAll.map(member=>{
      // 탈퇴한 회원 제외하기
        let result = '';
        if (member.memstatus == 'JOIN') {
          result =
            `<tr>
              <td>${member.memno}</td>
              <td>${member.memid}</td>
              <td>${member.memnickname}</td>
              <td>${member.memcdate}</td>
              <td><button class="btn btn_delete" onclick="delMember('${member.memid}')">탈퇴</button></td>
             </tr>`;
        }
          return result;
      }).join('')
      }).catch(err=>console.log(err));
}

// 검색 값 가져오기
function getInput() {
  const input = document.getElementById('search_spot').value;
  return input;
}

// 검색 입력창 clear
function clearInput() {
  document.getElementById('search_spot').value = '';
}

// 검색어로 검색
function search() {
  const input = getInput();

  const menu = document.querySelector('.menu-lists').classList[1];
  console.log(menu);
  switch(menu) {
      // 회원 닉네임, 아이디
      case "member":
        memberByMemInfo(input);
        clearInput();
        break;
      case "board":
        searchBoard(input);
        clearInput();
        break;
      case "reply":
        searchReply(input);
        clearInput();
        break;
      case "review":
        searchReview(input);
        clearInput();
        break;
      case "notice":
        clearInput();
        break;
      default:
        clearInput();
        break;
    }



  // 게시물 - 게시글 제목
  // 게시물 - 게시글 작성자
  function searchBoard(input) {
    const type = document.querySelector('.searchType');
    const selectedType = type.options[type.selectedIndex].text;
    if (selectedType === '제목') {
      boardByTitle(input);
    } else if (selectedType === '작성자') {
      boardByMemInfo(input);
    }
  }

  // 게시물 - 댓글 내용
  // 게시물 - 댓글 작성자
  function searchReply(input) {
    const type = document.querySelector('.searchType');
    const selectedType = type.options[type.selectedIndex].text;
    if (selectedType === '내용') {
      replyByContent(input);
    } else if (selectedType === '작성자') {
      replyByMemInfo(input);
    }
  }

  // 게시물 - 리뷰 내용
  // 게시물 - 리뷰 작성자
  // 게시물 - 리뷰 운동시설
  function searchReview(input) {
    const type = document.querySelector('.searchType');
    const selectedType = type.options[type.selectedIndex].text;
    if (selectedType === '내용') {
      reviewByContent(input);
    } else if (selectedType === '작성자') {
      reviewByMemInfo(input);
    } else if (selectedType === '운동시설') {
      reviewByFcName(input);
    }
  }

  // 게시물 - 문의글 제목
  // 게시물 - 문의글 작성자
}

// 회원 닉네임,아이디 검색 시 회원 목록
function memberByMemInfo(memInfo){
  const url = `http://localhost:9080/admin/member/${memInfo}`;
  fetch(url, {
    method: 'GET',
    headers: {
      'Accept' : 'application/json'
    }
  }).then(res=>res.json())
    .then(res=>{
      console.log(res.data.memberList);
      document.querySelector('#container .content .list-table tbody').innerHTML =
      res.data.memberList.map(member=>{
      // 탈퇴한 회원 제외하기
        let result = '';
        if (member.memstatus == 'JOIN') {
          result =
            `<tr>
              <td>${member.memno}</td>
              <td>${member.memid}</td>
              <td>${member.memnickname}</td>
              <td>${member.memcdate}</td>
              <td><button class="btn btn_delete" onclick="delMember('${member.memid}')">탈퇴</button></td>
             </tr>`;
        }
          return result;
      }).join('')
      }).catch(err=>console.log(err));
}

// 회원 계정 삭제 - 탈퇴 버튼 클릭 시
function delMember(memid){
  if(!confirm('회원계정을 삭제하시겠습니까?')) return;

  const url = `http://localhost:9080/admin/member/${memid}`;
    fetch(url, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
      },
    }).then(res=>res.json())
      .then(data=>{
        console.log(data);
        memberAll();

      }).catch(err=>console.log(err));
}

// ** 게시물 - 게시글 관리 **

// 게시물 관리 클릭
function managePost(){
  document.getElementById('submenu').style.display = 'block';
  document.querySelector('.th__2').textContent = "제목";
  document.querySelector('.th__3').textContent = "작성자";
  document.querySelector('.th__4').textContent = "작성날짜";
}

// 게시물 - 전체 게시글
function manageBoard(){
  document.querySelector('.th__2').textContent = "제목";
  document.querySelector('.th__3').textContent = "작성자";
  document.querySelector('.th__4').textContent = "작성날짜";
  const board = "board";
  changeCategory(board);
  boardAll();
  changeOptionsForBoard();
}

// 게시물 - 게시글 검색 옵션
function changeOptionsForBoard() {
  document.querySelector('.searchType1').textContent = '제목';
  document.querySelector('.searchType2').textContent = '작성자';
  const type = document.querySelector('.searchType');
  const count = type.childElementCount;
  if (count === 3) {
    type.lastElementChild.remove();
  }
}

// 게시물 - 전체 게시글 목록
function boardAll(){
  const url = `http://localhost:9080/admin/boardAll`;
  fetch(url, {
    method: 'GET',
    headers: {
      'Accept' : 'application/json'
    }
  }).then(res=>res.json())
    .then(res=>{
      console.log(res.data.boardAll);
      document.querySelector('#container .content .list-table tbody').innerHTML =
      res.data.boardAll.map(board=>{
        const result =
          `<tr>
            <td>${board.bdno}</td>
            <td><a href="/boards/list/${board.bdno}/detail">${board.bdtitle}</a></td>
            <td>${board.memnickname}</td>
            <td>${board.bdcdate}</td>
            <td><button class="btn btn_delete" onclick="delBoard(${board.bdno})">삭제</button></td>
           </tr>`;
          return result;
      }).join('')
      }).catch(err=>console.log(err));
}

// 게시물 - 게시글 제목 검색
function boardByTitle(title){
  const url = `http://localhost:9080/admin/board/list/${title}`;
  fetch(url, {
    method: 'GET',
    headers: {
      'Accept' : 'application/json'
    }
  }).then(res=>res.json())
    .then(res=>{
      console.log(res.data.boardList);
      document.querySelector('#container .content .list-table tbody').innerHTML =
      res.data.boardList.map(board=>{
        const result =
          `<tr>
            <td>${board.bdno}</td>
            <td><a href="/boards/list/${board.bdno}/detail">${board.bdtitle}</a></td>
            <td>${board.memnickname}</td>
            <td>${board.bdcdate}</td>
            <td><button class="btn btn_delete" onclick="delBoard(${board.bdno})">삭제</button></td>
           </tr>`;
          return result;
      }).join('')
      }).catch(err=>console.log(err));
}

// 게시물 - 게시글 작성자 검색
function boardByMemInfo(memInfo){
  const url = `http://localhost:9080/admin/board/${memInfo}`;
  fetch(url, {
    method: 'GET',
    headers: {
      'Accept' : 'application/json'
    }
  }).then(res=>res.json())
    .then(res=>{
      console.log(res.data.boardList);
      document.querySelector('#container .content .list-table tbody').innerHTML =
      res.data.boardList.map(board=>{
        const result =
          `<tr>
            <td>${board.bdno}</td>
            <td><a href="/boards/list/${board.bdno}/detail">${board.bdtitle}</a></td>
            <td>${board.memnickname}</td>
            <td>${board.bdcdate}</td>
            <td><button class="btn btn_delete" onclick="delBoard(${board.bdno})">삭제</button></td>
           </tr>`;
          return result;
      }).join('')
      }).catch(err=>console.log(err));
}

// 게시물 - 게시글 삭제
function delBoard(bdno){
  if(!confirm('해당 게시글을 삭제하시겠습니까?')) return;

  const url = `http://localhost:9080/admin/board/${bdno}`;
    fetch(url, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
      },
    }).then(res=>res.json())
      .then(data=>{
        console.log(data);
        boardAll();
      }).catch(err=>console.log(err));
}


// ** 게시물 - 댓글 관리 **

// 게시물 - 댓글
function manageReply(){
  document.querySelector('.th__2').textContent = "댓글 내용";
  document.querySelector('.th__3').textContent = "작성자";
  document.querySelector('.th__4').textContent = "작성날짜";
  const reply = "reply";
  changeCategory(reply);
  replyAll();
  changeOptionsForReply();
}
// 게시물 - 게시글 검색 옵션
function changeOptionsForReply() {
  document.querySelector('.searchType1').textContent = '내용';
  document.querySelector('.searchType2').textContent = '작성자';
  const type = document.querySelector('.searchType');
  const count = type.childElementCount;
  if (count === 3) {
    type.lastElementChild.remove();
  }
}

// 게시물 - 댓글 전체 목록
function replyAll(){
  const url = `http://localhost:9080/admin/replyAll`;
  fetch(url, {
    method: 'GET',
    headers: {
      'Accept' : 'application/json'
    }
  }).then(res=>res.json())
    .then(res=>{
      console.log(res.data.replyAll);
      document.querySelector('#container .content .list-table tbody').innerHTML =
      res.data.replyAll.map(reply=>{
        const result =
          `<tr>
            <td>${reply.rpno}</td>
            <td><a href="/boards/list/${reply.bdno}/detail">${reply.rpComment}</a></td>
            <td>${reply.memnickname}</td>
            <td>${reply.rpCDate}</td>
            <td><button class="btn btn_delete" onclick="delReply(${reply.rpno})">삭제</button></td>
           </tr>`;
          return result;
      }).join('')
      }).catch(err=>console.log(err));
}

// 게시물 - 댓글 내용 검색
function replyByContent(content){
  const url = `http://localhost:9080/admin/reply/list/${content}`;
  fetch(url, {
    method: 'GET',
    headers: {
      'Accept' : 'application/json'
    }
  }).then(res=>res.json())
    .then(res=>{
      console.log(res.data.replyList);
      document.querySelector('#container .content .list-table tbody').innerHTML =
      res.data.replyList.map(reply=>{
        const result =
          `<tr>
            <td>${reply.rpno}</td>
            <td><a href="/boards/list/${reply.bdno}/detail">${reply.rpComment}</a></td>
            <td>${reply.memnickname}</td>
            <td>${reply.rpCDate}</td>
            <td><button class="btn btn_delete" onclick="delReply(${reply.rpno})">삭제</button></td>
           </tr>`;
          return result;
      }).join('')
      }).catch(err=>console.log(err));
}

// 게시물 - 댓글 작성자 검색
function replyByMemInfo(memInfo){
  const url = `http://localhost:9080/admin/reply/${memInfo}`;
  fetch(url, {
    method: 'GET',
    headers: {
      'Accept' : 'application/json'
    }
  }).then(res=>res.json())
    .then(res=>{
      console.log(res.data.replyList);
      document.querySelector('#container .content .list-table tbody').innerHTML =
      res.data.replyList.map(reply=>{
        const result =
          `<tr>
            <td>${reply.rpno}</td>
            <td><a href="/boards/list/${reply.bdno}/detail">${reply.rpComment}</a></td>
            <td>${reply.memnickname}</td>
            <td>${reply.rpCDate}</td>
            <td><button class="btn btn_delete" onclick="delReply(${reply.rpno})">삭제</button></td>
           </tr>`;
          return result;
      }).join('')
      }).catch(err=>console.log(err));
}

// 게시물 - 댓글 삭제
function delReply(rpno){
  if(!confirm('해당 게시글을 삭제하시겠습니까?')) return;

  const url = `http://localhost:9080/admin/reply/${rpno}`;
    fetch(url, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
      },
    }).then(res=>res.json())
      .then(data=>{
        console.log(data);
        replyAll();
      }).catch(err=>console.log(err));
}

// ** 게시물 - 리뷰 관리 **

// 게시물 - 리뷰
function manageReview(){
  document.querySelector('.th__2').textContent = "리뷰 내용";
  document.querySelector('.th__3').textContent = "작성자";
  document.querySelector('.th__4').textContent = "운동시설";
  const review = "review";
  changeCategory(review);
  reviewAll();
  changeOptionsForReview();
}

// 게시물 - 리뷰 검색 옵션
function changeOptionsForReview() {
  document.querySelector('.searchType1').textContent = '내용';
  document.querySelector('.searchType2').textContent = '작성자';
  const option = document.createElement('option');
  option.classList.add('searchType3');
  option.textContent = '운동시설';
  document.querySelector('.searchType').appendChild(option);
}

// 게시물 - 리뷰 전체 목록
function reviewAll(){
  const url = `http://localhost:9080/admin/reviewAll`;
  fetch(url, {
    method: 'GET',
    headers: {
      'Accept' : 'application/json'
    }
  }).then(res=>res.json())
    .then(res=>{
      console.log(res.data.reviewAll);
      document.querySelector('#container .content .list-table tbody').innerHTML =
      res.data.reviewAll.map(review=>{
        const result =
          `<tr>
            <td>${review.rvno}</td>
            <td><a href="/facilities/${review.fcno}">${review.rvcontents}</a></td>
            <td>${review.member.memnickname}</td>
            <td>${review.facility.fcname}</td>
            <td><button class="btn btn_delete" onclick="delReview(${review.rvno})">삭제</button></td>
           </tr>`;
          return result;
      }).join('')
      }).catch(err=>console.log(err));
}

// 게시물 - 리뷰 내용 검색
function reviewByContent(content){
  const url = `http://localhost:9080/admin/review/list/${content}`;
  fetch(url, {
    method: 'GET',
    headers: {
      'Accept' : 'application/json'
    }
  }).then(res=>res.json())
    .then(res=>{
      console.log(res.data.reviewList);
      document.querySelector('#container .content .list-table tbody').innerHTML =
      res.data.reviewList.map(review=>{
        const result =
          `<tr>
            <td>${review.rvno}</td>
            <td><a href="/facilities/${review.fcno}">${review.rvcontents}</a></td>
            <td>${review.member.memnickname}</td>
            <td>${review.facility.fcname}</td>
            <td><button class="btn btn_delete" onclick="delReview(${review.rvno})">삭제</button></td>
           </tr>`;
          return result;
      }).join('')
      }).catch(err=>console.log(err));
}

// 게시물 - 리뷰 운동시설 검색
function reviewByFcName(fcName){
  const url = `http://localhost:9080/admin/review/search/${fcName}`;
  fetch(url, {
    method: 'GET',
    headers: {
      'Accept' : 'application/json'
    }
  }).then(res=>res.json())
    .then(res=>{
      console.log(res.data.reviewList);
      document.querySelector('#container .content .list-table tbody').innerHTML =
      res.data.reviewList.map(review=>{
        const result =
          `<tr>
            <td>${review.rvno}</td>
            <td><a href="/facilities/${review.fcno}">${review.rvcontents}</a></td>
            <td>${review.member.memnickname}</td>
            <td>${review.facility.fcname}</td>
            <td><button class="btn btn_delete" onclick="delReview(${review.rvno})">삭제</button></td>
           </tr>`;
          return result;
      }).join('')
      }).catch(err=>console.log(err));
}

// 게시물 - 리뷰 작성자 검색
function reviewByMemInfo(memInfo){
  const url = `http://localhost:9080/admin/review/${memInfo}`;
  fetch(url, {
    method: 'GET',
    headers: {
      'Accept' : 'application/json'
    }
  }).then(res=>res.json())
    .then(res=>{
      console.log(res.data.reviewList);
      document.querySelector('#container .content .list-table tbody').innerHTML =
      res.data.reviewList.map(review=>{
        const result =
          `<tr>
            <td>${review.rvno}</td>
            <td><a href="/facilities/${review.fcno}">${review.rvcontents}</a></td>
            <td>${review.member.memnickname}</td>
            <td>${review.facility.fcname}</td>
            <td><button class="btn btn_delete" onclick="delReview(${review.rvno})">삭제</button></td>
           </tr>`;
          return result;
      }).join('')
      }).catch(err=>console.log(err));
}

// 게시물 - 리뷰 삭제
function delReview(rvno){
  if(!confirm('해당 리뷰를 삭제하시겠습니까?')) return;

  const url = `http://localhost:9080/admin/review/${rvno}`;
    fetch(url, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
      },
    }).then(res=>res.json())
      .then(data=>{
        console.log(data);
        reviewAll();
      }).catch(err=>console.log(err));
}

// ** 게시물 - 문의글 관리 **

// 게시물 - 문의글
function manageNotice(){
  document.querySelector('.th__2').textContent = "제목";
  document.querySelector('.th__3').textContent = "작성자";
  document.querySelector('.th__4').textContent = "작성날짜";
  const notice = "notice";
  changeCategory(notice);
  noticeAll();
  changeOptionsForBoard();
}

// 게시물 - 문의글 검색 옵션
function changeOptionsForBoard() {
  document.querySelector('.searchType1').textContent = '제목';
  document.querySelector('.searchType2').textContent = '작성자';
  const type = document.querySelector('.searchType');
  const count = type.childElementCount;
  if (count === 3) {
    type.lastElementChild.remove();
  }
}

// 게시물 - 문의글 목록
function noticeAll(){
  const url = `http://localhost:9080/admin/noticeAll`;
  fetch(url, {
    method: 'GET',
    headers: {
      'Accept' : 'application/json'
    }
  }).then(res=>res.json())
    .then(res=>{
      console.log(res.data.noticeAll);
      document.querySelector('#container .content .list-table tbody').innerHTML =
      res.data.noticeAll.map(notice=>{
        const result =
          `<tr>
            <td>${notice.bdno}</td>
            <td><a href="/boards/list/${notice.bdno}/detail">${notice.bdtitle}</a></td>
            <td>${notice.memnickname}</td>
            <td>${notice.bdcdate}</td>
            <td><button class="btn btn_delete" onclick="delNotice(${notice.bdno})">삭제</button></td>
           </tr>`;
          return result;
      }).join('')
      }).catch(err=>console.log(err));
}

// 게시물 - 문의글 제목 검색
function noticeByTitle(title){
  const url = `http://localhost:9080/admin/notice/list/${title}`;
  fetch(url, {
    method: 'GET',
    headers: {
      'Accept' : 'application/json'
    }
  }).then(res=>res.json())
    .then(res=>{
      console.log(res.data.noticeList);
      document.querySelector('#container .content .list-table tbody').innerHTML =
      res.data.noticeList.map(notice=>{
        const result =
          `<tr>
            <td>${notice.bdno}</td>
            <td><a href="/boards/list/${notice.bdno}/detail">${notice.bdtitle}</a></td>
            <td>${notice.memnickname}</td>
            <td>${notice.bdcdate}</td>
            <td><button class="btn btn_delete" onclick="delNotice(${notice.bdno})">삭제</button></td>
           </tr>`;
          return result;
      }).join('')
      }).catch(err=>console.log(err));
}

// 게시물 - 문의글 작성자 검색
function noticeByMemInfo(memInfo){
  const url = `http://localhost:9080/admin/notice/${memInfo}`;
  fetch(url, {
    method: 'GET',
    headers: {
      'Accept' : 'application/json'
    }
  }).then(res=>res.json())
    .then(res=>{
      console.log(res.data.noticeList);
      document.querySelector('#container .content .list-table tbody').innerHTML =
      res.data.noticeList.map(notice=>{
        const result =
          `<tr>
            <td>${notice.bdno}</td>
            <td><a href="/boards/list/${notice.bdno}/detail">${notice.bdtitle}</a></td>
            <td>${notice.memnickname}</td>
            <td>${notice.bdcdate}</td>
            <td><button class="btn btn_delete" onclick="delNotice(${notice.bdno})">삭제</button></td>
           </tr>`;
          return result;
      }).join('')
      }).catch(err=>console.log(err));
}

// 게시물 - 문의글 삭제
function delNotice(bdno){
  if(!confirm('해당 문의글을 삭제하시겠습니까?')) return;

  const url = `http://localhost:9080/admin/board/${bdno}`;
    fetch(url, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
      },
    }).then(res=>res.json())
      .then(data=>{
        console.log(data);
        noticeAll();
      }).catch(err=>console.log(err));
}


// ** 운동시설 정보 수정 **
