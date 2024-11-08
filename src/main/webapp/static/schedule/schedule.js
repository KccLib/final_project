var requestCount = 0;
var firstDay;
var lastDay;
var employeeEvents = [];
var backgroundColors = [
  "#FFEB3B", // 밝은 노란색
  "#4CAF50", // 짙은 녹색
  "#2196F3", // 파란색
  "#F44336", // 빨간색
  "#9C27B0", // 보라색
];

var textColors = [
  "#000000", // 검은색
  "#FFFFFF", // 흰색
  "#FFFFFF", // 흰색
  "#FFFFFF", // 흰색
  "#FFFFFF", // 흰색
];

function getRandomColor() {
  const randomIndex = Math.floor(Math.random() * backgroundColors.length);
  return backgroundColors[randomIndex];
}

function getRandomTextColor() {
  const randomIndex = Math.floor(Math.random() * textColors.length);
  return textColors[randomIndex];
}

function getFirstAndLastDateOfMonth() {
  // 날짜를 'YYYY-MM-DD' 형식으로 포맷팅
  var formattedFirstDay = firstDay.toISOString().split("T")[0];
  var formattedLastDay = lastDay.toISOString().split("T")[0];

  console.log("요청횟수는 : " + requestCount);
  requestCount++;
  //json 형식으로 반환
  return {
    firstDay: formattedFirstDay,
    lastDay: formattedLastDay,
  };
}
function formatDateToISO(dateString) {
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

function fetchCalendarData() {
  var dateData = getFirstAndLastDateOfMonth();
  console.log(dateData);
  //일정조회 ajax
  $.ajax({
    url: "/api/schedules/calendar",
    method: "GET",
    data: {
      startDate: dateData.firstDay,
      endDate: dateData.lastDay,
    },
    dataType: "json",
    success: function (schedules) {
      console.log("통신 성공");

      // 배열 및 캘린더 초기화
      employeeEvents = [];
      calendar.removeAllEvents();

      schedules.forEach(function (schedule) {
        // 시간부분
        var startTime = schedule.startedDt.split(" ")[1];
        var endTime = schedule.endedDt.split(" ")[1];
        var event = {
          title: schedule.name,
          // ISO 8601 형식으로 변환
          start: formatDateToISO(schedule.startedDt),
          end: formatDateToISO(schedule.endedDt),
          extendedProps: {
            // 추가 정보를 담기 - 스케줄의 각 id의 고유한 번호, 내스케줄여부
            isMySchedule: schedule.isMySchedule,
            scheduleId: schedule.scheduleId,
          },
        };
        console.log("가져온 scheduleId : " + schedule.scheduleId);

        if (startTime === "00:00:00" && endTime === "00:00:00") {
          event.allDay = true; // allDay 이벤트로 설정
        }
        employeeEvents.push(event);
        calendar.addEvent(event); // 이벤트를 FullCalendar에 추가
      });
      console.log("events:", employeeEvents);

      calendar.render(); // 캘린더 업데이트
    },
    error: function (xhr, status, error) {
      console.error("일정 조회에 실패하였습니다.");
    },
  });
}

var schduleIdForDelete;

// 우클릭 이벤트 닫기
document.addEventListener("click", function (e) {
  // 노출 초기화
  popMenu.style.display = "none";
  popMenu.style.top = null;
  popMenu.style.left = null;
});

// 캘린더 랜더링
document.addEventListener("DOMContentLoaded", function () {
  var calendarEl = document.getElementById("calendar");
  calendar = new FullCalendar.Calendar(calendarEl, {
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
    events: employeeEvents,

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

      // 이벤트에 우클릭 이벤트 리스너 추가
      info.el.addEventListener("contextmenu", function (event) {
        event.preventDefault(); // 브라우저 기본 우클릭 메뉴를 막음
        // alert("이벤트를 우클릭했습니다: " + info.event.title);
        var x = event.pageX + "px"; // 현재 마우스의 X좌표
        var y = event.pageY + "px"; // 현재 마우스의 Y좌표

        schduleIdForDelete = info.event.extendedProps.scheduleId;
        // 디버깅을 위한 로그
        console.log("삭제할 일정 ID:", schduleIdForDelete);
        const popMenu = document.getElementById("popMenu"); // 팝업창을 담아옴

        /*
            스타일 지정, 우클릭 한 위치에 팝업창 띄워줌..
        */
        popMenu.style.position = "absolute"; // 변경: absolute로 설정
        popMenu.style.left = x;
        popMenu.style.top = y;
        popMenu.style.display = "block"; // 변경: display를 block으로 설정
        popMenu.style.zIndex = 10000; // zIndex 설정
      });
    },

    // 상세보기 구현
    eventClick: function (info) {
      const inviteCount = document.getElementById("count");
      let inviteMemberCount = 0;
      //수정을 위한
      scheduleIdForModify = info.event.extendedProps.scheduleId;
      const detailTitle = document.getElementById("detail-title");
      const detailStartDate = document.getElementById("start-date-detail");
      const detailEndDate = document.getElementById("end-date-detail");

      detailTitle.innerText = info.event.title;
      detailStartDate.value = formatDateTime(info.event.startStr);
      // detailEndDate.value = formatDateTime(info.event.endDate - 1);

      //allDay일 때 종료일 재설정
      if (info.event.allDay) {
        const endDateTime = new Date(info.event.endStr);
        endDateTime.setDate(endDateTime.getDate() - 1);
        const tempEndDate = formatDateTime(
          endDateTime.toISOString().split(".")[0],
        ); // ISO 형식으로 변환 후 포맷

        detailEndDate.value = tempEndDate.split(" ")[0];
      } else {
        detailEndDate.value = formatDateTime(info.event.endStr);
      }

      // 주최자 넣기
      const masterDiv = document.getElementById("master-name");

      //해당 일정에 schduleId ajax 통신 내용, 참여인원 status가져오기
      const scheduleId = info.event.extendedProps.scheduleId;
      schduleIdForDelete = info.event.extendedProps.scheduleId;
      $.ajax({
        type: "GET",
        url: "/api/schedules/detail",
        dataType: "json",
        data: {
          scheduleId: scheduleId,
        },
        success: function (scheduleDetail) {
          console.log("일정 상세 조회 성공 :");
          // // 예: scheduleDetail에서 데이터 추출 후 처리
          // console.log("내용:", scheduleDetail.contents);
          // console.log("작성자:", scheduleDetail.writer);
          // console.log("내 일정 여부:", scheduleDetail.isMySchedule);
          masterDiv.value =
            scheduleDetail.scheduleMaster.employeeName +
            " " +
            scheduleDetail.scheduleMaster.deptName;

          // 사원 정보 출력
          // 테이블 헤더 생성
          var tableHTML = `
            <table id="detail-table" style="width:100%; border-collapse: collapse;">
                <thead id="detail-people-header">
                    <tr>
                        <th style="width: 124px;">이름</th>
                        <th style="width: 207px;">부서</th>
                        <th>참석 여부</th>
                    </tr>
                </thead>
                <tbody id="detail-people-tbody">
        `;

          // 사원 정보 출력
          scheduleDetail.scheduleDetailEmployees.forEach(function (employee) {
            inviteMemberCount++;
            console.log("현재 회원의 상태 : " + employee.isParticipated);
            // 참석 여부에 따라 표시할 텍스트를 결정
            let participationStatus;
            const participationValue = parseInt(employee.isParticipated, 10); // 문자열을 숫자로 변환

            if (participationValue === 0) {
              participationStatus = "💬 대기 중";
            } else if (participationValue === 1) {
              participationStatus = "❌ 거절";
            } else if (participationValue === 2) {
              participationStatus = "✔ 승인";
            } else {
              participationStatus = "상태 불명"; // 예외 처리
            }
            tableHTML += `
                          <tr>
                              <td style="width: 125px; border-right: 1px solid #d2dae1;">${employee.employeeName}</td>
                              <td style="width: 207px; border-right: 1px solid #d2dae1;">${employee.deptName}</td>
                              <td style="width: 162px;">${participationStatus}</td>
                          </tr>
                      `;
          });

          //수정에 총원 보여주기 위해서 count
          inviteCount.innerText = inviteMemberCount; // innerText로 값 설정

          // 테이블 닫기
          tableHTML += `
                              </tbody id="detail-table-body">
                          </table>
                      `;

          // 테이블을 add-people-table div에 추가
          document.getElementById("add-people-table").innerHTML = tableHTML;

          const quillDeltaString = scheduleDetail.contents; // Quill Delta 형식의 JSON 문자열

          // JSON 문자열을 객체로 변환
          const quillDelta = JSON.parse(quillDeltaString);
          // Quill 임시 인스턴스 생성
          const tmpQuill = new Quill("#temp-quill-container", {
            theme: "snow",
            readOnly: true, // 읽기 전용으로 설정
            modules: {
              toolbar: false, // 툴바 비활성화
            },
          });

          // Delta 형식을 HTML로 변환
          // JSON 객체로 변환하여 설정
          tmpQuill.setContents(quillDelta);

          // HTML 가져오기
          const htmlContent = tmpQuill.root.innerHTML; // Quill 에디터의 root에서 HTML 가져오기
          console.log(
            "quill로 파싱하기 전 내용 : " + JSON.stringify(quillDelta),
          ); // JSON 형태로 보기 좋게 출력
          console.log("quill로 파싱한 내용 : " + htmlContent); // 변환된 HTML 출력

          // 내용을 div에 삽입
          document.getElementById("detail-text-contents").innerHTML =
            htmlContent;

          const modifyButton = document.getElementById("detail-modify");
          const addSchedule = document.getElementById("add-schedule-container");

          // 수정 button hidden 여부
          if (scheduleDetail.isMySchedule == 1) {
            modifyButton.classList.remove("hidden");
          } else {
            modifyButton.classList.add("hidden");
          }
          //****************  일정수정 확인 누르면 일정의 데이터를 일정 등록 페이지에 넣어두기
          modifyButton.addEventListener("click", function () {
            const addScheduleButton = document.getElementById(
              "add-schedule-modal-buttons",
            );
            addScheduleButton.innerHTML =
              '<button type="submit" id="submit-add-schedule">일정 수정</button><br>' +
              '<button id="close-button">닫기</button>';

            const closeButton = document.getElementById("close-button");

            // 모달 닫기 버튼 클릭 시 모달 닫기
            closeButton.onclick = function (event) {
              event.preventDefault(); // 기본 동작 방지
              event.stopPropagation(); // 이벤트 전파 방지
              modalContainer.classList.add("hidden");
              //내용 비우기
              document.getElementById("schedule-form").reset();
              quill.setContents([]);
            };

            Swal.fire({
              title: "일정을 수정하겠습니까?",
              showCancelButton: true,
              confirmButtonText: "확인",
              cancelButtonText: "취소",
            }).then((result) => {
              if (result.isConfirmed) {
                addSchedule.classList.remove("hidden"); // 확인 버튼을 눌렀을 때 hidden 클래스 제거
              }
              $.ajax({
                type: "GET",
                url: "/api/schedules/detail",
                dataType: "json",
                data: {
                  scheduleId: scheduleId,
                },
                success: function (scheduleDetail) {
                  //수정 종료 시작일 종료일
                  document.getElementById("schedule-name").value =
                    info.event.title;

                  document.getElementById("start-date").value =
                    info.event.startStr;
                  //allDay일 때 종료일 재설정
                  if (info.event.allDay) {
                    const endDateTime = new Date(info.event.endStr);
                    endDateTime.setDate(endDateTime.getDate() - 1);
                    tempEndDate = formatDateTime(
                      endDateTime.toISOString().split(".")[0],
                    );
                    document.getElementById("end-date").value =
                      tempEndDate.split(" ")[0];
                  } // ISO 형식으로 변환 후 포맷

                  //일정에 초대된 사람들 가져오기
                  const existingEmployees =
                    scheduleDetail.scheduleDetailEmployees.map(
                      function (employee) {
                        return {
                          name:
                            employee.employeeName +
                            "/" +
                            employee.position +
                            "/" +
                            employee.deptName,
                          value: employee.employeeId.toString(),
                        };
                      },
                    );
                  makeTagify(existingEmployees);

                  // 일정 내용 가져오기
                  const quillDeltaString = scheduleDetail.contents; // Quill Delta 형식의 JSON 문자열

                  // JSON 문자열을 객체로 변환
                  const quillDelta = JSON.parse(quillDeltaString);
                  // Delta 형식을 HTML로 변환
                  // JSON 객체로 변환하여 설정
                  quill.setContents(quillDelta);

                  //초대된 사람들 수도 갱신
                  const inviteCount = document.getElementById("count");
                  inviteCount.innerText = tagify.value.length; // innerText로 값 설정
                },
                error: function (error) {
                  console.log(
                    "수정을 위한 일정의 상세를 가져올 수 없습니다" + error,
                  );
                },
              });
            });
          });
        },
        error: function (error) {
          console.error("일정 상세 정보를 서버로부터 받아올 수 없습니다.");
        },
      });

      detailContainer.classList.remove("hidden");
    },

    displayEventTime: true,
    displayEventEnd: true,
    locale: "ko",

    datesSet: function (info) {
      firstDay = info.view.currentStart;
      lastDay = info.view.currentEnd;

      fetchCalendarData();
      // calendar.refetchEvents(); // FullCalendar에 이벤트 업데이트
      console.log("dataset");
    },
  });

  calendar.render();
});

