let currentNotificationId = 0;
let currentScheduleId = 0;

$(document).ready(function () {

    $(document).on('click', '.notification-list .notification-item', function() {

        $('.notification-item').removeClass('active');
        $(this).addClass('active');

        $('.default-contents').hide();
        $('.notification-contents').show();

        let notificationId = $(this).data('notification-id');
        let scheduleId = $(this).data('schedule-id');

        $('.notification-list').removeClass('active');
        $(this).addClass('active');
        currentNotificationId = notificationId;
        currentScheduleId = scheduleId;

        loadNotification(scheduleId);
    });

    $(document).on('click', '#detail-approve', function() {

        Swal.fire({
            title: "일정 수락",
            text: "일정에 참가하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            confirmButtonText: "참가",
            cancelButtonText: "취소",
            customClass: {
                cancelButton: 'cancel-button-custom' // 여기에 CSS 클래스를 지정
            }
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire({
                    title: "참가",
                    text: "참가 완료 되었습니다.",
                });
                // 2초 후 채팅방 삭제
                $.ajax({
                    url: '/api/schedules/' + currentScheduleId + '/approve',
                    method: 'POST',
                    contentType: 'application/json',
                    success: function (response) {
                        console.log('일정 승인 성공:', response);
                        loadNotification(currentScheduleId);
                    },
                    error: function (xhr, status, error) {
                        console.error('일정 승인에 실패했습니다:', error);
                    }
                });
            }
        })



    });

    $(document).on('click', '#detail-reject', function() {

        Swal.fire({
            title: "일정 거절",
            text: "일정에 불참하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: "#d33",
            confirmButtonText: "거절",
            cancelButtonText: "취소",
            customClass: {
                cancelButton: 'cancel-button-custom' // 여기에 CSS 클래스를 지정
            }
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire({
                    title: "거절",
                    text: "거절 완료 되었습니다.",
                });
                $.ajax({
                    url: '/api/schedules/' + currentScheduleId + '/reject',
                    method: 'POST',
                    contentType: 'application/json',
                    success: function (response) {
                        console.log('일정 거절 성공:', response);
                        loadNotification(currentScheduleId);
                    },
                    error: function (xhr, status, error) {
                        console.error('일정 거절에 실패했습니다:', error);
                    }
                });
            }
        })



    });



    updateDateFormat();
})

function updateDateFormat() {
    const chatDates = document.querySelectorAll(".notification-time p");
    const today = new Date().toISOString().slice(0, 10);

    chatDates.forEach(function (dateElement) {
        const fullDate = new Date(dateElement.textContent.trim());
        if (fullDate.toISOString().slice(0, 10) === today) {
            dateElement.textContent = fullDate.toLocaleTimeString('ko-KR', {
                hour: '2-digit',
                minute: '2-digit',
                hour12: false
            });
        } else {
            dateElement.textContent = fullDate.toLocaleDateString('ko-KR', {
                month: '2-digit',
                day: '2-digit'
            }).replace(/\.\s/g, '-').slice(0, -1);
        }
    });
}

function loadNotification(scheduleId) {
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

            $('#writer-detail').val(scheduleDetail.scheduleMaster.employeeName +
                " " +
                scheduleDetail.scheduleMaster.deptName);

            $('.detail-title').empty();
            $('.detail-title').append('<p>' + scheduleDetail.name + '</p>');

            var startDate = scheduleDetail.startedDt
            var endDate = scheduleDetail.endedDt

            // 시작일 포맷 조정
            if (startDate.includes("00:00") && endDate.includes("00:00")) {
                startDate = startDate.slice(0, 10); // 'yyyy-MM-dd' 형식으로 잘라냄
                endDate = endDate.slice(0, 10); // 'yyyy-MM-dd' 형식으로 잘라냄
                $('#start-date-detail').val(startDate);
                $('#end-date-detail').val(endDate);
            }

            $('#start-date-detail').val(startDate);
            $('#end-date-detail').val(endDate);

            // 사원 정보 출력
            // 테이블 헤더 생성
            var tableHTML = `
            <table class="table">
                <thead>
                  <tr>
                    <th scope="col">이름</th>
                    <th scope="col">부서</th>
                    <th scope="col">참석 여부</th>
                  </tr>
                </thead>
                <tbody>
        `;

            // 사원 정보 출력
            scheduleDetail.scheduleDetailEmployees.forEach(function (employee) {
                console.log("현재 회원의 상태 : " + employee.isParticipated);
                // 참석 여부에 따라 표시할 텍스트를 결정
                let participationStatus;
                const participationValue = parseInt(employee.isParticipated, 10); // 문자열을 숫자로 변환

                if (participationValue === 0) {
                    participationStatus = "<p class='waiting'>💬 대기 중</p>";
                } else if (participationValue === 1) {
                    participationStatus = "<p class='reject'>❌ 거절</p>";
                } else if (participationValue === 2) {
                    participationStatus = "<p class='approve'>✔ 승인</p>";
                } else {
                    participationStatus = "상태 불명"; // 예외 처리
                }
                tableHTML += `
                          <tr>
                              <td>${employee.employeeName}</td>
                              <td>${employee.deptName}</td>
                              <td>${participationStatus}</td>
                          </tr>
                      `;
            });

            // 테이블 닫기
            tableHTML += `
                              </tbody">
                          </table>
                      `;

            // 테이블을 add-people-table div에 추가
            document.getElementById("table-container").innerHTML = tableHTML;

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
                "quill로 파싱하기 전 내용 : " + JSON.stringify(quillDelta)
            ); // JSON 형태로 보기 좋게 출력
            console.log("quill로 파싱한 내용 : " + htmlContent); // 변환된 HTML 출력

            // 내용을 div에 삽입
            document.getElementById("detail-text-contents").innerHTML =
                htmlContent;

            const modifyButton = document.getElementById("detail-modify");
            const addSchedule = document.getElementById("add-schedule-container");

        },
        error: function (error) {
            console.error("일정 상세 정보를 서버로부터 받아올 수 없습니다.");
        },
    });
}