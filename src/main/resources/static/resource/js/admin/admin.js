manageMember();
memberAll();

// 회원 전체 목록
function memberAll(){
  const url = `http://localhost:9080/admin/manage/memberAll`;
  fetch(url, {
    method: 'GET',
    headears: {
      'Accept' : 'application/json'
    }
  }).then(res=>res.json())
    .then(res=>{
      for(let i in res.data){
        for(let j in res.data[i]){
          console.log(res.data[i][j].memno);
          const result =
                 `<td>${res.data[i][j].memno}</td>
                  <td>${res.data[i][j].memid}</td>
                  <td>${res.data[i][j].memnickname}</td>
                  <td>${res.data[i][j].memcdate}</td>
                  <td><button class="btn btn_delete">탈퇴</button></td>`;
          const newLine = document.createElement('tr')
          document.querySelector('#container .content .list-table tbody').appendChild(newLine);
          newLine.innerHTML = result;
        }
        }
      })
    .catch(err=>console.log(err));
}

// 닉네임 검색

// 아이디 검색

// 탈퇴 버튼 클릭

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
//  document.querySelector('.th__3').textContent = "작성자";
//  document.querySelector('.th__4').textContent = "작성날짜";
}


// 게시물 - 리뷰
function manageReview(){
  document.querySelector('.th__2').textContent = "리뷰 내용";
//  document.querySelector('.th__3').textContent = "작성자";
  document.querySelector('.th__4').textContent = "운동시설";
}

