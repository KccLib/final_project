// var csrfHeaderName = "${_csrf.headerName}";
// var csrfTokenValue = "${_csrf.token}";

// $(document).ajaxSend(function (e, xhr) {
//   xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
// });

const closeButton = document.getElementById("find-password-close");
const returnInsertIdPageButton = document.getElementById(
  "find-password-email-backpage"
);
const doneFindPassword = document.getElementById("close-password-page");
var findPasswordEmail = "";

function closePage() {
  window.location.href = "/login";
}

function donePage() {
  window.location.href = "/find-password";
}

if (closeButton) {
  closeButton.addEventListener("click", (event) => {
    console.log("close find-password page");
    event.preventDefault();
    closePage();
  });
}

if (returnInsertIdPageButton) {
  returnInsertIdPageButton.addEventListener("click", function (event) {
    console.log("return page");
    event.preventDefault();
    donePage();
  });
}

if (doneFindPassword) {
  doneFindPassword.addEventListener("click", function (event) {
    event.preventDefault();
    closePage();
  });
}

function setFindPasswordEmailCookie(email) {
  const expiryDate = new Date();
  expiryDate.setDate(expiryDate.getDate() + 1); // 쿠키 유효 기간을 1일로 설정
  document.cookie = `findPasswordEmail=${email}; expires=${expiryDate.toUTCString()}; path=/`;
}

function getCookie(name) {
  const nameEquals = name + "=";
  const decodedCookie = decodeURIComponent(document.cookie);
  const cookieArray = decodedCookie.split(";");
  for (let i = 0; i < cookieArray.length; i++) {
    const cookie = cookieArray[i];
    while (cookie.charAt(0) === " ") {
      cookie = cookie.substring(1);
    }
    if (cookie.indexOf(nameEquals) === 0) {
      return cookie.substring(nameEquals.length, cookie.length);
    }
  }
  return null;
}

$("#find-password-button").click(function (event) {
  event.preventDefault();
  var employeeId = $("#employeeId").val();
  findPasswordEmail = employeeId;
  setFindPasswordEmailCookie(findPasswordEmail);
  if (employeeId === null || employeeId === "") {
    $("#checkedId").html(
      '<p class="text-danger" style="margin-top: 3px">아이디(이메일)을 입력해주세요.</p>'
    );
  } else {
    $("#checkedId").html(""); // 해당 영역 초기화
  }
  // // 이메일 값 확인을 위해 콘솔 출력
  // console.log("이메일 값: " + employeeId);
  if (employeeId) {
    $.ajax({
      url: "/api/find-password/id",
      type: "GET",
      data: { email: employeeId },
      success: function () {
        window.location.href = "/find-password/email";
      },
      error: function () {
        $("#checkedId").html(
          '<p class="text-danger" style="margin-top: 3px">일치하는 회원정보가 없습니다.</p>'
        );
      },
    });
  }
});

$("#find-password-email-button").click(function (event) {
  // console.log("임시 비밀번호 발급 버튼 ");
  event.preventDefault();
  var externalEmailVal = $("#externalEmail").val();
  const findPasswordEmail = getCookie("findPasswordEmail");

  if (externalEmailVal === null || externalEmailVal === "") {
    $("#checkedId").html(
      '<p class="text-danger" style="margin-top: 3px">아이디(이메일)을 입력해주세요.</p>'
    );
  } else {
    $("#checkedId").html("");
    Swal.fire({
      title: "확인",
      text: "해당 이메일로 임시 비밀번호를 발급 받으시겠습니까?",
      showCancelButton: true,
      confirmButtonText: "예",
      cancelButtonText: "아니요",
    }).then((result) => {
      console.log(
        "사내메일 : " + findPasswordEmail + "외부메일 : " + externalEmailVal
      );
      if (result.isConfirmed) {
        $.ajax({
          url: "/api/find-password/email",
          type: "POST",
          data: {
            email: findPasswordEmail,
            externalEmail: externalEmailVal,
          },
          success: function () {
            window.location.href = "/find-password/done";
          },
          error: function () {
            $("#checkedId").html(
              '<p class="text-danger" style="margin-top: 3px">등록하신 사외 이메일을 입력해주세요.</p>'
            );
          },
        });
      }
    });
  }
});
