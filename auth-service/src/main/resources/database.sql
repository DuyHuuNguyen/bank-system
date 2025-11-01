CREATE TABLE "roles"
(
    "id"         BIGSERIAL PRIMARY KEY NOT NULL,
    "role_name"  varchar               NOT NULL,
    "is_active"  boolean               NOT NULL DEFAULT true,
    "version"    bigint                NOT NULL DEFAULT 0,
    "created_at" bigint                NOT NULL DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint,
    "updated_at" bigint                NOT NULL DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint
);

CREATE TABLE "account_roles"
(
    "id"         BIGSERIAL PRIMARY KEY NOT NULL,
    "user_id"    bigint                NOT NULL,
    "role_id"    bigint                NOT NULL,
    "is_active"  boolean               NOT NULL DEFAULT true,
    "version"    bigint                NOT NULL DEFAULT 0,
    "created_at" bigint                NOT NULL DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint,
    "updated_at" bigint                NOT NULL DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint
);

CREATE TABLE "accounts"
(
    "id"          BIGSERIAL PRIMARY KEY NOT NULL,
    "email"       varchar               NOT NULL,
    "phone"       varchar               NOT NULL,
    "password"    varchar               NOT NULL,
    "personal_id" varchar(50)           NOT NULL,
    "otp"         varchar,
    "is_active"   boolean               NOT NULL DEFAULT true,
    "version"     bigint                NOT NULL DEFAULT 0,
    "created_at"  bigint                NOT NULL DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint,
    "updated_at"  bigint                NOT NULL DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint
);


ALTER TABLE "account_roles"
    ADD FOREIGN KEY ("user_id") REFERENCES "accounts" ("id");

ALTER TABLE "account_roles"
    ADD FOREIGN KEY ("role_id") REFERENCES "roles" ("id");