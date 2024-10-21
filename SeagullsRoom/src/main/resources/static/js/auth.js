const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');


document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    fetch('http://54.180.154.212:8080/api/v1/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email: email, password: password })
    }).then(response => {
        if (response.ok) {
            // Assuming the token is in a JSON response
            const token = response.headers.get('Authorization');
            console.log(token);
            if (token) {
                localStorage.setItem('jwtToken', token);
                console.log('JWT Token saved to localStorage');
                location.href='main.html';
            } else {
                console.log('Login failed');
                // Handle login failure
            }
        }
    }).catch(error => {
        console.error('Error:', error);
    });
});


document.getElementById('signupForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const name_input = document.getElementById('signup_name');
    const email_input = document.getElementById('signup_email');
    const password_input = document.getElementById('signup_password');
    const name = name_input.value;
    const email = email_input.value;
    const password = password_input.value;

    fetch('http://54.180.154.212:8080/api/v1/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name: name, email: email, password: password })
    }).then(response => {
        if (response.ok) {
            console.log('Sign up successful');
            // location.href='login.html'
            name_input.value = '';
            email_input.value = '';
            password_input.value = '';
            container.classList.remove("right-panel-active");

            // Handle successful sign up
        } else {
            console.log('Sign up failed');
            // Handle sign up failure
        }
    }).catch(error => {
        console.error('Error:', error);
    });
});

signUpButton.addEventListener('click', () => {
    container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
    container.classList.remove("right-panel-active");
});