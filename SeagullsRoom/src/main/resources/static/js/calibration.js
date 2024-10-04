document.addEventListener('DOMContentLoaded', async () => {

    const canvas_plot = document.getElementById("plotting_canvas");
    const ctx_plot = canvas_plot.getContext("2d");

    function resizeCanvas() {
        canvas_plot.width = window.innerWidth;
        canvas_plot.height = window.innerHeight;
    }
    resizeCanvas();
    window.addEventListener("resize", resizeCanvas);


    function helpModalShow() {
        const helpModal = document.getElementById('helpModal');
        helpModal.style.display = 'block';

        const closeModal = document.querySelector('.modal .close');
        closeModal.addEventListener('click', () => {
            helpModal.style.display = 'none';
        });

        window.addEventListener('click', (event) => {
            if (event.target == helpModal) {
                helpModal.style.display = 'none';
            }
        });
    }

    helpModalShow();

    const eyetracker = getInstance();
    await eyetracker.begin();

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
                console.log('Gaze coordinates not available.');
            }
        }).catch(error => {
            console.error('Error predicting gaze:', error);
        });
    }
    setInterval(updateGazeCoordinates, 100); // 100ms마다 호출

    // document.addEventListener('click', async (event) => {
    //     await eyetracker.handleClick(event);
    // });
    // await webgazer.setRegression('ridge').saveDataAcrossSessions(false).begin();
    // webgazer.showVideoPreview(false); //.applyKalmanFilter(false);
    // webgazer.clearData();


    const container = document.getElementById('circle-container');
    const nextButton = document.getElementById('next-button');
    const restartButton = document.getElementById('restart-button');
    const totalCircles = 15; // 생성할 원의 개수
    const maxClicks = 3; // 원당 최대 클릭 횟수
    let circleCount = 0;
    let clickedCircles = 0;

    // 원을 생성하는 함수
    function createCircle() {
        const circle = document.createElement('div');
        circle.classList.add('circle');

        // 랜덤 위치를 설정합니다.
        const x = Math.random() * (container.clientWidth - 50);
        const y = Math.random() * (container.clientHeight - 50);
        circle.style.left = `${x}px`;
        circle.style.top = `${y}px`;

        // 클릭 이벤트를 추가합니다.
        let clicks = 0;
        circle.addEventListener('click', () => {
            clicks++;
            const newOpacity = 1 - (clicks / maxClicks); // 투명도 계산
            circle.style.opacity = newOpacity;

            if (clicks == maxClicks) {
                setTimeout(() => circle.style.display = 'none', 300); // 클릭 후 사라지기
                clickedCircles++;
                if (clickedCircles === totalCircles) {
                    nextButton.style.display = 'block';
                    restartButton.style.display = 'block';
                }
            }
        });

        container.appendChild(circle);
        circleCount++;
    }

    // 원을 생성합니다.
    for (let i = 0; i < totalCircles; i++) {
        createCircle();
    }

    restartButton.addEventListener('click', () => {
        nextButton.style.display = 'none';
        restartButton.style.display = 'none';
        clickedCircles = 0;

        for (let i = 0; i < totalCircles; i++) {
            createCircle();
        }
    });

    // 버튼 클릭 시 페이지 이동
    nextButton.addEventListener('click', () => {
        // webgazer.end();
        location.href = 'main.html';
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
});
