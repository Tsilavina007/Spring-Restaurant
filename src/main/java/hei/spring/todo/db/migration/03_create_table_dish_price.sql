create table if not exists dish_price
(
    id_dish           varchar PRIMARY KEY,
    update_datetime       timestamp,
    unit_price int,
    foreign key (id_dish) references dish (id_dish)
);
