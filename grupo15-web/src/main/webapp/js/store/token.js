function setToken(token) {
    localStorage.setItem('MITOKEN', token)
}

function getToken() {

    return localStorage.getItem('MITOKEN')
}