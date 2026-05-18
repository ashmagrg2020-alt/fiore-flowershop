/**
 * Fiore Flower Shop — main.js
 * Handles: hamburger menu, password toggle, order modal,
 *           wishlist buttons, filter tabs, quantity control,
 *           flash message auto-dismiss, form validation hints.
 */

document.addEventListener('DOMContentLoaded', function () {

    /* ── Hamburger / Mobile nav ─────────────────────────────────── */
    const hamburger = document.getElementById('hamburger');
    const navLinks  = document.getElementById('navLinks');
    if (hamburger && navLinks) {
        hamburger.addEventListener('click', function () {
            navLinks.classList.toggle('mobile-open');
        });
        // Close on outside click
        document.addEventListener('click', function (e) {
            if (!hamburger.contains(e.target) && !navLinks.contains(e.target)) {
                navLinks.classList.remove('mobile-open');
            }
        });
    }

    /* ── Password toggle ────────────────────────────────────────── */
    document.querySelectorAll('.pw-toggle').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var input = document.getElementById(btn.dataset.target);
            if (!input) return;
            if (input.type === 'password') {
                input.type = 'text';
                btn.textContent = '🙈';
            } else {
                input.type = 'password';
                btn.textContent = '👁️';
            }
        });
    });

    /* ── Order Modal ────────────────────────────────────────────── */
    var orderModal    = document.getElementById('orderModal');
    var modalBqId     = document.getElementById('modalBouquetId');
    var modalBqName   = document.getElementById('modalBouquetName');
    var modalBqPrice  = document.getElementById('modalBouquetPrice');
    var modalQty      = document.getElementById('modalQuantity');
    var modalTotal    = document.getElementById('modalTotal');
    var unitPriceData = 0;

    document.querySelectorAll('.open-order-modal').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var id    = btn.dataset.id;
            var name  = btn.dataset.name;
            var price = parseFloat(btn.dataset.price);
            unitPriceData = price;

            if (modalBqId)    modalBqId.value       = id;
            if (modalBqName)  modalBqName.textContent = name;
            if (modalBqPrice) modalBqPrice.textContent = 'NPR ' + price.toLocaleString('en-IN');
            if (modalQty)     modalQty.value          = 1;
            if (modalTotal)   modalTotal.textContent   = 'NPR ' + price.toLocaleString('en-IN');

            if (orderModal) orderModal.classList.add('open');
        });
    });

    // Quantity buttons inside modal
    var qtyMinus = document.getElementById('qtyMinus');
    var qtyPlus  = document.getElementById('qtyPlus');
    if (qtyMinus && qtyPlus && modalQty) {
        qtyMinus.addEventListener('click', function () {
            var v = parseInt(modalQty.value, 10);
            if (v > 1) {
                modalQty.value = v - 1;
                updateTotal();
            }
        });
        qtyPlus.addEventListener('click', function () {
            modalQty.value = parseInt(modalQty.value, 10) + 1;
            updateTotal();
        });
        modalQty.addEventListener('input', updateTotal);
    }

    function updateTotal() {
        if (!modalTotal || !modalQty) return;
        var qty   = Math.max(1, parseInt(modalQty.value, 10) || 1);
        var total = qty * unitPriceData;
        modalTotal.textContent = 'NPR ' + total.toLocaleString('en-IN');
    }

    // Close modal buttons
    document.querySelectorAll('.close-modal, .modal-overlay').forEach(function (el) {
        el.addEventListener('click', function (e) {
            if (e.target === el) {
                if (orderModal) orderModal.classList.remove('open');
            }
        });
    });
    document.querySelectorAll('.close-modal').forEach(function (btn) {
        btn.addEventListener('click', function () {
            if (orderModal) orderModal.classList.remove('open');
        });
    });

    /* ── Wishlist toggle (session-based, form POST) ─────────────── */
    document.querySelectorAll('.wishlist-toggle').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            var form = btn.closest('form') || btn.parentElement.querySelector('form');
            if (form) {
                form.submit();
            }
        });
    });

    /* ── Category filter tabs (shop page) ──────────────────────── */
    document.querySelectorAll('.filter-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var category = btn.dataset.category || 'All';
            var searchInput = document.getElementById('searchInput');
            var keyword = searchInput ? searchInput.value : '';
            var url = window.location.pathname
                    + '?category=' + encodeURIComponent(category)
                    + '&search='   + encodeURIComponent(keyword);
            window.location.href = url;
        });
    });

    /* ── Search form — submit on Enter ─────────────────────────── */
    var searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('keydown', function (e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                var active = document.querySelector('.filter-btn.active');
                var cat    = active ? active.dataset.category : 'All';
                window.location.href = window.location.pathname
                    + '?search='   + encodeURIComponent(searchInput.value)
                    + '&category=' + encodeURIComponent(cat);
            }
        });
    }

    /* ── Admin: confirm delete ──────────────────────────────────── */
    document.querySelectorAll('.confirm-delete').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            if (!confirm('Are you sure you want to delete this item? This action cannot be undone.')) {
                e.preventDefault();
            }
        });
    });

    /* ── Auto-dismiss alert / flash messages ────────────────────── */
    document.querySelectorAll('.alert-dismissible').forEach(function (alert) {
        setTimeout(function () {
            alert.style.opacity = '0';
            alert.style.transition = 'opacity 0.5s';
            setTimeout(function () { alert.remove(); }, 500);
        }, 4000);
    });

    /* ── Admin sidebar active link ──────────────────────────────── */
    var currentPath = window.location.pathname;
    document.querySelectorAll('.admin-nav-item').forEach(function (item) {
        if (item.getAttribute('href') && currentPath.startsWith(item.getAttribute('href'))) {
            item.classList.add('active');
        }
    });

    /* ── Image preview on file input (admin bouquet form) ──────── */
    var imgFileInput = document.getElementById('imageFile');
    var imgPreview   = document.getElementById('imagePreview');
    if (imgFileInput && imgPreview) {
        imgFileInput.addEventListener('change', function () {
            var file = imgFileInput.files[0];
            if (file) {
                var reader = new FileReader();
                reader.onload = function (ev) {
                    imgPreview.src = ev.target.result;
                    imgPreview.style.display = 'block';
                };
                reader.readAsDataURL(file);
            }
        });
    }

    /* ── Client-side form validation ───────────────────────────── */
    var registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', function (e) {
            var name = document.getElementById('fullName');
            if (name && /\d/.test(name.value)) {
                e.preventDefault();
                showFieldError(name, 'Name must not contain numbers.');
            }
        });
    }

    function showFieldError(input, msg) {
        var err = input.parentElement.querySelector('.form-error');
        if (!err) {
            err = document.createElement('span');
            err.className = 'form-error';
            input.parentElement.appendChild(err);
        }
        err.textContent = msg;
        input.classList.add('is-invalid');
        input.focus();
    }
});
