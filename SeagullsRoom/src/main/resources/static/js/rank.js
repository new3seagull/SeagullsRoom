window.onload = function(){
    var table = document.getElementById("rankTable").getElementsByTagName('tbody')[0];
    var newRow = table.insertRow();
    newRow.classList.add("highlighted");

    var rankCell = newRow.insertCell(0);
    var nameCell = newRow.insertCell(1);
    var timeCell = newRow.insertCell(2);
    var mailCell = newRow.insertCell(3);


    fetch('http://54.180.154.212:8080/api/v1/studies/top10', {
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
            studyRank.data.forEach((activity, index) => {  // index 추가
                var newRow = table.insertRow();

                var rankCell = newRow.insertCell(0);
                var nameCell = newRow.insertCell(1);
                var timeCell = newRow.insertCell(2);
                var mailCell = newRow.insertCell(3);

                console.log(activity);

                // 순위 인덱스는 index + 1로 설정
                rankCell.innerHTML = index + 1;
                nameCell.innerHTML = activity.name;
                timeCell.innerHTML = activity.studyTime;
                mailCell.innerHTML = activity.userEmail;
            })
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });

};