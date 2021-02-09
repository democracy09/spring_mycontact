insert into person(id, name, age, blood_type, year_of_birthday, month_of_birthday, day_of_birthday, job) values (1, 'aa', 10, 'A', 1991, 8, 15, 'programmer');
insert into person(id, name, age, blood_type, year_of_birthday, month_of_birthday, day_of_birthday) values (2, 'bb', 9, 'B', 1992, 7, 21);
insert into person(id, name, age, blood_type, year_of_birthday, month_of_birthday, day_of_birthday) values (3, 'cc', 8, 'O', 1993, 10, 15);
insert into person(id, name, age, blood_type, year_of_birthday, month_of_birthday, day_of_birthday) values (4, 'dd', 7, 'AB', 1994, 5, 7);
insert into person(id, name, age, blood_type, year_of_birthday, month_of_birthday, day_of_birthday) values (5, 'ee', 6, 'A', 19955, 8, 6);

insert into block(id, name) values (1, 'bb');
insert into block(id, name) values (2, 'dd');

update person set block_id = 1 where id = 2;
update person set block_id = 2 where id = 4;
