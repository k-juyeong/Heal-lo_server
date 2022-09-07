// 현재 달력 가져오기
let date = new Date();



const createCalendar = () => {
    const selectedYear = date.getFullYear();
    const selectedMonth = date.getMonth();
    const thisMonthLast = new Date(selectedYear, selectedMonth+1, 0);


    // 현재 출력되는 연도,월 표시하기
    const $showSelectedMonth = document.querySelector('.calendar__selected-month');
    $showSelectedMonth.textContent = `${selectedYear}년 ${selectedMonth+1}월`;


    // 달력 날짜 채우기
    const insertDates = document.querySelector('.calendar__dates');
    const dates = [];
    for(let i=1; i<=thisMonthLast.getDate(); i++) {
        dates.push(i);
    }


    const prevMonthLast = new Date(selectedYear, selectedMonth,0);
    const nextMonthFirst = new Date(selectedYear, selectedMonth+1, 1);
    
    // 지난달 마지막 주 날짜
    const prevDates = [];
    if(prevMonthLast.getDay() !== 6) {
        for(let i=0; i<=prevMonthLast.getDay(); i++) {
            prevDates[i] = `<div class="calendar__prevDate"></div>`;
        }
    }
        
    // 이번달 날짜
    dates.forEach((ele, idx)=> {
        dates[idx] = `<div class="calendar__date">${ele}</div>`;
    });
    
    // 다음달 첫째주 날짜
    const nextDates = [];
    if(nextMonthFirst.getDay() !== 0) {
        for(let i=0; i < 7-nextMonthFirst.getDay(); i++) {
            nextDates[i] = `<div class="calendar__nextDate"></div>`;
        }
    }

    const allDates = prevDates.concat(dates).concat(nextDates);
    
    insertDates.innerHTML = allDates.join('');

    // 오늘 날짜 표시하기
    const today = new Date();
    if(selectedYear === today.getFullYear() && selectedMonth === today.getMonth()) {
        for(let date of document.querySelectorAll('.calendar__date')) {
            if(Number(date.innerText) == today.getDate()) {
                date.classList.add('today');
                break;
            }
        }
    }
}


createCalendar();


// 이전달 달력 가져오기
const $goPrev = document.querySelector('.calendar__go-prev');
$goPrev.addEventListener('click', goPrev_h = () => {
    date.setMonth(date.getMonth()-1);
    createCalendar();
});

// 다음달 달력 가져오기
const $goNext = document.querySelector('.calendar__go-next');
$goNext.addEventListener('click', goNext_h = () => {
    date.setMonth(date.getMonth()+1);
    createCalendar();
});

// 이번달 달력으로 돌아오기
const $today = document.querySelector('.calendar__go-today');
$today.addEventListener('click', goToday_h = () => {
    date = new Date();
    createCalendar();
});
