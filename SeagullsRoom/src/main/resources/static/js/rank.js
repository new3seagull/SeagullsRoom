window.onload = function(){
    var table = document.getElementById("rankTable").getElementsByTagName('tbody')[0];
    var newRow = table.insertRow();
    newRow.classList.add("highlighted");

    var rankCell = newRow.insertCell(0);
    var nameCell = newRow.insertCell(1);
    var timeCell = newRow.insertCell(2);
    var mailCell = newRow.insertCell(3);

    rankCell.innerHTML = '3';
    nameCell.innerHTML = '김상유';
    timeCell.innerHTML = '10:00:01';
    mailCell.innerHTML = 'Kim@naver.com';

    fetch('http://localhost:8080/api/v1/studies/top10', {
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
        .then(studyRank => {
            console.log("here!!")
            console.log(studyRank)
            studyRank.data.forEach(activity => {
                var newRow = table.insertRow();

                var rankCell = newRow.insertCell(0);
                var nameCell = newRow.insertCell(1);
                var timeCell = newRow.insertCell(2);
                var mailCell = newRow.insertCell(3);

                console.log(activity);


                rankCell.innerHTML = activity.id;
                nameCell.innerHTML = '이름 내놔';
                timeCell.innerHTML = activity.studyTime;
                mailCell.innerHTML = activity.userEmail;
            })
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });

};