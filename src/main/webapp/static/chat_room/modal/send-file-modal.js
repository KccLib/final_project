// var sendFileModalVisible = false
// var tagify2;

document.addEventListener('DOMContentLoaded', function() {
    var modal = document.getElementById("send-file-modal");
    var fileIcon = document.querySelector(".fa-file");

    fileIcon.addEventListener('click', function() {
        var rect = fileIcon.getBoundingClientRect();
        var modalWidth = modal.offsetWidth;
        var modalHeight = modal.offsetHeight;

        var leftPosition = window.scrollX + rect.left - modalWidth - 400;
        var topPosition = window.scrollY + rect.top - modalHeight - 360; // 모달 높이와 추가적인 간격을 빼서 위로 이동

        modal.style.top = topPosition + "px";
        modal.style.left = leftPosition + "px";
        modal.style.display = "block";
    });

});