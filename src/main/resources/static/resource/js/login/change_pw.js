const $changePw = document.getElementById('changePw');
const $changePwR = document.getElementById('changePwR');
const $btn = document.getElementById('btn');
const $form = document.getElementById('changePwContent');

$btn.addEventListener('click',e => {
    const pw1 = $changePw.value.trim('');
    const pw2 = $changePwR.value.trim('');
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
    }
);


