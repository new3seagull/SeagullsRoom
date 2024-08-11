let timerInterval;
let startTime;
let pausedTime = 0;
let isPaused = false;
let gazerInterval;

const canvas = document.getElementById('canvas');
const screenVideo = document.getElementById('screenVideo');
let mediaStream;

function formatTime(ms) {
    let totalSeconds = Math.floor(ms / 1000);
    let hours = Math.floor(totalSeconds / 3600);
    let minutes = Math.floor((totalSeconds % 3600) / 60);
    let seconds = totalSeconds % 60;

    return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
}

function startTimer() {
    startTime = Date.now() - pausedTime;
    timerInterval = setInterval(function () {
        const currentTime = Date.now();
        const elapsedTime = currentTime - startTime;
        document.getElementById('timer').innerText = formatTime(elapsedTime);
    }, 1000);

    Notification.requestPermission();
    document.getElementById('startButton').style.display = 'none';
    document.getElementById('pauseButton').style.display = 'inline-block';
    document.getElementById('pauseButton').innerText = '일시정지';
    document.getElementById('stopButton').style.display = 'inline-block';
    isPaused = false;
}

function pauseTimer() {
    clearInterval(timerInterval);
    pausedTime = Date.now() - startTime;
    document.getElementById('pauseButton').innerText = '재시작';
    isPaused = true;
}

function controlTimer(data) {
    if (data == 0 && !isPaused) {
        console.log('stop');
        pauseTimer();

        var img = "/images/logo.png";
        var text = '공부에 집중하세요. 타이머가 정지됩니다.';
        var notification = new Notification("SeagullsRoom", { body: text, icon: img });
        setTimeout(notification.close.bind(notification), 4000);
    } else if (data == 1 && isPaused) {
        console.log('start');
        startTimer();
    }
}

function appendMessage(sender, message, imageFile = null, isSent = false) {
    const chatMessages = document.getElementById('chatMessages');

    if (imageFile) {
        const imageContainer = document.createElement('div');
        imageContainer.classList.add('message-container', isSent ? 'sent' : 'received');

        const img = document.createElement('img');
        img.src = URL.createObjectURL(imageFile);
        img.alt = 'Image';
        imageContainer.classList.add('capture');
        imageContainer.appendChild(img);

        chatMessages.appendChild(imageContainer);
    }

    if (message) {
        const messageContainer = document.createElement('div');
        messageContainer.classList.add('message-container', isSent ? 'sent' : 'received');

        const messageElement = document.createElement('div');
        messageElement.classList.add('message');

        const senderElement = document.createElement('div');
        senderElement.classList.add('sender');
        senderElement.textContent = `${sender}`;
        messageElement.appendChild(senderElement);

        const messageContent = document.createElement('div');
        messageContent.classList.add('text');
        messageContent.textContent = message;
        messageElement.appendChild(messageContent);

        messageContainer.appendChild(messageElement);
        chatMessages.appendChild(messageContainer);
    }

    chatMessages.scrollTop = chatMessages.scrollHeight;
}

document.getElementById('startButton').addEventListener('click', async function () {
    mediaStream = await navigator.mediaDevices.getDisplayMedia({video: true});

    await webgazer.setRegression('ridge').saveDataAcrossSessions(true).begin();
    webgazer.showVideoPreview(false);

    startTimer();
    gazerInterval = setInterval(async function () {
        screenVideo.srcObject = mediaStream;
        await new Promise((resolve) => {
            screenVideo.onloadedmetadata = () => {
                resolve();
            };
        });

        var startX = 0;
        var startY = 0;
        const captureWidth = 700;
        const captureHeight = 500;

        await webgazer.getCurrentPrediction().then(prediction => {
            if (prediction) {
                startX = prediction.x;
                startY = prediction.y;
                console.log(`Current Prediction - X: ${startX}, Y: ${startY}`);
            }
        });

        const context = canvas.getContext('2d');
        context.drawImage(screenVideo, startX, startY, captureWidth, captureHeight, 0, 0, captureWidth, captureHeight);

        canvas.toBlob((blob) => {
            const imageFile = new File([blob], 'screenshot.png', {type: 'image/png'});

            if (imageFile) {
                const formData = new FormData();
                formData.append('image', imageFile);
                const jwtToken = localStorage.getItem('jwtToken');
                appendMessage('클라이언트', "위 이미지가 공부와 관련이 있으면 1을 없으면 0을 출력해 줘", imageFile, true);
                fetch('http://localhost:8080/api/v1/gpt/chat', {
                    method: 'POST',
                    headers: {
                        'Authorization': `${jwtToken}`
                    },
                    body: formData
                })
                .then(response => response.text())
                .then(data => {
                    console.log(data + " data");
                    controlTimer(data);
                    appendMessage('GPT4-o: ', data);
                })
                .catch(error => {
                    console.error('Error:', error);
                });
            } else {
                alert('Please select an image file.');
            }
        });
    }, 5000);
});

document.getElementById('pauseButton').addEventListener('click', function() {
    if (isPaused) {
        startTimer();
    } else {
        pauseTimer();
    }
});

document.getElementById('stopButton').addEventListener('click', function () {
    clearInterval(timerInterval);
    clearInterval(gazerInterval);
    webgazer.end();
    mediaStream.getTracks().forEach(track => track.stop());

    fetch('http://localhost:8080/api/v1/studies', {
        method: 'POST',
        headers: {
            'Authorization': localStorage.getItem('jwtToken'),
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            "studyTime": document.getElementById('timer').innerText
        })
    })
    .then(response => response.json())
    .then(data => console.log(data))
    .catch(error => console.error('Error:', error));

    document.getElementById('timer').innerText = '00:00:00';
    pausedTime = 0;
    isPaused = false;

    document.getElementById('startButton').style.display = 'inline-block';
    document.getElementById('pauseButton').style.display = 'none';
    document.getElementById('stopButton').style.display = 'none';
});

document.getElementById('calibrationButton').addEventListener('click', function() {
    location.href = '../calibration.html';
});