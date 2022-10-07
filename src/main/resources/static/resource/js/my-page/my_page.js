  //탈퇴버튼 클릭시
  btnOut.addEventListener('click',e=>{
    if(!confirm("탈퇴 하시겠습니까?")) return;
    const memid = document.querySelector('input[name=memid]').value;
    const url = `/members/${memid}/del`;
    location.href = url;
  });

    const $nnameCk = document.querySelector('.nnameCk');
    const $btnChange = document.getElementById('btnChange');
    const $form = document.getElementById('form');
<<<<<<< HEAD
    const $phoneNb = document.getElementById('phoneNb');
=======
    const $changePw = document.getElementById('changePw');
    const $changePwCh = document.getElementById('changePwCh');
>>>>>>> f957e88bf96440ec20d16a186d4b91021c82dfc4
    let dupNick = false;

  $phoneNb.addEventListener('input',(e) => {
      const length = e.target.value.length;

      if (e.inputType == "deleteContentBackward") {
          if (length == 4 || length == 9) {
              e.target.value = e.target.value.slice(0, e.target.value.length - 1);
          }
      } else {
          if (length == 4 || length == 9) {
              const lastIn = e.target.value.slice(e.target.value.length - 1, e.target.value.length)
              e.target.value = e.target.value.slice(0, e.target.value.length - 1) + '-' + lastIn;
          }

          if (length == 3 || length == 8) {
              e.target.value += '-';
          }
      }
  })

    $btnChange.addEventListener('click',e=>{
        e.preventDefault();
        const pw1 = $changePw.value.trim('');
        const pw2 = $changePwCh.value.trim('');
        if(dupNick == false){
            alert("닉네임 중복확인을 해주세요");
            return;
        }
        if(pw1 == ''){
            alert('비밀번호를 입력해주세요!');
            return;
        }

        if(pw2 == ''){
            alert('비밀번호를 재입력해주세요!');
            return;
        }

        if (pw1 != pw2) {
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }
        $form.submit();

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
                $nnameCk.innerHTML = '사용가능한 닉네임입니다';
                $nnameCk.classList.remove('nnameCkRed');
                $nnameCk.classList.add('nnameCkBlue');
                dupNick = true;
            } else if (data.header.code == '01') {
                document.getElementById('nname').style.border = '3px solid red';
                $nnameCk.innerHTML = '동일한 닉네임이 존재합니다';
                $nnameCk.classList.remove('nnameCkBlue');
                $nnameCk.classList.add('nnameCkRed');
            } else {
                throw new Error(data.data);
            }
          })
          .catch(err => console.log(err))
  });