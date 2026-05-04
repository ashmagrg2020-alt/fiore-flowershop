/* BloomCart Flower Shop - main.js */
'use strict';

/* ── Utilities ───────────────────────────────── */
const $ = (sel, ctx = document) => ctx.querySelector(sel);
const $$ = (sel, ctx = document) => [...ctx.querySelectorAll(sel)];

/* ── DOM Ready ───────────────────────────────── */
document.addEventListener('DOMContentLoaded', () => {
  initNavbar();
  initAlerts();
  initPasswordToggle();
  initCartQuantity();
  initWishlist();
  initToast();
  initSidebar();
  initFormValidation();
  initPreviewImage();
  initFilterToggle();
  initConfirmDialogs();
  initSearch();
  autoSubmitSorts();
});

/* ── Navbar Mobile ───────────────────────────── */
function initNavbar() {
  const hamburger = $('.hamburger');
  const navLinks  = $('.nav-links');
  if (!hamburger || !navLinks) return;

  hamburger.addEventListener('click', () => {
    const open = navLinks.classList.toggle('open');
    hamburger.setAttribute('aria-expanded', open);
    $$('.hamburger span').forEach((s, i) => {
      if (open) {
        if (i === 0) s.style.transform = 'rotate(45deg) translate(5px,5px)';
        if (i === 1) s.style.opacity = '0';
        if (i === 2) s.style.transform = 'rotate(-45deg) translate(5px,-5px)';
      } else {
        s.style.transform = ''; s.style.opacity = '';
      }
    });
  });

  document.addEventListener('click', (e) => {
    if (!e.target.closest('.navbar') && navLinks.classList.contains('open')) {
      navLinks.classList.remove('open');
      $$('.hamburger span').forEach(s => { s.style.transform = ''; s.style.opacity = ''; });
    }
  });
}

/* ── Alert Auto-dismiss ──────────────────────── */
function initAlerts() {
  $$('.alert .close-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const alert = btn.closest('.alert');
      alert.style.opacity = '0'; alert.style.transform = 'translateY(-6px)';
      alert.style.transition = '0.3s ease';
      setTimeout(() => alert.remove(), 320);
    });
  });
  $$('.alert[data-auto-dismiss]').forEach(alert => {
    const delay = parseInt(alert.dataset.autoDismiss) || 4000;
    setTimeout(() => {
      if (alert.isConnected) {
        alert.style.opacity = '0'; alert.style.transition = '0.4s ease';
        setTimeout(() => alert.remove(), 420);
      }
    }, delay);
  });
}

/* ── Password Toggle ─────────────────────────── */
function initPasswordToggle() {
  $$('.toggle-password').forEach(btn => {
    btn.addEventListener('click', () => {
      const input = btn.previousElementSibling || document.getElementById(btn.dataset.target);
      if (!input) return;
      const isPass = input.type === 'password';
      input.type = isPass ? 'text' : 'password';
      btn.textContent = isPass ? '🙈' : '👁';
    });
  });
}

/* ── Cart Quantity Controls ──────────────────── */
function initCartQuantity() {
  document.addEventListener('click', (e) => {
    if (e.target.matches('.qty-btn')) {
      const wrapper = e.target.closest('.qty-control');
      const input   = wrapper?.querySelector('.qty-input');
      if (!input) return;
      const max = parseInt(input.dataset.max) || 999;
      let val = parseInt(input.value) || 1;
      if (e.target.dataset.action === 'inc') val = Math.min(val + 1, max);
      if (e.target.dataset.action === 'dec') val = Math.max(val - 1, 1);
      input.value = val;
      input.dispatchEvent(new Event('change', { bubbles: true }));
    }
  });
}

/* ── Wishlist Toggle (session-based via AJAX) ── */
function initWishlist() {
  document.addEventListener('click', async (e) => {
    const btn = e.target.closest('.wishlist-btn');
    if (!btn) return;
    const flowerId = btn.dataset.flowerId;
    if (!flowerId) return;

    try {
      const res  = await fetch(`/wishlist?action=toggle&flowerId=${flowerId}`, { method: 'POST' });
      const data = await res.json();
      btn.classList.toggle('active', data.inWishlist);
      btn.title = data.inWishlist ? 'Remove from wishlist' : 'Add to wishlist';
      showToast(data.message || (data.inWishlist ? 'Added to wishlist' : 'Removed from wishlist'), data.inWishlist ? 'success' : 'warn');
      updateWishlistCount(data.count);
    } catch (err) {
      showToast('Please login to use wishlist', 'error');
    }
  });
}

