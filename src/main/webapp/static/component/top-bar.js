const modalOpenButton = document.getElementById("employee-icon");
const modalCloseButton = document.getElementById("modalCloseButton");
const modal = document.getElementById("modalContainer");
const statusOpenButton = document.getElementById("user-status-box");
const statusClose = document.getElementById("status-contents");
const statusModel = document.getElementById("status-container");
var isStatusOpen = true;

/**
 * 사용자 상태 조회 및 변경
 *
 */

const userName = document.getElementById("user-name");
const userDept = document.getElementById("user-dept");
const userEmail = document.getElementById("user-email");
const userProfileImg = document.getElementById("profile-img");
const userStatusMessageContents = document.getElementById(
  "status-message-contents",
);
const userStatusBox = document.getElementById("user-status-box");

modalOpenButton.addEventListener("click", () => {
  modal.classList.remove("hidden");

  userName.innerHTML = "";
  userDept.innerText = "";
  userEmail.innerText = "";
  userProfileImg.innerText = "";
  userStatusMessageContents.innerText = "";
  userStatusBox.innerHTML = "";

  $.ajax({
    url: "/api/employees/current-employee",
    method: "GET",
    dataType: "json",
    success: function (employeeInfo) {
      userName.innerHTML = `${employeeInfo.name} <span id="user-position">${employeeInfo.position}</span>`;
      userDept.innerText = `${employeeInfo.deptName}`;
      userEmail.innerText = `${employeeInfo.email}`;
      userProfileImg.innerHTML = `<img
              src="${employeeInfo.profileUrl}"
              alt="Profile Image"
              width="150"
              height="150"
            /><div id="profile-img-modify"><i class="fa-solid fa-camera"></i></div>`;

      if (employeeInfo.statusMessage) {
        userStatusMessageContents.innerText = `${employeeInfo.statusMessage}`;
      } else {
        console.log("status가 null입니다.");
      }

      if (employeeInfo.status === 1) {
        userStatusBox.innerHTML = `<div id="status"><i class="fa-solid fa-check"></i></div>
                                  <div id="status-text">대화 가능</div>
                                  <div id="status-modify-icon">
                                    <i class="fa-solid fa-chevron-right"></i>
                                  </div>
                                `;
      } else if (employeeInfo.status === 2) {
        userStatusBox.innerHTML = `<div class="status-reset-icon"><i class="fa-solid fa-minus"></i></div>
                                    <div id="status-text">자리비움</div>
                                    <div id="status-modify-icon">
                                      <i class="fa-solid fa-chevron-right"></i>
                                    </div>`;
      } else if (employeeInfo.status === 3) {
        userStatusBox.innerHTML = `<div class="status-offline-icon"><i class="fa-solid fa-minus"></i></div>
                                    <div id="status-text">오프라인</div>
                                    <div id="status-modify-icon">
                                      <i class="fa-solid fa-chevron-right"></i>
                                    </div>`;
      } else if (employeeInfo.status === 4) {
        userStatusBox.innerHTML = `<div class="status-disturb-icon"><i class="fa-solid fa-minus"></i></div>
                                  <div id="status-text">방해금지</div>
                                  <div id="status-modify-icon">
                                    <i class="fa-solid fa-chevron-right"></i>
                                  </div>`;
      }
    },
    error: function (xhr, status, error) {
      console.log("회원 조회에 실패했습니다 :" + error);
    },
  });
});

modalCloseButton.addEventListener("click", () => {
  modal.classList.add("hidden");
});

// 닫기 버튼 말고 다른 창을 클릭해도 닫히게 하기.
window.addEventListener("click", function (event) {
  if (event.target === modal) {
    modal.classList.add("hidden");
    statusModel.classList.add("hidden");

    statusMessageButtons.innerHTML = "";
    statusMessageButtons.innerHTML = `<div id="message-pen"><i class="fa-solid fa-pen"></i></div>
              <div id="message-trash">
                <i class="fa-solid fa-trash-can"></i>
              </div>`;
  }
});
/**
 *  사용자 상태 변경
 */
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
    logoutForm.submit();
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
  "search-group-chat-rooms",
);

/**
 *  검색바 구현
 *
 */

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
 * 프로필 사진 변경
 *
 */
const profileImg = document.getElementById("profile-img");
const fileInput = document.getElementById("fileInput");
const forProfileTarget = document.getElementById("modalContent");

//하나만 선택되도록 변경
fileInput.removeAttribute("multiple");

profileImg.addEventListener("click", function (event) {
  modal.classList.add("hidden");

  Swal.fire({
    title: "프로필 사진 변경",
    text: "프로필 사진을 변경하시겠습니까?",
    showCancelButton: true,
    confirmButtonColor: "#2993ff",
    cancelButtonColor: "#9a9a9a",
    confirmButtonText: "변경",
    cancelButtonText: "취소",
  }).then((result) => {
    if (result.isConfirmed) {
      fileInput.click(); //
      const formData = new FormData();
      // 파일이 선택되면 프로필 이미지 변경
      fileInput.addEventListener("change", function () {
        const file = fileInput.files[0]; // 선택된 파일

        if (file) {
          // 이미지 파일 형식인지 확인
          if (file.type.startsWith("image/")) {
            formData.append("profile", file);
            $.ajax({
              method: "PUT",
              url: "/api/employees/profile",
              contentType: false,
              processData: false,
              data: formData,
              success: function (employeeProfileUrl) {
                Swal.fire("변경완료 되었습니다.");
                // modal.classList.add("hidden");
              },
              error: function (xhr, status, error) {
                console.log("파일 업로드 실패 + " + error);
              },
            });
          } else {
            Swal.fire("이미지 파일만 선택 가능합니다.");
            fileInput.value = ""; // 유효하지 않은 파일 선택 시 초기화
          }
        }
      });
    }
  });
});

