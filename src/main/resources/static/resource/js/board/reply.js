
// 게시글 번호
const bdno = document.getElementById('bdno').textContent;

// 댓글 수, 댓글 목록 불러오기
count(bdno);
all(bdno);


// 전역변수
const form = {
  rpComment:document.getElementById('rpComment')
}

// 작성한 댓글 가져오기
function getReply() {
  const comment = form.rpComment.value;

  return comment;
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
      for(let i in res.data){
        for(let j in res.data[i]){
//          console.log(res.data[i][j].memnickname);
//          console.log(res.data[i][j].rpComment);
//          const date = (res.data[i][j].rpUDate).substr(0,10);
//          console.log(date);
          const date = (res.data[i][j].rpUDate).substr(0,10);
          const result =
                 `<div class="reply__list nickname">${res.data[i][j].memnickname}</div>
                  <div class="reply__list content">${res.data[i][j].rpComment}</div>
                  <div class="reply__list date">${date}</div>`;
          const newLine = document.createElement('div')
          newLine.classList.add("reply__list");
          document.querySelector('#reply .reply__main .reply__form').appendChild(newLine);
          newLine.innerHTML = result;
        }
      }
    })
    .catch(err=>console.log(err));
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
    }).catch(err=>console.log(err));
}

// 본인 댓글이면 수정, 삭제버튼 노출

// textarea 비우기
function clearTextarea() {
  form.rpComment.value = '';
}
