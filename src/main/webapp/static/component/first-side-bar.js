const currentPath = window.location.pathname;
console.log("Current Path:", currentPath);

// 모든 사이드바의 링크 요소를 선택
const links = document.querySelectorAll(".side-bar-menu-icon-with-name a");

links.forEach((link) => {
  // 각 링크의 href 값을 가져와서 출력
  const hrefValue = link.getAttribute("href");

  // 링크의 href 값이 현재 경로로 시작하면
  if (currentPath.startsWith(hrefValue)) {
    // 링크의 부모 div에 'active' 클래스를 추가
    link.parentElement.classList.add("active");
  }
});

const chatBotButton = document.getElementsByClassName("chat-bot")[0];
const chatBotContainer = document.getElementById("chat-bot-container");
const chatBotCloseButton = document.getElementById("chat-bot-close");
const chatBotListRemoveButton = document.getElementById("chat-bot-return");
let chatBotList = document.getElementById("chat-bot-messages");
const chatBotSubmitButton =
  document.getElementsByClassName("fa-location-arrow")[0];

chatBotButton.addEventListener("click", function () {
  chatBotContainer.classList.remove("hidden");
});

window.addEventListener("click", function (event) {
  if (event.target === chatBotContainer) {
    chatBotContainer.classList.add("hidden");
  }
});

chatBotCloseButton.addEventListener("click", function () {
  chatBotContainer.classList.add("hidden");
});

chatBotListRemoveButton.addEventListener("click", function () {
  chatBotList.innerHTML = ""; // 내용 지우기
});

//client 채팅 submit 3개 하기
const chatBotInput = document.getElementById("chat-bot-input");
chatBotSubmitButton.addEventListener("click", function () {
  submitChatBot();
});

chatBotInput.addEventListener("keydown", function (event) {
  if (event.key === "Enter") {
    event.preventDefault(); // 기본 동작 방지 (폼 제출 방지)
    submitChatBot();
  }
});

function submitChatBot() {
  //입력값 가져오고 있으면 채팅방으로 value비우기
  const chatBotUserMessage = chatBotInput.value;
  const sendClientMessage = chatBotUserMessage;
  if (chatBotUserMessage) {
    displayClientChatBotMessage(chatBotUserMessage);
    chatBotInput.value = "";
  }

  console.log("클라이언트에서 보내는 메세지 : " + sendClientMessage);
  const serverMessageElement = addServerMessageBox(); // 서버 메시지 박스 생성 및 반환값 저장

  const eventSource = new EventSource(
      `/api/chat-bot?clientMessage=${encodeURIComponent(sendClientMessage)}`
  );

  eventSource.onmessage = function (event) {
    // if (event.data) {
    //   console.log("받은 메시지:" + event.data); // 받은 메시지 확인
    //
    //   // 한 글자씩 응답을 받아서 채팅에 계속 추가
    //   addServerMessage(event.data, serverMessageElement);
    // }
    addServerMessage(event.data, serverMessageElement);

  };

  eventSource.onerror = function (event) {
    console.error("Error occurred", event);
    eventSource.close();
  };
}

function displayClientChatBotMessage(message) {
  const chatMessageElement = document.createElement("div");
  chatMessageElement.className = "chat-bot-client";
  chatMessageElement.innerHTML = `<p class="chat-bot-client-messages" >${message}</p>`;
  chatBotList.appendChild(chatMessageElement); // 메시지를 채팅방에 추가

  chatBotList.scrollTop = chatBotList.scrollHeight;
}

let chatMessageServerElement; // 서버 메시지 박스를 저장할 변수

//채팅방에 채팅 박스 먼저 생성
function addServerMessageBox() {
  const chatMessageServerElement = document.createElement("div");
  chatMessageServerElement.className = "chat-bot-server";
  chatMessageServerElement.innerHTML = `<p class="chat-bot-server-messages" >💬</p>`;
  chatBotList.appendChild(chatMessageServerElement); // 서버 메시지 박스를 채팅방에 추가
  return chatMessageServerElement; // 생성된 박스를 반환
}

//채팅박스에 내용채우기
function addServerMessage(message, serverMessageElement) {
  console.log(serverMessageElement); // serverMessageElement 확인

  const addChatMessage = serverMessageElement.querySelector(
    ".chat-bot-server-messages"
  );
  if (addChatMessage.innerText.includes("💬")) {
    addChatMessage.innerText = addChatMessage.innerText.replace("💬", ""); // "💬"를 빈 문자열로 대체
  }
  console.log("추가할 메시지:", message, "공백검사ㅌ"); // 추가할 메시지 로그

  // 공백을 검사하여 ""인 경우 " "로 대체
  if (message === "") {
    message = " ";
  }
  addChatMessage.textContent += message;  // 공백이 있는 그대로 추가
  chatBotList.scrollTop = chatBotList.scrollHeight;
}
