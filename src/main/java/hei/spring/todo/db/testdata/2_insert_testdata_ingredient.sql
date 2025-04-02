insert into ingredient (id_ingredient, name) values
('1', 'Saucisse'),
('2', 'Huile'),
('3', 'Oeuf'),
('4', 'Pain');

insert into ingredient_price (id_ingredient, unit_price, unit, update_datetime) values
('1', 40, 'G', '2024-01-01 00:00:00'),
('1', 20, 'G', '2025-01-01 00:00:00'),
('2', 10000, 'L', '2025-01-01 00:00:00'),
('3', 1000, 'U', '2025-01-01 00:00:00'),
('4', 1000, 'U', '2025-01-01 00:00:00');

insert into movement (id_mvm, id_ingredient, quantity_mvm, unit, type_mvm, update_datetime) values
('3', '3', 100, 'U', 'IN',  '2025-02-01 08:00:00'),
('4', '4', 50, 'U', 'IN',  '2025-02-01 08:00:00'),
('1', '1', 10000, 'G', 'IN',  '2025-02-01 08:00:00'),
('2', '2', 20, 'L', 'IN',  '2025-02-01 08:00:00');

insert into movement (id_mvm, id_ingredient, quantity_mvm, unit, type_mvm, update_datetime) values
('5', '3', 10, 'U', 'OUT',  '2025-02-02 10:00:00'),
('6', '3', 10, 'U', 'OUT',  '2025-02-03 15:00:00'),
('7', '4', 20, 'U', 'OUT',  '2025-02-05 16:00:00');

