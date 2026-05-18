<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fiore Flower Shop — Where Every Petal Tells a Story</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<%@ include file="header.jsp" %>

<!-- ══════════════════════════════════════════════════════════
     HERO SECTION
     Place your banner image at: static/images/banner.jpg
     ══════════════════════════════════════════════════════════ -->
<section class="hero">
    <%-- Replace the gradient background with your image by uncommenting below --%>
    <%-- <img src="${pageContext.request.contextPath}/static/images/banner.jpg"
             alt="Fiore Banner" class="hero-banner-img"> --%>
    <div class="hero-content">
        <h1>Where Every Petal Tells a Story</h1>
        <p>Discover exquisite bouquets crafted with love, perfect for every special moment</p>
        <div class="hero-btns">
            <a href="${pageContext.request.contextPath}/shop" class="btn btn-primary btn-lg">Shop Now</a>
            <a href="${pageContext.request.contextPath}/shop" class="btn btn-outline-white btn-lg">Browse Collections</a>
        </div>
    </div>
</section>

<!-- ══════════════════════════════════════════════════════════
     FEATURED BOUQUETS
     ══════════════════════════════════════════════════════════ -->
<section class="section">
    <div class="container">
        <h2 class="section-title">Featured Bouquets</h2>
        <p class="section-subtitle">Handpicked selections for every occasion</p>

        <c:choose>
            <c:when test="${not empty featuredBouquets}">
                <div style="display:flex; justify-content:center; gap:1.5rem; flex-wrap:wrap;">
                    <c:forEach var="b" items="${featuredBouquets}">
                        <div class="bouquet-card">
                            <div class="card-img">
                                <img src="${pageContext.request.contextPath}/static/images/bouquets/${b.imagePath}"
                                     alt="${b.name}"
                                     onerror="this.src='${pageContext.request.contextPath}/static/images/bouquets/default.jpg'">
                                <c:if test="${b.featured}">
                                    <span class="card-badge">Featured</span>
                                </c:if>
                                <!-- Wishlist button -->
                                <form method="post" action="${pageContext.request.contextPath}/wishlist" style="display:inline;">
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
                    <p>No featured bouquets yet. Check back soon!</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<!-- ══════════════════════════════════════════════════════════
     BROWSE BY CATEGORY
     ══════════════════════════════════════════════════════════ -->
<section class="section section--shaded">
    <div class="container">
        <h2 class="section-title">Browse by Category</h2>
        <p class="section-subtitle">Find the perfect bouquet for any occasion</p>
        <div style="display:flex; justify-content:center; gap:1.25rem; flex-wrap:wrap;">
            <a href="${pageContext.request.contextPath}/shop?category=Real+Flowers"
               style="background:var(--rose);border-radius:20px;padding:2rem 1rem;text-align:center;border:1px solid var(--rose-mid);cursor:pointer;width:200px;display:block;">
                <div style="font-size:2.5rem;margin-bottom:.75rem;">🌹</div>
                <div style="font-family:'Cormorant Garamond',serif;font-size:1.15rem;font-weight:600;color:#2C1A1F;">Real Flowers</div>
            </a>
            <a href="${pageContext.request.contextPath}/shop?category=Artificial+Flowers"
               style="background:var(--rose);border-radius:20px;padding:2rem 1rem;text-align:center;border:1px solid var(--rose-mid);cursor:pointer;width:200px;display:block;">
                <div style="font-size:2.5rem;margin-bottom:.75rem;">✨</div>
                <div style="font-family:'Cormorant Garamond',serif;font-size:1.15rem;font-weight:600;color:#2C1A1F;">Artificial Flowers</div>
            </a>
            <a href="${pageContext.request.contextPath}/shop?category=Mixed+Bouquets"
               style="background:var(--rose);border-radius:20px;padding:2rem 1rem;text-align:center;border:1px solid var(--rose-mid);cursor:pointer;width:200px;display:block;">
                <div style="font-size:2.5rem;margin-bottom:.75rem;">🎁</div>
                <div style="font-family:'Cormorant Garamond',serif;font-size:1.15rem;font-weight:600;color:#2C1A1F;">Mixed Bouquets</div>
            </a>
            <a href="${pageContext.request.contextPath}/shop?category=Seasonal"
               style="background:var(--rose);border-radius:20px;padding:2rem 1rem;text-align:center;border:1px solid var(--rose-mid);cursor:pointer;width:200px;display:block;">
                <div style="font-size:2.5rem;margin-bottom:.75rem;">📅</div>
                <div style="font-family:'Cormorant Garamond',serif;font-size:1.15rem;font-weight:600;color:#2C1A1F;">Seasonal Specials</div>
            </a>
        </div>
    </div>
</section>

<!-- ══════════════════════════════════════════════════════════
     ORDER MODAL (shared — also used on shop page)
     ══════════════════════════════════════════════════════════ -->
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

                    <div style="display:flex;gap:1rem;align-items:center;margin-bottom:1.5rem;
                                background:var(--rose-light);border-radius:var(--radius-sm);padding:1rem;">
                        <div>
                            <div style="font-weight:600;color:var(--dark);margin-bottom:.25rem;"
                                 id="modalBouquetName">—</div>
                            <div style="color:var(--primary);font-weight:600;font-size:1.05rem;"
                                 id="modalBouquetPrice">NPR 0</div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Quantity</label>
                        <div class="quantity-control">
                            <button type="button" class="qty-btn" id="qtyMinus">−</button>
                            <input type="number" class="form-control qty-value"
                                   id="modalQuantity" name="quantity"
                                   value="1" min="1" style="width:70px;text-align:center;">
                            <button type="button" class="qty-btn" id="qtyPlus">+</button>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Special Note <small>(optional)</small></label>
                        <textarea class="form-control" name="specialNote" rows="2"
                                  placeholder="Any special requests or message..."></textarea>
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
                <a href="${pageContext.request.contextPath}/login"
                   class="btn btn-primary btn-block">Login to Order</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@ include file="footer.jsp" %>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
