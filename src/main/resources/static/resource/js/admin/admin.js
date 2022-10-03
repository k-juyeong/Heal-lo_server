manageMember();
memberAll();

// ** 회원 계정 관리 **

// 회원 계정 관리 클릭
function manageMember(){
  document.getElementById('submenu').style.display = 'none';
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

        const result =
          `<tr>
            <td>${member.memno}</td>
            <td>${member.memid}</td>
            <td>${member.memnickname}</td>
            <td>${member.memcdate}</td>
            <td><button class="btn btn_delete" onclick="delMember('${member.memid}')">탈퇴</button></td>
           </tr>`;
          return result;
      }).join('')
      }).catch(err=>console.log(err));
}

// 회원 닉네임,아이디로 검색

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

        const result =
          `<tr>
            <td>${member.memno}</td>
            <td>${member.memid}</td>
            <td>${member.memnickname}</td>
            <td>${member.memcdate}</td>
            <td><button class="btn btn_delete" onclick="delMember('${member.memid}')">탈퇴</button></td>
           </tr>`;
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
  // boardAll();
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
            <td>${board.bdtitle}</td>
            <td>${board.memnickname}</td>
            <td>${board.bdcdate}</td>
            <td><button class="btn btn_delete" onclick="delBoard(${board.bdno})">탈퇴</button></td>
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
            <td>${board.bdtitle}</td>
            <td>${board.memnickname}</td>
            <td>${board.bdcdate}</td>
            <td><button class="btn btn_delete" onclick="delBoard(${board.bdno})">탈퇴</button></td>
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
            <td>${board.bdtitle}</td>
            <td>${board.memnickname}</td>
            <td>${board.bdcdate}</td>
            <td><button class="btn btn_delete" onclick="delBoard(${board.bdno})">탈퇴</button></td>
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

      }).catch(err=>console.log(err));
}


// ** 게시물 - 댓글 관리 **

// 게시물 - 댓글
function manageReply(){
  document.querySelector('.th__2').textContent = "댓글 내용";
  document.querySelector('.th__3').textContent = "작성자";
  document.querySelector('.th__4').textContent = "작성날짜";
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
            <td>${reply.rpcomment}</td>
            <td>${reply.memnickname}</td>
            <td>${reply.rpcdate}</td>
            <td><button class="btn btn_delete" onclick="delReply(${reply.rpno})">탈퇴</button></td>
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
            <td>${reply.rpcomment}</td>
            <td>${reply.memnickname}</td>
            <td>${reply.rpcdate}</td>
            <td><button class="btn btn_delete" onclick="delReply(${reply.rpno})">탈퇴</button></td>
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
            <td>${reply.rpcomment}</td>
            <td>${reply.memnickname}</td>
            <td>${reply.rpcdate}</td>
            <td><button class="btn btn_delete" onclick="delReply(${reply.rpno})">탈퇴</button></td>
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

      }).catch(err=>console.log(err));
}

// ** 게시물 - 리뷰 관리 **

// 게시물 - 리뷰
function manageReview(){
  document.querySelector('.th__2').textContent = "리뷰 내용";
  document.querySelector('.th__3').textContent = "작성자";
  document.querySelector('.th__4').textContent = "운동시설";
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
            <td>${review.rvcontents}</td>
            <td>${review.memnickname}</td>
            <td>${review.fcno}</td>
            <td><button class="btn btn_delete" onclick="delReview(${review.rvno})">탈퇴</button></td>
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
            <td>${review.rvcontents}</td>
            <td>${review.memnickname}</td>
            <td>${review.fcno}</td>
            <td><button class="btn btn_delete" onclick="delReview(${review.rvno})">탈퇴</button></td>
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
            <td>${review.rvcontents}</td>
            <td>${review.memnickname}</td>
            <td>${review.fcno}</td>
            <td><button class="btn btn_delete" onclick="delReview(${review.rvno})">탈퇴</button></td>
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
            <td>${review.rvcontents}</td>
            <td>${review.memnickname}</td>
            <td>${review.fcno}</td>
            <td><button class="btn btn_delete" onclick="delReview(${review.rvno})">탈퇴</button></td>
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

      }).catch(err=>console.log(err));
}

// ** 게시물 - 문의글 관리 **

// ** 운동시설 정보 수정 **
