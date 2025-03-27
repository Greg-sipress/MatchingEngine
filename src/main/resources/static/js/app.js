const socket = new WebSocket('ws://' + window.location.host + '/ws/market-data');
const priceChart = new Chart(
    document.getElementById('priceChart'),
    {
        type: 'line',
        data: {
            datasets: []
        }
    }
);

// Store last 100 data points per symbol
const historicalData = {};

socket.onmessage = (event) => {
    const data = JSON.parse(event.data);
    updateDataGrid(data);
    updateChart(data);
};

function updateDataGrid(data) {
    const row = document.createElement('tr');
    row.innerHTML = `
        <td>${data.symbol}</td>
        <td>$${data.price.toFixed(2)}</td>
        <td class="${data.change >= 0 ? 'positive' : 'negative'}">
            ${data.change >= 0 ? '+' : ''}${data.change.toFixed(2)}%
        </td>
        <td>${data.volume.toLocaleString()}</td>
    `;
    document.querySelector('#dataGrid tbody').prepend(row);
}

function updateChart(data) {
    if (!historicalData[data.symbol]) {
        historicalData[data.symbol] = {
            label: data.symbol,
            data: [],
            borderColor: getRandomColor(),
            tension: 0.1
        };
        priceChart.data.datasets.push(historicalData[data.symbol]);
    }

    historicalData[data.symbol].data.push({
        x: new Date(data.timestamp),
        y: data.price
    });

    // Keep only last 100 points
    if (historicalData[data.symbol].data.length > 100) {
        historicalData[data.symbol].data.shift();
    }

    priceChart.update();
}

function startSimulator() {
    const rate = document.getElementById('rateInput').value;
    socket.send(JSON.stringify({
        action: 'start',
        rate: parseInt(rate)
    }));
}

function stopSimulator() {
    socket.send(JSON.stringify({
        action: 'stop'
    }));
}

function getRandomColor() {
    return `#${Math.floor(Math.random()*16777215).toString(16)}`;
}