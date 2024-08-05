let timerInterval;
let startTime;
let pausedTime = 0;
let isPaused = false;

const canvas = document.getElementById('canvas');
const screenVideo = document.getElementById('screenVideo');
let mediaStream;

function formatTime(ms){
    let totalSeconds = Math.floor(ms / 1000);
    let hours = Math.floor(totalSeconds / 3600);
    let minutes = Math.floor((totalSeconds % 3600) / 60);
    let seconds = totalSeconds % 60;

    return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
}

function startTimer() {
    startTime = Date.now() - pausedTime;
    timerInterval = setInterval(function() {
        const currentTime = Date.now();
        const elapsedTime = currentTime - startTime;
        document.getElementById('timer').innerText = formatTime(elapsedTime);
    }, 1000);

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

function appendMessage(sender, message, imageFile = null, isSent = false) {
    const chatMessages = document.getElementById('chatMessages');

    // 이미지 메시지 추가
    if (imageFile) {
        const imageContainer = document.createElement('div');
        imageContainer.classList.add('message-container', isSent ? 'sent' : 'received');

        const img = document.createElement('img');
        img.src = URL.createObjectURL(imageFile);
        img.alt = 'Image';
        imageContainer.appendChild(img);

        chatMessages.appendChild(imageContainer);
    }

    // 텍스트 메시지 추가
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

    chatMessages.scrollTop = chatMessages.scrollHeight; // 자동 스크롤
}

document.getElementById('startButton').addEventListener('click', async function() {
    mediaStream = await navigator.mediaDevices.getDisplayMedia({video: true});

    await webgazer.setRegression('ridge').saveDataAcrossSessions(true).begin();
    webgazer.showVideoPreview(false); //.applyKalmanFilter(false);
    // webgazer.clearData();

    startTimer();
    gazerInterval = setInterval(async function() {
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
        console.log("2_3");
        const context = canvas.getContext('2d');
        context.drawImage(screenVideo, startX, startY, captureWidth, captureHeight, 0, 0, captureWidth, captureHeight);

        canvas.toBlob((blob) => {
            // blob을 파일 객체로 변환
            const imageFile = new File([blob], 'screenshot.png', { type: 'image/png' });




            // API 요청에 파일을 포함시켜 전송
            if (imageFile) {
                const formData = new FormData();
                formData.append('image', imageFile);
                const jwtToken = localStorage.getItem('jwtToken');
                appendMessage('클라이언트', "위 이미지가 공부와 관련이 있으면 1을 없으면 0을 출력해 줘", imageFile, true); // gpt에 요청을 할 내용 챗봇에 추가
                fetch('http://localhost:8080/api/v1/gpt/chat', {
                    method: 'POST',
                    headers: {
                        'Authorization': `${jwtToken}`
                    },
                    body: formData
                })
                    .then(response => response.text())
                    .then(data => {
                        // document.getElementById('responseContent').textContent = data;
                        console.log(data + " data")
                        controlTimer(data);
                        appendMessage('GPT4-o', "gpt의 결과: " + data); //gpt의 응답
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


function controlTimer(data){
    if(data == 0 && !isPaused) {
        console.log('stop');
        pauseTimer()
    }else if(data == 1 && isPaused){
        console.log('start');
        startTimer();
    }
}

document.getElementById('pauseButton').addEventListener('click', function() {
    // if (isPaused) {
    //     startTimer();
    // } else {
    //     pauseTimer();
    // }
});

document.getElementById('stopButton').addEventListener('click', function() {
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

    // fetch('http://localhost:8080/api/v1/studies', {
    //     method: 'GET',
    //     headers: {
    //         'Authorization': localStorage.getItem('jwtToken'),
    //     },
    // })
    //     .then(response => {
    //         if (!response.ok) {
    //             throw new Error('Network response was not ok ' + response.statusText);
    //         }
    //         return response.json(); // 응답을 JSON으로 변환
    //     })
    //     .then(data => {
    //         // JSON 데이터가 배열 형태로 들어옴
    //         data.forEach(item => {
    //             console.log(`ID: ${item.id}`);
    //             console.log(`User Email: ${item.userEmail}`);
    //             console.log(`Study Time: ${item.studyTime}`);
    //             console.log(`Created At: ${item.createdAt}`);
    //             console.log(`Updated At: ${item.updatedAt}`);
    //             console.log('---');
    //         });
    //     })
    //     .catch(error => {
    //         console.error('There was a problem with the fetch operation:', error);
    //     });


    location.href = '../calibration.html';
});

