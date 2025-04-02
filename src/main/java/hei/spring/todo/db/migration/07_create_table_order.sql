do
$$
    begin
		if not exists(select from pg_type where typname = 'type_status') then
			create type "type_status" as enum ('CREATED', 'CONFIRMED', 'IN_PREPARATION', 'COMPLETED', 'DELIVERED', 'CANCELED');
		end if;   end
$$;

create table if not exists orders
(
    id_order           varchar PRIMARY KEY NOT NULL,
	status             type_status NOT NULL,
	created_at         timestamp NOT NULL,
	confirmed_at       timestamp,
	in_preparation_at  timestamp,
	completed_at       timestamp,
	delivered_at       timestamp,
	canceled_at        timestamp
);


-- create or replace function check_status_and_dates_in_order()
-- returns trigger as
-- $$
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
-- 	elsif (new.status = 'CANCELED') then
-- 		if (new.canceled_at is null) then
-- 			new.canceled_at := now();
-- 		end if;
-- 	else
-- 		raise exception 'Invalid status';
-- 	end if;
-- 	return new;
-- end;
-- $$ language plpgsql;

-- create trigger trg_check_status_and_dates_in_order
-- before update on "orders"
-- for each row
-- execute function check_status_and_dates_in_order();
