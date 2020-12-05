CREATE TABLE link_category
(
    id      VARCHAR(36)  NOT NULL UNIQUE,
    name    VARCHAR(255) NOT NUll UNIQUE,
    user_id VARCHAR(36)  NOT NULL,
    KEY `category_to_user` (`user_id`),
    CONSTRAINT `category_to_user` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`),
    PRIMARY KEY (id)
) ENGINE = INNODB;

CREATE TABLE link
(
    id          VARCHAR(36) NOT NULL UNIQUE,
    url         TEXT        NOT NULL,
    description TEXT,
    status      VARCHAR(50) NOT NULL,
    category_id VARCHAR(36) NOT NULL,
    user_id     VARCHAR(36) NOT NULL,
    PRIMARY KEY (id),
    KEY `link_to_category` (`category_id`),
    CONSTRAINT `link_to_category` FOREIGN KEY (`category_id`) REFERENCES `link_category` (`id`),
    KEY `link_to_user` (`user_id`),
    CONSTRAINT `link_to_user` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`)
) ENGINE = INNODB;
