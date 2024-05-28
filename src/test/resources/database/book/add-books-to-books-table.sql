INSERT INTO books (id, title, author, isbn, price, description, cover_image)
VALUES (1, 'Dragon Lance', 'Laura & Tracy Hickman', '978-0-306-40615-7', 10.99, 'Adventures of Red Wizard', 'https://example.com/updated-cover-image.jpg');
INSERT INTO books (id, title, author, isbn, price, description, cover_image)
VALUES (2, 'Dragon Lance 2', 'Laura & Tracy Hickman', '978-0-306-40611-7', 11.99, 'Adventures of Red Wizard', 'https://example.com/updated-cover-image.jpg');
INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);
INSERT INTO books_categories (book_id, category_id) VALUES (2, 1);