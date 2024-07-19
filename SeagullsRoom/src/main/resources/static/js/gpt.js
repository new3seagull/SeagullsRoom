//Gpt 요청 보내기
document.getElementById('imageForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const imageFileInput = document.getElementById('imageFile');
    const imageFile = imageFileInput.files[0];

    if (imageFile) {
        const formData = new FormData();
        formData.append('image', imageFile);
        const jwtToken = localStorage.getItem('jwtToken');

        fetch('http://localhost:8080/api/v1/gpt/chat', {
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
});