function formatDateTime(dateTimeStr) {
  // 날짜와 시간 구분자 'T'의 위치 찾기
  let tIndex = dateTimeStr.indexOf("T");

  // 날짜만 있는 경우
  if (tIndex === -1) {
    return dateTimeStr; // 'T'가 없으면 그대로 반환
  }

  // 'T'를 기준으로 문자열을 나눔
  let datePart = dateTimeStr.slice(0, tIndex); // 날짜 부분
  let timePart = dateTimeStr.slice(tIndex + 1, tIndex + 6); // 시간 부분 (HH:MM까지)

  return datePart + " " + timePart; // 날짜와 시간 부분을 공백으로 구분해서 반환
}

function formatDateTime(dateTimeStr) {
  // 날짜와 시간 구분자 'T'의 위치 찾기
  let tIndex = dateTimeStr.indexOf("T");

  // 날짜만 있는 경우
  if (tIndex === -1) {
    return dateTimeStr; // 'T'가 없으면 그대로 반환
  }

  // 'T'를 기준으로 문자열을 나눔
  let datePart = dateTimeStr.slice(0, tIndex); // 날짜 부분
  let timePart = dateTimeStr.slice(tIndex + 1, tIndex + 6); // 시간 부분 (HH:MM까지)

  return datePart + " " + timePart; // 날짜와 시간 부분을 공백으로 구분해서 반환
}

