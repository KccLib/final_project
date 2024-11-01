var idleTime = 0;
var isAway = false;
setInterval(timerIncrement, 60000); // 1분마다 실행

statusModel.addEventListener("click", function (event) {
  const target = event.target.closest(".user-status-select");
  if (target) {
    // console.log("사용자가 선택한 status" + target.dataset.status);
    const status = target.dataset.status;

    userStatusBox.innerHTML = "";
    isStatusOpen = !isStatusOpen;
    $.ajax({
      method: "PUT",
      url: "/api/employees/status",
      dataType: "json",
      data: {
        status: status,
      },
      success: function (employeeInfo) {
        console.log("사용자 상태변경 성공");
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
        console.log("사용자 상태변경 실패 : " + error);
      },
    });
  }

  statusModel.classList.add("hidden");
});

$(document).on("mousemove keypress mousedown scroll touchstart", function () {
  // console.log("타이머 작동");
  idleTime = 0;
  if (isAway) {
    isAway = false;
    updateUserStatus("ACTIVE"); // 상태가 '온라인'으로 변경될 때만 호출
  }
});

function timerIncrement() {
  idleTime++;
  if (idleTime >= 30 && !isAway) {
    // 10분 비활동 시
    isAway = true;
    updateUserStatus("ABSENT"); // 상태가 '자리비움'으로 변경될 때만 호출
  }
}

function updateUserStatus(status) {
  $.ajax({
    url: "/api/employees/status",
    type: "POST",
    contentType: "application/json",
    data: JSON.stringify({ status: status }),
    success: function (response) {
      console.log("상태 업데이트 성공:", status);
    },
    error: function (error) {
      console.log("상태 업데이트 실패:", error);
    },
  });
}
