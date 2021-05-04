CREATE SEQUENCE table_nodes_id_seq;

CREATE TABLE IF NOT EXISTS nodes (
    id                int constraint node_pkey  PRIMARY KEY DEFAULT nextval('table_nodes_id_seq'),
    name              character varying(45) NOT NULL,
    parent_id         int,
    available         boolean               NOT NULL,
    creation_datetime timestamptz           NOT NULL
);

CREATE TABLE IF NOT EXISTS directories (
    id int not null
    constraint directories_pkey
    primary key
    constraint directories_nodes_id_fk
    references nodes ON DELETE CASCADE
);

CREATE SEQUENCE table_types_id_seq;

CREATE TYPE   importance_type AS ENUM ('low','middle','high');

CREATE TABLE IF NOT EXISTS types (
    id   int PRIMARY KEY DEFAULT nextval('table_types_id_seq'),
    name character varying(45) UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS documents (
                                         id             int                   not null
            constraint documents_pkey
               primary key
            constraint documents_nodes_id_fk
                references nodes ON DELETE CASCADE,
                                         description    character varying(45) NOT NULL,
                                         type_id        int REFERENCES types (id),
                                         importance     importance_type        NOT NULL,
                                         version_number int                   NOT NULL,
                                         verified       boolean               NOT NULL,
                                         previous_version_id int REFERENCES documents (id) ON DELETE SET NULL
);

CREATE SEQUENCE table_users_id_seq;

CREATE TYPE user_role AS ENUM ('user','admin');

CREATE TABLE IF NOT EXISTS users (
    id            int PRIMARY KEY DEFAULT nextval('table_users_id_seq'),
    login         character varying(45) NOT NULL,
    password_hash character varying(100) NOT NULL,
    role          user_role              NOT NULL,
    mail          character varying(45) NOT NULL
);

CREATE TYPE access_type AS ENUM ('none','read','write','verify');

CREATE SEQUENCE table_permissions_id_seq;

CREATE TABLE IF NOT EXISTS  permissions
(
    id      int PRIMARY KEY DEFAULT nextval('table_permissions_id_seq'),
    user_id int         NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    access  access_type NOT NULL,
    node_id int         NOT NULL REFERENCES nodes (id) ON DELETE CASCADE
);

CREATE SEQUENCE table_files_id_seq;

CREATE TABLE IF NOT EXISTS files
(
    id                int PRIMARY KEY DEFAULT nextval('table_files_id_seq'),
    creation_datetime timestamptz           NOT NULL,
    document_id       int                   NOT NULL,
    name              character varying(45) NOT NULL
);

CREATE EXTENSION pgcrypto;