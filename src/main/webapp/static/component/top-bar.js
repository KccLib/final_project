const modalOpenButton = document.getElementById("employee-icon");
const modalCloseButton = document.getElementById("modalCloseButton");
const modal = document.getElementById("modalContainer");
const statusOpenButton = document.getElementById("user-status-box");
const statusClose = document.getElementById("status-contents");
const statusModel = document.getElementById("status-container");
var isStatusOpen = true;

modalOpenButton.addEventListener("click", () => {
  console.log("remove");
  modal.classList.remove("hidden");
});

modalCloseButton.addEventListener("click", () => {
  modal.classList.add("hidden");
});

// 닫기 버튼 말고 다른 창을 클릭해도 닫히게 하기.
window.addEventListener("click", function (event) {
  if (event.target === modal) {
    modal.classList.add("hidden");
    statusModel.classList.add("hidden");
  }
});

// 사용자 상태 변경 js
statusOpenButton.addEventListener("click", function () {
  if (isStatusOpen) {
    statusModel.classList.add("hidden");
  } else {
    statusModel.classList.remove("hidden");
  }
  isStatusOpen = !isStatusOpen;
});

document
  .getElementById("logoutLink")
  .addEventListener("click", function (event) {
    event.preventDefault(); // 기본 링크 동작 방지

    // POST 요청을 위한 form 생성
    const logoutForm = document.createElement("form");
    logoutForm.method = "POST";
    logoutForm.action = "/logout"; // 로그아웃 URL

    document.body.appendChild(logoutForm); // form을 body에 추가
    logoutForm.submit(); // 폼 제출
  });

document
    .getElementById("logoutLink")
    .addEventListener("click", function (event) {
        localStorage.removeItem('Fcmtoken');

    });


const searchBar = document.getElementById("search-bar");
const searchContainer = document.getElementById("search-bar-container");
let searchBarCount = 0;

searchBar.addEventListener("click", function () {
    if(searchBarCount % 2 === 0) {
        searchContainer.classList.remove("hidden");
    } else {
        searchContainer.classList.add("hidden");
    }
    console.log(searchBarCount);
        searchBarCount++;
});

window.addEventListener("click", function (event) {
    if (event.target === searchContainer) {
        searchContainer.classList.add("hidden");
    }
});