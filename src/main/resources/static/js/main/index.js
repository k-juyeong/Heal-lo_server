const bgBoxTriggerMargin = 300;
const bgBoxElementList = document.querySelectorAll('.bg');

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