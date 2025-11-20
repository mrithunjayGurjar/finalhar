-- Sample Test Data for CRM Ticketing System
-- This file contains sample users, tickets, and notes for testing

USE crm_ticketing;

-- Sample Users with different roles
-- Password for all users: password123
INSERT INTO users (name, email, password, role) VALUES
('John Customer', 'customer@example.com', '$2a$10$rRz5vXYJQiLZp.iFWGvQC.OhLZVJWVVZvZx6fQqL0JZqZnv0ZqZqy', 'USER'),
('Jane Agent', 'agent@example.com', '$2a$10$rRz5vXYJQiLZp.iFWGvQC.OhLZVJWVVZvZx6fQqL0JZqZnv0ZqZqy', 'AGENT'),
('Bob Manager', 'manager@example.com', '$2a$10$rRz5vXYJQiLZp.iFWGvQC.OhLZVJWVVZvZx6fQqL0JZqZnv0ZqZqy', 'MANAGER'),
('Alice Support', 'support@example.com', '$2a$10$rRz5vXYJQiLZp.iFWGvQC.OhLZVJWVVZvZx6fQqL0JZqZnv0ZqZqy', 'AGENT');

-- Sample Tickets
INSERT INTO tickets (title, description, created_by, assigned_to, priority, status, created_at, updated_at) VALUES
('Login Issue', 'Unable to login to the application', 1, 2, 'HIGH', 'OPEN', NOW(), NOW()),
('Feature Request', 'Add export functionality to reports', 1, 2, 'MEDIUM', 'IN_PROGRESS', NOW(), NOW()),
('Bug Report', 'Application crashes on submit', 1, 3, 'CRITICAL', 'ESCALATED', NOW(), NOW()),
('Performance Issue', 'Page loads very slowly', 1, 2, 'HIGH', 'RESOLVED', NOW(), NOW());

-- Sample Ticket Notes
INSERT INTO ticket_notes (ticket_id, author_id, message, type, created_at) VALUES
(1, 1, 'I tried resetting my password but still cannot login', 'CUSTOMER_REPLY', NOW()),
(1, 2, 'We are investigating this issue. Please provide your username.', 'TEAM_REPLY', NOW()),
(2, 1, 'It would be great to export data to Excel format', 'CUSTOMER_REPLY', NOW()),
(2, 2, 'We have added this to our roadmap for Q2', 'TEAM_REPLY', NOW()),
(3, 1, 'This happens every time I click the submit button', 'CUSTOMER_REPLY', NOW()),
(3, 3, 'Escalated to engineering team for immediate fix', 'TEAM_REPLY', NOW()),
(4, 1, 'The dashboard takes over 30 seconds to load', 'CUSTOMER_REPLY', NOW()),
(4, 2, 'We have optimized the queries. Please check now.', 'TEAM_REPLY', NOW()),
(4, 1, 'Much better now, thank you!', 'CUSTOMER_REPLY', NOW());
