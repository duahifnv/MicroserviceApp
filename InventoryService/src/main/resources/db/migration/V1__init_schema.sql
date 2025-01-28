create table inventory(
    id bigserial primary key,
    sku_code varchar(255) default null,
    quantity int default null
)