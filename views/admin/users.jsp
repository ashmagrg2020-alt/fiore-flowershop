<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Users — Fiore Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<div class="admin-layout">
    <%@ include file="sidebar.jsp" %>
    <main class="admin-main">
        <div class="admin-topbar">
            <h1>Manage Users</h1>
        </div>

        <c:if test="${not empty sessionScope.flashSuccess}">
            <div class="alert alert-success alert-dismissible mb-2">${sessionScope.flashSuccess}</div>
            <% session.removeAttribute("flashSuccess"); %>
        </c:if>

        <!-- Pending -->
        <c:if test="${not empty pendingUsers}">
            <div class="admin-card mb-3">
                <h3 style="font-size:1.05rem;margin-bottom:1rem;">⏳ Pending Approvals</h3>
                <div class="table-wrapper">
                    <table>
                        <thead><tr><th>Name</th><th>Email</th><th>Phone</th><th>Address</th><th>Registered</th><th>Actions</th></tr></thead>
                        <tbody>
                            <c:forEach var="u" items="${pendingUsers}">
                                <tr>
                                    <td style="font-weight:500;">${u.fullName}</td>
                                    <td>${u.email}</td>
                                    <td>${u.phone}</td>
                                    <td style="font-size:.82rem;color:var(--mid-gray);">${u.address}</td>
                                    <td style="font-size:.82rem;color:var(--mid-gray);">${u.createdAt}</td>
                                    <td>
                                        <form method="post" action="${pageContext.request.contextPath}/admin/users" style="display:inline;">
                                            <input type="hidden" name="id" value="${u.id}">
                                            <input type="hidden" name="action" value="approve">
                                            <button class="btn btn-success btn-sm">✓ Approve</button>
                                        </form>
                                        <form method="post" action="${pageContext.request.contextPath}/admin/users" style="display:inline;margin-left:.4rem;">
                                            <input type="hidden" name="id" value="${u.id}">
                                            <input type="hidden" name="action" value="reject">
                                            <button class="btn btn-danger btn-sm">✕ Reject</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:if>

        <!-- All users -->
        <div class="admin-card">
            <h3 style="font-size:1.05rem;margin-bottom:1rem;">All Registered Users</h3>
            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr><th>#</th><th>Name</th><th>Email</th><th>Phone</th><th>Status</th><th>Registered</th><th>Actions</th></tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${users}" varStatus="s">
                            <tr>
                                <td>${s.count}</td>
                                <td style="font-weight:500;">${u.fullName}</td>
                                <td>${u.email}</td>
                                <td>${u.phone}</td>
                                <td><span class="badge badge-${u.status}">${u.status}</span></td>
                                <td style="font-size:.82rem;color:var(--mid-gray);">${u.createdAt}</td>
                                <td>
                                    <c:if test="${u.status == 'pending'}">
                                        <form method="post" action="${pageContext.request.contextPath}/admin/users" style="display:inline;">
                                            <input type="hidden" name="id" value="${u.id}">
                                            <input type="hidden" name="action" value="approve">
                                            <button class="btn btn-success btn-sm">Approve</button>
                                        </form>
                                    </c:if>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/users"
                                          style="display:inline;margin-left:.4rem;">
                                        <input type="hidden" name="id" value="${u.id}">
                                        <input type="hidden" name="action" value="delete">
                                        <button class="btn btn-danger btn-sm confirm-delete">Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty users}">
                            <tr><td colspan="7" style="text-align:center;color:var(--mid-gray);padding:2rem;">No users found.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
