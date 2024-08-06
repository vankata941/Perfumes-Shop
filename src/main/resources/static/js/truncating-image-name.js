document.addEventListener('DOMContentLoaded', () => {
    const truncateText = (element, maxLength) => {
        const text = element.textContent;
        if (text.length > maxLength) {
            element.textContent = text.slice(0, maxLength) + '...';
        }
    };

    const maxLength = 20;

    document.querySelectorAll('.image-name').forEach(el => {
        truncateText(el, maxLength);
    });
});