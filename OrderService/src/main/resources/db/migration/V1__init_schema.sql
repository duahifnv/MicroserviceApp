create table orders(
    id bigserial primary key,
    order_number varchar(255) default null,
    sku_code varchar(255),
    price decimal(19, 2),
    quantity int
);