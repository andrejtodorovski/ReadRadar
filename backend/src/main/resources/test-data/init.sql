insert into role (id, name)
VALUES (1, 'USER'),
       (2, 'ADMIN');

insert into users_table (id, username, password, email, profile_picture, role_id)
VALUES (1, 'user', 'user', 'user@mail.com', 'test.jpg', 1);

insert into book (id, title, isbn, author, description, cover_image, average_rating, view_count)
VALUES (10, 'Test Book', '123456789321', 'Test Author', 'Test Description', 'test.jpg', 0, 1590),
       (20, 'Test Book 2', '123456789322', 'Test Author2', 'Test Description2', 'test2.jpg', 0, 0);

insert into review (id, rating, comment, book_id, user_id)
VALUES (1000, 4.5, 'Test', 20, 1);

insert into category(id, name, description)
VALUES (10, 'Romance', 'Test Description'),
       (20, 'Test Category 2', 'Test Description 2');

insert into book_category (id, book_id, category_id)
VALUES (10, 10, 10),
       (20, 20, 20);
