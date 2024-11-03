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
