<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Calculator - CI/CD Demo</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 400px; margin: 60px auto; text-align: center; }
        input, select, button { padding: 8px; margin: 5px; font-size: 16px; }
        #result { font-size: 20px; font-weight: bold; margin-top: 20px; color: #2c662d; }
    </style>
</head>
<body>
    <h2>Simple Calculator</h2>
    <input type="number" id="a" placeholder="Number A"><br>
    <input type="number" id="b" placeholder="Number B"><br>
    <select id="op">
        <option value="add">+</option>
        <option value="subtract">−</option>
        <option value="multiply">×</option>
        <option value="divide">÷</option>
    </select><br>
    <button onclick="calculate()">Calculate</button>
    <div id="result"></div>

    <script>
        function calculate() {
            const a = document.getElementById('a').value;
            const b = document.getElementById('b').value;
            const op = document.getElementById('op').value;
            fetch('calc?a=' + a + '&b=' + b + '&op=' + op)
                .then(response => response.text())
                .then(text => {
                    document.getElementById('result').innerText = text;
                });
        }
    </script>
</body>
</html>