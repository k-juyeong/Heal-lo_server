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