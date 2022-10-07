const $joinBtn = document.getElementById('joinBtn');
  $joinBtn.addEventListener('click', function (e) {
      e.preventDefault();
      document.location.href = '/members/join';
  });

  const $loginBtn = document.getElementById('loginBtn');
  $loginBtn.addEventListener('click', function () {
    const $id = document.getElementById('id');
    const $pwd = document.getElementById('pwd');
    const $msg = document.getElementById('msg');
    if ($id.value.trim().length == 0) {
      alertMsg($msg, '아이디를 입력하세요.');
      return;
    }
    if ($pwd.value.trim().length == 0) {
      alertMsg($msg, '비밀번호를 입력하세요.');
      return;
    }
    alert('네이버로 이동합니다!');
    document.location.href = 'http://www.naver.com';
  });

  function alertMsg(tag, msg) {
    tag.textContent = msg;
    tag.style.color = 'red';
    tag.style.fontWeight = 'bold';
    tag.style.fontSize = '0.8em';
  }

  document.getElementById('naver').addEventListener('click', e => {
    e.preventDefault();
    const searchPrams = new URLSearchParams(location.search);
    fetch(`/members/naver-join?requestURI=${searchPrams.get("requestURI")}`, {
      method: `GET`,
      headers: {
        'Accept': 'application/json'
      }
    })
        .then(res => res.json())
        .then(jsonData => location.href = jsonData.data.redirect)
        .catch(err => console.log(err));
  });




