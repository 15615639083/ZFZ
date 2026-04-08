# Database Design

## Design Goals

- align with the current `admin + app + server` split
- keep CRMEB-like engineering structure, but replace mall domains with service-platform domains
- make MVP runnable with the smallest useful set of tables
- leave room for IM, payment, withdrawal, and moderation in phase two and three

## Naming Rules

- table names use plural snake case
- primary key uses `id`
- timestamps use `created_at` and `updated_at`
- status fields use short enum strings for readability in the MVP stage

## MVP Core Tables

### `users`

| Column | Type | Required | Description |
| --- | --- | --- | --- |
| id | bigint | yes | PK |
| username | varchar(64) | yes | login/display name |
| phone | varchar(20) | no | mobile number |
| email | varchar(128) | no | email |
| password_hash | varchar(128) | yes | password hash placeholder for MVP |
| role | varchar(20) | yes | `user` / `engineer` / `admin` |
| status | varchar(20) | yes | `active` / `disabled` / `pending` |
| avatar | varchar(255) | no | avatar URL |
| real_name | varchar(64) | no | real name |
| created_at | datetime | yes | create time |
| updated_at | datetime | yes | update time |

Indexes:

- `uk_users_phone(phone)`
- `uk_users_email(email)`
- `idx_users_role_status(role, status)`

### `engineer_profiles`

| Column | Type | Required | Description |
| --- | --- | --- | --- |
| id | bigint | yes | PK |
| user_id | bigint | yes | FK -> users.id |
| real_name | varchar(64) | yes | applicant real name |
| id_card_mask | varchar(64) | no | masked identity number |
| certificates | text | no | certificate URLs or JSON text |
| skills | varchar(255) | yes | technical tags |
| service_tags | varchar(255) | yes | service category tags |
| intro | varchar(500) | no | self introduction |
| hourly_price | decimal(10,2) | no | reference price |
| verification_status | varchar(20) | yes | `pending` / `approved` / `rejected` |
| verification_remark | varchar(255) | no | review remark |
| online_status | varchar(20) | yes | `online` / `busy` / `offline` |
| rating | decimal(3,2) | yes | average rating |
| completed_order_count | int | yes | completed order count |
| reviewed_by | bigint | no | reviewer admin id |
| reviewed_at | datetime | no | review time |
| created_at | datetime | yes | create time |
| updated_at | datetime | yes | update time |

Indexes:

- `uk_engineer_profiles_user_id(user_id)`
- `idx_engineer_profiles_status(verification_status, online_status)`

### `resources`

| Column | Type | Required | Description |
| --- | --- | --- | --- |
| id | bigint | yes | PK |
| title | varchar(128) | yes | resource title |
| category_name | varchar(64) | yes | category |
| description | varchar(1000) | no | description |
| cover_url | varchar(255) | no | cover |
| file_url | varchar(255) | no | file location |
| file_hash | varchar(128) | no | integrity hash |
| price | decimal(10,2) | yes | price |
| is_free | tinyint(1) | yes | free flag |
| status | varchar(20) | yes | `draft` / `published` / `disabled` |
| download_count | int | yes | download count |
| uploader_id | bigint | no | uploader |
| created_at | datetime | yes | create time |
| updated_at | datetime | yes | update time |

Indexes:

- `idx_resources_status_category(status, category_name)`

### `resource_download_logs`

| Column | Type | Required | Description |
| --- | --- | --- | --- |
| id | bigint | yes | PK |
| resource_id | bigint | yes | FK -> resources.id |
| user_id | bigint | yes | downloader |
| download_ip | varchar(64) | no | request IP |
| created_at | datetime | yes | create time |

Indexes:

- `idx_resource_download_logs_resource(resource_id)`
- `idx_resource_download_logs_user(user_id)`

### `service_orders`

| Column | Type | Required | Description |
| --- | --- | --- | --- |
| id | bigint | yes | PK |
| order_no | varchar(32) | yes | order number |
| user_id | bigint | yes | demander |
| engineer_id | bigint | no | selected/assigned engineer |
| dispatcher_admin_id | bigint | no | admin dispatcher |
| service_title | varchar(128) | yes | service subject |
| requirement_desc | varchar(2000) | yes | requirement details |
| price | decimal(10,2) | yes | quoted amount |
| status | varchar(30) | yes | `pending_dispatch` / `assigned` / `processing` / `waiting_acceptance` / `completed` / `cancelled` / `disputed` |
| pay_status | varchar(20) | yes | `unpaid` / `paid` / `refunded` |
| dispatch_remark | varchar(255) | no | admin dispatch note |
| delivery_note | varchar(500) | no | engineer delivery note |
| created_at | datetime | yes | create time |
| updated_at | datetime | yes | update time |
| paid_at | datetime | no | payment time |
| completed_at | datetime | no | completion time |

Indexes:

- `uk_service_orders_order_no(order_no)`
- `idx_service_orders_user(user_id, status)`
- `idx_service_orders_engineer(engineer_id, status)`
- `idx_service_orders_dispatch(status, pay_status)`

## Phase Two Extension Tables

### `messages`

Used for order chat and moderation.

Suggested fields:

- `id`
- `order_id`
- `sender_id`
- `message_type`
- `content`
- `file_url`
- `created_at`

### `transactions`

Used for recharge, order payment, settlement, withdrawal, refund.

Suggested fields:

- `id`
- `biz_no`
- `user_id`
- `order_id`
- `amount`
- `type`
- `status`
- `channel`
- `created_at`
- `updated_at`

### `withdrawal_requests`

Used for engineer withdrawal review.

Suggested fields:

- `id`
- `engineer_id`
- `amount`
- `account_type`
- `account_no_mask`
- `status`
- `reviewed_by`
- `reviewed_at`
- `created_at`
- `updated_at`

## Recommended Initialization SQL

The current MVP code uses the following tables directly:

- `users`
- `engineer_profiles`
- `resources`
- `resource_download_logs`
- `service_orders`

Reference SQL is stored in `docs/sql/mvp_init.sql`.
