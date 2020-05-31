DROP TABLE SALE CASCADE CONSTRAINTS;

DROP TABLE SALE_STATUS CASCADE CONSTRAINTS;

DROP TABLE SALE_PROVISION CASCADE CONSTRAINTS;


CREATE TABLE SALE(
    sale_id         VARCHAR(32)  NOT NULL PRIMARY KEY,
    seller_id       VARCHAR(50)	 NOT NULL,
    cashier_id      VARCHAR(50),
    appuser_id      VARCHAR(50),
    sale_status_id  VARCHAR(50)	 NOT NULL,
    date_order      DATE,
    sale_date       DATE,
    code            VARCHAR(15) NOT NULL,
    payment_method  VARCHAR(30) NOT NULL,
    subtotal        NUMERIC(12) NOT NULL,
    total           NUMERIC(12) NOT NULL,
    updated_at		DATE		NOT NULL,
    created_at		DATE		NOT NULL,
    deleted			NUMERIC(1)	NOT NULL
);

CREATE TABLE SALE_STATUS(
    sale_status_id 	    VARCHAR(32)		        NOT NULL PRIMARY KEY,
    name				VARCHAR(50)		        NOT NULL,
    updated_at		    DATE					NOT NULL,
    created_at			DATE					NOT NULL,
    deleted				NUMERIC(1)			    NOT NULL
);

CREATE TABLE SALE_PROVISION(
    provision_id    VARCHAR(32)  NOT NULL PRIMARY KEY,
    sale_id         VARCHAR(32)  NOT NULL,
    product_id      VARCHAR(32),
    serv_id         VARCHAR(32),
    quantity        NUMERIC(3)   NOT NULL,
    unit_price      NUMERIC(12)  NOT NULL,
    total           NUMERIC(12)  NOT NULL,
    updated_at		DATE		 NOT NULL,
    created_at		DATE		 NOT NULL,
    deleted			NUMERIC(1)	 NOT NULL
);  


--CONSTRAINT
ALTER TABLE SALE ADD CONSTRAINT sale_status_id_fk FOREIGN KEY(sale_status_id) REFERENCES SALE_STATUS(sale_status_id);
ALTER TABLE SALE ADD CONSTRAINT appuser_fk FOREIGN KEY(appuser_id) REFERENCES APP_USER(appuser_id);
ALTER TABLE SALE ADD CONSTRAINT seller_id_fk FOREIGN KEY(seller_id) REFERENCES APP_USER(appuser_id);
ALTER TABLE SALE ADD CONSTRAINT cashier_id_fk FOREIGN KEY(cashier_id) REFERENCES APP_USER(appuser_id);

ALTER TABLE SALE_PROVISION ADD CONSTRAINT serv_id_fk FOREIGN KEY(serv_id) REFERENCES SERVICE(serv_id);

ALTER TABLE SALE_PROVISION ADD CONSTRAINT product_id_fk FOREIGN KEY(product_id) REFERENCES PRODUCT(product_id);
ALTER TABLE SALE_PROVISION ADD CONSTRAINT sale_id_fk FOREIGN KEY(sale_id) REFERENCES SALE(sale_id);

