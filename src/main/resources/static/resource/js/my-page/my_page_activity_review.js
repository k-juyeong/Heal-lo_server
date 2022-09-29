//리뷰 삭제
function mpDeleteReview(e) {
    const rvno = e.target.closest('.button-box').id;
    fetch(`/reviews/${rvno}`, {
        method: `DELETE`
    })
        .then(response => response.json())
        .then(jsonData => {
        })
        .catch(error => console.log(error));
}