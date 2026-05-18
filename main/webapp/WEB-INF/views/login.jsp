<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login — Fiore Flower Shop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<div class="auth-page">
    <div class="auth-card">

        <!-- Logo -->
        <div class="auth-logo">
            <img src="${pageContext.request.contextPath}/static/images/logo.png"
                 alt="Fiore Logo"
                 onerror="this.style.display='none'">
            Fiore
        </div>
        <div class="auth-title">Welcome Back</div>

        <!-- Alerts -->
        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <c:if test="${not empty param.redirect}">
                <input type="hidden" name="redirect" value="${param.redirect}">
            </c:if>

            <div class="form-group">
                <label class="form-label" for="email">Email</label>
                <input class="form-control" type="email" id="email" name="email"
                       placeholder="your.email@example.com" required autofocus>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">Password</label>
                <div class="input-group">
                    <input class="form-control" type="password" id="password" name="password"
                           placeholder="••••••••" required>
                    <button type="button" class="input-group-icon pw-toggle" data-target="password">👁️</button>
                </div>
            </div>

            <div class="form-check">
                <input type="checkbox" id="remember" name="remember">
                <label for="remember">Remember Me</label>
            </div>

            <button type="submit" class="btn btn-primary btn-block btn-lg">Login</button>
        </form>

        <div class="auth-footer">
            Don't have an account?
            <a href="${pageContext.request.contextPath}/register" class="auth-link">Register</a>
        </div>
        <div class="auth-footer" style="margin-top:.5rem;">
            <a href="${pageContext.request.contextPath}/home" class="auth-link">← Back to Home</a>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
