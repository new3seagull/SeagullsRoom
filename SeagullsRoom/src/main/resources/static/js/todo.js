// 전역 변수로 todos 배열을 선언
let todos = [];

// 모달 관련 함수들
function openModal() {
    document.getElementById('todoModal').style.display = 'block';
}

function closeModal() {
    document.getElementById('todoModal').style.display = 'none';
    document.getElementById('todoForm').reset();
}

// 헤더에 토큰을 추가하는 함수
function getHeaders() {
    return {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('jwtToken')
    };
}

// Todo 목록을 가져오는 함수
function fetchTodos() {
    console.log('Fetching todos...');
    const headers = getHeaders();
    return fetch('http://54.180.154.212:8080/api/v1/todo', {
        method: 'GET',
        headers: headers
    })
    .then(handleResponse)
    .then(fetchedTodos => {
        console.log('Received todos:', fetchedTodos);
        todos = fetchedTodos; // 전역 todos 배열을 업데이트합니다.
        renderTodos(); // Todo 목록을 렌더링합니다.
    })
    .catch(error => {
        console.error('Error in fetchTodos:', error);
        handleError(error);
    });
}

// Todo 목록을 렌더링하는 함수
function renderTodos() {
    const todoList = document.getElementById('todoList');
    todoList.innerHTML = ''; // 기존 항목을 지웁니다.
    todos.forEach(todo => {
        const li = document.createElement('li');
        li.innerHTML = `
            <input type="checkbox" id="todo-${todo.id}" ${todo.completed
            ? 'checked' : ''}>
            <label for="todo-${todo.id}">${todo.title}</label>
        `;
        const checkbox = li.querySelector('input[type="checkbox"]');
        checkbox.addEventListener('change',
            () => updateTodo(todo.id, checkbox.checked));
        todoList.appendChild(li);
    });
}

// Todo 항목을 업데이트하는 함수
function updateTodo(id, completed) {
    console.log(`Updating todo ${id} to completed: ${completed}`);
    const headers = getHeaders();
    const todoItem = todos.find(todo => todo.id === id);
    if (!todoItem) {
        console.error('Todo item not found');
        return;
    }

    const updatedTodo = {
        ...todoItem,
        completed: completed
    };

    return fetch(`http://54.180.154.212:8080/api/v1/todo/${id}`, {
        method: 'PUT',
        headers: headers,
        body: JSON.stringify(updatedTodo)
    })
    .then(handleResponse)
    .then(() => fetchTodos()) // 업데이트 후 전체 목록을 다시 가져옵니다.
    .catch(error => {
        console.error('Error in updateTodo:', error);
        handleError(error);
    });
}

// 새로운 Todo 항목을 추가하는 함수
function addTodo(event) {
    event.preventDefault();
    const title = document.getElementById('todoTitle').value.trim();
    const description = document.getElementById('todoDescription').value.trim();
    const completed = document.getElementById('todoCompleted').checked;

    if (!title) {
        alert('제목을 입력해주세요.');
        return;
    }

    const headers = getHeaders();
    const newTodo = { title, description, completed };

    fetch('http://54.180.154.212:8080/api/v1/todo', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(newTodo)
    })
    .then(handleResponse)
    .then(() => {
        closeModal();
        return fetchTodos(); // 새로운 Todo가 추가된 후 전체 목록을 다시 가져옵니다.
    })
    .catch(error => {
        console.error('Error in addTodo:', error);
        handleError(error);
    });
}

// 응답 처리 함수
function handleResponse(response) {
    if (!response.ok) {
        console.error('Response not OK:', response.status, response.statusText);
        if (response.status === 401) {
            throw new Error('Unauthorized');
        }
        throw new Error('HTTP error ' + response.status);
    }
    return response.json();
}

// 에러 처리 함수
function handleError(error) {
    console.error('An error occurred:', error);
    if (error.message === 'No token found') {
        alert('로그인 정보를 찾을 수 없습니다. 다시 로그인해 주세요.');
        window.location.href = '/login';
    } else if (error.message === 'Unauthorized' || error.response?.status
        === 401) {
        alert('인증에 실패했습니다. 다시 로그인해 주세요.');
        localStorage.removeItem('jwtToken');
        window.location.href = '/login';
    } else {
        alert('오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
    }
}

// 페이지 로드 시 실행
document.addEventListener('DOMContentLoaded', function () {
    console.log('DOM content loaded');
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        console.log('No token found on page load, redirecting to login');
        window.location.href = 'http://54.180.154.212:8080/login';
    } else {
        console.log('Token found, fetching todos');
        fetchTodos().catch(handleError);

        // 모달 관련 이벤트 리스너 추가
        const addTodoButton = document.getElementById('addTodoButton');
        if (addTodoButton) {
            addTodoButton.addEventListener('click', openModal);
            console.log('Add Todo button event listener added');
        } else {
            console.error('Add Todo button not found');
        }

        const closeButton = document.querySelector('.close');
        if (closeButton) {
            closeButton.addEventListener('click', closeModal);
            console.log('Close button event listener added');
        } else {
            console.error('Close button not found');
        }

        const todoForm = document.getElementById('todoForm');
        if (todoForm) {
            todoForm.addEventListener('submit', addTodo);
            console.log('Todo form submit event listener added');
        } else {
            console.error('Todo form not found');
        }

        // 모달 외부 클릭 시 닫기
        window.addEventListener('click', (event) => {
            if (event.target === document.getElementById('todoModal')) {
                closeModal();
            }
        });
    }
});
