--
-- PostgreSQL database dump
--

-- Dumped from database version 8.3.14
-- Dumped by pg_dump version 9.2.2
-- Started on 2013-09-25 09:32:33 CDT

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 1855 (class 1262 OID 16386)
-- Name: LTSystem; Type: DATABASE; Schema: -; Owner: LTSysAdmin
--

CREATE DATABASE "LTSystem" WITH TEMPLATE = template0 ENCODING = 'UTF8';


ALTER DATABASE "LTSystem" OWNER TO "LTSysAdmin";

\connect "LTSystem"

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

--
-- TOC entry 439 (class 1247 OID 16415)
-- Name: lo; Type: DOMAIN; Schema: public; Owner: LTSysAdmin
--

CREATE DOMAIN lo AS oid;


ALTER DOMAIN public.lo OWNER TO "LTSysAdmin";

--
-- TOC entry 141 (class 1259 OID 18222)
-- Name: conveyortasks_id_seq; Type: SEQUENCE; Schema: public; Owner: LTSysAdmin
--

CREATE SEQUENCE conveyortasks_id_seq
    INCREMENT BY 1
    MINVALUE 2
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.conveyortasks_id_seq OWNER TO "LTSysAdmin";

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 142 (class 1259 OID 18224)
-- Name: conveyortasks; Type: TABLE; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

CREATE TABLE conveyortasks (
    id integer DEFAULT nextval('conveyortasks_id_seq'::regclass) NOT NULL,
    "time" timestamp with time zone,
    mode text,
    timespan bigint,
    duration bigint,
    startmode integer,
    priority integer,
    status integer,
    oac_id bigint,
    oac_label text,
    creator text,
    created timestamp with time zone,
    changedby text,
    changedat timestamp with time zone
);


ALTER TABLE public.conveyortasks OWNER TO "LTSysAdmin";

--
-- TOC entry 132 (class 1259 OID 16418)
-- Name: image_file_table; Type: TABLE; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

CREATE TABLE image_file_table (
    id bigint NOT NULL,
    path text
);


ALTER TABLE public.image_file_table OWNER TO "LTSysAdmin";

--
-- TOC entry 131 (class 1259 OID 16416)
-- Name: image_file_table_id_seq; Type: SEQUENCE; Schema: public; Owner: LTSysAdmin
--

CREATE SEQUENCE image_file_table_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.image_file_table_id_seq OWNER TO "LTSysAdmin";

--
-- TOC entry 1858 (class 0 OID 0)
-- Dependencies: 131
-- Name: image_file_table_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: LTSysAdmin
--

ALTER SEQUENCE image_file_table_id_seq OWNED BY image_file_table.id;


--
-- TOC entry 139 (class 1259 OID 18211)
-- Name: lemnateclock_id_seq; Type: SEQUENCE; Schema: public; Owner: LTSysAdmin
--

CREATE SEQUENCE lemnateclock_id_seq
    INCREMENT BY 1
    MINVALUE 2
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.lemnateclock_id_seq OWNER TO "LTSysAdmin";

--
-- TOC entry 140 (class 1259 OID 18213)
-- Name: lemnateclock; Type: TABLE; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

CREATE TABLE lemnateclock (
    id integer DEFAULT nextval('lemnateclock_id_seq'::regclass) NOT NULL,
    look integer,
    "user" text,
    datetime text
);


ALTER TABLE public.lemnateclock OWNER TO "LTSysAdmin";

--
-- TOC entry 128 (class 1259 OID 16391)
-- Name: ltdbs; Type: TABLE; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

CREATE TABLE ltdbs (
    id bigint NOT NULL,
    name text,
    servername text,
    servertype text,
    creator text,
    created timestamp without time zone,
    removed boolean,
    "time" timestamp without time zone,
    propagated boolean
);


ALTER TABLE public.ltdbs OWNER TO "LTSysAdmin";

--
-- TOC entry 127 (class 1259 OID 16389)
-- Name: ltdbs_id_seq; Type: SEQUENCE; Schema: public; Owner: LTSysAdmin
--

CREATE SEQUENCE ltdbs_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ltdbs_id_seq OWNER TO "LTSysAdmin";

--
-- TOC entry 1859 (class 0 OID 0)
-- Dependencies: 127
-- Name: ltdbs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: LTSysAdmin
--

ALTER SEQUENCE ltdbs_id_seq OWNED BY ltdbs.id;


--
-- TOC entry 130 (class 1259 OID 16404)
-- Name: ltuser; Type: TABLE; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

CREATE TABLE ltuser (
    id bigint NOT NULL,
    name text,
    passwd text,
    role integer,
    db_ids text,
    notes text,
    creator text,
    created timestamp without time zone,
    removed boolean,
    "time" timestamp without time zone,
    propagated boolean,
    "colorArgb" integer
);


ALTER TABLE public.ltuser OWNER TO "LTSysAdmin";

--
-- TOC entry 129 (class 1259 OID 16402)
-- Name: ltuser_id_seq; Type: SEQUENCE; Schema: public; Owner: LTSysAdmin
--

CREATE SEQUENCE ltuser_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ltuser_id_seq OWNER TO "LTSysAdmin";

--
-- TOC entry 1860 (class 0 OID 0)
-- Dependencies: 129
-- Name: ltuser_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: LTSysAdmin
--

ALTER SEQUENCE ltuser_id_seq OWNED BY ltuser.id;


--
-- TOC entry 135 (class 1259 OID 18064)
-- Name: plants_id_seq; Type: SEQUENCE; Schema: public; Owner: LTSysAdmin
--

CREATE SEQUENCE plants_id_seq
    INCREMENT BY 1
    MINVALUE 2
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.plants_id_seq OWNER TO "LTSysAdmin";

--
-- TOC entry 137 (class 1259 OID 18068)
-- Name: plants; Type: TABLE; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

CREATE TABLE plants (
    id bigint DEFAULT nextval('plants_id_seq'::regclass) NOT NULL,
    propagated boolean,
    identcode text,
    carid text,
    type text,
    active boolean,
    creator text,
    "timestamp" timestamp with time zone,
    on_system boolean
);


ALTER TABLE public.plants OWNER TO "LTSysAdmin";

--
-- TOC entry 136 (class 1259 OID 18066)
-- Name: pumps_id_seq; Type: SEQUENCE; Schema: public; Owner: LTSysAdmin
--

CREATE SEQUENCE pumps_id_seq
    INCREMENT BY 1
    MINVALUE 2
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pumps_id_seq OWNER TO "LTSysAdmin";

--
-- TOC entry 138 (class 1259 OID 18077)
-- Name: pumps; Type: TABLE; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

CREATE TABLE pumps (
    pumpname text,
    comport text,
    califacmlpr real,
    speedinmlpm integer,
    backlash integer,
    direction text,
    type text,
    id bigint DEFAULT nextval('pumps_id_seq'::regclass) NOT NULL,
    propagated boolean,
    label text,
    state integer,
    last_state_change timestamp with time zone,
    last_state_changed_by text,
    creator text,
    time_stamp timestamp with time zone
);


ALTER TABLE public.pumps OWNER TO "LTSysAdmin";

--
-- TOC entry 133 (class 1259 OID 17861)
-- Name: serviceinterval_id_seq; Type: SEQUENCE; Schema: public; Owner: LTSysAdmin
--

CREATE SEQUENCE serviceinterval_id_seq
    INCREMENT BY 1
    MINVALUE 2
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.serviceinterval_id_seq OWNER TO "LTSysAdmin";

--
-- TOC entry 134 (class 1259 OID 17863)
-- Name: serviceinterval; Type: TABLE; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

CREATE TABLE serviceinterval (
    id bigint DEFAULT nextval('serviceinterval_id_seq'::regclass) NOT NULL,
    data text,
    device text,
    name text,
    serviceinfo text,
    propagated boolean
);


ALTER TABLE public.serviceinterval OWNER TO "LTSysAdmin";

--
-- TOC entry 145 (class 1259 OID 18244)
-- Name: serviceresets_id_seq; Type: SEQUENCE; Schema: public; Owner: LTSysAdmin
--

CREATE SEQUENCE serviceresets_id_seq
    INCREMENT BY 1
    MINVALUE 2
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.serviceresets_id_seq OWNER TO "LTSysAdmin";

--
-- TOC entry 146 (class 1259 OID 18246)
-- Name: serviceresets; Type: TABLE; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

CREATE TABLE serviceresets (
    id integer DEFAULT nextval('serviceresets_id_seq'::regclass) NOT NULL,
    devicename text,
    duetime timestamp with time zone,
    currentcounter integer,
    "user" text,
    "timestamp" timestamp with time zone
);


ALTER TABLE public.serviceresets OWNER TO "LTSysAdmin";

--
-- TOC entry 143 (class 1259 OID 18233)
-- Name: watering_id_seq; Type: SEQUENCE; Schema: public; Owner: LTSysAdmin
--

CREATE SEQUENCE watering_id_seq
    INCREMENT BY 1
    MINVALUE 2
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.watering_id_seq OWNER TO "LTSysAdmin";

--
-- TOC entry 144 (class 1259 OID 18235)
-- Name: watering; Type: TABLE; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

CREATE TABLE watering (
    id integer DEFAULT nextval('watering_id_seq'::regclass) NOT NULL,
    identcode text,
    "time" timestamp with time zone,
    starthour integer,
    finishhour integer,
    quantity integer,
    pumpname text,
    type text,
    "FormulaTW" text,
    done boolean,
    status text,
    watered integer,
    processed timestamp with time zone,
    deficency text,
    deflimit integer,
    creator text,
    created timestamp with time zone,
    pumpconfigids text,
    commentary text
);


ALTER TABLE public.watering OWNER TO "LTSysAdmin";

--
-- TOC entry 1799 (class 2604 OID 16421)
-- Name: id; Type: DEFAULT; Schema: public; Owner: LTSysAdmin
--

ALTER TABLE ONLY image_file_table ALTER COLUMN id SET DEFAULT nextval('image_file_table_id_seq'::regclass);


--
-- TOC entry 1797 (class 2604 OID 16394)
-- Name: id; Type: DEFAULT; Schema: public; Owner: LTSysAdmin
--

ALTER TABLE ONLY ltdbs ALTER COLUMN id SET DEFAULT nextval('ltdbs_id_seq'::regclass);


--
-- TOC entry 1798 (class 2604 OID 16407)
-- Name: id; Type: DEFAULT; Schema: public; Owner: LTSysAdmin
--

ALTER TABLE ONLY ltuser ALTER COLUMN id SET DEFAULT nextval('ltuser_id_seq'::regclass);


--
-- TOC entry 1846 (class 0 OID 18224)
-- Dependencies: 142
-- Data for Name: conveyortasks; Type: TABLE DATA; Schema: public; Owner: LTSysAdmin
--

COPY conveyortasks (id, "time", mode, timespan, duration, startmode, priority, status, oac_id, oac_label, creator, created, changedby, changedat) FROM stdin;
946	2013-09-25 10:00:00-05	Aufnehmen:Greenhouse 1 (even):conveyor,Even lanes left shift with watering and weighing 30 min block size 10,1,True,False:18:MSW_B1_GH1e_iww:	120	240	1	0	0	18	MSW_091113	LTAdmin	2013-09-24 15:49:53.921-05	\N	0001-01-01 00:00:00-06
947	2013-09-25 14:00:00-05	Aufnehmen:Greenhouse 3 (odd):conveyor,Odd lanes left shift with watering and weighing 30 min block size 10,1,True,False:18:MSW_B1_GH3o_iww:	120	240	1	0	0	18	MSW_091113	LTAdmin	2013-09-24 15:51:53.343-05	\N	0001-01-01 00:00:00-06
948	2013-09-25 18:00:00-05	Aufnehmen:Greenhouse 1 (odd):conveyor,Odd lanes left shift with watering and weighing 30 min block size 10,1,True,False:18:MSW_B1_GH1o_iww:	120	240	1	0	0	18	MSW_091113	LTAdmin	2013-09-24 15:52:40.827-05	\N	0001-01-01 00:00:00-06
942	2013-09-24 11:03:54.827-05	Aufnehmen:Greenhouse 4 (odd):conveyor,Odd lanes left shift with watering and weighing 30 min block size 10,1,True,False:18:MSW_B1_GH4e_iww:	0	399	0	0	4	18	MSW_091113	LTAdmin	2013-09-24 11:03:54.827-05	\N	0001-01-01 00:00:00-06
943	2013-09-24 18:00:00-05	Aufnehmen:Greenhouse 4 (even):conveyor,Even lanes left shift with watering and weighing 30 min block size 10,1,True,False:18:MSW_B1_GH4e_iww:	180	407	1	0	4	18	MSW_091113	LTAdmin	2013-09-24 11:04:58.171-05	\N	0001-01-01 00:00:00-06
944	2013-09-25 04:00:00-05	Rollieren:Greenhouse 4 (all):conveyor,All lanes first half left shift,0,True,False:0:Unknown:	120	32	1	0	4	0		LTAdmin	2013-09-24 15:35:31.108-05	\N	0001-01-01 00:00:00-06
945	2013-09-25 06:00:00-05	Aufnehmen:Greenhouse 3 (even):conveyor,Even lanes left shift with watering and weighing 30 min block size 10,1,True,False:18:MSW_B1_GH3e_iww:	120	240	1	0	1	18	MSW_091113	LTAdmin	2013-09-24 15:48:49.718-05	\N	0001-01-01 00:00:00-06
\.


--
-- TOC entry 1861 (class 0 OID 0)
-- Dependencies: 141
-- Name: conveyortasks_id_seq; Type: SEQUENCE SET; Schema: public; Owner: LTSysAdmin
--

SELECT pg_catalog.setval('conveyortasks_id_seq', 948, true);


--
-- TOC entry 1836 (class 0 OID 16418)
-- Dependencies: 132
-- Data for Name: image_file_table; Type: TABLE DATA; Schema: public; Owner: LTSysAdmin
--

COPY image_file_table (id, path) FROM stdin;
\.


--
-- TOC entry 1862 (class 0 OID 0)
-- Dependencies: 131
-- Name: image_file_table_id_seq; Type: SEQUENCE SET; Schema: public; Owner: LTSysAdmin
--

SELECT pg_catalog.setval('image_file_table_id_seq', 1, false);


--
-- TOC entry 1844 (class 0 OID 18213)
-- Dependencies: 140
-- Data for Name: lemnateclock; Type: TABLE DATA; Schema: public; Owner: LTSysAdmin
--

COPY lemnateclock (id, look, "user", datetime) FROM stdin;
2	1	ControllerJobManager	9/23/2013 7:37:09 PM
\.


--
-- TOC entry 1863 (class 0 OID 0)
-- Dependencies: 139
-- Name: lemnateclock_id_seq; Type: SEQUENCE SET; Schema: public; Owner: LTSysAdmin
--

SELECT pg_catalog.setval('lemnateclock_id_seq', 2, true);


--
-- TOC entry 1832 (class 0 OID 16391)
-- Dependencies: 128
-- Data for Name: ltdbs; Type: TABLE DATA; Schema: public; Owner: LTSysAdmin
--

COPY ltdbs (id, name, servername, servertype, creator, created, removed, "time", propagated) FROM stdin;
1	LemnaTest		Postgres	LTAdmin	2010-01-01 00:00:00	f	2010-01-01 00:00:00	t
2	MaliaTest	10.18.0.21	\N	LTAdmin	2013-06-29 17:43:46.64	f	\N	f
\.


--
-- TOC entry 1864 (class 0 OID 0)
-- Dependencies: 127
-- Name: ltdbs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: LTSysAdmin
--

SELECT pg_catalog.setval('ltdbs_id_seq', 2, true);


--
-- TOC entry 1834 (class 0 OID 16404)
-- Dependencies: 130
-- Data for Name: ltuser; Type: TABLE DATA; Schema: public; Owner: LTSysAdmin
--

COPY ltuser (id, name, passwd, role, db_ids, notes, creator, created, removed, "time", propagated, "colorArgb") FROM stdin;
1	LTAdmin	32,44,185,98,172,89,7,91,150,75,7,21,45,35,75,112	127	1,2		LTAdmin	2010-01-01 00:00:00	f	2010-01-01 00:00:00	t	0
\.


--
-- TOC entry 1865 (class 0 OID 0)
-- Dependencies: 129
-- Name: ltuser_id_seq; Type: SEQUENCE SET; Schema: public; Owner: LTSysAdmin
--

SELECT pg_catalog.setval('ltuser_id_seq', 1, true);


--
-- TOC entry 1841 (class 0 OID 18068)
-- Dependencies: 137
-- Data for Name: plants; Type: TABLE DATA; Schema: public; Owner: LTSysAdmin
--

