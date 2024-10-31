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
    localStorage.removeItem("Fcmtoken");
  });

const searchBar = document.getElementById("search-bar");
const searchContainer = document.getElementById("search-bar-container");
let searchBarCount = 0;

const searchContentsPeople = document.getElementById("search-people-contents");
const searchContentsChatRooms = document.getElementById(
  "search-group-chat-rooms"
);

searchBar.addEventListener("click", function () {
  if (searchBarCount % 2 === 0) {
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
            '<div class="search-peoples" style="cursor: pointer;" data-id="' +
            employee.id +
            '">' +
            '    <div class="search-profile-img">' +
            '        <img src="' +
            employee.profileURL +
            '" alt="프로필 이미지" />' +
            "    </div>" +
            '    <p class="search-profile-name">' +
            employee.name +
            "</p>" +
            '    <p class="search-profile-dept">' +
            employee.deptName +
            "</p>" +
            '    <p class="search-profile-position">' +
            employee.position +
            "</p>" +
            "</div>";
        });

        searchContentsChatRooms.innerHTML = "";

        responseSearch.searchChatRoomList.forEach(function (chatroom) {
          // 새로운 HTML 요소 생성
          searchContentsChatRooms.innerHTML +=
            `<div class='search-group-contents' data-chatroomid="${chatroom.myChatRoomId}">` +
            "<div class='search-chat-profile-img'>" +
            "<img src='" +
            chatroom.imageURL +
            "' alt='그룹 채팅방 이미지' />" +
            "</div>" +
            "<p class='search-chatroom-name'>" +
            chatroom.name +
            "</p>" +
            "</div>";
        });
      },
      error: function (xhr, status, error) {
        console.log("요청 실패:");
        console.log("XHR:", xhr);
        console.log("상태:", status);
        console.log("오류:", error);
      },
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
      keyword: searchBarValue,
    },
    dataType: "json",
    success: function (responseSearch) {
      // console.log("검색된 개체 개수 :" + responseSearch.searchEmployeeList + responseSearch.searchChatRoomList);

      // searchContentsPeople 요소를 초기화
      searchContentsPeople.innerHTML = "";

      // searchEmployeeList에서 각 직원 정보를 가져와서 HTML 생성
      responseSearch.searchEmployeeList.forEach(function (employee) {
        // console.log("넘어온 employee id : " + employee.id);
        searchContentsPeople.innerHTML +=
          '<div class="search-peoples" style="cursor: pointer;" data-id="' +
          employee.id +
          '">' +
          '    <div class="search-profile-img">' +
          '        <img src="' +
          employee.profileURL +
          '" alt="프로필 이미지" />' +
          "    </div>" +
          '    <p class="search-profile-name">' +
          employee.name +
          "</p>" +
          '    <p class="search-profile-dept">' +
          employee.deptName +
          "</p>" +
          '    <p class="search-profile-position">' +
          employee.position +
          "</p>" +
          "</div>";
      });

      searchContentsChatRooms.innerHTML = "";

      responseSearch.searchChatRoomList.forEach(function (chatroom) {
        // 새로운 HTML 요소 생성
        searchContentsChatRooms.innerHTML +=
            `<div class='search-group-contents' data-chatroomid="${chatroom.myChatRoomId}">` +
          "<div class='search-chat-profile-img'>" +
          "<img src='" +
          chatroom.imageURL +
          "' alt='그룹 채팅방 이미지' />" +
          "</div>" +
          "<p class='search-chatroom-name'>" +
          chatroom.name +
          "</p>" +
          "</div>";
      });
    },
    error: function (xhr, status, error) {
      console.error("그룹채팅방을 가져올 수 없습니다. " + error);
    },
  });
});

/**
 * 다른사용자 조회 js
 *
 */
const otherEmployeeContainer = document.getElementById(
  "other-employee-info-container"
);
const otherEmployeeClose = document.getElementById(
  "other-employee-schedule-close"
);
const searchPeopleElements = document.getElementById("search-people-contents");
let otherEmployeeId = 0;

/**
 * 상위 요소에 이벤트 리스너 추가 - employee profile은 동적으로 생성
 */
