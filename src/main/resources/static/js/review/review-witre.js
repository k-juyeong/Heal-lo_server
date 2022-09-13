import makeElements from '../module/create-elememt.js'

//dom접근
const $changeScore = document.querySelector('.change-score');
const $inputImg = document.querySelector('.input-img');
const $previewWrap = document.querySelector('.preview-wrap');
const $RegBtn = document.querySelector('.btn-reg');

let ratingScore = 5;
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
    const contents = document.querySelector('.textarea').value;
    const fcno = document.querySelector('.facility-card').dataset.fcno;
    const formData = new FormData();
    formData.append('rvscore', ratingScore);
    formData.append('rvcontents', contents);
    uploadImgs?.forEach(ele => {
        formData.append('multipartFiles', ele);
    })

    const xhr = new XMLHttpRequest();
    const url = `/reviews/${fcno}/add`;
    xhr.open('POST', url);
    xhr.send(formData);

    xhr.addEventListener('readystatechange', () => {
        if (xhr.readyState == XMLHttpRequest.DONE) {
            if (xhr.status == 0 || (xhr.status >= 200 && xhr.status < 400)) {
                location.href = xhr.getResponseHeader("location");
            } else {
                console.log("에러");
            }
        }
    });
});


