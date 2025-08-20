CREATE TABLE category (
                          category_id          UUID         NOT NULL,
                          category_name        VARCHAR(255) NOT NULL,
                          category_description VARCHAR(255) NOT NULL,
                          created_on           TIMESTAMP WITHOUT TIME ZONE,
                          updated_on           TIMESTAMP WITHOUT TIME ZONE,
                          delete_yn            VARCHAR(255),
                          CONSTRAINT pk_category PRIMARY KEY (category_id),
                          CONSTRAINT uc_category_categorydescription UNIQUE (category_description),
                          CONSTRAINT uc_category_categoryname UNIQUE (category_name)
);

ALTER TABLE books
    ADD COLUMN category_id UUID;

ALTER TABLE books
    ADD CONSTRAINT uc_books_categoryid UNIQUE (category_id);

ALTER TABLE books
    ADD CONSTRAINT FK_BOOKS_ON_CATEGORYID FOREIGN KEY (category_id)
        REFERENCES category(category_id);