// 일정등록 모달
const addScheduleButton = document.getElementById("add-schedule-button");
var modalContainer = document.getElementById("add-schedule-container");

// 버튼 클릭 시 일정등록 열기
addScheduleButton.onclick = function () {
  modalContainer.classList.remove("hidden");
  closeModelButtonSwitching();
  makeTagify();
  const inviteCount = document.getElementById("count");
  inviteCount.innerText = 0; // innerText로 값 설정
};

var closeButton = document.getElementById("close-button");

function closeModelButtonSwitching() {
  const addScheduleButton = document.getElementById(
    "add-schedule-modal-buttons",
  );
  addScheduleButton.innerHTML =
    '<button type="submit" id="submit-add-schedule">일정 추가</button><br>' +
    '<button id="close-button">닫기</button>';

  // 새로운 closeButton 요소 재정의
  closeButton = document.getElementById("close-button");

  // 모달 닫기 버튼 클릭 시 모달 닫기
  closeButton.onclick = function (event) {
    event.preventDefault(); // 기본 동작 방지
    event.stopPropagation(); // 이벤트 전파 방지
    modalContainer.classList.add("hidden");
    closeModelButtonSwitching();
    //내용 비우기
    document.getElementById("schedule-form").reset();
    quill.setContents([]);
  };
}

