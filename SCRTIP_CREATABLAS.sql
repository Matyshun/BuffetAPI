
-------------------------------- DROP ORIGINAL TABLES --------------------------------

DROP TABLE APP_USER CASCADE CONSTRAINTS;

DROP TABLE USER_TYPE CASCADE CONSTRAINTS;

DROP TABLE USER_STATUS CASCADE CONSTRAINTS;

DROP TABLE VERIFICATIONTOKEN;

DROP TABLE PUBLICATION CASCADE CONSTRAINTS;

DROP TABLE PUBLIC_STATUS;

DROP TABLE SERVICE CASCADE CONSTRAINTS;

DROP TABLE SERVICE_STATUS;

DROP TABLE PRODUCT CASCADE CONSTRAINTS;

DROP TABLE PRODUCT_STATUS;

DROP TABLE UNIT;

DROP TABLE BOOKING CASCADE CONSTRAINTS;  

DROP TABLE STATUS_BOOKING;

DROP TABLE BOOKING_RESTRICTION CASCADE CONSTRAINTS;  

DROP TABLE STORE_SCHEDULE;

DROP TABLE SALE CASCADE CONSTRAINTS;

DROP TABLE SALE_STATUS CASCADE CONSTRAINTS;

DROP TABLE SALE_PROVISION CASCADE CONSTRAINTS;


-------------------------------- CREATE TABLES --------------------------------

-------------------------------------------------------------------------
-------------------------------- USERS -------------------------------- 
-------------------------------------------------------------------------

CREATE TABLE USER_TYPE(
	user_type_id 	    VARCHAR(32)		        NOT NULL PRIMARY KEY,
	name				VARCHAR(50)		        NOT NULL UNIQUE,
	updated_at		    DATE					NOT NULL,
	deleted				NUMERIC(1)			    NOT NULL,
	created_at			DATE					NOT NULL
);

CREATE TABLE USER_STATUS(
    status_id   		VARCHAR(32)		        NOT NULL PRIMARY KEY,
	status      		VARCHAR(32)		    NOT NULL,
	
	updated_at		    DATE					    NOT NULL,
	deleted				NUMERIC(1)			    NOT NULL,
	created_at			DATE					NOT NULL
	
);

CREATE TABLE APP_USER (
	appuser_id		    VARCHAR(32)			    NOT NULL PRIMARY KEY,
	username			VARCHAR(50) 			NOT NULL UNIQUE,
	hash				VARCHAR(255)			NOT NULL,
	email				VARCHAR(50)			    NOT NULL UNIQUE,
	name				VARCHAR(100)			NOT NULL,
	last_names	        VARCHAR(100)			NOT NULL,
    rut                 VARCHAR(13)			    NOT NULL,
	adress				VARCHAR(100),
	phone				VARCHAR(100),
	birthday			DATE,
    lastLogin			DATE					NOT NULL,
    mail_confirmed	    NUMERIC(1)	        	    NOT NULL,
	user_type_id    	VARCHAR(32)	    	    NOT NULL,
	status_id       	VARCHAR(32)	    	    NOT NULL,
	updated_at		    DATE						NOT NULL,
	deleted				NUMERIC(1)				NOT NULL,
	created_at			DATE					NOT NULL
);

ALTER TABLE APP_USER ADD CONSTRAINT user_type_fk FOREIGN KEY(user_type_id) REFERENCES USER_TYPE(user_type_id);
ALTER TABLE APP_USER ADD CONSTRAINT user_status_fk FOREIGN KEY(status_id) REFERENCES USER_STATUS(status_id);

CREATE TABLE VERIFICATIONTOKEN (
	token				VARCHAR(32)			NOT NULL PRIMARY KEY,
	appuser_id		    VARCHAR(50) 		NOT NULL,
	expiry_date		    DATE				NOT NULL
);

ALTER TABLE VERIFICATIONTOKEN ADD CONSTRAINT VARIF_USER_FK FOREIGN KEY(appuser_id) REFERENCES APP_USER(appuser_id);


------------------------------------------------------------------------------------------------------
-------------------------------- PUBLICACIONES DE MECÁNICO -------------------------------- 
------------------------------------------------------------------------------------------------------