insert into SALE_STATUS values('PEN','Pendiente',   to_date ('15-05-2020','DD-MM-YYYY '),to_date ('15-05-2020','DD-MM-YYYY'),0);
insert into SALE_STATUS values('PAG','Pagado',      to_date ('15-05-2020','DD-MM-YYYY '),to_date ('15-05-2020','DD-MM-YYYY'),0);
insert into SALE_STATUS values('CAN','Cancelado',   to_date ('15-05-2020','DD-MM-YYYY '),to_date ('15-05-2020','DD-MM-YYYY'),0);

  
--venta 1
INSERT INTO SALE VALUES
    ('9DB8539940214DE2BDE44462E670D7DB',--sale_id
    '3E309C13E3314519A9270C336AFB75C2',--seller_id
    '5093234C73AE4954BB6DD7E841FEEE12',--cashier_id
    '70BA44E0EC9A42018FA0D9465CC695B5',--user_id
    'PEN',--sale_status_id
    to_date('15-05-2020 11:00:00','dd-mm-yyyy hh24:mi:ss'),--date_order
    to_date('15-05-2020 11:00:00','dd-mm-yyyy hh24:mi:ss'),--sale_date
    '523698556740210',--code
    'Tarjeta Debito',--payment_method
    30000,--subtotal
    51000,--total
    to_date ('15-05-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0--deleted
);

--VENTA 1
INSERT INTO SALE_PROVISION VALUES(
    SYS_GUID(),--provision_id
    '9DB8539940214DE2BDE44462E670D7DB',--sale_id
    'D9CCD278B8B849788F1F4FF6E074B499',--product_id
    '',--serv_id
    1,--quantity
    15000,--unit_price
    15000,--total
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0--deleted
);

INSERT INTO SALE_PROVISION VALUES(
    SYS_GUID(),--provision_id
    '9DB8539940214DE2BDE44462E670D7DB',--sale_id
    'C4D20AB8F1114202A98156BFF9C6521E',--product_id
    '',--serv_id
    4,--quantity
    9000,--unit_price
    36000,--total
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0--deleted
);




--venta 2
INSERT INTO SALE VALUES
    ('176FD7BB0CDF4F2F910FEF5EE4ED4F59',--sale_id
    '3E309C13E3314519A9270C336AFB75C2',--seller_id
    '5093234C73AE4954BB6DD7E841FEEE12',--cashier_id
    '70BA44E0EC9A42018FA0D9465CC695B5',--user_id
    'PAG',--sale_status_id
    to_date('15-05-2020 12:05:00','dd-mm-yyyy hh24:mi:ss'),--date_order
    to_date('15-05-2020 12:05:00','dd-mm-yyyy hh24:mi:ss'),--sale_date
    '523698556740365',--code
    'Tarjeta Credito',--payment_method
    170000,--subtotal
    188000,--total
    to_date ('15-05-2020 12:05 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 12:05 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0--deleted
);

--VENTA 2
INSERT INTO SALE_PROVISION VALUES(
    SYS_GUID(),--provision_id
    '176FD7BB0CDF4F2F910FEF5EE4ED4F59',--sale_id
    'BB5801A8406F49CC953A277F918E770D',--product_id
    '',--serv_id
    4,--quantity
    35000,--unit_price
    140000,--total
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0--deleted
);

INSERT INTO SALE_PROVISION VALUES(
    SYS_GUID(),--provision_id
    '176FD7BB0CDF4F2F910FEF5EE4ED4F59',--sale_id
    'D9CCD278B8B849788F1F4FF6E074B499',--product_id
    '',--serv_id
    2,--quantity
    15000,--unit_price
    30000,--total
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0--deleted
);

INSERT INTO SALE_PROVISION VALUES(
    SYS_GUID(),--provision_id
    '176FD7BB0CDF4F2F910FEF5EE4ED4F59',--sale_id
    'C4D20AB8F1114202A98156BFF9C6521E',--product_id
    '',--serv_id
    2,--quantity
    9000,--unit_price
    18000,--total
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0--deleted
);




--venta 3
INSERT INTO SALE VALUES
    ('0B9104C3B9FE48C5B61B436C50042471',--sale_id
    '3E309C13E3314519A9270C336AFB75C2',--seller_id
    '5093234C73AE4954BB6DD7E841FEEE12',--cashier_id
    '70BA44E0EC9A42018FA0D9465CC695B5',--user_id
    'CAN',--sale_status_id
    to_date('15-05-2020 12:00:00','dd-mm-yyyy hh24:mi:ss'),--date_order
    to_date('15-05-2020 12:00:00','dd-mm-yyyy hh24:mi:ss'),--sale_date
    '523698556744741',--code
    'Tarjeta Credito',--payment_method
    124000,--subtotal
    124000,--total
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0--deleted
);

INSERT INTO SALE_PROVISION VALUES(
    SYS_GUID(),--provision_id
    '0B9104C3B9FE48C5B61B436C50042471',--sale_id
    'BB5801A8406F49CC953A277F918E770D',--product_id
    '',--serv_id
    2,--quantity
    35000,--unit_price
    70000,--total
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    1--deleted
);

INSERT INTO SALE_PROVISION VALUES(
    SYS_GUID(),--provision_id
    '0B9104C3B9FE48C5B61B436C50042471',--sale_id
    'C4D20AB8F1114202A98156BFF9C6521E',--product_id
    '',--serv_id
    3,--quantity
    9000,--unit_price
    27000,--total
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    1--deleted
);



--venta 4
INSERT INTO SALE VALUES
    ('1A7F7099D06E4C77AC91B6DD869E99CE',--sale_id
    '3E309C13E3314519A9270C336AFB75C2',--seller_id
    '5093234C73AE4954BB6DD7E841FEEE12',--cashier_id
    '70BA44E0EC9A42018FA0D9465CC695B5',--user_id
    'PEN',--sale_status_id
    to_date('15-05-2020 12:10:00','dd-mm-yyyy hh24:mi:ss'),--date_order
    to_date('15-05-2020 12:10:00','dd-mm-yyyy hh24:mi:ss'),--sale_date
    '523698556758787',--code
    'Tarjeta Credito',--payment_method
    162000,--subtotal
    162000,--total
    to_date ('15-05-2020 12:10 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 12:10 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0--deleted
);

--venta 4
INSERT INTO SALE_PROVISION VALUES(
    SYS_GUID(),--provision_id
    '1A7F7099D06E4C77AC91B6DD869E99CE',--sale_id
    'C4D20AB8F1114202A98156BFF9C6521E',--product_id
    '',--serv_id
    3,--quantity
    9000,--unit_price
    27000,--total
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0--deleted
);

INSERT INTO SALE_PROVISION VALUES(
    SYS_GUID(),--provision_id
    '1A7F7099D06E4C77AC91B6DD869E99CE',--sale_id
    'D9CCD278B8B849788F1F4FF6E074B499',--product_id
    '',--serv_id
    1,--quantity
    15000,--unit_price
    15000,--total
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0--deleted
);

INSERT INTO SALE_PROVISION VALUES(
    SYS_GUID(),--provision_id
    '1A7F7099D06E4C77AC91B6DD869E99CE',--sale_id
    '6CFE0D33D63A40ECB90BBBCA50E6E6EB',--product_id
    '',--serv_id
    2,--quantity
    25000,--unit_price
    50000,--total
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0--deleted
);

INSERT INTO SALE_PROVISION VALUES(
    SYS_GUID(),--provision_id
    '1A7F7099D06E4C77AC91B6DD869E99CE',--sale_id
    'BB5801A8406F49CC953A277F918E770D',--product_id
    '',--serv_id
    2,--quantity
    35000,--unit_price
    70000,--total
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0--deleted
);




--venta 5
INSERT INTO SALE VALUES
    ('842B5124650A44F9AD69396C0F920810',--sale_id
    '3E309C13E3314519A9270C336AFB75C2',--seller_id
    '5093234C73AE4954BB6DD7E841FEEE12',--cashier_id
    '70BA44E0EC9A42018FA0D9465CC695B5',--user_id
    'PEN',--sale_status_id
    to_date('15-05-2020 12:25:00','dd-mm-yyyy hh24:mi:ss'),--date_order
    to_date('15-05-2020 12:25:00','dd-mm-yyyy hh24:mi:ss'),--sale_date
    '523698556758787',--code
    'Tarjeta Credito',--payment_method
    28000,--subtotal
    28000,--total
    to_date ('15-05-2020 12:25 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 12:25 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0--deleted
);

--venta 5
INSERT INTO SALE_PROVISION VALUES(
    SYS_GUID(),--provision_id
    '842B5124650A44F9AD69396C0F920810',--sale_id
    '',--product_id
    'A7515A96926141479DEC462D7CC647F0',--serv_id
    1,--quantity
    28000,--unit_price
    28000,--total
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0--deleted
);




--venta 6
INSERT INTO SALE VALUES
    ('D6847A1017A84231A453B82EBEFBD432',--sale_id
    '3E309C13E3314519A9270C336AFB75C2',--seller_id
    '5093234C73AE4954BB6DD7E841FEEE12',--cashier_id
    '70BA44E0EC9A42018FA0D9465CC695B5',--user_id
    'PAG',--sale_status_id
    to_date('15-05-2020 12:25:00','dd-mm-yyyy hh24:mi:ss'),--date_order
    to_date('15-05-2020 12:25:00','dd-mm-yyyy hh24:mi:ss'),--sale_date
    '523698556758787',--code
    'Tarjeta Credito',--payment_method
    40000,--subtotal
    40000,--total
    to_date ('15-05-2020 12:30 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 12:30 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0--deleted
);

--venta 6
INSERT INTO SALE_PROVISION VALUES(
    SYS_GUID(),--provision_id
    'D6847A1017A84231A453B82EBEFBD432',--sale_id
    '',--product_id
    '50D00011F4994D4FBB4FBDA04D2AFB97',--serv_id
    1,--quantity
    40000,--unit_price
    40000,--total
    to_date ('15-05-2020 12:30 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('15-05-2020 12:30 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0--deleted
);


commit;