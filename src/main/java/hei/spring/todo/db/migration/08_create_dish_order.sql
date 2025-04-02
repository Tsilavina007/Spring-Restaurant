create table if not exists dish_order
(
    id_dish           varchar NOT NULL,
	id_order                varchar NOT NULL,
	quantity                int NOT NULL,
	status                  type_status NOT NULL,
	created_at              timestamp NOT NULL,
	confirmed_at            timestamp,
	in_preparation_at       timestamp,
	completed_at            timestamp,
	delivered_at            timestamp,
	canceled_at             timestamp,
	primary key (id_dish, id_order),
	foreign key (id_order) references "orders" (id_order),
	foreign key (id_dish) references dish (id_dish)
);

-- create or replace function check_status_and_dates()
-- returns trigger as $$
-- begin
-- 	if (new.status = old.status) then
-- 		return new;
-- 	end if;
-- 	if (new.status = 'CONFIRMED') then
-- 		if (new.created_at is null) then
-- 			raise exception 'created_at cannot be null when status is CONFIRMED';
-- 		end if;
-- 		if (old.status is distinct from 'CREATED') then
-- 			raise exception 'Invalid status transition to CONFIRMED';
-- 		end if;
-- 		if (new.confirmed_at is null) then
-- 			new.confirmed_at := now();
-- 		end if;
-- 		perform check_and_update_order_status(0, new.id_order, 'CONFIRMED');
-- 	elsif (new.status = 'IN_PREPARATION') then
-- 		if (new.confirmed_at is null) then
-- 			raise exception 'confirmed_at cannot be null when status is IN_PREPARATION';
-- 		end if;
-- 		if (old.status is distinct from 'CONFIRMED') then
-- 			raise exception 'Invalid status transition to IN_PREPARATION';
-- 		end if;
-- 		if (new.in_preparation_at is null) then
-- 			new.in_preparation_at := now();
-- 		end if;
-- 		-- perform check_and_update_order_status(0, new.id_order, 'IN_PREPARATION');
-- 	elsif (new.status = 'COMPLETED') then
-- 		if (new.in_preparation_at is null) then
-- 			raise exception 'in_preparation_at cannot be null when status is COMPLETED';
-- 		end if;
-- 		if (old.status is distinct from 'IN_PREPARATION') then
-- 			raise exception 'Invalid status transition to COMPLETED';
-- 		end if;
-- 		if (new.completed_at is null) then
-- 			new.completed_at := now();
-- 		end if;
-- 		perform check_and_update_order_status(0, new.id_order, 'COMPLETED');
-- 	elsif (new.status = 'DELIVERED') then
-- 		if (new.completed_at is null) then
-- 			raise exception 'completed_at cannot be null when status is DELIVERED';
-- 		end if;
-- 		if (old.status is distinct from 'COMPLETED') then
-- 			raise exception 'Invalid status transition to DELIVERED';
-- 		end if;
-- 		if (new.delivered_at is null) then
-- 			new.delivered_at := now();
-- 		end if;
-- 		perform check_and_update_order_status(0, new.id_order, 'DELIVERED');
-- 	elsif (new.status = 'CANCELED') then
-- 		if (new.canceled_at is null) then
-- 			new.canceled_at := now();
-- 		end if;
-- 	elsif (new.status = 'CREATED') then
-- 		-- do nothing
-- 	else
-- 		raise exception 'Invalid status';
-- 	end if;
-- 	return new;
-- end;
-- $$ language plpgsql;

-- create or replace function check_and_update_order_status(remaining_count int, order_id varchar, new_status varchar)
-- returns void as $$
-- begin
-- 	if new_status = 'CONFIRMED' then
-- 		select count(*) into remaining_count
-- 		from dish_order
-- 		where id_order = order_id and status = 'CREATED';
-- 		if remaining_count = 1 then
-- 			update "orders" set status = 'CONFIRMED' where id_order = order_1;
-- 		end if;
-- 	elsif new_status = 'IN_PREPARATION' then
-- 		select count(*) into remaining_count
-- 		from dish_order
-- 		where id_order = order_id and status = 'CONFIRMED';
-- 		if remaining_count = 1 then
-- 			update "orders" set status = 'IN_PREPARATION' where id_order = order_id;
-- 		end if;
-- 	elsif new_status = 'COMPLETED' then
-- 		select count(*) into remaining_count
-- 		from dish_order
-- 		where id_order = order_id and status = 'IN_PREPARATION';
-- 		if remaining_count = 1 then
-- 			update "orders" set status = 'COMPLETED' where id_order = order_id;
-- 		end if;
-- 	elsif new_status = 'DELIVERED' then
-- 		select count(*) into remaining_count
-- 		from dish_order
-- 		where id_order = order_id and status = 'COMPLETED';
-- 		if remaining_count = 1 thenwev
-- 			update "orders" set status = 'DELIVERED' where id_order = order_id;
-- 		end if;
-- 	end if;
-- end;
-- $$ language plpgsql;

-- create trigger trg_check_status_and_dates_in_dish_order
-- before insert or update on dish_order
-- for each row
-- execute function check_status_and_dates();


-- DROP FUNCTION IF EXISTS check_and_update_order_status(character varying, character varying) CASCADE;
-- DROP FUNCTION IF EXISTS check_and_update_order_status(integer, character varying, character varying) CASCADE;
-- DROP FUNCTION IF EXISTS check_status_and_dates() CASCADE;
-- DROP FUNCTION IF EXISTS check_status_and_dates_in_order() CASCADE;

-- drop trigger if exists trg_check_status_and_dates_in_dish_order on dish_order;
-- drop trigger if exists trg_check_status_and_dates_in_order on orders;
