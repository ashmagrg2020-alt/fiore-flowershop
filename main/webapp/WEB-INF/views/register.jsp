<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register — Fiore Flower Shop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<div class="auth-page">
    <div class="auth-card">

        <div class="auth-logo">
            <img src="${pageContext.request.contextPath}/static/images/logo.png"
                 alt="Fiore Logo" onerror="this.style.display='none'">
            Fiore
        </div>
        <div class="auth-title">Create Your Account</div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/register" id="registerForm">

            <div class="form-group">
                <label class="form-label" for="fullName">Full Name</label>
                <input class="form-control" type="text" id="fullName" name="fullName"
                       placeholder="John Doe"
                       value="${fullName}" required autofocus>
                <span class="form-hint">Letters and spaces only — no numbers.</span>
            </div>

            <div class="form-group">
                <label class="form-label" for="email">Email</label>
                <input class="form-control" type="email" id="email" name="email"
                       placeholder="your.email@example.com"
                       value="${email}" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="phone">Phone Number</label>
                <input class="form-control" type="tel" id="phone" name="phone"
                       placeholder="+977 123 456 7890"
                       value="${phone}" required>
                <span class="form-hint">Used as a unique identifier — must not already be registered.</span>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label" for="password">Password</label>
                    <div class="input-group">
                        <input class="form-control" type="password" id="password" name="password"
                               placeholder="••••••••" required minlength="6">
                        <button type="button" class="input-group-icon pw-toggle" data-target="password">👁️</button>
                    </div>
                    <span class="form-hint">Minimum 6 characters.</span>
                </div>
                <div class="form-group">
                    <label class="form-label" for="confirmPassword">Confirm Password</label>
                    <input class="form-control" type="password" id="confirmPassword"
                           name="confirmPassword" placeholder="••••••••" required>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label" for="dateOfBirth">Date of Birth</label>
                <input class="form-control" type="date" id="dateOfBirth" name="dateOfBirth"
                       value="${dob}" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="address">Address</label>
                <textarea class="form-control" id="address" name="address" rows="2"
                          placeholder="Enter your full address" required>${address}</textarea>
            </div>

            <div class="auth-note">
                <strong>Note:</strong> Your registration will be reviewed by an admin before approval.
                You will be able to log in once your account is approved.
            </div>

            <button type="submit" class="btn btn-primary btn-block btn-lg">Register</button>
        </form>

        <div class="auth-footer">
            Already have an account?
            <a href="${pageContext.request.contextPath}/login" class="auth-link">Login</a>
        </div>
        <div class="auth-footer" style="margin-top:.5rem;">
            <a href="${pageContext.request.contextPath}/home" class="auth-link">← Back to Home</a>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
