create table if not exists todo (
    id bigserial primary key,
    item varchar(50) not null unique
);
