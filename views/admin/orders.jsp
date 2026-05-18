<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Orders — Fiore Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<div class="admin-layout">
    <%@ include file="sidebar.jsp" %>
    <main class="admin-main">
        <div class="admin-topbar">
            <h1>Manage Orders</h1>
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
                            <th>#</th><th>Customer</th><th>Bouquet</th>
                            <th>Qty</th><th>Total</th><th>Note</th>
                            <th>Status</th><th>Date</th><th>Update Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="o" items="${orders}" varStatus="s">
                            <tr>
                                <td>${s.count}</td>
                                <td style="font-weight:500;">${o.userName}</td>
                                <td>
                                    <div style="display:flex;align-items:center;gap:.5rem;">
                                        <img src="${pageContext.request.contextPath}/static/images/bouquets/${o.bouquetImage}"
                                             style="width:36px;height:36px;border-radius:6px;object-fit:cover;"
                                             onerror="this.style.display='none'" alt="">
                                        ${o.bouquetName}
                                    </div>
                                </td>
                                <td>${o.quantity}</td>
                                <td style="color:var(--primary);font-weight:600;">${o.formattedTotal}</td>
                                <td style="font-size:.82rem;color:var(--mid-gray);">${not empty o.specialNote ? o.specialNote : '—'}</td>
                                <td><span class="badge badge-${o.status}">${o.status}</span></td>
                                <td style="font-size:.78rem;color:var(--mid-gray);">${o.orderedAt}</td>
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/orders">
                                        <input type="hidden" name="id" value="${o.id}">
                                        <select name="status" class="form-control" style="padding:.3rem .5rem;font-size:.8rem;width:auto;">
                                            <option value="pending"   ${o.status=='pending'   ? 'selected':''}>Pending</option>
                                            <option value="confirmed" ${o.status=='confirmed' ? 'selected':''}>Confirmed</option>
                                            <option value="delivered" ${o.status=='delivered' ? 'selected':''}>Delivered</option>
                                            <option value="cancelled" ${o.status=='cancelled' ? 'selected':''}>Cancelled</option>
                                        </select>
                                        <button class="btn btn-primary btn-sm" style="margin-top:.3rem;">Update</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty orders}">
                            <tr><td colspan="9" style="text-align:center;color:var(--mid-gray);padding:2rem;">No orders yet.</td></tr>
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
