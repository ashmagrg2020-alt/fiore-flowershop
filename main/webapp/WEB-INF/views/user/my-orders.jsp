<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Orders — Fiore Flower Shop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<%@ include file="../header.jsp" %>

<div class="page-hero">
    <div class="page-hero-content">
        <h1>My Orders</h1>
        <p>Track your bouquet purchase history</p>
    </div>
</div>

<section class="section">
    <div class="container">

        <c:if test="${not empty sessionScope.flashSuccess}">
            <div class="alert alert-success alert-dismissible">${sessionScope.flashSuccess}</div>
            <% session.removeAttribute("flashSuccess"); %>
        </c:if>
        <c:if test="${not empty sessionScope.flashError}">
            <div class="alert alert-error">${sessionScope.flashError}</div>
            <% session.removeAttribute("flashError"); %>
        </c:if>

        <c:choose>
            <c:when test="${not empty orders}">
                <div class="admin-card">
                    <div class="table-wrapper">
                        <table>
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Bouquet</th>
                                    <th>Qty</th>
                                    <th>Total</th>
                                    <th>Status</th>
                                    <th>Note</th>
                                    <th>Ordered At</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="o" items="${orders}" varStatus="loop">
                                    <tr>
                                        <td>${loop.count}</td>
                                        <td>
                                            <div style="display:flex;align-items:center;gap:.75rem;">
                                                <img src="${pageContext.request.contextPath}/static/images/bouquets/${o.bouquetImage}"
                                                     alt="${o.bouquetName}"
                                                     style="width:44px;height:44px;border-radius:8px;object-fit:cover;"
                                                     onerror="this.style.display='none'">
                                                <span style="font-weight:500;">${o.bouquetName}</span>
                                            </div>
                                        </td>
                                        <td>${o.quantity}</td>
                                        <td style="font-weight:600;color:var(--primary);">${o.formattedTotal}</td>
                                        <td><span class="badge badge-${o.status}">${o.status}</span></td>
                                        <td style="color:var(--mid-gray);font-size:.85rem;">${not empty o.specialNote ? o.specialNote : '—'}</td>
                                        <td style="font-size:.82rem;color:var(--mid-gray);">${o.orderedAt}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="no-results">
                    <div class="no-results-icon">🛒</div>
                    <p>You haven't placed any orders yet.</p>
                    <a href="${pageContext.request.contextPath}/shop" class="btn btn-primary mt-2">Browse Bouquets</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<%@ include file="../footer.jsp" %>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
