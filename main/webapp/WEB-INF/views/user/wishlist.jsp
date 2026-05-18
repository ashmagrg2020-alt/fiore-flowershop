<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Wishlist — Fiore Flower Shop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<%@ include file="../header.jsp" %>

<div class="page-hero">
    <div class="page-hero-content">
        <h1>My Wishlist</h1>
        <p>Your saved bouquets</p>
    </div>
</div>

<section class="section">
    <div class="container">
        <c:choose>
            <c:when test="${not empty wishlistItems}">
                <div class="grid-3">
                    <c:forEach var="b" items="${wishlistItems}">
                        <div class="bouquet-card">
                            <div class="card-img">
                                <img src="${pageContext.request.contextPath}/static/images/bouquets/${b.imagePath}"
                                     alt="${b.name}"
                                     onerror="this.src='${pageContext.request.contextPath}/static/images/bouquets/default.jpg'">
                                <!-- Remove from wishlist -->
                                <form method="post" action="${pageContext.request.contextPath}/wishlist">
                                    <input type="hidden" name="id"     value="${b.id}">
                                    <input type="hidden" name="action" value="remove">
                                    <button type="submit" class="card-wishlist-btn active" title="Remove from wishlist">♥</button>
                                </form>
                            </div>
                            <div class="card-body">
                                <div class="card-name">${b.name}</div>
                                <div class="card-tags">
                                    <span class="tag">${b.category}</span>
                                    <c:if test="${not empty b.occasion}"><span class="tag">${b.occasion}</span></c:if>
                                </div>
                                <div class="card-meta">
                                    <span class="card-price">${b.formattedPrice}</span>
                                    <span class="badge-stock ${b.inStock ? 'in-stock' : 'out-stock'}">
                                        ${b.inStock ? 'In Stock' : 'Out of Stock'}
                                    </span>
                                </div>
                                <c:choose>
                                    <c:when test="${b.inStock}">
                                        <button class="card-order-btn open-order-modal"
                                                data-id="${b.id}" data-name="${b.name}" data-price="${b.price}">
                                            Order Now
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="card-order-btn" disabled>Out of Stock</button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="wishlist-empty">
                    <div class="empty-icon">♡</div>
                    <p>Your wishlist is empty.</p>
                    <a href="${pageContext.request.contextPath}/shop" class="btn btn-primary mt-2">Browse Bouquets</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<!-- Order Modal -->
<div class="modal-overlay" id="orderModal">
    <div class="modal-box">
        <div class="modal-header">
            <h3>Order Bouquet</h3>
            <button class="modal-close close-modal">✕</button>
        </div>
        <c:choose>
            <c:when test="${not empty sessionScope.loggedInUser}">
                <form method="post" action="${pageContext.request.contextPath}/order">
                    <input type="hidden" name="bouquetId" id="modalBouquetId">
                    <div style="background:var(--rose-light);border-radius:8px;padding:1rem;margin-bottom:1.25rem;">
                        <div style="font-weight:600;" id="modalBouquetName">—</div>
                        <div style="color:var(--primary);font-weight:600;" id="modalBouquetPrice">NPR 0</div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Quantity</label>
                        <div class="quantity-control">
                            <button type="button" class="qty-btn" id="qtyMinus">−</button>
                            <input type="number" class="form-control" id="modalQuantity" name="quantity"
                                   value="1" min="1" style="width:70px;text-align:center;">
                            <button type="button" class="qty-btn" id="qtyPlus">+</button>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Special Note <small>(optional)</small></label>
                        <textarea class="form-control" name="specialNote" rows="2"></textarea>
                    </div>
                    <div class="order-summary">
                        <span>Total:</span>
                        <strong id="modalTotal">NPR 0</strong>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block btn-lg">Confirm Order</button>
                </form>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-block">Login to Order</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@ include file="../footer.jsp" %>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