COPY plants (id, propagated, identcode, carid, type, active, creator, "timestamp", on_system) FROM stdin;
3478	f	1408	1408		f	LTAdmin	2013-09-11 13:53:08.093-05	f
3480	f	1730	1730		f	LTAdmin	2013-09-11 13:53:32.078-05	f
3481	f	1633	1633		f	LTAdmin	2013-09-11 13:53:44.093-05	f
3482	f	1640	1640		f	LTAdmin	2013-09-11 13:53:56.093-05	f
3483	f	1670	1670		f	LTAdmin	2013-09-11 13:54:08.093-05	f
3484	f	1636	1636		f	LTAdmin	2013-09-11 13:54:20.109-05	f
3485	f	1870	1870		f	LTAdmin	2013-09-11 13:56:06.593-05	f
3486	f	1791	1791		f	LTAdmin	2013-09-11 13:56:54.093-05	f
3487	f	1801	1801		f	LTAdmin	2013-09-11 13:59:14.625-05	f
3488	f	1181	1181		f	LTAdmin	2013-09-11 13:59:38.625-05	f
3489	f	2005	2005		f	LTAdmin	2013-09-11 14:00:02.093-05	f
3490	f	1627	1627		f	LTAdmin	2013-09-11 14:02:06.109-05	f
3491	f	1284	1284		f	LTAdmin	2013-09-11 14:03:46.672-05	f
3493	f	2079	2079		t	LTAdmin	2013-09-12 04:01:15.047-05	t
3492	f	1953	1953		f	LTAdmin	2013-09-12 04:01:05.062-05	t
2595	f	DAAA000613	1468		t	LTAdmin	2013-09-10 10:01:06.047-05	t
2596	f	DBAA000753	1466		t	LTAdmin	2013-09-10 10:01:16.656-05	t
2597	f	DAAA000607	1486		t	LTAdmin	2013-09-10 10:01:29.484-05	t
2598	f	DBAA000754	1467		t	LTAdmin	2013-09-10 10:01:51.093-05	t
2605	f	DBAA000756	2135		t	LTAdmin	2013-09-10 10:04:03.156-05	t
2599	f	DAAA000614	1884		t	LTAdmin	2013-09-10 10:02:14.703-05	t
2600	f	DBAA000750	1462		t	LTAdmin	2013-09-10 10:02:25-05	t
2601	f	DAAA000611	1451		t	LTAdmin	2013-09-10 10:02:36.312-05	t
2602	f	DBAA000757	1852		t	LTAdmin	2013-09-10 10:02:50.531-05	t
2603	f	DAAA000608	1858		t	LTAdmin	2013-09-10 10:03:15.218-05	t
2604	f	DAAA000601	1796		t	LTAdmin	2013-09-10 10:03:51.031-05	t
2606	f	DAAA000602	1550		t	LTAdmin	2013-09-10 10:05:32.515-05	t
2607	f	DBAA000733	1696		t	LTAdmin	2013-09-10 10:05:41.812-05	t
2608	f	DAAA000605	1101		t	LTAdmin	2013-09-10 10:05:51.422-05	t
2609	f	DBAA000734	1879		t	LTAdmin	2013-09-10 10:06:01.625-05	t
2610	f	DAAA000612	1972		t	LTAdmin	2013-09-10 10:06:11.328-05	t
2611	f	DBAA000731	1764		t	LTAdmin	2013-09-10 10:06:21.125-05	t
2612	f	DAAA000609	1454		t	LTAdmin	2013-09-10 10:06:37.968-05	t
2613	f	DBAA000732	1437		t	LTAdmin	2013-09-10 10:06:47.781-05	t
2614	f	DAAA000634	1033		t	LTAdmin	2013-09-10 10:06:58.39-05	t
3080	f	Arab46	1243		f	LTAdmin	2013-09-11 12:10:30.218-05	f
3494	f	2092	2092		t	LTAdmin	2013-09-12 18:10:24.234-05	t
3495	f	HAAA000002	1129		t	LTAdmin	2013-09-13 08:01:51.203-05	t
3496	f	HAAA000001	1845		t	LTAdmin	2013-09-13 08:02:02.828-05	t
3497	f	HAAA000003	2026		t	LTAdmin	2013-09-13 08:02:14.234-05	t
3498	f	HAAA000005	1068		t	LTAdmin	2013-09-13 08:02:24.125-05	t
3499	f	HAAA000004	1053		t	LTAdmin	2013-09-13 08:02:34.781-05	t
3500	f	IAAA000002	1075		t	LTAdmin	2013-09-13 08:02:48.547-05	t
3501	f	IAAA000005	1919		t	LTAdmin	2013-09-13 08:02:58.468-05	t
3502	f	IAAA000003	1422		t	LTAdmin	2013-09-13 08:03:07.672-05	t
3503	f	IAAA000001	1876		t	LTAdmin	2013-09-13 08:03:16.968-05	t
3504	f	IAAA000004	1096		t	LTAdmin	2013-09-13 08:03:28.593-05	t
2701	f	BBAA000369	1703		f	LTAdmin	2013-09-10 10:49:31.703-05	t
2907	f	AAAA000030	1201		f	LTAdmin	2013-09-11 08:27:48.218-05	t
2620	f	DAAA000638	1740		t	LTAdmin	2013-09-10 10:08:45.406-05	t
2621	f	DBAA000735	1616		t	LTAdmin	2013-09-10 10:08:56.718-05	t
2622	f	DAAA000635	1218		t	LTAdmin	2013-09-10 10:09:06.312-05	t
2615	f	DBAA000740	1545		t	LTAdmin	2013-09-10 10:07:14.437-05	t
2616	f	DAAA000631	1647		t	LTAdmin	2013-09-10 10:07:26.14-05	t
2617	f	DBAA000737	1392		t	LTAdmin	2013-09-10 10:07:38-05	t
2623	f	DBAA000701	1065		t	LTAdmin	2013-09-10 10:09:37.484-05	t
2618	f	DAAA000632	1618		t	LTAdmin	2013-09-10 10:07:56.703-05	t
2619	f	DBAA000738	1606		t	LTAdmin	2013-09-10 10:08:29.468-05	t
2624	f	DAAA000699	1089		t	LTAdmin	2013-09-10 10:09:56.265-05	t
2625	f	DBAA000743	1664		t	LTAdmin	2013-09-10 10:10:04.359-05	t
2626	f	DAAA000629	1093		t	LTAdmin	2013-09-10 10:10:29.062-05	t
2627	f	DBAA000744	1595		t	LTAdmin	2013-09-10 10:10:39.093-05	t
2628	f	DAAA000636	1646		t	LTAdmin	2013-09-10 10:10:55.515-05	t
2629	f	DBAA000741	1384		t	LTAdmin	2013-09-10 10:11:04.953-05	t
2630	f	DAAA000630	1954		t	LTAdmin	2013-09-10 10:11:18.578-05	t
2631	f	DBAA000742	1813		t	LTAdmin	2013-09-10 10:11:54.984-05	t
2632	f	DAAA000633	1047		t	LTAdmin	2013-09-10 10:12:07.593-05	t
2633	f	DBAA000739	1005		t	LTAdmin	2013-09-10 10:12:18.922-05	t
2634	f	DAAA000641	1675		t	LTAdmin	2013-09-10 10:12:28.953-05	t
2635	f	DBAA000767	2107		t	LTAdmin	2013-09-10 10:13:25.531-05	t
2640	f	DAAA000640	1996		t	LTAdmin	2013-09-10 10:14:20.406-05	t
2641	f	DBAA000772	1149		t	LTAdmin	2013-09-10 10:14:31.922-05	t
2642	f	DAAA000637	1018		t	LTAdmin	2013-09-10 10:14:43.047-05	t
2643	f	DBAA000771	1266		t	LTAdmin	2013-09-10 10:14:58.89-05	t
2644	f	DAAA000620	2100		t	LTAdmin	2013-09-10 10:15:15.968-05	t
2636	f	DAAA000642	1988		t	LTAdmin	2013-09-10 10:13:41.984-05	t
2637	f	DBAA000770	2106		t	LTAdmin	2013-09-10 10:13:52.297-05	t
2645	f	DBAA000763	2033		t	LTAdmin	2013-09-10 10:15:31.609-05	t
2646	f	DBAA000766	1978		t	LTAdmin	2013-09-10 10:15:42.625-05	t
2647	f	DBAA000760	1225		t	LTAdmin	2013-09-10 10:15:54.14-05	t
2638	f	DAAA000639	2024		t	LTAdmin	2013-09-10 10:14:01.484-05	t
2639	f	DBAA000769	2030		t	LTAdmin	2013-09-10 10:14:11.50-05	t
2648	f	DAAA000617	1281		t	LTAdmin	2013-09-10 10:16:13.437-05	t
2649	f	DBAA000759	2117		t	LTAdmin	2013-09-10 10:16:48.047-05	t
2650	f	DAAA000624	1215		t	LTAdmin	2013-09-10 10:16:59.468-05	t
2651	f	DBAA000765	1204		t	LTAdmin	2013-09-10 10:17:14.922-05	t
2652	f	.DAAA000618	1806		t	LTAdmin	2013-09-10 10:17:25.828-05	t
2653	f	DBAA000768	1676		t	LTAdmin	2013-09-10 10:17:58.297-05	t
2654	f	DAAA000621	2132		t	LTAdmin	2013-09-10 10:18:07.39-05	t
2655	f	DBAA000762	1506		t	LTAdmin	2013-09-10 10:18:17.50-05	t
2656	f	DAAA000627	1901		t	LTAdmin	2013-09-10 10:18:26.484-05	t
3082	f	1584	1584		f	LTAdmin	2013-09-11 12:11:27.218-05	f
3081	f	CBAA000524	1243		t	LTAdmin	2013-09-11 12:11:12.453-05	t
3085	f	1544	1544		f	LTAdmin	2013-09-11 12:12:17.734-05	f
3083	f	CAAA000494	1584		t	LTAdmin	2013-09-11 12:12:00.531-05	t
3084	f	CBAA000527	1787		t	LTAdmin	2013-09-11 12:12:12.156-05	t
3086	f	CAAA000495	1687		t	LTAdmin	2013-09-11 12:12:28.718-05	t
3087	f	CBAA000528	1759		t	LTAdmin	2013-09-11 12:12:40.64-05	t
3096	f	1458	1458		f	LTAdmin	2013-09-11 12:14:57.218-05	f
3088	f	CAAA000496	1544		t	LTAdmin	2013-09-11 12:12:52.562-05	t
3089	f	CBAA000523	1261		t	LTAdmin	2013-09-11 12:13:02.375-05	t
2657	f	DBAA000761	1707		t	LTAdmin	2013-09-10 10:18:35.281-05	t
3090	f	CAAA000497	1198		t	LTAdmin	2013-09-11 12:13:12.687-05	t
3092	f	CBAA000530	1191		t	LTAdmin	2013-09-11 12:13:24.187-05	t
3093	f	CAAA000498	1240		t	LTAdmin	2013-09-11 12:13:34-05	t
3091	f	1031	1031		f	LTAdmin	2013-09-11 12:13:17.718-05	f
3094	f	CBAA000529	1031		t	LTAdmin	2013-09-11 12:13:43.203-05	t
3095	f	CAAA000500	1030		t	LTAdmin	2013-09-11 12:14:36.328-05	t
3097	f	CBAA000526	1579		t	LTAdmin	2013-09-11 12:15:13.625-05	t
3099	f	CAAA000499	1013		t	LTAdmin	2013-09-11 12:15:29.593-05	t
3100	f	CBAA000592	1738		t	LTAdmin	2013-09-11 12:15:42.125-05	t
3098	f	1074	1074		f	LTAdmin	2013-09-11 12:15:21.234-05	f
3101	f	CBAA000585	1458		t	LTAdmin	2013-09-11 12:15:55.25-05	t
3102	f	CBAA000588	1043		t	LTAdmin	2013-09-11 12:16:05.468-05	t
3103	f	CAAA000487	1074		t	LTAdmin	2013-09-11 12:16:15.172-05	t
3104	f	CBAA000591	1341		t	LTAdmin	2013-09-11 12:16:26.203-05	t
3105	f	CBAA000534	1923		t	LTAdmin	2013-09-11 12:16:43.437-05	t
3106	f	CAAA000484	2105		t	LTAdmin	2013-09-11 12:16:53.156-05	t
3109	f	CBAA000533	2102		t	LTAdmin	2013-09-11 12:17:25.734-05	t
3110	f	CAAA000486	1140		t	LTAdmin	2013-09-11 12:17:37.359-05	t
3107	f	1092	1092		f	LTAdmin	2013-09-11 12:17:10.765-05	f
2658	f	DAAA000628	1348		t	LTAdmin	2013-09-10 10:18:45.797-05	t
2659	f	DBAA000764	1117		t	LTAdmin	2013-09-10 10:18:55.50-05	t
3111	f	CBAA000536	1092		t	LTAdmin	2013-09-11 12:17:50.484-05	t
3108	f	1770	1770		f	LTAdmin	2013-09-11 12:17:22.75-05	f
3112	f	CAAA000489	1770		t	LTAdmin	2013-09-11 12:18:06.125-05	t
3113	f	CBAA000515	1436		t	LTAdmin	2013-09-11 12:18:17.328-05	t
3114	f	CAAA000491	1272		t	LTAdmin	2013-09-11 12:18:27.125-05	t
3115	f	CBAA000512	1172		t	LTAdmin	2013-09-11 12:18:37.734-05	t
3116	f	CAAA000490	1177		t	LTAdmin	2013-09-11 12:18:48.843-05	t
3117	f	CBAA000518	1232		t	LTAdmin	2013-09-11 12:19:01.078-05	t
3118	f	CAAA000488	1164		t	LTAdmin	2013-09-11 12:19:12.297-05	t
3119	f	CBAA000511	1578		t	LTAdmin	2013-09-11 12:19:52.422-05	t
3120	f	CAAA000492	1995		t	LTAdmin	2013-09-11 12:20:03.453-05	t
3121	f	CBAA000514	1821		t	LTAdmin	2013-09-11 12:20:30.093-05	t
3122	f	CAAA000477	1892		t	LTAdmin	2013-09-11 12:20:39.203-05	t
3123	f	CBAA000517	1987		t	LTAdmin	2013-09-11 12:20:48.406-05	t
3124	f	CAAA000476	1991		t	LTAdmin	2013-09-11 12:21:01.781-05	t
3126	f	CBAA000513	2077		t	LTAdmin	2013-09-11 12:21:13.906-05	t
3127	f	CAAA000478	2101		t	LTAdmin	2013-09-11 12:21:23.328-05	t
3125	f	1126	1126		f	LTAdmin	2013-09-11 12:21:04.797-05	f
3128	f	CBAA000520	1126		t	LTAdmin	2013-09-11 12:21:33.031-05	t
3129	f	CAAA000479	1704		t	LTAdmin	2013-09-11 12:21:45.453-05	t
3131	f	CBAA000519	1891		t	LTAdmin	2013-09-11 12:22:35.031-05	t
3130	f	1166	1166		f	LTAdmin	2013-09-11 12:22:17.797-05	f
3132	f	CAAA000480	1166		t	LTAdmin	2013-09-11 12:22:53.015-05	t
3133	f	CBAA000516	2065		t	LTAdmin	2013-09-11 12:23:04.328-05	t
3134	f	CAAA000481	2090		t	LTAdmin	2013-09-11 12:23:15.656-05	t
3135	f	CBAA000538	1819		t	LTAdmin	2013-09-11 12:23:30.406-05	t
3136	f	CAAA000483	1376		t	LTAdmin	2013-09-11 12:23:41.812-05	t
3137	f	CBAA000535	1709		t	LTAdmin	2013-09-11 12:23:53.218-05	t
3138	f	CAAA000485	1850		t	LTAdmin	2013-09-11 12:24:06.047-05	t
3139	f	CBAA000537	1804		t	LTAdmin	2013-09-11 12:24:16.843-05	t
3140	f	CAAA000468	2049		t	LTAdmin	2013-09-11 12:24:31.781-05	t
3141	f	1323	1323		f	LTAdmin	2013-09-11 12:24:44.812-05	f
3142	f	CBAA000509	1323		t	LTAdmin	2013-09-11 12:25:13.015-05	t
3143	f	CAAA000482	1564		t	LTAdmin	2013-09-11 12:25:25.031-05	t
3144	f	CBAA000502	1035		t	LTAdmin	2013-09-11 12:25:39.062-05	t
3146	f	CAAA000467	1279		t	LTAdmin	2013-09-11 12:25:52.078-05	t
3147	f	CBAA000507	1461		t	LTAdmin	2013-09-11 12:26:10.547-05	t
3145	f	1061	1061		f	LTAdmin	2013-09-11 12:25:47.812-05	f
3148	f	CAAA000469	1061		t	LTAdmin	2013-09-11 12:26:34.031-05	t
3149	f	CBAA000510	1134		t	LTAdmin	2013-09-11 12:26:45.953-05	t
3150	f	CAAA000470	1677		t	LTAdmin	2013-09-11 12:27:04.422-05	t
3151	f	CBAA000501	1768		t	LTAdmin	2013-09-11 12:27:14.125-05	t
3152	f	CAAA000473	1711		t	LTAdmin	2013-09-11 12:27:25.937-05	t
2660	f	DAAA000615	1147		t	LTAdmin	2013-09-10 10:19:13.656-05	t
3153	f	CBAA000508	1615		t	LTAdmin	2013-09-11 12:27:58.312-05	t
2661	f	DBAA000781	1577		t	LTAdmin	2013-09-10 10:20:00.453-05	t
2662	f	DAAA000622	1900		t	LTAdmin	2013-09-10 10:20:15.89-05	t
2663	f	DBAA000784	1854		t	LTAdmin	2013-09-10 10:20:29.625-05	t
2664	f	DAAA000625	2015		t	LTAdmin	2013-09-10 10:20:48.50-05	t
2665	f	DBAA000777	1903		t	LTAdmin	2013-09-10 10:20:59.203-05	t
2666	f	DAAA000616	1964		t	LTAdmin	2013-09-10 10:21:08.812-05	t
2667	f	DBAA000783	1872		t	LTAdmin	2013-09-10 10:21:18.843-05	t
2668	f	DAAA000619	1180		t	LTAdmin	2013-09-10 10:21:28.453-05	t
2669	f	DBAA000774	2076		t	LTAdmin	2013-09-10 10:21:38.468-05	t
2670	f	DAAA000626	1255		t	LTAdmin	2013-09-10 10:21:49.062-05	t
2671	f	DBAA000780	1968		t	LTAdmin	2013-09-10 10:21:57.968-05	t
2672	f	DAAA000623	1755		t	LTAdmin	2013-09-10 10:22:07.968-05	t
2673	f	DBAA000786	1586		t	LTAdmin	2013-09-10 10:22:18.484-05	t
2674	f	DAAA000690	1224		t	LTAdmin	2013-09-10 10:22:45.109-05	t
2675	f	DBAA000773	1958		t	LTAdmin	2013-09-10 10:22:58.031-05	t
2676	f	DAAA000687	1785		t	LTAdmin	2013-09-10 10:23:10.25-05	t
3154	f	CAAA000471	1603		t	LTAdmin	2013-09-11 12:28:09.547-05	t
3156	f	CBAA000504	1050		t	LTAdmin	2013-09-11 12:28:45.031-05	t
3157	f	CAAA000474	1961		t	LTAdmin	2013-09-11 12:28:58.875-05	t
3158	f	CBAA000503	1886		t	LTAdmin	2013-09-11 12:29:10.797-05	t
3159	f	CAAA000472	1881		t	LTAdmin	2013-09-11 12:29:24.328-05	t
3160	f	CBAA000505	1887		t	LTAdmin	2013-09-11 12:29:38.453-05	t
3161	f	CAAA000475	1076		t	LTAdmin	2013-09-11 12:29:54.922-05	t
3162	f	CBAA000506	1882		t	LTAdmin	2013-09-11 12:30:08.14-05	t
3163	f	CAAA000457	1998		t	LTAdmin	2013-09-11 12:30:23.484-05	t
3164	f	CBAA000568	2099		t	LTAdmin	2013-09-11 12:30:41.937-05	t
3165	f	CAAA000466	1063		t	LTAdmin	2013-09-11 12:30:53.64-05	t
3166	f	CBAA000567	1227		t	LTAdmin	2013-09-11 12:31:08.281-05	t
3155	f	Elf3	1363		f	LTAdmin	2013-09-11 12:28:32.859-05	f
3167	f	CAAA000460	1363		t	LTAdmin	2013-09-11 12:31:38.015-05	t
3168	f	CBAA000571	1382		t	LTAdmin	2013-09-11 12:31:56.593-05	t
3169	f	CAAA000462	2013		t	LTAdmin	2013-09-11 12:32:12.343-05	t
3170	f	CBAA000574	1295		t	LTAdmin	2013-09-11 12:32:23.25-05	t
3171	f	CAAA000458	1366		t	LTAdmin	2013-09-11 12:32:34.453-05	t
3172	f	CBAA000573	1494		t	LTAdmin	2013-09-11 12:32:48.281-05	t
2509	f	DAAA000657	1915		t	LTAdmin	2013-09-10 09:38:53.015-05	t
3173	f	CAAA000459	1286		t	LTAdmin	2013-09-11 12:32:59.015-05	t
3174	f	CBAA000570	1136		t	LTAdmin	2013-09-11 12:33:22.906-05	t
3175	f	CAAA000448	2006		t	LTAdmin	2013-09-11 12:33:35.031-05	t
3176	f	CBAA000575	1249		t	LTAdmin	2013-09-11 12:33:50.968-05	t
3177	f	CAAA000447	1189		t	LTAdmin	2013-09-11 12:34:08.828-05	t
3178	f	CBAA000569	1539		t	LTAdmin	2013-09-11 12:34:22.875-05	t
3179	f	CAAA000449	1086		t	LTAdmin	2013-09-11 12:34:33.812-05	t
3180	f	CBAA000572	1531		t	LTAdmin	2013-09-11 12:34:52.797-05	t
3181	f	CAAA000450	1500		t	LTAdmin	2013-09-11 12:35:04.328-05	t
3182	f	CBAA000586	1219		t	LTAdmin	2013-09-11 12:35:16.047-05	t
3183	f	CAAA000451	1749		t	LTAdmin	2013-09-11 12:35:28.984-05	t
3184	f	CBAA000579	1024		t	LTAdmin	2013-09-11 12:36:17.781-05	t
3185	f	CAAA000453	1048		t	LTAdmin	2013-09-11 12:36:31.297-05	t
3187	f	CBAA000596	1040		t	LTAdmin	2013-09-11 12:36:49.875-05	t
3188	f	CAAA000452	1016		t	LTAdmin	2013-09-11 12:37:04.015-05	t
3186	f	1007	1007		f	LTAdmin	2013-09-11 12:36:48.437-05	f
2498	f	DBAA000722	1502		t	LTAdmin	2013-09-10 09:35:46.343-05	t
2499	f	DAAA000662	1378		t	LTAdmin	2013-09-10 09:36:12.156-05	t
2500	f	DBAA000719	1194		t	LTAdmin	2013-09-10 09:36:35.14-05	t
2501	f	DAAA000659	1141		t	LTAdmin	2013-09-10 09:36:51.078-05	t
2510	f	DAAA000664	2072		t	LTAdmin	2013-09-10 09:39:21.484-05	t
2511	f	DBAA000717	2075		t	LTAdmin	2013-09-10 09:39:47.328-05	t
2512	f	DAAA000658	1656		t	LTAdmin	2013-09-10 09:40:07.687-05	t
2513	f	DBAA000724	2060		t	LTAdmin	2013-09-10 09:40:35.203-05	t
2502	f	DBAA000720	1679		t	LTAdmin	2013-09-10 09:37:06.515-05	t
2503	f	DAAA000660	1674		t	LTAdmin	2013-09-10 09:37:21.265-05	t
2504	f	DBAA000726	1610		t	LTAdmin	2013-09-10 09:37:34.078-05	t
2505	f	DAAA000666	1992		t	LTAdmin	2013-09-10 09:37:54.265-05	t
2506	f	DBAA000723	1945		t	LTAdmin	2013-09-10 09:38:15.656-05	t
2507	f	DAAA000663	1554		t	LTAdmin	2013-09-10 09:38:27.765-05	t
2508	f	DBAA000729	1907		t	LTAdmin	2013-09-10 09:38:42.39-05	t
2514	f	DBAA000718	1653		t	LTAdmin	2013-09-10 09:40:51.656-05	t
2515	f	DAAA000661	1651		t	LTAdmin	2013-09-10 09:41:00.765-05	t
2516	f	DBAA000727	1689		t	LTAdmin	2013-09-10 09:41:17.812-05	t
2517	f	DAAA000669	1692		t	LTAdmin	2013-09-10 09:41:33.453-05	t
2518	f	DBAA000721	1599		t	LTAdmin	2013-09-10 09:41:44.156-05	t
2519	f	DAAA000670	1078		t	LTAdmin	2013-09-10 09:41:58.39-05	t
2520	f	DBAA000728	1735		t	LTAdmin	2013-09-10 09:42:17.25-05	t
2521	f	DAAA000667	1062		t	LTAdmin	2013-09-10 09:42:30.797-05	t
2522	f	DBAA000725	1598		t	LTAdmin	2013-09-10 09:42:59.718-05	t
2523	f	DAAA000668	1596		t	LTAdmin	2013-09-10 09:43:11.047-05	t
3189	f	CBAA000600	1023		t	LTAdmin	2013-09-11 12:37:16.828-05	t
3190	f	CAAA000454	1007		t	LTAdmin	2013-09-11 12:37:30.031-05	t
3192	f	CBAA000598	1728		t	LTAdmin	2013-09-11 12:37:40.64-05	t
3193	f	CAAA000455	2085		t	LTAdmin	2013-09-11 12:37:49.953-05	t
3194	f	CBAA000597	1641		t	LTAdmin	2013-09-11 12:38:00.547-05	t
3191	f	1083	1083		f	LTAdmin	2013-09-11 12:37:35.937-05	f
3195	f	CAAA000461	1083		t	LTAdmin	2013-09-11 12:38:10.25-05	t
3196	f	CBAA000599	1719		t	LTAdmin	2013-09-11 12:38:56.422-05	t
3197	f	CAAA000465	1004		t	LTAdmin	2013-09-11 12:39:09.14-05	t
3198	f	CBAA000560	1721		t	LTAdmin	2013-09-11 12:39:27.89-05	t
3199	f	CAAA000463	1476		t	LTAdmin	2013-09-11 12:39:40.328-05	t
3200	f	CBAA000559	2066		t	LTAdmin	2013-09-11 12:39:52.234-05	t
3201	f	CAAA000437	1303		t	LTAdmin	2013-09-11 12:40:05.062-05	t
3202	f	CBAA000562	1364		t	LTAdmin	2013-09-11 12:40:16.093-05	t
3203	f	CAAA000438	1367		t	LTAdmin	2013-09-11 12:40:28.734-05	t
3204	f	CBAA000561	1388		t	LTAdmin	2013-09-11 12:40:37.906-05	t
3205	f	CAAA000439	2136		t	LTAdmin	2013-09-11 12:40:47.734-05	t
3206	f	CBAA000563	1809		t	LTAdmin	2013-09-11 12:41:48.50-05	t
3207	f	CAAA000440	2029		t	LTAdmin	2013-09-11 12:42:01.718-05	t
3208	f	CBAA000565	1803		t	LTAdmin	2013-09-11 12:42:13.937-05	t
3209	f	CAAA000441	1559		t	LTAdmin	2013-09-11 12:42:27.359-05	t
3210	f	CBAA000566	1324		t	LTAdmin	2013-09-11 12:42:43.109-05	t
3211	f	CAAA000443	1322		t	LTAdmin	2013-09-11 12:42:53.718-05	t
3212	f	CBAA000564	1826		t	LTAdmin	2013-09-11 12:43:05.843-05	t
3213	f	CAAA000444	1751		t	LTAdmin	2013-09-11 12:43:18.672-05	t
3214	f	CBAA000550	1671		t	LTAdmin	2013-09-11 12:43:30.828-05	t
3215	f	CAAA000445	1472		t	LTAdmin	2013-09-11 12:43:40.609-05	t
3216	f	1921	1921		f	LTAdmin	2013-09-11 12:44:04.922-05	f
3217	f	1920	1920		f	LTAdmin	2013-09-11 12:44:16.937-05	f
3218	f	1977	1977		f	LTAdmin	2013-09-11 12:44:31.937-05	f
3220	f	1820	1820		f	LTAdmin	2013-09-11 12:44:43.906-05	f
3222	f	1591	1591		f	LTAdmin	2013-09-11 12:44:55.922-05	f
3224	f	1859	1859		f	LTAdmin	2013-09-11 12:45:07.922-05	f
3226	f	2039	2039		f	LTAdmin	2013-09-11 12:45:19.906-05	f
3228	f	1924	1924		f	LTAdmin	2013-09-11 12:45:31.406-05	f
3230	f	1928	1928		f	LTAdmin	2013-09-11 12:45:43.406-05	f
3232	f	1414	1414		f	LTAdmin	2013-09-11 12:45:54.906-05	f
3236	f	1369	1369		f	LTAdmin	2013-09-11 12:46:53.406-05	f
3238	f	CBAA000558	1369		t	LTAdmin	2013-09-11 12:47:19.812-05	t
3237	f	1354	1354		f	LTAdmin	2013-09-11 12:47:05.406-05	f
3241	f	CAAA000431	1354		t	LTAdmin	2013-09-11 12:47:35.562-05	t
3239	f	177 BIG B1-1	1228		f	LTAdmin	2013-09-11 12:47:20.922-05	f
3243	f	CBAA000551	1228		t	LTAdmin	2013-09-11 12:47:47.093-05	t
3240	f	176 BIG D9	1426		f	LTAdmin	2013-09-11 12:47:32.937-05	f
3245	f	CAAA000432	1426		t	LTAdmin	2013-09-11 12:48:46.453-05	t
3242	f	177 BIG B1-4	1449		f	LTAdmin	2013-09-11 12:47:44.922-05	f
3246	f	CBAA000554	1449		t	LTAdmin	2013-09-11 12:49:04.906-05	t
3247	f	CAAA000433	1786		t	LTAdmin	2013-09-11 12:49:17.312-05	t
3244	f	1830	1830		f	LTAdmin	2013-09-11 12:48:08.922-05	f
3248	f	CBAA000557	1830		t	LTAdmin	2013-09-11 12:49:33.078-05	t
3250	f	CAAA000436	1956		t	LTAdmin	2013-09-11 12:49:43.578-05	t
3251	f	CBAA000577	1827		t	LTAdmin	2013-09-11 12:49:56.187-05	t
3252	f	CAAA000434	1840		t	LTAdmin	2013-09-11 12:50:12.64-05	t
3249	f	1837	1837		f	LTAdmin	2013-09-11 12:49:42.453-05	f
3253	f	CAAA000417	1837		t	LTAdmin	2013-09-11 12:50:24.859-05	t
3255	f	CBAA000583	1849		t	LTAdmin	2013-09-11 12:50:45.547-05	t
2524	f	DBAA000708	1732		t	LTAdmin	2013-09-10 09:43:45.515-05	t
3256	f	CAAA000416	1492		t	LTAdmin	2013-09-11 12:50:56.859-05	t
2525	f	DAAA000665	1463		t	LTAdmin	2013-09-10 09:43:54.718-05	t
3257	f	CBAA000580	1941		t	LTAdmin	2013-09-11 12:51:09.718-05	t
3258	f	CAAA000419	2035		t	LTAdmin	2013-09-11 12:51:21.718-05	t
3254	f	1124	1124		f	LTAdmin	2013-09-11 12:50:45.437-05	f
2526	f	DBAA000705	1457		t	LTAdmin	2013-09-10 09:44:25.172-05	t
3259	f	CBAA000584	1124		t	LTAdmin	2013-09-11 12:51:32.843-05	t
3260	f	CAAA000418	1133		t	LTAdmin	2013-09-11 12:51:44.859-05	t
3261	f	CBAA000578	1176		t	LTAdmin	2013-09-11 12:51:58.297-05	t
3262	f	CAAA000421	1560		t	LTAdmin	2013-09-11 12:52:09.609-05	t
3263	f	CBAA000581	1017		t	LTAdmin	2013-09-11 12:52:19.297-05	t
2527	f	DAAA000648	2086		t	LTAdmin	2013-09-10 09:44:53.218-05	t
3264	f	1055	1055		f	LTAdmin	2013-09-11 12:52:33.968-05	f
3267	f	CAAA000420	1055		t	LTAdmin	2013-09-11 12:53:08.578-05	t
2528	f	DBAA000712	1329		t	LTAdmin	2013-09-10 09:45:07.781-05	t
3265	f	1041	1041		f	LTAdmin	2013-09-11 12:52:45.984-05	f
3269	f	CBAA000576	1041		t	LTAdmin	2013-09-11 12:53:22.015-05	t
3268	f	1444	1444		f	LTAdmin	2013-09-11 12:53:13.984-05	f
3270	f	1060	1060		f	LTAdmin	2013-09-11 12:53:26-05	f
3219	f	CBAA000553	1921		f	LTAdmin	2013-09-11 12:44:36.125-05	t
3221	f	CAAA000446	1920		f	LTAdmin	2013-09-11 12:44:48.343-05	t
3223	f	CBAA000549	1977		f	LTAdmin	2013-09-11 12:45:03.297-05	t
3225	f	CAAA000428	1820		f	LTAdmin	2013-09-11 12:45:14.515-05	t
3227	f	CBAA000556	1591		f	LTAdmin	2013-09-11 12:45:23.312-05	t
3229	f	CAAA000427	1859		f	LTAdmin	2013-09-11 12:45:34.109-05	t
3231	f	CBAA000555	2039		f	LTAdmin	2013-09-11 12:45:54.578-05	t
3233	f	CAAA000430	1924		f	LTAdmin	2013-09-11 12:46:05.078-05	t
3234	f	CBAA000552	1928		f	LTAdmin	2013-09-11 12:46:18.297-05	t
3235	f	CAAA000429	1414		f	LTAdmin	2013-09-11 12:46:32.437-05	t
2529	f	DAAA000645	1306		t	LTAdmin	2013-09-10 09:45:23.937-05	t
2530	f	DBAA000706	1374		t	LTAdmin	2013-09-10 09:45:37.375-05	t
2531	f	DAAA000652	1933		t	LTAdmin	2013-09-10 09:45:47.672-05	t
2532	f	DBAA000709	2070		t	LTAdmin	2013-09-10 09:46:01.609-05	t
2533	f	DAAA000655	1475		t	LTAdmin	2013-09-10 09:46:11.922-05	t
2534	f	DBAA000715	1488		t	LTAdmin	2013-09-10 09:46:23.922-05	t
2535	f	DAAA000646	1226		t	LTAdmin	2013-09-10 09:46:42.625-05	t
3266	f	1515	1515		f	LTAdmin	2013-09-11 12:53:02-05	f
3272	f	CAAA000423	1515		t	LTAdmin	2013-09-11 12:53:43.578-05	t
3274	f	CAAA000405	1444		t	LTAdmin	2013-09-11 12:53:54.812-05	t
3276	f	CBAA000595	1060		t	LTAdmin	2013-09-11 12:54:03.906-05	t
3271	f	1082	1082		f	LTAdmin	2013-09-11 12:53:38-05	f
3278	f	CAAA000403	1082		t	LTAdmin	2013-09-11 12:54:13.797-05	t
3273	f	1861	1861		f	LTAdmin	2013-09-11 12:53:50-05	f
3279	f	CBAA000589	1861		t	LTAdmin	2013-09-11 12:54:23.593-05	t
3275	f	1829	1829		f	LTAdmin	2013-09-11 12:54:01.50-05	f
3281	f	CAAA000425	1829		t	LTAdmin	2013-09-11 12:54:32.89-05	t
3277	f	1807	1807		f	LTAdmin	2013-09-11 12:54:13.50-05	f
3282	f	CBAA000593	1807		t	LTAdmin	2013-09-11 12:54:43.922-05	t
3280	f	1842	1842		f	LTAdmin	2013-09-11 12:54:25-05	f
3283	f	CAAA000422	1842		t	LTAdmin	2013-09-11 12:54:54.828-05	t
3284	f	2123	2123		f	LTAdmin	2013-09-11 12:55:25.50-05	f
3287	f	CBAA000590	2123		t	LTAdmin	2013-09-11 12:55:52.922-05	t
3285	f	1655	1655		f	LTAdmin	2013-09-11 12:55:37.50-05	f
3288	f	CAAA000426	1655		t	LTAdmin	2013-09-11 12:56:03.328-05	t
3286	f	1600	1600		f	LTAdmin	2013-09-11 12:55:52.50-05	f
3291	f	CBAA000594	1600		t	LTAdmin	2013-09-11 12:56:18.172-05	t
3289	f	1652	1652		f	LTAdmin	2013-09-11 12:56:04-05	f
3292	f	CAAA000424	1652		t	LTAdmin	2013-09-11 12:56:30.703-05	t
3290	f	1635	1635		f	LTAdmin	2013-09-11 12:56:15.984-05	f
3293	f	CBAA000582	1635		t	LTAdmin	2013-09-11 12:56:42.718-05	t
3294	f	CBAA000587	1417		t	LTAdmin	2013-09-11 12:56:52.812-05	t
3295	f	1484	1484		f	LTAdmin	2013-09-11 12:58:13.984-05	f
2536	f	DBAA000716	1222		t	LTAdmin	2013-09-10 09:46:55.937-05	t
2537	f	DAAA000649	2115		t	LTAdmin	2013-09-10 09:47:08.875-05	t
2538	f	DBAA000703	2073		t	LTAdmin	2013-09-10 09:47:26.547-05	t
2539	f	DAAA000656	1282		t	LTAdmin	2013-09-10 09:47:40.187-05	t
2540	f	DBAA000710	1299		t	LTAdmin	2013-09-10 09:47:51-05	t
2541	f	DAAA000653	1020		t	LTAdmin	2013-09-10 09:47:59.39-05	t
2542	f	DBAA000713	1645		t	LTAdmin	2013-09-10 09:48:15.14-05	t
2543	f	DAAA000650	1643		t	LTAdmin	2013-09-10 09:48:25.843-05	t
2544	f	DBAA000714	1613		t	LTAdmin	2013-09-10 09:48:40.093-05	t
3296	f	2028	2028		f	LTAdmin	2013-09-11 13:01:22.484-05	f
3297	f	1604	1604		f	LTAdmin	2013-09-11 13:02:00.484-05	f
3298	f	1752	1752		f	LTAdmin	2013-09-11 13:02:12.484-05	f
3299	f	1877	1877		f	LTAdmin	2013-09-11 13:02:35.968-05	f
3300	f	2087	2087		f	LTAdmin	2013-09-11 13:02:47.968-05	f
3301	f	1666	1666		f	LTAdmin	2013-09-11 13:02:59.968-05	f
3302	f	1661	1661		f	LTAdmin	2013-09-11 13:04:01.968-05	f
2549	f	DAAA000647	1411		t	LTAdmin	2013-09-10 09:49:29.484-05	t
2550	f	DBAA000711	1380		t	LTAdmin	2013-09-10 09:49:50.859-05	t
2551	f	DAAA000644	1706		t	LTAdmin	2013-09-10 09:50:01.765-05	t
2552	f	DBAA000792	1767		t	LTAdmin	2013-09-10 09:50:15.312-05	t
2553	f	AA000651	1960		t	LTAdmin	2013-09-10 09:50:24.547-05	t
2554	f	DAAA000651	1630		t	LTAdmin	2013-09-10 09:51:35.484-05	t
2555	f	DBAA000789	2119		t	LTAdmin	2013-09-10 09:51:54.859-05	t
2556	f	DBAA000736	1974		t	LTAdmin	2013-09-10 09:52:06.672-05	t
3303	f	1844	1844		f	LTAdmin	2013-09-11 13:04:13.953-05	f
3304	f	1853	1853		f	LTAdmin	2013-09-11 13:04:28.468-05	f
3305	f	1943	1943		f	LTAdmin	2013-09-11 13:04:40.453-05	f
3306	f	1622	1622		f	LTAdmin	2013-09-11 13:04:52.953-05	f
3307	f	1056	1056		f	LTAdmin	2013-09-11 13:05:04.453-05	f
3308	f	1025	1025		f	LTAdmin	2013-09-11 13:05:16.453-05	f
3309	f	1855	1855		f	LTAdmin	2013-09-11 13:05:28.453-05	f
3310	f	2037	2037		f	LTAdmin	2013-09-11 13:05:40.453-05	f
3311	f	1848	1848		f	LTAdmin	2013-09-11 13:05:52.453-05	f
3312	f	1965	1965		f	LTAdmin	2013-09-11 13:06:54.968-05	f
3313	f	1825	1825		f	LTAdmin	2013-09-11 13:07:06.953-05	f
3314	f	1774	1774		f	LTAdmin	2013-09-11 13:07:22.453-05	f
3315	f	1776	1776		f	LTAdmin	2013-09-11 13:07:34.453-05	f
3316	f	1938	1938		f	LTAdmin	2013-09-11 13:08:10.468-05	f
3317	f	1536	1536		f	LTAdmin	2013-09-11 13:08:45.953-05	f
3318	f	1019	1019		f	LTAdmin	2013-09-11 13:09:51.953-05	f
3321	f	EAAA000002	1019		t	LTAdmin	2013-09-11 13:10:21.437-05	t
3319	f	1546	1546		f	LTAdmin	2013-09-11 13:10:03.953-05	f
3322	f	EAAA000001	1546		t	LTAdmin	2013-09-11 13:10:32.343-05	t
3320	f	1527	1527		f	LTAdmin	2013-09-11 13:10:20.453-05	f
3325	f	EAAA000003	1527		t	LTAdmin	2013-09-11 13:10:48.375-05	t
3323	f	1389	1389		f	LTAdmin	2013-09-11 13:10:32.453-05	f
3327	f	EAAA000005	1389		t	LTAdmin	2013-09-11 13:11:01.406-05	t
3324	f	1498	1498		f	LTAdmin	2013-09-11 13:10:44.953-05	f
3329	f	EAAA000004	1498		t	LTAdmin	2013-09-11 13:11:16.765-05	t
3326	f	1183	1183		f	LTAdmin	2013-09-11 13:10:56.968-05	f
3331	f	FAAA000002	1183		t	LTAdmin	2013-09-11 13:11:28.468-05	t
3328	f	1963	1963		f	LTAdmin	2013-09-11 13:11:08.453-05	f
3333	f	FAAA000001	1963		t	LTAdmin	2013-09-11 13:11:40.39-05	t
3330	f	1660	1660		f	LTAdmin	2013-09-11 13:11:20.437-05	f
3335	f	FAAA000003	1660		t	LTAdmin	2013-09-11 13:11:51.109-05	t
3332	f	1763	1763		f	LTAdmin	2013-09-11 13:11:32.453-05	f
3336	f	FAAA000005	1763		t	LTAdmin	2013-09-11 13:12:01.515-05	t
3334	f	1684	1684		f	LTAdmin	2013-09-11 13:11:44.453-05	f
3337	f	FAAA000004	1684		t	LTAdmin	2013-09-11 13:12:12.109-05	t
3338	f	1715	1715		f	LTAdmin	2013-09-11 13:12:52.468-05	f
3341	f	GAAA000002	1715		t	LTAdmin	2013-09-11 13:13:20.75-05	t
3339	f	1167	1167		f	LTAdmin	2013-09-11 13:13:04.468-05	f
3343	f	GAAA000001	1167		t	LTAdmin	2013-09-11 13:13:38.812-05	t
3340	f	1993	1993		f	LTAdmin	2013-09-11 13:13:20.484-05	f
3346	f	GAAA000003	1993		t	LTAdmin	2013-09-11 13:13:57.593-05	t
3342	f	1949	1949		f	LTAdmin	2013-09-11 13:13:32.468-05	f
3349	f	GAAA000004	1949		t	LTAdmin	2013-09-11 13:14:22.093-05	t
3344	f	1908	1908		f	LTAdmin	2013-09-11 13:13:44.468-05	f
3352	f	GAAA000005	1908		t	LTAdmin	2013-09-11 13:14:44.015-05	t
3345	f	1113	1113		f	LTAdmin	2013-09-11 13:13:56.468-05	f
3353	f	JAAA000003	1113		t	LTAdmin	2013-09-11 13:14:57.437-05	t
3347	f	1152	1152		f	LTAdmin	2013-09-11 13:14:08.484-05	f
3354	f	JAAA000001	1152		t	LTAdmin	2013-09-11 13:15:12.281-05	t
3348	f	1135	1135		f	LTAdmin	2013-09-11 13:14:20.484-05	f
3355	f	JAAA000005	1135		t	LTAdmin	2013-09-11 13:15:32.656-05	t
3350	f	1128	1128		f	LTAdmin	2013-09-11 13:14:31.984-05	f
3356	f	JAAA000002	1128		t	LTAdmin	2013-09-11 13:15:50.718-05	t
3351	f	1203	1203		f	LTAdmin	2013-09-11 13:14:44-05	f
3359	f	JAAA000004	1203		t	LTAdmin	2013-09-11 13:16:12.297-05	t
3357	f	1129	1129		f	LTAdmin	2013-09-11 13:15:53.515-05	f
3358	f	1845	1845		f	LTAdmin	2013-09-11 13:16:05.50-05	f
2545	f	DAAA000643	1612		t	LTAdmin	2013-09-10 09:48:50.297-05	t
2546	f	DBAA000707	1649		t	LTAdmin	2013-09-10 09:49:00.078-05	t
2547	f	DAAA000654	1702		t	LTAdmin	2013-09-10 09:49:08.781-05	t
2548	f	DBAA000704	1469		t	LTAdmin	2013-09-10 09:49:20.39-05	t
2567	f	DBAA000797	1300		t	LTAdmin	2013-09-10 09:54:43.672-05	t
2568	f	DAAA000677	1474		t	LTAdmin	2013-09-10 09:54:55.687-05	t
2569	f	DBAA000794	1929		t	LTAdmin	2013-09-10 09:55:08.531-05	t
3360	f	2026	2026		f	LTAdmin	2013-09-11 13:16:21.515-05	f
3361	f	1068	1068		f	LTAdmin	2013-09-11 13:16:33.015-05	f
3362	f	1053	1053		f	LTAdmin	2013-09-11 13:16:45.50-05	f
3363	f	1075	1075		f	LTAdmin	2013-09-11 13:16:57.50-05	f
3364	f	1919	1919		f	LTAdmin	2013-09-11 13:17:08.984-05	f
3365	f	1422	1422		f	LTAdmin	2013-09-11 13:17:21.031-05	f
3367	f	1096	1096		f	LTAdmin	2013-09-11 13:17:45.015-05	f
2557	f	DBAA000796	1860		f	LTAdmin	2013-09-10 09:52:26.765-05	t
2558	f	DAAA000676	1340		f	LTAdmin	2013-09-10 09:53:03.765-05	t
2559	f	DBAA000799	1337		f	LTAdmin	2013-09-10 09:53:19.50-05	t
2560	f	DAAA000673	1316		f	LTAdmin	2013-09-10 09:53:31.625-05	t
2561	f	DBAA000790	1345		f	LTAdmin	2013-09-10 09:53:43.031-05	t
2562	f	DAAA000680	2045		f	LTAdmin	2013-09-10 09:53:54.75-05	t
2563	f	DBAA000793	1542		f	LTAdmin	2013-09-10 09:54:04.64-05	t
2564	f	DAAA000683	1549		f	LTAdmin	2013-09-10 09:54:13.937-05	t
2565	f	DBAA000800	1182		f	LTAdmin	2013-09-10 09:54:24.953-05	t
2566	f	DAAA000684	1534		f	LTAdmin	2013-09-10 09:54:33.656-05	t
3366	f	1876	1876		f	LTAdmin	2013-09-11 13:17:33.015-05	f
3368	f	1969	1969		f	LTAdmin	2013-09-11 13:18:58.531-05	f
3369	f	1106	1106		f	LTAdmin	2013-09-11 13:19:10.531-05	f
3370	f	1423	1423		f	LTAdmin	2013-09-11 13:19:27.031-05	f
3371	f	1421	1421		f	LTAdmin	2013-09-11 13:19:39.031-05	f
3372	f	1070	1070		f	LTAdmin	2013-09-11 13:19:51.031-05	f
3373	f	1574	1574		f	LTAdmin	2013-09-11 13:20:03.015-05	f
3374	f	1778	1778		f	LTAdmin	2013-09-11 13:20:15.015-05	f
3375	f	1779	1779		f	LTAdmin	2013-09-11 13:20:27.015-05	f
3376	f	1766	1766		f	LTAdmin	2013-09-11 13:20:38.515-05	f
3377	f	1580	1580		f	LTAdmin	2013-09-11 13:20:50.50-05	f
3378	f	1626	1626		f	LTAdmin	2013-09-11 13:21:58.015-05	f
3379	f	1528	1528		f	LTAdmin	2013-09-11 13:22:10-05	f
3380	f	1519	1519		f	LTAdmin	2013-09-11 13:22:25.50-05	f
3381	f	1514	1514		f	LTAdmin	2013-09-11 13:22:37.50-05	f
3382	f	1673	1673		f	LTAdmin	2013-09-11 13:22:49.50-05	f
3383	f	1211	1211		f	LTAdmin	2013-09-11 13:23:01.515-05	f
2570	f	DAAA000674	1416		t	LTAdmin	2013-09-10 09:55:19.437-05	t
2571	f	DBAA000787	1010		t	LTAdmin	2013-09-10 09:55:29.343-05	t
2572	f	DAAA000681	1270		t	LTAdmin	2013-09-10 09:55:44.781-05	t
2573	f	DBAA000798	1277		t	LTAdmin	2013-09-10 09:55:53.89-05	t
2574	f	DAAA000678	1893		t	LTAdmin	2013-09-10 09:56:03.593-05	t
2575	f	DBAA000791	2021		t	LTAdmin	2013-09-10 09:56:16.125-05	t
2576	f	DAAA000671	2036		t	LTAdmin	2013-09-10 09:56:25.812-05	t
2577	f	DBAA000788	1360		t	LTAdmin	2013-09-10 09:56:37.422-05	t
2578	f	DAAA000672	1298		t	LTAdmin	2013-09-10 09:56:48.14-05	t
2579	f	DBAA000758	1888		t	LTAdmin	2013-09-10 09:56:58.578-05	t
2580	f	DAAA000675	1283		t	LTAdmin	2013-09-10 09:57:12.297-05	t
2581	f	DBAA000795	1273		t	LTAdmin	2013-09-10 09:57:21.984-05	t
3384	f	1896	1896		f	LTAdmin	2013-09-11 13:23:13.515-05	f
3385	f	1756	1756		f	LTAdmin	2013-09-11 13:23:25.515-05	f
3386	f	1757	1757		f	LTAdmin	2013-09-11 13:23:37-05	f
3387	f	1741	1741		f	LTAdmin	2013-09-11 13:23:49-05	f
3388	f	1576	1576		f	LTAdmin	2013-09-11 13:24:56.531-05	f
3389	f	1760	1760		f	LTAdmin	2013-09-11 13:25:08.50-05	f
3390	f	1585	1585		f	LTAdmin	2013-09-11 13:25:25-05	f
3391	f	1242	1242		f	LTAdmin	2013-09-11 13:25:37-05	f
3392	f	1885	1885		f	LTAdmin	2013-09-11 13:26:01-05	f
3393	f	1496	1496		f	LTAdmin	2013-09-11 13:26:13-05	f
3394	f	2131	2131		f	LTAdmin	2013-09-11 13:26:24.484-05	f
3395	f	1246	1246		f	LTAdmin	2013-09-11 13:26:36.50-05	f
3396	f	1478	1478		f	LTAdmin	2013-09-11 13:26:48.515-05	f
3397	f	2140	2140		f	LTAdmin	2013-09-11 13:27:58.015-05	f
2582	f	DAAA000682	1260		t	LTAdmin	2013-09-10 09:57:32.609-05	t
2583	f	DBAA000752	1453		t	LTAdmin	2013-09-10 09:57:45.234-05	t
2584	f	DAAA000679	2020		t	LTAdmin	2013-09-10 09:57:55.343-05	t
2585	f	DBAA000748	2113		t	LTAdmin	2013-09-10 09:58:16.125-05	t
2586	f	DAAA000606	1012		t	LTAdmin	2013-09-10 09:58:45.593-05	t
2587	f	DBAA000746	1169		t	LTAdmin	2013-09-10 09:58:56.812-05	t
2588	f	DAAA000603	1148		t	LTAdmin	2013-09-10 09:59:09.734-05	t
2589	f	DBAA000745	1700		t	LTAdmin	2013-09-10 09:59:47.453-05	t
2590	f	DAAA000604	1605		t	LTAdmin	2013-09-10 09:59:59.375-05	t
2591	f	DBAA000751	2052		t	LTAdmin	2013-09-10 10:00:11.281-05	t
2592	f	DAAA000610	1139		t	LTAdmin	2013-09-10 10:00:21.078-05	t
2593	f	DBAA000747	1547		t	LTAdmin	2013-09-10 10:00:42.187-05	t
2594	f	DBAA000755	1477		t	LTAdmin	2013-09-10 10:00:55.922-05	t
2677	f	DBAA000779	1396		t	LTAdmin	2013-09-10 10:23:44.422-05	t
2678	f	DAAA000694	1253		t	LTAdmin	2013-09-10 10:24:01.687-05	t
2679	f	DBAA000776	1690		t	LTAdmin	2013-09-10 10:24:17.14-05	t
2680	f	DAAA000697	1307		t	LTAdmin	2013-09-10 10:24:29.859-05	t
2681	f	DBAA000775	1541		t	LTAdmin	2013-09-10 10:24:45.093-05	t
2682	f	DAAA000688	1402		t	LTAdmin	2013-09-10 10:24:58.625-05	t
2683	f	DBAA000782	1196		t	LTAdmin	2013-09-10 10:25:11.047-05	t
2684	f	DAAA000691	1003		t	LTAdmin	2013-09-10 10:25:21.656-05	t
2685	f	DBAA000785	1562		t	LTAdmin	2013-09-10 10:25:37.422-05	t
2686	f	DAAA000685	1032		t	LTAdmin	2013-09-10 10:25:50.656-05	t
2687	f	DBAA000778	1350		t	LTAdmin	2013-09-10 10:26:01.562-05	t
2688	f	DAAA000698	1438		t	LTAdmin	2013-09-10 10:26:14.187-05	t
2689	f	DAAA000692	1693		t	LTAdmin	2013-09-10 10:26:24.703-05	t
2690	f	DAAA000695	1718		t	LTAdmin	2013-09-10 10:26:34.718-05	t
2691	f	DAAA000696	1397		t	LTAdmin	2013-09-10 10:26:44.50-05	t
2692	f	DAAA000689	1824		t	LTAdmin	2013-09-10 10:26:54.109-05	t
2693	f	DAAA000686	2088		t	LTAdmin	2013-09-10 10:27:02.687-05	t
2694	f	DAAA000693	2018		t	LTAdmin	2013-09-10 10:27:29.515-05	t
2705	f	BBAA000356	1665		t	LTAdmin	2013-09-10 10:50:26.89-05	t
2706	f	BAAA000261	1662		t	LTAdmin	2013-09-10 10:50:44.062-05	t
2707	f	BBAA000349	1293		t	LTAdmin	2013-09-10 10:50:55.078-05	t
2708	f	BAAA000265	1632		t	LTAdmin	2013-09-10 10:51:07.109-05	t
2709	f	BBAA000344	1634		t	LTAdmin	2013-09-10 10:51:18.297-05	t
2710	f	BAAA000269	1637		t	LTAdmin	2013-09-10 10:51:31.515-05	t
2711	f	BBAA000358	1726		t	LTAdmin	2013-09-10 10:51:41.734-05	t
2712	f	BAAA000268	1723		t	LTAdmin	2013-09-10 10:51:53.047-05	t
2713	f	BBAA000346	1800		t	LTAdmin	2013-09-10 10:52:03.75-05	t
2714	f	BAAA000271	1529		t	LTAdmin	2013-09-10 10:52:14.75-05	t
2715	f	BBAA000345	2110		t	LTAdmin	2013-09-10 10:52:25.562-05	t
2716	f	BAAA000264	1835		t	LTAdmin	2013-09-10 10:52:44.922-05	t
2717	f	BBAA000352	1812		t	LTAdmin	2013-09-10 10:52:54.218-05	t
2718	f	BAAA000267	1818		t	LTAdmin	2013-09-10 10:53:04.562-05	t
2719	f	BBAA000355	1899		t	LTAdmin	2013-09-10 10:53:19.375-05	t
2720	f	BAAA000270	1856		t	LTAdmin	2013-09-10 10:53:34.312-05	t
2721	f	BBAA000348	1724		t	LTAdmin	2013-09-10 10:53:46.828-05	t
2722	f	BAAA000273	1797		t	LTAdmin	2013-09-10 10:53:57.343-05	t
2723	f	BBAA000347	1238		t	LTAdmin	2013-09-10 10:54:26.468-05	t
2724	f	BAAA000231	1631		t	LTAdmin	2013-09-10 10:54:40.89-05	t
2725	f	BBAA000368	1898		t	LTAdmin	2013-09-10 10:54:56.531-05	t
2726	f	BAAA000233	2061		t	LTAdmin	2013-09-10 10:55:08.64-05	t
2727	f	BBAA000361	1836		t	LTAdmin	2013-09-10 10:55:22.875-05	t
2728	f	BAAA000234	1910		t	LTAdmin	2013-09-10 10:55:35.203-05	t
2729	f	BBAA000343	1838		t	LTAdmin	2013-09-10 10:55:52.359-05	t
2730	f	BAAA000232	1403		t	LTAdmin	2013-09-10 10:56:15.656-05	t
2731	f	BBAA000367	2129		t	LTAdmin	2013-09-10 10:56:28.765-05	t
2732	f	BAAA000238	2103		t	LTAdmin	2013-09-10 10:56:43.593-05	t
2733	f	BBAA000364	2133		t	LTAdmin	2013-09-10 10:56:55.906-05	t
2734	f	BAAA000236	1556		t	LTAdmin	2013-09-10 10:57:08.734-05	t
2735	f	BBAA000350	1555		t	LTAdmin	2013-09-10 10:57:23.078-05	t
2736	f	BAAA000235	1338		t	LTAdmin	2013-09-10 10:57:39.515-05	t
2737	f	BBAA000360	1339		t	LTAdmin	2013-09-10 10:57:51.828-05	t
2738	f	BAAA000240	2054		t	LTAdmin	2013-09-10 10:58:03.828-05	t
2739	f	BBAA000363	1394		t	LTAdmin	2013-09-10 10:58:15.343-05	t
2740	f	BAAA000237	1321		t	LTAdmin	2013-09-10 10:58:28.984-05	t
2741	f	BAAA000239	1335		t	LTAdmin	2013-09-10 10:58:46.14-05	t
2742	f	BBAA000370	1347		t	LTAdmin	2013-09-10 10:59:05.718-05	t
2743	f	BAAA000241	1375		t	LTAdmin	2013-09-10 10:59:20.859-05	t
2744	f	BBAA000359	1811		t	LTAdmin	2013-09-10 10:59:32.359-05	t
2745	f	BAAA000242	1464		t	LTAdmin	2013-09-10 10:59:45.484-05	t
2746	f	BBAA000366	1857		t	LTAdmin	2013-09-10 10:59:56.50-05	t
2747	f	BAAA000244	1064		t	LTAdmin	2013-09-10 11:00:10.14-05	t
2748	f	BBAA000353	2057		t	LTAdmin	2013-09-10 11:00:21.328-05	t
2749	f	BAAA000243	1171		t	LTAdmin	2013-09-10 11:00:33.547-05	t
2750	f	BBAA000362	1480		t	LTAdmin	2013-09-10 11:00:44.859-05	t
2751	f	BAAA000246	1538		t	LTAdmin	2013-09-10 11:01:24.265-05	t
2752	f	BBAA000381	1325		t	LTAdmin	2013-09-10 11:01:35.172-05	t
2753	f	BAAA000245	1973		t	LTAdmin	2013-09-10 11:01:47.375-05	t
2754	f	BBAA000384	1195		t	LTAdmin	2013-09-10 11:02:01.093-05	t
2755	f	BAAA000249	1805		t	LTAdmin	2013-09-10 11:02:13.718-05	t
2756	f	BBAA000377	2032		t	LTAdmin	2013-09-10 11:02:26.312-05	t
2757	f	BAAA000253	1088		t	LTAdmin	2013-09-10 11:02:43.375-05	t
2758	f	BBAA000374	1097		t	LTAdmin	2013-09-10 11:02:54.781-05	t
2759	f	BAAA000248	1815		t	LTAdmin	2013-09-10 11:03:06.89-05	t
2760	f	BBAA000383	2130		t	LTAdmin	2013-09-10 11:03:17.906-05	t
2761	f	BAAA000252	1231		t	LTAdmin	2013-09-10 11:03:29.328-05	t
2762	f	BBAA000380	1160		t	LTAdmin	2013-09-10 11:03:42.047-05	t
2763	f	BAAA000256	1205		t	LTAdmin	2013-09-10 11:03:54.281-05	t
2764	f	BBAA000373	1199		t	LTAdmin	2013-09-10 11:04:07-05	t
2765	f	BAAA000247	1256		t	LTAdmin	2013-09-10 11:04:18.625-05	t
2766	f	BBAA000385	1210		t	LTAdmin	2013-09-10 11:04:31.343-05	t
2767	f	BAAA000251	1257		t	LTAdmin	2013-09-10 11:04:42.14-05	t
2768	f	BBAA000379	1044		t	LTAdmin	2013-09-10 11:04:56.984-05	t
2769	f	BBAA000376	1291		t	LTAdmin	2013-09-10 11:05:11.718-05	t
2770	f	BAAA000255	1510		t	LTAdmin	2013-09-10 11:05:25.468-05	t
2771	f	BAAA000250	2011		t	LTAdmin	2013-09-10 11:05:45.734-05	t
3399	f	2094	2094		f	LTAdmin	2013-09-11 13:28:37.547-05	f
2695	f	BBAA000351	1639		f	LTAdmin	2013-09-10 10:48:02.375-05	t
2696	f	BAAA000260	1798		f	LTAdmin	2013-09-10 10:48:23.672-05	t
2697	f	BBAA000354	1851		f	LTAdmin	2013-09-10 10:48:37.125-05	t
2698	f	BAAA000259	1601		f	LTAdmin	2013-09-10 10:48:50.64-05	t
2699	f	BBAA000365	1609		f	LTAdmin	2013-09-10 10:49:03.359-05	t
2700	f	BAAA000263	1614		f	LTAdmin	2013-09-10 10:49:16.984-05	t
2702	f	BAAA000262	1174		f	LTAdmin	2013-09-10 10:49:46.547-05	t
2703	f	BBAA000357	1729		f	LTAdmin	2013-09-10 10:50:01.578-05	t
2704	f	BAAA000266	1769		f	LTAdmin	2013-09-10 10:50:12.172-05	t
2772	f	BBAA000386	1990		t	LTAdmin	2013-09-10 11:05:58.156-05	t
2773	f	BAAA000254	1754		t	LTAdmin	2013-09-10 11:06:08.484-05	t
2774	f	BBAA000382	1686		t	LTAdmin	2013-09-10 11:06:19.468-05	t
2775	f	BAAA000258	2042		t	LTAdmin	2013-09-10 11:06:31.093-05	t
2776	f	BBAA000375	1681		t	LTAdmin	2013-09-10 11:06:43.406-05	t
2777	f	BAAA000257	1490		t	LTAdmin	2013-09-10 11:06:55.015-05	t
2778	f	BBAA000378	1685		t	LTAdmin	2013-09-10 11:07:07.953-05	t
2779	f	BAAA000204	1625		t	LTAdmin	2013-09-10 11:07:40.297-05	t
2780	f	BBAA000396	1543		t	LTAdmin	2013-09-10 11:07:50.50-05	t
2781	f	BAAA000205	1911		t	LTAdmin	2013-09-10 11:08:02.203-05	t
2782	f	BBAA000397	1902		t	LTAdmin	2013-09-10 11:08:13.515-05	t
2783	f	BAAA000203	1909		t	LTAdmin	2013-09-10 11:08:35.609-05	t
2784	f	BBAA000392	1904		t	LTAdmin	2013-09-10 11:08:47.312-05	t
2785	f	BAAA000206	1984		t	LTAdmin	2013-09-10 11:09:16.562-05	t
2786	f	BBAA000387	1233		t	LTAdmin	2013-09-10 11:09:27.578-05	t
2787	f	BAAA000210	1108		t	LTAdmin	2013-09-10 11:09:38.984-05	t
2788	f	BBAA000398	1875		t	LTAdmin	2013-09-10 11:09:49.703-05	t
2789	f	BAAA000208	1878		t	LTAdmin	2013-09-10 11:10:00.484-05	t
2790	f	BBAA000393	1939		t	LTAdmin	2013-09-10 11:10:09.984-05	t
2791	f	BAAA000207	1725		t	LTAdmin	2013-09-10 11:10:21.593-05	t
2792	f	BBAA000388	2047		t	LTAdmin	2013-09-10 11:10:33.109-05	t
2793	f	BAAA000211	1833		t	LTAdmin	2013-09-10 11:10:45.922-05	t
2794	f	BBAA000399	1834		t	LTAdmin	2013-09-10 11:10:57.437-05	t
2795	f	BBAA000394	2067		t	LTAdmin	2013-09-10 11:11:08.343-05	t
2796	f	BAAA000209	2084		t	LTAdmin	2013-09-10 11:11:21.468-05	t
2797	f	BAAA000212	1843		t	LTAdmin	2013-09-10 11:11:41.031-05	t
2798	f	BBAA000389	2116		t	LTAdmin	2013-09-10 11:11:52.843-05	t
2799	f	BAAA000216	1271		t	LTAdmin	2013-09-10 11:12:06.781-05	t
2800	f	BBAA000400	1119		t	LTAdmin	2013-09-10 11:12:18.39-05	t
2801	f	BAAA000214	1537		t	LTAdmin	2013-09-10 11:12:28.703-05	t
2802	f	BBAA000395	1557		t	LTAdmin	2013-09-10 11:12:39.328-05	t
2803	f	BAAA000213	2008		t	LTAdmin	2013-09-10 11:12:51.156-05	t
2804	f	BBAA000390	2003		t	LTAdmin	2013-09-10 11:13:02.062-05	t
2805	f	BAAA000215	1979		t	LTAdmin	2013-09-10 11:13:12.984-05	t
2806	f	BBAA000391	1441		t	LTAdmin	2013-09-10 11:13:23.39-05	t
2807	f	BAAA000287	1448		t	LTAdmin	2013-09-10 11:13:50.297-05	t
2808	f	BBAA000323	1042		t	LTAdmin	2013-09-10 11:14:00.89-05	t
2809	f	BAAA000288	1011		t	LTAdmin	2013-09-10 11:14:11.937-05	t
2810	f	BBAA000326	1008		t	LTAdmin	2013-09-10 11:14:23.547-05	t
2811	f	BAAA000292	1540		t	LTAdmin	2013-09-10 11:14:35.047-05	t
2812	f	BBAA000319	1551		t	LTAdmin	2013-09-10 11:14:49.578-05	t
2813	f	BAAA000296	1431		t	LTAdmin	2013-09-10 11:15:02.203-05	t
2814	f	BBAA000316	1460		t	LTAdmin	2013-09-10 11:15:13.953-05	t
2815	f	BAAA000289	1511		t	LTAdmin	2013-09-10 11:15:28.172-05	t
2816	f	BBAA000325	1517		t	LTAdmin	2013-09-10 11:15:39.797-05	t
2817	f	BAAA000293	1362		t	LTAdmin	2013-09-10 11:15:55.453-05	t
2818	f	BBAA000322	1828		t	LTAdmin	2013-09-10 11:16:06.375-05	t
2819	f	BAAA000297	1178		t	LTAdmin	2013-09-10 11:16:17.265-05	t
2820	f	BBAA000315	1871		t	LTAdmin	2013-09-10 11:16:28.172-05	t
2821	f	BAAA000290	1918		t	LTAdmin	2013-09-10 11:16:41.797-05	t
2822	f	BBAA000328	1916		t	LTAdmin	2013-09-10 11:16:52.812-05	t
2823	f	BAAA000294	2082		t	LTAdmin	2013-09-10 11:17:04.015-05	t
2824	f	BBAA000321	1694		t	LTAdmin	2013-09-10 11:17:15.625-05	t
2825	f	BAAA000298	1592		t	LTAdmin	2013-09-10 11:17:27.453-05	t
2826	f	BBAA000318	1927		t	LTAdmin	2013-09-10 11:17:43.718-05	t
2827	f	BAAA000291	1914		t	LTAdmin	2013-09-10 11:17:55.031-05	t
2828	f	BBAA000327	2120		t	LTAdmin	2013-09-10 11:18:05.14-05	t
2829	f	BAAA000295	1102		t	LTAdmin	2013-09-10 11:18:15.843-05	t
2830	f	BBAA000324	1223		t	LTAdmin	2013-09-10 11:18:27.75-05	t
2831	f	BAAA000299	1145		t	LTAdmin	2013-09-10 11:18:37.765-05	t
2832	f	BBAA000317	1170		t	LTAdmin	2013-09-10 11:18:47.953-05	t
2833	f	BAAA000300	1123		t	LTAdmin	2013-09-10 11:18:59.781-05	t
2834	f	BBAA000320	1127		t	LTAdmin	2013-09-10 11:19:12.89-05	t
2835	f	BAAA000272	1558		t	LTAdmin	2013-09-10 11:19:39.218-05	t
2836	f	BBAA000337	1415		t	LTAdmin	2013-09-10 11:19:49.234-05	t
2837	f	BAAA000275	1420		t	LTAdmin	2013-09-10 11:19:59.937-05	t
2838	f	BBAA000340	2114		t	LTAdmin	2013-09-10 11:20:14.172-05	t
2839	f	BAAA000279	1100		t	LTAdmin	2013-09-10 11:20:43.078-05	t
2840	f	BBAA000333	1072		t	LTAdmin	2013-09-10 11:20:55.906-05	t
2841	f	BAAA000283	1144		t	LTAdmin	2013-09-10 11:21:10.734-05	t
2842	f	BBAA000330	1553		t	LTAdmin	2013-09-10 11:21:22.937-05	t
2843	f	BAAA000274	1445		t	LTAdmin	2013-09-10 11:21:37.078-05	t
2844	f	BBAA000339	1057		t	LTAdmin	2013-09-10 11:21:52.906-05	t
2845	f	BAAA000278	1789		t	LTAdmin	2013-09-10 11:22:04.218-05	t
2846	f	BBAA000336	1014		t	LTAdmin	2013-09-10 11:22:16.64-05	t
2847	f	BAAA000282	1594		t	LTAdmin	2013-09-10 11:22:28.468-05	t
2848	f	BBAA000329	1535		t	LTAdmin	2013-09-10 11:22:39.468-05	t
2849	f	BAAA000277	1157		t	LTAdmin	2013-09-10 11:22:52.187-05	t
2850	f	BBAA000342	1588		t	LTAdmin	2013-09-10 11:23:05.593-05	t
2851	f	BAAA000281	1237		t	LTAdmin	2013-09-10 11:23:16.718-05	t
2852	f	BBAA000335	1758		t	LTAdmin	2013-09-10 11:23:28.437-05	t
2853	f	BAAA000285	1331		t	LTAdmin	2013-09-10 11:23:40.14-05	t
2854	f	BBAA000332	1922		t	LTAdmin	2013-09-10 11:23:51.265-05	t
2855	f	BAAA000276	1913		t	LTAdmin	2013-09-10 11:24:03.468-05	t
2856	f	BBAA000341	1159		t	LTAdmin	2013-09-10 11:24:16.093-05	t
2857	f	BAAA000280	2138		t	LTAdmin	2013-09-10 11:24:27.953-05	t
2858	f	BBAA000338	2022		t	LTAdmin	2013-09-10 11:24:39.172-05	t
2859	f	BAAA000284	1997		t	LTAdmin	2013-09-10 11:24:51.39-05	t
2860	f	BBAA000331	1079		t	LTAdmin	2013-09-10 11:25:05.109-05	t
2861	f	BAAA000286	1936		t	LTAdmin	2013-09-10 11:25:17.234-05	t
2862	f	BBAA000334	1966		t	LTAdmin	2013-09-10 11:25:30.562-05	t
2863	f	BAAA000218	1925		t	LTAdmin	2013-09-10 11:26:00-05	t
2864	f	BBAA000309	2109		t	LTAdmin	2013-09-10 11:26:11.015-05	t
2865	f	BAAA000219	1358		t	LTAdmin	2013-09-10 11:26:23.547-05	t
2866	f	BBAA000312	1297		t	LTAdmin	2013-09-10 11:26:38.375-05	t
2867	f	BBAA000305	1258		t	LTAdmin	2013-09-10 11:26:50.89-05	t
2868	f	BAAA000217	1294		t	LTAdmin	2013-09-10 11:27:02.515-05	t
2869	f	BAAA000220	1744		t	LTAdmin	2013-09-10 11:27:15.328-05	t
2870	f	BBAA000302	1427		t	LTAdmin	2013-09-10 11:27:27.953-05	t
2871	f	BAAA000224	1890		t	LTAdmin	2013-09-10 11:27:39.968-05	t
2872	f	BBAA000311	1313		t	LTAdmin	2013-09-10 11:27:50.468-05	t
2873	f	BAAA000222	1440		t	LTAdmin	2013-09-10 11:28:02.687-05	t
2874	f	BBAA000308	2048		t	LTAdmin	2013-09-10 11:28:12.797-05	t
2875	f	BAAA000221	1471		t	LTAdmin	2013-09-10 11:28:23.187-05	t
2876	f	BBAA000301	1356		t	LTAdmin	2013-09-10 11:28:34.203-05	t
2877	f	BAAA000225	1193		t	LTAdmin	2013-09-10 11:28:46.75-05	t
2878	f	BBAA000314	1359		t	LTAdmin	2013-09-10 11:29:02.984-05	t
2879	f	BAAA000223	1186		t	LTAdmin	2013-09-10 11:29:14.281-05	t
2880	f	BBAA000307	2019		t	LTAdmin	2013-09-10 11:29:24.484-05	t
2881	f	BAAA000226	1059		t	LTAdmin	2013-09-10 11:29:35.781-05	t
2882	f	BBAA000304	1217		t	LTAdmin	2013-09-10 11:29:46.297-05	t
2883	f	BAAA000230	1104		t	LTAdmin	2013-09-10 11:29:56.703-05	t
2884	f	BBAA000313	1099		t	LTAdmin	2013-09-10 11:30:06.937-05	t
2885	f	BAAA000228	1052		t	LTAdmin	2013-09-10 11:30:17.328-05	t
2886	f	BBAA000310	1087		t	LTAdmin	2013-09-10 11:30:28.64-05	t
2887	f	BAAA000227	1208		t	LTAdmin	2013-09-10 11:30:39.734-05	t
2888	f	BBAA000303	1212		t	LTAdmin	2013-09-10 11:30:50.734-05	t
2889	f	BAAA000229	1413		t	LTAdmin	2013-09-10 11:31:01.125-05	t
2890	f	BBAA000306	1971		t	LTAdmin	2013-09-10 11:31:11.437-05	t
2891	f	BAAA000202	1116		t	LTAdmin	2013-09-10 11:31:26.859-05	t
2892	f	BBAA000371	1455		t	LTAdmin	2013-09-10 11:31:39.765-05	t
2893	f	BAAA000201	2043		t	LTAdmin	2013-09-10 11:31:51.078-05	t
2894	f	BBAA000372	1308		t	LTAdmin	2013-09-10 11:32:04.906-05	t
2897	f	AAAA000024	1869		t	LTAdmin	2013-09-11 08:23:42.484-05	t
2899	f	AAAA000029	1489		t	LTAdmin	2013-09-11 08:24:05.547-05	t
2901	f	AAAA000026	1197		t	LTAdmin	2013-09-11 08:25:03.265-05	t
2904	f	ABAA000115	1944		t	LTAdmin	2013-09-11 08:25:37.015-05	t
2905	f	AAAA000028	1999		t	LTAdmin	2013-09-11 08:25:55.484-05	t
2906	f	ABAA000117	1989		t	LTAdmin	2013-09-11 08:26:05.984-05	t
2908	f	ABAA000157	1822		t	LTAdmin	2013-09-11 08:28:12.203-05	t
2910	f	ABAA000155	1957		t	LTAdmin	2013-09-11 08:28:32.125-05	t
2912	f	ABAA000103	1153		t	LTAdmin	2013-09-11 08:28:51.437-05	t
2913	f	AAAA000021	1648		t	LTAdmin	2013-09-11 08:29:04.765-05	t
2914	f	ABAA000104	1054		t	LTAdmin	2013-09-11 08:29:18.187-05	t
2915	f	AAAA000019	1247		t	LTAdmin	2013-09-11 08:29:31.109-05	t
2916	f	ABAA000105	1161		t	LTAdmin	2013-09-11 08:29:40.203-05	t
2917	f	AAAA000022	1259		t	LTAdmin	2013-09-11 08:29:57.765-05	t
2918	f	ABAA000106	2089		t	LTAdmin	2013-09-11 08:30:09.468-05	t
2919	f	AAAA000013	1981		t	LTAdmin	2013-09-11 08:30:25.812-05	t
2920	f	ABAA000108	1593		t	LTAdmin	2013-09-11 08:30:37.718-05	t
2921	f	AAAA000014	1782		t	LTAdmin	2013-09-11 08:30:49.031-05	t
2922	f	ABAA000110	1114		t	LTAdmin	2013-09-11 08:31:02.14-05	t
2923	f	AAAA000015	1202		t	LTAdmin	2013-09-11 08:31:26.859-05	t
2924	f	ABAA000111	1184		t	LTAdmin	2013-09-11 08:31:37.156-05	t
2925	f	AAAA000016	1456		t	LTAdmin	2013-09-11 08:31:47.359-05	t
2926	f	ABAA000112	1479		t	LTAdmin	2013-09-11 08:31:59.187-05	t
2927	f	AAAA000017	1430		t	LTAdmin	2013-09-11 08:32:11.515-05	t
2928	f	ABAA000154	1357		t	LTAdmin	2013-09-11 08:32:29.765-05	t
2929	f	AAAA000020	1409		t	LTAdmin	2013-09-11 08:32:39.468-05	t
2930	f	ABAA000152	1412		t	LTAdmin	2013-09-11 08:32:49.765-05	t
2931	f	AAAA000023	1429		t	LTAdmin	2013-09-11 08:32:59.656-05	t
2932	f	ABAA000151	1483		t	LTAdmin	2013-09-11 08:33:23.25-05	t
2933	f	AAAA000071	1029		t	LTAdmin	2013-09-11 08:34:33.797-05	t
2934	f	ABAA000153	1951		t	LTAdmin	2013-09-11 08:34:46.50-05	t
2935	f	AAAA000011	2000		t	LTAdmin	2013-09-11 08:34:58.515-05	t
2936	f	ABAA000150	2111		t	LTAdmin	2013-09-11 08:35:10.218-05	t
2937	f	AAAA000012	1447		t	LTAdmin	2013-09-11 08:35:20.234-05	t
2938	f	ABAA000148	1120		t	LTAdmin	2013-09-11 08:35:32.547-05	t
2939	f	AAAA000007	1304		t	LTAdmin	2013-09-11 08:35:42.547-05	t
2940	f	ABAA000146	1865		t	LTAdmin	2013-09-11 08:35:54.359-05	t
2895	f	AAAA000025	1434		f	LTAdmin	2013-09-11 08:23:19.047-05	t
2941	f	AAAA000010	1839		t	LTAdmin	2013-09-11 08:36:08.39-05	t
2942	f	ABAA000149	1862		t	LTAdmin	2013-09-11 08:36:20.187-05	t
2943	f	AAAA000009	2121		t	LTAdmin	2013-09-11 08:36:32.797-05	t
2944	f	ABAA000144	1832		t	LTAdmin	2013-09-11 08:36:45.312-05	t
2945	f	AAAA000002	2064		t	LTAdmin	2013-09-11 08:36:55.609-05	t
2946	f	ABAA000145	1743		t	LTAdmin	2013-09-11 08:37:12.75-05	t
2947	f	AAAA000001	1880		t	LTAdmin	2013-09-11 08:37:22.953-05	t
2948	f	ABAA000147	1487		t	LTAdmin	2013-09-11 08:37:32.953-05	t
2949	f	AAAA000004	1873		t	LTAdmin	2013-09-11 08:37:44.359-05	t
2950	f	ABAA000168	1067		t	LTAdmin	2013-09-11 08:37:55.687-05	t
2951	f	AAAA000006	1481		t	LTAdmin	2013-09-11 08:38:07.015-05	t
2952	f	ABAA000171	1290		t	LTAdmin	2013-09-11 08:38:17.922-05	t
2953	f	AAAA000065	1276		t	LTAdmin	2013-09-11 08:38:28.218-05	t
2954	f	ABAA000170	1931		t	LTAdmin	2013-09-11 08:38:39.843-05	t
2955	f	AAAA000067	1575		t	LTAdmin	2013-09-11 08:39:00.422-05	t
2956	f	ABAA000169	1629		t	LTAdmin	2013-09-11 08:39:09.906-05	t
2957	f	AAAA000068	1251		t	LTAdmin	2013-09-11 08:39:18.922-05	t
2958	f	ABAA000172	1209		t	LTAdmin	2013-09-11 08:39:30.062-05	t
2959	f	AAAA000064	1103		t	LTAdmin	2013-09-11 08:39:50.234-05	t
2960	f	ABAA000167	1395		t	LTAdmin	2013-09-11 08:40:00.25-05	t
2961	f	AAAA000061	1405		t	LTAdmin	2013-09-11 08:40:10.047-05	t
2962	f	ABAA000191	1781		t	LTAdmin	2013-09-11 08:40:20.265-05	t
2963	f	AAAA000063	1566		t	LTAdmin	2013-09-11 08:40:29.672-05	t
2964	f	ABAA000190	1570		t	LTAdmin	2013-09-11 08:40:39.312-05	t
2896	f	ABAA000162	1165		f	LTAdmin	2013-09-11 08:23:31.172-05	t
2898	f	ABAA000113	1846		f	LTAdmin	2013-09-11 08:23:53.828-05	t
2900	f	ABAA000116	2080		f	LTAdmin	2013-09-11 08:24:52.859-05	t
2902	f	ABAA000118	1207		f	LTAdmin	2013-09-11 08:25:13.765-05	t
2903	f	AAAA000031	1173		f	LTAdmin	2013-09-11 08:25:27.109-05	t
2909	f	AAAA000032	1151		f	LTAdmin	2013-09-11 08:28:21.515-05	t
2911	f	AAAA000005	1131		f	LTAdmin	2013-09-11 08:28:41.625-05	t
2965	f	AAAA000060	1716		t	LTAdmin	2013-09-11 08:40:48.312-05	t
2966	f	ABAA000189	1745		t	LTAdmin	2013-09-11 08:41:02.64-05	t
2967	f	AAAA000062	1739		t	LTAdmin	2013-09-11 08:41:11.734-05	t
2968	f	ABAA000188	1066		t	LTAdmin	2013-09-11 08:41:25.047-05	t
2969	f	AAAA000058	1905		t	LTAdmin	2013-09-11 08:41:39.89-05	t
2970	f	ABAA000186	1491		t	LTAdmin	2013-09-11 08:41:59.859-05	t
2971	f	AAAA000073	1410		t	LTAdmin	2013-09-11 08:42:11.578-05	t
2972	f	ABAA000187	1516		t	LTAdmin	2013-09-11 08:42:23.187-05	t
2973	f	AAAA000074	1526		t	LTAdmin	2013-09-11 08:42:40.234-05	t
2974	f	ABAA000184	1383		t	LTAdmin	2013-09-11 08:42:52.453-05	t
2975	f	AAAA000077	1513		t	LTAdmin	2013-09-11 08:43:01.953-05	t
2976	f	ABAA000181	1521		t	LTAdmin	2013-09-11 08:43:24.656-05	t
2977	f	AAAA000075	1507		t	LTAdmin	2013-09-11 08:43:34.468-05	t
2978	f	ABAA000180	1265		t	LTAdmin	2013-09-11 08:43:54.828-05	t
2979	f	AAAA000042	1274		t	LTAdmin	2013-09-11 08:44:10.781-05	t
2980	f	ABAA000183	1239		t	LTAdmin	2013-09-11 08:44:19.875-05	t
2981	f	AAAA000093	1230		t	LTAdmin	2013-09-11 08:44:39.937-05	t
2982	f	ABAA000182	1162		t	LTAdmin	2013-09-11 08:44:49.25-05	t
2983	f	AAAA000038	1446		t	LTAdmin	2013-09-11 08:44:59.765-05	t
2984	f	ABAA000185	1473		t	LTAdmin	2013-09-11 08:45:08.672-05	t
2985	f	AAAA000043	1950		t	LTAdmin	2013-09-11 08:45:33.562-05	t
2986	f	ABAA000134	1495		t	LTAdmin	2013-09-11 08:45:46.578-05	t
2987	f	AAAA000044	1994		t	LTAdmin	2013-09-11 08:45:57.703-05	t
2988	f	ABAA000133	1241		t	LTAdmin	2013-09-11 08:46:06.812-05	t
2989	f	AAAA000039	2071		t	LTAdmin	2013-09-11 08:46:15.625-05	t
3398	f	2139	2139		f	LTAdmin	2013-09-11 13:28:25.531-05	f
2990	f	ABAA000136	2004		t	LTAdmin	2013-09-11 08:46:37.922-05	t
2991	f	AAAA000036	1192		t	LTAdmin	2013-09-11 08:46:48.047-05	t
2992	f	ABAA000138	1049		t	LTAdmin	2013-09-11 08:46:56.875-05	t
2993	f	AAAA000041	1037		t	LTAdmin	2013-09-11 08:47:06.265-05	t
2994	f	ABAA000135	1034		t	LTAdmin	2013-09-11 08:47:15.265-05	t
2995	f	AAAA000035	1026		t	LTAdmin	2013-09-11 08:47:32.922-05	t
2996	f	ABAA000137	1206		t	LTAdmin	2013-09-11 08:47:42.578-05	t
2997	f	AAAA000037	1505		t	LTAdmin	2013-09-11 08:47:55.093-05	t
2998	f	ABAA000142	1667		t	LTAdmin	2013-09-11 08:48:06.109-05	t
2999	f	AAAA000033	1792		t	LTAdmin	2013-09-11 08:48:15.515-05	t
3000	f	ABAA000140	1399		t	LTAdmin	2013-09-11 08:48:25.422-05	t
3001	f	AAAA000034	1015		t	LTAdmin	2013-09-11 08:48:35.265-05	t
3002	f	ABAA000132	1883		t	LTAdmin	2013-09-11 08:48:52.609-05	t
3003	f	AAAA000086	1287		t	LTAdmin	2013-09-11 08:49:01.203-05	t
3004	f	ABAA000130	1638		t	LTAdmin	2013-09-11 08:49:10.703-05	t
3005	f	AAAA000088	1522		t	LTAdmin	2013-09-11 08:49:20.312-05	t
3006	f	ABAA000131	1234		t	LTAdmin	2013-09-11 08:49:29.093-05	t
3007	f	AAAA000087	1118		t	LTAdmin	2013-09-11 08:49:38.484-05	t
3008	f	ABAA000129	1264		t	LTAdmin	2013-09-11 08:49:47.984-05	t
3009	f	AAAA000089	2023		t	LTAdmin	2013-09-11 08:49:57.672-05	t
3010	f	ABAA000128	1485		t	LTAdmin	2013-09-11 08:50:07.015-05	t
3011	f	AAAA000085	2014		t	LTAdmin	2013-09-11 08:50:16.015-05	t
3012	f	ABAA000127	2063		t	LTAdmin	2013-09-11 08:50:25.093-05	t
3013	f	AAAA000084	1680		t	LTAdmin	2013-09-11 08:50:34.797-05	t
3014	f	ABAA000126	1947		t	LTAdmin	2013-09-11 08:50:48.859-05	t
3015	f	AAAA000083	1967		t	LTAdmin	2013-09-11 08:51:11.578-05	t
3016	f	ABAA000125	1268		t	LTAdmin	2013-09-11 08:51:29.547-05	t
3017	f	AAAA000082	1275		t	LTAdmin	2013-09-11 08:51:38.64-05	t
3018	f	ABAA000123	1262		t	LTAdmin	2013-09-11 08:51:50.468-05	t
3019	f	AAAA000045	1672		t	LTAdmin	2013-09-11 08:52:06.093-05	t
3020	f	ABAA000124	1748		t	LTAdmin	2013-09-11 08:52:17.64-05	t
3021	f	AAAA000076	1425		t	LTAdmin	2013-09-11 08:52:27.953-05	t
3022	f	ABAA000121	1418		t	LTAdmin	2013-09-11 08:52:42.843-05	t
3023	f	AAAA000081	1683		t	LTAdmin	2013-09-11 08:52:52.031-05	t
3024	f	ABAA000122	1712		t	LTAdmin	2013-09-11 08:53:03.047-05	t
3025	f	AAAA000079	1733		t	LTAdmin	2013-09-11 08:53:12.453-05	t
3026	f	ABAA000193	1391		t	LTAdmin	2013-09-11 08:53:34.859-05	t
3027	f	AAAA000047	1433		t	LTAdmin	2013-09-11 08:53:45.953-05	t
3028	f	ABAA000192	1370		t	LTAdmin	2013-09-11 08:53:56.343-05	t
3029	f	AAAA000046	2122		t	LTAdmin	2013-09-11 08:54:06.859-05	t
3030	f	ABAA000194	1917		t	LTAdmin	2013-09-11 08:54:17.656-05	t
3031	f	AAAA000048	2096		t	LTAdmin	2013-09-11 08:54:28.359-05	t
3032	f	ABAA000195	1163		t	LTAdmin	2013-09-11 08:54:41.265-05	t
3033	f	AAAA000050	1955		t	LTAdmin	2013-09-11 08:54:51.578-05	t
3034	f	ABAA000196	1154		t	LTAdmin	2013-09-11 08:55:00.687-05	t
3035	f	AAAA000049	1624		t	LTAdmin	2013-09-11 08:55:12.50-05	t
3036	f	ABAA000199	1552		t	LTAdmin	2013-09-11 08:55:24-05	t
3037	f	AAAA000052	2025		t	LTAdmin	2013-09-11 08:55:33.437-05	t
3038	f	ABAA000139	1252		t	LTAdmin	2013-09-11 08:55:48.781-05	t
3039	f	AAAA000051	1810		t	LTAdmin	2013-09-11 08:55:59.281-05	t
3040	f	ABAA000143	1077		t	LTAdmin	2013-09-11 08:56:09.578-05	t
3041	f	AAAA000054	1365		t	LTAdmin	2013-09-11 08:56:19.125-05	t
3042	f	ABAA000141	1387		t	LTAdmin	2013-09-11 08:56:31.75-05	t
3043	f	AAAA000053	2078		t	LTAdmin	2013-09-11 08:56:51.812-05	t
3044	f	ABAA000109	1146		t	LTAdmin	2013-09-11 08:57:02.218-05	t
3045	f	AAAA000055	1959		t	LTAdmin	2013-09-11 08:57:19.562-05	t
3046	f	ABAA000198	1245		t	LTAdmin	2013-09-11 08:57:29.078-05	t
3047	f	AAAA000056	1623		t	LTAdmin	2013-09-11 08:57:38.875-05	t
3048	f	ABAA000197	1190		t	LTAdmin	2013-09-11 08:57:48.609-05	t
3049	f	ABAA000200	2127		t	LTAdmin	2013-09-11 08:58:10.578-05	t
3050	f	AAAA000078	1669		t	LTAdmin	2013-09-11 08:58:22.406-05	t
3051	f	CAAA000406	1530		t	LTAdmin	2013-09-11 09:25:25.031-05	t
3052	f	CBAA000540	1036		t	LTAdmin	2013-09-11 09:25:35.218-05	t
3053	f	CAAA000401	1385		t	LTAdmin	2013-09-11 09:25:45.015-05	t
3054	f	CBAA000543	1080		t	LTAdmin	2013-09-11 09:25:55.547-05	t
3055	f	CBAA000539	1109		t	LTAdmin	2013-09-11 09:26:10.984-05	t
3056	f	CAAA000402	1091		t	LTAdmin	2013-09-11 09:26:20.984-05	t
3057	f	CBAA000546	1090		t	LTAdmin	2013-09-11 09:26:32.89-05	t
3058	f	CBAA000545	1105		t	LTAdmin	2013-09-11 09:28:10.203-05	t
3059	f	CAAA000404	1009		t	LTAdmin	2013-09-11 09:28:22.75-05	t
3060	f	CBAA000541	1935		t	LTAdmin	2013-09-11 09:28:35.156-05	t
3061	f	CAAA000408	1022		t	LTAdmin	2013-09-11 09:28:49.812-05	t
3062	f	CBAA000542	1058		t	LTAdmin	2013-09-11 09:29:14.343-05	t
3063	f	CAAA000407	1906		t	LTAdmin	2013-09-11 09:29:39.25-05	t
3064	f	CBAA000548	1765		t	LTAdmin	2013-09-11 09:29:54.187-05	t
3065	f	CAAA000409	1085		t	LTAdmin	2013-09-11 09:30:19.406-05	t
3066	f	CBAA000544	1895		t	LTAdmin	2013-09-11 09:30:30.718-05	t
3067	f	CAAA000410	2016		t	LTAdmin	2013-09-11 09:30:44.062-05	t
3068	f	CBAA000547	1942		t	LTAdmin	2013-09-11 09:30:56.187-05	t
3069	f	CAAA000412	1946		t	LTAdmin	2013-09-11 09:31:11.125-05	t
3070	f	CBAA000531	2118		t	LTAdmin	2013-09-11 09:31:30-05	t
3071	f	CAAA000414	1695		t	LTAdmin	2013-09-11 09:31:46.343-05	t
3072	f	CBAA000522	1731		t	LTAdmin	2013-09-11 09:32:12.359-05	t
3073	f	CAAA000411	2010		t	LTAdmin	2013-09-11 09:32:35.078-05	t
3074	f	CBAA000525	1736		t	LTAdmin	2013-09-11 09:32:51.922-05	t
3075	f	CAAA000413	1581		t	LTAdmin	2013-09-11 09:33:01.218-05	t
3076	f	CBAA000521	2104		t	LTAdmin	2013-09-11 09:33:13.062-05	t
3077	f	CAAA000415	1525		t	LTAdmin	2013-09-11 09:33:29.312-05	t
3078	f	CBAA000532	1642		t	LTAdmin	2013-09-11 09:33:39.812-05	t
3079	f	CAAA000493	1244		t	LTAdmin	2013-09-11 09:33:51.234-05	t
3400	f	2017	2017		f	LTAdmin	2013-09-11 13:28:50.047-05	f
3401	f	1568	1568		f	LTAdmin	2013-09-11 13:29:02.547-05	f
3402	f	1398	1398		f	LTAdmin	2013-09-11 13:29:14.047-05	f
3403	f	1442	1442		f	LTAdmin	2013-09-11 13:29:26.031-05	f
3404	f	1788	1788		f	LTAdmin	2013-09-11 13:29:37.515-05	f
3405	f	1746	1746		f	LTAdmin	2013-09-11 13:29:49.515-05	f
3406	f	1762	1762		f	LTAdmin	2013-09-11 13:30:59.015-05	f
3407	f	1330	1330		f	LTAdmin	2013-09-11 13:31:11-05	f
3408	f	1784	1784		f	LTAdmin	2013-09-11 13:31:26.515-05	f
3409	f	1508	1508		f	LTAdmin	2013-09-11 13:31:38.531-05	f
3410	f	1934	1934		f	LTAdmin	2013-09-11 13:31:50.50-05	f
3411	f	1628	1628		f	LTAdmin	2013-09-11 13:32:02.50-05	f
3412	f	2058	2058		f	LTAdmin	2013-09-11 13:32:14.50-05	f
3413	f	1071	1071		f	LTAdmin	2013-09-11 13:32:26-05	f
3414	f	1567	1567		f	LTAdmin	2013-09-11 13:32:37.984-05	f
3415	f	1569	1569		f	LTAdmin	2013-09-11 13:32:49.984-05	f
3416	f	2053	2053		f	LTAdmin	2013-09-11 13:33:59.031-05	f
3417	f	1565	1565		f	LTAdmin	2013-09-11 13:34:10.515-05	f
3418	f	2137	2137		f	LTAdmin	2013-09-11 13:34:26.515-05	f
3419	f	1155	1155		f	LTAdmin	2013-09-11 13:34:38.015-05	f
3420	f	1121	1121		f	LTAdmin	2013-09-11 13:34:50.515-05	f
3421	f	1130	1130		f	LTAdmin	2013-09-11 13:35:02.531-05	f
3422	f	1122	1122		f	LTAdmin	2013-09-11 13:35:14.531-05	f
3423	f	2040	2040		f	LTAdmin	2013-09-11 13:35:26.531-05	f
3424	f	1982	1982		f	LTAdmin	2013-09-11 13:35:38.531-05	f
3425	f	1717	1717		f	LTAdmin	2013-09-11 13:35:50.531-05	f
3426	f	1482	1482		f	LTAdmin	2013-09-11 13:37:04.531-05	f
3427	f	1084	1084		f	LTAdmin	2013-09-11 13:37:16.515-05	f
3428	f	1753	1753		f	LTAdmin	2013-09-11 13:37:31.515-05	f
3429	f	1499	1499		f	LTAdmin	2013-09-11 13:37:43.531-05	f
3430	f	1038	1038		f	LTAdmin	2013-09-11 13:37:55.515-05	f
3431	f	1344	1344		f	LTAdmin	2013-09-11 13:38:07.531-05	f
3432	f	1216	1216		f	LTAdmin	2013-09-11 13:38:19.547-05	f
3433	f	2108	2108		f	LTAdmin	2013-09-11 13:38:31.031-05	f
3434	f	1617	1617		f	LTAdmin	2013-09-11 13:38:43.031-05	f
3435	f	1611	1611		f	LTAdmin	2013-09-11 13:38:55.031-05	f
3436	f	1720	1720		f	LTAdmin	2013-09-11 13:40:07.031-05	f
3437	f	1737	1737		f	LTAdmin	2013-09-11 13:40:19.031-05	f
3438	f	1607	1607		f	LTAdmin	2013-09-11 13:40:34.047-05	f
3439	f	1761	1761		f	LTAdmin	2013-09-11 13:40:46.031-05	f
3440	f	1393	1393		f	LTAdmin	2013-09-11 13:40:57.531-05	f
3441	f	1937	1937		f	LTAdmin	2013-09-11 13:41:09.531-05	f
3442	f	1976	1976		f	LTAdmin	2013-09-11 13:41:21.531-05	f
3443	f	1975	1975		f	LTAdmin	2013-09-11 13:41:33.531-05	f
3444	f	1213	1213		f	LTAdmin	2013-09-11 13:41:45.531-05	f
3445	f	1312	1312		f	LTAdmin	2013-09-11 13:41:57.531-05	f
3446	f	1523	1523		f	LTAdmin	2013-09-11 13:43:10.531-05	f
3447	f	1373	1373		f	LTAdmin	2013-09-11 13:43:22.031-05	f
3448	f	1386	1386		f	LTAdmin	2013-09-11 13:43:37.547-05	f
3449	f	1619	1619		f	LTAdmin	2013-09-11 13:43:49.031-05	f
3450	f	2097	2097		f	LTAdmin	2013-09-11 13:44:01.047-05	f
3451	f	1790	1790		f	LTAdmin	2013-09-11 13:44:13.031-05	f
3452	f	1750	1750		f	LTAdmin	2013-09-11 13:44:25.062-05	f
3453	f	1986	1986		f	LTAdmin	2013-09-11 13:44:48.547-05	f
3454	f	2012	2012		f	LTAdmin	2013-09-11 13:45:00.531-05	f
3455	f	1512	1512		f	LTAdmin	2013-09-11 13:46:17.062-05	f
3456	f	Arab47	1296		f	LTAdmin	2013-09-11 13:46:29.047-05	f
3457	f	Arab48	1400		f	LTAdmin	2013-09-11 13:46:45.047-05	f
3458	f	Arab49	1406		f	LTAdmin	2013-09-11 13:46:57.062-05	f
3459	f	Arab50	1817		f	LTAdmin	2013-09-11 13:47:09.062-05	f
3460	f	Arab51	1816		f	LTAdmin	2013-09-11 13:47:21.047-05	f
3461	f	Arab52	1713		f	LTAdmin	2013-09-11 13:47:33.047-05	f
3462	f	Arab53	1831		f	LTAdmin	2013-09-11 13:47:45.047-05	f
3463	f	Arab54	1808		f	LTAdmin	2013-09-11 13:47:56.547-05	f
3464	f	Arab55	1701		f	LTAdmin	2013-09-11 13:48:08.562-05	f
3465	f	Brachy22	2091		f	LTAdmin	2013-09-11 13:49:22.578-05	f
3466	f	Brachy23	1250		f	LTAdmin	2013-09-11 13:49:34.578-05	f
3467	f	Brachy24	1377		f	LTAdmin	2013-09-11 13:49:50.562-05	f
3468	f	Brachy25	1926		f	LTAdmin	2013-09-11 13:50:02.578-05	f
3469	f	Arab104	1710		f	LTAdmin	2013-09-11 13:50:14.578-05	f
3470	f	1691	1691		f	LTAdmin	2013-09-11 13:50:26.562-05	f
3471	f	1302	1302		f	LTAdmin	2013-09-11 13:50:38.562-05	f
3472	f	1657	1657		f	LTAdmin	2013-09-11 13:50:50.547-05	f
3473	f	1697	1697		f	LTAdmin	2013-09-11 13:51:02.062-05	f
3474	f	1587	1587		f	LTAdmin	2013-09-11 13:51:14.062-05	f
3475	f	1582	1582		f	LTAdmin	2013-09-11 13:52:28.578-05	f
3476	f	1794	1794		f	LTAdmin	2013-09-11 13:52:40.578-05	f
3477	f	2134	2134		f	LTAdmin	2013-09-11 13:52:56.578-05	f
3479	f	1742	1742		f	LTAdmin	2013-09-11 13:53:20.593-05	f
\.


