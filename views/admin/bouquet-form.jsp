<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty bouquet ? 'Add' : 'Edit'} Bouquet — Fiore Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<div class="admin-layout">
    <%@ include file="sidebar.jsp" %>
    <main class="admin-main">
        <div class="admin-topbar">
            <h1>${empty bouquet ? 'Add New Bouquet' : 'Edit Bouquet'}</h1>
            <a href="${pageContext.request.contextPath}/admin/bouquets" class="btn btn-outline">← Back</a>
        </div>

        <div class="admin-card" style="max-width:700px;">
            <c:if test="${not empty error}">
                <div class="alert alert-error mb-2">${error}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/admin/bouquets">
                <input type="hidden" name="action" value="${empty bouquet ? 'add' : 'edit'}">
                <c:if test="${not empty bouquet}">
                    <input type="hidden" name="id" value="${bouquet.id}">
                </c:if>

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Bouquet Name *</label>
                        <input class="form-control" type="text" name="name"
                               value="${bouquet.name}" placeholder="e.g. Rose Romance" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Category *</label>
                        <select class="form-control" name="category" required>
                            <option value="">-- Select --</option>
                            <c:forEach var="cat" items="${['Real Flowers','Artificial Flowers','Mixed Bouquets','Seasonal']}">
                                <option value="${cat}" ${bouquet.category == cat ? 'selected' : ''}>${cat}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Description *</label>
                    <textarea class="form-control" name="description" rows="3"
                              placeholder="Brief description of the bouquet..." required>${bouquet.description}</textarea>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Price (NPR) *</label>
                        <input class="form-control" type="number" name="price" step="0.01" min="1"
                               value="${bouquet.price}" placeholder="e.g. 2500" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Stock Quantity *</label>
                        <input class="form-control" type="number" name="stockQuantity" min="0"
                               value="${bouquet.stockQuantity}" placeholder="e.g. 10" required>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Occasion</label>
                        <input class="form-control" type="text" name="occasion"
                               value="${bouquet.occasion}" placeholder="e.g. Wedding, Birthday">
                    </div>
                    <div class="form-group">
                        <label class="form-label">Image Filename</label>
                        <input class="form-control" type="text" name="imagePath"
                               value="${bouquet.imagePath}"
                               placeholder="e.g. rose-romance.jpg">
                        <span class="form-hint">
                            Place your image in: <code>static/images/bouquets/</code>
                        </span>
                    </div>
                </div>

                <c:if test="${not empty bouquet.imagePath}">
                    <div class="form-group">
                        <label class="form-label">Current Image</label><br>
                        <img src="${pageContext.request.contextPath}/static/images/bouquets/${bouquet.imagePath}"
                             id="imagePreview"
                             style="width:120px;height:120px;object-fit:cover;border-radius:12px;border:2px solid var(--rose);"
                             onerror="this.style.display='none'">
                    </div>
                </c:if>
                <c:if test="${empty bouquet}">
                    <div class="form-group">
                        <img id="imagePreview" style="display:none;width:120px;height:120px;
                             object-fit:cover;border-radius:12px;border:2px solid var(--rose);" alt="Preview">
                    </div>
                </c:if>

                <div class="form-check">
                    <input type="checkbox" id="featured" name="featured"
                           ${bouquet.featured ? 'checked' : ''}>
                    <label for="featured">Mark as Featured (shows on homepage)</label>
                </div>

                <div style="display:flex;gap:1rem;margin-top:1rem;">
                    <button type="submit" class="btn btn-primary btn-lg">
                        ${empty bouquet ? 'Add Bouquet' : 'Save Changes'}
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/bouquets" class="btn btn-outline btn-lg">Cancel</a>
                </div>
            </form>
        </div>
    </main>
</div>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