window.addEventListener("click", function (event) {
  if (event.target === modalContainer) {
    modalContainer.classList.add("hidden");
    closeModelButtonSwitching();
  }
});

// 일정 상세 모달
var detailContainer = document.getElementById("detail-container");
var detailCloseButton = document.getElementById("detail-close");

// 모달 닫기 버튼 클릭 시 모달 닫기
detailCloseButton.onclick = function () {
  detailContainer.classList.add("hidden");
};

window.addEventListener("click", function (event) {
  if (event.target === detailContainer) {
    detailContainer.classList.add("hidden");
  }
});

// Mobiscroll에 한국어 로케일 설정
mobiscroll.setOptions({
  locale: mobiscroll.localeKo,
});

// Mobiscroll 시작 날짜와 시간 선택기
var startDatePicker = mobiscroll.datepicker("#start-date", {
  controls: ["datetime"], // 날짜와 시간 또는 날짜만 선택
  display: "bubble", // 팝업 형식
  dateFormat: "YYYY-MM-DD", // 날짜 형식
  timeFormat: "HH:mm", // 시간 형식
  stepMinute: 5, // 분 단위 증가폭
  returnFormat: "iso8601", // ISO 8601 형식 반환
  onChange: function (event) {
    if (event.value) {
      validateDates();
    }
  },
});

