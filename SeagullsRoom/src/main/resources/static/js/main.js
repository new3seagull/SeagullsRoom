let timerInterval;
let startTime;
let pausedTime = 0;
let isPaused = false;
let gazerInterval;

const canvas = document.getElementById('canvas');
const screenVideo = document.getElementById('screenVideo');
let mediaStream;

const canvas_plot = document.getElementById("plotting_canvas");
const ctx_plot = canvas_plot.getContext("2d");

function resizeCanvas() {
    canvas_plot.width = window.innerWidth;
    canvas_plot.height = window.innerHeight;
}
resizeCanvas();
window.addEventListener("resize", resizeCanvas);

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
    // document.getElementById('pauseButton').style.display = 'inline-block';
    // document.getElementById('pauseButton').innerText = '일시정지';
    document.getElementById('stopButton').style.display = 'inline-block';
    isPaused = false;
}

function pauseTimer() {
    clearInterval(timerInterval);
    pausedTime = Date.now() - startTime;
    // document.getElementById('pauseButton').innerText = '재시작';
    isPaused = true;
}

function controlTimer(data) {
    const stopCategories = ["GAME", "SNS", "OTHER"];

    if (stopCategories.includes(data)) {
        if (!isPaused) {
            pauseTimer();
            var img = "/images/logo.png";
            var text = data + '공부에 집중하세요. 타이머가 정지됩니다.';
            var notification = new Notification("SeagullsRoom", { body: text, icon: img });
            setTimeout(notification.close.bind(notification), 4000);
        }
    } else if (isPaused) {
        startTimer();
        var img = "/image/images.jpg";
        var text = data + ' 타이머가 재시작됩니다.';
        var notification = new Notification("SeagullsRoom", { body: text, icon: img });
        setTimeout(notification.close.bind(notification), 4000);
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
    document.getElementById('timer').innerText = '00:00:00';
    pausedTime = 0;

    const categories = JSON.parse(localStorage.getItem('categories')) || [];

    if (categories.length < 3) {
        alert('공부를 시작하려면 최소 3개의 카테고리를 추가해야 합니다.');
        return;
    }

    mediaStream = await navigator.mediaDevices.getDisplayMedia({video: true});

    // await webgazer.setRegression('ridge').saveDataAcrossSessions(true).begin();
    // webgazer.showVideoPreview(false);
    const eyetracker = getInstance();
    await eyetracker.begin();
    // document.addEventListener('click', async (event) => {
    //     await eyetracker.handleClick(event);
    // });

    startTimer();

    function updateGazeCoordinates() {
        eyetracker.predict().then(gazeCoordinates => {
            if (gazeCoordinates) {
                // console.log('Predicted gaze coordinates:', gazeCoordinates);
                bound(gazeCoordinates);
                gazeWindow.add(gazeCoordinates.x, gazeCoordinates.y)
                const avgCoordinates = gazeWindow.getAverage();
                if (avgCoordinates){
                    drawCoordinates("red", gazeCoordinates.x, gazeCoordinates.y);
                }


            } else {
                controlTimer("None");
                console.log('Gaze coordinates not available.');
            }
        }).catch(error => {
            console.error('Error predicting gaze:', error);
        });
    }
    gazeDrawer = setInterval(updateGazeCoordinates, 100); // 100ms마다 호출


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

        await eyetracker.predict().then(prediction => {
            if (prediction) {
                startX = prediction.x;
                startY = prediction.y;
                console.log(`Current Prediction - X: ${startX}, Y: ${startY}`);
            }
        });

        // await webgazer.getCurrentPrediction().then(prediction => {
        //     if (prediction) {
        //         startX = prediction.x;
        //         startY = prediction.y;
        //         console.log(`Current Prediction - X: ${startX}, Y: ${startY}`);
        //     }
        // });

        const context = canvas.getContext('2d');
        context.drawImage(screenVideo, startX, startY, captureWidth, captureHeight, 0, 0, captureWidth, captureHeight);

        canvas.toBlob((blob) => {
            const imageFile = new File([blob], 'screenshot.png', {type: 'image/png'});

            if (imageFile) {
                const formData = new FormData();
                formData.append('image', imageFile);
                // 사용자 정의 카테고리 추가
                formData.append('userCategories', JSON.stringify(categories));
                const jwtToken = localStorage.getItem('jwtToken');
                appendMessage('클라이언트', "주어진 이미지가 어떤 카테고리에 해당하는지 반환", imageFile, true);
                fetch('http://54.180.154.212:8080/api/v1/gpt/chat', {
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
                    // LocalStorage에 카테고리 카운트 증가
                    updateCategoryCount(data.trim());
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

// 스크린타임 카테고리 카운트를 업데이트하는 함수
function updateCategoryCount(category) {
    let screenTimeData = JSON.parse(localStorage.getItem('screenTimeData') || '{}');
    screenTimeData[category] = (screenTimeData[category] || 0) + 1;
    localStorage.setItem('screenTimeData', JSON.stringify(screenTimeData));
    console.log(`${category} count: ${screenTimeData[category]}`);
}

// document.getElementById('pauseButton').addEventListener('click', function() {
//     if (isPaused) {
//         startTimer();
//     } else {
//         pauseTimer();
//     }
// });

document.getElementById('stopButton').addEventListener('click', function () {
    clearInterval(timerInterval);
    clearInterval(gazerInterval);
    clearInterval(gazeDrawer);
    ctx_plot.clearRect(0, 0, canvas_plot.width, canvas_plot.height); // 이전 원 지우기

    eyetracker.end();
    // webgazer.end();
    mediaStream.getTracks().forEach(track => track.stop());

    // 공부 시간 전송
    fetch('http://54.180.154.212:8080/api/v1/studies', {
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
    .then(data => console.log('Study time sent:', data))
    .catch(error => console.error('Error sending study time:', error));

    // 스크린타임 데이터 전송 및 초기화
    const screenTimeData = JSON.parse(localStorage.getItem('screenTimeData') || '{}');

    const promises = Object.entries(screenTimeData).map(([category, count]) => {
        return fetch('http://54.180.154.212:8080/api/v1/screenTime', {
            method: 'POST',
            headers: {
                'Authorization': localStorage.getItem('jwtToken'),
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                category: category,
                count: count
            })
        })
        .then(response => response.json())
        .then(data => {
            console.log(`Screen time for ${category} sent:`, data);
            return data;
        })
        .catch(error => {
            console.error(`Error sending screen time for ${category}:`, error);
            throw error;
        });
    });

    Promise.all(promises)
    .then(() => {
        console.log('All screen time data sent successfully');
        // localStorage의 스크린타임 데이터 초기화
        localStorage.setItem('screenTimeData', JSON.stringify({}));
    })
    .catch(error => {
        console.error('Error sending some screen time data:', error);
    });

    document.getElementById('startButton').style.display = 'inline-block';
    // document.getElementById('pauseButton').style.display = 'none';
    document.getElementById('stopButton').style.display = 'none';
});

document.getElementById('calibrationButton').addEventListener('click', function() {
    location.href = '../calibration.html';
});

function drawCoordinates(colour, x, y) {
    ctx_plot.clearRect(0, 0, canvas_plot.width, canvas_plot.height); // 이전 원 지우기
    ctx_plot.fillStyle = colour;
    ctx_plot.beginPath();
    ctx_plot.arc(x, y, 10, 0, Math.PI * 2, true); // 반지름 10짜리 원 그리기
    ctx_plot.fill();
}

function bound(prediction){
    if(prediction.x < 0)
        prediction.x = 0;
    if(prediction.y < 0)
        prediction.y = 0;
    var w = Math.max(document.documentElement.clientWidth, window.innerWidth || 0);
    var h = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);
    if(prediction.x > w){
        prediction.x = w;
    }

    if(prediction.y > h)
    {
        prediction.y = h;
    }
    return prediction;
}


class WindowSlice {
    constructor(size)
    {
        this.size = size;
        this.data = [];
    }

    add(x, y) {
        if (this.data.length >= this.size) {
            this.data.shift();
        }
        this.data.push({x, y});
    }

    getAverage() {
        if (this.data.length === 0){
            return null;
        }

        const sum = this.data.reduce((acc, val) => {
            acc.x += val.x;
            acc.y += val.y;
            return acc;
        }, {x: 0, y: 0});

        return {
            x: sum.x / this.data.length,
            y: sum.y / this.data.length
        };
    }
}

const gazeWindow = new WindowSlice(4);
