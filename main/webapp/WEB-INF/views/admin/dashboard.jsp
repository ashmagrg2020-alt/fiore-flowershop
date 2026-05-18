<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard — Fiore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<div class="admin-layout">

    <%@ include file="sidebar.jsp" %>

    <main class="admin-main">
        <div class="admin-topbar">
            <h1>Dashboard</h1>
            <span style="color:var(--mid-gray);font-size:.9rem;">Welcome back, <strong>${sessionScope.userName}</strong></span>
        </div>

        <c:if test="${not empty sessionScope.flashSuccess}">
            <div class="alert alert-success alert-dismissible mb-2">${sessionScope.flashSuccess}</div>
            <% session.removeAttribute("flashSuccess"); %>
        </c:if>

        <!-- ── Stat cards ── -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon">👥</div>
                <div class="stat-label">Total Users</div>
                <div class="stat-value">${totalUsers}</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">💐</div>
                <div class="stat-label">Total Bouquets</div>
                <div class="stat-value">${totalBouquets}</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">📦</div>
                <div class="stat-label">Total Orders</div>
                <div class="stat-value">${totalOrders}</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">💰</div>
                <div class="stat-label">Total Revenue</div>
                <div class="stat-value">
                    NPR <fmt:formatNumber value="${totalRevenue}" pattern="#,##0"/>
                </div>
            </div>
        </div>

        <!-- ── Pending Approvals ── -->
        <c:if test="${not empty pendingUsers}">
            <div class="admin-card mb-3">
                <h3 style="font-size:1.1rem;margin-bottom:1rem;color:var(--dark);">
                    ⏳ Pending Registration Approvals
                    <span style="background:#FEF9C3;color:#854D0E;font-size:.75rem;padding:2px 10px;
                                 border-radius:50px;margin-left:.5rem;">${pendingUsers.size()}</span>
                </h3>
                <div class="table-wrapper">
                    <table>
                        <thead>
                            <tr><th>Name</th><th>Email</th><th>Phone</th><th>Registered</th><th>Actions</th></tr>
                        </thead>
                        <tbody>
                            <c:forEach var="u" items="${pendingUsers}">
                                <tr>
                                    <td>${u.fullName}</td>
                                    <td>${u.email}</td>
                                    <td>${u.phone}</td>
                                    <td style="font-size:.82rem;color:var(--mid-gray);">${u.createdAt}</td>
                                    <td>
                                        <form method="post" action="${pageContext.request.contextPath}/admin/users" style="display:inline;">
                                            <input type="hidden" name="id" value="${u.id}">
                                            <input type="hidden" name="action" value="approve">
                                            <button class="btn btn-success btn-sm">Approve</button>
                                        </form>
                                        <form method="post" action="${pageContext.request.contextPath}/admin/users" style="display:inline;margin-left:.4rem;">
                                            <input type="hidden" name="id" value="${u.id}">
                                            <input type="hidden" name="action" value="reject">
                                            <button class="btn btn-danger btn-sm">Reject</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:if>

        <!-- ── Top Sellers ── -->
        <div class="grid-2">
            <div class="admin-card">
                <h3 style="font-size:1.1rem;margin-bottom:1rem;">🏆 Top Selling Bouquets</h3>
                <c:choose>
                    <c:when test="${not empty topSellers}">
                        <c:forEach var="b" items="${topSellers}" varStatus="s">
                            <div style="display:flex;align-items:center;gap:.75rem;padding:.6rem 0;
                                        border-bottom:1px solid var(--rose);font-size:.875rem;">
                                <span style="font-weight:700;color:var(--primary);min-width:20px;">${s.count}.</span>
                                <img src="${pageContext.request.contextPath}/static/images/bouquets/${b.imagePath}"
                                     style="width:36px;height:36px;border-radius:8px;object-fit:cover;"
                                     onerror="this.style.display='none'" alt="">
                                <span style="flex:1;">${b.name}</span>
                                <span style="color:var(--primary);font-weight:600;">${b.formattedPrice}</span>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise><p style="color:var(--mid-gray);">No sales data yet.</p></c:otherwise>
                </c:choose>
            </div>

            <!-- ── Recent Orders ── -->
            <div class="admin-card">
                <h3 style="font-size:1.1rem;margin-bottom:1rem;">📋 Recent Orders</h3>
                <c:choose>
                    <c:when test="${not empty recentOrders}">
                        <div class="table-wrapper">
                            <table>
                                <thead><tr><th>User</th><th>Bouquet</th><th>Total</th><th>Status</th></tr></thead>
                                <tbody>
                                    <c:forEach var="o" items="${recentOrders}" end="4">
                                        <tr>
                                            <td style="font-size:.82rem;">${o.userName}</td>
                                            <td style="font-size:.82rem;">${o.bouquetName}</td>
                                            <td style="font-size:.82rem;color:var(--primary);font-weight:600;">${o.formattedTotal}</td>
                                            <td><span class="badge badge-${o.status}">${o.status}</span></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise><p style="color:var(--mid-gray);">No orders yet.</p></c:otherwise>
                </c:choose>
            </div>
        </div>

    </main>
</div>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
