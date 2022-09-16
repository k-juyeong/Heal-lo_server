import makeElements from '../module/create-elememt.js'

//dom접근
const $changeScore = document.querySelector('.change-score');
const $inputImg = document.querySelector('.input-img');
const $previewWrap = document.querySelector('.preview-wrap');
const $updateBtn = document.querySelector('.btn-update');
const $btnCancel = document.querySelector('.btn-cancel');
const $textarea = document.querySelector('.textarea');
const $errorClass = document.querySelector('.error-class');

let ratingScore = $review.rvscore;
const deleteImages = [];
const uploadImgs = [];

//수정 전 파일 프리뷰 생성
(function previewDivRender() {
    $review.imageFiles.forEach(file => {
        const previewDiv =
            makeElements('div', {class: 'preview-img', [`data-img-name`]: file.uffname},
                makeElements('img', {src: `/images/${file.ufsname}`}),
                makeElements('button', {class: 'fa-solid fa-xmark'}));
        $previewWrap.appendChild(previewDiv);

        previewDiv.querySelector('button').addEventListener('click', e => {
            deleteImages.push(file.ufno);
            previewDiv.remove();
        });
    })
})();

//별점 변경 이벤트
$changeScore.addEventListener('input',({target}) => {
    if(target.value < 0.5) {
        target.value = 0.5;
        return;
    }

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
    if(deleteImages.length+uploadImgs.length+target.files.length > 5) {
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

//수정 버튼 이벤트
$updateBtn.addEventListener('click', () => {
    const formData = new FormData();
    formData.append('rvscore', ratingScore);
    formData.append('rvcontents', $textarea.value);
    formData.append('deleteImages',deleteImages)
    uploadImgs?.forEach(ele => {
        formData.append('multipartFiles', ele);
    })

    fetch(`/reviews/${$review.rvno}`, {
        method: 'PATCH',
        body: formData
    })
        .then((response) => response.json())
        .then((jsonData) => {
            if (jsonData.statusCode == '001') location.href = jsonData.data.redirect;
            else {
                if(jsonData.statusCode == '002') {
                    jsonData.errors.forEach((error) => {
                        if (error.field == 'rvcontents') {
                            $errorClass.textContent = error.message;
                            $textarea.style.border = '1px solid red';
                            $textarea.addEventListener('click', () => {
                                $errorClass.textContent = '';
                                $textarea.style.border = '1px solid #D6D6D6';
                            });
                        }
                    });
                }
            }
        })
        .catch((error) => {
            console.log(error)
        });
});

//취소버튼 이벤트
$btnCancel.addEventListener('click',() => {
    const fcno = document.querySelector('.facility-card').dataset.fcno;
    location.href = `/facilities/${fcno}`;
})

