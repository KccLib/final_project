var idleTime = 0;
var isAway = false;
setInterval(timerIncrement, 60000); // 1분마다 실행
var employeeInfo = 0;

statusModel.addEventListener("click", function (event) {
  const target = event.target.closest(".user-status-select");
  if (target) {
    console.log("사용자가 선택한 status" + target.dataset.status);
    const status = target.dataset.status;
    userStatusBox.innerHTML = "";
    isStatusOpen = !isStatusOpen;
    employeeInfo = status;
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

  if (employeeInfo !== 1) {
    return;
  }

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
    console.log("사용자 상태변경");
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

document.addEventListener('DOMContentLoaded', function() {
  var links = document.querySelectorAll('a');

  links.forEach(function(link) {
    link.addEventListener('click', function(event) {
      var href = link.getAttribute('href');

      // 외부 링크나 특정 조건에서는 제외
      if (href.startsWith('http') || href.startsWith('mailto:') || href === '#') {
        return;
      }

      // 로딩 스피너 표시
      document.getElementById('loading-spinner').style.display = 'flex';

      // 최소 로딩 시간 설정 (예: 500ms)
      var startTime = Date.now();

      // 페이지 이동을 약간 지연시킵니다.
      setTimeout(function() {
        var elapsedTime = Date.now() - startTime;
        var delay = elapsedTime < 500 ? 500 - elapsedTime : 0;

        setTimeout(function() {
          window.location.href = href;
        }, delay);
      }, 0);

      // 기본 링크 동작 방지
      event.preventDefault();
    });
  });
});

document.addEventListener('DOMContentLoaded', function() {
  var forms = document.querySelectorAll('form');

  forms.forEach(function(form) {
    form.addEventListener('submit', function(event) {
      // 로딩 스피너 표시
      document.getElementById('loading-spinner').style.display = 'flex';
    });
  });
});

window.addEventListener('load', function() {
  document.getElementById('loading-spinner').style.display = 'none';
});
