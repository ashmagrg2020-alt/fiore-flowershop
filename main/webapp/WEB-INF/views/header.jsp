<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    // Determine the active nav link from request URI
    String uri = request.getRequestURI();
    String ctx = request.getContextPath();
    // Count wishlist items from session
    java.util.Set<?> wl = (java.util.Set<?>) session.getAttribute("wishlist");
    int wishlistCount = (wl != null) ? wl.size() : 0;
%>
<nav class="navbar">
    <div class="container">

        <!-- Brand / Logo -->
        <a href="<%= ctx %>/home" class="navbar-brand">
            <img src="<%= ctx %>/static/images/logo.png"
                 alt="Fiore Logo"
                 class="logo-img"
                 onerror="this.style.display='none'">
            Fiore
        </a>

        <!-- Nav links -->
        <ul class="navbar-nav" id="navLinks">
            <li><a href="<%= ctx %>/home"    class="<%= uri.contains("/home")    || uri.equals(ctx+"/") ? "active" : "" %>">Home</a></li>
            <li><a href="<%= ctx %>/shop"    class="<%= uri.contains("/shop")    ? "active" : "" %>">Shop</a></li>
            <li><a href="<%= ctx %>/about"   class="<%= uri.contains("/about")   ? "active" : "" %>">About</a></li>
            <li><a href="<%= ctx %>/contact" class="<%= uri.contains("/contact") ? "active" : "" %>">Contact</a></li>

            <c:if test="${not empty sessionScope.loggedInUser}">
                <li><a href="<%= ctx %>/orders"   class="<%= uri.contains("/orders")  ? "active" : "" %>">My Orders</a></li>
                <li><a href="<%= ctx %>/wishlist"  class="<%= uri.contains("/wishlist") ? "active" : "" %>">
                    Wishlist
                    <% if (wishlistCount > 0) { %><span class="wishlist-count"><%= wishlistCount %></span><% } %>
                </a></li>
                <li><a href="<%= ctx %>/profile"  class="<%= uri.contains("/profile") ? "active" : "" %>">Profile</a></li>
                <c:if test="${sessionScope.userRole == 'admin'}">
                    <li><a href="<%= ctx %>/admin/dashboard">⚙ Admin</a></li>
                </c:if>
            </c:if>
        </ul>

        <!-- Action buttons -->
        <div class="navbar-actions">
            <c:choose>
                <c:when test="${not empty sessionScope.loggedInUser}">
                    <span style="font-size:.85rem;color:var(--dark-gray);margin-right:.5rem;">
                        Hi, <strong>${sessionScope.userName}</strong>
                    </span>
                    <a href="<%= ctx %>/logout" class="btn btn-outline btn-sm">Logout</a>
                </c:when>
                <c:otherwise>
                    <a href="<%= ctx %>/login"    class="btn btn-outline btn-sm">Login</a>
                    <a href="<%= ctx %>/register" class="btn btn-primary btn-sm">Register</a>
                </c:otherwise>
            </c:choose>

            <!-- Hamburger (mobile) -->
            <button class="hamburger" id="hamburger" aria-label="Toggle menu">
                <span></span><span></span><span></span>
            </button>
        </div>

    </div>
</nav>
