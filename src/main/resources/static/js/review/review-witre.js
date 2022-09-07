import makeElements from '../module/create-elememt.js'

//dom접근
const $changeScore = document.querySelector('.change-score');
const $inputImg = document.querySelector('.input-img');
const $previewWrap = document.querySelector('.preview-wrap');
const $textarea = document.querySelector('.textarea');
const $RegBtn = document.querySelector('.btn-reg');

let ratingScore = 0;
let uploadImgs = [];

//별점 변경 이벤트
$changeScore.addEventListener('input',({target}) => {
    const value = target.value
    document.querySelector('.inner-star').style.width = `${target.value * 10}%`;
    ratingScore = parseInt(value)/2;

    document.querySelector('.text-score').textContent = ratingScore;
})

//이미지 업로드 이벤트
$inputImg.addEventListener('change',({target}) => {
    const files = target.files;
    if(!target.files && !target.files[0]) return;
    const notSupportFile = [...files].find(ele => ele.type != 'image/png' && ele.type != 'image/jpeg');
    if(notSupportFile) {
        alert('지원하지 않는 파일')
        return;
    }
    if(uploadImgs.length+target.files.length > 5) {
        alert('최대 업로드 수 초과');
        return;
    }



    [...files].forEach(ele => {
        const reader = new FileReader();

        reader.onload = (e) => {
            uploadImgs.push(ele);
            const previewDiv =
            makeElements('div',{class : 'preview-img', [`data-img-name`] : ele.name},
                makeElements('img',{src : `${e.target.result}`}),
                makeElements('button',{class : 'fa-solid fa-xmark'}));
                $previewWrap.appendChild(previewDiv);

            previewDiv.querySelector('button').addEventListener('click',(e)=> {
                const idx = uploadImgs.findIndex(ele => ele.name == previewDiv.dataset.imgName);
                uploadImgs.splice(idx,1);
                previewDiv.remove();
            })
        }

        reader.readAsDataURL(ele);
    });
})

//등록 버튼 이벤트
$RegBtn.addEventListener('click', () => {
    const submitData = {
        ratingScore : ratingScore,
        imgs : uploadImgs,
        contents : $textarea.value
    }

    // requestPublicApi(submitData);
})

// 서버에 외부api 통신요청(GET,Accept = json)
function requestPublicApi(data) {
    const xhr = new XMLHttpRequest();
    const url = 'http://localhost:9080/public/review'; //매핑url은 수정 가능성 있음
    
    xhr.open('POST',url);
    xhr.send(JSON.stringify(data));
} 

