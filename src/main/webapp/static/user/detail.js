const modifyEmployeeClose = document.getElementById("employee-detail-close");

modifyEmployeeClose.addEventListener("click", (e) => {
  e.preventDefault();
  window.history.back();
});
