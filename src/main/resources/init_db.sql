
CREATE VIEW public.glassfish_auth_view AS SELECT account.login, account.password, al.access_level_role FROM (public.access_level al JOIN public.account ON ((al.account_id = account.id))) WHERE ((account.confirmed = true) AND (account.active = true) AND (al.active = true));

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;

GRANT SELECT,INSERT,UPDATE ON TABLE public.access_level TO ssbd01mok;
GRANT SELECT ON TABLE public.access_level TO ssbd01moa;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.account TO ssbd01mok;
GRANT SELECT ON TABLE public.account TO ssbd01moa;


GRANT SELECT,INSERT,UPDATE ON TABLE public.admin_data TO ssbd01mok;
GRANT SELECT ON TABLE public.admin_data TO ssbd01moa;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.category TO ssbd01moa;


GRANT SELECT,INSERT,UPDATE ON TABLE public.chemist_data TO ssbd01mok;
GRANT SELECT ON TABLE public.chemist_data TO ssbd01moa;


GRANT SELECT ON TABLE public.glassfish_auth_view TO ssbd01glassfish;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.medication TO ssbd01moa;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.order_medication TO ssbd01moa;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.patient_data TO ssbd01mok;
GRANT SELECT ON TABLE public.patient_data TO ssbd01moa;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.patient_order TO ssbd01moa;


GRANT SELECT,INSERT,DELETE ON TABLE public.prescription TO ssbd01moa;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.shipment TO ssbd01moa;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.shipment_medication TO ssbd01moa;

GRANT SELECT, UPDATE ON TABLE public.account_id_seq TO ssbd01mok;

GRANT SELECT, UPDATE ON TABLE public.access_level_id_seq TO ssbd01mok;

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.token TO ssbd01mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.token_id_seq TO ssbd01mok;

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.category_id_seq TO ssbd01moa;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.medication_id_seq TO ssbd01moa;

GRANT SELECT, UPDATE ON TABLE public.order_medication_id_seq TO ssbd01moa;
GRANT SELECT, UPDATE ON TABLE public.shipment_id_seq TO ssbd01moa;
GRANT SELECT, UPDATE ON TABLE public.shipment_medication_id_seq TO ssbd01moa;

insert into account (active, confirmed, created_by, creation_date, email, language, last_negative_login, last_positive_login, logical_address, login,incorrect_login_attempts, modification_date, modified_by, password, version) values (true, true, null, now(), 'admin@o2.pl', 'en', null, null, null, 'admin123', 0, null, null,  'b03ddf3ca2e714a6548e7495e2a03f5e824eaac9837cd7f159c67b90fb4b7342', 0);
insert into account (active, confirmed, created_by, creation_date, email, language, last_negative_login, last_positive_login, logical_address, login,incorrect_login_attempts, modification_date, modified_by, password, version) values (true, true, null, now(), 'chemist@o2.pl', 'en', null, null, null, 'chemist123', 0, null, null,  '52d7a0431ddd469b9e0929a09ef67fefe99e3511893edb0c2fa0b09892df1e52', 0);
insert into account (active, confirmed, created_by, creation_date, email, language, last_negative_login, last_positive_login, logical_address, login, incorrect_login_attempts, modification_date, modified_by, password, version) values (true, true, null, now(), 'patient@o2.pl', 'en', null, null, null, 'patient123', 0, null, null,'b03ddf3ca2e714a6548e7495e2a03f5e824eaac9837cd7f159c67b90fb4b7342', 0);

insert into access_level (account_id, active, created_by, creation_date, modification_date, modified_by, version, access_level_role) values (1, true, null, now(), null, null, 0, 'ADMIN');
insert into access_level (account_id, active, created_by, creation_date, modification_date, modified_by, version, access_level_role) values (2, true, null, now(), null, null, 0, 'CHEMIST');
insert into access_level (account_id, active, created_by, creation_date, modification_date, modified_by, version, access_level_role) values (1, true, null, now(), null, null, 0, 'CHEMIST');
insert into access_level (account_id, active, created_by, creation_date, modification_date, modified_by, version, access_level_role) values (3, true, null, now(), null, null, 0, 'PATIENT');

insert into admin_data (id, work_phone_number) values (1, '123456789');
insert into chemist_data (id, license_number) values (2, '456456');
insert into chemist_data (id, license_number) values (1, '123123');
insert into patient_data (nip, first_name, last_name, pesel, phone_number, id) values (7777777777, 'testowy', 'pacjent', '77777777777', '777777777', 2);
insert into patient_data (id, pesel, nip, phone_number, first_name, last_name) values (3, '22344678801', '2234557890', '721545784', 'Jan', 'Kowalski');

insert into category (id, creation_date, modification_date, version, isonprescription, name, created_by, modified_by) VALUES (1, now(), null, 1, false, 'leki', null, null);
insert into category (id, name, created_by, creation_date, modification_date, modified_by, version, isOnPrescription) values (2, 'antidepressant', null, now(), null, null, 0, true);
insert into category (id, name, created_by, creation_date, modification_date, modified_by, version, isOnPrescription) values (3, 'vitamins', null, now(), null, null, 0, true);

insert into medication (id, medication_name, price, category_id, created_by, creation_date, modification_date, modified_by, version, stock) values (1, 'Prozac', 10, 1, null, now(), null, null, 0, 100);
insert into medication (id, medication_name, price, category_id, created_by, creation_date, modification_date, modified_by, version, stock) values (2, 'Zoloft', 20, 1, null, now(), null, null, 0, 150);
insert into medication (id, medication_name, price, category_id, created_by, creation_date, modification_date, modified_by, version, stock) values (3, 'Marsjanki', 25, 1, null, now(), null, null, 0, 400);
insert into medication (id, creation_date, modification_date, version, medication_name, price, stock, created_by, modified_by, category_id) VALUES (4, now(), null, 0, 'testlek', 10, 10, null, null, 1);

insert into prescription (id, creation_date, modification_date, version, prescription_number, created_by, modified_by, patient_data_id) values (1, now(), null, 0, 0, null, null, 2);
insert into prescription (id, created_by, creation_date, modification_date, modified_by, version, prescription_number, patient_data_id) VALUES (2, null, now(), null, null, 0, '123456789', 3);

insert into patient_order (id, patient_data_id, order_date, in_queue, created_by, creation_date, modification_date, modified_by, version, chemist_data_id, prescription_id) values (1, 3, now(), true, null, now(), null, null, 0, 2, 1);
insert into patient_order (id, patient_data_id, order_date, in_queue, created_by, creation_date, modification_date, modified_by, version, chemist_data_id) values (2, 3, now(), true, null, now(), null, null, 0, 2);

insert into order_medication (id, order_id, medication_id, quantity, created_by, creation_date, modification_date, modified_by, version) values (1, 1, 1, 2, null, now(), null, null, 0);
insert into order_medication (id, order_id, medication_id, quantity, created_by, creation_date, modification_date, modified_by, version) values (2, 1, 2, 4, null, now(), null, null, 0);
insert into order_medication (id, order_id, medication_id, quantity, created_by, creation_date, modification_date, modified_by, version) values (3, 2, 3, 20, null, now(), null, null, 0);