searchPeopleElements.addEventListener("click", function (event) {
  //각 요소 비우기
  const otherEmployeeNamePosition = document.getElementById(
    "other-employee-name-position"
  );
  const otherDeptContainer = document.getElementById("other-dept-container");
  const otherEmailContainer = document.getElementById("other-email-container");
  const otherLocateContainer = document.getElementById(
    "other-locate-container"
  );
  const otherContentsContainer = document.getElementById(
    "other-contents-container"
  );
  const otherEmployeeImg = document.getElementById("other-employee-img");
  const employeeName = document.getElementById(
    "employee-name-location-top-bar"
  );

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
    otherEmployeeId = id;

    $.ajax({
      method: "GET",
      url: `/api/other/${id}`,
      dataType: "json",
      success: function (employeeInfo) {
        console.log("회원 정보를 가져왔습니다." + employeeInfo.name);

        otherEmployeeNamePosition.innerHTML = `
                                              <p id="other-name">${employeeInfo.name}</p>&nbsp;
                                              <p id="other-position">${employeeInfo.position}</p>
                                              `;

        otherDeptContainer.innerHTML = `<p id="other-dept">${employeeInfo.deptName}</p>`;
        otherEmployeeImg.innerHTML = `<img src="${employeeInfo.profileUrl}" id="other-profile-img" alt="프로필 이미지" />`;

        otherEmailContainer.innerHTML = `<p id="other-email">${employeeInfo.email}</p>`;

        if (employeeInfo.location) {
          otherLocateContainer.innerHTML = `<p id="other-locate">${employeeInfo.location}</p>`;
        }

        otherContentsContainer.innerHTML = `<p id="other-contents>${employeeInfo.contents}</p>`;
        employeeName.innerHTML = `<p>${employeeInfo.name} 님의 일정</p>`;
      },
      error: function (xhr, status, error) {
        console.error("회원 조회에 실패했습니다. " + error);
      },
    });
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

/**
 * 그룹채팅방 눌렀을 때 이동시키기
 *
 */

const searchChatRoomElements = document.getElementById("search-group-chat-rooms");

searchChatRoomElements.addEventListener("click", function (event) {

  const target = event.target.closest(".search-group-contents");
  if (target) {
    const roomId = target.dataset.chatroomid;

    const form = document.createElement("form");
    form.method = "GET";
    form.action = "/group";

    const input = document.createElement("input");
    input.type = "hidden";
    input.name = "roomId";
    input.value = roomId;
    form.appendChild(input);

    document.body.appendChild(form);
    form.submit();  }

});




/**
 * 달력 랜더링
 */
var employeeEventsForOthers = [];
var firstDayForOthers;
var lastDayForOthers;

function getFirstAndLastDateOfMonthForOther() {
  // 날짜를 'YYYY-MM-DD' 형식으로 포맷팅
  var formattedFirstDay = firstDayForOthers.toISOString().split("T")[0];
  var formattedLastDay = lastDayForOthers.toISOString().split("T")[0];

  //json 형식으로 반환
  return {
    firstDayForOthers: formattedFirstDay,
    lastDayForOthers: formattedLastDay,
  };
}

function formatDateToISOForOther(dateString) {
  // 'YYYY-MM-DD HH:mm:ss' 형식의 문자열을 Date 객체로 변환
  const [datePart, timePart] = dateString.split(" ");
  const [year, month, day] = datePart.split("-");
  const [hour, minute, second] = timePart.split(":");

  // Date 객체를 한국 시간(KST) 기준으로 생성
  const localDate = new Date(year, month - 1, day, hour, minute, second);

  const isoDate = new Date(
      localDate.getTime() - localDate.getTimezoneOffset() * 60000
  ).toISOString();

  return isoDate.replace(".000Z", ""); // 초까지 포함된 형식을 유지
}

function  fetchCalendarDataForOther(){
  var dateData = getFirstAndLastDateOfMonthForOther();
  // console.log(dateData);
  //일정조회 ajax
  $.ajax({
    url: "/api/schedules/other",
    method: "GET",
    data: {
      employeeId: otherEmployeeId,
      startDate: dateData.firstDayForOthers,
      endDate: dateData.lastDayForOthers,
    },
    dataType: "json",
    success: function (schedules) {
      console.log("other employee schedule 통신 성공");

      // 배열 및 캘린더 초기화
      employeeEventsForOthers = [];
      calendarTopBar.removeAllEvents();

      schedules.forEach(function (schedule) {
        // 시간부분
        var startTime = schedule.startedDt.split(" ")[1];
        var endTime = schedule.endedDt.split(" ")[1];
        var event = {
          title: schedule.name,
          // ISO 8601 형식으로 변환
          start: formatDateToISOForOther(schedule.startedDt),
          end: formatDateToISOForOther(schedule.endedDt),
          extendedProps: {
            // 추가 정보를 담기 - 스케줄의 각 id의 고유한 번호, 내스케줄여부
            isMySchedule: schedule.isMySchedule,
            scheduleId: schedule.scheduleId,
          },
        };
        console.log("가져온 others scheduleId : " + schedule.scheduleId);

        if (startTime === "00:00:00" && endTime === "00:00:00") {
          event.allDay = true; // allDay 이벤트로 설정
        }
        employeeEventsForOthers.push(event);
        calendarTopBar.addEvent(event); // 이벤트를 FullCalendar에 추가
      });
      console.log("events:", employeeEventsForOthers);

      calendarTopBar.render(); // 캘린더 업데이트
    },
    error: function (xhr, status, error) {
      console.error("일정 조회에 실패하였습니다." + error);
    },
  });
}

// 캘린더 랜더링
searchPeopleElements.addEventListener("click", function (event) {

    const calendarEl = document.getElementById("calendar-top-bar");
    calendarTopBar = new FullCalendar.Calendar(calendarEl, {
      initialView: "dayGridMonth",
      headerToolbar: {
        left: "prev next today",
        center: "title",
        right: "dayGridMonth,dayGridWeek,dayGridDay",
      },
      buttonText: {
        today: "오늘",
        month: "월",
        week: "주",
        day: "일",
        list: "목록",
      },

      // 일 빼기
      dayCellContent: function (info) {
        var number = document.createElement("a");
        number.classList.add("fc-daygrid-day-number");
        number.innerHTML = info.dayNumberText.replace("일", "");
        if (info.view.type === "dayGridMonth") {
          return {
            html: number.outerHTML,
          };
        }
        return {
          domNodes: [],
        };
      },
      selectable: true,
      dayMaxEventRows: true,
      dayMaxEventRows: 3, // 하루에 표시할 최대 이벤트 수
      events: employeeEventsForOthers,

      eventDidMount: function (info) {
        info.el.style.cursor = "pointer";
        // isMySchedule에 따른 색상 변경 처리
        if (info.event.extendedProps.isMySchedule === 1) {
          if (!info.event.allDay) {
            info.el.style.color = "black";
            info.el.style.backgroundColor = "white";
            info.el.classList.add("time-event-dot"); // 클래스 추가
          } else {
            info.el.style.backgroundColor = "#FF7364";
            info.el.style.paddingLeft = "5px";
            info.el.style.border = "0px"; // 테두리 제거
          }
        } else {
          if (!info.event.allDay) {
            info.el.style.color = "black";
            info.el.style.backgroundColor = "white";
          } else {
            info.el.style.backgroundColor = "#6769FF";
            info.el.style.paddingLeft = "5px";
            info.el.style.border = "0px"; // 테두리 제거
          }
        }

        // data-schedule-id 속성 추가
        info.el.setAttribute(
          "data-schedule-id",
          info.event.extendedProps.scheduleId
        );
      },

      // 상세보기는 other에서 제거
      eventClick: function (info) {},

      displayEventTime: true,
      displayEventEnd: true,
      locale: "ko",

      datesSet: function (info) {
        firstDayForOthers = info.view.currentStart;
        lastDayForOthers = info.view.currentEnd;

        fetchCalendarDataForOther();
        // calendar.refetchEvents(); // FullCalendar에 이벤트 업데이트
        console.log("dataset other Employee Schedules");
      },
    });

    calendarTopBar.render();
  }
);

/**
 * 대화하기
 */

const otherEmployeeChatRoom = document.getElementById("other-chat-button");

otherEmployeeChatRoom.addEventListener("click", function (event) {
  event.preventDefault();
  // console.log("요청한 employeeId =", otherEmployeeId);

  const form = document.createElement("form");
  form.method = "GET";
  form.action = "/chatrooms";

  const input = document.createElement("input");
  input.type = "hidden";
  input.name = "targetId";
  input.value = otherEmployeeId;
  form.appendChild(input);

  document.body.appendChild(form);
  form.submit();
});