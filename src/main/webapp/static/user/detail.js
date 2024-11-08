const modifyEmployeeClose = document.getElementById("employee-detail-close");
const intoPasswordChange = document.getElementById("password-modify");

modifyEmployeeClose.addEventListener("click", (e) => {
  e.preventDefault();

  Swal.fire({
    title: "회원 수정 창을 닫으시겠습니까?",
    text: "저장하지 않고 닫으면 변경사항이 모두 사라집니다.",
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#efefef",
    confirmButtonText: "변경사항 폐기 후 닫기",
    cancelButtonText: "돌아가기",
    customClass: {
      confirmButton: "custom-confirm-button",
      cancelButton: "custom-cancel-button",
    },
  }).then((result) => {
    if (result.isConfirmed) {
      window.history.back();
    }
  });
});

intoPasswordChange.addEventListener("click", (e) => {
  e.preventDefault();
  window.location.href = "/employees/change-password";
});

/**
 * validation
 * @type {HTMLElement}
 */

//메일검증
const detailEmailContainer = document.getElementById(
  "external-email-container",
);
let mailCheck = true;
detailEmailContainer.addEventListener("change", (e) => {
  const alarmEmail = document.getElementById("alarm-email");
  if (alarmEmail) {
    alarmEmail.remove();
  }

  const email = e.target.value;
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(email)) {
    detailEmailContainer.innerHTML += `<p id="alarm-email" style="margin-top: 5px; color: red">올바른 이메일 주소를 입력하세요.</p>`;
    mailCheck = false;
  } else {
    mailCheck = true;
  }
});

const detailNumber = document.getElementById("detail-phone-number");
const detailNumberContainer = document.getElementById("phone-number-container");
let numberCheck = true;
// 전화번호 검증
detailNumberContainer.addEventListener("change", (e) => {
  const alarmPhone = document.getElementById("alarm-phone");
  if (alarmPhone) {
    alarmPhone.remove();
  }

  const phoneNumber = e.target.value;
  const phoneRegex = /^\d{3}-\d{3,4}-\d{4}$/;
  if (!phoneRegex.test(phoneNumber)) {
    detailNumberContainer.innerHTML += `<p id="alarm-phone" style="margin-top: 5px; color: red">전화번호 형식이 올바르지 않습니다. 예: 010-1234-5678</p>`;
    numberCheck = false;
  } else {
    numberCheck = true;
  }
});

// 팩스번호 검증
const faxNumber = document.getElementById("detail-fax-number");
const detailFaxContainer = document.getElementById("fax-number-container");
let faxCheck = true;

detailFaxContainer.addEventListener("change", (e) => {
  const alarmFax = document.getElementById("alarm-fax");
  if (alarmFax) {
    alarmFax.remove();
  }

  const faxNumber = e.target.value;
  const faxRegex = /^\d{2,3}-\d{3,4}-\d{4}$/;
  if (!faxRegex.test(faxNumber)) {
    detailFaxContainer.innerHTML += `<p id="alarm-fax" style="margin-top: 5px; color: red">팩스번호 형식이 올바르지 않습니다. 예: 02-1234-5678</p>`;
    faxCheck = false;
  } else {
    faxCheck = true;
  }
});

//비밀번호 확인
const passwordCheckValue = document.getElementById("detail-password-check");
const passwordCheckContainer = document.getElementById(
  "password-check-container",
);
let passwordCheck = false;
//수정버튼 활성화를 위해
const modifyApply = document.getElementById("modify-apply");