// Mobiscroll 끝 날짜와 시간 선택기
var endDatePicker = mobiscroll.datepicker("#end-date", {
  controls: ["datetime"], // 날짜와 시간 또는 날짜만 선택
  display: "bubble",
  dateFormat: "YYYY-MM-DD",
  timeFormat: "HH:mm",
  stepMinute: 5,
  returnFormat: "iso8601",
  onChange: function (event) {
    console.log("종료값을 설정하였습니다");
    if (event.value) {
      validateDates();
    }
  },
});

// "하루 종일" 체크박스 이벤트
document.getElementById("allDayCheck").addEventListener("change", function (e) {
  const isChecked = e.target.checked;

  if (isChecked) {
    startDatePicker.setOptions({ controls: ["date"] });
    endDatePicker.setOptions({ controls: ["date"] });
    console.log("하루 종일 선택됨");
  } else {
    startDatePicker.setOptions({ controls: ["datetime"] });
    endDatePicker.setOptions({ controls: ["datetime"] });
    console.log("하루 종일 해제됨");
  }
});

function validateDates() {
  const startDateInput = document.getElementById("start-date");
  const endDateInput = document.getElementById("end-date");
  const isChecked = document.getElementById("allDayCheck");

  const startDate = new Date(startDateInput.value);
  const endDate = new Date(endDateInput.value);

  // 끝 날짜가 시작 날짜보다 작을 때
  if (endDate < startDate) {
    endDateInput.value = ""; // 끝 날짜 초기화
    mobiscroll.getInst(endDateInput).setVal(null); // Mobiscroll에서 끝 날짜 필드 초기화
    endDatePicker.close();

    Swal.fire({
      text: "끝 날짜는 시작 날짜보다 커야 합니다.",
    });
  }

  // 종일 체크박스가 체크되어 있지 않은 경우에만 날짜 비교
  if (!isChecked.checked) {
    console.log("Start Date:", startDate);
    console.log("End Date:", endDate);

    // `startDate`와 `endDate`가 Date 객체인지 확인
    if (startDate instanceof Date && endDate instanceof Date) {
      if (endDate.getTime() && endDate.getDate() !== startDate.getDate()) {
        // 날짜가 다르면 끝 날짜 초기화
        endDateInput.value = ""; // 끝 날짜 입력 필드 초기화
        mobiscroll.getInst(endDateInput).setVal(null); // Mobiscroll에서 끝 날짜 필드 초기화
        endDatePicker.close(); // 종료일 선택기 닫기

        // 경고 메시지 표시
        Swal.fire({
          text: "시작일과 종료일의 날짜가 같아야 합니다.",
        });
      }
    } else {
      console.error(
        "Invalid Date: startDate 또는 endDate가 유효하지 않습니다.",
      );
    }
  }
}

var inviteEmployee = 0;
var quill = new Quill("#schedule-contents", {
  theme: "snow", // 또는 'bubble'
  modules: {
    toolbar: [
      [{ header: [1, 2, false] }],
      ["bold", "italic", "underline"],
      ["link", "image"],
      ["clean"], // remove formatting button
    ],
  },
  // 에디터의 크기 설정
  style: {
    width: "100%", // 원하는 너비
  },
});

/* 인원초대를 위한 code   태기파이 */
var tagify;

