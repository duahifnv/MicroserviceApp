create table orders(
    id bigserial primary key,
    orderNumber varchar(255) default null,
    skuCode varchar(255),
    price decimal(19, 2),
    quantity int
);