import makeElements from '../module/create-elememt.js'

//전역변수
let ratingScore = 5;
let uploadImgs = [];

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
            document.querySelector('.preview-wrap').appendChild(previewDiv);

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
document.querySelector('.btn-reg').addEventListener('click', () => {
    const fcno = document.querySelector('.facility-card').dataset.fcno;
    const $textarea = document.querySelector('.textarea');
    const $errorClass = document.querySelector('.error-class');

    const formData = new FormData();
    formData.append('rvscore', ratingScore);
    formData.append('rvcontents', $textarea.value);
    uploadImgs?.forEach(ele => {
        formData.append('multipartFiles', ele);
    })

    fetch(`/reviews/${fcno}`, {
        method: 'POST',
        body: formData
    })
        .then((response) => response.json())
        .then((jsonData) => {
            if (jsonData.header.code == '00') location.href = jsonData.data.redirect;
            else if(jsonData.header.code == '01'){
                jsonData.data.errors.forEach(error => {
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
                throw new Error(jsonData.message)
            }
        })
        .catch((error) => console.log(error));
});

//취소버튼 이벤트
document.querySelector('.btn-cancel').addEventListener('click',() => {
    const fcno = document.querySelector('.facility-card').dataset.fcno;
    location.href = `/facilities/${fcno}`;
})
