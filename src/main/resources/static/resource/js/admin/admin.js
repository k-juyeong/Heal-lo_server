
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
      // console.log(res);
      console.log(res.data);
      for(let i in res.data){
        for(let j in res.data[i]){
          console.log(res.data[i][j]);
          console.log(res.data[i][j].memno);
          const result =
                 `<td>${res.data[i][j].memno}</td>
                  <td>${res.data[i][j].memid}</td>
                  <td>${res.data[i][j].memnickname}</td>
                  <td>${res.data[i][j].memcdate}</td>
                  <td><button class="btn btn_delete">탈퇴</button></td>`;
//          const newLine = document.createElement('tr')
//          let newLine = document.querySelector('#container .content .list-table tbody').createElement('tr')
//          newLine.innerHTML = result;
//          document.querySelector('#container .content .list-table tbody').innerHTML = result;
        }
      }
    })
    .catch(err=>console.log(err));
}