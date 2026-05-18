<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile — Fiore Flower Shop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<%@ include file="../header.jsp" %>

<div class="page-hero">
    <div class="page-hero-content">
        <h1>My Profile</h1>
        <p>Manage your personal information and password</p>
    </div>
</div>

<section class="section">
    <div class="container" style="max-width:800px;">

        <!-- ── Update Profile ── -->
        <div class="admin-card mb-3">
            <h3 style="font-family:'Cormorant Garamond',serif;font-size:1.5rem;margin-bottom:1.5rem;">
                Personal Information
            </h3>

            <c:if test="${not empty profileSuccess}">
                <div class="alert alert-success alert-dismissible">${profileSuccess}</div>
            </c:if>
            <c:if test="${not empty profileError}">
                <div class="alert alert-error">${profileError}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/profile">
                <input type="hidden" name="action" value="profile">

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Full Name</label>
                        <input class="form-control" type="text" name="fullName"
                               value="${user.fullName}" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Email (read-only)</label>
                        <input class="form-control" type="email" value="${user.email}" disabled>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Phone</label>
                        <input class="form-control" type="tel" name="phone"
                               value="${user.phone}" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Date of Birth</label>
                        <input class="form-control" type="date" name="dateOfBirth"
                               value="${user.dateOfBirth}">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Address</label>
                    <textarea class="form-control" name="address" rows="2">${user.address}</textarea>
                </div>

                <button type="submit" class="btn btn-primary">Update Profile</button>
            </form>
        </div>

        <!-- ── Change Password ── -->
        <div class="admin-card">
            <h3 style="font-family:'Cormorant Garamond',serif;font-size:1.5rem;margin-bottom:1.5rem;">
                Change Password
            </h3>

            <c:if test="${not empty pwSuccess}">
                <div class="alert alert-success alert-dismissible">${pwSuccess}</div>
            </c:if>
            <c:if test="${not empty pwError}">
                <div class="alert alert-error">${pwError}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/profile">
                <input type="hidden" name="action" value="password">

                <div class="form-group">
                    <label class="form-label">Current Password</label>
                    <div class="input-group">
                        <input class="form-control" type="password" id="currentPw"
                               name="currentPassword" placeholder="••••••••" required>
                        <button type="button" class="input-group-icon pw-toggle" data-target="currentPw">👁️</button>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">New Password</label>
                        <div class="input-group">
                            <input class="form-control" type="password" id="newPw"
                                   name="newPassword" placeholder="••••••••" required minlength="6">
                            <button type="button" class="input-group-icon pw-toggle" data-target="newPw">👁️</button>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Confirm New Password</label>
                        <input class="form-control" type="password"
                               name="confirmNewPassword" placeholder="••••••••" required>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary">Change Password</button>
            </form>
        </div>

    </div>
</section>

<%@ include file="../footer.jsp" %>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