function updateWishlistCount(count) {
  const badge = $('#wishlist-count');
  if (!badge) return;
  badge.textContent = count;
  badge.style.display = count > 0 ? 'flex' : 'none';
}

/* ── Toast Notifications ─────────────────────── */
function initToast() {
  if (!$('.toast-container')) {
    const c = document.createElement('div');
    c.className = 'toast-container';
    document.body.appendChild(c);
  }
}

function showToast(message, type = 'success') {
  const icons = { success: '✅', error: '❌', warn: '⚠️', info: 'ℹ️' };
  const container = $('.toast-container');
  if (!container) return;
  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  toast.innerHTML = `<span class="toast-icon">${icons[type] || 'ℹ️'}</span><span class="toast-msg">${message}</span>`;
  container.appendChild(toast);
  setTimeout(() => toast.remove(), 4200);
}
window.showToast = showToast;

/* ── Admin Sidebar ───────────────────────────── */
function initSidebar() {
  const toggle  = $('.sidebar-toggle');
  const sidebar = $('.sidebar');
  const overlay = $('.sidebar-overlay');
  if (!toggle || !sidebar) return;

  toggle.addEventListener('click', () => {
    const open = sidebar.classList.toggle('open');
    if (overlay) overlay.classList.toggle('open', open);
  });
  if (overlay) {
    overlay.addEventListener('click', () => {
      sidebar.classList.remove('open');
      overlay.classList.remove('open');
    });
  }
}

/* ── Client-side Form Validation ─────────────── */
function initFormValidation() {
  $$('form[data-validate]').forEach(form => {
    form.addEventListener('submit', (e) => {
      let valid = true;
      $$('[data-required]', form).forEach(field => {
        clearFieldError(field);
        if (!field.value.trim()) {
          setFieldError(field, field.dataset.required || 'This field is required');
          valid = false;
        }
      });
      $$('[data-email]', form).forEach(field => {
        if (field.value && !isValidEmail(field.value)) {
          setFieldError(field, 'Please enter a valid email address');
          valid = false;
        }
      });
      $$('[data-phone]', form).forEach(field => {
        if (field.value && !isValidPhone(field.value)) {
          setFieldError(field, 'Please enter a valid phone number');
          valid = false;
        }
      });
      $$('[data-min-length]', form).forEach(field => {
        const min = parseInt(field.dataset.minLength);
        if (field.value && field.value.length < min) {
          setFieldError(field, `Minimum ${min} characters required`);
          valid = false;
        }
      });
      $$('[data-match]', form).forEach(field => {
        const target = document.getElementById(field.dataset.match);
        if (target && field.value !== target.value) {
          setFieldError(field, 'Passwords do not match');
          valid = false;
        }
      });
      $$('[data-positive]', form).forEach(field => {
        if (field.value && parseFloat(field.value) <= 0) {
          setFieldError(field, 'Value must be positive');
          valid = false;
        }
      });
      $$('[data-no-negative]', form).forEach(field => {
        if (field.value && parseFloat(field.value) < 0) {
          setFieldError(field, 'Value cannot be negative');
          valid = false;
        }
      });
      $$('[data-no-numbers]', form).forEach(field => {
        if (field.value && /\d/.test(field.value)) {
          setFieldError(field, 'This field must not contain numbers');
          valid = false;
        }
      });
      if (!valid) e.preventDefault();
    });
  });
}

function setFieldError(field, msg) {
  field.classList.add('is-invalid');
  let err = field.parentElement.querySelector('.form-error');
  if (!err) {
    err = document.createElement('div');
    err.className = 'form-error';
    field.parentElement.appendChild(err);
  }
  err.innerHTML = `<span>⚠</span> ${msg}`;
}