/**
 * 사용자 상태 메세지 변경
 *
 */
const messagePen = document.getElementById("status-message-buttons");
const statusMessageButtons = document.getElementById("status-message-buttons");
messagePen.addEventListener("click", function (event) {
  const targetPen = event.target.closest("#message-pen");

  if (targetPen) {
    statusMessageButtons.innerHTML = "";
    userStatusMessageContents.innerHTML =
      "<textarea  id='tmp-employee-contents' type='text' placeholder='메시지를 입력하세요' maxlength='80'></textarea>";

    statusMessageButtons.innerHTML = `<div id="message-save"><i class="fa-solid fa-circle-chevron-down"></i></div>
              <div id="message-trash">
                <i class="fa-solid fa-trash-can"></i>
              </div>`;
  }
});

//엔터 두번 방지하기
messagePen.addEventListener("input", function (event) {
  const targetContents = event.target.closest("#tmp-employee-contents");

  if (targetContents) {
    // 연속된 엔터 두 번을 하나로 치환
    this.value = this.value.replace(/\n{2,}/g, "\n");
  }
});

statusMessageButtons.addEventListener("click", function (event) {
  const targetTrash = event.target.closest("#message-trash");
  const targetSave = event.target.closest("#message-save");
  // const targetTmpContents = event.target.closest("#tmp-employee-contents");

  if (targetTrash) {
    userStatusMessageContents.innerText = "";
    statusMessageButtons.innerHTML = `<div id="message-pen"><i class="fa-solid fa-pen"></i></div>
              <div id="message-trash">
                <i class="fa-solid fa-trash-can"></i>
              </div>`;
  }

  if (targetSave) {
    const targetTmpContents = document.getElementById("tmp-employee-contents");
    if (targetTmpContents) {
      // 입력된 텍스트를 저장
      userStatusMessageContents.innerText = targetTmpContents.value;

      // <input> 요소만 삭제
      targetTmpContents.remove();

      statusMessageButtons.innerHTML = "";
      statusMessageButtons.innerHTML = `<div id="message-pen"><i class="fa-solid fa-pen"></i></div>
              <div id="message-trash">
                <i class="fa-solid fa-trash-can"></i>
              </div>`;
    }
  }
});

/**
 * 다른사용자 조회 js
 *
 */
const otherEmployeeContainer = document.getElementById(
  "other-employee-info-container",
);
const otherEmployeeClose = document.getElementById(
  "other-employee-schedule-close",
);
const searchPeopleElements = document.getElementById("search-people-contents");
let otherEmployeeId = 0;

/**
 * 상위 요소에 이벤트 리스너 추가 - employee profile은 동적으로 생성
 */
searchPeopleElements.addEventListener("click", function (event) {
  //각 요소 비우기
  const otherEmployeeNamePosition = document.getElementById(
    "other-employee-name-position",
  );
  const otherDeptContainer = document.getElementById("other-dept-container");
  const otherEmailContainer = document.getElementById("other-email-container");
  const otherLocateContainer = document.getElementById(
    "other-locate-container",
  );

  const otherEmployeeImg = document.getElementById("other-employee-img");
  const employeeName = document.getElementById(
    "employee-name-location-top-bar",
  );
  const otherEmployeeStatus = document.getElementById("other-employee-status");
  const otherContents = document.getElementById("other-contents-container");

  otherEmployeeNamePosition.innerHTML = "";
  otherDeptContainer.innerHTML = "";
  otherEmailContainer.innerHTML = "";
  otherLocateContainer.innerHTML = "";
  otherEmployeeStatus.innerHTML = "";
  otherContents.innerHTML = "";
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

        employeeName.innerHTML = `<p>${employeeInfo.name} 님의 일정</p>`;

        if (employeeInfo.statusMessage) {
          otherContents.innerHTML = `<p id="other-contents" style="font-size: 13px;">
                                      ${employeeInfo.statusMessage}
                                      </p>`;
        }

        if (employeeInfo.status === 1) {
          otherEmployeeStatus.innerHTML = `<div id="employee-status" style="background-color: #92c353;"><i class="fa-solid fa-check" style="color: white;"></i></div>`;
        } else if (employeeInfo.status === 2) {
          otherEmployeeStatus.innerHTML = `<div id="employee-status" style="background-color: #eaa300;"><i class="fa-solid fa-minus" style="color: white;"></i></div>`;
        } else if (employeeInfo.status === 3) {
          otherEmployeeStatus.innerHTML = `<div id="employee-status" style="background-color: #d9d9d9;"><i class="fa-solid fa-minus" style="color: white;"></i></div>`;
        } else if (employeeInfo.status === 4) {
          otherEmployeeStatus.innerHTML = `<div id="employee-status" style="background-color: #ea0000;"><i class="fa-solid fa-minus" style="color: white;"></i></div>`;
        }
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

const searchChatRoomElements = document.getElementById(
  "search-group-chat-rooms",
);

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
    form.submit();
  }
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
    localDate.getTime() - localDate.getTimezoneOffset() * 60000,
  ).toISOString();

  return isoDate.replace(".000Z", ""); // 초까지 포함된 형식을 유지
}

function fetchCalendarDataForOther() {
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
        info.event.extendedProps.scheduleId,
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
});

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
