function renderSensorChart(canvasId, config) {
    const canvas = document.getElementById(canvasId);
    if (!canvas) return;

    if (!config.labels || config.labels.length === 0) {
        canvas.parentElement.innerHTML = "<p>No data available</p>";
        return;
    }

    const chartType = config.type || "line";

    const safeValues = (config.values || []).map(v => v ?? 0);

    if (canvas.chartInstance) {
        canvas.chartInstance.destroy();
    }

    canvas.chartInstance = new Chart(canvas, {
        type: chartType,
        data: {
            labels: config.labels || [],
            datasets: [{
                label: config.datasetLabel || "Average value",
                data: safeValues,
                borderColor: "#166534",
                backgroundColor: chartType === "bar"
                    ? "rgba(22, 101, 52, 0.75)"
                    : "rgba(34, 197, 94, 0.20)",
                borderWidth: 2,
                tension: 0.35,
                fill: chartType !== "bar",
                pointRadius: chartType === "bar" ? 0 : 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: true },
                tooltip: {
                    callbacks: {
                        label: function (context) {
                            const value = context.raw ?? 0;
                            const formatted = Number(value).toFixed(2);
                            const unit = config.unit ? " " + config.unit : "";
                            return "Average: " + formatted + unit;
                        }
                    }
                }
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: "Time"
                    }
                },
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function (value) {
                            return config.unit ? value + " " + config.unit : value;
                        }
                    }
                }
            }
        }
    });
}