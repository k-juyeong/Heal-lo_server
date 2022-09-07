const $bookmarkBtn = document.querySelectorAll('.bookmarkBtn');
for(let i = 0; i < $bookmarkBtn.length; i++){
  $bookmarkBtn[i].addEventListener('click',e=>{
    $bookmarkBtn[i].classList.toggle('bookmark-btn-color');
  });
}