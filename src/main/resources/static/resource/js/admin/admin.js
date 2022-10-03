manageMember();
memberAll();

// 회원 전체 목록
function memberAll(){
  const url = `http://localhost:9080/admin/memberAll`;
  fetch(url, {
    method: 'GET',
    headears: {
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

// 닉네임,아이디 검색


// 탈퇴 버튼 클릭
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

// 회원 계정 관리 클릭
function manageMember(){
  document.getElementById('submenu').style.display = 'none';
}

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
}

// 게시물 - 댓글
function manageReply(){
  document.querySelector('.th__2').textContent = "댓글 내용";
  document.querySelector('.th__3').textContent = "작성자";
  document.querySelector('.th__4').textContent = "작성날짜";
}


// 게시물 - 리뷰
function manageReview(){
  document.querySelector('.th__2').textContent = "리뷰 내용";
  document.querySelector('.th__3').textContent = "작성자";
  document.querySelector('.th__4').textContent = "운동시설";
}

