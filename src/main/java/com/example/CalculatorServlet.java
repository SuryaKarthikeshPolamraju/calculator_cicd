package com.example;

import java.io.*;
import jakarta.servlet.http.*;

public class CalculatorServlet extends HttpServlet {
    private Calculator calc = new Calculator();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        try {
            int a = Integer.parseInt(req.getParameter("a"));
            int b = Integer.parseInt(req.getParameter("b"));
            String op = req.getParameter("op");

            double result;
            switch (op) {
                case "add": result = calc.add(a, b); break;
                case "subtract": result = calc.subtract(a, b); break;
                case "multiply": result = calc.multiply(a, b); break;
                case "divide": result = calc.divide(a, b); break;
                default:
                    resp.getWriter().println("Unknown operation: " + op);
                    return;
            }
            resp.getWriter().println("Result: " + result);
        } catch (Exception e) {
            resp.getWriter().println("Error: " + e.getMessage());
        }
    }
}