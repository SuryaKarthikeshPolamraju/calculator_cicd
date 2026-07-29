package test.java.com.example;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculatorServletTest {

    private CalculatorServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new CalculatorServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void testAdd() throws Exception {
        when(request.getParameter("a")).thenReturn("5");
        when(request.getParameter("b")).thenReturn("3");
        when(request.getParameter("op")).thenReturn("add");

        servlet.doGet(request, response);
        printWriter.flush();

        assertTrue(stringWriter.toString().contains("Result: 8.0"));
    }

    @Test
    void testSubtract() throws Exception {
        when(request.getParameter("a")).thenReturn("10");
        when(request.getParameter("b")).thenReturn("4");
        when(request.getParameter("op")).thenReturn("subtract");

        servlet.doGet(request, response);
        printWriter.flush();

        assertTrue(stringWriter.toString().contains("Result: 6.0"));
    }

    @Test
    void testMultiply() throws Exception {
        when(request.getParameter("a")).thenReturn("6");
        when(request.getParameter("b")).thenReturn("7");
        when(request.getParameter("op")).thenReturn("multiply");

        servlet.doGet(request, response);
        printWriter.flush();

        assertTrue(stringWriter.toString().contains("Result: 42.0"));
    }

    @Test
    void testDivide() throws Exception {
        when(request.getParameter("a")).thenReturn("20");
        when(request.getParameter("b")).thenReturn("4");
        when(request.getParameter("op")).thenReturn("divide");

        servlet.doGet(request, response);
        printWriter.flush();

        assertTrue(stringWriter.toString().contains("Result: 5.0"));
    }

    @Test
    void testDivideByZero() throws Exception {
        when(request.getParameter("a")).thenReturn("10");
        when(request.getParameter("b")).thenReturn("0");
        when(request.getParameter("op")).thenReturn("divide");

        servlet.doGet(request, response);
        printWriter.flush();

        assertTrue(stringWriter.toString().contains("Result:") || stringWriter.toString().contains("Error:"));
    }

    @Test
    void testUnknownOperation() throws Exception {
        when(request.getParameter("a")).thenReturn("5");
        when(request.getParameter("b")).thenReturn("3");
        when(request.getParameter("op")).thenReturn("modulo");

        servlet.doGet(request, response);
        printWriter.flush();

        assertTrue(stringWriter.toString().contains("Unknown operation: modulo"));
    }

    @Test
    void testInvalidNumberFormat() throws Exception {
        when(request.getParameter("a")).thenReturn("abc");
        when(request.getParameter("b")).thenReturn("3");
        when(request.getParameter("op")).thenReturn("add");

        servlet.doGet(request, response);
        printWriter.flush();

        assertTrue(stringWriter.toString().contains("Error:"));
    }

    @Test
    void testMissingParameter() throws Exception {
        when(request.getParameter("a")).thenReturn(null);
        when(request.getParameter("b")).thenReturn("3");
        when(request.getParameter("op")).thenReturn("add");

        servlet.doGet(request, response);
        printWriter.flush();

        assertTrue(stringWriter.toString().contains("Error:"));
    }
}