--
-- TOC entry 1866 (class 0 OID 0)
-- Dependencies: 135
-- Name: plants_id_seq; Type: SEQUENCE SET; Schema: public; Owner: LTSysAdmin
--

SELECT pg_catalog.setval('plants_id_seq', 3504, true);


--
-- TOC entry 1842 (class 0 OID 18077)
-- Dependencies: 138
-- Data for Name: pumps; Type: TABLE DATA; Schema: public; Owner: LTSysAdmin
--

COPY pumps (pumpname, comport, califacmlpr, speedinmlpm, backlash, direction, type, id, propagated, label, state, last_state_change, last_state_changed_by, creator, time_stamp) FROM stdin;
Watering 3 Pump 2	\N	7.34975	1569	0	Clockwise	\N	10	f	Pump3B_001	0	2013-05-27 16:21:27.968-05	LemnaTec Support	LemnaTec Support	2013-05-27 16:21:27.968-05
Watering 2 Pump 2	\N	7.2399998	1052	0	Clockwise	\N	13	f	Pump2B_001	0	2013-05-27 17:03:31.906-05	LemnaTec Support	LemnaTec Support	2013-05-27 17:03:31.906-05
Watering 2 Pump 2	\N	6.9520102	1040	0	Clockwise	\N	11	f	Pump3A_001	1	2013-05-27 17:08:51.359-05	LemnaTec Support	LemnaTec Support	2013-05-27 16:37:23.578-05
Watering 2 Pump 1	\N	7.2705498	1581	0	Clockwise	\N	12	f	Pump2A_001	0	2013-05-27 17:09:08.781-05	LemnaTec Support	LemnaTec Support	2013-05-27 16:48:14.375-05
Watering 1 Pump 1	\N	7.1121402	1573	0	Clockwise	\N	14	f	Pump1A_001	0	2013-05-28 10:22:23.315-05	LemnaTec Support	LemnaTec Support	2013-05-28 10:22:23.315-05
Watering 4 Pump 1	\N	7.2399998	1539	0	Clockwise	\N	7	f	Pump4A_001	0	2013-05-27 14:44:23.328-05	LemnaTec Support	LemnaTec Support	2013-05-27 14:44:23.328-05
Watering A Pump 1	\N	7.0329399	769	0	Clockwise	\N	2	f	Pump 1A 50rev/min	1	2013-05-28 10:52:11.205-05	LemnaTec Support	LemnaTec Support	2013-05-27 09:28:44.437-05
Watering A Pump 1	\N	7.0329599	350	0	Clockwise	\N	3	f	Pump 1A 50rev/min	1	2013-05-28 10:52:13.72-05	LemnaTec Support	LemnaTec Support	2013-05-27 14:06:40.875-05
Watering 3 Pump 1	\N	7.20719	1576	0	Clockwise	\N	9	f	Pump3A_001	0	2013-05-28 10:52:29.908-05	LemnaTec Support	LemnaTec Support	2013-05-27 16:16:06.812-05
Watering A Pump 1	\N	7.0329599	350	0	Clockwise	\N	4	f	Pump1A	1	2013-05-28 10:52:40.986-05	LemnaTec Support	LemnaTec Support	2013-05-27 14:06:53.703-05
Watering A Pump 1	\N	7.2329402	1500	0	Clockwise	\N	5	f	PumpA1_001	0	2013-05-27 14:17:30.031-05	LemnaTec Support	LemnaTec Support	2013-05-27 14:17:30.031-05
Watering 4 Pump 2	\N	7.348012	1500	0	Clockwise	\N	8	f	Pump4B_001	0	2013-05-27 14:50:49.703-05	LemnaTec Support	LemnaTec Support	2013-05-27 14:50:49.703-05
Watering A Pump 2	\N	7.33887	1500	0	Clockwise	\N	6	f	PumpA2_001	0	2013-05-27 14:22:26.078-05	LemnaTec Support	LemnaTec Support	2013-05-27 14:22:26.078-05
Watering 1 Pump 2	\N	7.1755099	1571	0	Clockwise	\N	15	f	Pump1B_001	1	2013-08-29 14:25:43.786-05	LTAdmin	LemnaTec Support	2013-05-28 10:28:29.705-05
Watering A Pump 2	\N	7.4685631	670	0	Clockwise	\N	17	f	PumpA2_002	0	2013-08-29 15:00:35.317-05	LTAdmin	LTAdmin	2013-08-29 15:00:35.317-05
Watering 1 Pump 2	\N	7.5465589	669	0	Clockwise	\N	16	f	Pump1B_002	0	2013-08-29 14:24:27.083-05	LTAdmin	LTAdmin	2013-08-29 14:24:27.083-05
Watering 3 Pump 2	\N	7.3072491	669	0	Clockwise	\N	18	f	Pump3B_002	0	2013-08-29 15:40:19.723-05	LTAdmin	LTAdmin	2013-08-29 15:40:19.723-05
Watering 4 Pump 2	\N	7.3675032	669	0	Clockwise	\N	19	f	Pump4B_002	0	2013-08-29 15:51:32.552-05	LTAdmin	LTAdmin	2013-08-29 15:51:32.552-05
Watering 2 Pump 2	\N	7.4180589	674	0	Clockwise	\N	20	f	Pump2B_002	0	2013-08-29 16:31:41.942-05	LTAdmin	LTAdmin	2013-08-29 16:31:41.942-05
\.


