create user ssbd01admin password 'admin';
create user ssbd01glassfish password 'glassfishpassword';
create user ssbd01mok password 'mokpassword';
create user ssbd01moa password 'moapassword';

create database ssbd01;

grant all on schema public to ssbd01admin;
