-- CREATE USER gastro_admin WITH PASSWORD 'gastro_admin';

-- Grant USER
grant connect on database gastro_management to gastro_admin;

grant USAGE ON schema public to gastro_admin;
grant INSERT, SELECT, DELETE, UPDATE on ALL TABLES IN schema public to gastro_admin;
grant usage on ALL SEQUENCES IN schema public to gastro_admin;
grant references on all TABLES IN schema public to gastro_admin;

grant all privileges on database gastro_management to gastro_admin;
grant all privileges on ALL TABLES IN schema public to gastro_admin;
grant all privileges on ALL sequences IN schema public to gastro_admin;
grant all privileges on ALL FUNCTIONS IN schema public to gastro_admin;
grant all privileges on schema public to gastro_admin;
