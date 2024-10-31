document.addEventListener('DOMContentLoaded', function() {
    fetchScreenTimeData();
});

function fetchScreenTimeData() {
    const jwtToken = localStorage.getItem('jwtToken');

    fetch('http://localhost:8080/api/v1/screenTime', {
        method: 'GET',
        headers: {
            'Authorization': `${jwtToken}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        displayScreenTimeData(data);
    })
    .catch(error => {
        console.error('Error fetching screen time data:', error);
        document.getElementById('screenTimeData').innerHTML =
            '<p class="error-message">Error loading screen time data. Please try again later.</p>';
    });
}

function displayScreenTimeData(data) {
    const screenTimeDataElement = document.getElementById('screenTimeData');

    if (data.length === 0) {
        screenTimeDataElement.innerHTML = '<p class="no-data-message">No screen time data available.</p>';
        return;
    }

    const totalTime = data.reduce((sum, item) => sum + item.count, 0);

    let sortedData = data.map(item => ({
        category: item.category,
        count: item.count,
        percentage: (item.count / totalTime) * 100
    })).sort((a, b) => b.percentage - a.percentage);

    let html = '<ul>';
    sortedData.forEach(item => {
        // 시간과 분으로 변환
        const hours = Math.floor(item.count / 60);
        const minutes = item.count % 60;
        const timeString = hours > 0 ?
            `${hours}h ${minutes}m` :
            `${minutes}m`;

        html += `
            <li>
                <span style="color: #000; font-weight: 700">${item.category}</span>
                <span style="color: #000; font-weight: 700">${item.percentage.toFixed(1)}% (${timeString})</span>
            </li>`;
    });
    html += '</ul>';

    screenTimeDataElement.innerHTML = html;
    createDonutChart(sortedData);
}

const colors = [
    '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0',
    '#9966FF', '#FF9F40', '#FF9999', '#66CCCC'
];

function createDonutChart(data) {
    const svg = document.querySelector('svg');
    svg.innerHTML = '<circle cx="21" cy="21" r="15.91549430918954" fill="#fff"></circle>';

    const totalTime = data.reduce((sum, item) => sum + item.count, 0);
    let cumulativePercentage = 0;

    data.forEach((item, index) => {
        const percentage = item.count / totalTime;
        const startAngle = cumulativePercentage * 2 * Math.PI;
        const endAngle = (cumulativePercentage + percentage) * 2 * Math.PI;

        const x1 = 21 + 15.91549430918954 * Math.sin(startAngle);
        const y1 = 21 - 15.91549430918954 * Math.cos(startAngle);
        const x2 = 21 + 15.91549430918954 * Math.sin(endAngle);
        const y2 = 21 - 15.91549430918954 * Math.cos(endAngle);

        const largeArcFlag = percentage > 0.5 ? 1 : 0;

        const pathData = [
            `M ${x1} ${y1}`,
            `A 15.91549430918954 15.91549430918954 0 ${largeArcFlag} 1 ${x2} ${y2}`,
            `L 21 21`
        ].join(' ');

        const path = document.createElementNS('http://www.w3.org/2000/svg', 'path');
        path.setAttribute('d', pathData);
        path.setAttribute('fill', colors[index % colors.length]);
        svg.appendChild(path);

        cumulativePercentage += percentage;
    });

    createLegend(data);
}

function createLegend(data) {
    const legend = document.querySelector('.legend');
    legend.innerHTML = '';

    data.forEach((item, index) => {
        const legendItem = document.createElement('div');
        legendItem.className = 'legend-item';

        const colorBox = document.createElement('div');
        colorBox.className = 'color-box';
        colorBox.style.backgroundColor = colors[index % colors.length];

        const text = document.createElement('span');
        // 시간 표시 추가
        const hours = Math.floor(item.count / 60);
        const minutes = item.count % 60;
        const timeString = hours > 0 ?
            `${hours}h ${minutes}m` :
            `${minutes}m`;

        text.textContent = `${item.category} (${timeString})`;

        legendItem.appendChild(colorBox);
        legendItem.appendChild(text);
        legend.appendChild(legendItem);
    });
}