do
$$
    begin
        if not exists(select from pg_type where typname = 'unit') then
            create type "unit" as enum ('G','L', 'U');
        end if;
    end
$$;

create table if not exists dish_ingredient
(
    id_ingredient           varchar NOT NULL,
    id_dish                 varchar NOT NULL,
    required_quantity       float,
    unit unit,
	primary key (id_ingredient, id_dish),
    foreign key (id_ingredient) references ingredient (id_ingredient),
	foreign key (id_dish) references dish (id_dish)
);