--
-- TOC entry 1867 (class 0 OID 0)
-- Dependencies: 136
-- Name: pumps_id_seq; Type: SEQUENCE SET; Schema: public; Owner: LTSysAdmin
--

SELECT pg_catalog.setval('pumps_id_seq', 20, true);


--
-- TOC entry 1838 (class 0 OID 17863)
-- Dependencies: 134
-- Data for Name: serviceinterval; Type: TABLE DATA; Schema: public; Owner: LTSysAdmin
--

COPY serviceinterval (id, data, device, name, serviceinfo, propagated) FROM stdin;
2	AADbmXo/MQkAAAAAAQAAAAAAAAA=	databaseconnection	localhost: Physical Disk Warning	\N	\N
3	AADbmXo/MQkAAAAAAQAAAAAAAAA=	databaseconnection	localhost: Storage Controller Warning	\N	\N
4	AADbmXo/MQkAAAAAAQAAAAAAAAA=	databaseconnection	localhost: Storage Controller Warning	\N	\N
5	AADbmXo/MQkAAAAAAQAAAAAAAAA=	databaseconnection	localhost: Storage Controller Warning	\N	\N
6	AADbmXo/MQkAAAAAAQAAAAAAAAA=	databaseconnection	localhost: Physical Disk Warning	\N	\N
36	AADbmXo/MQkAAAAAAQAAAAAAAAA=	databaseconnection	localhost: Storage Controller Warning	\N	\N
37	AADbmXo/MQkAAAAAAQAAAAAAAAA=	databaseconnection	localhost: Storage Controller Warning	\N	\N
38	AADbmXo/MQkAAAAAAQAAAAAAAAA=	databaseconnection	localhost: Storage Controller Warning	\N	\N
39	8ihD4uYx0IgAAAAAAQAAAAEAAAA=	databaseconnection	localhost: Physical Disk Warning	\N	f
44	hiqB2KOC0Ij4AQAAAAAAAAAAAAA=	additionalservicing.greasemobileparts	Grease  Mobile Parts		f
45	StZFv3fA0IgQEQAAAAAAAAAAAAA=	additionalservicing.serverclimatisation	Server Climatisation		f
46	QqlhgzJ50IiICAAAAAAAAAAAAAA=	conveyor.safedomains.WateringA.pumps.pump1	Watering A Pump 1		f
47	XvdvgzJ50IiICAAAAAAAAAAAAAA=	conveyor.safedomains.WateringA.pumps.pump2	Watering A Pump 2		f
48	siEyl7TA0IgQEQAAAAAAAAAAAAA=	conveyor.safedomains.WateringA.scales	Scales A		f
49	AotGhDJ50IiICAAAAAAAAAAAAAA=	conveyor.safedomains.Watering4.pumps.pump1	Watering 4 Pump 1		f
50	0p1ZhDJ50IiICAAAAAAAAAAAAAA=	conveyor.safedomains.Watering4.pumps.pump2	Watering 4 Pump 2		f
51	NO8imLTA0IgQEQAAAAAAAAAAAAA=	conveyor.safedomains.Watering4.scales	Scales 4		f
52	bEuohDJ50IiICAAAAAAAAAAAAAA=	conveyor.safedomains.Watering3.pumps.pump1	Watering 3 Pump 1		f
53	4vu4hDJ50IiICAAAAAAAAAAAAAA=	conveyor.safedomains.Watering3.pumps.pump2	Watering 3 Pump 2		f
54	KP9zmLTA0IgQEQAAAAAAAAAAAAA=	conveyor.safedomains.Watering3.scales	Scales 3		f
55	ur37hDJ50IiICAAAAAAAAAAAAAA=	conveyor.safedomains.Watering2.pumps.pump1	Watering 2 Pump 1		f
56	MG4MhTJ50IiICAAAAAAAAAAAAAA=	conveyor.safedomains.Watering2.pumps.pump2	Watering 2 Pump 2		f
57	0NPJmLTA0IgQEQAAAAAAAAAAAAA=	conveyor.safedomains.Watering2.scales	Scales 2		f
58	YpJRhTJ50IiICAAAAAAAAAAAAAA=	conveyor.safedomains.Watering1.pumps.pump1	Watering 1 Pump 1		f
59	5mlphTJ50IiICAAAAAAAAAAAAAA=	conveyor.safedomains.Watering1.pumps.pump2	Watering 1 Pump 2		f
60	LG0kmbTA0IgQEQAAAAAAAAAAAAA=	conveyor.safedomains.Watering1.scales	Scales 1		f
61	2uJ/WiST0IjwAwAAAAAAAAAAAAA=	conveyor.safedomains.VIS.devices.lights.side	VIS Side Light		f
62	QmyJWiST0IjwAwAAAAAAAAAAAAA=	conveyor.safedomains.VIS.devices.lights.top	VIS Top Light		f
63	OkyrU4JQ0Yg4IgAAAAAAAAAAAAA=	imagingunits.NIR.side.cameradriver.camera	NIR Side Camera		f
40	Spauq/V40IiICAAAAAAAAAAAAAA=	additionalservicing.donaldsonfilter	Donaldson Filter		f
41	StZFv3fA0IgQEQAAAAAAAAAAAAA=	additionalservicing.controlcomputerharddiskcheck	Control Computer Harddisk Check		f
64	uv6RYoJQ0Yg4IgAAAAAAAAAAAAA=	imagingunits.NIR.top.cameradriver.camera	NIR Top Camera		f
65	UJOQWiST0IjwAwAAAAAAAAAAAAA=	conveyor.safedomains.NIR.devices.lights.side	NIR Side Light		f
42	KorKgqN30IioAAAAAAAAAAAAAAA=	additionalservicing.databasebackup	Database Backup		f
43	2rpJ1qOC0Ij4AQAAAAAAAAAAAAA=	additionalservicing.systemcleaning	System Cleaning		f
67	AADbmXo/MQkAAAAAAQAAAAAAAAA=	databaseconnection	10.18.0.21: Just another test, no need for intervention	\N	f
68	AADbmXo/MQkAAAAAAQAAAAAAAAA=	databaseconnection	10.18.0.21: Power Supply Critical	\N	\N
69	AADbmXo/MQkAAAAAAQAAAAAAAAA=	databaseconnection	10.18.0.21: Redundancy Lost	\N	\N
70	xIfU0QhA0IgAAAAAAQAAAAEAAAA=	databaseconnection	10.18.0.21: 	\N	f
66	XrqXWiST0IjwAwAAAAAAAAAAAAA=	conveyor.safedomains.NIR.devices.lights.top	NIR Top Light		f
\.


