
// 게시글 번호
const bdno = document.getElementById('bdno').textContent;

// 댓글 수, 댓글 목록 불러오기
count(bdno);
all(bdno);

// 전역변수
const form = {
  reply:document.getElementById('rpComment')
}

// 작성한 댓글 가져오기
function getReply() {
  const rpComment = form.reply.value;

  return {rpComment};
}

// 댓글 등록하기
saveBtn.addEventListener('click', e=>{
  const reply = getReply();

  save(reply, bdno);
  clearTextarea();
});


// 댓글 수
function count(bdno) {
  const url = `http://localhost:9080/reply/count/${bdno}`;
  fetch(url, {
    method: 'GET',
    headers: {
      'Accept': 'application/json',
    }
  }).then(res=>res.json())
    .then(res=>{
//      console.log(res.data.count);
      const count = res.data.count;
      document.querySelector('.reply__count').textContent = count;
    })
    .catch(err=>console.log(err));
}

// 댓글 목록
function all(bdno){
  const url = `http://localhost:9080/reply/${bdno}`;
  fetch(url, {
    method: 'GET',
    headers: {
      'Accept': 'application/json',
    }
  }).then(res=>res.json())
    .then(res=>{
      console.log(res.data.replyList);
      document.querySelector('#reply .reply__main .reply__form').innerHTML =
      res.data.replyList.map(reply=>{
          const date = (reply.rpUDate).substr(0,10);
          let result = '';
          const rpno = reply.rpno;
          // 본인 댓글이면 수정, 삭제버튼 노출
          if (reply.login == true){
            result =
                   `<div class="reply__list">
                     <div class="reply__list_form">
                        <div class="reply__list nickname">${reply.memnickname}</div>
                        <div class="reply__list content">${reply.rpComment}</div>
                        <div class="reply__list date">${date}</div>
                        <div class="reply__list btngrp">
                          <button type="button" id="editIcon" onclick="editEditor(event, ${rpno})"><i class="fa-solid fa-pen-to-square"></i></button>
                          <button type="button" id="delIcon" onclick="del(${rpno})"><i class="fa-solid fa-x"></i></button>
                        </div>
                     </div>
                   </div>`;

          } else{
            result =
                   `<div class="reply__list">
                      <div class="reply__list nickname">${reply.memnickname}</div>
                      <div class="reply__list content">${reply.rpComment}</div>
                      <div class="reply__list date">${date}</div>
                   </div>`;
          }
          return result;
      }).join('')
    }).catch(err=>console.log(err));
}

// 댓글 등록
function save(reply, bdno){
  const url = `http://localhost:9080/reply/${bdno}`;
  fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
    body: JSON.stringify(reply)
  }).then(res=>res.json())
    .then(data=>{
      console.log(data)
      all(bdno);
      count(bdno);
    }).catch(err=>console.log(err));
}



// 수정아이콘 클릭 시
// 수정 에디터 출력 없으면 있으면 토글
function editEditor(event, rpno) {
  const ele = event.target.parentElement.parentElement.parentElement;
  const parentEle = ele.parentElement;
  console.log(parentEle);
  console.log(parentEle.childElementCount);

  if(parentEle.childElementCount == 2) {
    console.log(ele.nextElementSibling);
    const siblingEle = ele.nextElementSibling;
    siblingEle.remove();
  } else {
    const editor = document.createElement("div");
    editor.classList.add('reply__edit');
    editor.innerHTML =
       `<textarea class="reply__edit toolbox" rows="10"></textarea>
         <div class="reply__edit btngrp">
           <button type="button" id="editBtn" onclick="editChk(event, ${rpno})">수정</button>
           <button type="button" id="cancelBtn" onclick="cancel(event)">취소</button>
         </div>`;
    parentEle.append(editor);
  }
}

// 수정
function edit(reply, rpno) {
  console.log('click');
  console.log(rpno);
  console.log(reply);
    const url = `http://localhost:9080/reply/${rpno}`;
    fetch(url, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      },
      body: JSON.stringify(reply)
    }).then(res=>res.json())
      .then(data=>{
        console.log(data)
        all(bdno);
        count(bdno);
      }).catch(err=>console.log(err));
}


// 수정 버튼 클릭 시
function editChk(event, rpno){
  const tar = event.target.parentElement;
//  console.log(tar);
//  console.log(tar.previousElementSibling);

  function getUpdatedReply() {
    const rpComment = tar.previousElementSibling.value;

    return {rpComment};
  }
  const reply = getUpdatedReply();

  edit(reply, rpno);
}

// 수정 취소 버튼 클릭 시
function cancel(event) {
  const btn = event.target.parentElement;
  const replyEdit = btn.parentElement;
//  console.log(replyEdit);
  replyEdit.remove();
}

// 삭제 버튼 클릭 시
function del(rpno) {
  if(!confirm('삭제하시겠습니까?')) return;

  const url = `http://localhost:9080/reply/${rpno}`;
  fetch(url, {
    method: 'DELETE',
    headers: {
      'Accept': 'application/json',
    },
  }).then(res=>res.json())
    .then(data=>{
      console.log(data);
      all(bdno);
      count(bdno);
    }).catch(err=>console.log(err));
}

// textarea 비우기
function clearTextarea() {
  form.reply.value = '';
}
