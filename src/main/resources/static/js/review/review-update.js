import makeElements from '../module/create-elememt.js'

//전역변수
let ratingScore = $review.rvscore;
const deleteImages = [];
const uploadImgs = [];

//수정 전 파일 프리뷰 생성
(function previewDivRender() {
    const $previewWrap = document.querySelector('.preview-wrap');

    $review.imageFiles.forEach(file => {
        const previewDiv =
            makeElements('div', {class: 'preview-img', [`data-img-name`]: file.uffname},
                makeElements('img', {src: `/images/${file.code}/${file.ufsname}`}),
                makeElements('button', {class: 'fa-solid fa-xmark'}));
        $previewWrap.appendChild(previewDiv);

        previewDiv.querySelector('button').addEventListener('click', e => {
            deleteImages.push(file.ufno);
            previewDiv.remove();
        });
    })
})();

//별점 변경 이벤트
document.querySelector('.change-score').addEventListener('input',({target}) => {
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
document.querySelector('.input-img').addEventListener('change',({target}) => {
    const $previewWrap = document.querySelector('.preview-wrap');

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
document.querySelector('.btn-update').addEventListener('click', () => {
    const $textarea = document.querySelector('.textarea');
    const $errorClass = document.querySelector('.error-class');

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
            if (jsonData.header.code == '00') location.href = jsonData.data.redirect;
            else if (jsonData.header.code == '01') {
                jsonData.data.errors.forEach((error) => {
                    if (error.field == 'rvcontents') {
                        $errorClass.textContent = error.message;
                        $textarea.style.border = '1px solid red';
                        $textarea.addEventListener('click', () => {
                            $errorClass.textContent = '';
                            $textarea.style.border = '1px solid #D6D6D6';
                        });
                    }
                });
            } else {
                throw new Error(jsonData.message);
            }
        })
        .catch((error) => console.log(error)
        );
});

//취소버튼 이벤트
document.querySelector('.btn-cancel').addEventListener('click',() => {
    const fcno = document.querySelector('.facility-card').dataset.fcno;
    location.href = `/facilities/${fcno}`;
})

