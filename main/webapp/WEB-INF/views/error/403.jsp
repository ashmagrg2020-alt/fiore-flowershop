<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Access Denied — Fiore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<div style="min-height:100vh;display:flex;align-items:center;justify-content:center;text-align:center;padding:2rem;">
    <div>
        <div style="font-size:5rem;margin-bottom:1rem;">🚫</div>
        <h1 style="font-family:'Cormorant Garamond',serif;font-size:3rem;color:var(--primary);margin-bottom:.5rem;">403</h1>
        <h2 style="font-size:1.3rem;color:var(--dark);margin-bottom:1rem;">Access Denied</h2>
        <p style="color:var(--mid-gray);margin-bottom:2rem;">You don't have permission to access this page.</p>
        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary btn-lg">Go Home</a>
    </div>
</div>
</body>
</html>
