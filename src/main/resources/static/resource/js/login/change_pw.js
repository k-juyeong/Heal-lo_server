const $changePw = document.getElementById('changePw');
const $changePwR = document.getElementById('changePwR');
const $btn = document.getElementById('btn');


$btn.addEventListener('click',e => {
        if($changePw.value == ''){
            alert('비밀번호를 입력해주세요!');
        }else if($changePwR.value == ''){
            alert('비밀번호를 재입력해주세요!');
        }
    }
);
