<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reports — Fiore Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<div class="admin-layout">
    <%@ include file="sidebar.jsp" %>
    <main class="admin-main">
        <div class="admin-topbar">
            <h1>Reports &amp; Analytics</h1>
        </div>

        <!-- Summary Stats -->
        <div class="stats-grid mb-3">
            <div class="stat-card">
                <div class="stat-icon">💰</div>
                <div class="stat-label">Total Revenue</div>
                <div class="stat-value">NPR <fmt:formatNumber value="${totalRevenue}" pattern="#,##0"/></div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">📦</div>
                <div class="stat-label">Total Orders</div>
                <div class="stat-value">${totalOrders}</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">👥</div>
                <div class="stat-label">Registered Users</div>
                <div class="stat-value">${totalUsers}</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">💐</div>
                <div class="stat-label">Total Bouquets</div>
                <div class="stat-value">${allBouquets.size()}</div>
            </div>
        </div>

        <div class="grid-2">
            <!-- Top Sellers -->
            <div class="admin-card">
                <h3 style="font-size:1.1rem;margin-bottom:1rem;">🏆 Top Selling Bouquets</h3>
                <div class="table-wrapper">
                    <table>
                        <thead><tr><th>#</th><th>Name</th><th>Category</th><th>Price</th></tr></thead>
                        <tbody>
                            <c:forEach var="b" items="${topSellers}" varStatus="s">
                                <tr>
                                    <td><strong>${s.count}</strong></td>
                                    <td>${b.name}</td>
                                    <td><span class="tag">${b.category}</span></td>
                                    <td style="color:var(--primary);font-weight:600;">${b.formattedPrice}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty topSellers}">
                                <tr><td colspan="4" style="text-align:center;color:var(--mid-gray);">No sales data.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Stock availability -->
            <div class="admin-card">
                <h3 style="font-size:1.1rem;margin-bottom:1rem;">📊 Stock Availability</h3>
                <div class="table-wrapper">
                    <table>
                        <thead><tr><th>Bouquet</th><th>Category</th><th>Stock</th><th>Status</th></tr></thead>
                        <tbody>
                            <c:forEach var="b" items="${allBouquets}">
                                <tr>
                                    <td style="font-weight:500;">${b.name}</td>
                                    <td><span class="tag">${b.category}</span></td>
                                    <td><strong>${b.stockQuantity}</strong></td>
                                    <td>
                                        <span class="badge-stock ${b.stockQuantity > 0 ? 'in-stock' : 'out-stock'}">
                                            ${b.stockQuantity > 0 ? 'In Stock' : 'Out of Stock'}
                                        </span>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty allBouquets}">
                                <tr><td colspan="4" style="text-align:center;color:var(--mid-gray);">No bouquets found.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

    </main>
</div>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
