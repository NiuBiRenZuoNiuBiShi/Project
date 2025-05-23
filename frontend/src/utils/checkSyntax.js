export const checkPassword = (password) => {
    const regex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
    return regex.test(password);
}

export const checkUsername = (username) => {
    const regex = /^[a-zA-Z0-9._-]{3,}$/;
    return regex.test(username);
}