CREATE TABLE PUBLICATION(
    public_id		        VARCHAR(32)		NOT NULL PRIMARY KEY,
    appuser_id              VARCHAR(32)		NOT NULL,
    user_type_id            VARCHAR(32)		NOT NULL,
    public_status_id        VARCHAR(32)		NOT NULL,
    created_at               DATE,
    updated_at               DATE,
    deleted                 numeric(1),
	
    title                   VARCHAR(50)		NOT NULL,
    public_desc             VARCHAR2(500)	NOT NULL,
    schedule                VARCHAR(50)            NOT NULL,
    services                VARCHAR2(500)	NOT NULL,
    bussiness_name          VARCHAR(50),
    address                  VARCHAR(100)	NOT NULL,
    comuna                  VARCHAR(100)	NOT NULL,
    region                  VARCHAR(100)	NOT NULL,
    landline                VARCHAR(32),
    mobile_number           VARCHAR(32),
    email                   VARCHAR(40)		NOT NULL,
	
    views                   NUMERIC(10)     NOT NULL
);


CREATE TABLE PUBLIC_STATUS(
    public_status_id		VARCHAR(32)		NOT NULL PRIMARY KEY,
    status_name             VARCHAR(32)		NOT NULL,
    created_at               DATE,
    updated_at               DATE,
    deleted                 NUMERIC(1)
);

--CONSTRAINT
ALTER TABLE PUBLICATION ADD CONSTRAINT appuser_id_fk FOREIGN KEY(appuser_id) REFERENCES APP_USER(appuser_id);
ALTER TABLE PUBLICATION ADD CONSTRAINT user_type_id_fk FOREIGN KEY(user_type_id) REFERENCES USER_TYPE(user_type_id);

--VERIFICACION DE USER_TYPE MECANICO
alter table PUBLICATION
add constraint mechanic_verify
check (user_type_id='MEC');


-------------------------------------------------------------------------------
-------------------------------- SERVICIOS -------------------------------- 
-------------------------------------------------------------------------------


CREATE TABLE SERVICE (
	serv_id	            VARCHAR(32)		NOT NULL PRIMARY KEY,
	name                VARCHAR(120)     NOT NULL,
	price               NUMERIC(15)     NOT NULL,
	serv_desc           VARCHAR2(400)   NOT NULL,
	estimated_time      NUMERIC(4)     NOT NULL,
	serv_status         VARCHAR(32)		NOT NULL,
	created_at           DATE,
	updated_at           DATE,
	deleted             NUMERIC(1) NOT NULL
);

CREATE TABLE SERVICE_STATUS (
	status_id	        VARCHAR(32)		NOT NULL PRIMARY KEY,
	status              VARCHAR(64)     NOT NULL,
	created_at           DATE,
	updated_at           DATE,
	deleted             numeric(1) NOT NULL
);

ALTER TABLE SERVICE ADD CONSTRAINT serv_status_fk FOREIGN KEY(serv_status) REFERENCES SERVICE_STATUS(status_id);



--------------------------------------------------------------------------------
-------------------------------- PRODUCTOS -------------------------------- 
--------------------------------------------------------------------------------


CREATE TABLE PRODUCT(
	product_id	        VARCHAR(32)		NOT NULL PRIMARY KEY,
	name                VARCHAR(120)     NOT NULL,
	product_desc        VARCHAR2(300)    NOT NULL,
	price               NUMERIC(15)     NOT NULL,
	stock               NUMERIC(12)     NOT NULL,
	stock_alert         NUMERIC(12)     NOT NULL,
	brand               VARCHAR(120)    NOT NULL,
	unit_id             VARCHAR(12)     NOT NULL,
	product_status      VARCHAR(32)		NOT NULL,
	created_at           DATE,
	updated_at           DATE,
	deleted             numeric(1) NOT NULL
);

CREATE TABLE PRODUCT_STATUS (
	status_id	        VARCHAR(32)		NOT NULL PRIMARY KEY,
	status              VARCHAR(64)     NOT NULL,
	created_at           DATE,
	updated_at           DATE,
	deleted             numeric(1) NOT NULL
);

