<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<aside class="admin-sidebar">
    <div class="admin-sidebar-logo">
        <img src="${pageContext.request.contextPath}/static/images/logo.png"
             alt="Logo" onerror="this.style.display='none'">
        Fiore Admin
    </div>

    <nav>
        <a href="${pageContext.request.contextPath}/admin/dashboard"
           class="admin-nav-item">
            <span class="nav-icon">📊</span> Dashboard
        </a>
        <a href="${pageContext.request.contextPath}/admin/bouquets"
           class="admin-nav-item">
            <span class="nav-icon">💐</span> Bouquets
        </a>
        <a href="${pageContext.request.contextPath}/admin/users"
           class="admin-nav-item">
            <span class="nav-icon">👥</span> Users
        </a>
        <a href="${pageContext.request.contextPath}/admin/orders"
           class="admin-nav-item">
            <span class="nav-icon">📦</span> Orders
        </a>
        <a href="${pageContext.request.contextPath}/admin/reports"
           class="admin-nav-item">
            <span class="nav-icon">📈</span> Reports
        </a>
        <hr style="border-color:rgba(255,255,255,.1);margin:1rem 0;">
        <a href="${pageContext.request.contextPath}/home"
           class="admin-nav-item">
            <span class="nav-icon">🏠</span> View Site
        </a>
        <a href="${pageContext.request.contextPath}/logout"
           class="admin-nav-item">
            <span class="nav-icon">🚪</span> Logout
        </a>
    </nav>
</aside>
