/* ===================================================================
   Enterprise Shop — API Client
   Handles all communication with the backend microservices.
   =================================================================== */

const API = (() => {
    // Default base URLs — adjustable from the UI
    const defaults = {
        auth: 'http://localhost:8081',
        product: 'http://localhost:8082',
        inventory: 'http://localhost:8083',
        order: 'http://localhost:8084',
        notification: 'http://localhost:8085',
    };

    // Current URLs (can be overridden)
    let urls = { ...defaults };

    // Token storage
    let accessToken = localStorage.getItem('es_access_token') || null;
    let refreshToken = localStorage.getItem('es_refresh_token') || null;

    function setTokens(access, refresh) {
        accessToken = access;
        refreshToken = refresh;
        if (access) localStorage.setItem('es_access_token', access);
        else localStorage.removeItem('es_access_token');
        if (refresh) localStorage.setItem('es_refresh_token', refresh);
        else localStorage.removeItem('es_refresh_token');
    }

    function getAccessToken() { return accessToken; }
    function isLoggedIn() { return !!accessToken; }

    function clearTokens() {
        accessToken = null;
        refreshToken = null;
        localStorage.removeItem('es_access_token');
        localStorage.removeItem('es_refresh_token');
        localStorage.removeItem('es_user');
    }

    function setBaseUrl(service, url) {
        urls[service] = url.replace(/\/$/, '');
    }
    function getBaseUrl(service) { return urls[service]; }
    function getDefaults() { return { ...defaults }; }

    async function request(baseService, path, options = {}) {
        const url = `${urls[baseService]}${path}`;
        const headers = { 'Content-Type': 'application/json', ...options.headers };
        if (accessToken && !options.noAuth) {
            headers['Authorization'] = `Bearer ${accessToken}`;
        }

        try {
            const resp = await fetch(url, {
                method: options.method || 'GET',
                headers,
                body: options.body ? JSON.stringify(options.body) : undefined,
            });

            // Try token refresh on 401
            if (resp.status === 401 && refreshToken && !options._isRetry) {
                const refreshed = await tryRefresh();
                if (refreshed) {
                    return request(baseService, path, { ...options, _isRetry: true });
                }
            }

            if (!resp.ok) {
                const errBody = await resp.json().catch(() => null);
                const msg = errBody?.message || errBody?.error || `HTTP ${resp.status}`;
                throw new Error(msg);
            }

            if (resp.status === 204) return null;
            return await resp.json();
        } catch (err) {
            if (err.name === 'TypeError' && err.message.includes('fetch')) {
                throw new Error(`Cannot connect to ${baseService} (${urls[baseService]})`);
            }
            throw err;
        }
    }

    async function tryRefresh() {
        try {
            const resp = await fetch(`${urls.auth}/api/auth/refresh`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ refreshToken }),
            });
            if (!resp.ok) return false;
            const data = await resp.json();
            setTokens(data.accessToken, data.refreshToken);
            return true;
        } catch {
            return false;
        }
    }

    // ── Auth ─────────────────────────────────────────────────────────
    async function login(username, password) {
        const data = await request('auth', '/api/auth/login', {
            method: 'POST',
            body: { username, password },
            noAuth: true,
        });
        setTokens(data.accessToken, data.refreshToken);
        localStorage.setItem('es_user', JSON.stringify({
            username: data.username,
            email: data.email,
            role: data.role,
        }));
        return data;
    }

    async function register(username, email, password) {
        const data = await request('auth', '/api/auth/signup', {
            method: 'POST',
            body: { username, email, password },
            noAuth: true,
        });
        setTokens(data.accessToken, data.refreshToken);
        localStorage.setItem('es_user', JSON.stringify({
            username: data.username,
            email: data.email,
            role: data.role,
        }));
        return data;
    }

    async function getMe() {
        return request('auth', '/api/auth/me');
    }

    async function getAllUsers() {
        return request('auth', '/api/auth/users');
    }

    // ── Products ─────────────────────────────────────────────────────
    async function getProducts(category) {
        const q = category ? `?category=${encodeURIComponent(category)}` : '';
        return request('product', `/api/products${q}`);
    }

    async function searchProducts(name, category, page = 0, size = 20) {
        const params = new URLSearchParams();
        if (name) params.set('name', name);
        if (category) params.set('category', category);
        params.set('page', page);
        params.set('size', size);
        return request('product', `/api/products/search?${params}`);
    }

    async function getProduct(id) {
        return request('product', `/api/products/${id}`);
    }

    async function createProduct(dto) {
        return request('product', '/api/products', { method: 'POST', body: dto });
    }

    async function updateProduct(id, dto) {
        return request('product', `/api/products/${id}`, { method: 'PUT', body: dto });
    }

    async function deleteProduct(id) {
        return request('product', `/api/products/${id}`, { method: 'DELETE' });
    }

    // ── Inventory ────────────────────────────────────────────────────
    async function getInventory(sku) {
        const q = sku ? `?sku=${encodeURIComponent(sku)}` : '';
        return request('inventory', `/api/inventory${q}`);
    }

    async function reserveInventory(sku, quantity) {
        return request('inventory', '/api/inventory/reserve', {
            method: 'POST',
            body: { sku, quantity },
        });
    }

    // ── Orders ───────────────────────────────────────────────────────
    async function createOrder(userEmail, items) {
        return request('order', '/api/orders', {
            method: 'POST',
            body: { userEmail, items },
        });
    }

    async function getOrder(id) {
        return request('order', `/api/orders/${id}`);
    }

    // ── Health checks ────────────────────────────────────────────────
    async function checkHealth(service) {
        try {
            const resp = await fetch(`${urls[service]}/actuator/health`, {
                signal: AbortSignal.timeout(3000),
            });
            return resp.ok;
        } catch {
            return false;
        }
    }

    async function checkAllHealth() {
        const services = ['auth', 'product', 'inventory', 'order', 'notification'];
        const results = {};
        await Promise.all(services.map(async (s) => {
            results[s] = await checkHealth(s);
        }));
        return results;
    }

    return {
        login, register, getMe, getAllUsers,
        getProducts, searchProducts, getProduct, createProduct, updateProduct, deleteProduct,
        getInventory, reserveInventory,
        createOrder, getOrder,
        checkHealth, checkAllHealth,
        setBaseUrl, getBaseUrl, getDefaults,
        setTokens, getAccessToken, isLoggedIn, clearTokens,
    };
})();