passwordCheckContainer.addEventListener("change", (e) => {
  var password = e.target.value;
  console.log("서버로 보내지는 password + " + password);

  // 기존 메시지 제거
  const existingMessage = document.getElementById("alram-password-check");
  if (existingMessage) {
    existingMessage.remove();
  }

  $.ajax({
    method: "POST",
    url: "/api/employees/detail/check",
    contentType: "application/json",
    data: JSON.stringify({ password: password }),
    dataType: "json",
    success: function (response) {
      // 메시지 추가를 위한 p 요소 생성
      const messageElement = document.createElement("p");
      messageElement.id = "alram-password-check";
      messageElement.style.marginTop = "5px";

      if (response.passwordCheck === 2) {
        messageElement.textContent = "비밀번호가 일치하지 않습니다.";
        messageElement.style.color = "red";
        passwordCheck = false;
        modifyApply.disabled = true;
      } else if (response.passwordCheck === 1) {
        messageElement.textContent = "비밀번호가 일치합니다.";
        messageElement.style.color = "#10B97B";
        passwordCheck = true;
        modifyApply.disabled = false;
      }

      // 메시지 추가
      passwordCheckContainer.appendChild(messageElement);
    },
    error: function (xhr, status, error) {
      console.log("비밀번호 조회에 실패하였습니다." + error);
    },
  });
});
/**
 * 수정 submit
 */
modifyApply.addEventListener("click", (e) => {
  e.preventDefault();

  Swal.fire({
    title: "회원 수정",
    text: "변경사항을 저장할까요?",
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#efefef",
    confirmButtonText: "저장",
    cancelButtonText: "취소",
    customClass: {
      confirmButton: "custom-confirm-button",
      cancelButton: "custom-cancel-button",
    },
  }).then((result) => {
    if (result.isConfirmed) {
      // 폼 데이터를 수집
      const employeeInfo = {
        location: document.getElementById("detail-locate").value,
        externalEmail: document.getElementById("detail-external-email").value,
        phoneNum: document.getElementById("detail-phone-number").value,
        fax: document.getElementById("detail-fax-number").value,
      };

      $.ajax({
        url: "/employees/modify",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(employeeInfo),
        success: function (response) {
          Swal.fire(response).then(() => {
            window.location.href = "/notifications";
          });
        },
        error: function (xhr, status, error) {
          // 에러 처리
          console.log("수정 실패:", error);
        },
      });
    }
  });
});

/**
 * 프로필 사진 변경
 *
 */
const detailProfileImg = document.getElementById("detail-profile-img");
const detailFileInput = document.getElementById("detail-fileInput");

//하나만 선택되도록 변경
detailFileInput.removeAttribute("multiple");

detailProfileImg.addEventListener("click", function (event) {
  console.log("프로필 이미지 변경 ");
  Swal.fire({
    title: "프로필 사진 변경",
    text: "프로필 사진을 변경하시겠습니까?",
    showCancelButton: true,
    confirmButtonColor: "#2993ff",
    cancelButtonColor: "#9a9a9a",
    confirmButtonText: "변경",
    cancelButtonText: "취소",
  }).then((resultDetail) => {
    if (resultDetail.isConfirmed) {
      detailFileInput.click(); //
      const formData = new FormData();
      // 파일이 선택되면 프로필 이미지 변경
      detailFileInput.addEventListener("change", function () {
        const file = detailFileInput.files[0]; // 선택된 파일

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
                Swal.fire("변경이 완료되었습니다.").then(() => {
                  window.location.href = "/employees/detail";
                });
              },
              error: function (xhr, status, error) {
                console.log("파일 업로드 실패 + " + error);
              },
            });
          } else {
            Swal.fire("이미지 파일만 선택 가능합니다.");
            detailFileInput.value = ""; // 유효하지 않은 파일 선택 시 초기화
          }
        }
      });
    }
  });
});

/**
 * password 보기 안보기
 */

// const passwordInput = document.getElementById("detail-password-check");
// const toggleButton = document.getElementById("watch-password");
//
// toggleButton.addEventListener("click", () => {
//   if (passwordInput.type === "password") {
//     passwordInput.type = "text";
//     toggleButton.textContent = "숨기기";
//   } else {
//     passwordInput.type = "password";
//     toggleButton.textContent = "보기";
//   }
// });