function clearFieldError(field) {
  field.classList.remove('is-invalid', 'is-valid');
  const err = field.parentElement?.querySelector('.form-error');
  if (err) err.remove();
}

function isValidEmail(email) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function isValidPhone(phone) {
  return /^[\+]?[\d\s\-\(\)]{7,15}$/.test(phone);
}

/* Live validation feedback */
document.addEventListener('blur', (e) => {
  const field = e.target;
  if (!field.matches('input, select, textarea')) return;
  clearFieldError(field);
  if (field.dataset.required && !field.value.trim()) {
    setFieldError(field, field.dataset.required || 'Required');
  } else if (field.dataset.email && field.value && !isValidEmail(field.value)) {
    setFieldError(field, 'Invalid email');
  } else if (field.dataset.phone && field.value && !isValidPhone(field.value)) {
    setFieldError(field, 'Invalid phone');
  } else if (field.value) {
    field.classList.add('is-valid');
  }
}, true);

/* ── Image Preview ───────────────────────────── */
function initPreviewImage() {
  $$('input[type="file"][data-preview]').forEach(input => {
    const previewId = input.dataset.preview;
    const preview   = document.getElementById(previewId);
    if (!preview) return;
    input.addEventListener('change', () => {
      const file = input.files[0];
      if (!file) return;
      const reader = new FileReader();
      reader.onload = (e) => {
        if (preview.tagName === 'IMG') {
          preview.src = e.target.result;
          preview.style.display = 'block';
        } else {
          preview.style.backgroundImage = `url(${e.target.result})`;
        }
      };
      reader.readAsDataURL(file);
    });
  });
}

/* ── Filter Panel Toggle (mobile) ────────────── */
function initFilterToggle() {
  const btn   = $('#toggle-filters');
  const panel = $('.filter-panel');
  if (!btn || !panel) return;
  btn.addEventListener('click', () => {
    const open = panel.classList.toggle('open');
    btn.textContent = open ? 'Hide Filters' : 'Show Filters';
  });
}

/* ── Confirm Dialogs ─────────────────────────── */
function initConfirmDialogs() {
  document.addEventListener('click', (e) => {
    const btn = e.target.closest('[data-confirm]');
    if (!btn) return;
    const msg = btn.dataset.confirm || 'Are you sure?';
    if (!confirm(msg)) e.preventDefault();
  });

  document.addEventListener('submit', (e) => {
    const confirmMsg = e.target.dataset.confirm;
    if (confirmMsg && !confirm(confirmMsg)) e.preventDefault();
  });
}

/* ── Search Auto-clear ───────────────────────── */
function initSearch() {
  const clearBtn = $('#clear-search');
  const searchInput = $('#search-input');
  if (clearBtn && searchInput) {
    clearBtn.addEventListener('click', () => {
      searchInput.value = '';
      searchInput.form?.submit();
    });
    searchInput.addEventListener('input', () => {
      clearBtn.style.display = searchInput.value ? 'flex' : 'none';
    });
  }
}

/* ── Sort auto-submit ────────────────────────── */
function autoSubmitSorts() {
  $$('select[data-auto-submit]').forEach(sel => {
    sel.addEventListener('change', () => sel.form?.submit());
  });
}

/* ── Admin: Confirm delete forms ─────────────── */
function confirmDelete(formId) {
  if (confirm('Are you sure you want to delete this record? This action cannot be undone.')) {
    document.getElementById(formId).submit();
  }
}
window.confirmDelete = confirmDelete;

/* ── Cart: update on qty change ──────────────── */
document.addEventListener('change', (e) => {
  if (e.target.matches('.qty-input[data-auto-update]')) {
    const form = e.target.closest('form');
    if (form) form.submit();
  }
});

/* ── Admin stats count-up animation ──────────── */
function animateCountUp(el) {
  const target = parseFloat(el.dataset.target || el.textContent.replace(/[^0-9.]/g, ''));
  const isFloat = el.dataset.float === 'true';
  const prefix  = el.dataset.prefix || '';
  const suffix  = el.dataset.suffix || '';
  const duration = 1000;
  const start    = performance.now();

  function step(now) {
    const progress = Math.min((now - start) / duration, 1);
    const ease     = 1 - Math.pow(1 - progress, 3);
    const current  = target * ease;
    el.textContent = prefix + (isFloat ? current.toFixed(2) : Math.floor(current)) + suffix;
    if (progress < 1) requestAnimationFrame(step);
  }
  requestAnimationFrame(step);
}

