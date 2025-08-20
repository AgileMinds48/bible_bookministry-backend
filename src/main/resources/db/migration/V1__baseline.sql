--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.5

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: books; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.books (
    book_id uuid NOT NULL,
    amount_in_stock integer,
    amount_sold integer,
    book_author character varying(255) NOT NULL,
    book_category character varying(255),
    book_description text NOT NULL,
    book_price numeric(38,2) NOT NULL,
    book_title character varying(255) NOT NULL,
    book_value numeric(38,2),
    created_on timestamp(6) without time zone,
    delete_yn character varying(255),
    is_available boolean NOT NULL,
    media jsonb,
    quantity integer,
    updated_on timestamp(6) without time zone,
    employee_id uuid,
    CONSTRAINT books_book_category_check CHECK (((book_category)::text = ANY ((ARRAY['RELIGIOUS'::character varying, 'COMMENTARY'::character varying, 'CHURCH_HISTORY'::character varying, 'THEOLOGICAL'::character varying, 'COMMENTARIES'::character varying, 'BIBLES'::character varying, 'CHILDREN'::character varying])::text[]))),
    CONSTRAINT books_delete_yn_check CHECK (((delete_yn)::text = ANY ((ARRAY['Y'::character varying, 'N'::character varying])::text[])))
);


ALTER TABLE public.books OWNER TO postgres;

--
-- Name: customer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customer (
    customer_id uuid NOT NULL,
    delete_yn character varying(255),
    user_id uuid NOT NULL,
    CONSTRAINT customer_delete_yn_check CHECK (((delete_yn)::text = ANY ((ARRAY['Y'::character varying, 'N'::character varying])::text[])))
);


ALTER TABLE public.customer OWNER TO postgres;

--
-- Name: customer_order; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customer_order (
    order_id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    delete_yn character varying(255),
    order_reference character varying(255),
    order_status character varying(255),
    total_price numeric(38,2),
    updated_at timestamp(6) without time zone,
    customer_id_customer_id uuid,
    order_payment_paymentid uuid,
    CONSTRAINT customer_order_delete_yn_check CHECK (((delete_yn)::text = ANY ((ARRAY['Y'::character varying, 'N'::character varying])::text[]))),
    CONSTRAINT customer_order_order_status_check CHECK (((order_status)::text = ANY ((ARRAY['PENDING'::character varying, 'IN_CART'::character varying, 'PAID'::character varying, 'CANCELLED'::character varying, 'SHIPPED'::character varying, 'DELIVERED'::character varying, 'FAILED'::character varying])::text[])))
);


ALTER TABLE public.customer_order OWNER TO postgres;

--
-- Name: customer_order_order_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customer_order_order_items (
    customer_orders_order_id uuid NOT NULL,
    order_items_order_item_id uuid NOT NULL
);


ALTER TABLE public.customer_order_order_items OWNER TO postgres;

--
-- Name: employee; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.employee (
    employee_id uuid NOT NULL,
    delete_yn character varying(255),
    user_id uuid NOT NULL,
    CONSTRAINT employee_delete_yn_check CHECK (((delete_yn)::text = ANY ((ARRAY['Y'::character varying, 'N'::character varying])::text[])))
);


ALTER TABLE public.employee OWNER TO postgres;

--
-- Name: order_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.order_item (
    order_item_id uuid NOT NULL,
    delete_yn character varying(255),
    quantity integer,
    total numeric(38,2) NOT NULL,
    unit_price numeric(38,2) NOT NULL,
    book_id uuid,
    order_id uuid,
    CONSTRAINT order_item_delete_yn_check CHECK (((delete_yn)::text = ANY ((ARRAY['Y'::character varying, 'N'::character varying])::text[])))
);


ALTER TABLE public.order_item OWNER TO postgres;

--
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    role_id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    delete_yn character varying(255),
    role_name character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone,
    user_id uuid NOT NULL,
    CONSTRAINT role_delete_yn_check CHECK (((delete_yn)::text = ANY ((ARRAY['Y'::character varying, 'N'::character varying])::text[])))
);


