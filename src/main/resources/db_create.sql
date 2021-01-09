-- Schema
create schema if not exists staticid;
-- Component Type table
create table if not exists staticid.component_type
(
    id         serial      not null
        constraint component_type_pk primary key,
    name       text        not null,
    created_at timestamptz not null default now(),
    deleted_at timestamptz
);
-- Role table
create table if not exists staticid.app_role
(
    id         serial
        constraint app_role_pk primary key,
    name       text        not null,
    created_at timestamptz not null default now(),
    deleted_at timestamptz
);
-- User table
create table if not exists staticid.app_user
(
    id         serial      not null
        constraint app_user_pk primary key,
    username   text unique not null,
    passwd     text        not null,
    first_name text,
    last_name  text,
    email      text unique not null,
    created_at timestamptz not null default now(),
    deleted_at timestamptz
);
-- User Role join table
create table if not exists staticid.app_user_role
(
    id      serial not null
        constraint app_user_role_pk primary key,
    user_id integer
        constraint fk_app_user_id references app_user (id),
    role_id integer
        constraint fk_app_role_id references app_role (id)
);
-- Created Static ID table
create table if not exists staticid.created_id
(
    id             serial
        constraint created_id_pk primary key,
    component_id   integer     not null
        constraint fk_component_type_id references component_type (id),
    id_value       text        not null unique,
    create_user_id integer     not null
        constraint fk_user_id references app_user (id),
    created_at     timestamptz not null default now(),
    deleted_at     timestamptz
);
