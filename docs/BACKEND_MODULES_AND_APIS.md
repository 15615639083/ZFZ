# Backend Modules And APIs

## Module Split

### `auth`

Responsibilities:

- register and login
- return current token placeholder and user identity

### `user`

Responsibilities:

- current user profile
- user role extension points

### `engineer`

Responsibilities:

- engineer apply
- engineer public profile list
- engineer own profile
- admin review and approval

### `resource`

Responsibilities:

- resource list and detail
- resource download log
- admin resource creation and publish management

### `serviceorder`

Responsibilities:

- create service order
- list user orders
- payment/acceptance placeholder flow
- admin dispatch and list management

### `dashboard`

Responsibilities:

- admin overview counts for MVP operation panel

## Identity Convention In MVP

The current MVP does not implement JWT filter or RBAC middleware yet.

To keep development moving while preserving future structure, APIs support these request headers:

- `X-User-Id`: current operator id, defaults to `1`
- `X-User-Role`: current operator role, defaults to `user`

Admin APIs require `X-User-Role: admin`.

## API List

### Public APIs

| Method | Path | Description |
| --- | --- | --- |
| POST | `/api/public/auth/register` | register user |
| POST | `/api/public/auth/login` | account login |
| GET | `/api/public/health` | health check |

### Front APIs

| Method | Path | Description |
| --- | --- | --- |
| GET | `/api/front/index` | homepage data |
| GET | `/api/front/user/me` | current user profile |
| GET | `/api/front/engineers` | approved engineer list |
| GET | `/api/front/engineer/me` | current user's engineer profile |
| POST | `/api/front/engineer/apply` | submit engineer application |
| GET | `/api/front/resources` | published resources |
| GET | `/api/front/resources/{id}` | resource detail |
| POST | `/api/front/resources/{id}/download` | record resource download |
| POST | `/api/front/service-orders` | create service order |
| GET | `/api/front/service-orders` | current user's orders |
| POST | `/api/front/service-orders/{id}/pay` | mark current order paid |
| POST | `/api/front/service-orders/{id}/confirm` | confirm service acceptance |

### Admin APIs

| Method | Path | Description |
| --- | --- | --- |
| GET | `/api/admin/dashboard/overview` | dashboard metrics |
| GET | `/api/admin/engineers` | all engineer applications |
| GET | `/api/admin/engineers/pending` | pending engineer applications |
| POST | `/api/admin/engineers/{id}/review` | approve or reject engineer |
| GET | `/api/admin/resources` | all resources |
| POST | `/api/admin/resources` | create resource |
| GET | `/api/admin/service-orders` | all service orders |
| POST | `/api/admin/service-orders/{id}/dispatch` | dispatch order to engineer |

## Recommended Build Order

1. auth + user
2. engineer apply + review
3. resource list + download log
4. service order create + dispatch
5. dashboard aggregation
6. payment, IM, settlement, withdrawal
