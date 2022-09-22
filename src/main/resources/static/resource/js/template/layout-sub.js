const menu = ['검색', '게시판', '캘린더']
const menus = document.querySelectorAll('.header__sitemenu .header__site-icon');
let sitemenuInfo = document.getElementById('sitemenuInfo');
const sitemenuContain = document.getElementById("siteMenuContain");


menuCategoryInit();

menus.forEach((item) => {
  item.addEventListener('mouseover',(e) => iconMouseoverEvent(e))
})

sitemenuContain.addEventListener('mouseout',(e) => iconMouseoutEvent(e))


function menuCategoryInit() {
  for(let i = 0; i<menus.length; i++) {
    menus[i].category = menu[i];
  } 
}

function iconMouseoverEvent(e) {
  sitemenuInfo.innerHTML = e.target.category;
  sitemenuInfo.style.left = e.target.offsetLeft + "px";
  sitemenuInfo.style.top = e.target.offsetTop + -e.target.offsetHeight + 12 + "px";
  sitemenuInfo.style.opacity = 1;
  sitemenuInfo.style.color = "#f39c12";
}

function iconMouseoutEvent() {
  sitemenuInfo.style.opacity = 0;
}

