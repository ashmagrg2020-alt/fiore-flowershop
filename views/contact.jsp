<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact — Fiore Flower Shop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<%@ include file="header.jsp" %>

<div class="page-hero">
    <div class="page-hero-content">
        <h1>Get in Touch</h1>
        <p>We'd love to hear from you</p>
    </div>
</div>

<section class="section">
    <div class="container">
        <div class="contact-grid">

            <!-- ── Contact Form ── -->
            <div class="contact-form-card">
                <h3 style="font-family:'Cormorant Garamond',serif;font-size:1.6rem;
                           color:var(--dark);margin-bottom:1.5rem;">Send us a Message</h3>

                <c:if test="${not empty success}">
                    <div class="alert alert-success alert-dismissible">${success}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-error alert-dismissible">${error}</div>
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/contact">
                    <div class="form-group">
                        <label class="form-label" for="name">Name</label>
                        <input class="form-control" type="text" id="name" name="name"
                               placeholder="Your name" value="${formName}" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="email">Email</label>
                        <input class="form-control" type="email" id="email" name="email"
                               placeholder="your.email@example.com" value="${formEmail}" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="subject">Subject</label>
                        <input class="form-control" type="text" id="subject" name="subject"
                               placeholder="How can we help?" value="${formSubject}" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="message">Message</label>
                        <textarea class="form-control" id="message" name="message" rows="5"
                                  placeholder="Tell us more about your inquiry..."
                                  required>${formMessage}</textarea>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block btn-lg">Send Message</button>
                </form>
            </div>

            <!-- ── Contact Info ── -->
            <div class="contact-info">
                <h3>Contact Information</h3>

                <div class="contact-item">
                    <div class="contact-item-icon">📍</div>
                    <div>
                        <div class="contact-item-title">Address</div>
                        <div class="contact-item-text">Lakeside, Pokhara<br>Pokhara, Nepal</div>
                    </div>
                </div>

                <div class="contact-item">
                    <div class="contact-item-icon">📞</div>
                    <div>
                        <div class="contact-item-title">Phone</div>
                        <div class="contact-item-text">+977 123 456 789</div>
                    </div>
                </div>

                <div class="contact-item">
                    <div class="contact-item-icon">✉️</div>
                    <div>
                        <div class="contact-item-title">Email</div>
                        <div class="contact-item-text">hello@fiore.com</div>
                    </div>
                </div>

                <div class="contact-item">
                    <div class="contact-item-icon">🕐</div>
                    <div>
                        <div class="contact-item-title">Working Hours</div>
                        <div class="contact-item-text">
                            Monday – Friday: 9:00 AM – 6:00 PM<br>
                            Saturday: 10:00 AM – 4:00 PM<br>
                            Sunday: Closed
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</section>

<%@ include file="footer.jsp" %>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
