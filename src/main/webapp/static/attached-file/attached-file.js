$(document).ready(function() {
    // 현재 페이지의 오프셋과 로딩 상태를 관리하는 변수
    let offset = 0;
    let isLoading = false;
    let isEnd = false; // 모든 데이터를 불러왔는지 여부

    // 필터링에 사용될 변수
    let keyword = '';
    let extensions = '';
    let senderId = '';
    let startDate = '';
    let endDate = '';

    // 현재 진행 중인 AJAX 요청을 추적하기 위한 변수
    let currentRequest = null;

    // 디바운싱을 위한 타이머 변수
    let debounceTimer = null;
    let employeeDebounceTimer = null;

    // 초기 데이터 로드
    loadData();
    resetEmployeeDataAndLoad("");

    // 검색어 입력 필드의 키보드 입력 감지 (디바운싱 적용)
    $('input[name="search-item"]').on('keyup', function() {
        keyword = $(this).val();
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(function() {
            resetDataAndLoad();
        }, 100); // 300ms 후에 실행
    });

    // 드롭다운 메뉴 변경 감지
    // 확장자 드롭다운
    $('.dropdown-menu[aria-labelledby="dropdownMenuButton1"] .dropdown-item').on('click', function(e) {
        e.preventDefault();
        console.log("실행");
        extensions = $(this).data('extension');
        let extensionText = $(this).text().trim();
        // 선택된 i 태그를 가져와서 아이콘 클래스 변경
        let iconClass = $(this).find('i').attr('class');
        $('#dropdownMenuButton1 i').attr('class', iconClass);

        $('#dropdownMenuButton1 .filter-title').text(extensionText);
        resetDataAndLoad();
    });

    // 사람 드롭다운
    $(document).on('click', '.dropdown-menu[aria-labelledby="dropdownMenuButton2"] .dropdown-item', function(e) {
        e.preventDefault();
        senderId = $(this).data('sender-id'); // 각 항목에 data-sender-id 속성을 추가해야 합니다.
        let senderName = $(this).text().trim();
        $('#dropdownMenuButton2 .filter-title').text(senderName);
        resetDataAndLoad();
    });

    // 일자 드롭다운
    $('.dropdown-menu[aria-labelledby="dropdownMenuButton3"] .dropdown-item').on('click', function(e) {
        e.preventDefault();
        let dateOption = $(this).text().trim();
        $('#dropdownMenuButton3 .filter-title').text(dateOption);
        // dateOption에 따라 startDate와 endDate 설정
        let today = new Date();
        if (dateOption === '오늘') {
            startDate = formatDate(today);
            endDate = formatDate(today);
        } else if (dateOption === '지난 7일') {
            let pastDate = new Date();
            pastDate.setDate(today.getDate() - 7);
            startDate = formatDate(pastDate);
            endDate = formatDate(today);
        } else if (dateOption === '지난 30일') {
            let pastDate = new Date();
            pastDate.setDate(today.getDate() - 30);
            startDate = formatDate(pastDate);
            endDate = formatDate(today);
        } else if (dateOption === '올해') {
            startDate = today.getFullYear() + '-01-01';
            endDate = formatDate(today);
        } else {
            startDate = '';
            endDate = '';
        }
        resetDataAndLoad();
    });

    // 스크롤 감지하여 무한 스크롤 구현
    $(window).on('scroll', function() {
        if ($(window).scrollTop() + $(window).height() >= $(document).height() - 100) {
            if (!isLoading && !isEnd) {
                loadData();
            }
        }
    });

    // 데이터 로드 함수
    function loadData() {
        isLoading = true;

        // 진행 중인 AJAX 요청이 있으면 취소
        if (currentRequest != null) {
            currentRequest.abort();
        }

        currentRequest = $.ajax({
            url: '/api/attached-files',
            type: 'GET',
            data: {
                keyword: keyword,
                extensions: extensions,
                senderId: senderId,
                startDate: startDate,
                endDate: endDate,
                offset: offset
            },
            success: function(data) {
                if (offset === 0) {
                    // 처음 로드이거나 필터가 변경되어 데이터를 초기화한 경우
                    $('.file-table tbody').empty();
                }
                if (data.length > 0) {
                    appendDataToTable(data);
                    offset += data.length;
                } else {
                    isEnd = true; // 더 이상 데이터 없음
                }
            },
            error: function(xhr, status, error) {
                if (status !== 'abort') { // 요청이 취소된 경우가 아니면 에러 처리
                    console.error('데이터 로드 실패:', error);
                }
            },
            complete: function() {
                isLoading = false;
                currentRequest = null;
            }
        });
    }

    // 테이블에 데이터 추가
    function appendDataToTable(data) {
        let tbody = $('.file-table tbody');
        $.each(data, function(index, item) {
            let tags = '';
            if (item.tags && item.tags.length > 0) {
                $.each(item.tags, function(i, tag) {
                    tags += '<span class="tag">' + tag + '</span>';
                });
            }
            let row = `
                <tr>
                    <th style="width: 30%; margin-left: 10px;">
                    <div class="row align-items-center preview-trigger" style="height: 30px;">
                        <p class="file-name">
                            <i class="${addExtensionIcon(item)}"></i>
                            ${item.fileName}
                        </p>
                        </div>
                        <div class="file-preview" style="display: none; position: absolute; z-index: 1000;">
                          <!-- The preview content will be injected here -->
                        </div>
                    </th>
                    <td style="width: 15%;">
                        <div class="row align-items-center">
                            <img src="${item.senderProfileUrl}" />
                            &nbsp;${item.senderName}
                        </div>
                    </td>
                    <td style="width: 15%; margin-left: 10px;">
                    <div class="row align-items-center" style="height: 30px;">
                    <p class="chat-room-name">${item.chatRoomName}</p>
                    </div>
                    </td>
                    <td style="width: 10%;">
                    <div class="row align-items-center" style="height: 30px;">
                    ${item.writeDt}
                    </div>
                    </td>
                    <td style="width: 20%;">
                    <div class="row align-items-center" style="height: 30px;">
                        ${tags}
                        </div>
                    </td>
                    <td style="width: 10%;">
                    <div class="row align-items-center" style="height: 30px; margin-left: 10px;">
                    <i class="fa-solid fa-download" data-file-id="${item.fileId}"></i>
                    </div>
                    </td>
                </tr>
            `;
            tbody.append(row);
        });
    }

    // 데이터 초기화 및 새로 로드
    function resetDataAndLoad() {
        offset = 0;
        isEnd = false;
        // 진행 중인 AJAX 요청 취소
        if (currentRequest != null) {
            currentRequest.abort();
        }
        loadData();
    }

    // 날짜 형식 변환 함수 (YYYY-MM-DD)
    function formatDate(date) {
        let year = date.getFullYear();
        let month = ('0' + (1 + date.getMonth())).slice(-2);
        let day = ('0' + date.getDate()).slice(-2);

        return year + '-' + month + '-' + day;
    }

    function addExtensionIcon(item) {
        let iconClass = 'fa-solid fa-file'; // 기본 아이콘
        if (item.fileExtension) {
            let ext = item.fileExtension.toLowerCase();
            switch (ext) {
                case 'pdf':
                    iconClass = 'fa-solid fa-file-pdf';
                    break;
                case 'png':
                case 'jpg':
                case 'jpeg':
                case 'gif':
                    iconClass = 'fa-solid fa-file-image';
                    break;
                case 'csv':
                    iconClass = 'fa-solid fa-file-csv';
                    break;
                case 'doc':
                case 'docx':
                    iconClass = 'fa-solid fa-file-word';
                    break;
                case 'xls':
                case 'xlsx':
                    iconClass = 'fa-solid fa-file-excel';
                    break;
                case 'ppt':
                case 'pptx':
                    iconClass = 'fa-solid fa-file-powerpoint';
                    break;
                case 'zip':
                case 'rar':
                case '7z':
                    iconClass = 'fa-solid fa-file-zipper';
                    break;
                case 'mp4':
                case 'avi':
                case 'mkv':
                    iconClass = 'fa-solid fa-file-video';
                    break;
                default:
                    iconClass = 'fa-solid fa-file';
            }
        }
        return iconClass
    }

    $('#search-employee').on('keyup', function() {
        employeeKeyword = $(this).val();
        clearTimeout(employeeDebounceTimer);
        employeeDebounceTimer = setTimeout(function() {
            resetEmployeeDataAndLoad(employeeKeyword);
        }, 100); // 300ms 후에 실행
    });


    function resetEmployeeDataAndLoad(employeeKeyword) {
        $.ajax({
            url: '/api/search/employees',
            type: 'GET',
            data: {
                keyword: employeeKeyword,
            },
            success: function(data) {
                $('.search-people-list').empty();
                if (data.length > 0) {
                    appendEmployeeDataToTable(data);
                }
            },
            error: function(xhr, status, error) {
                if (status !== 'abort') { // 요청이 취소된 경우가 아니면 에러 처리
                    console.error('데이터 로드 실패:', error);
                }
            },
            complete: function() {
                isLoading = false;
                currentRequest = null;
            }
        });
    }

    function appendEmployeeDataToTable(data) {
        let tbody = $('.search-people-list');
        $.each(data, function(index, item) {
            let row = `
                <li>
                      <a class="dropdown-item" href="#" data-sender-id="${item.id}">
                        <div class="row align-items-center">
                          <img
                            src="${item.profileURL}"
                          />
                          &nbsp;${item.name}
                        </div>
                      </a>
                    </li>
                `
            tbody.append(row);
        });
    }

    $('#resetButton').on('click', function() {
        // 필터 변수 초기화
        keyword = '';
        extensions = '';
        senderId = '';
        startDate = '';
        endDate = '';

        // 입력 필드 초기화
        $('input[name="search-item"]').val('');

        // 드롭다운 메뉴 초기화
        // 확장자 드롭다운
        $('#dropdownMenuButton1 .filter-title').text('유형');
        $('#dropdownMenuButton1 i').attr('class', 'fa-regular fa-file filter-icon');

        // 사람 드롭다운
        $('#dropdownMenuButton2 .filter-title').text('사람');

        // 일자 드롭다운
        $('#dropdownMenuButton3 .filter-title').text('일자');

        // 데이터 다시 로드
        resetDataAndLoad();
    });

    $(document).on('click', '.fa-download', function() {
        let fileId = $(this).data('file-id');
        // 파일 다운로드를 위한 URL 설정
        $.ajax({
            url: `/api/chatrooms/image/${fileId}/download`,
            method: 'GET',
            xhrFields: {
                responseType: 'blob' // 바이너리 데이터 수신을 위해 필요합니다.
            },
            success: function(response, textStatus, xhr) {
                // 파일명 추출 (Content-Disposition 헤더에서 추출)
                var filename = "";
                var disposition = xhr.getResponseHeader('Content-Disposition');
                if (disposition && disposition.indexOf('attachment') !== -1) {
                    var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                    var matches = filenameRegex.exec(disposition);
                    if (matches != null && matches[1]) {
                        filename = decodeURIComponent(matches[1].replace(/['"]/g, ''));
                    }
                } else {
                    // 파일명이 없을 경우 기본값 설정
                    filename = "downloaded_file";
                }

                // Blob 객체 생성 후 다운로드
                var blob = new Blob([response], { type: 'application/octet-stream' });
                var link = document.createElement('a');
                link.href = window.URL.createObjectURL(blob);
                link.download = filename;
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            },
            error: function(xhr, status, error) {
                console.error('파일 다운로드에 실패했습니다:', error);
            }
        });
    });

    $(document).on('mouseenter', '.preview-trigger', function() {
        let fileId = $(this).closest('tr').find('.fa-download').data('file-id');
        let previewContainer = $(this).closest('th').find('.file-preview');
        let self = $(this); // Reference to the hovered element

        // Show loading indicator
        previewContainer.html('<p>Loading preview...</p>').show();

        // Fetch the file preview
        $.ajax({
            url: `/api/attached-files/${fileId}/preview`,
            method: 'GET',
            xhrFields: {
                responseType: 'blob'
            },
            success: function(blob, status, xhr) {
                let contentType = xhr.getResponseHeader('Content-Type');

                let contentHtml = '';
                if (contentType.startsWith('image/')) {
                    let url = URL.createObjectURL(blob);
                    contentHtml = `<img src="${url}" alt="Image Preview" style="max-width: 400px; max-height: 400px;" />`;
                } else if (contentType === 'application/pdf') {
                    let url = URL.createObjectURL(blob);
                    contentHtml = `<embed src="${url}" type="application/pdf" width="400px" height="400px" />`;
                } else {
                    contentHtml = '<p>No preview available for this file type.</p>';
                }

                // Set the content
                previewContainer.html(contentHtml);

                // Wait for the content to load
                previewContainer.find('img, embed').on('load', function() {
                    setPosition();
                });

                // For non-image/pdf content or if load event doesn't fire
                setTimeout(setPosition, 100);

                function setPosition() {
                    // Get the actual dimensions of the preview container
                    let previewWidth = previewContainer.outerWidth();
                    let previewHeight = previewContainer.outerHeight();

                    // Calculate positions
                    let offset = self.offset();
                    let leftPosition = offset.left + self.outerWidth();
                    let topPosition = offset.top - $(window).scrollTop();

                    // Adjust if the preview goes beyond the viewport width
                    let viewportWidth = $(window).width();
                    if (leftPosition + previewWidth > viewportWidth) {
                        leftPosition = offset.left - previewWidth;
                    }

                    // Adjust if the preview goes beyond the viewport height
                    let viewportHeight = $(window).height();
                    if (topPosition + previewHeight > viewportHeight) {
                        topPosition = viewportHeight - previewHeight;
                        if (topPosition < 0) {
                            topPosition = 0;
                        }
                    }

                    // Set the position
                    previewContainer.css({
                        'top': topPosition + 'px',
                        'left': leftPosition - 400 + 'px',
                        'position': 'fixed'
                    });
                }
            },
            error: function(xhr, status, error) {
                previewContainer.html('<p>Failed to load preview.</p>');
            }
        });
    });

    $(document).on('mouseleave', '.preview-trigger, .file-preview', function() {
        let previewContainer = $(this).closest('th').find('.file-preview');
        previewContainer.hide().empty();
    });

});