--
-- TOC entry 1868 (class 0 OID 0)
-- Dependencies: 133
-- Name: serviceinterval_id_seq; Type: SEQUENCE SET; Schema: public; Owner: LTSysAdmin
--

SELECT pg_catalog.setval('serviceinterval_id_seq', 70, true);


--
-- TOC entry 1850 (class 0 OID 18246)
-- Dependencies: 146
-- Data for Name: serviceresets; Type: TABLE DATA; Schema: public; Owner: LTSysAdmin
--

COPY serviceresets (id, devicename, duetime, currentcounter, "user", "timestamp") FROM stdin;
2	databaseconnection	0001-01-01 00:00:00-06	0	LTAdmin	2013-06-07 17:38:36.659-05
3	additionalservicing.databasebackup	2013-06-14 03:53:20.19-05	0	LTAdmin	2013-06-25 17:16:41.399-05
4	databaseconnection	0001-01-01 00:00:00-06	0	LTAdmin	2013-06-25 17:16:48.352-05
5	additionalservicing.systemcleaning	2013-06-28 03:53:20.19-05	0	LTAdmin	2013-06-28 07:06:21.125-05
6	additionalservicing.greasemobileparts	2013-06-28 03:53:20.19-05	0	LTAdmin	2013-06-28 07:06:22.984-05
7	additionalservicing.databasebackup	2013-07-02 17:16:41.399-05	0	LTAdmin	2013-07-03 08:42:37.859-05
8	additionalservicing.databasebackup	2013-07-10 08:42:37.875-05	0	LTAdmin	2013-07-12 14:44:28.196-05
9	additionalservicing.databasebackup	2013-07-19 14:44:28.212-05	0	LTAdmin	2013-08-28 11:32:41.677-05
10	additionalservicing.systemcleaning	2013-07-19 07:06:21.14-05	0	LTAdmin	2013-08-28 11:32:44.567-05
11	additionalservicing.greasemobileparts	2013-07-19 07:06:22.984-05	0	LTAdmin	2013-08-28 11:32:48.27-05
12	conveyor.safedomains.VIS.devices.lights.side	2013-07-19 11:11:49.956-05	0	LTAdmin	2013-08-28 11:33:00.567-05
13	conveyor.safedomains.VIS.devices.lights.top	2013-07-19 11:11:49.971-05	0	LTAdmin	2013-08-28 11:33:00.614-05
14	conveyor.safedomains.NIR.devices.lights.side	2013-07-19 11:13:10.409-05	0	LTAdmin	2013-08-28 11:33:00.677-05
15	conveyor.safedomains.NIR.devices.lights.top	2013-07-19 11:13:10.425-05	0	LTAdmin	2013-08-28 11:33:00.708-05
\.


