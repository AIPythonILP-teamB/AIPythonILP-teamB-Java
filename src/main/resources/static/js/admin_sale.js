const sales = [];

function addRow() {
    const select = document.getElementById('productSelect');
    const productId = select.value;
    const productName = select.options[select.selectedIndex].text;
    const quantity = document.getElementById('quantityInput').value;

    if (!productId || !quantity || quantity <= 0) return;

    sales.push({
        productId: parseInt(productId),
        productName: productName,
        quantity: parseInt(quantity)
    });

    const row = `<tr><td>${productName}</td><td>${quantity}</td></tr>`;
    document.querySelector('#salesTable tbody').insertAdjacentHTML('beforeend', row);

    document.getElementById('quantityInput').value = '';
}

document.getElementById('confirmForm').addEventListener('submit', function (event) {
    const date = document.getElementById('inputDate').value;
    if (!date || sales.length === 0) {
        alert("日付と販売実績を入力してください");
        event.preventDefault(); // フォーム送信を止める
        return;
    }

    document.getElementById('hiddenDate').value = date;
    document.getElementById('salesJson').value = JSON.stringify(sales);
});

