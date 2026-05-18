<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<footer class="footer">
    <div class="container">
        <div class="footer-grid">

            <!-- Brand -->
            <div>
                <div class="footer-brand">
                    <img src="${pageContext.request.contextPath}/static/images/logo.png"
                         alt="Fiore Logo"
                         onerror="this.style.display='none'">
                    Fiore
                </div>
                <p class="footer-tagline">Where Every Petal Tells a Story</p>
            </div>

            <!-- Navigation -->
            <div>
                <div class="footer-heading">Navigation</div>
                <a href="${pageContext.request.contextPath}/home"    class="footer-link">Home</a>
                <a href="${pageContext.request.contextPath}/shop"    class="footer-link">Shop</a>
                <a href="${pageContext.request.contextPath}/about"   class="footer-link">About</a>
                <a href="${pageContext.request.contextPath}/contact" class="footer-link">Contact</a>
            </div>

            <!-- Contact -->
            <div>
                <div class="footer-heading">Contact</div>
                <p class="footer-text">Lakeside, Pokhara</p>
                <p class="footer-text">Pokhara, Nepal</p>
                <p class="footer-text">+977 123 456 789</p>
                <p class="footer-text">hello@fiore.com</p>
            </div>

            <!-- Working Hours -->
            <div>
                <div class="footer-heading">Working Hours</div>
                <p class="footer-text">Monday – Friday</p>
                <p class="footer-text">9:00 AM – 6:00 PM</p>
                <br>
                <p class="footer-text">Saturday</p>
                <p class="footer-text">10:00 AM – 4:00 PM</p>
                <br>
                <p class="footer-text">Sunday: Closed</p>
            </div>

        </div>
        <hr class="footer-divider">
        <p class="footer-bottom">© 2026 Fiore Flower Shop. All rights reserved.</p>
    </div>
</footer>