--
-- TOC entry 1869 (class 0 OID 0)
-- Dependencies: 145
-- Name: serviceresets_id_seq; Type: SEQUENCE SET; Schema: public; Owner: LTSysAdmin
--

SELECT pg_catalog.setval('serviceresets_id_seq', 15, true);


--
-- TOC entry 1848 (class 0 OID 18235)
-- Dependencies: 144
-- Data for Name: watering; Type: TABLE DATA; Schema: public; Owner: LTSysAdmin
--

COPY watering (id, identcode, "time", starthour, finishhour, quantity, pumpname, type, "FormulaTW", done, status, watered, processed, deficency, deflimit, creator, created, pumpconfigids, commentary) FROM stdin;
41939	CBAA000527	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 13:29:18.186-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.718-05	5	
41940	CAAA000495	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 13:30:29.843-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.718-05	5	
41942	CAAA000496	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 13:32:54.311-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.733-05	5	
41943	CBAA000523	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 13:34:06.327-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.733-05	5	
41944	CAAA000497	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 13:35:18.968-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.749-05	5	
41945	CBAA000530	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 13:36:31.468-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.764-05	5	
41946	CAAA000498	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 13:37:43.718-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.764-05	5	
41947	CBAA000529	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 13:38:55.233-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.764-05	5	
41948	CAAA000500	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 13:40:07.358-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.78-05	5	
41949	CBAA000526	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 13:41:20.546-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.78-05	5	
41950	CAAA000499	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 13:42:32.671-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.78-05	5	
41951	CBAA000592	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 13:43:45.28-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.78-05	5	
41952	CBAA000585	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 13:44:56.233-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.78-05	5	
41923	CBAA000544	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:46:15.327-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.671-05	5	
41925	CBAA000547	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:48:39.593-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.671-05	5	
41926	CAAA000412	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:49:33.514-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.686-05	5	
41927	CBAA000531	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:50:26.249-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.686-05	5	
41928	CAAA000414	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:55:40.046-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.686-05	5	
41929	CBAA000522	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:56:57.889-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.686-05	5	
41930	CAAA000411	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:58:09.327-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.686-05	5	
41931	CBAA000525	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:59:21.983-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.686-05	5	
41932	CAAA000413	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:00:33.483-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.702-05	5	
41933	CBAA000521	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:01:45.999-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.702-05	5	
41934	CAAA000415	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:02:59.264-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.702-05	5	
41935	CBAA000532	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:04:11.093-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.702-05	5	
41936	CAAA000493	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:05:05.483-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.718-05	5	
41908	CAAA000406	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:10:44.358-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.624-05	5	
41909	CBAA000540	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:12:00.874-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.624-05	5	
41910	CAAA000401	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:13:13.686-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.624-05	5	
41911	CBAA000543	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:14:26.171-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.624-05	5	
41912	CBAA000539	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:15:38.561-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.639-05	5	
41953	CBAA000588	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:39:12.639-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.78-05	5	
41954	CAAA000487	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:40:24.702-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.796-05	5	
41955	CBAA000591	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:41:37.249-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.796-05	5	
41956	CBAA000534	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:42:30.155-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.796-05	5	
41957	CAAA000484	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:43:21.749-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.796-05	5	
41958	CBAA000533	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:48:30.171-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.796-05	5	
41959	CAAA000486	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:49:47.186-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.796-05	5	
41961	CAAA000489	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:52:13.124-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.811-05	5	
41962	CBAA000515	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:53:24.702-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.811-05	5	
41913	CAAA000402	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:17:08.921-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.639-05	5	
41914	CBAA000546	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:18:22.28-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.639-05	5	
41915	CBAA000545	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:19:35.889-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.639-05	5	
41916	CAAA000404	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:20:29.155-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.639-05	5	
41917	CBAA000541	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:21:21.264-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.639-05	5	
41918	CAAA000408	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:26:20.749-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.655-05	5	
41919	CBAA000542	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:27:38.436-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.655-05	5	
41920	CAAA000407	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:28:51.061-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.671-05	5	
41921	CBAA000548	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:30:03.108-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.671-05	5	
41922	CAAA000409	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:31:14.843-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.671-05	5	
41999	CAAA000471	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:19:12.077-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.968-05	5	
42000	CBAA000504	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:20:24.608-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.968-05	5	
42002	CBAA000503	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:22:48.733-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.968-05	5	
42003	CAAA000472	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:24:00.78-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.968-05	5	
42004	CBAA000505	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:25:13.639-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.983-05	5	
42005	CAAA000475	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:26:26.218-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.983-05	5	
42006	CBAA000506	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:27:20.155-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.983-05	5	
42007	CAAA000457	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:28:12.874-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.983-05	5	
42008	CBAA000568	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:40:09.468-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.999-05	5	
42009	CAAA000466	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:41:26.077-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.999-05	5	
42010	CBAA000567	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:42:38.03-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.999-05	5	
42011	CAAA000460	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:43:50.655-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.999-05	5	
42012	CBAA000571	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:45:03.218-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.999-05	5	
41983	CAAA000483	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:39:43.671-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.921-05	5	
41985	CAAA000485	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:42:08.264-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.921-05	5	
41986	CBAA000537	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:43:01.343-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.921-05	5	
41987	CAAA000468	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:43:54.014-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.921-05	5	
41988	CBAA000509	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:52:05.624-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.936-05	5	
41989	CAAA000482	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:53:23.436-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.936-05	5	
41990	CBAA000502	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:54:35.389-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.936-05	5	
41991	CAAA000467	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:55:47.577-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.936-05	5	
41992	CBAA000507	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:56:59.718-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.952-05	5	
41993	CAAA000469	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:58:12.077-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.952-05	5	
41994	CBAA000510	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:59:24.983-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.952-05	5	
41995	CAAA000470	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 16:00:36.546-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.952-05	5	
41996	CBAA000501	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 16:01:30.046-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.952-05	5	
42013	CAAA000462	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:16:50.389-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.999-05	5	
42014	CBAA000574	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:18:03.452-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.014-05	5	
42015	CAAA000458	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:19:15.499-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.014-05	5	
42016	CBAA000573	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:20:09.468-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.014-05	5	
42017	CAAA000459	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:21:02.421-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.014-05	5	
41963	CAAA000491	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:54:36.468-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.811-05	5	
41964	CBAA000512	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:55:49.718-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.827-05	5	
41965	CAAA000490	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:57:01.796-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.827-05	5	
41966	CBAA000518	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:57:56.53-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.827-05	5	
41967	CAAA000488	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:58:48.171-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.827-05	5	
41978	CBAA000519	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:11:03.405-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.905-05	5	
41979	CAAA000480	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:12:20.593-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.905-05	5	
41981	CAAA000481	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:14:44.811-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.905-05	5	
41982	CBAA000538	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:15:57.374-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.905-05	5	
41968	CBAA000511	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:49:03.014-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.843-05	5	
41969	CAAA000492	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:50:19.671-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.874-05	5	
41970	CBAA000514	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:51:32.358-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.874-05	5	
41971	CAAA000477	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:52:44.405-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.874-05	5	
41972	CBAA000517	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:53:56.311-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.874-05	5	
41973	CAAA000476	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:55:08.389-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.889-05	5	
41974	CBAA000513	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:56:21.186-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.889-05	5	
41975	CAAA000478	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:57:33.936-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.889-05	5	
41976	CBAA000520	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:58:27.389-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.889-05	5	
41977	CAAA000479	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:59:19.389-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.905-05	5	
42059	CAAA000431	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:34:53.561-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.139-05	5	
42060	CBAA000551	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:36:05.796-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.139-05	5	
42062	CBAA000554	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:38:31.108-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.155-05	5	
42043	CAAA000437	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 16:33:05.499-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.093-05	5	
42044	CBAA000562	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 16:34:22.249-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.093-05	5	
42045	CAAA000438	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 16:35:34.202-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.093-05	5	
42046	CBAA000561	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 16:36:28.108-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.108-05	5	
42047	CAAA000439	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 16:37:20.108-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.108-05	5	
42048	CBAA000563	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 16:45:26.046-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.108-05	5	
42049	CAAA000440	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 16:46:43.155-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.108-05	5	
42050	CBAA000565	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 16:47:55.733-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.108-05	5	
42051	CAAA000441	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 16:49:08.436-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.108-05	5	
42052	CBAA000566	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 16:50:19.249-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.124-05	5	
42053	CAAA000443	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 16:51:31.702-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.124-05	5	
42055	CAAA000444	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 16:53:56.671-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.124-05	5	
42056	CBAA000550	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 16:54:50.436-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.124-05	5	
42057	CAAA000445	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 16:55:42.186-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.139-05	5	
42018	CBAA000570	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:33:06.889-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.014-05	5	
42019	CAAA000448	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:34:23.452-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.014-05	5	
42020	CBAA000575	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:35:35.858-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.03-05	5	
42021	CAAA000447	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:36:47.749-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.03-05	5	
42022	CBAA000569	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:38:00.468-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.03-05	5	
42023	CAAA000449	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:32:27.28-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.03-05	5	
42024	CBAA000572	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:33:40.296-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.03-05	5	
42025	CAAA000450	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:34:53.077-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.03-05	5	
42026	CBAA000586	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:35:46.374-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.046-05	5	
42028	CBAA000579	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:04:09.983-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.046-05	5	
42029	CAAA000453	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:05:26.655-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.046-05	5	
42030	CBAA000596	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:06:38.858-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.061-05	5	
42031	CAAA000452	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:07:52.202-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.061-05	5	
42032	CBAA000600	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:09:04.233-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.061-05	5	
42033	CAAA000454	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:10:15.983-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.061-05	5	
42034	CBAA000598	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:11:29.405-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.061-05	5	
42035	CAAA000455	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:12:41.452-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.061-05	5	
42036	CBAA000597	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:13:36.514-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.077-05	5	
42037	CAAA000461	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:14:27.608-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.077-05	5	
42038	CBAA000599	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:26:59.546-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.077-05	5	
42039	CAAA000465	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:28:17.108-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.077-05	5	
42041	CAAA000463	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:30:41.421-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.093-05	5	
42042	CBAA000559	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:31:53.889-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.093-05	5	
42063	CAAA000433	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:33:06.593-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.155-05	5	
42064	CBAA000557	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:34:19.343-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.155-05	5	
42065	CAAA000436	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:35:31.077-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.155-05	5	
42066	CBAA000577	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:36:24.014-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.155-05	5	
42067	CAAA000434	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:37:16.764-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.155-05	5	
42068	CAAA000417	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:41:59.577-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.155-05	5	
42069	CBAA000583	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:43:17.077-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.171-05	5	
42070	CAAA000416	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:44:29.343-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.171-05	5	
42071	CBAA000580	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:45:41.452-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.171-05	5	
42072	CAAA000419	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:46:53.452-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.171-05	5	
42119	JAAA000003	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 11:50:06.311-05	Skip	-1	LTAdmin	2013-09-24 11:02:44.108-05	5	
42120	JAAA000001	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 11:52:28.874-05	Skip	-1	LTAdmin	2013-09-24 11:02:44.124-05	5	
42121	JAAA000005	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 11:53:40.53-05	Skip	-1	LTAdmin	2013-09-24 11:02:44.124-05	5	
42122	JAAA000002	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 11:54:34.639-05	Skip	-1	LTAdmin	2013-09-24 11:02:44.124-05	5	
42123	JAAA000004	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 11:55:26.608-05	Skip	-1	LTAdmin	2013-09-24 11:02:44.124-05	5	
42109	HAAA000002	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 12:15:54.374-05	Skip	-1	LTAdmin	2013-09-24 11:01:53.686-05	5	
42110	HAAA000001	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 12:17:11.749-05	Skip	-1	LTAdmin	2013-09-24 11:01:53.686-05	5	
42111	HAAA000003	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 12:18:24.671-05	Skip	-1	LTAdmin	2013-09-24 11:01:53.702-05	5	
42112	HAAA000005	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 12:19:36.561-05	Skip	-1	LTAdmin	2013-09-24 11:01:53.702-05	5	
42113	HAAA000004	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 12:20:48.608-05	Skip	-1	LTAdmin	2013-09-24 11:01:53.702-05	5	
42114	IAAA000002	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 12:22:00.546-05	Skip	-1	LTAdmin	2013-09-24 11:02:17.264-05	5	
42115	IAAA000005	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 12:23:13.171-05	Skip	-1	LTAdmin	2013-09-24 11:02:17.28-05	5	
42116	IAAA000003	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 12:24:24.624-05	Skip	-1	LTAdmin	2013-09-24 11:02:17.28-05	5	
42117	IAAA000001	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 12:25:37.093-05	Skip	-1	LTAdmin	2013-09-24 11:02:17.28-05	5	
42118	IAAA000004	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 12:26:49.608-05	Skip	-1	LTAdmin	2013-09-24 11:02:17.28-05	5	
41938	CAAA000494	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 13:28:00.78-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.718-05	5	
41941	CBAA000528	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 13:31:42.405-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.718-05	5	
42094	EAAA000002	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 18:37:31.577-05	Skip	-1	LTAdmin	2013-09-24 11:00:39.811-05	5	
42095	EAAA000001	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 18:39:53.686-05	Skip	-1	LTAdmin	2013-09-24 11:00:39.811-05	5	
42096	EAAA000003	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 18:41:05.702-05	Skip	-1	LTAdmin	2013-09-24 11:00:39.811-05	5	
42097	EAAA000005	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 18:42:18.499-05	Skip	-1	LTAdmin	2013-09-24 11:00:39.811-05	5	
42098	EAAA000004	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 18:43:30.561-05	Skip	-1	LTAdmin	2013-09-24 11:00:39.827-05	5	
42099	FAAA000002	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 18:44:43.374-05	Skip	-1	LTAdmin	2013-09-24 11:01:04.889-05	5	
42100	FAAA000001	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 18:45:55.936-05	Skip	-1	LTAdmin	2013-09-24 11:01:04.889-05	5	
42101	FAAA000003	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 18:47:09.608-05	Skip	-1	LTAdmin	2013-09-24 11:01:04.889-05	5	
42102	FAAA000005	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 18:48:21.671-05	Skip	-1	LTAdmin	2013-09-24 11:01:04.905-05	5	
42103	FAAA000004	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 18:49:33.436-05	Skip	-1	LTAdmin	2013-09-24 11:01:04.905-05	5	
42105	GAAA000001	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 18:51:58.389-05	Skip	-1	LTAdmin	2013-09-24 11:01:31.139-05	5	
42106	GAAA000003	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 18:53:10.624-05	Skip	-1	LTAdmin	2013-09-24 11:01:31.139-05	5	
42107	GAAA000004	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 18:54:23.233-05	Skip	-1	LTAdmin	2013-09-24 11:01:31.139-05	5	
42108	GAAA000005	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 18:55:35.186-05	Skip	-1	LTAdmin	2013-09-24 11:01:31.139-05	5	
42073	CBAA000584	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:48:06.108-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.186-05	5	
42074	CAAA000418	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:49:19.186-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.186-05	5	
42075	CBAA000578	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:50:31.139-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.202-05	5	
42076	CAAA000421	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:51:24.358-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.202-05	5	
42077	CBAA000581	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:52:16.233-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.202-05	5	
42078	CAAA000420	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Waiting	0	2013-09-25 00:04:55.827-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.202-05	5	
42079	CBAA000576	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Waiting	0	2013-09-25 00:06:13.499-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.218-05	5	
42080	CAAA000423	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Waiting	0	2013-09-25 00:07:26.639-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.218-05	5	
42082	CBAA000595	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Waiting	0	2013-09-25 00:09:50.405-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.218-05	5	
42083	CAAA000403	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Waiting	0	2013-09-25 00:11:02.358-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.218-05	5	
42084	CBAA000589	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Waiting	0	2013-09-25 00:12:14.514-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.233-05	5	
42085	CAAA000425	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Waiting	0	2013-09-25 00:13:26.624-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.233-05	5	
42086	CBAA000593	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Waiting	0	2013-09-25 00:14:19.608-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.233-05	5	
42087	CAAA000422	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Waiting	0	2013-09-25 00:15:11.655-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.233-05	5	
42088	CBAA000590	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Waiting	0	2013-09-25 00:19:40.014-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.249-05	5	
42089	CAAA000426	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Waiting	0	2013-09-25 00:20:57.608-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.249-05	5	
42090	CBAA000594	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Waiting	0	2013-09-25 00:22:10.218-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.249-05	5	
42091	CAAA000424	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Waiting	0	2013-09-25 00:23:21.624-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.249-05	5	
42092	CBAA000582	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Waiting	0	2013-09-25 00:24:34.171-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.249-05	5	
41998	CBAA000508	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:17:55.03-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.968-05	5	
42001	CAAA000474	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:21:36.858-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.968-05	5	
41924	CAAA000410	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 14:47:27.796-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.671-05	5	
41937	CBAA000524	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:05:57.905-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.718-05	5	
42124	BBAA000356	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.014-05	5	\N
42125	BAAA000261	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.014-05	5	\N
42126	BBAA000349	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.014-05	5	\N
42127	BAAA000265	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.03-05	5	\N
42128	BBAA000344	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.03-05	5	\N
42129	BAAA000269	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.03-05	5	\N
42130	BBAA000358	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.046-05	5	\N
42131	BAAA000268	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.046-05	5	\N
42132	BBAA000346	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.046-05	5	\N
42133	BAAA000271	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.046-05	5	\N
42134	BBAA000345	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.046-05	5	\N
42135	BAAA000264	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.046-05	5	\N
42136	BBAA000352	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.061-05	5	\N
42137	BAAA000267	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.061-05	5	\N
42138	BBAA000355	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.061-05	5	\N
42139	BAAA000270	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.061-05	5	\N
42140	BBAA000348	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.061-05	5	\N
42141	BAAA000273	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.061-05	5	\N
42142	BBAA000347	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.077-05	5	\N
42143	BAAA000231	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.077-05	5	\N
42144	BBAA000368	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.077-05	5	\N
42145	BAAA000233	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.077-05	5	\N
42146	BBAA000361	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.077-05	5	\N
42147	BAAA000234	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.077-05	5	\N
42148	BBAA000343	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.093-05	5	\N
42149	BAAA000232	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.093-05	5	\N
42150	BBAA000367	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.093-05	5	\N
42151	BAAA000238	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.093-05	5	\N
42152	BBAA000364	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.108-05	5	\N
42153	BAAA000236	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.108-05	5	\N
42154	BBAA000350	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.108-05	5	\N
42155	BAAA000235	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.108-05	5	\N
42156	BBAA000360	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.108-05	5	\N
42157	BAAA000240	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.108-05	5	\N
42158	BBAA000363	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.124-05	5	\N
42159	BAAA000237	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.124-05	5	\N
42160	BAAA000239	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.124-05	5	\N
42161	BBAA000370	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.124-05	5	\N
42162	BAAA000241	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.139-05	5	\N
42163	BBAA000359	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.139-05	5	\N
42164	BAAA000242	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.139-05	5	\N
42165	BBAA000366	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.139-05	5	\N
42166	BAAA000244	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.139-05	5	\N
42167	BBAA000353	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.139-05	5	\N
42168	BAAA000243	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.155-05	5	\N
42169	BBAA000362	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.155-05	5	\N
42170	BAAA000246	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.155-05	5	\N
42171	BBAA000381	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.155-05	5	\N
42172	BAAA000245	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.155-05	5	\N
42173	BBAA000384	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.155-05	5	\N
42174	BAAA000249	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.171-05	5	\N
42175	BBAA000377	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.171-05	5	\N
42176	BAAA000253	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.171-05	5	\N
42177	BBAA000374	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.171-05	5	\N
42178	BAAA000248	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.171-05	5	\N
42179	BBAA000383	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.171-05	5	\N
42180	BAAA000252	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.186-05	5	\N
42181	BBAA000380	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.202-05	5	\N
42182	BAAA000256	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.202-05	5	\N
42183	BBAA000373	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.202-05	5	\N
42184	BAAA000247	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.202-05	5	\N
42185	BBAA000385	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.218-05	5	\N
42186	BAAA000251	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.218-05	5	\N
42187	BBAA000379	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.218-05	5	\N
42188	BBAA000376	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.233-05	5	\N
42189	BAAA000255	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.233-05	5	\N
42190	BAAA000250	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.233-05	5	\N
42191	BBAA000386	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.249-05	5	\N
42192	BAAA000254	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.249-05	5	\N
42193	BBAA000382	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.249-05	5	\N
42194	BAAA000258	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.249-05	5	\N
42195	BBAA000375	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.249-05	5	\N
42196	BAAA000257	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.249-05	5	\N
42197	BBAA000378	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.264-05	5	\N
42198	BAAA000204	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.264-05	5	\N
42199	BBAA000396	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.264-05	5	\N
42200	BAAA000205	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.264-05	5	\N
42201	BBAA000397	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.264-05	5	\N
42202	BAAA000203	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.264-05	5	\N
42203	BBAA000392	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.28-05	5	\N
42204	BAAA000206	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.296-05	5	\N
42205	BBAA000387	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.296-05	5	\N
42206	BAAA000210	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.311-05	5	\N
42207	BBAA000398	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.311-05	5	\N
42208	BAAA000208	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.311-05	5	\N
42209	BBAA000393	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.327-05	5	\N
42210	BAAA000207	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.327-05	5	\N
42211	BBAA000388	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.327-05	5	\N
42212	BAAA000211	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.327-05	5	\N
42213	BBAA000399	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.327-05	5	\N
42214	BBAA000394	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.343-05	5	\N
42215	BAAA000209	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.343-05	5	\N
42216	BAAA000212	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.343-05	5	\N
42217	BBAA000389	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.343-05	5	\N
42218	BAAA000216	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.343-05	5	\N
42219	BBAA000400	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.358-05	5	\N
42220	BAAA000214	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.358-05	5	\N
42221	BBAA000395	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.358-05	5	\N
42222	BAAA000213	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.358-05	5	\N
42223	BBAA000390	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.374-05	5	\N
42224	BAAA000215	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.374-05	5	\N
42225	BBAA000391	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.374-05	5	\N
42226	BAAA000287	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.374-05	5	\N
42227	BBAA000323	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.374-05	5	\N
42228	BAAA000288	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.389-05	5	\N
42229	BBAA000326	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.389-05	5	\N
42230	BAAA000292	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.389-05	5	\N
42231	BBAA000319	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.389-05	5	\N
42232	BAAA000296	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.389-05	5	\N
42233	BBAA000316	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.405-05	5	\N
42234	BAAA000289	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.405-05	5	\N
42235	BBAA000325	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.405-05	5	\N
42236	BAAA000293	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.421-05	5	\N
42237	BBAA000322	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.421-05	5	\N
42238	BAAA000297	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.436-05	5	\N
42239	BBAA000315	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.436-05	5	\N
42240	BAAA000290	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.436-05	5	\N
42241	BBAA000328	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.452-05	5	\N
42242	BAAA000294	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.452-05	5	\N
42243	BBAA000321	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.452-05	5	\N
42244	BAAA000298	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.452-05	5	\N
42245	BBAA000318	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.452-05	5	\N
42246	BAAA000291	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.468-05	5	\N
42247	BBAA000327	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.468-05	5	\N
42248	BAAA000295	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.468-05	5	\N
42249	BBAA000324	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.468-05	5	\N
42250	BAAA000299	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.483-05	5	\N
42251	BBAA000317	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.483-05	5	\N
42252	BAAA000300	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.483-05	5	\N
42253	BBAA000320	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.483-05	5	\N
42254	BAAA000272	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.483-05	5	\N
42255	BBAA000337	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.499-05	5	\N
42256	BAAA000275	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.499-05	5	\N
42257	BBAA000340	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.514-05	5	\N
42258	BAAA000279	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.514-05	5	\N
42259	BBAA000333	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.53-05	5	\N
42260	BAAA000283	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.53-05	5	\N
42261	BBAA000330	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.546-05	5	\N
42262	BAAA000274	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.546-05	5	\N
42263	BBAA000339	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.546-05	5	\N
42264	BAAA000278	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.546-05	5	\N
42265	BBAA000336	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.546-05	5	\N
42266	BAAA000282	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.546-05	5	\N
42267	BBAA000329	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.561-05	5	\N
42268	BAAA000277	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.561-05	5	\N
42269	BBAA000342	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.561-05	5	\N
42270	BAAA000281	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.561-05	5	\N
42271	BBAA000335	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.561-05	5	\N
42272	BAAA000285	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.577-05	5	\N
42273	BBAA000332	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.577-05	5	\N
42274	BAAA000276	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.577-05	5	\N
42275	BBAA000341	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.577-05	5	\N
42276	BAAA000280	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.577-05	5	\N
42277	BBAA000338	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.577-05	5	\N
42278	BAAA000284	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.593-05	5	\N
42279	BBAA000331	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.593-05	5	\N
42280	BAAA000286	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.593-05	5	\N
42281	BBAA000334	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.593-05	5	\N
42282	BAAA000218	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.608-05	5	\N
42283	BBAA000309	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.608-05	5	\N
42284	BAAA000219	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.608-05	5	\N
42285	BBAA000312	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.624-05	5	\N
42286	BBAA000305	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.624-05	5	\N
42287	BAAA000217	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.624-05	5	\N
42288	BAAA000220	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.639-05	5	\N
42289	BBAA000302	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.655-05	5	\N
42290	BAAA000224	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.655-05	5	\N
42291	BBAA000311	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.655-05	5	\N
42292	BAAA000222	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.655-05	5	\N
42293	BBAA000308	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.671-05	5	\N
42294	BAAA000221	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.671-05	5	\N
42295	BBAA000301	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.671-05	5	\N
42296	BAAA000225	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.671-05	5	\N
42297	BBAA000314	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.671-05	5	\N
42298	BAAA000223	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.671-05	5	\N
42299	BBAA000307	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.686-05	5	\N
42300	BAAA000226	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.686-05	5	\N
42301	BBAA000304	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.686-05	5	\N
42302	BAAA000230	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.686-05	5	\N
42303	BBAA000313	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.686-05	5	\N
42304	BAAA000228	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.702-05	5	\N
42305	BBAA000310	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.702-05	5	\N
42306	BAAA000227	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.702-05	5	\N
42307	BBAA000303	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.718-05	5	\N
42308	BAAA000229	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.718-05	5	\N
42309	BBAA000306	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.718-05	5	\N
42310	BAAA000202	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.718-05	5	\N
42311	BBAA000371	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.718-05	5	\N
42312	BAAA000201	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.733-05	5	\N
42313	BBAA000372	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:31:57.749-05	5	\N
42328	DAAA000658	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.03-05	5	\N
42329	DBAA000724	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.03-05	5	\N
42330	DBAA000718	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.03-05	5	\N
42331	DAAA000661	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.046-05	5	\N
42332	DBAA000727	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.046-05	5	\N
42333	DAAA000669	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.046-05	5	\N
42334	DBAA000721	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.061-05	5	\N
42335	DAAA000670	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.061-05	5	\N
42336	DBAA000728	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.061-05	5	\N
42337	DAAA000667	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.061-05	5	\N
42338	DBAA000725	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.061-05	5	\N
42315	DAAA000662	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 06:48:05.139-05	Skip	-1	LTAdmin	2013-09-24 15:32:36.999-05	5	
42316	DBAA000719	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 06:49:17.046-05	Skip	-1	LTAdmin	2013-09-24 15:32:36.999-05	5	
42318	DBAA000720	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 06:51:02.311-05	Skip	-1	LTAdmin	2013-09-24 15:32:36.999-05	5	
42319	DAAA000660	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:11:30.233-05	Skip	-1	LTAdmin	2013-09-24 15:32:36.999-05	5	
42320	DBAA000726	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:12:46.749-05	Skip	-1	LTAdmin	2013-09-24 15:32:36.999-05	5	
42321	DAAA000666	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:13:58.936-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.014-05	5	
42322	DBAA000723	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:15:11.093-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.014-05	5	
42323	DAAA000663	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:16:23.749-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.014-05	5	
42324	DBAA000729	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:17:35.702-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.014-05	5	
42325	DAAA000657	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:18:47.889-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.014-05	5	
42326	DAAA000664	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:21:12.593-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.014-05	5	
42327	DBAA000717	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:22:25.093-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.03-05	5	
42339	DAAA000668	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.061-05	5	\N
42340	DBAA000708	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.077-05	5	\N
42341	DAAA000665	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.077-05	5	\N
42342	DBAA000705	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.077-05	5	\N
42343	DAAA000648	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.077-05	5	\N
42344	DBAA000712	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.077-05	5	\N
42345	DAAA000645	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.077-05	5	\N
42346	DBAA000706	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.093-05	5	\N
42347	DAAA000652	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.093-05	5	\N
42348	DBAA000709	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.093-05	5	\N
42349	DAAA000655	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.093-05	5	\N
42350	DBAA000715	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.108-05	5	\N
42351	DAAA000646	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.108-05	5	\N
42352	DBAA000716	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.108-05	5	\N
42353	DAAA000649	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.108-05	5	\N
42354	DBAA000703	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.108-05	5	\N
42355	DAAA000656	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.108-05	5	\N
42356	DBAA000710	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.124-05	5	\N
42357	DAAA000653	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.124-05	5	\N
42377	DAAA000681	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.186-05	5	\N
42378	DBAA000798	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.186-05	5	\N
42379	DAAA000678	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.202-05	5	\N
42380	DBAA000791	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.202-05	5	\N
42381	DAAA000671	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.202-05	5	\N
42382	DBAA000788	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.202-05	5	\N
42383	DAAA000672	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.202-05	5	\N
42384	DBAA000758	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.202-05	5	\N
42385	DAAA000675	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.218-05	5	\N
42386	DBAA000795	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.218-05	5	\N
42387	DAAA000682	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.218-05	5	\N
42388	DBAA000752	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.218-05	5	\N
42389	DAAA000679	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.218-05	5	\N
42390	DBAA000748	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.218-05	5	\N
42391	DAAA000606	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.233-05	5	\N
42392	DBAA000746	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.233-05	5	\N
42393	DAAA000603	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.249-05	5	\N
42359	DAAA000650	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:24:50.061-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.124-05	5	
42360	DBAA000714	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:26:02.874-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.124-05	5	
42362	DBAA000707	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:28:26.843-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.139-05	5	
42363	DAAA000654	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:29:38.78-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.139-05	5	
42364	DBAA000704	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:30:50.764-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.155-05	5	
42365	DAAA000647	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:32:03.999-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.155-05	5	
42366	DBAA000711	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:32:57.593-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.155-05	5	
42367	DAAA000644	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:33:49.53-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.155-05	5	
42368	DBAA000792	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:49:28.843-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.155-05	5	
42369	DAAA000651	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:51:58.561-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.155-05	5	
42370	DBAA000789	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:53:10.436-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.171-05	5	
42371	DBAA000736	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:54:22.327-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.171-05	5	
42372	DBAA000797	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:04:18.608-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.171-05	5	
42373	DAAA000677	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:05:35.171-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.171-05	5	
42375	DAAA000674	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:07:41.046-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.171-05	5	
42376	DBAA000787	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:08:33.999-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.186-05	5	
42394	DBAA000745	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.264-05	5	\N
42395	DAAA000604	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.264-05	5	\N
42396	DBAA000751	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.264-05	5	\N
42397	DAAA000610	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.264-05	5	\N
42398	DBAA000747	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.264-05	5	\N
42399	DBAA000755	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.264-05	5	\N
42400	DAAA000613	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.28-05	5	\N
42401	DBAA000753	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.28-05	5	\N
42402	DAAA000607	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.28-05	5	\N
42403	DBAA000754	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.28-05	5	\N
42404	DAAA000614	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.28-05	5	\N
42405	DBAA000750	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.296-05	5	\N
42406	DAAA000611	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.296-05	5	\N
42437	DAAA000633	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.389-05	5	\N
42438	DBAA000739	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.405-05	5	\N
42439	DAAA000641	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.405-05	5	\N
42440	DBAA000767	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.405-05	5	\N
42441	DAAA000642	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.405-05	5	\N
42442	DBAA000770	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.405-05	5	\N
42443	DAAA000639	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.405-05	5	\N
42444	DBAA000769	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.421-05	5	\N
42445	DAAA000640	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.421-05	5	\N
42446	DBAA000772	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.421-05	5	\N
42447	DAAA000637	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.421-05	5	\N
42448	DBAA000771	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.421-05	5	\N
42408	DAAA000608	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:28:42.436-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.296-05	5	
42409	DAAA000601	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:29:54.811-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.311-05	5	
42411	DAAA000602	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:32:19.686-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.311-05	5	
42412	DBAA000733	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:33:32.233-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.311-05	5	
42413	DAAA000605	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:34:44.655-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.311-05	5	
42414	DBAA000734	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:35:57.155-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.311-05	5	
42415	DAAA000612	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:37:09.874-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.327-05	5	
42416	DBAA000731	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:38:22.296-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.327-05	5	
42417	DAAA000609	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:39:35.718-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.327-05	5	
42418	DBAA000732	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:40:47.124-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.327-05	5	
42419	DAAA000634	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:41:59.03-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.327-05	5	
42420	DBAA000740	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:43:11.936-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.327-05	5	
42421	DAAA000631	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:44:24.577-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.343-05	5	
42422	DBAA000737	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:45:36.639-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.343-05	5	
42424	DBAA000738	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:48:01.999-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.358-05	5	
42425	DAAA000638	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:48:55.343-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.358-05	5	
42426	DBAA000735	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:49:47.671-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.358-05	5	
42427	DAAA000635	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:05:26.093-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.358-05	5	
42428	DBAA000701	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:06:42.436-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.374-05	5	
42429	DAAA000699	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:07:54.561-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.374-05	5	
42430	DBAA000743	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:09:07.061-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.374-05	5	
42431	DAAA000629	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:10:19.264-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.374-05	5	
42432	DBAA000744	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:11:31.749-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.374-05	5	
42433	DAAA000636	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:12:43.999-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.374-05	5	
42434	DBAA000741	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:13:56.999-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.389-05	5	
42435	DAAA000630	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:15:09.405-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.389-05	5	
42449	DAAA000620	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.436-05	5	\N
42450	DBAA000763	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.436-05	5	\N
42451	DBAA000766	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.436-05	5	\N
42452	DBAA000760	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.436-05	5	\N
42453	DAAA000617	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.436-05	5	\N
42454	DBAA000759	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.436-05	5	\N
42455	DAAA000624	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.452-05	5	\N
42456	DBAA000765	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.452-05	5	\N
42457	DBAA000768	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.452-05	5	\N
42458	DAAA000621	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.452-05	5	\N
42459	DBAA000762	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.468-05	5	\N
42460	DAAA000627	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.468-05	5	\N
42461	DBAA000761	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.468-05	5	\N
42462	DAAA000628	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.468-05	5	\N
42463	DBAA000764	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.468-05	5	\N
42464	DAAA000615	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.483-05	5	\N
42465	DBAA000781	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.483-05	5	\N
42476	DAAA000623	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.514-05	5	\N
42477	DBAA000786	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.514-05	5	\N
42478	DAAA000690	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.514-05	5	\N
42479	DBAA000773	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.514-05	5	\N
42480	DAAA000687	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.53-05	5	\N
42481	DBAA000779	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.53-05	5	\N
42482	DAAA000694	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.53-05	5	\N
42483	DBAA000776	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.53-05	5	\N
42484	DAAA000697	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.53-05	5	\N
42485	DBAA000775	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.53-05	5	\N
42486	DAAA000688	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.546-05	5	\N
42487	DBAA000782	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.546-05	5	\N
42488	DAAA000691	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.546-05	5	\N
42489	DBAA000785	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.546-05	5	\N
42490	DAAA000685	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.561-05	5	\N
42491	DBAA000778	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.561-05	5	\N
42492	DAAA000698	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.561-05	5	\N
42493	DAAA000692	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.561-05	5	\N
42494	DAAA000695	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.561-05	5	\N
42495	DAAA000696	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.577-05	5	\N
42496	DAAA000689	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.593-05	5	\N
42497	DAAA000686	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.593-05	5	\N
42498	DAAA000693	2013-09-25 00:00:00-05	0	24	40	\N	Absolute		f	Waiting	0	1999-01-01 00:00:00-06	Skip	-1	LTAdmin	2013-09-24 15:32:37.593-05	5	\N
42058	CBAA000558	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:33:37.561-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.139-05	5	
42061	CAAA000432	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:37:18.936-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.139-05	5	
41984	CBAA000535	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 15:40:56.186-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.921-05	5	
41997	CAAA000473	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 16:02:22.311-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.952-05	5	
42467	DBAA000784	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:18:46.218-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.483-05	5	
42468	DAAA000625	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:19:58.155-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.483-05	5	
42469	DBAA000777	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:21:10.514-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.499-05	5	
42470	DAAA000616	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:22:23.374-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.499-05	5	
42471	DBAA000783	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:23:35.749-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.499-05	5	
42472	DAAA000619	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:24:48.311-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.499-05	5	
42473	DBAA000774	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:26:00.28-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.499-05	5	
42474	DAAA000626	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:26:53.843-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.499-05	5	
42475	DBAA000780	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:27:45.468-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.514-05	5	
42054	CBAA000564	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 16:52:44.421-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.124-05	5	
42093	CBAA000587	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 17:26:05.358-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.264-05	5	
42104	GAAA000002	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 18:50:46.139-05	Skip	-1	LTAdmin	2013-09-24 11:01:31.124-05	5	
41960	CBAA000536	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 21:50:59.421-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.811-05	5	
41980	CBAA000516	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:13:33.233-05	Skip	-1	LTAdmin	2013-09-24 11:00:06.905-05	5	
42027	CAAA000451	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 22:36:38.968-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.046-05	5	
42040	CBAA000560	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-24 23:29:29.249-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.093-05	5	
42081	CAAA000405	2013-09-24 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Waiting	0	2013-09-25 00:08:38.343-05	Skip	-1	LTAdmin	2013-09-24 11:00:07.218-05	5	
42314	DBAA000722	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 06:45:43.343-05	Skip	-1	LTAdmin	2013-09-24 15:32:36.983-05	5	
42317	DAAA000659	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 06:50:10.093-05	Skip	-1	LTAdmin	2013-09-24 15:32:36.999-05	5	
42358	DBAA000713	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:23:37.561-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.124-05	5	
42361	DAAA000643	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 07:27:14.343-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.124-05	5	
42374	DBAA000794	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:06:47.639-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.171-05	5	
42407	DBAA000757	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:27:25.796-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.296-05	5	
42410	DBAA000756	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:31:07.296-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.311-05	5	
42423	DAAA000632	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 08:46:48.983-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.343-05	5	
42436	DBAA000742	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:16:21.389-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.389-05	5	
42466	DAAA000622	2013-09-25 00:00:00-05	0	24	40	Watering A Pump 1	Absolute		t	Watered	40	2013-09-25 09:17:33.858-05	Skip	-1	LTAdmin	2013-09-24 15:32:37.483-05	5	
\.


