<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Bouquets — Fiore Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<div class="admin-layout">
    <%@ include file="sidebar.jsp" %>
    <main class="admin-main">
        <div class="admin-topbar">
            <h1>Manage Bouquets</h1>
            <a href="${pageContext.request.contextPath}/admin/bouquets?action=add"
               class="btn btn-primary">+ Add Bouquet</a>
        </div>

        <c:if test="${not empty sessionScope.flashSuccess}">
            <div class="alert alert-success alert-dismissible mb-2">${sessionScope.flashSuccess}</div>
            <% session.removeAttribute("flashSuccess"); %>
        </c:if>

        <div class="admin-card">
            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Image</th>
                            <th>Name</th>
                            <th>Category</th>
                            <th>Occasion</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Featured</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="b" items="${bouquets}" varStatus="s">
                            <tr>
                                <td>${s.count}</td>
                                <td>
                                    <img src="${pageContext.request.contextPath}/static/images/bouquets/${b.imagePath}"
                                         alt="${b.name}"
                                         style="width:50px;height:50px;border-radius:8px;object-fit:cover;"
                                         onerror="this.style.display='none'">
                                </td>
                                <td style="font-weight:500;">${b.name}</td>
                                <td>${b.category}</td>
                                <td>${not empty b.occasion ? b.occasion : '—'}</td>
                                <td style="color:var(--primary);font-weight:600;">${b.formattedPrice}</td>
                                <td>
                                    <span class="badge-stock ${b.stockQuantity > 0 ? 'in-stock' : 'out-stock'}">
                                        ${b.stockQuantity}
                                    </span>
                                </td>
                                <td>
                                    <c:if test="${b.featured}">
                                        <span class="badge badge-featured">Yes</span>
                                    </c:if>
                                    <c:if test="${!b.featured}">—</c:if>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/bouquets?action=edit&id=${b.id}"
                                       class="btn btn-outline btn-sm">Edit</a>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/bouquets"
                                          style="display:inline;margin-left:.4rem;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id"     value="${b.id}">
                                        <button class="btn btn-danger btn-sm confirm-delete">Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty bouquets}">
                            <tr><td colspan="9" style="text-align:center;color:var(--mid-gray);padding:2rem;">No bouquets found.</td></tr>
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
