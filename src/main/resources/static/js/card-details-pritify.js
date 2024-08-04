function formatCardNumber(event) {
    const input = event.target;
    const value = input.value.replace(/\D/g, '');
    const formattedValue = value.match(/.{1,4}/g)?.join(' ') || '';
    input.value = formattedValue;
}

document.addEventListener('DOMContentLoaded', () => {
    const cardNumberInput = document.getElementById('cardNumber');
    if (cardNumberInput) {
        cardNumberInput.addEventListener('input', formatCardNumber);
    }
});

function formatExpirationDate(event) {
    const input = event.target;
    let value = input.value.replace(/\D/g, '');

    if (value.length > 2) {
        value = value.slice(0, 2) + '/' + value.slice(2, 4);
    }

    input.value = value;
}

document.addEventListener('DOMContentLoaded', () => {
    const expirationDateInput = document.getElementById('cardExpiration');
    if (expirationDateInput) {
        expirationDateInput.addEventListener('input', formatExpirationDate);
    }
});