window.onload = function() {
    function timeStringToSeconds(timeString) {
        const [hours, minutes, seconds] = timeString.split(':').map(Number);
        return hours * 3600 + minutes * 60 + seconds;
    }

    function secondsToTimeString(totalSeconds) {
        const hours = Math.floor(totalSeconds / 3600)
        const minutes = Math.floor((totalSeconds % 3600) / 60);
        const seconds = totalSeconds % 60;
        return `${hours}시간 ${minutes}분 ${seconds}초`;
    }

    fetch('http://54.180.154.212:8080/api/v1/friend/count', {
        method: 'GET',
        headers: {
            'Authorization': localStorage.getItem('jwtToken'),
            'Content-Type': 'application/json'
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json(); // 응답을 JSON으로 변환
        })
        .then(data => {
            document.querySelector(".profile-stats span:nth-child(1) strong").textContent = data.followerCount.toString();
            document.querySelector(".profile-stats span:nth-child(2) strong").textContent = data.followingCount.toString();
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });

    fetch('http://54.180.154.212:8080/api/v1/studies/user/month', {
        method: 'GET',
        headers: {
            'Authorization': localStorage.getItem('jwtToken'),
            'Content-Type': 'application/json'
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json(); // 응답을 JSON으로 변환
        })
        .then(studyData => {
            // JSON 데이터가 배열 형태로 들어옴
            let monthStudySeconds = 0
            const activityList = document.getElementById('activity-list');
            activityList.innerHTML = '';
            studyData.data.forEach(activity => {
                const listItem = document.createElement('li');
                listItem.innerText = `${activity.updatedAt.substr(0,10)} - ${activity.studyTime} 공부`
                activityList.appendChild(listItem);
                monthStudySeconds += timeStringToSeconds(activity.studyTime);
            })

            const monthStudyTime = secondsToTimeString(monthStudySeconds);

            document.getElementById('month-study-time').innerText = monthStudyTime;
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });


    fetch('http://54.180.154.212:8080/api/v1/studies/user/date', {
        method: 'GET',
        headers: {
            'Authorization': localStorage.getItem('jwtToken'),
            'Content-Type': 'application/json'
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json(); // 응답을 JSON으로 변환
        })
        .then(studyData => {
            // JSON 데이터가 배열 형태로 들어옴
            const item = studyData.data;

            console.log(`ID: ${item.id}`);
            console.log(`User Email: ${item.userEmail}`);
            console.log(`Study Time: ${item.studyTime}`);
            console.log(`Updated At: ${item.updatedAt}`);
            console.log('---');

            let dateStudySeconds = timeStringToSeconds(item.studyTime);



            const dateStudyTime = secondsToTimeString(dateStudySeconds);

            document.getElementById('daily-study-time').innerText = dateStudyTime;
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });


};