function makeTagify(existingEmployees = []) {
  // 화이트리스트 초기화
  let whitelist = [];

  // AJAX를 통해 직원 목록 가져오기
  $.ajax({
    url: "/api/employees/all/include",
    method: "GET",
    dataType: "json",
    success: function (allEmployeeData) {
      whitelist = allEmployeeData.map(function (employee) {
        return {
          name:
            employee.name + "/" + employee.position + "/" + employee.deptName, // 태그에 표시될 내용
          value: employee.id.toString(), // 직원 ID를 문자열로 변환하여 저장
        };
      });

      console.log("직원 데이터를 성공적으로 가져왔습니다:", whitelist);

      // Tagify 초기화
      let inputElm = document.querySelector("input[name='employees[]']");

      // initialize Tagify
      tagify = new Tagify(inputElm, {
        enforceWhitelist: true, // 화이트리스트에서 허용된 태그만 사용
        whitelist: whitelist, // 화이트 리스트 배열. 화이트 리스트를 등록하면 자동으로 드롭다운 메뉴가 생긴다
        autogrow: false, // 태그 입력창이 자동으로 늘어난다
        originalInputValueFormat: function (valuesArr) {
          return valuesArr.map(function (item) {
            return item.value;
          });
        },
        templates: {
          tag: function (tagData) {
            return `
                    <tag title="${tagData.name}"
                        contenteditable='false'
                        spellcheck='false'
                        class='tagify__tag ${
                          tagData.class ? tagData.class : ""
                        }'
                        ${this.getAttributes(tagData)}>
                        <x title='remove tag' class='tagify__tag__removeBtn'></x>
                        <div>
                            <span class='tagify__tag-text'>${
                              tagData.name
                            }</span>
                        </div>
                    </tag>`;
          },
          dropdownItem: function (tagData) {
            return `
                    <div ${this.getAttributes(tagData)}
                        class='tagify__dropdown__item ${
                          tagData.class ? tagData.class : ""
                        }'>
                        <span>${tagData.name}</span>
                    </div>`;
          },
        },
        dropdown: {
          // 검색에 사용할 속성 지정
          searchKeys: ["name"],
          maxItems: 10, // 최대 표시 아이템 수 (필요에 따라 조정)
          enabled: 0, // 0으로 설정하면 입력한 글자 수와 상관없이 항상 드롭다운을 표시
        },
      });
      // 기존 직원 태그 추가
      if (existingEmployees.length > 0) {
        tagify.addTags(existingEmployees);
      }
      // 이벤트 리스너 등록 및 기타 Tagify 관련 설정
      // 만일 모든 태그 지우기 기능 버튼을 구현한다면
      // document
      //   .querySelector("#clearTagsButton") // ID로 선택
      //   .addEventListener("click", tagify.removeAllTags.bind(tagify));

      // tagify 전용 이벤트 리스터
      tagify
        .on("add", onAddTag) // 태그가 추가되면
        .on("remove", onRemoveTag) // 태그가 제거되면
        .on("input", onInput) // 태그가 입력되고 있을 경우
        .on("invalid", onInvalidTag) // 허용되지 않는 태그일 경우
        .on("click", onTagClick) // 해시 태그 블럭을 클릭할 경우
        .on("focus", onTagifyFocusBlur) // 포커스 될 경우
        .on("blur", onTagifyFocusBlur) // 반대로 포커스를 잃을 경우
        .on("edit:start", onTagEdit) // 입력된 태그 수정을 할 경우
        .on("dropdown:hide dropdown:show", (e) => console.log(e.type)) // 드롭다운 메뉴가 사라질경우
        .on("dropdown:select", function (e) {
          console.log("Selected employee ID:", e.detail.data.value);
        });

      // 이벤트 리스너 콜백 메소드 정의
      function onAddTag(e) {
        console.log("onAddTag: ", e.detail);
        console.log("original input value: ", inputElm.value);
        const inviteCount = document.getElementById("count");
        inviteCount.innerText = tagify.value.length; // innerText로 값 설정
      }

      function onRemoveTag(e) {
        console.log(
          "onRemoveTag:",
          e.detail,
          "tagify instance value:",
          tagify.value,
        );
        const inviteCount = document.getElementById("count");
        inviteCount.innerText = tagify.value.length; // innerText로 값 설정
      }

      function onTagEdit(e) {
        console.log("onTagEdit: ", e.detail);
      }

      function onInvalidTag(e) {
        console.log("onInvalidTag: ", e.detail);
      }

      function onTagClick(e) {
        console.log(e.detail);
        console.log("onTagClick: ", e.detail);
      }

      function onTagifyFocusBlur(e) {
        console.log(e.type, "event fired");
      }

      function onDropdownSelect(e) {
        console.log("onDropdownSelect: ", e.detail);
      }

      function onInput(e) {
        console.log("onInput: ", e.detail);

        tagify.loading(true); // 태그 입력하는데 우측에 loader 애니메이션 추가
        tagify.loading(false); // loader 애니메이션 제거

        tagify.dropdown.show(e.detail.value); // 드롭다운 메뉴 보여주기
        tagify.dropdown.hide(); // 드롭다운 제거
      }
    },
    error: function (xhr, status, error) {
      console.error("직원 데이터를 가져오는 데 실패했습니다:", error);
    },
  });
}
let scheduleIdForModify;

