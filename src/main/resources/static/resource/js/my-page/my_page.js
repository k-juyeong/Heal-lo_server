  //탈퇴버튼 클릭시
  btnOut.addEventListener('click',e=>{
    if(!confirm("탈퇴 하시겠습니까?")) return;
    const memid = document.querySelector('input[name=memid]').value;
    const url = `/members/${memid}/del`;
    location.href = url;
  });

  //중복확인 버튼 클릭
  btnCheck.addEventListener('click',e=>{
    const url = `/members/nickname-check?nickname=${nname.value}`
    fetch(
        url,{
          method: 'GET',
          headers: {
              'Accept': 'application/json'
          }
        })
          .then(res=>res.json())
          .then(data => {
            if (data.header.code == '00') {
                console.log(data);
                document.getElementById('nname').style.border = '3px solid blue';
                document.querySelector('.nnameCk').innerHTML = '사용가능한 닉네임입니다';
                document.querySelector('.nnameCk').classList.remove('nnameCkRed');
                document.querySelector('.nnameCk').classList.add('nnameCkBlue');
            } else if (data.header.code == '01') {
                document.getElementById('nname').style.border = '3px solid red';
                document.querySelector('.nnameCk').innerHTML = '동일한 닉네임이 존재합니다';
                document.querySelector('.nnameCk').classList.remove('nnameCkBlue');
                document.querySelector('.nnameCk').classList.add('nnameCkRed');
            } else {
                throw new Error(data.data);
            }
          })
          .catch(err => console.log(err))
  });