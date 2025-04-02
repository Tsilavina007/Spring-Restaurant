do
$$
    begin
        if not exists(select from pg_type where typname = 'unit') then
            create type "unit" as enum ('G','L', 'U');
        end if;
    end
$$;

create table if not exists ingredient_price
(
    id_ingredient           varchar,
    update_datetime       timestamp,
    unit_price int,
	unit unit,
    foreign key (id_ingredient) references ingredient (id_ingredient)
);
