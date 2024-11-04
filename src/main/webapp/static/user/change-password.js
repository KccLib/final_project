const changeAcceptButton = document.getElementById("change-password-button");

/**
 * 기존 비밀번호 확인
 * 상위 이벤트로 선택
 */

const existingPassword = document.getElementById("existing-password");
let passwordCheck = false;
existingPassword.addEventListener("change", (e) => {
  const existingPasswordValue = e.target.value;

  // 기존 메시지 제거
  const existingMessage = document.getElementById("alram-password-check");
  if (existingMessage) {
    existingMessage.remove();
  }

  $.ajax({
    method: "POST",
    url: "/api/employees/detail/check",
    contentType: "application/json",
    data: JSON.stringify({ password: existingPasswordValue }),
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
      } else if (response.passwordCheck === 1) {
        messageElement.textContent = "비밀번호가 일치합니다.";
        messageElement.style.color = "#10B97B";
        passwordCheck = true;
      }

      // 메시지 추가
      existingPassword.appendChild(messageElement);
    },
    error: function (xhr, status, error) {
      console.log("비밀번호 조회에 실패하였습니다." + error);
    },
  });
});

/**
 * 새 비밀번호 validation
 */
const passwordRegex =
  /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,15}$/;
const newPasswordContainer = document.getElementById("change-password");
let newPasswordForCheck;
let newPasswordCheck = false;
newPasswordContainer.addEventListener("change", (e) => {
  const newPassword = e.target.value;
  newPasswordForCheck = newPassword;
  const newPasswordMessage = document.getElementById("new-password-check");
  if (newPasswordMessage) {
    newPasswordMessage.remove();
  }

  const messageElement = document.createElement("p");
  messageElement.id = "new-password-check";
  messageElement.style.marginTop = "5px";

  if (!passwordRegex.test(newPassword)) {
    messageElement.textContent = "비밀번호 형식이 일치하지 않습니다.";
    messageElement.style.color = "red";
    newPasswordCheck = false;
  } else if (passwordRegex.test(newPassword)) {
    messageElement.textContent = "비밀번호 형식이 일치합니다.";
    messageElement.style.color = "#10B97B";
    newPasswordCheck = true;
  }

  newPasswordContainer.appendChild(messageElement);
});

/**
 * 비밀번호 확인
 */
const newPasswordCorrect = document.getElementById("change-password-check");
let newPasswordCorrectCheck = false;
newPasswordCorrect.addEventListener("change", (e) => {
  const correctCheckPassword = e.target.value;

  const newPasswordCorrectMessage = document.getElementById(
    "new-password-correct-check",
  );
  if (newPasswordCorrectMessage) {
    newPasswordCorrectMessage.remove();
  }

  const messageElement = document.createElement("p");
  messageElement.id = "new-password-correct-check";
  messageElement.style.marginTop = "5px";

  if (correctCheckPassword === newPasswordForCheck) {
    messageElement.textContent = "비밀번호가 일치합니다.";
    messageElement.style.color = "#10B97B";
    newPasswordCorrectCheck = true;
  } else {
    messageElement.textContent = "입력된 비밀번호가 다릅니다.";
    messageElement.style.color = "red";
    newPasswordCorrectCheck = false;
  }

  newPasswordCorrect.appendChild(messageElement);
});

/**
 * 저장하기 버튼 활성화
 */
window.addEventListener("change", function () {
  if (newPasswordCorrectCheck && newPasswordCheck && passwordCheck) {
    changeAcceptButton.disabled = false;
  } else {
    changeAcceptButton.disabled = true;
  }
});

/**
 * 비밀번호 보기 버튼
 */

const firstPassword = document.getElementById("first-password");
const secondPassword = document.getElementById("second-password");
const thirdPassword = document.getElementById("third-password");
const firstInput = document.getElementById("existing-password-input");
const secondInput = document.getElementById("change-password-input");
const thirdInput = document.getElementById("change-password-check-input");

function togglePasswordVisibility(button, input) {
  button.addEventListener("click", () => {
    input.type = input.type === "password" ? "text" : "password";
  });
}

togglePasswordVisibility(firstPassword, firstInput);
togglePasswordVisibility(secondPassword, secondInput);
togglePasswordVisibility(thirdPassword, thirdInput);

/**
 * 저장하기
 */

/**
 * 닫기
 */

const closeButton = document.getElementById("close-change-password");

closeButton.addEventListener("click", function (e) {
  e.preventDefault();

  Swal.fire({
    title: "비밀번호 수정 창을 닫으시겠습니까?",
    text: "저장하지 않고 닫으면 변경사항이 모두 사라집니다.",
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#efefef",
    confirmButtonText: "닫기",
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