function submitAndModifySchedule(submitType) {
  if (submitType == 1) {
    console.log("일정 등록을 수행하겠습니다.");
    // 확인 버튼이 눌렸을 때만 실행
    Swal.fire({
      title: "일정을 등록하시겠습니까?",
      showCancelButton: true,
      confirmButtonText: "저장",
    }).then((result) => {
      if (result.isConfirmed) {
        // 사용자가 확인 버튼을 클릭한 경우
        // 입력 값 가져오기
        var scheduleName = document.getElementById("schedule-name").value;
        var startDate = document.getElementById("start-date").value;
        var endDate = document.getElementById("end-date").value;

        // 에디터 내용 가져오기
        var deltaContent = quill.getContents();
        var deltaContentJson = JSON.stringify(deltaContent); // JSON 문자열로 변환

        // 선택한 직원 ID 가져오기
        var selectedEmployeeIds = tagify.value.map(function (tag) {
          return tag.value;
        });

        // 값들 확인
        console.log(
          "일정 추가:",
          scheduleName,
          startDate,
          endDate,
          selectedEmployeeIds,
          deltaContentJson,
        );
        var emailCheck;
        const checkedEmailSend = document.getElementById("email-alram");
        if (checkedEmailSend.checked) {
          emailCheck = 1;
        } else {
          emailCheck = 0;
        }

        // 서버로 전송할 데이터 설정
        var formData = {
          name: scheduleName,
          startedDt: startDate,
          endedDt: endDate,
          employeeIds: selectedEmployeeIds,
          contents: deltaContentJson, // 에디터 내용 추가
          emailCheck: emailCheck,
        };
        // 일정등록  AJAX
        $.ajax({
          url: "/schedules/save",
          method: "POST",
          contentType: "application/json",

          data: JSON.stringify(formData),
          success: function (response) {
            Swal.fire({
              title: "일정 등록을 성공했습니다.",
              icon: "success",
              confirmButtonText: "확인",
            }).then((result) => {
              if (result.isConfirmed) {
                window.location.href = "/schedules";
              }
            }); // 입력 필드 초기화
            document.getElementById("schedule-form").reset();
            quill.setContents([]); // 에디터 내용 초기화
          },
          error: function (xhr, status, error) {
            Swal.fire({
              icon: "error",
              title: "일정 등록을 실패했습니다.",
            });
            console.error("Error:", xhr.responseText); // 오류 응답 내용을 콘솔에 출력
          },
        });
      }
    });
  } else {
    // 확인 버튼이 눌렸을 때만 실행
    Swal.fire({
      title: "일정을 수정하시겠습니까?",
      html: "초대받은 사람들의 초대 승인 여부는 초기화됩니다.",
      showCancelButton: true,
      confirmButtonText: "저장",
    }).then((result) => {
      if (result.isConfirmed) {
        // 사용자가 확인 버튼을 클릭한 경우
        // 입력 값 가져오기
        var scheduleName = document.getElementById("schedule-name").value;
        var startDate = document.getElementById("start-date").value;
        var endDate = document.getElementById("end-date").value;

        // 에디터 내용 가져오기
        var deltaContent = quill.getContents();
        var deltaContentJson = JSON.stringify(deltaContent); // JSON 문자열로 변환

        // 선택한 직원 ID 가져오기
        var selectedEmployeeIds = tagify.value.map(function (tag) {
          return tag.value;
        });

        // 값들 확인
        console.log(
          "일정 추가:",
          scheduleName,
          startDate,
          endDate,
          selectedEmployeeIds,
          deltaContentJson,
        );
        var emailCheck;
        const checkedEmailSend = document.getElementById("email-alram");
        if (checkedEmailSend.checked) {
          emailCheck = 1;
        } else {
          emailCheck = 0;
        }

        // 서버로 전송할 데이터 설정
        var formData = {
          scheduleId: scheduleIdForModify,
          name: scheduleName,
          startedDt: startDate,
          endedDt: endDate,
          employeeIds: selectedEmployeeIds,
          contents: deltaContentJson, // 에디터 내용 추가
          emailCheck: emailCheck,
        };
        // 일정등록  AJAX
        $.ajax({
          url: "/api/schedules/modify",
          method: "PATCH",
          contentType: "application/json",
          data: JSON.stringify(formData),
          success: function (response) {
            Swal.fire({
              title: "일정 수정을 성공했습니다.",
              icon: "success",
              confirmButtonText: "확인",
            }).then((result) => {
              if (result.isConfirmed) {
                window.location.href = "/schedules";
              }
            }); // 입력 필드 초기화
            document.getElementById("schedule-form").reset();
            quill.setContents([]); // 에디터 내용 초기화
          },
          error: function (xhr, status, error) {
            Swal.fire({
              icon: "error",
              title: "일정 수정을 실패했습니다.",
            });
            console.error("Error:", xhr.responseText); // 오류 응답 내용을 콘솔에 출력
          },
        });
      }
    });
  }
}
// 일정 추가 양식 제출 시 이벤트 처리
document.getElementById("schedule-form").onsubmit = function (event) {
  event.preventDefault(); // 기본 제출 동작 방지
  const submitButton = document.getElementById("submit-add-schedule");
  const buttonText = submitButton.textContent; // 또는 submitButton.innerText;
  let actionValue; // 값을 저장할 변수

  if (buttonText === "일정 추가") {
    actionValue = 1; // "일정 추가"일 때 1
  } else if (buttonText === "일정 수정") {
    actionValue = 2; // "일정 수정"일 때 2
  } else {
    actionValue = 0; // 기본값 (선택 사항)
  }
  submitAndModifySchedule(actionValue);
};
// 일정 삭제
var deleteButton = document.getElementById("detail-delete");

