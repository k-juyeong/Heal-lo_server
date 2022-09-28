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
                document.querySelectorAll('.bookmark').remove();
            } else if (jsonData.header.code == '03') {


            } else {
                throw new Error(jsonData.data);

            }
        })
        .catch(err => console.log(err));
}