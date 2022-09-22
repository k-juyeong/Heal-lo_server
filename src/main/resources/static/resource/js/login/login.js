
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
          // $msg.textContent = '아이디를 입력하세요';
          // $msg.style.color = 'red';
          // $msg.style.fontWeight = 'bold';
          // $msg.style.fontSize = '0.8em';
          //alert('아이디를 입력하세요.');
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