--
-- TOC entry 1870 (class 0 OID 0)
-- Dependencies: 143
-- Name: watering_id_seq; Type: SEQUENCE SET; Schema: public; Owner: LTSysAdmin
--

SELECT pg_catalog.setval('watering_id_seq', 42498, true);


--
-- TOC entry 1826 (class 2606 OID 18232)
-- Name: conveyortasks_pkey; Type: CONSTRAINT; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

ALTER TABLE ONLY conveyortasks
    ADD CONSTRAINT conveyortasks_pkey PRIMARY KEY (id);


--
-- TOC entry 1816 (class 2606 OID 16426)
-- Name: image_file_table_pkey; Type: CONSTRAINT; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

ALTER TABLE ONLY image_file_table
    ADD CONSTRAINT image_file_table_pkey PRIMARY KEY (id);


--
-- TOC entry 1824 (class 2606 OID 18221)
-- Name: lemnateclock_pkey; Type: CONSTRAINT; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

ALTER TABLE ONLY lemnateclock
    ADD CONSTRAINT lemnateclock_pkey PRIMARY KEY (id);


--
-- TOC entry 1808 (class 2606 OID 16401)
-- Name: ltdbs_name_unique; Type: CONSTRAINT; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

ALTER TABLE ONLY ltdbs
    ADD CONSTRAINT ltdbs_name_unique UNIQUE (name);


