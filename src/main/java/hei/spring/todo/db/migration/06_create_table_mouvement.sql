do
$$
    begin
        if not exists(select from pg_type where typname = 'type_mvm') then
            create type "type_mvm" as enum ('IN','OUT');
        end if;
    end
$$;

create table if not exists movement
(
    id_mvm           varchar PRIMARY KEY NOT NULL,
    quantity_mvm       float,
    unit unit,
	type_mvm type_mvm,
	update_datetime  timestamp,
	id_ingredient varchar,
    foreign key (id_ingredient) references ingredient (id_ingredient)
);
