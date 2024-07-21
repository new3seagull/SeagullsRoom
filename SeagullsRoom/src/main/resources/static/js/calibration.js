document.addEventListener('DOMContentLoaded', async () => {

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


    await webgazer.setRegression('ridge').saveDataAcrossSessions(false).begin();
    // webgazer.showVideoPreview(false); //.applyKalmanFilter(false);
    webgazer.clearData();


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

            if (clicks >= maxClicks) {
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
        webgazer.end();
        location.href = 'main.html';
    });
});
