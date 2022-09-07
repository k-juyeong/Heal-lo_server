    //아이디 찾기 alert
    const $idNameInput = document.getElementById('idName');
    const $idEamilInput = document.getElementById('idEmail');
    const $idBtn = document.getElementById('idBtn');
    $idBtn.addEventListener('click',e=>{
      if($idNameInput.value == ''){
        alert('이름을 입력해주세요!');
      }else if($idEamilInput.value == ''){
        alert('이메일을 입력해주세요!');
      }
    });
    //비밀번호 찾기 alert
    const $pwIdInput = document.getElementById('pwId');
    const $pwNameInput = document.getElementById('pwName');
    const $pwEmailInput = document.getElementById('pwEmail');
    const $pwBtn = document.getElementById('pwBtn');
    $pwBtn.addEventListener('click',e=>{
      if($pwIdInput.value == ''){
        alert('아이디를 입력해주세요!');
      }else if($pwNameInput.value == ''){
        alert('이름을 입력해주세요!');
      }else if($pwEmailInput.value == ''){
        alert('이메일을 입력해주세요!');
      }
    });