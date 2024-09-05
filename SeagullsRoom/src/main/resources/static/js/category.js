let categories = JSON.parse(localStorage.getItem('categories')) || [];

const categoryList = document.getElementById('categoryList');
const addCategoryButton = document.getElementById('addCategoryButton');
const categoryModal = document.getElementById('categoryModal');
const categoryForm = document.getElementById('categoryForm');
const categoryNameInput = document.getElementById('categoryName');

function updateCategoryList() {
    categoryList.innerHTML = '';
    categories.forEach((category, index) => {
        const li = document.createElement('li');
        li.textContent = category;
        const deleteButton = document.createElement('button');
        deleteButton.textContent = 'X';
        deleteButton.onclick = () => deleteCategory(index);
        li.appendChild(deleteButton);
        categoryList.appendChild(li);
    });
}

function addCategory(name) {
    // 카테고리 이름을 소문자로 변환하여 대소문자 구분 없이 비교
    const lowercaseName = name.toLowerCase();

    // 중복 검사
    if (categories.some(category => category.toLowerCase() === lowercaseName)) {
        alert('이미 존재하는 카테고리입니다.');
        return;
    }

    if (categories.length < 10) {
        categories.push(name);
        localStorage.setItem('categories', JSON.stringify(categories));
        updateCategoryList();
    } else {
        alert('최대 10개의 카테고리만 추가할 수 있습니다.');
    }
}

function deleteCategory(index) {
    categories.splice(index, 1);
    localStorage.setItem('categories', JSON.stringify(categories));
    updateCategoryList();
}

addCategoryButton.onclick = () => {
    categoryModal.style.display = 'block';
};

categoryModal.querySelector('.close').onclick = () => {
    categoryModal.style.display = 'none';
};

categoryForm.onsubmit = (e) => {
    e.preventDefault();
    const categoryName = categoryNameInput.value.trim();
    if (categoryName) {
        addCategory(categoryName);
        categoryNameInput.value = '';
        // 중복된 카테고리가 아닐 경우에만 모달을 닫습니다.
        if (!categories.some(category => category.toLowerCase() === categoryName.toLowerCase())) {
            categoryModal.style.display = 'none';
        }
    }
};

window.onclick = (event) => {
    if (event.target == categoryModal) {
        categoryModal.style.display = 'none';
    }
};

updateCategoryList();