ALTER TABLE public.role OWNER TO postgres;

--
-- Name: user_payments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_payments (
    paymentid uuid NOT NULL,
    amount numeric(38,2),
    payment_date timestamp(6) without time zone,
    payment_method character varying(255),
    payment_reference character varying(255),
    status character varying(255),
    customer_orders_order_id uuid,
    CONSTRAINT user_payments_payment_method_check CHECK (((payment_method)::text = ANY ((ARRAY['CASH'::character varying, 'CARD'::character varying])::text[]))),
    CONSTRAINT user_payments_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'PAID'::character varying, 'CANCELLED'::character varying, 'REFUNDED'::character varying])::text[])))
);


ALTER TABLE public.user_payments OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id uuid NOT NULL,
    city character varying(255),
    country character varying(255),
    created_at timestamp(6) without time zone,
    delete_yn smallint,
    email character varying(255) NOT NULL,
    first_name character varying(255) NOT NULL,
    is_active boolean NOT NULL,
    is_email_valid boolean NOT NULL,
    last_name character varying(255) NOT NULL,
    password character varying(255),
    phone_number character varying(255) NOT NULL,
    profile_pictureurl character varying(255),
    state character varying(255),
    updated_at timestamp(6) without time zone,
    user_gender character varying(255),
    user_name character varying(255) NOT NULL,
    user_role character varying(255),
    user_status character varying(255),
    role_id uuid,
    CONSTRAINT users_delete_yn_check CHECK (((delete_yn >= 0) AND (delete_yn <= 1))),
    CONSTRAINT users_user_gender_check CHECK (((user_gender)::text = ANY ((ARRAY['MALE'::character varying, 'FEMALE'::character varying])::text[]))),
    CONSTRAINT users_user_role_check CHECK (((user_role)::text = ANY ((ARRAY['ADMIN'::character varying, 'CUSTOMER'::character varying])::text[]))),
    CONSTRAINT users_user_status_check CHECK (((user_status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying])::text[])))
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: books books_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books
    ADD CONSTRAINT books_pkey PRIMARY KEY (book_id);


--
-- Name: customer_order_order_items customer_order_order_items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_order_order_items
    ADD CONSTRAINT customer_order_order_items_pkey PRIMARY KEY (customer_orders_order_id, order_items_order_item_id);


--
-- Name: customer_order customer_order_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_order
    ADD CONSTRAINT customer_order_pkey PRIMARY KEY (order_id);


--
-- Name: customer customer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (customer_id);


--
-- Name: employee employee_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_pkey PRIMARY KEY (employee_id);


