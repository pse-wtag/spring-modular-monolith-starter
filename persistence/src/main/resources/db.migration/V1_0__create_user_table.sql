CREATE TABLE `_user`
(
    id                 VARCHAR(36)  NOT NULL,
    username           VARCHAR(255) NOT NULL UNIQUE,
    email              VARCHAR(255) NOT NULL,
    firstname          VARCHAR(50)  NOT NULL,
    lastname           VARCHAR(50)  NOT NULL,
    age                INT          NOT NULL,
    gender             VARCHAR(10)  NOT NULL CHECK (gender IN ('FEMALE', 'MALE', 'OTHER')),
    role               VARCHAR(10)  NOT NULL CHECK (role IN ('ADMIN', 'USER')),
    keycloak_id        VARCHAR(255) NOT NULL UNIQUE,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP(6) NOT NULL,
    modified_by        VARCHAR(255),
    last_modified_date TIMESTAMP(6) NOT NULL,

    PRIMARY KEY (id)
) ENGINE = InnoDB