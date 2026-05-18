<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shop — Fiore Flower Shop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<%@ include file="header.jsp" %>

<!-- Page Hero -->
<div class="page-hero">
    <div class="page-hero-content">
        <h1>Our Flower Collection</h1>
        <p>Explore our exquisite selection of fresh and artificial bouquets</p>
    </div>
</div>

<!-- Flash messages -->
<c:if test="${not empty sessionScope.flashSuccess}">
    <div class="container mt-2">
        <div class="alert alert-success alert-dismissible">${sessionScope.flashSuccess}</div>
    </div>
    <% session.removeAttribute("flashSuccess"); %>
</c:if>
<c:if test="${not empty sessionScope.flashError}">
    <div class="container mt-2">
        <div class="alert alert-error alert-dismissible">${sessionScope.flashError}</div>
    </div>
    <% session.removeAttribute("flashError"); %>
</c:if>

<section class="section">
    <div class="container">
        <h2 class="section-title" style="text-align:left;margin-bottom:1rem;">Browse Our Collection</h2>

        <!-- Search bar -->
        <div class="search-bar">
            <span class="search-icon">🔍</span>
            <input type="text"
                   id="searchInput"
                   placeholder="Search for bouquets by name..."
                   value="${keyword}">
        </div>

        <!-- Category filter buttons -->
        <div class="filter-bar">
            <c:forEach var="cat" items="${['All','Real Flowers','Artificial Flowers','Mixed Bouquets','Seasonal']}">
                <button class="filter-btn ${activeCategory == cat ? 'active' : ''}"
                        data-category="${cat}">${cat}</button>
            </c:forEach>
        </div>

        <!-- Results grid -->
        <c:choose>
            <c:when test="${not empty bouquets}">
                <div class="grid-3">
                    <c:forEach var="b" items="${bouquets}">
                        <div class="bouquet-card">
                            <div class="card-img">
                                <img src="${pageContext.request.contextPath}/static/images/bouquets/${b.imagePath}"
                                     alt="${b.name}"
                                     onerror="this.src='${pageContext.request.contextPath}/static/images/bouquets/default.jpg'">
                                <c:if test="${b.featured}">
                                    <span class="card-badge">Featured</span>
                                </c:if>
                                <!-- Wishlist -->
                                <form method="post" action="${pageContext.request.contextPath}/wishlist">
                                    <input type="hidden" name="id"     value="${b.id}">
                                    <input type="hidden" name="action" value="add">
                                    <button type="submit" class="card-wishlist-btn" title="Add to wishlist">♡</button>
                                </form>
                            </div>
                            <div class="card-body">
                                <div class="card-name">${b.name}</div>
                                <div class="card-tags">
                                    <span class="tag">${b.category}</span>
                                    <c:if test="${not empty b.occasion}">
                                        <span class="tag">${b.occasion}</span>
                                    </c:if>
                                </div>
                                <p style="font-size:.85rem;color:var(--mid-gray);margin-bottom:.75rem;line-height:1.6;">
                                    ${b.description}
                                </p>
                                <div class="card-meta">
                                    <span class="card-price">${b.formattedPrice}</span>
                                    <span class="badge-stock ${b.inStock ? 'in-stock' : 'out-stock'}">
                                        ${b.inStock ? 'In Stock' : 'Out of Stock'}
                                    </span>
                                </div>
                                <c:choose>
                                    <c:when test="${b.inStock}">
                                        <button class="card-order-btn open-order-modal"
                                                data-id="${b.id}"
                                                data-name="${b.name}"
                                                data-price="${b.price}">
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
                <div class="no-results">
                    <div class="no-results-icon">🌸</div>
                    <p>No bouquets found matching your search.</p>
                    <a href="${pageContext.request.contextPath}/shop" class="btn btn-outline mt-2">View All</a>
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
                    <div style="background:var(--rose-light);border-radius:var(--radius-sm);padding:1rem;margin-bottom:1.25rem;">
                        <div style="font-weight:600;color:var(--dark);margin-bottom:.2rem;" id="modalBouquetName">—</div>
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
                        <textarea class="form-control" name="specialNote" rows="2"
                                  placeholder="Any special requests..."></textarea>
                    </div>
                    <div class="order-summary">
                        <span>Total:</span>
                        <strong id="modalTotal">NPR 0</strong>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block btn-lg">Confirm Order</button>
                </form>
            </c:when>
            <c:otherwise>
                <p style="text-align:center;color:var(--mid-gray);margin-bottom:1.5rem;">
                    Please log in to place an order.
                </p>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-block">Login to Order</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@ include file="footer.jsp" %>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
