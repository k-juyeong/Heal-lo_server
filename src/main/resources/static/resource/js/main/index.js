const bgBoxTriggerMargin = 300;
const bgBoxElementList = document.querySelectorAll('.bg');
const textInput = document.querySelector('.search-bar__content');
const form = document.querySelector('.section1__search-bar__group');

const bgBoxFunc = () => {
    for(const ele of bgBoxElementList) {
        if(!ele.classList.contains('show')) {
            if(
                window.innerHeight > 
                ele.getBoundingClientRect().top + bgBoxTriggerMargin
            ) {
                ele.classList.add('show');
            }
        }
    }
};

window.addEventListener('load', bgBoxFunc);
window.addEventListener('scroll', bgBoxFunc);

//운동시설 검색
form.addEventListener('submit',(e) => {
    e.preventDefault();

    const keyword = textInput.value.trim('');
    if (keyword == '') {
        location.href = `/facilities`;
    } else {
        location.href = `/facilities?keyword=${keyword}`;
    }
});
