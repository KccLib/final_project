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

const searchContentsPeople = document.getElementById("search-people-contents");
const searchContentsChatRooms = document.getElementById("search-group-chat-rooms");

searchBar.addEventListener("click", function () {
    if(searchBarCount % 2 === 0) {
        searchContainer.classList.remove("hidden");
        //열렸을 때 load 시키기
        $.ajax({
            method: "GET",
            url: "/api/search",
            dataType: "json",
            success: function (responseSearch) {
                console.log("검색된 개체 개수 :" + responseSearch.searchEmployeeList + responseSearch.searchChatRoomList);


                // searchContentsPeople 요소를 초기화
                searchContentsPeople.innerHTML = "";

                // searchEmployeeList에서 각 직원 정보를 가져와서 HTML 생성
                responseSearch.searchEmployeeList.forEach(function (employee) {
                    searchContentsPeople.innerHTML +=
                        "<div class=\"search-peoples\" style=\"cursor: pointer;\">" +
                        "    <div class=\"search-profile-img\">" +
                        "        <img src=\"" + employee.profileURL + "\" alt=\"프로필 이미지\" />" +
                        "    </div>" +
                        "    <p class=\"search-profile-name\">" + employee.name + "</p>" +
                        "    <p class=\"search-profile-dept\">" + employee.deptName + "</p>" +
                        "    <p class=\"search-profile-position\">" + employee.position + "</p>" +
                        "</div>";
                });

                searchContentsChatRooms.innerHTML = "";

                responseSearch.searchChatRoomList.forEach(function (chatroom) {
                    // 새로운 HTML 요소 생성
                    searchContentsChatRooms.innerHTML +=
                        "<div class='search-group-contents'>" +
                        "<div class='search-chat-profile-img'>" +
                        "<img src='" + chatroom.imageURL + "' alt='그룹 채팅방 이미지' />" +
                        "</div>" +
                        "<p class='search-chatroom-name'>" + chatroom.name + "</p>" +
                        "</div>";
                });

            },
            error: function (result) {}
        });



    } else {
        searchContainer.classList.add("hidden");
        searchBar.value = "";

    }
    console.log(searchBarCount);
        searchBarCount++;
});

window.addEventListener("click", function (event) {
    if (event.target === searchContainer) {
        searchContainer.classList.add("hidden");
    }
    searchBar.value = "";

});

searchBar.addEventListener("input", function (event) {
    let searchBarValue = event.target.value;
    console.log("변경된 값이 있습니다");

    $.ajax({
        method: "GET",
        url: "/api/search/change",
        data: {
            keyword : searchBarValue },
        dataType: "json",
        success: function (responseSearch) {
            console.log("검색된 개체 개수 :" + responseSearch.searchEmployeeList + responseSearch.searchChatRoomList);


            // searchContentsPeople 요소를 초기화
            searchContentsPeople.innerHTML = "";

            // searchEmployeeList에서 각 직원 정보를 가져와서 HTML 생성
            responseSearch.searchEmployeeList.forEach(function (employee) {
                searchContentsPeople.innerHTML +=
                    "<div class=\"search-peoples\" style=\"cursor: pointer;\">" +
                    "    <div class=\"search-profile-img\">" +
                    "        <img src=\"" + employee.profileURL + "\" alt=\"프로필 이미지\" />" +
                    "    </div>" +
                    "    <p class=\"search-profile-name\">" + employee.name + "</p>" +
                    "    <p class=\"search-profile-dept\">" + employee.deptName + "</p>" +
                    "    <p class=\"search-profile-position\">" + employee.position + "</p>" +
                    "</div>";
            });

            searchContentsChatRooms.innerHTML = "";

            responseSearch.searchChatRoomList.forEach(function (chatroom) {
                // 새로운 HTML 요소 생성
                searchContentsChatRooms.innerHTML +=
                    "<div class='search-group-contents'>" +
                    "<div class='search-chat-profile-img'>" +
                    "<img src='" + chatroom.imageURL + "' alt='그룹 채팅방 이미지' />" +
                    "</div>" +
                    "<p class='search-chatroom-name'>" + chatroom.name + "</p>" +
                    "</div>";
            });

        },
        error: function (result) {}
    });
})  ;