
    // Function to make a GET request to the server
    function fetchUserName() {
    // Get the JWT token from localStorage
    const token = localStorage.getItem('jwtToken');

    fetch('http://localhost:8080', {
    method: 'GET',
    headers: {
    'Authorization': `${token}`, // Add the JWT token to the header
    'Content-Type': 'application/json'
}
})
    .then(response => response.json())
    .then(response => {
    // Assuming the response JSON has a field named 'name'
    const userName = response.data;
    // Display the greeting message
    // document.getElementById('greeting').innerText = `안녕하세요 ${userName} 님`;
        console.log(userName);
})
    .catch(error => console.error('Error fetching user name:', error));
}


    window.onload = fetchUserName;


let timerInterval;
let startTime;
let pausedTime = 0;
let isPaused = false;

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

function controlTimer(startX){
    if(startX >= 800 && !isPaused) {
        pauseTimer()
    }else if(startX < 800 && isPaused){
        startTimer();
    }
}

document.getElementById('startButton').addEventListener('click', async function() {
    await webgazer.setRegression('ridge').saveDataAcrossSessions(true).begin();
    webgazer.showVideoPreview(false); //.applyKalmanFilter(false);
    // webgazer.clearData();

    startTimer();

    gazerInterval = setInterval(function() {
        webgazer.getCurrentPrediction().then(prediction => {
            if (prediction) {
                startX = prediction.x;
                startY = prediction.y;
                console.log(`Current Prediction - X: ${startX}, Y: ${startY}`);

                controlTimer(startX);
            }
        });
    }, 3000);
});

document.getElementById('pauseButton').addEventListener('click', function() {
    if (isPaused) {
        startTimer();
    } else {
        pauseTimer();
    }
});

document.getElementById('stopButton').addEventListener('click', function() {
    clearInterval(timerInterval);
    clearInterval(gazerInterval);



    document.getElementById('timer').innerText = '00:00:00';
    pausedTime = 0;
    isPaused = false;

    document.getElementById('startButton').style.display = 'inline-block';
    document.getElementById('pauseButton').style.display = 'none';
    document.getElementById('stopButton').style.display = 'none';
});


document.getElementById('calibrationButton').addEventListener('click', function() {



    // fetch('http://localhost:8080/api/v1/studies', {
    //     method: 'GET'
    // }).then(response => {
    //     if(response.ok){
    //         console.log(response.id)
    //     }
    // })

    location.href = '../calibration.html';
});