--
-- TOC entry 1810 (class 2606 OID 16399)
-- Name: ltdbs_pkey; Type: CONSTRAINT; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

ALTER TABLE ONLY ltdbs
    ADD CONSTRAINT ltdbs_pkey PRIMARY KEY (id);


--
-- TOC entry 1812 (class 2606 OID 16414)
-- Name: ltuser_name_unique; Type: CONSTRAINT; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

ALTER TABLE ONLY ltuser
    ADD CONSTRAINT ltuser_name_unique UNIQUE (name);


--
-- TOC entry 1814 (class 2606 OID 16412)
-- Name: ltuser_pkey; Type: CONSTRAINT; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

ALTER TABLE ONLY ltuser
    ADD CONSTRAINT ltuser_pkey PRIMARY KEY (id);


--
-- TOC entry 1820 (class 2606 OID 18076)
-- Name: plants_pkey; Type: CONSTRAINT; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

ALTER TABLE ONLY plants
    ADD CONSTRAINT plants_pkey PRIMARY KEY (id);


--
-- TOC entry 1822 (class 2606 OID 18085)
-- Name: pumps_pkey; Type: CONSTRAINT; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

ALTER TABLE ONLY pumps
    ADD CONSTRAINT pumps_pkey PRIMARY KEY (id);


--
-- TOC entry 1818 (class 2606 OID 17871)
-- Name: serviceinterval_pkey; Type: CONSTRAINT; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

ALTER TABLE ONLY serviceinterval
    ADD CONSTRAINT serviceinterval_pkey PRIMARY KEY (id);


--
-- TOC entry 1830 (class 2606 OID 18254)
-- Name: serviceresets_pkey; Type: CONSTRAINT; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

ALTER TABLE ONLY serviceresets
    ADD CONSTRAINT serviceresets_pkey PRIMARY KEY (id);


--
-- TOC entry 1828 (class 2606 OID 18243)
-- Name: watering_pkey; Type: CONSTRAINT; Schema: public; Owner: LTSysAdmin; Tablespace: 
--

ALTER TABLE ONLY watering
    ADD CONSTRAINT watering_pkey PRIMARY KEY (id);


--
-- TOC entry 1857 (class 0 OID 0)
-- Dependencies: 3
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2013-09-25 09:32:34 CDT

--
-- PostgreSQL database dump complete
--