const observer = new IntersectionObserver((entries) => {
  entries.forEach(entry => {
    if (entry.isIntersecting && !entry.target.dataset.animated) {
      entry.target.dataset.animated = 'true';
      animateCountUp(entry.target);
    }
  });
}, { threshold: 0.5 });

$$('.stat-value[data-target]').forEach(el => observer.observe(el));

/* ── Modal helpers ───────────────────────────── */
function openModal(id) {
  const modal = document.getElementById(id);
  if (modal) modal.classList.add('open');
}
function closeModal(id) {
  const modal = document.getElementById(id);
  if (modal) modal.classList.remove('open');
}
window.openModal  = openModal;
window.closeModal = closeModal;

document.addEventListener('click', (e) => {
  if (e.target.matches('.modal-overlay')) closeModal(e.target.id);
  if (e.target.matches('.modal-close'))  {
    const modal = e.target.closest('.modal-overlay');
    if (modal) modal.classList.remove('open');
  }
  if (e.target.dataset.openModal)  openModal(e.target.dataset.openModal);
  if (e.target.dataset.closeModal) closeModal(e.target.dataset.closeModal);
});

/* ── Flower gallery thumbnails ───────────────── */
$$('.thumb-img').forEach(thumb => {
  thumb.addEventListener('click', () => {
    const mainImg = document.getElementById('main-flower-img');
    if (mainImg) mainImg.src = thumb.src;
    $$('.thumb-img').forEach(t => t.classList.remove('active'));
    thumb.classList.add('active');
  });
});

/* ── Occasion/category filter pills ──────────── */
$$('.filter-pill').forEach(pill => {
  pill.addEventListener('click', () => {
    const group = pill.dataset.group;
    $$(`.filter-pill[data-group="${group}"]`).forEach(p => p.classList.remove('active'));
    pill.classList.add('active');
  });
});

/* ── Smooth scroll to anchor ─────────────────── */
$$('a[href^="#"]').forEach(link => {
  link.addEventListener('click', (e) => {
    const target = document.getElementById(link.getAttribute('href').slice(1));
    if (target) {
      e.preventDefault();
      target.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  });
});

/* ── Print order ─────────────────────────────── */
function printOrder() { window.print(); }
window.printOrder = printOrder;

/* ── Cart total live update ──────────────────── */
function recalcCart() {
  let total = 0;
  $$('.cart-item').forEach(item => {
    const price = parseFloat(item.dataset.price) || 0;
    const qty   = parseInt(item.querySelector('.qty-input')?.value || '1');
    total += price * qty;
  });
  const totalEl = document.getElementById('cart-total');
  if (totalEl) totalEl.textContent = 'Rs. ' + total.toFixed(2);
}
document.addEventListener('change', (e) => {
  if (e.target.matches('.cart-item .qty-input')) recalcCart();
});

/* ── Scroll-to-top button ────────────────────── */
(function() {
  const btn = document.createElement('button');
  btn.id = 'scroll-top';
  btn.innerHTML = '↑';
  btn.title = 'Back to top';
  btn.style.cssText = `
    position:fixed;bottom:24px;left:24px;z-index:800;
    width:40px;height:40px;border-radius:50%;border:none;
    background:var(--pink-dark);color:white;font-size:1rem;
    cursor:pointer;box-shadow:0 4px 14px rgba(194,98,122,0.35);
    opacity:0;transition:opacity 0.3s;pointer-events:none;
  `;
  document.body.appendChild(btn);
  window.addEventListener('scroll', () => {
    const show = window.scrollY > 300;
    btn.style.opacity = show ? '1' : '0';
    btn.style.pointerEvents = show ? 'auto' : 'none';
  });
  btn.addEventListener('click', () => window.scrollTo({ top: 0, behavior: 'smooth' }));
})();
