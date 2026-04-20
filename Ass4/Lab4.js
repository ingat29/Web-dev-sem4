const box = document.getElementById('draggable-box');

let offsetX = 0;
let offsetY = 0;
let isDragging = false;

box.addEventListener('mousedown', function(e) {
    isDragging = true;

    offsetX = e.clientX - box.offsetLeft;
    offsetY = e.clientY - box.offsetTop;
});

document.addEventListener('mousemove', function(e) {
    if (!isDragging) return;

    const newX = e.clientX - offsetX;
    const newY = e.clientY - offsetY;

    box.style.left = newX + 'px';
    box.style.top = newY + 'px';
});

document.addEventListener('mouseup', function() {
    isDragging = false;
});