CREATE TABLE UNIT(
	abbreviation        VARCHAR(32)		NOT NULL PRIMARY KEY,
	name                VARCHAR(32)		NOT NULL,
	plural_name         VARCHAR(32)		NOT NULL,
	created_at           DATE,
	updated_at           DATE,
	deleted             numeric(1) NOT NULL
);

ALTER TABLE PRODUCT ADD CONSTRAINT product_status_fk FOREIGN KEY(product_status) REFERENCES PRODUCT_STATUS(status_id);
ALTER TABLE PRODUCT ADD CONSTRAINT unit_id_fk FOREIGN KEY(unit_id) REFERENCES UNIT(abbreviation);



-------------------------------------------------------------------------------------
-------------------------------- RESERVACIONES -------------------------------- 
-------------------------------------------------------------------------------------


CREATE TABLE BOOKING(
    booking_id 	        VARCHAR(32)		        NOT NULL PRIMARY KEY,
    serv_id 	        VARCHAR(32)		        NOT NULL,
    appuser_id			VARCHAR(50)		        NOT NULL,
    start_date_hour     DATE                    NOT NULL,
    end_date_hour       DATE                    NOT NULL,
    status_booking_id	VARCHAR(32)             NOT NULL,
	
    updated_at		    DATE					NOT NULL,
    created_at			DATE					NOT NULL,
    deleted				NUMERIC(1)			    NOT NULL
);

CREATE TABLE STATUS_BOOKING (
    status_booking_id	VARCHAR(32)		        NOT NULL PRIMARY KEY,
    name				VARCHAR(50)		        NOT NULL,
    updated_at		    DATE					NOT NULL,
    created_at			DATE					NOT NULL,
    deleted				NUMERIC(1)			    NOT NULL
);


-- CONSTRAINT
ALTER TABLE BOOKING ADD CONSTRAINT BOOKING_appuser_id_fk FOREIGN KEY(appuser_id) REFERENCES APP_USER(appuser_id);
ALTER TABLE BOOKING ADD CONSTRAINT status_booking_id_fk FOREIGN KEY(status_booking_id) REFERENCES STATUS_BOOKING(status_booking_id);
ALTER TABLE BOOKING ADD CONSTRAINT booking_serv_id_fk FOREIGN KEY(serv_id) REFERENCES SERVICE(serv_id);


CREATE TABLE BOOKING_RESTRICTION(
    restriction_id 	    VARCHAR(32)		        NOT NULL PRIMARY KEY,
    serv_id 	        VARCHAR(32)		        NOT NULL,
    start_date_hour     DATE                    NOT NULL,
    end_date_hour       DATE                    NOT NULL,
    updated_at		    DATE					NOT NULL,
    created_at			DATE					NOT NULL,
    deleted				NUMERIC(1)			    NOT NULL
);

CREATE TABLE STORE_SCHEDULE(
    schedule_id         VARCHAR(32)		        NOT NULL PRIMARY KEY,
    start_hour          DATE                NOT NULL,
    start_lunch_hour    DATE                NOT NULL,
    end_lunch_hour      DATE                NOT NULL,
    end_hour            DATE                NOT NULL,
    updated_at		    DATE					NOT NULL,
    created_at			DATE					NOT NULL,
    deleted				NUMERIC(1)			    NOT NULL
);

-- CONSTRAINT
ALTER TABLE BOOKING_RESTRICTION ADD CONSTRAINT restriction_serv_id_fk FOREIGN KEY(serv_id) REFERENCES SERVICE(serv_id);



------------------------------------------------------------------------------------------
-------------------------------- SISTEMA DE VENTAS -------------------------------- 
------------------------------------------------------------------------------------------

CREATE TABLE SALE(
    sale_id         VARCHAR(32)  NOT NULL PRIMARY KEY,
    seller_id       VARCHAR(50)	 NOT NULL,
    cashier_id      VARCHAR(50),
    appuser_id      VARCHAR(50),
    sale_status_id  VARCHAR(50)	 NOT NULL,
    date_order      DATE,
    sale_date       DATE,
    code            VARCHAR(15) NOT NULL,
    payment_method  VARCHAR(30) ,
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
