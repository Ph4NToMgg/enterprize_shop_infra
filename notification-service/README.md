# notification-service

**Files to edit for business logic:**
- `service/NotificationConsumer.java` — Deserialize Kafka events, add routing to email/SMS
- `service/EmailService.java` — Add HTML email templates, attachments
- Add `service/SmsService.java` for SMS integration (Twilio, etc.)
- `controller/NotificationController.java` — Add admin trigger endpoint, notification history
