-- CREATE VIEW public.glassfish_auth_view AS SELECT account.login, account.password, al.access_level_role FROM (public.access_level al JOIN public.account ON ((al.account_id = account.id)));
CREATE VIEW public.glassfish_auth_view AS SELECT account.login, account.password, al.access_level_role FROM (public.access_level al JOIN public.account ON ((al.account_id = account.id))) WHERE ((account.confirmed = true) AND (account.active = true) AND (al.active = true));

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- Name: TABLE access_level; Type: ACL; Schema: public; Owner: ssbd01admin
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.access_level TO ssbd01mok;
GRANT SELECT ON TABLE public.access_level TO ssbd01moa;


--
-- Name: TABLE account; Type: ACL; Schema: public; Owner: ssbd01admin
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.account TO ssbd01mok;
GRANT SELECT ON TABLE public.account TO ssbd01moa;


--
-- Name: TABLE admin_data; Type: ACL; Schema: public; Owner: ssbd01admin
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.admin_data TO ssbd01mok;
GRANT SELECT ON TABLE public.admin_data TO ssbd01moa;


--
-- Name: TABLE category; Type: ACL; Schema: public; Owner: ssbd01admin
--

GRANT SELECT ON TABLE public.category TO ssbd01mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.category TO ssbd01moa;


--
-- Name: TABLE chemist_data; Type: ACL; Schema: public; Owner: ssbd01admin
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.chemist_data TO ssbd01mok;
GRANT SELECT ON TABLE public.chemist_data TO ssbd01moa;


--
-- Name: TABLE glassfish_auth_view; Type: ACL; Schema: public; Owner: ssbd01admin
--

GRANT SELECT ON TABLE public.glassfish_auth_view TO ssbd01glassfish;


--
-- Name: TABLE medication; Type: ACL; Schema: public; Owner: ssbd01admin
--

GRANT SELECT ON TABLE public.medication TO ssbd01mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.medication TO ssbd01moa;


--
-- Name: TABLE order_medication; Type: ACL; Schema: public; Owner: ssbd01admin
--

GRANT SELECT ON TABLE public.order_medication TO ssbd01mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.order_medication TO ssbd01moa;


--
-- Name: TABLE patient_data; Type: ACL; Schema: public; Owner: ssbd01admin
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.patient_data TO ssbd01mok;
GRANT SELECT ON TABLE public.patient_data TO ssbd01moa;


--
-- Name: TABLE patient_order; Type: ACL; Schema: public; Owner: ssbd01admin
--

GRANT SELECT ON TABLE public.patient_order TO ssbd01mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.patient_order TO ssbd01moa;


--
-- Name: TABLE prescription; Type: ACL; Schema: public; Owner: ssbd01admin
--

GRANT SELECT ON TABLE public.prescription TO ssbd01mok;
GRANT SELECT,INSERT,DELETE ON TABLE public.prescription TO ssbd01moa;


--
-- Name: TABLE shipment; Type: ACL; Schema: public; Owner: ssbd01admin
--

GRANT SELECT ON TABLE public.shipment TO ssbd01mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.shipment TO ssbd01moa;


--
-- Name: TABLE shipment_medication; Type: ACL; Schema: public; Owner: ssbd01admin
--

GRANT SELECT ON TABLE public.shipment_medication TO ssbd01mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.shipment_medication TO ssbd01moa;

--
-- Name: TABLE account_id_seq; Type: ACL; Schema: public; Owner: ssbd01mok
--

GRANT SELECT, UPDATE ON TABLE public.account_id_seq TO ssbd01mok;

--
-- Name: TABLE access_level_id_seq; Type: ACL; Schema: public; Owner: ssbd01mok
--

GRANT SELECT, UPDATE ON TABLE public.access_level_id_seq TO ssbd01mok;

insert into account (active, confirmed, created_by, creation_date, email, language, last_negative_login, last_positive_login, logical_address, login,incorrect_login_attempts, modification_date, modified_by, password, version) values (true, true, null, now(), 'admin@o2.pl', 'en', null, null, null, 'admin123', 0, null, null,  'b03ddf3ca2e714a6548e7495e2a03f5e824eaac9837cd7f159c67b90fb4b7342', 0);
insert into account (active, confirmed, created_by, creation_date, email, language, last_negative_login, last_positive_login, logical_address, login,incorrect_login_attempts, modification_date, modified_by, password, version) values (true, true, null, now(), 'chemist@o2.pl', 'en', null, null, null, 'chemist123', 0, null, null,  '52d7a0431ddd469b9e0929a09ef67fefe99e3511893edb0c2fa0b09892df1e52', 0);

insert into access_level (account_id, active, created_by, creation_date, modification_date, modified_by, version, access_level_role) values (1, true, null, now(), null, null, 0, 'ADMIN');
insert into access_level (account_id, active, created_by, creation_date, modification_date, modified_by, version, access_level_role) values (2, true, null, now(), null, null, 0, 'CHEMIST');
insert into access_level (account_id, active, created_by, creation_date, modification_date, modified_by, version, access_level_role) values (1, true, null, now(), null, null, 0, 'CHEMIST');

insert into admin_data (id, work_phone_number) values (1, '123456789');
insert into chemist_data (id, license_number) values (2, '456456');
insert into chemist_data (id, license_number) values (1, '123123');

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.token TO ssbd01mok;

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.token_id_seq TO ssbd01mok;
