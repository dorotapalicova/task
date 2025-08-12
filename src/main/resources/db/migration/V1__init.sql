CREATE TABLE users
(
    name          VARCHAR(255) PRIMARY KEY,
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    email_address VARCHAR(255),
    birth_date    DATE,
    registered_on DATE
);

CREATE TABLE user_organization_unit
(
    user_name         VARCHAR(255) NOT NULL,
    organization_unit VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_name, organization_unit),
    CONSTRAINT fk_user_org_user FOREIGN KEY (user_name) REFERENCES users (name) ON DELETE CASCADE
);

CREATE TABLE policies
(
    id   VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE policy_conditions
(
    policy_id       VARCHAR(255) NOT NULL,
    condition_key   VARCHAR(255) NOT NULL,
    condition_value VARCHAR(255),
    PRIMARY KEY (policy_id, condition_key),
    CONSTRAINT fk_policy_conditions_policy FOREIGN KEY (policy_id) REFERENCES policies (id) ON DELETE CASCADE
);

CREATE TABLE user_policy
(
    user_name VARCHAR(255) NOT NULL,
    policy_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_name, policy_id),
    CONSTRAINT fk_user_policy_user FOREIGN KEY (user_name) REFERENCES users (name) ON DELETE CASCADE
);
