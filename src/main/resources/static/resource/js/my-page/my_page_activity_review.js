function askMpDeleteReview(e){
    if(confirm("리뷰를 삭제하시겠습니까?")){
        mpDeleteReview(e);
    }else{
        return;
    }
}


//리뷰 삭제
function mpDeleteReview(e) {
console.log( e.target.closest('.button-box').id)
    const rvno = e.target.closest('.button-box').id;
    fetch(`/reviews/${rvno}`, {
        method: `DELETE`
    })
        .then(response => response.json())
        .then(jsonData => {
            e.target.closest('.substance').remove();
        })
        .catch(error => console.log(error));
}