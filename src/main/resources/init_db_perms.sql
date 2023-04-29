CREATE VIEW public.glassfish_auth_view AS
SELECT account.login,
       account.password,
       al.access_level_role
FROM (public.access_level al
    JOIN public.account ON ((al.account_id = account.id)))
WHERE ((account.confirmed = true) AND (account.active = true) AND (al.active = true));


-- INSERT INTO account (id, creation_date, active, confirmed, email, language,
--                             last_positive_login, last_negative_login, logical_address)
-- values
--     (1, '', true, true, 'test@email.com', '', '', '', '127.0.0.1');

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
