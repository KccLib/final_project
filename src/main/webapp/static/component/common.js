var idleTime = 0;
var isAway = false;
var idleInterval = setInterval(timerIncrement, 60000); // 1분마다 실행

$(document).on('mousemove keypress mousedown scroll touchstart', function() {
    idleTime = 0;
    if (isAway) {
        isAway = false;
        updateUserStatus('ACTIVE'); // 상태가 '온라인'으로 변경될 때만 호출
    }
});


function timerIncrement() {
    idleTime++;
    if (idleTime >= 10 && !isAway) { // 10분 비활동 시
        isAway = true;
        updateUserStatus('ABSENT'); // 상태가 '자리비움'으로 변경될 때만 호출
    }
}

function updateUserStatus(status) {
    $.ajax({
        url: '/api/employees/status',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({status: status}),
        success: function(response) {
            console.log('상태 업데이트 성공:', status);
        },
        error: function(error) {
            console.log('상태 업데이트 실패:', error);
        }
    });
}