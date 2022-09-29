const $bookmarkBtn = document.querySelectorAll('.bookmarkBtn');

for(let i = 0; i < $bookmarkBtn.length; i++){
  $bookmarkBtn[i].addEventListener('click',e=>{
    $bookmarkBtn[i].classList.toggle('bookmark-btn-color');
  });
}

function domClean(e) {
    const id = e.target.closest('.bookmark').id;
    fetch(`/bookmarks/${id}`, {
        method: 'PATCH',        //http method
        headers: {             //http header
            'Accept': 'application/json'
        }
    })
        .then(response => response.json())
        .then(jsonData => {
            if (jsonData.header.code == '00') {
                console.log(jsonData);
                e.target.closest('.bookmark').remove();
            } else if (jsonData.header.code == '03') {
            } else {
                throw new Error(jsonData.data);
            }
        })
        .catch(err => console.log(err));
}

const $new = document.querySelector('.new');
$new.addEventListener('click',e=>{
    console.log('최신 등록순 클릭');
    location.href=`/bookmarks?orderBy=${e.target.dataset.order}`
});

const $grade = document.querySelector('.grade');
$grade.addEventListener('click',e=>{
    console.log('별점 높은순 클릭');
    location.href=`/bookmarks?orderBy=${e.target.dataset.order}`
});