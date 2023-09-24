create table if not exists book
(
    id
    bigserial
    primary
    key,
    author
    varchar
(
    255
),
    average_rating double precision,
    cover_image varchar
(
    255
),
    description text,
    isbn varchar
(
    255
),
    title varchar
(
    255
),
    view_count bigint
    );


create table if not exists category
(
    id
    bigserial
    primary
    key,
    description
    varchar
(
    255
),
    name varchar
(
    255
)
    );


create table if not exists book_category
(
    id
    bigserial
    primary
    key,
    book_id
    bigint
    constraint
    fknyegcbpvce2mnmg26h0i856fd
    references
    book,
    category_id
    bigint
    constraint
    fkam8llderp40mvbbwceqpu6l2s
    references
    category
);


create table if not exists role
(
    id
    bigserial
    primary
    key,
    name
    varchar
(
    255
)
    );


create table if not exists users_table
(
    id
    bigserial
    primary
    key,
    email
    varchar
(
    255
),
    password varchar
(
    255
),
    profile_picture varchar
(
    255
),
    username varchar
(
    255
),
    role_id bigint
    constraint fk29ygrvstobx8rtmry3897vl2m
    references role
    );


create table if not exists review
(
    id
    bigserial
    primary
    key,
    rating
    double
    precision
    not
    null,
    timestamp
    timestamp
(
    6
),
    book_id bigint
    constraint fk70yrt09r4r54tcgkrwbeqenbs
    references book,
    user_id bigint
    constraint fki4tyego8mhfnay51uddh99cnl
    references users_table,
    comment varchar
(
    255
)
    );


