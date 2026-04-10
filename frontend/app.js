/* ═══════════════════════════════════════════════════════════════
   Enterprise Shop — Application Controller
   Handles all UI logic, routing, and user interactions.
   ═══════════════════════════════════════════════════════════════ */

(() => {
    'use strict';

    // ── Toast Notifications ──────────────────────────────────────
    function showToast(message, type = 'info') {
        const container = document.getElementById('toast-container');
        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        toast.textContent = message;
        container.appendChild(toast);
        setTimeout(() => {
            toast.classList.add('toast-exit');
            setTimeout(() => toast.remove(), 300);
        }, 4000);
    }

    // ── Modal ────────────────────────────────────────────────────
    function showModal(title, bodyHTML, footerHTML) {
        const container = document.getElementById('modal-container');
        container.innerHTML = `
            <div class="modal-overlay" id="modal-overlay">
                <div class="modal">
                    <div class="modal-header">
                        <h3>${title}</h3>
                        <button class="btn-icon" id="modal-close-btn">✕</button>
                    </div>
                    <div class="modal-body">${bodyHTML}</div>
                    ${footerHTML ? `<div class="modal-footer">${footerHTML}</div>` : ''}
                </div>
            </div>`;
        document.getElementById('modal-close-btn').onclick = closeModal;
        document.getElementById('modal-overlay').addEventListener('click', e => {
            if (e.target.id === 'modal-overlay') closeModal();
        });
    }

    function closeModal() {
        document.getElementById('modal-container').innerHTML = '';
    }

    // ── Auth Page ────────────────────────────────────────────────
    const authPage = document.getElementById('auth-page');
    const appShell = document.getElementById('app-shell');
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');
    const authAlert = document.getElementById('auth-alert');

    // Tab switching
    document.getElementById('auth-tabs').addEventListener('click', (e) => {
        const tab = e.target.closest('.auth-tab');
        if (!tab) return;
        document.querySelectorAll('.auth-tab').forEach(t => t.classList.remove('active'));
        tab.classList.add('active');
        const isLogin = tab.dataset.tab === 'login';
        loginForm.style.display = isLogin ? '' : 'none';
        registerForm.style.display = isLogin ? 'none' : '';
        authAlert.innerHTML = '';
    });

    function showAuthError(msg) {
        authAlert.innerHTML = `<div class="alert alert-error">${msg}</div>`;
    }

    function showAuthSuccess(msg) {
        authAlert.innerHTML = `<div class="alert alert-success">${msg}</div>`;
    }

    // Login
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const btn = document.getElementById('login-btn');
        const username = document.getElementById('login-username').value.trim();
        const password = document.getElementById('login-password').value;
        if (!username || !password) return showAuthError('Please fill in all fields');
        btn.disabled = true;
        btn.innerHTML = '<div class="spinner"></div>';
        try {
            await API.login(username, password);
            showAuthSuccess('Login successful! Redirecting…');
            setTimeout(() => enterApp(), 500);
        } catch (err) {
            showAuthError(err.message);
        } finally {
            btn.disabled = false;
            btn.innerHTML = '<span>Sign In</span>';
        }
    });

    // Register
    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const btn = document.getElementById('register-btn');
        const username = document.getElementById('reg-username').value.trim();
        const email = document.getElementById('reg-email').value.trim();
        const password = document.getElementById('reg-password').value;
        if (!username || !email || !password) return showAuthError('Please fill in all fields');
        btn.disabled = true;
        btn.innerHTML = '<div class="spinner"></div>';
        try {
            await API.register(username, email, password);
            showAuthSuccess('Account created! Redirecting…');
            setTimeout(() => enterApp(), 500);
        } catch (err) {
            showAuthError(err.message);
        } finally {
            btn.disabled = false;
            btn.innerHTML = '<span>Create Account</span>';
        }
    });

    // ── Enter / Exit App ─────────────────────────────────────────
    function enterApp() {
        authPage.style.display = 'none';
        appShell.classList.add('active');
        updateUserInfo();
        loadDashboard();
    }

    function exitApp() {
        API.clearTokens();
        appShell.classList.remove('active');
        authPage.style.display = '';
        authAlert.innerHTML = '';
        loginForm.reset();
        registerForm.reset();
    }

    function updateUserInfo() {
        try {
            const user = JSON.parse(localStorage.getItem('es_user') || '{}');
            const name = user.username || 'User';
            document.getElementById('user-display-name').textContent = name;
            document.getElementById('user-display-role').textContent = user.role || 'USER';
            document.getElementById('user-avatar').textContent = name.charAt(0).toUpperCase();
        } catch { /* ignore */ }
    }

    document.getElementById('logout-btn').addEventListener('click', () => {
        exitApp();
        showToast('Signed out successfully', 'info');
    });

    // ── Navigation ───────────────────────────────────────────────
    document.querySelectorAll('.nav-item[data-page]').forEach(item => {
        item.addEventListener('click', () => {
            const page = item.dataset.page;
            navigateTo(page);
        });
    });

    function navigateTo(page) {
        document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));
        document.querySelector(`.nav-item[data-page="${page}"]`)?.classList.add('active');
        document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
        document.getElementById(`page-${page}`)?.classList.add('active');

        if (page === 'dashboard') loadDashboard();
        else if (page === 'products') loadProducts();
        else if (page === 'inventory') loadInventory();
        else if (page === 'users') loadUsers();
        else if (page === 'orders') loadOrders();
        else if (page === 'settings') loadSettings();
    }

    // ── Dashboard ────────────────────────────────────────────────
    async function loadDashboard() {
        refreshHealth();
        loadStats();
    }

    async function refreshHealth() {
        const grid = document.getElementById('health-grid');
        const names = { auth: 'Auth Service', product: 'Product Service', inventory: 'Inventory Service', order: 'Order Service', notification: 'Notification Service' };
        const icons = { auth: '🔐', product: '📦', inventory: '🏭', order: '🛒', notification: '🔔' };

        // Set all to loading
        grid.innerHTML = Object.entries(names).map(([key, name]) => `
            <div class="health-item">
                <div class="health-dot loading"></div>
                <div>
                    <div class="health-item-name">${icons[key]} ${name}</div>
                    <div class="health-item-status">Checking…</div>
                </div>
            </div>
        `).join('');

        const results = await API.checkAllHealth();
        const onlineCount = Object.values(results).filter(Boolean).length;

        grid.innerHTML = Object.entries(names).map(([key, name]) => {
            const up = results[key];
            return `
                <div class="health-item">
                    <div class="health-dot ${up ? 'up' : 'down'}"></div>
                    <div>
                        <div class="health-item-name">${icons[key]} ${name}</div>
                        <div class="health-item-status">${up ? 'Online' : 'Offline'}</div>
                    </div>
                </div>`;
        }).join('');

        document.getElementById('stat-services').textContent = `${onlineCount}/5`;
    }

    async function loadStats() {
        try {
            const products = await API.getProducts();
            document.getElementById('stat-products').textContent = Array.isArray(products) ? products.length : '—';
        } catch { document.getElementById('stat-products').textContent = '—'; }

        try {
            const inv = await API.getInventory();
            document.getElementById('stat-inventory').textContent = Array.isArray(inv) ? inv.length : '—';
        } catch { document.getElementById('stat-inventory').textContent = '—'; }

        try {
            const users = await API.getAllUsers();
            document.getElementById('stat-users').textContent = Array.isArray(users) ? users.length : '—';
        } catch { document.getElementById('stat-users').textContent = '—'; }

        document.getElementById('stat-uptime').textContent = document.getElementById('stat-services')?.textContent || '—';
    }

    document.getElementById('refresh-health-btn').addEventListener('click', () => {
        refreshHealth();
        showToast('Refreshing health checks…', 'info');
    });

    // ── Products ─────────────────────────────────────────────────
    async function loadProducts() {
        const container = document.getElementById('products-content');
        container.innerHTML = '<div class="empty-state"><div class="spinner" style="margin:0 auto;width:32px;height:32px;border-width:3px;"></div><h3 style="margin-top:16px;">Loading…</h3></div>';

        try {
            const products = await API.getProducts();
            if (!products || products.length === 0) {
                container.innerHTML = `
                    <div class="empty-state">
                        <div class="empty-state-icon">📦</div>
                        <h3>No products yet</h3>
                        <p>Click "Add Product" to create your first product</p>
                    </div>`;
                return;
            }
            container.innerHTML = `<div class="product-grid">${products.map(p => productCardHTML(p)).join('')}</div>`;
        } catch (err) {
            container.innerHTML = `<div class="alert alert-error">Failed to load products: ${err.message}</div>`;
        }
    }

    function productCardHTML(p) {
        const categoryEmoji = { electronics: '💻', clothing: '👕', food: '🍔', books: '📚', sports: '⚽' };
        const emoji = categoryEmoji[p.category?.toLowerCase()] || '📦';
        return `
            <div class="product-card">
                <div class="product-card-img">${emoji}</div>
                <div class="product-card-body">
                    <h4>${escapeHTML(p.name)}</h4>
                    <div class="sku">SKU: ${escapeHTML(p.sku)}</div>
                    <div class="flex items-center gap-8">
                        <span class="price">$${Number(p.price).toFixed(2)}</span>
                        ${p.category ? `<span class="badge badge-success">${escapeHTML(p.category)}</span>` : ''}
                    </div>
                </div>
                <div class="product-card-footer">
                    <button class="btn btn-secondary btn-sm" onclick="window.__editProduct('${p.id}')">✏️ Edit</button>
                    <button class="btn btn-danger btn-sm" onclick="window.__deleteProduct('${p.id}')">🗑️ Delete</button>
                </div>
            </div>`;
    }

    // Add Product
    document.getElementById('add-product-btn').addEventListener('click', () => showProductModal());

    function showProductModal(product = null) {
        const isEdit = !!product;
        const body = `
            <div class="form-group">
                <label>Product Name</label>
                <input type="text" id="modal-p-name" placeholder="e.g. Gaming Laptop" value="${escapeHTML(product?.name || '')}" required>
            </div>
            <div class="form-group">
                <label>SKU</label>
                <input type="text" id="modal-p-sku" placeholder="e.g. SKU-001" value="${escapeHTML(product?.sku || '')}" ${isEdit ? 'readonly style="opacity:0.5"' : ''}>
            </div>
            <div class="form-group">
                <label>Price</label>
                <input type="number" id="modal-p-price" step="0.01" placeholder="99.99" value="${product?.price || ''}">
            </div>
            <div class="form-group">
                <label>Category</label>
                <input type="text" id="modal-p-category" placeholder="e.g. Electronics" value="${escapeHTML(product?.category || '')}">
            </div>
            <div class="form-group">
                <label>Description</label>
                <input type="text" id="modal-p-desc" placeholder="Short description" value="${escapeHTML(product?.description || '')}">
            </div>`;
        const footer = `
            <button class="btn btn-secondary" onclick="document.getElementById('modal-container').innerHTML=''">Cancel</button>
            <button class="btn btn-primary btn-sm" id="modal-save-product">${isEdit ? 'Update' : 'Create'} Product</button>`;
        showModal(isEdit ? 'Edit Product' : 'New Product', body, footer);
        document.getElementById('modal-save-product').onclick = async () => {
            const dto = {
                name: document.getElementById('modal-p-name').value.trim(),
                sku: document.getElementById('modal-p-sku').value.trim(),
                price: parseFloat(document.getElementById('modal-p-price').value),
                category: document.getElementById('modal-p-category').value.trim(),
                description: document.getElementById('modal-p-desc').value.trim(),
            };
            if (!dto.name || !dto.sku || isNaN(dto.price)) return showToast('Please fill in required fields', 'error');
            try {
                if (isEdit) await API.updateProduct(product.id, dto);
                else {
                    await API.createProduct(dto);
                    await API.addInventory(dto.sku, 100);
                }
                closeModal();
                showToast(isEdit ? 'Product updated!' : 'Product created!', 'success');
                loadProducts();
            } catch (err) {
                showToast(err.message, 'error');
            }
        };
    }

    window.__editProduct = async (id) => {
        try {
            const product = await API.getProduct(id);
            showProductModal(product);
        } catch (err) {
            showToast(err.message, 'error');
        }
    };

    window.__deleteProduct = async (id) => {
        if (!confirm('Delete this product?')) return;
        try {
            await API.deleteProduct(id);
            showToast('Product deleted', 'success');
            loadProducts();
        } catch (err) {
            showToast(err.message, 'error');
        }
    };

    // ── Inventory ────────────────────────────────────────────────
    async function loadInventory() {
        const container = document.getElementById('inventory-content');
        container.innerHTML = '<div class="empty-state"><div class="spinner" style="margin:0 auto;width:32px;height:32px;border-width:3px;"></div><h3 style="margin-top:16px;">Loading…</h3></div>';

        try {
            const items = await API.getInventory();
            if (!items || items.length === 0) {
                container.innerHTML = `
                    <div class="empty-state">
                        <div class="empty-state-icon">🏭</div>
                        <h3>No inventory data</h3>
                        <p>Inventory items will appear here once products have stock</p>
                    </div>`;
                return;
            }
            container.innerHTML = `
                <div class="card">
                    <table class="data-table">
                        <thead><tr>
                            <th>SKU</th><th>Available</th><th>Reserved</th><th>Status</th><th>Actions</th>
                        </tr></thead>
                        <tbody>${items.map(i => `
                            <tr>
                                <td style="font-family:monospace;font-weight:600;">${escapeHTML(i.sku)}</td>
                                <td>${i.availableQuantity}</td>
                                <td>${i.reservedQuantity || 0}</td>
                                <td>${i.availableQuantity > 10
                    ? '<span class="badge badge-success">In Stock</span>'
                    : i.availableQuantity > 0
                        ? '<span class="badge badge-warning">Low Stock</span>'
                        : '<span class="badge badge-danger">Out of Stock</span>'}</td>
                                <td><button class="btn btn-secondary btn-sm" onclick="window.__reserveInventory('${escapeHTML(i.sku)}')">Reserve</button></td>
                            </tr>`).join('')}
                        </tbody>
                    </table>
                </div>`;
        } catch (err) {
            container.innerHTML = `<div class="alert alert-error">Failed to load inventory: ${err.message}</div>`;
        }
    }

    window.__reserveInventory = (sku) => {
        const body = `
            <div class="form-group">
                <label>SKU</label>
                <input type="text" value="${escapeHTML(sku)}" readonly style="opacity:0.5">
            </div>
            <div class="form-group">
                <label>Quantity to Reserve</label>
                <input type="number" id="modal-reserve-qty" min="1" value="1" placeholder="1">
            </div>`;
        const footer = `
            <button class="btn btn-secondary" onclick="document.getElementById('modal-container').innerHTML=''">Cancel</button>
            <button class="btn btn-primary btn-sm" id="modal-reserve-btn">Reserve</button>`;
        showModal('Reserve Inventory', body, footer);
        document.getElementById('modal-reserve-btn').onclick = async () => {
            const qty = parseInt(document.getElementById('modal-reserve-qty').value);
            if (!qty || qty < 1) return showToast('Enter a valid quantity', 'error');
            try {
                await API.reserveInventory(sku, qty);
                closeModal();
                showToast(`Reserved ${qty} units of ${sku}`, 'success');
                loadInventory();
            } catch (err) {
                showToast(err.message, 'error');
            }
        };
    };

    document.getElementById('refresh-inventory-btn').addEventListener('click', () => {
        loadInventory();
        showToast('Refreshing inventory…', 'info');
    });

    // ── Orders ───────────────────────────────────────────────────
    document.getElementById('create-order-btn').addEventListener('click', () => {
        const body = `
            <div class="form-group">
                <label>Customer Email</label>
                <input type="email" id="modal-order-email" placeholder="customer@example.com">
            </div>
            <div class="form-group">
                <label>SKU</label>
                <input type="text" id="modal-order-sku" placeholder="SKU-001">
            </div>
            <div class="form-group">
                <label>Quantity</label>
                <input type="number" id="modal-order-qty" min="1" value="1">
            </div>
            <div class="form-group">
                <label>Unit Price</label>
                <input type="number" id="modal-order-price" step="0.01" placeholder="99.99">
            </div>`;
        const footer = `
            <button class="btn btn-secondary" onclick="document.getElementById('modal-container').innerHTML=''">Cancel</button>
            <button class="btn btn-primary btn-sm" id="modal-order-btn">Place Order</button>`;
        showModal('Create Order', body, footer);
        document.getElementById('modal-order-btn').onclick = async () => {
            const email = document.getElementById('modal-order-email').value.trim();
            const sku = document.getElementById('modal-order-sku').value.trim();
            const qty = parseInt(document.getElementById('modal-order-qty').value);
            const price = parseFloat(document.getElementById('modal-order-price').value);
            if (!email || !sku || !qty || isNaN(price)) return showToast('Please fill in all fields', 'error');
            try {
                const order = await API.createOrder(email, [{ sku, quantity: qty, unitPrice: price }]);
                closeModal();
                showToast(`Order created! ID: ${order.orderId || 'OK'}`, 'success');
                loadOrders();
            } catch (err) {
                showToast(err.message, 'error');
            }
        };
    });

    async function loadOrders() {
        const container = document.getElementById('orders-content');
        container.innerHTML = '<div class="empty-state"><div class="spinner" style="margin:0 auto;width:32px;height:32px;border-width:3px;"></div><h3 style="margin-top:16px;">Loading…</h3></div>';

        try {
            const orders = await API.getAllOrders();
            if (!orders || orders.length === 0) {
                container.innerHTML = `
                    <div class="empty-state">
                        <div class="empty-state-icon">🛒</div>
                        <h3>No orders yet</h3>
                        <p>Create your first order to get started</p>
                    </div>`;
                return;
            }
            container.innerHTML = `
                <div class="card">
                    <table class="data-table">
                        <thead><tr>
                            <th>ID</th><th>User</th><th>Status</th><th>Total</th>
                        </tr></thead>
                        <tbody>${orders.map(o => `
                            <tr>
                                <td style="font-family:monospace;font-size:12px;">${escapeHTML(o.orderId.substring(0, 8))}…</td>
                                <td>${escapeHTML(o.userEmail)}</td>
                                <td><span class="badge ${o.status === 'CONFIRMED' ? 'badge-success' : 'badge-warning'}">${escapeHTML(o.status)}</span></td>
                                <td style="font-weight:bold;">$${Number(o.totalAmount).toFixed(2)}</td>
                            </tr>`).join('')}
                        </tbody>
                    </table>
                </div>`;
        } catch (err) {
            container.innerHTML = `<div class="alert alert-error">Failed to load orders: ${err.message}</div>`;
        }
    }

    // ── Users ────────────────────────────────────────────────────
    async function loadUsers() {
        const container = document.getElementById('users-content');
        container.innerHTML = '<div class="empty-state"><div class="spinner" style="margin:0 auto;width:32px;height:32px;border-width:3px;"></div><h3 style="margin-top:16px;">Loading…</h3></div>';

        try {
            const users = await API.getAllUsers();
            if (!users || users.length === 0) {
                container.innerHTML = `
                    <div class="empty-state">
                        <div class="empty-state-icon">👥</div>
                        <h3>No users found</h3>
                        <p>Registered users will appear here</p>
                    </div>`;
                return;
            }
            container.innerHTML = `
                <div class="card">
                    <table class="data-table">
                        <thead><tr>
                            <th>Username</th><th>Email</th><th>Role</th>
                        </tr></thead>
                        <tbody>${users.map(u => `
                            <tr>
                                <td style="font-weight:600;">${escapeHTML(u.username)}</td>
                                <td>${escapeHTML(u.email)}</td>
                                <td><span class="badge ${u.role === 'ADMIN' ? 'badge-warning' : 'badge-success'}">${escapeHTML(u.role || 'USER')}</span></td>
                            </tr>`).join('')}
                        </tbody>
                    </table>
                </div>`;
        } catch (err) {
            container.innerHTML = `<div class="alert alert-error">Failed to load users: ${err.message}</div>`;
        }
    }

    document.getElementById('refresh-users-btn').addEventListener('click', () => {
        loadUsers();
        showToast('Refreshing users…', 'info');
    });

    // ── Settings ─────────────────────────────────────────────────
    function loadSettings() {
        const services = { auth: 'Auth Service', product: 'Product Service', inventory: 'Inventory Service', order: 'Order Service', notification: 'Notification Service' };
        const form = document.getElementById('settings-form');
        form.innerHTML = Object.entries(services).map(([key, name]) => `
            <div class="form-group">
                <label>${name}</label>
                <input type="text" id="setting-${key}" value="${API.getBaseUrl(key)}" placeholder="http://localhost:808x">
            </div>`).join('') +
            '<button class="btn btn-primary btn-sm mt-16" id="save-settings-btn">💾 Save Settings</button>';

        document.getElementById('save-settings-btn').addEventListener('click', () => {
            Object.keys(services).forEach(key => {
                const val = document.getElementById(`setting-${key}`).value.trim();
                if (val) API.setBaseUrl(key, val);
            });
            showToast('Settings saved!', 'success');
        });
    }

    // ── Utilities ────────────────────────────────────────────────
    function escapeHTML(str) {
        if (!str) return '';
        return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
    }

    // ── Boot ─────────────────────────────────────────────────────
    if (API.isLoggedIn()) {
        enterApp();
    }

})();
