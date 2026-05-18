<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About Us — Fiore Flower Shop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<%@ include file="header.jsp" %>

<!-- Page Hero -->
<div class="page-hero">
    <div class="page-hero-content">
        <h1>About Fiore</h1>
        <p>Crafting beautiful moments through the art of flowers</p>
    </div>
</div>

<!-- ══ Our Story ════════════════════════════════════════════════ -->
<section class="section">
    <div class="container">
        <div class="about-story-grid">
            <div>
                <h2 style="font-family:'Cormorant Garamond',serif;font-size:2rem;font-weight:500;
                           color:var(--dark);margin-bottom:1rem;">Our Story</h2>
                <p style="color:var(--mid-gray);line-height:1.85;margin-bottom:1rem;font-size:.96rem;">
                    Founded in 2020, Fiore Flower Shop was born from a passion for bringing beauty and joy
                    into people's lives through the art of floral arrangement. What started as a small
                    boutique in the heart of Lakeside, Pokhara has grown into a beloved destination for
                    those seeking the perfect bouquet.
                </p>
                <p style="color:var(--mid-gray);line-height:1.85;font-size:.96rem;">
                    We believe that every petal tells a story, and every arrangement should be as unique
                    as the moment it celebrates. Whether it is a wedding, an anniversary, or a simple
                    gesture of appreciation, we are here to help you express what words cannot.
                </p>
            </div>
            <div class="about-quote">
                <div class="quote-icon">🌸</div>
                <p>"Where Every Petal Tells a Story"</p>
            </div>
        </div>
    </div>
</section>

<!-- ══ Our Mission ══════════════════════════════════════════════ -->
<section class="section section--shaded">
    <div class="container">
        <h2 class="section-title">Our Mission</h2>
        <p class="section-subtitle" style="max-width:680px;margin:0 auto 2.5rem;">
            To make every floral experience exceptional — sourcing only the finest blooms from trusted
            growers, combining artistry with care, and delivering arrangements that inspire and delight.
        </p>

        <div style="display:flex; justify-content:center; gap:1.5rem; flex-wrap:wrap; margin-bottom:3rem;">
            <div style="background:var(--rose-light);border-radius:20px;padding:2rem;text-align:center;border:1px solid var(--rose);width:220px;">
                <div style="font-size:2.4rem;margin-bottom:.75rem;">🏆</div>
                <h3 style="font-family:'Jost',sans-serif;font-size:1rem;font-weight:600;margin-bottom:.5rem;">Quality</h3>
                <p style="font-size:.85rem;color:var(--mid-gray);line-height:1.7;">We source only the finest flowers from trusted growers to ensure every bouquet is pristine and long-lasting.</p>
            </div>
            <div style="background:var(--rose-light);border-radius:20px;padding:2rem;text-align:center;border:1px solid var(--rose);width:220px;">
                <div style="font-size:2.4rem;margin-bottom:.75rem;">💛</div>
                <h3 style="font-family:'Jost',sans-serif;font-size:1rem;font-weight:600;margin-bottom:.5rem;">Care</h3>
                <p style="font-size:.85rem;color:var(--mid-gray);line-height:1.7;">Every bouquet is crafted with love and meticulous attention to detail, from the first stem to the final ribbon.</p>
            </div>
            <div style="background:var(--rose-light);border-radius:20px;padding:2rem;text-align:center;border:1px solid var(--rose);width:220px;">
                <div style="font-size:2.4rem;margin-bottom:.75rem;">✨</div>
                <h3 style="font-family:'Jost',sans-serif;font-size:1rem;font-weight:600;margin-bottom:.5rem;">Beauty</h3>
                <p style="font-size:.85rem;color:var(--mid-gray);line-height:1.7;">We create arrangements that inspire, delight, and leave a lasting impression on every recipient.</p>
            </div>
        </div>
    </div>
</section>

<!-- ══ Meet the Team ════════════════════════════════════════════ -->
<section class="section">
    <div class="container">
        <h2 class="section-title">Meet the Team</h2>
        <p class="section-subtitle">The passionate people behind every beautiful arrangement</p>

        <div class="team-grid">

            <!-- Team Member 1 -->
            <div class="team-card">
                <div class="team-avatar">
                    <%-- Place photo at: static/images/team/ashma.jpg --%>
                    <img src="${pageContext.request.contextPath}/static/images/team/ashma.jpg"
                         alt="Ashma Gurung"
                         onerror="this.outerHTML='<span style=\'font-size:3rem;\'>👩</span>'">
                </div>
                <div class="team-name">Ashma Gurung</div>
                <div class="team-role">Founder &amp; Head Florist</div>
            </div>

            <!-- Team Member 2 -->
            <div class="team-card">
                <div class="team-avatar">
                    <img src="${pageContext.request.contextPath}/static/images/team/shreya.jpg"
                         alt="Shreya Gurung"
                         onerror="this.outerHTML='<span style=\'font-size:3rem;\'>👩‍🎨</span>'">
                </div>
                <div class="team-name">Shreya Gurung</div>
                <div class="team-role">Creative Director</div>
            </div>

            <!-- Team Member 3 -->
            <div class="team-card">
                <div class="team-avatar">
                    <img src="${pageContext.request.contextPath}/static/images/team/sadrishya.jpg"
                         alt="Sadrishya Shrestha"
                         onerror="this.outerHTML='<span style=\'font-size:3rem;\'>👩‍💼</span>'">
                </div>
                <div class="team-name">Sadrishya Shrestha</div>
                <div class="team-role">Customer Relations</div>
            </div>

            <!-- Team Member 4 -->
            <div class="team-card">
                <div class="team-avatar">
                    <img src="${pageContext.request.contextPath}/static/images/team/supriya.jpg"
                         alt="Supriya Gurung"
                         onerror="this.outerHTML='<span style=\'font-size:3rem;\'>👩‍💻</span>'">
                </div>
                <div class="team-name">Supriya Gurung</div>
                <div class="team-role">Operations Manager</div>
            </div>

            <!-- Team Member 5 -->
            <div class="team-card">
                <div class="team-avatar">
                    <img src="${pageContext.request.contextPath}/static/images/team/sainma.jpg"
                         alt="Sainma Gurung"
                         onerror="this.outerHTML='<span style=\'font-size:3rem;\'>📣</span>'">
                </div>
                <div class="team-name">Sainma Gurung</div>
                <div class="team-role">Marketing Specialist</div>
            </div>

        </div>
    </div>
</section>

<%@ include file="footer.jsp" %>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
