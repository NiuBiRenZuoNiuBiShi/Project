const searchTrainTicket = async (from, to, date) => {
    const response = await fetch(
        `https://api.example.com/train-tickets?from=${from}&to=${to}&date=${date}`
    );
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const data = await response.json();
    return data;
}