document.addEventListener("DOMContentLoaded", function() {
    const currencyDropDown = document.getElementById('currency');

    function setCookie(name, value) {
    document.cookie = `${name}=${value}; path=/`;
}

    function getCookie(name) {
    const nameEQ = name + "=";
    const cookies = document.cookie.split(';');
    for (const cookie of cookies) {
    let c = cookie.trim();
    if (c.startsWith(nameEQ)) {
    return c.substring(nameEQ.length);
}
}
    return null;
}

    function getCurrencySymbol(currency) {
    const symbols = {
    USD: '$',
    EUR: '€',
    BGN: 'лв.'
};
    return symbols[currency] || '$'; // Default to '$' if currency not found
}

    function currencyChange() {
    const selectedCurrency = currencyDropDown.value;
    const currencySymbol = getCurrencySymbol(selectedCurrency);

    setCookie('preferredCurrency', selectedCurrency);

    const priceElements = document.querySelectorAll('.price');
    const priceInBGNElements = document.querySelectorAll('.priceInBGN');

    priceElements.forEach((priceElement, index) => {
    const amountInBGN = priceInBGNElements[index].value;

    fetch('/currency/convert?' + new URLSearchParams({
    from: 'BGN',
    to: selectedCurrency,
    amount: amountInBGN
}))
    .then(response => response.json())
    .then(data => {
    priceElement.textContent = `${data.result} ${currencySymbol}`;
})
    .catch(() => console.log('An error occurred while fetching currency data!'));
});
}

    function applyPreferredCurrency() {
    const preferredCurrency = getCookie('preferredCurrency');
    if (preferredCurrency) {
    currencyDropDown.value = preferredCurrency;
    currencyChange();
}
}

    currencyDropDown.addEventListener('change', currencyChange);
    applyPreferredCurrency();
});
