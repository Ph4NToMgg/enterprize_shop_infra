import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

/**
 * k6 load test for order creation endpoint.
 *
 * Usage:
 *   k6 run k6/load-test.js
 *
 * Environment variables:
 *   BASE_URL  - Order service URL (default: http://localhost:8084)
 *   AUTH_URL  - Auth service URL (default: http://localhost:8081)
 */

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8084';
const AUTH_URL = __ENV.AUTH_URL || 'http://localhost:8081';

const errorRate = new Rate('errors');

export const options = {
    stages: [
        { duration: '30s', target: 10 },  // ramp up to 10 VUs
        { duration: '1m', target: 10 },  // stay at 10 VUs
        { duration: '30s', target: 50 },  // ramp up to 50 VUs
        { duration: '1m', target: 50 },  // stay at 50 VUs
        { duration: '30s', target: 0 },   // ramp down
    ],
    thresholds: {
        http_req_duration: ['p(95)<2000'], // 95th percentile < 2s
        errors: ['rate<0.1'],              // error rate < 10%
    },
};

export default function () {
    const payload = JSON.stringify({
        userEmail: `user${__VU}@test.com`,
        items: [
            {
                sku: 'LAPTOP-001',
                quantity: 1,
                unitPrice: 999.99,
            },
        ],
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(`${BASE_URL}/api/orders`, payload, params);

    const success = check(res, {
        'status is 201': (r) => r.status === 201,
        'response has orderId': (r) => JSON.parse(r.body).orderId !== undefined,
    });

    errorRate.add(!success);
    sleep(1);
}
