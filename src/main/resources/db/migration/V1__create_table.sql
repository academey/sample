-- vim: filetype=sql sw=4

DO
$$
    BEGIN
        CREATE TYPE USER_ROLE AS ENUM (
            'GUEST',
            'ADMIN',
            'USER'
            );
    EXCEPTION
        WHEN duplicate_object THEN null;
    END
$$;

DO
$$
    BEGIN
        CREATE TYPE SOCIAL_PROVIDER AS ENUM (
            'GOOGLE',
            'KAKAO',
            'APPLE'
            );
    EXCEPTION
        WHEN duplicate_object THEN null;
    END
$$;

CREATE TABLE IF NOT EXISTS public."user"
(
    id                BIGSERIAL NOT NULL,
    email             VARCHAR   NULL,
    "name"            VARCHAR   NOT NULL,
    role              USER_ROLE NOT NULL,
    picture           VARCHAR,
    "password"        VARCHAR,
    "expiration_date" timestamp NOT NULL,
    "failed_count"    INT       NOT NULL DEFAULT 0,
    "created_at"      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT user_pk PRIMARY KEY (id),
    UNIQUE (email),
    UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS public."anonymous_user"
(
    id           BIGSERIAL NOT NULL,
    uid          VARCHAR   NOT NULL,
    "created_at" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT anonymous_user_pk PRIMARY KEY (id),
    UNIQUE (uid)
);

CREATE TABLE IF NOT EXISTS public."o_auth_provider"
(
    id           BIGSERIAL       NOT NULL,
    user_id      BIGINT          NOT NULL,
    provider     SOCIAL_PROVIDER NOT NULL,
    uid          VARCHAR         NOT NULL,
    "created_at" timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT o_auth_provider_pk PRIMARY KEY (id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES "user" (id)
);

CREATE TABLE IF NOT EXISTS public."match"
(
    id            BIGSERIAL NOT NULL,
    title         VARCHAR   NOT NULL,
    icon          VARCHAR   NOT NULL,
    thumbnail_url VARCHAR   NOT NULL,
    start_at      timestamp NULL,
    end_at        timestamp NULL,
    is_deleted    BOOLEAN   NOT NULL DEFAULT false,
    created_at    timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT match_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.tag
(
    id         BIGSERIAL NOT NULL,
    "name"     VARCHAR   NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT tag_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.match_and_tag
(
    match_id BIGINT NOT NULL,
    tag_id   BIGINT NOT NULL,
    CONSTRAINT match_and_tag_pk PRIMARY KEY (match_id, tag_id),
    CONSTRAINT match_and_tag_fk FOREIGN KEY (match_id) REFERENCES match (id),
    CONSTRAINT match_and_tag_fk_1 FOREIGN KEY (tag_id) REFERENCES tag (id)
);

CREATE TABLE IF NOT EXISTS public."match_comment"
(
    id                BIGSERIAL NOT NULL,
    match_id          BIGINT    NOT NULL,
    user_id           BIGINT    NULL,
    anonymous_user_id BIGINT    NULL,
    content           TEXT      NOT NULL,
    "created_at"      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT match_comment_pk PRIMARY KEY (id),
    CONSTRAINT fk_match
        FOREIGN KEY (match_id)
            REFERENCES "match" (id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES "user" (id),
    CONSTRAINT fk_anonymous_user
        FOREIGN KEY (anonymous_user_id)
            REFERENCES "anonymous_user" (id)
);


CREATE TABLE IF NOT EXISTS public."match_comment_report"
(
    id                BIGSERIAL NOT NULL,
    match_comment_id  BIGINT    NOT NULL,
    user_id           BIGINT    NULL,
    anonymous_user_id BIGINT    NULL,
    reason            TEXT      NULL,
    "created_at"      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT match_comment_report_pk PRIMARY KEY (id),
    CONSTRAINT fk_match_comment
        FOREIGN KEY (match_comment_id)
            REFERENCES "match_comment" (id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES "user" (id),
    CONSTRAINT fk_anonymous_user
        FOREIGN KEY (anonymous_user_id)
            REFERENCES "anonymous_user" (id)
);


CREATE TABLE IF NOT EXISTS public."match_comment_like"
(
    id                BIGSERIAL NOT NULL,
    user_id           BIGINT    NULL,
    anonymous_user_id BIGINT    NULL,
    match_comment_id  BIGINT    NOT NULL,
    is_liked          BOOLEAN   NOT NULL DEFAULT true,
    "created_at"      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT match_comment_like_pk PRIMARY KEY (id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES "user" (id),
    CONSTRAINT fk_anonymous_user
        FOREIGN KEY (anonymous_user_id)
            REFERENCES "anonymous_user" (id),
    CONSTRAINT fk_match
        FOREIGN KEY (match_comment_id)
            REFERENCES "match_comment" (id)
);

CREATE TABLE IF NOT EXISTS public."user_follow"
(
    id             BIGSERIAL NOT NULL,
    user_id        BIGINT    NULL,
    followed_user_id BIGINT    NULL,
    is_followed      BOOLEAN   NOT NULL DEFAULT true,
    "created_at"   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT user_follow_pk PRIMARY KEY (id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES "user" (id),
    CONSTRAINT fk_followed_user
        FOREIGN KEY (followed_user_id)
            REFERENCES "user" (id),
    UNIQUE (user_id, followed_user_id)
);