deleteButton.addEventListener("click", function () {
  console.log("현재 삭제하려고 하는 일정 : " + schduleIdForDelete);
  // SweetAlert로 삭제 확인창 표시
  Swal.fire({
    title: "정말 삭제하시겠습니까?",
    html: "일정의 주최자가 일정을 삭제하면<br>초대된 사람의 일정에서도 삭제됩니다.<br><br>또한, 초대 받은 일정은 거절로 상태가 변경됩니다.",

    showCancelButton: true,
    confirmButtonColor: "#d33",
    cancelButtonColor: "#3085d6",
    confirmButtonText: "일정삭제",
    cancelButtonText: "닫기",
  }).then((result) => {
    if (result.isConfirmed) {
      $.ajax({
        url: `/api/schedules/${schduleIdForDelete}`,
        method: "DELETE",
        success: function () {
          Swal.fire({
            title: "삭제완료",
            text: "일정이 삭제되었습니다.",
            icon: "success",
            confirmButtonText: "확인",
          }).then((result) => {
            if (result.isConfirmed) {
              // 성공시 리다이렉트
              window.location.href = "/schedules";
            }
          });
        },
        error: function (error) {
          Swal.fire("오류", "일정삭제에 실패하였습니다.");
          console.error("일정 삭제에 실패하였음 : " + error);
        },
      });
    }
  });
});

// 우클릭 삭제하기 구현
var rightButtonDelete = document.getElementById("right-button-for-delete");

rightButtonDelete.addEventListener("click", function (event) {
  event.preventDefault();
  // SweetAlert로 삭제 확인창 표시
  Swal.fire({
    title: "정말 삭제하시겠습니까?",
    html: "일정의 주최자가 일정을 삭제하면<br>초대된 사람의 일정에서도 삭제됩니다.<br><br>또한, 초대 받은 일정은 거절로 상태가 변경됩니다.",
    showCancelButton: true,
    confirmButtonColor: "#d33",
    cancelButtonColor: "#3085d6",
    confirmButtonText: "일정삭제",
    cancelButtonText: "닫기",
  }).then((result) => {
    if (result.isConfirmed) {
      $.ajax({
        url: `/api/schedules/${schduleIdForDelete}`,
        method: "DELETE",
        success: function () {
          // 삭제 성공 후 SweetAlert 표시
          Swal.fire({
            title: "삭제완료",
            text: "일정이 삭제되었습니다.",
            icon: "success",
            confirmButtonText: "확인",
          }).then((resultNext) => {
            // 사용자가 확인 버튼을 클릭하면 리다이렉트
            if (resultNext.isConfirmed) {
              window.location.href = "/schedules"; // 성공 시 리다이렉트
            }
          });
        },
        error: function (error) {
          Swal.fire("오류", "일정삭제에 실패하였습니다.", "error"); // 오류 메시지 표시
          console.error("일정 삭제에 실패하였음 : " + error);
        },
      });
    }
  });
});
