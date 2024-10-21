const canvas = document.getElementById('canvas');
const screenVideo = document.getElementById('screenVideo');

//Gpt 요청 보내기
document.getElementById('imageForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    // const imageFileInput = document.getElementById('imageFile');
    // const imageFile = imageFileInput.files[0];

    const mediaStream = await navigator.mediaDevices.getDisplayMedia({video: true});
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

            fetch('http://54.180.154.212:8080/api/v1/gpt/chat', {
                method: 'POST',
                headers: {
                    'Authorization': `${jwtToken}`
                },
                body: formData
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById('responseContent').textContent = data;
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        } else {
            alert('Please select an image file.');
        }

        // 스트림 정지
        mediaStream.getTracks().forEach(track => track.stop());
    });




});
