-- Insert a new order with status 'CREATED'
insert into orders (id_order, status, created_at) values ('order_1', 'CREATED', now());

-- Update the order to 'CONFIRMED'
update orders set status = 'CONFIRMED' where id_order = 'order_1';

-- Update the order to 'IN_PREPARATION'
-- update "order" set status = 'IN_PREPARATION' where id_order = 'order_1';

-- Update the order to 'COMPLETED'
-- update "order" set status = 'COMPLETED' where id_order = 'order_1';

-- Update the order to 'DELIVERED'
-- update "order" set status = 'DELIVERED' where id_order = 'order_1';

-- Insert another order and cancel it
-- insert into "order" (id_order, status, created_at) values ('order_2', 'CREATED', now());
-- update "order" set status = 'CANCELED' where id_order = 'order_2';


-- Insert a new dish order with status 'CREATED'
insert into dish_order (id_dish, id_order, quantity, status, created_at)
values ('1', 'order_1', 2, 'CREATED', now());

insert into dish_order (id_dish, id_order, quantity, status, created_at)
values ('tacos_id', 'order_1', 1, 'CREATED', now());

-- Update the dish order to 'CONFIRMED'
update dish_order set status = 'CONFIRMED' where id_dish = '1' and id_order = 'order_1';
update dish_order set status = 'CONFIRMED' where id_dish = 'tacos_id' and id_order = 'order_1';

-- Update the dish order to 'IN_PREPARATION'
update dish_order set status = 'IN_PREPARATION' where id_dish = '1' and id_order = 'order_1';
update dish_order set status = 'IN_PREPARATION' where id_dish = 'tacos_id' and id_order = 'order_1';


-- Update the dish order to 'COMPLETED'
update dish_order set status = 'COMPLETED' where id_dish = '1' and id_order = 'order_1';

-- Update the dish order to 'DELIVERED'
update dish_order set status = 'DELIVERED' where id_dish = '1' and id_order = 'order_1';

-- Insert another dish order and cancel it

-- update dish_order set status = 'CANCELED' where id_dish = 'dish_2' and id_order = 'order_1';
