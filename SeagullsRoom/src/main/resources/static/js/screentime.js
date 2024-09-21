document.addEventListener('DOMContentLoaded', function() {
    fetchScreenTimeData();
});

function fetchScreenTimeData() {
    const jwtToken = localStorage.getItem('jwtToken');

    fetch('http://localhost:8080/api/v1/screenTime', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        displayScreenTimeData(data);
    })
    .catch(error => {
        console.error('Error fetching screen time data:', error);
    });
}

function displayScreenTimeData(data) {
    const screenTimeDataElement = document.getElementById('screenTimeData');

    if (data.length === 0) {
        screenTimeDataElement.innerHTML = '<p>No screen time data available.</p>';
        return;
    }

    let html = '<ul>';
    data.forEach(item => {
        html += `<li>${item.category}: ${item.count} minutes</li>`;
    });
    html += '</ul>';

    screenTimeDataElement.innerHTML = html;
}