--
-- Name: order_item order_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_item
    ADD CONSTRAINT order_item_pkey PRIMARY KEY (order_item_id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (role_id);


--
-- Name: users uk6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: users uk9q63snka3mdh91as4io72espi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk9q63snka3mdh91as4io72espi UNIQUE (phone_number);


--
-- Name: user_payments ukbhx37j0s49km41h4unqse1a04; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_payments
    ADD CONSTRAINT ukbhx37j0s49km41h4unqse1a04 UNIQUE (customer_orders_order_id);


--
-- Name: customer_order ukdr00mhaspj1cx99fs2dei2wvd; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_order
    ADD CONSTRAINT ukdr00mhaspj1cx99fs2dei2wvd UNIQUE (order_payment_paymentid);


--
-- Name: customer_order_order_items uki7xasubr4v54hjkqux6uh6k7w; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_order_order_items
    ADD CONSTRAINT uki7xasubr4v54hjkqux6uh6k7w UNIQUE (order_items_order_item_id);


--
-- Name: role ukiubw515ff0ugtm28p8g3myt0h; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT ukiubw515ff0ugtm28p8g3myt0h UNIQUE (role_name);


--
-- Name: customer ukj7ja2xvrxudhvssosd4nu1o92; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT ukj7ja2xvrxudhvssosd4nu1o92 UNIQUE (user_id);


--
-- Name: users ukk8d0f2n7n88w1a16yhua64onx; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT ukk8d0f2n7n88w1a16yhua64onx UNIQUE (user_name);


--
-- Name: users ukkrvotbtiqhudlkamvlpaqus0t; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT ukkrvotbtiqhudlkamvlpaqus0t UNIQUE (role_id);


--
-- Name: role ukkyiccjhffirji07hqfrsgtoig; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT ukkyiccjhffirji07hqfrsgtoig UNIQUE (user_id);


--
-- Name: employee ukmpps3d3r9pdvyjx3iqixi96fi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT ukmpps3d3r9pdvyjx3iqixi96fi UNIQUE (user_id);


--
-- Name: user_payments user_payments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_payments
    ADD CONSTRAINT user_payments_pkey PRIMARY KEY (paymentid);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- Name: idx_book_author; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_book_author ON public.books USING btree (book_author);


--
-- Name: idx_book_title; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_book_title ON public.books USING btree (book_title);


--
-- Name: customer_order_order_items fk2mdyu3nby610kgupg9vtqv0ib; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_order_order_items
    ADD CONSTRAINT fk2mdyu3nby610kgupg9vtqv0ib FOREIGN KEY (customer_orders_order_id) REFERENCES public.customer_order(order_id);


--
-- Name: users fk4qu1gr772nnf6ve5af002rwya; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fk4qu1gr772nnf6ve5af002rwya FOREIGN KEY (role_id) REFERENCES public.role(role_id);


--
-- Name: user_payments fk6fkvqlv5tw5gacee2ymow78w8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_payments
    ADD CONSTRAINT fk6fkvqlv5tw5gacee2ymow78w8 FOREIGN KEY (customer_orders_order_id) REFERENCES public.customer_order(order_id);


--
-- Name: order_item fkegdk0kfrrdr1248hq5ixgr5x6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_item
    ADD CONSTRAINT fkegdk0kfrrdr1248hq5ixgr5x6 FOREIGN KEY (book_id) REFERENCES public.books(book_id);


--
-- Name: role fkgg3583634e0ydkacyk8wbbm19; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT fkgg3583634e0ydkacyk8wbbm19 FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- Name: order_item fkgv4bnmo7cbib2nh0b2rw9yvir; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_item
    ADD CONSTRAINT fkgv4bnmo7cbib2nh0b2rw9yvir FOREIGN KEY (order_id) REFERENCES public.customer_order(order_id);


--
-- Name: customer_order_order_items fkgymgfl8ma5h2b4bn2cun08too; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_order_order_items
    ADD CONSTRAINT fkgymgfl8ma5h2b4bn2cun08too FOREIGN KEY (order_items_order_item_id) REFERENCES public.order_item(order_item_id);


--
-- Name: employee fkhal2duyxxjtadykhxos7wd3wg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT fkhal2duyxxjtadykhxos7wd3wg FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- Name: books fkj0p37mqwbn8w2vcsjxs7443k8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books
    ADD CONSTRAINT fkj0p37mqwbn8w2vcsjxs7443k8 FOREIGN KEY (employee_id) REFERENCES public.employee(employee_id);


--
-- Name: customer_order fkka2ttt7womanbgx17uf5g3yoi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_order
    ADD CONSTRAINT fkka2ttt7womanbgx17uf5g3yoi FOREIGN KEY (order_payment_paymentid) REFERENCES public.user_payments(paymentid);


--
-- Name: customer fkra1cb3fu95r1a0m7aksow0nk4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT fkra1cb3fu95r1a0m7aksow0nk4 FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- Name: customer_order fkt6d7ly7jooc2yutp39usiwq60; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_order
    ADD CONSTRAINT fkt6d7ly7jooc2yutp39usiwq60 FOREIGN KEY (customer_id_customer_id) REFERENCES public.customer(customer_id);


--
-- PostgreSQL database dump complete
--

