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
                // console.log("검색된 개체 개수 :" + responseSearch.searchEmployeeList + responseSearch.searchChatRoomList);


                // searchContentsPeople 요소를 초기화
                searchContentsPeople.innerHTML = "";

                // searchEmployeeList에서 각 직원 정보를 가져와서 HTML 생성
                responseSearch.searchEmployeeList.forEach(function (employee) {
                    searchContentsPeople.innerHTML +=
                        "<div class=\"search-peoples\" style=\"cursor: pointer;\" data-id=\"" + employee.id + "\">" +
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
            error: function(xhr, status, error) {
                console.log("요청 실패:");
                console.log("XHR:", xhr);
                console.log("상태:", status);
                console.log("오류:", error);
            }
        });



    } else {
        searchContainer.classList.add("hidden");
        searchBar.value = "";

    }
    // console.log(searchBarCount);
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
    searchContainer.classList.remove("hidden");

    console.log("변경된 값이 있습니다");

    $.ajax({
        method: "GET",
        url: "/api/search/change",
        data: {
            keyword : searchBarValue },
        dataType: "json",
        success: function (responseSearch) {
            // console.log("검색된 개체 개수 :" + responseSearch.searchEmployeeList + responseSearch.searchChatRoomList);


            // searchContentsPeople 요소를 초기화
            searchContentsPeople.innerHTML = "";

            // searchEmployeeList에서 각 직원 정보를 가져와서 HTML 생성
            responseSearch.searchEmployeeList.forEach(function (employee) {
                // console.log("넘어온 employee id : " + employee.id);
                searchContentsPeople.innerHTML +=
                    "<div class=\"search-peoples\" style=\"cursor: pointer;\" data-id=\"" + employee.id + "\">" +
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
        error: function (xhr, status, error) {
            console.error("그룹채팅방을 가져올 수 없습니다. " + error);
        }
    });
})  ;

/**
 * 다른사용자 조회 js
 *
 */
const otherEmployeeContainer = document.getElementById("other-employee-info-container");
const otherEmployeeClose = document.getElementById("other-employee-schedule-close");
const searchPeopleElements = document.getElementById("search-people-contents");


/**
 * 상위 요소에 이벤트 리스너 추가 - employee profile은 동적으로 생성
 */
searchPeopleElements.addEventListener("click", function(event) {

    //각 요소 비우기
    const otherEmployeeNamePosition = document.getElementById("other-employee-name-position");
    const otherDeptContainer = document.getElementById("other-dept-container");
    const otherEmailContainer = document.getElementById("other-email-container");
    const otherLocateContainer = document.getElementById("other-locate-container");
    const otherContentsContainer = document.getElementById("other-contents-container");
    const otherEmployeeImg = document.getElementById("other-employee-img");

    otherEmployeeNamePosition.innerHTML = "";
    otherDeptContainer.innerHTML = "";
    otherEmailContainer.innerHTML = "";
    otherLocateContainer.innerHTML = "";
    otherContentsContainer.innerHTML = "";

    const target = event.target.closest(".search-peoples");
    if (target) {
        const id = target.dataset.id;
        console.log("user profile 선택" + id);
        otherEmployeeContainer.classList.remove("hidden");
        searchContainer.classList.add("hidden");
        searchBar.value = "";

        $.ajax({
            method: "GET",
            url: `/api/other/${id}`,
            dataType: "json",
            success: function (employeeInfo) {
                console.log("회원 정보를 가져왔습니다." + employeeInfo.name);

                otherEmployeeNamePosition.innerHTML = `
                                                        <p id="other-name">${employeeInfo.name}</p>
                                                        <p id="other-position">${employeeInfo.position}</p>
                                                    `;

                otherDeptContainer.innerHTML = `<p id="other-dept">${employeeInfo.deptName}</p>`;
                otherEmployeeImg.innerHTML =  `<img src="${employeeInfo.profileUrl}" id="other-profile-img" alt="프로필 이미지" />` ;

                otherEmailContainer.innerHTML = `<p id="other-email">${employeeInfo.email}</p>`;

                otherLocateContainer.innerHTML = `<p id="other-locate">${employeeInfo.location}</p>`;

                otherContentsContainer.innerHTML = `<p id="other-contents>${employeeInfo.contents}</p>`;
            },
            error: function (xhr, status, error) {
                console.error("회원 조회에 실패했습니다. " + error);
            }
        })


    }
});


// //엔터키 눌렀을 때 검색된 사람 중 제일 앞에 사람
// document.addEventListener("keydown", function(event) {
//     // 엔터 키가 눌렸는지 확인
//     if (event.key === "Enter") {
//         // 선택할 요소
//         const selectedElement = document.getElementById("search-people-contents");
//
//         if (selectedElement.target.closest(".search-peoples")) {
//             console.log("user profile 선택");
//             otherEmployeeContainer.classList.remove("hidden");
//         }
//     }
// });

otherEmployeeClose.addEventListener("click", function () {
   otherEmployeeContainer.classList.add("hidden");
});