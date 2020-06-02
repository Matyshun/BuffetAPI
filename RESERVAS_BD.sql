  
DROP TABLE RESERVE CASCADE CONSTRAINTS;  

DROP TABLE STATUS_RESERVE;

DROP TABLE RESERVE_RESTRICTION CASCADE CONSTRAINTS;  

DROP TABLE STORE_SCHEDULE;

CREATE TABLE RESERVE(
    reserve_id 	        VARCHAR(32)		        NOT NULL PRIMARY KEY,
    serv_id 	        VARCHAR(32)		        NOT NULL,
    appuser_id			VARCHAR(50)		        NOT NULL,
    start_date_hour     DATE                    NOT NULL,
    end_date_hour       DATE                    NOT NULL,
    status_reserve_id	VARCHAR(32)             NOT NULL,
    updated_at		    DATE					NOT NULL,
    created_at			DATE					NOT NULL,
    deleted				NUMERIC(1)			    NOT NULL
);

CREATE TABLE STATUS_RESERVE (
    status_reserve_id	VARCHAR(32)		        NOT NULL PRIMARY KEY,
    name				VARCHAR(50)		        NOT NULL,
    updated_at		    DATE					NOT NULL,
    created_at			DATE					NOT NULL,
    deleted				NUMERIC(1)			    NOT NULL
);


--CONSTRAINT
ALTER TABLE RESERVE ADD CONSTRAINT reserve_appuser_id_fk FOREIGN KEY(appuser_id) REFERENCES APP_USER(appuser_id);
ALTER TABLE RESERVE ADD CONSTRAINT status_reserv_id_fk FOREIGN KEY(status_reserve_id) REFERENCES STATUS_RESERVE(status_reserve_id);
ALTER TABLE RESERVE ADD CONSTRAINT reserve_serv_id_fk FOREIGN KEY(serv_id) REFERENCES SERVICE(serv_id);


CREATE TABLE RESERVE_RESTRICTION(
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

--CONSTRAINT
ALTER TABLE RESERVE_RESTRICTION ADD CONSTRAINT restriction_serv_id_fk FOREIGN KEY(serv_id) REFERENCES SERVICE(serv_id);

insert into STATUS_RESERVE values('ACT','Activa',           to_date ('15-05-2020','DD-MM-YYYY '),to_date ('15-05-2020','DD-MM-YYYY'),0);
insert into STATUS_RESERVE values('REA','Realizada',        to_date ('15-05-2020','DD-MM-YYYY '),to_date ('15-05-2020','DD-MM-YYYY'),0);
insert into STATUS_RESERVE values('CAN','Cancelada',        to_date ('15-05-2020','DD-MM-YYYY '),to_date ('15-05-2020','DD-MM-YYYY'),0);
insert into STATUS_RESERVE values('PER','Hora Perdida',     to_date ('15-05-2020','DD-MM-YYYY '),to_date ('15-05-2020','DD-MM-YYYY'),0);    


insert into RESERVE values(
'7087D8C3A17F48FF961A23EB4575B519',--reserve_id
'A7515A96926141479DEC462D7CC647F0',--serv_id
'70BA44E0EC9A42018FA0D9465CC695B5',--appuser_id
to_date('01-06-2020 09:00:00','dd-mm-yyyy hh24:mi:ss'),--start_date_hour
to_date('01-06-2020 09:30:00','dd-mm-yyyy hh24:mi:ss'),--end_date_hour
'ACT',--status_reserve_id
to_date('01-06-2020 09:00 AM','DD-MM-YYYY HH:MI AM'),--updated_at
to_date('01-06-2020 09:00 AM','DD-MM-YYYY HH:MI AM'),--created_at
0--deleted
);

insert into RESERVE values(
'5E4CCF00C8B849E88A1BEAD880980E69',--reserve_id
'09F68676182ACF8AE040578CB20B7491',--serv_id
'70BA44E0EC9A42018FA0D9465CC695B5',--appuser_id
to_date('01-06-2020 10:00:00','dd-mm-yyyy hh24:mi:ss'),--start_date_hour
to_date('01-06-2020 12:00:00','dd-mm-yyyy hh24:mi:ss'),--end_date_hour
'ACT',--status_reserve_id
to_date('01-06-2020 09:00 AM','DD-MM-YYYY HH:MI AM'),--updated_at
to_date('01-06-2020 09:00 AM','DD-MM-YYYY HH:MI AM'),--created_at
0--deleted
);

insert into RESERVE values(
'09CFF36B69144A96A90E2868539C17BF',--reserve_id
'50D00011F4994D4FBB4FBDA04D2AFB97',--serv_id
'70BA44E0EC9A42018FA0D9465CC695B5',--appuser_id
to_date('01-06-2020 14:00:00','dd-mm-yyyy hh24:mi:ss'),--start_date_hour
to_date('01-06-2020 14:30:00','dd-mm-yyyy hh24:mi:ss'),--end_date_hour
'ACT',--status_reserve_id
to_date('01-06-2020 09:00 AM','DD-MM-YYYY HH:MI AM'),--updated_at
to_date('01-06-2020 09:00 AM','DD-MM-YYYY HH:MI AM'),--created_at
0--deleted
);


insert into RESERVE_RESTRICTION values(
'DCD286FE94C045669CBD7B921A3EEE71',--restriction_id
'09F68676182ACF8AE040578CB20B7491',--serv_id
to_date('02-06-2020 17:00:00','dd-mm-yyyy hh24:mi:ss'),--start_date_hour
to_date('02-06-2020 19:00:00','dd-mm-yyyy hh24:mi:ss'),--end_date_hour
to_date('01-06-2020 08:00 AM','DD-MM-YYYY HH:MI AM'),--updated_at
to_date('01-06-2020 08:00 AM','DD-MM-YYYY HH:MI AM'),--created_at
0--deleted
);

insert into RESERVE_RESTRICTION values(
'0CF7A76A3FB9457F86055F02E563D1A5',--restriction_id
'44703D2E22344158BAE3E3BB30699F06',--serv_id
to_date('01-06-2020 17:00:00','dd-mm-yyyy hh24:mi:ss'),--start_date_hour
to_date('01-06-2020 19:00:00','dd-mm-yyyy hh24:mi:ss'),--end_date_hour
to_date('01-06-2020 09:00 AM','DD-MM-YYYY HH:MI AM'),--updated_at
to_date('01-06-2020 09:00 AM','DD-MM-YYYY HH:MI AM'),--created_at
0--deleted
);

insert into STORE_SCHEDULE values(
sys_guid(),
to_date('21-02-2006 09:00:00','dd-mm-yyyy hh24:mi:ss'),--start_hour
to_date('21-02-2006 13:00:00','dd-mm-yyyy hh24:mi:ss'),--start_lunch_hour
to_date('21-02-2006 14:00:00','dd-mm-yyyy hh24:mi:ss'),--end_lunch_hour
to_date('21-02-2006 19:00:00','dd-mm-yyyy hh24:mi:ss'),--end_hour
to_date('01-06-2020 09:00 AM','DD-MM-YYYY HH:MI AM'),--updated_at
to_date('01-06-2020 09:00 AM','DD-MM-YYYY HH:MI AM'),--created_at
0--deleted
);



commit;
