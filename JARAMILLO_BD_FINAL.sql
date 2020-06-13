-------------------------------- DROP ORIGINAL TABLES --------------------------------
DROP TABLE APP_USER CASCADE CONSTRAINTS;

DROP TABLE USER_TYPE CASCADE CONSTRAINTS;

DROP TABLE USER_STATUS CASCADE CONSTRAINTS;

DROP TABLE VERIFICATIONTOKEN;
-------------------------------- CREATE NEW TABLES --------------------------------
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


INSERT INTO USER_TYPE ( USER_TYPE_ID, NAME, CREATED_AT , UPDATED_AT , DELETED ) VALUES('ADM', 'Administrador', SYSDATE, SYSDATE, 0);
INSERT INTO USER_TYPE ( USER_TYPE_ID, NAME, CREATED_AT , UPDATED_AT , DELETED) VALUES('CLI', 'Cliente', SYSDATE, SYSDATE, 0);
INSERT INTO USER_TYPE ( USER_TYPE_ID, NAME, CREATED_AT , UPDATED_AT , DELETED) VALUES('CAJ', 'Cajero', SYSDATE, SYSDATE, 0);
INSERT INTO USER_TYPE ( USER_TYPE_ID, NAME, CREATED_AT , UPDATED_AT , DELETED) VALUES('VEN', 'Vendedor', SYSDATE, SYSDATE, 0);
INSERT INTO USER_TYPE ( USER_TYPE_ID, NAME, CREATED_AT , UPDATED_AT , DELETED) VALUES('SUP', 'Supervisor', SYSDATE, SYSDATE, 0);
INSERT INTO USER_TYPE ( USER_TYPE_ID, NAME, CREATED_AT , UPDATED_AT , DELETED) VALUES('MEC', 'Mecánico', SYSDATE, SYSDATE, 0);
INSERT INTO USER_TYPE ( USER_TYPE_ID, NAME, CREATED_AT , UPDATED_AT , DELETED) VALUES('TES', 'Testing', SYSDATE, SYSDATE, 0);

INSERT INTO USER_STATUS ( STATUS_ID , STATUS , CREATED_AT , UPDATED_AT , DELETED) VALUES('BAN', 'Desactivado', SYSDATE, SYSDATE, 0);
INSERT INTO USER_STATUS ( STATUS_ID , STATUS , CREATED_AT , UPDATED_AT , DELETED) VALUES('ACT', 'Activo', SYSDATE, SYSDATE, 0);


-- ADMIN SEBA
INSERT INTO APP_USER (APPUSER_ID, USERNAME, HASH, EMAIL, NAME, LAST_NAMES, RUT, ADRESS, PHONE, BIRTHDAY, LASTLOGIN, MAIL_CONFIRMED, USER_TYPE_ID, STATUS_ID, UPDATED_AT, DELETED, CREATED_AT)
VALUES(
	SYS_GUID(), -- APPUSER_ID
	'sebaherrera', -- USERNAME
	'42437a69a8fd1d5f3a9d1a01f072187282a663cf327ffbf11412f25ff01cd29a', -- HASH : alfalfa8.
	'seherreraluna@gmail.cl', -- EMAIL
	'Sebastian', -- NAME
	'Herrera Luna', -- LAST_NAMES
    '18.594.857-9',--RUT
	'Calle los Michis #469', -- ADRESS
	'+56966666666', -- PHONE
	TO_DATE('1994/06/18 00:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- BIRTHDAY
	TO_DATE('2020/04/10 20:36:20', 'yyyy/mm/dd hh24:mi:ss'), -- LASTLOGIN
	1, -- MAIL_CONFIRMED
	'ADM', -- USER_TYPE_ID
	'ACT', -- STATUS_ID
	TO_DATE('2020/03/20 09:02:11', 'yyyy/mm/dd hh24:mi:ss'), -- UPDATED_AT
	0, -- DELETED
	TO_DATE('2020/03/20 09:02:11', 'yyyy/mm/dd hh24:mi:ss') -- CREATED_AT
);

-- ADMIN MATI
INSERT INTO APP_USER (APPUSER_ID, USERNAME, HASH, EMAIL, NAME, LAST_NAMES,RUT, ADRESS, PHONE, BIRTHDAY, LASTLOGIN, MAIL_CONFIRMED, USER_TYPE_ID, STATUS_ID, UPDATED_AT, DELETED, CREATED_AT)
VALUES(
	SYS_GUID(), -- APPUSER_ID
	'matiespinoza', -- USERNAME
	'5b7975dfb3bec75ab9b665f8c3bbb0494303ebed3e25df287102bf34d64c2d4c', -- HASH : mati
	'mati.esp@gmail.cl', -- EMAIL
	'Matias', -- NAME
	'Espinoza', -- LAST_NAMES
    '19.387.716-8',--RUT
	'Calle Campo #69', -- ADRESS
	'+56955555555', -- PHONE
	TO_DATE('1996/05/20 00:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- BIRTHDAY
	TO_DATE('2020/04/10 20:36:20', 'yyyy/mm/dd hh24:mi:ss'), -- LASTLOGIN
	1, -- MAIL_CONFIRMED
	'ADM', -- USER_TYPE_ID
	'ACT', -- STATUS_ID
	TO_DATE('2020/03/20 09:02:11', 'yyyy/mm/dd hh24:mi:ss'), -- UPDATED_AT
	0, -- DELETED
	TO_DATE('2020/03/20 09:02:11', 'yyyy/mm/dd hh24:mi:ss') -- CREATED_AT
);

-- ADMIN IRINA
INSERT INTO APP_USER (APPUSER_ID, USERNAME, HASH, EMAIL, NAME, LAST_NAMES, RUT, ADRESS, PHONE, BIRTHDAY, LASTLOGIN, MAIL_CONFIRMED, USER_TYPE_ID, STATUS_ID, UPDATED_AT, DELETED, CREATED_AT)
VALUES(
	SYS_GUID(), -- APPUSER_ID
	'irinmunoz', -- USERNAME
	'753923b3a91b753ca991116df61a90c06f97721277cb2829b21a4a209f7aa932', -- HASH : irin.munoz
	'irin.munoz@gmail.cl', -- EMAIL
	'Irina', -- NAME
	'Muñoz', -- LAST_NAMES
    '20.124.502-8',--RUT
	'Calle los Dogo #696', -- ADRESS
	'+5697777777', -- PHONE
	TO_DATE('1999/06/01 00:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- BIRTHDAY
	TO_DATE('2020/04/01 20:36:20', 'yyyy/mm/dd hh24:mi:ss'), -- LASTLOGIN
	1, -- MAIL_CONFIRMED
	'ADM', -- USER_TYPE_ID
	'ACT', -- STATUS_ID
	TO_DATE('2020/03/20 09:02:11', 'yyyy/mm/dd hh24:mi:ss'), -- UPDATED_AT
	0, -- DELETED
	TO_DATE('2020/03/20 09:02:11', 'yyyy/mm/dd hh24:mi:ss') -- CREATED_AT
);

-- MECANICO DELETEADO
INSERT INTO APP_USER (APPUSER_ID, USERNAME, HASH, EMAIL, NAME, LAST_NAMES, RUT, ADRESS, PHONE, BIRTHDAY, LASTLOGIN, MAIL_CONFIRMED, USER_TYPE_ID, STATUS_ID, UPDATED_AT, DELETED, CREATED_AT)
VALUES(
	'E1A6C6A452134F5A9F06CFABFC762F6E', -- APPUSER_ID
	'Mecanico_del', -- USERNAME
	'80dd4af09cf88727fd03e84f81f9744755bde12e9f3aac1c655eb86a847b87f2', -- HASH : MecanicoDel.
	'Mecanico@gmail.cl', -- EMAIL
	'Mec', -- NAME
	'Anico', -- LAST_NAMES
    '16.915.351-5',--RUT
	'Avenida Gran avenida #420', -- ADRESS
	'+56921569878', -- PHONE
	TO_DATE('1990/05/12 00:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- BIRTHDAY
	TO_DATE('2020/04/09 18:02:43', 'yyyy/mm/dd hh24:mi:ss'), -- LASTLOGIN
	1, -- MAIL_CONFIRMED
	'MEC', -- USER_TYPE_ID
	'ACT', -- STATUS_ID
	TO_DATE('2020/02/18 17:08:11', 'yyyy/mm/dd hh24:mi:ss'), -- UPDATED_AT
	1, -- DELETED
	TO_DATE('2020/02/18 17:08:11', 'yyyy/mm/dd hh24:mi:ss') -- CREATED_AT
);


-- MECÁNICA CON MAIL CONFIRMADO
INSERT INTO APP_USER (APPUSER_ID, USERNAME, HASH, EMAIL, NAME, LAST_NAMES, RUT, ADRESS, PHONE, BIRTHDAY, LASTLOGIN, MAIL_CONFIRMED, USER_TYPE_ID, STATUS_ID, UPDATED_AT, DELETED, CREATED_AT)
VALUES(
	'5D0E6C7B078A4B0AB3B5C459641213FB', -- APPUSER_ID
	'ReGarrido', -- USERNAME
	'55648da29f4d04cbddcd9f82f43c26068ffdc63749a6b2bda0b617ab8286dd61', -- HASH : Renata1659
	'rgarrido@gmail.cl', -- EMAIL
	'Renata', -- NAME
	'Garrido Gonzales', -- LAST_NAMES
    '17.359.326-4',--RUT
	'Calle Serrano #14', -- ADRESS
	'+56921659878', -- PHONE
	TO_DATE('1990/05/12 00:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- BIRTHDAY
	TO_DATE('2020/04/09 18:02:43', 'yyyy/mm/dd hh24:mi:ss'), -- LASTLOGIN
	1, -- MAIL_CONFIRMED
	'MEC', -- USER_TYPE_ID
	'ACT', -- STATUS_ID
	TO_DATE('2020/02/18 17:08:11', 'yyyy/mm/dd hh24:mi:ss'), -- UPDATED_AT
	0, -- DELETED
	TO_DATE('2020/02/18 17:08:11', 'yyyy/mm/dd hh24:mi:ss') -- CREATED_AT
);


-- MECÁNICA CON MAIL SIN CONFIRMAR
INSERT INTO APP_USER (APPUSER_ID, USERNAME, HASH, EMAIL, NAME, LAST_NAMES, RUT, ADRESS, PHONE, BIRTHDAY, LASTLOGIN, MAIL_CONFIRMED, USER_TYPE_ID, STATUS_ID, UPDATED_AT, DELETED, CREATED_AT)
VALUES(
	'6A79225115CB499C8C72B9340EEEC5CA', -- APPUSER_ID
	'EdMartinez', -- USERNAME
	'e24a0c782d188215708bd4239c721c2551969e243fd4750e0fe6ca6ee9a11e7e', -- HASH : EdoTerribleClave
	'edu.martinez@gmail.cl', -- EMAIL
	'Eduardo', -- NAME
	'Martinez', -- LAST_NAMES
    '15.684.326-0',--RUT
	'Calle las Ilusiones #1965', -- ADRESS
	'+56965987845', -- PHONE
	TO_DATE('1985/02/28 00:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- BIRTHDAY
	TO_DATE('2020/03/25 18:02:43', 'yyyy/mm/dd hh24:mi:ss'), -- LASTLOGIN
	0, -- MAIL_CONFIRMED
	'MEC', -- USER_TYPE_ID
	'ACT', -- STATUS_ID
	TO_DATE('2020/03/25 17:08:11', 'yyyy/mm/dd hh24:mi:ss'), -- UPDATED_AT
	0, -- DELETED
	TO_DATE('2020/03/25 17:08:11', 'yyyy/mm/dd hh24:mi:ss') -- CREATED_AT
);
--MECÁNICO PUBLICACIÓN 1
INSERT INTO APP_USER (APPUSER_ID, USERNAME, HASH, EMAIL, NAME, LAST_NAMES, RUT, ADRESS, PHONE, BIRTHDAY, LASTLOGIN, MAIL_CONFIRMED, USER_TYPE_ID, STATUS_ID, UPDATED_AT, DELETED, CREATED_AT)
VALUES(
	'7CD5B7769DF75CEFE034080020825436', -- APPUSER_ID
	'AutSuzuki', -- USERNAME
	'3070bb0d40134f685eaacd8da49e576a878676091c720db237cbcebc6a6380bd', -- HASH : suzukimotriz
	'automotriz.contacto@gmail.cl', -- EMAIL
	'Raul', -- NAME
	'Cerda Martinez', -- LAST_NAMES
    '12.035.261-5',--RUT
	'Calle los Cardenales #14', -- ADRESS
	'+56921659878', -- PHONE
	TO_DATE('1976/11/02 00:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- BIRTHDAY
	TO_DATE('2020/04/19 12:02:43', 'yyyy/mm/dd hh24:mi:ss'), -- LASTLOGIN
	1, -- MAIL_CONFIRMED
	'MEC', -- USER_TYPE_ID
	'ACT', -- STATUS_ID
	TO_DATE('2020/04/05 19:08:11', 'yyyy/mm/dd hh24:mi:ss'), -- UPDATED_AT
	0, -- DELETED
	TO_DATE('2020/04/05 19:08:11', 'yyyy/mm/dd hh24:mi:ss') -- CREATED_AT
);

--MECÁNICO PUBLICACIÓN 2
INSERT INTO APP_USER (APPUSER_ID, USERNAME, HASH, EMAIL, NAME, LAST_NAMES, RUT, ADRESS, PHONE, BIRTHDAY, LASTLOGIN, MAIL_CONFIRMED, USER_TYPE_ID, STATUS_ID, UPDATED_AT, DELETED, CREATED_AT)
VALUES(
	'7CD5B7769DF85CEFE034080020825436', -- APPUSER_ID
	'KaiserMecanic', -- USERNAME
	'4ba19c09e9b41d673f952d7f3c0dc7ba9ee11746902d6392682fec00188fea4a', -- HASH : Kaiser1991
	'Kaiser1991@gmail.cl', -- EMAIL
	'Ariana', -- NAME
	'Fernandez Ortega', -- LAST_NAMES
    '18.215.891-k',--RUT
	'Villa san Matin, Melipilla', -- ADRESS
	'+56924521786', -- PHONE
	TO_DATE('1991/01/25 00:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- BIRTHDAY
	TO_DATE('2020/04/18 12:03:26', 'yyyy/mm/dd hh24:mi:ss'), -- LASTLOGIN
	1, -- MAIL_CONFIRMED
	'MEC', -- USER_TYPE_ID
	'ACT', -- STATUS_ID
	TO_DATE('2020/04/18 12:02:43', 'yyyy/mm/dd hh24:mi:ss'), -- UPDATED_AT
	0, -- DELETED
	TO_DATE('2020/04/05 12:02:43', 'yyyy/mm/dd hh24:mi:ss') -- CREATED_AT
);

-- VENDEDOR
INSERT INTO APP_USER (APPUSER_ID, USERNAME, HASH, EMAIL, NAME, LAST_NAMES, RUT, ADRESS, PHONE, BIRTHDAY, LASTLOGIN, MAIL_CONFIRMED, USER_TYPE_ID, STATUS_ID, UPDATED_AT, DELETED, CREATED_AT)
VALUES(
	'3E309C13E3314519A9270C336AFB75C2', -- APPUSER_ID
	'Emili', -- USERNAME
	'6ec1a7ef23bedc20828fee4ac8f04fe5edb54344cd12930501ef44ab1cfd2854', -- HASH : emilia1998
	'emiliBags@gmail.cl', -- EMAIL
	'Emilia', -- NAME
	'Bolson de la Comarca', -- LAST_NAMES
    '19.215.891-8',--RUT
	'Alto Prado, Melipilla', -- ADRESS
	'+56924521857', -- PHONE
	TO_DATE('1998/01/25 00:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- BIRTHDAY
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- LASTLOGIN
	1, -- MAIL_CONFIRMED
	'VEN', -- USER_TYPE_ID
	'ACT', -- STATUS_ID
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- UPDATED_AT
	0, -- DELETED
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss') -- CREATED_AT
);

-- CAJERO
INSERT INTO APP_USER (APPUSER_ID, USERNAME, HASH, EMAIL, NAME, LAST_NAMES, RUT, ADRESS, PHONE, BIRTHDAY, LASTLOGIN, MAIL_CONFIRMED, USER_TYPE_ID, STATUS_ID, UPDATED_AT, DELETED, CREATED_AT)
VALUES(
	'7729037DEE6B45F4907AF8FA785C6971', -- APPUSER_ID
	'Cajero_1', -- USERNAME
	'680b461d8bc6b9a0da059b3c5786bb0de16e3994c59694774246f1378127ff85', -- HASH : cajero_uno
	'lecajero@gmail.cl', -- EMAIL
	'Cajero', -- NAME
	'Numero Uno', -- LAST_NAMES
    '16.215.831-0',--RUT
	'Las berenjenas, Melipilla', -- ADRESS
	'+56924524578', -- PHONE
	TO_DATE('1993/09/20 00:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- BIRTHDAY
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- LASTLOGIN
	1, -- MAIL_CONFIRMED
	'CAJ', -- USER_TYPE_ID
	'ACT', -- STATUS_ID
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- UPDATED_AT
	0, -- DELETED
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss') -- CREATED_AT
);

INSERT INTO APP_USER (APPUSER_ID, USERNAME, HASH, EMAIL, NAME, LAST_NAMES, RUT, ADRESS, PHONE, BIRTHDAY, LASTLOGIN, MAIL_CONFIRMED, USER_TYPE_ID, STATUS_ID, UPDATED_AT, DELETED, CREATED_AT)
VALUES(
	'5093234C73AE4954BB6DD7E841FEEE12', -- APPUSER_ID
	'Alberto', -- USERNAME
	'ec58ad359735b33047b76cdd24deb26ff20be3a6e9cbe6223d60e4af5d8e355f', -- HASH : Alberto123.
	'albert@gmail.cl', -- EMAIL
	'Alberto', -- NAME
	'Castro Vera', -- LAST_NAMES
    '16.215.831-0',--RUT
	'Las berenjenas, Melipilla', -- ADRESS
	'+56924524578', -- PHONE
	TO_DATE('1993/09/20 00:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- BIRTHDAY
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- LASTLOGIN
	1, -- MAIL_CONFIRMED
	'CAJ', -- USER_TYPE_ID
	'ACT', -- STATUS_ID
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- UPDATED_AT
	0, -- DELETED
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss') -- CREATED_AT
);

-- CLIENTE
INSERT INTO APP_USER (APPUSER_ID, USERNAME, HASH, EMAIL, NAME, LAST_NAMES, RUT, ADRESS, PHONE, BIRTHDAY, LASTLOGIN, MAIL_CONFIRMED, USER_TYPE_ID, STATUS_ID, UPDATED_AT, DELETED, CREATED_AT)
VALUES(
	'70BA44E0EC9A42018FA0D9465CC695B5', -- APPUSER_ID
	'Catalina', -- USERNAME
	'6ec1a7ef23bedc20828fee4ac8f04fe5edb54344cd12930501ef44ab1cfd2854', -- HASH : emilia1998
	'catalin@gmail.cl', -- EMAIL
	'Catalina', -- NAME
	'Latorre Villacura', -- LAST_NAMES
    '17.215.238-5',--RUT
	'PSJA Kast, Melipilla', -- ADRESS
	'+56978549865', -- PHONE
	TO_DATE('1997/11/01 00:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- BIRTHDAY
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- LASTLOGIN
	1, -- MAIL_CONFIRMED
	'CLI', -- USER_TYPE_ID
	'ACT', -- STATUS_ID
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- UPDATED_AT
	0, -- DELETED
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss') -- CREATED_AT
);

INSERT INTO APP_USER (APPUSER_ID, USERNAME, HASH, EMAIL, NAME, LAST_NAMES, RUT, ADRESS, PHONE, BIRTHDAY, LASTLOGIN, MAIL_CONFIRMED, USER_TYPE_ID, STATUS_ID, UPDATED_AT, DELETED, CREATED_AT)
VALUES(
	'6728165C24E54E7BB11AF7AC553FC465', -- APPUSER_ID
	'Cliente_1', -- USERNAME
	'2b8d15c748a560f355808453472959bb1820c1192c4bd318de57cd19786ce44b', -- HASH : cliente_sin_eliminar
	'cliente_1@gmail.cl', -- EMAIL
	'Cliente', -- NAME
	'Sin eliminar 1', -- LAST_NAMES
    '23.788.885-5',--RUT
	'PSJ Nueva Mayoria, Putahendo', -- ADRESS
	'+56978598465', -- PHONE
	TO_DATE('1987/11/01 00:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- BIRTHDAY
	TO_DATE('2020/04/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- LASTLOGIN
	1, -- MAIL_CONFIRMED
	'CLI', -- USER_TYPE_ID
	'ACT', -- STATUS_ID
	TO_DATE('2020/06/13 12:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- UPDATED_AT
	0, -- DELETED
	TO_DATE('2020/06/13 12:00:00', 'yyyy/mm/dd hh24:mi:ss') -- CREATED_AT
);

INSERT INTO APP_USER (APPUSER_ID, USERNAME, HASH, EMAIL, NAME, LAST_NAMES, RUT, ADRESS, PHONE, BIRTHDAY, LASTLOGIN, MAIL_CONFIRMED, USER_TYPE_ID, STATUS_ID, UPDATED_AT, DELETED, CREATED_AT)
VALUES(
	'ABA080E4FC1E4DDCA2F8A2000CA1DEB9', -- APPUSER_ID
	'Cliente_2', -- USERNAME
	'6f02fecb159da7dd2426b9baaf39a311354e08ee2be04b54fb04030238eabbbd', -- HASH : cliente_sin_eliminar2
	'cliente_2@gmail.cl', -- EMAIL
	'Cliente', -- NAME
	'Sin eliminar 2', -- LAST_NAMES
    '9.498.966-3',--RUT
	'PSJ Unidad Popular, Valdivia', -- ADRESS
	'+56987598465', -- PHONE
	TO_DATE('1999/11/01 00:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- BIRTHDAY
	TO_DATE('2020/04/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- LASTLOGIN
	1, -- MAIL_CONFIRMED
	'CLI', -- USER_TYPE_ID
	'ACT', -- STATUS_ID
	TO_DATE('2020/06/13 12:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- UPDATED_AT
	0, -- DELETED
	TO_DATE('2020/06/13 12:00:00', 'yyyy/mm/dd hh24:mi:ss') -- CREATED_AT
);


-- CLIENTE
INSERT INTO APP_USER (APPUSER_ID, USERNAME, HASH, EMAIL, NAME, LAST_NAMES, RUT, ADRESS, PHONE, BIRTHDAY, LASTLOGIN, MAIL_CONFIRMED, USER_TYPE_ID, STATUS_ID, UPDATED_AT, DELETED, CREATED_AT)
VALUES(
	'69877AA15ADA4FDD8D8763A79A10E016', -- APPUSER_ID
	'dummy_USER', -- USERNAME
	'eb0432b915bc612423fa80ea208d5a5ae583f26eb7278e10ff4ebc6ecf0cb88a', -- HASH : pass12345.
	'dummyuser1@gmail.cl', -- EMAIL
	'Nombre Nombre', -- NAME
	'Apellido Apellido', -- LAST_NAMES
    '00.000.000-0',--RUT
	'Dirección Lorem Ipsum, Comuna', -- ADRESS
	'+56900000000', -- PHONE
	TO_DATE('1990/01/01 00:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- BIRTHDAY
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- LASTLOGIN
	1, -- MAIL_CONFIRMED
	'CLI', -- USER_TYPE_ID
	'ACT', -- STATUS_ID
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- UPDATED_AT
	0, -- DELETED
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss') -- CREATED_AT
);


-- SUPERVIDOR
INSERT INTO APP_USER (APPUSER_ID, USERNAME, HASH, EMAIL, NAME, LAST_NAMES, RUT, ADRESS, PHONE, BIRTHDAY, LASTLOGIN, MAIL_CONFIRMED, USER_TYPE_ID, STATUS_ID, UPDATED_AT, DELETED, CREATED_AT)
VALUES(
	'AF2F8526AFD54C62831CE57FBA245A4F', -- APPUSER_ID
	'Supervisor', -- USERNAME
	'42437a69a8fd1d5f3a9d1a01f072187282a663cf327ffbf11412f25ff01cd29a', -- HASH : alfalfa8.
	'sup@jaramillo.cl', -- EMAIL
	'Supervisor', -- NAME
	'Apellido', -- LAST_NAMES
    '18.531.236-8',--RUT
	'Calle Bleh, Melipilla', -- ADRESS
	'+56991232145', -- PHONE
	TO_DATE('1994/08/08 00:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- BIRTHDAY
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- LASTLOGIN
	1, -- MAIL_CONFIRMED
	'SUP', -- USER_TYPE_ID
	'ACT', -- STATUS_ID
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- UPDATED_AT
	0, -- DELETED
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss') -- CREATED_AT
);

-- TESTING
INSERT INTO APP_USER (APPUSER_ID, USERNAME, HASH, EMAIL, NAME, LAST_NAMES, RUT, ADRESS, PHONE, BIRTHDAY, LASTLOGIN, MAIL_CONFIRMED, USER_TYPE_ID, STATUS_ID, UPDATED_AT, DELETED, CREATED_AT)
VALUES(
	'E2989DC123F2497E8C1B9EF13B43FA37', -- APPUSER_ID
	'Testing', -- USERNAME
	'42437a69a8fd1d5f3a9d1a01f072187282a663cf327ffbf11412f25ff01cd29a', -- HASH : alfalfa8.
	'test@jaramillo.cl', -- EMAIL
	'Testing', -- NAME
	'Apellido', -- LAST_NAMES
    '20.355.888-8',--RUT
	'Calle Cansado, Melipilla', -- ADRESS
	'+56941233512', -- PHONE
	TO_DATE('2001/06/06 00:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- BIRTHDAY
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- LASTLOGIN
	1, -- MAIL_CONFIRMED
	'TES', -- USER_TYPE_ID
	'ACT', -- STATUS_ID
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss'), -- UPDATED_AT
	0, -- DELETED
	TO_DATE('2020/05/14 12:00:00', 'yyyy/mm/dd hh24:mi:ss') -- CREATED_AT
);



-----------------------------------------------------o-----------------------------------------------------
--                                              PUBLICACIONES
-----------------------------------------------------o-----------------------------------------------------
drop table PUBLICATION CASCADE CONSTRAINTS;

drop table PUBLIC_STATUS;

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

--INSERT TABLA PUBLIC_STATUS
insert into PUBLIC_STATUS values('PEN','Pendiente', to_date('11-04-2020','DD-MM-YYYY '),to_date ('11-04-2020','DD-MM-YYYY'),0);--pending
insert into PUBLIC_STATUS values('ACT','Activa',    to_date('11-04-2020','DD-MM-YYYY '),to_date ('11-04-2020','DD-MM-YYYY'),0);--active
insert into PUBLIC_STATUS values('DEB','Con deuda', to_date('11-04-2020','DD-MM-YYYY '),to_date ('11-04-2020','DD-MM-YYYY'),0);--debt(MOROSA)
insert into PUBLIC_STATUS values('INA','Inactiva',  to_date('11-04-2020','DD-MM-YYYY '),to_date ('11-04-2020','DD-MM-YYYY'),0);--unactive
insert into PUBLIC_STATUS values('REJ','Rechazada',  to_date('11-04-2020','DD-MM-YYYY '),to_date ('11-04-2020','DD-MM-YYYY'),0);--Rechazada

--INSERT TABLA PUBLICATION
insert into PUBLICATION --publicacion 1
(public_id,appuser_id,user_type_id,public_status_id,created_at,updated_at,deleted,title,public_desc,schedule,services,bussiness_name,address,comuna,region,landline,mobile_number,email,views)
values
    (SYS_GUID(),--public_id
    '7CD5B7769DF75CEFE034080020825436',--mecha_id
    'MEC',--user_type_id
    'ACT',--public_status_id
    to_date ('13-04-2020 10:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('13-04-2020 10:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0,--deleted
    'Automotriz J.D.C Suzuki',--title
    'Servicio Suzuki.Chevrolet,Mazda, Mantenciones de kilometraje y desabolladura.Personal especializado en las marcas.Garantía por trabajos realizados.20 años de experiencia en el rubro.',--public_desc
    'De 8:30 a 19:30, abierto toda la semana',--schedule
    'Mecánica general, Mantenciones preventivas, Alineación, Balanceo, Frenos, Embrague, Afinamiento, Scanner, Limpieza de tapiz, Cambio de aceite, Tren delantero, Emergencia 24 horas,
    Simunizado y encerado, Diagnostico Computarizado',--services
    'Automotriz J.D.C',--bussiness_name
    'Calle Fernández Concha 146, Ñuñoa, Región Metropolitana',--adress
    'Ñuñoa',--comuna
    'Metropolitana',--region
    '(2)22744400',--landline
    '0',--mobile_number
    'jdc.contacto@gmail.com',--email
    5--views
);

insert into PUBLICATION  --publicacion 2
(public_id,appuser_id,user_type_id,public_status_id,created_at,updated_at,deleted,title,public_desc,schedule,services,bussiness_name,address,comuna,region,landline,mobile_number,email,views)
values
    (SYS_GUID(),--public_id
    '7CD5B7769DF85CEFE034080020825436',--mecha_id
    'MEC',--user_type_id
    'ACT',--public_status_id
    to_date ('05-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('05-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0,--deleted
    'Kaiser Lubricentro',--title
    'Kaiser Lubricentro un taller automotriz dedicado a dar servicios de lubricación en general tanto para vehículos bencineros como diesel. cambiamos el aceite de motor, caja de cambio, mecánica o automática, diferenciales, cajas de transparencias, dirección, hidráulica, etc.',--public_desc
    'De 9:30 a 18:30, de lunes a viernes',--schedule
    'CAMBIO DE ACEITE,El cambio de aceite se debe realizar cada 5.000 kms o cada 10.000 kms dependiento del aceite que se utilice.
     AFINAMIENTO, El afinamiento se debe realizar cada 15.000 o 20.000 kms. (Una vez al año aproximadamente)
     MECANICA, Mecánica express consiste en trabajos que tardan desde 2 hrs. a 1 día o 2 dependiendo de lo que necesite.
     ACCESORIOS,Accesorios para diferentes cosas del auto, dependiendo de lo que necesite.',--services
    'Kaiser Lubricentro',--bussiness_name
    'Avenida Colón 2878,Valparaíso, Región V',--adress
    'Valparaíso',--comuna
    'Región V',--region
    '(32)2492905',--landline
    '0',--mobile_number
    'KaiserContacto@gmail.com',--email
    65--views
);

insert into PUBLICATION  --publicacion 3
(public_id,appuser_id,user_type_id,public_status_id,created_at,updated_at,deleted,title,public_desc,schedule,services,bussiness_name,address,comuna,region,landline,mobile_number,email,views)
values
    (SYS_GUID(),--public_id
    '5D0E6C7B078A4B0AB3B5C459641213FB',--mecha_id
    'MEC',--user_type_id
    'PEN',--public_status_id
    to_date ('13-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('13-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0,--deleted
    'Lubricamiento y engrasamiento',--title
    'Vulcanización el indio picaro ofrece mantención a automiviles, especializandose en aceitados, lubricaciones y engrasamientos',--public_desc
    'De 10:00 a 18:30, de lunes a viernes',--schedule
    'Lubricamiento: Se lo lubricamo.
	Aceitados: a lo chef.
	Engrasamientos: Se lo engrasamo',--services
    'El indio picaro LTDA',--bussiness_name
    'Avenida Colo-colo 2878,Valparaíso, Región V',--adress
    'Valparaíso',--comuna
    'Región V',--region
    '(32)2429905',--landline
    '0',--mobile_number
    'El1ndio@gmail.com',--email
    3--views
);

insert into PUBLICATION  --publicacion 4
(public_id,appuser_id,user_type_id,public_status_id,created_at,updated_at,deleted,title,public_desc,schedule,services,bussiness_name,address,comuna,region,landline,mobile_number,email,views)
values
    (SYS_GUID(),--public_id
    '6A79225115CB499C8C72B9340EEEC5CA',--mecha_id
    'MEC',--user_type_id
    'PEN',--public_status_id
    to_date ('13-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('13-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0,--deleted
    'Venta de partes',--title
    'Venta de repuestos para autos de todas las marcas y edades',--public_desc
    'De 10:00 a 18:30, de lunes a sabado',--schedule
    'Venta de repuestos: Contamos con repuestos para las marcas Audi, Toyota, Mitsubishi, Lada y Crevrolet.',--services
    'Distribuidora El destripador',--bussiness_name
    'Avenida Wakala 8888,Santiago, Región Metropolitana',--adress
    'Las condes',--comuna
    'Región Metropolitana',--region
    '(32)3429905',--landline
    '0',--mobile_number
    'Destripando_ventas@gmail.com',--email
    88--views
);

insert into PUBLICATION  --publicacion 5
(public_id,appuser_id,user_type_id,public_status_id,created_at,updated_at,deleted,title,public_desc,schedule,services,bussiness_name,address,comuna,region,landline,mobile_number,email,views)
values
    (SYS_GUID(),--public_id
    '7CD5B7769DF85CEFE034080020825436',--mecha_id
    'MEC',--user_type_id
    'DEB',--public_status_id
    to_date ('13-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('13-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0,--deleted
    'Tuneo de pana',--title
    'Le enchulamos la maquina pa que sea el mas conotao de la pobla',--public_desc
    'De 10:00 a 18:30, de lunes a sabado',--schedule
    'Tuneo personalizado: le bajamos la altura de las ruedas pa que se vea chorizo, le ponemos una manito de pintura, unos neones, un alerón y le van a llover las washas.
	Hacemos boletas y facturas para los negocios',--services
    'Big Ds Tunning',--bussiness_name
    'Avenida Piñen 2314,Santiago, Región Metropolitana',--adress
    'La pintana',--comuna
    'Región Metropolitana',--region
    '(32)3431105',--landline
    '0',--mobile_number
    'tu.sersimbol@gmail.com',--email
    0--views
);

insert into PUBLICATION  --publicacion 6
(public_id,appuser_id,user_type_id,public_status_id,created_at,updated_at,deleted,title,public_desc,schedule,services,bussiness_name,address,comuna,region,landline,mobile_number,email,views)
values
    (SYS_GUID(),--public_id
    '7CD5B7769DF75CEFE034080020825436',--mecha_id
    'MEC',--user_type_id
    'INA',--public_status_id
    to_date ('13-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('13-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0,--deleted
    'Desabolladura',--title
    'Realizamos servicio de desembolladuria y mantención basica',--public_desc
    'De 10:00 a 18:30, de lunes a sabado',--schedule
    'Desabolladuria: reparamos daños al chasis por choques o rozaduras.
	Mantenciones: se realizan cuidados basicos, cambio de aceite y lavado',--services
    'Desabolladuria Big Bong',--bussiness_name
    'Avenida Thor 1223,Santiago, Región Metropolitana',--adress
    'Renca',--comuna
    'Región Metropolitana',--region
    '(32)3442205',--landline
    '0',--mobile_number
    'martilleando_arte@gmail.com',--email
    243--views
);

--prublicación public_test1(1 x INA - deletd 1) 
insert into PUBLICATION --publicacion 1
(public_id,appuser_id,user_type_id,public_status_id,created_at,updated_at,deleted,title,public_desc,schedule,services,bussiness_name,address,comuna,region,landline,mobile_number,email,views)
values
    (SYS_GUID(),--public_id
    'E1A6C6A452134F5A9F06CFABFC762F6E',--mecha_id
    'MEC',--user_type_id
    'INA',--public_status_id
    to_date ('13-06-2020 10:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('13-06-2020 10:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    1,--deleted
    'Automotriz N°',--title
    'Lorem ipsum dolor sit amet, consectetur adipiscing elit.',--public_desc
    'De 8:30 a 19:30, abierto toda la semana',--schedule
    'Maecenas sit amet pellentesque dolor.',--services
    'Automotriz prueba 1',--bussiness_name
    'pharetra consectetur #532',--adress
    'Melipilla',--comuna
    'Metropolitana',--region
    '(2)22744404',--landline
    '0',--mobile_number
    'publictest@gmail.com',--email
    35--views
);

--prublicación public_test2(1 x REJ - deleted 0) 
insert into PUBLICATION --publicacion 1
(public_id,appuser_id,user_type_id,public_status_id,created_at,updated_at,deleted,title,public_desc,schedule,services,bussiness_name,address,comuna,region,landline,mobile_number,email,views)
values
    (SYS_GUID(),--public_id
    'E1A6C6A452134F5A9F06CFABFC762F6E',--mecha_id
    'MEC',--user_type_id
    'REJ',--public_status_id
    to_date ('13-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('13-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    0,--deleted
    'Automotriz N°2',--title
    ' Aenean tincidunt nibh quis dolor semper finibus. Praesent sollicitudin massa ac mi commodo, non suscipit augue posuere. Maecenas ut fermentum eros. Suspendisse tempus pharetra augue id maximus. Aliquam finibus arcu et sem rutrum eleifend. Nulla tempus vulputate justo et ullamcorper.',--public_desc
    'De 8:30 a 19:30, abierto toda la semana',--schedule
    'Aenean quis posuere odio, ac fermentum mauris.',--services
    'Automotriz prueba 2',--bussiness_name
    'pharetra consectetur #0101',--adress
    'Lo Prado',--comuna
    'Metropolitana',--region
    '(2)22741457',--landline
    '0',--mobile_number
    'publictest2@gmail.com',--email
    59--views
);

--prublicación public_test3(1 x REJ - deletd 1) 
insert into PUBLICATION --publicacion 1
(public_id,appuser_id,user_type_id,public_status_id,created_at,updated_at,deleted,title,public_desc,schedule,services,bussiness_name,address,comuna,region,landline,mobile_number,email,views)
values
    (SYS_GUID(),--public_id
    'E1A6C6A452134F5A9F06CFABFC762F6E',--mecha_id
    'MEC',--user_type_id
    'REJ',--public_status_id
    to_date ('14-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
    to_date ('14-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
    1,--deleted
    'Automotriz N°3',--title
    'Cras nec urna sit amet nulla mollis interdum. Proin at diam nec elit euismod luctus non at nulla',--public_desc
    'De 8:30 a 19:30, abierto toda la semana',--schedule
    'Maecenas sit amet pellentesque dolor.',--services
    'Automotriz prueba 1',--bussiness_name
    'pharetra consectetur #4512',--adress
    'Las Condes',--comuna
    'Metropolitana',--region
    '(2)22742031',--landline
    '0',--mobile_number
    'publictest2@gmail.com',--email
    59--views
);
-----------------------------------------------------o-----------------------------------------------------
--                                              PRESTACIONES/SERVICIOS
-----------------------------------------------------o-----------------------------------------------------

DROP TABLE SERVICE CASCADE CONSTRAINTS;

DROP TABLE SERVICE_STATUS;

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

insert into SERVICE_STATUS values('ACT','Activo',   to_date ('20-04-2020','DD-MM-YYYY '),to_date ('20-04-2020','DD-MM-YYYY'),0);--active
insert into SERVICE_STATUS values('INA','Inactivo', to_date ('20-04-2020','DD-MM-YYYY '),to_date ('20-04-2020','DD-MM-YYYY'),0);--unactive

--insert tabla service
--servicio cambio de aceite 
INSERT INTO SERVICE
(serv_id,name,price,serv_desc,estimated_time,serv_status,created_at,updated_at,deleted) 
VALUES('A7515A96926141479DEC462D7CC647F0',--serv_id
'Cambio De Aceite',--serv_name
28000,--serv_price
'Utilizamos las mejores marcas para el cambio de aceite de su vehículo',--serv_desc
28,--estimated_time
'ACT',--serv_status
to_date ('05-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
to_date ('05-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
0--deleted
);

--servicio accesorio
INSERT INTO SERVICE
(serv_id,name,price,serv_desc,estimated_time,serv_status,created_at,updated_at,deleted) 
VALUES('50D00011F4994D4FBB4FBDA04D2AFB97',--serv_id
'Correas Accesorio',--serv_name
40000,--serv_price
'Utilizamos las mejores marcas para el cambio de correas y accesorios de su vehículo',--serv_desc
28,--estimated_time
'ACT',--serv_status
to_date ('05-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
to_date ('05-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
0--deleted
);

--servicio mano de obra
INSERT INTO SERVICE
(serv_id,name,price,serv_desc,estimated_time,serv_status,created_at,updated_at,deleted) 
VALUES('44703D2E22344158BAE3E3BB30699F06',--serv_id
'Desabolladura y Pintura',--serv_name
55000,--serv_price
'Brindamos una solución integral de desabolladura y pintura en tiempo record. Reparamos pintura, abollones, rayas, piquetes y medianas colisiones.',--serv_desc
45,--estimated_time
'ACT',--serv_status
to_date ('05-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
to_date ('05-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
0--deleted
);

--servicio repuestos
INSERT INTO SERVICE
(serv_id,name,price,serv_desc,estimated_time,serv_status,created_at,updated_at,deleted) 
VALUES('09F68676182ACF8AE040578CB20B7491',--serv_id
'Cambio de neumáticos',--serv_name
30000,--serv_price
'Utilizamos las mejores marcas en neumáticos para cambiarselos a tu coche',--serv_desc
120,--estimated_time
'ACT',--serv_status
to_date ('05-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
to_date ('05-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
0--deleted
);

--servicio Serv_1 (1 x ACT - deleted 1) 
INSERT INTO SERVICE
(serv_id,name,price,serv_desc,estimated_time,serv_status,created_at,updated_at,deleted) 
VALUES(
'84ABAB2402ED4814A68B38450E6E9149',--serv_id
'Serv_1',--serv_name
50000,--serv_price
'Servicio de prueba para Initial-D',--serv_desc
40,--estimated_time
'ACT',--serv_status
to_date ('13-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
to_date ('13-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
1--deleted
);

--servicio Serv_2 (1 x INA - deleted 1) 
INSERT INTO SERVICE
(serv_id,name,price,serv_desc,estimated_time,serv_status,created_at,updated_at,deleted) 
VALUES(
'4C85693032CA4125A1C50EC7BA5882D4',--serv_id
'Serv_2',--serv_name
18000,--serv_price
'Servicio de prueba 2 para Initial-D',--serv_desc
10,--estimated_time
'INA',--serv_status
to_date ('13-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
to_date ('13-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
1--deleted
);

--servicio Serv_3 (1 x INA - deleted 0) 
INSERT INTO SERVICE
(serv_id,name,price,serv_desc,estimated_time,serv_status,created_at,updated_at,deleted) 
VALUES(
'83791DAFDAF24085A874A487B3376365',--serv_id
'Serv_3',--serv_name
32000,--serv_price
'Servicio de prueba 3 para Initial-D',--serv_desc
10,--estimated_time
'INA',--serv_status
to_date ('13-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
to_date ('13-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
0--deleted
);
-----------------------------------------------------o-----------------------------------------------------
--                                              PRESTACIONES/PRODUCTOS
-----------------------------------------------------o-----------------------------------------------------
DROP TABLE PRODUCT CASCADE CONSTRAINTS;

DROP TABLE PRODUCT_STATUS;

DROP TABLE UNIT;

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

insert into PRODUCT_STATUS values('ACT','Activo',to_date ('21-04-2020','DD-MM-YYYY '),to_date ('21-04-2020','DD-MM-YYYY'),0);--active
insert into PRODUCT_STATUS values('INA','Inactivo',to_date ('21-04-2020','DD-MM-YYYY '),to_date ('21-04-2020','DD-MM-YYYY'),0);--unactive

--insert tabla UNIT
INSERT INTO UNIT (abbreviation, name, plural_name,created_at,updated_at,deleted) 
VALUES(
'Lt',--abbreviation
'Litro',--name
'Litros',--plural_name
to_date ('24-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
to_date ('24-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
0--deleted
);

INSERT INTO UNIT (abbreviation, name, plural_name,created_at,updated_at,deleted) 
VALUES(
'Uni',--abbreviation
'Unidad',--name
'Unidades',--plural_name
to_date ('24-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
to_date ('24-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
0--deleted
);

INSERT INTO UNIT (abbreviation, name, plural_name,created_at,updated_at,deleted) 
VALUES(
'Kg',--abbreviation
'Kilo',--name
'Kilos',--plural_name
to_date ('24-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at
to_date ('24-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at
0--deleted
);

--insert tabla PRODUCT
INSERT INTO PRODUCT (product_id,name,product_desc,price,stock,stock_alert,brand,unit_id,product_status,created_at,updated_at,deleted)
VALUES(
'D9CCD278B8B849788F1F4FF6E074B499',--product_id	        
'Aceite de motor CASTROL EDGE',--name                
'Castrol EDGE es un aceite para motor totalmente sintético desarrollado para conductores que solo quieren lo mejor de sus motores.',--product_desc        
15000,--price
15,--stock 
5,--stock_alert
'CASTROL EDGE',--brand               
'Lt',--unit_id             
'ACT',--product_status      
to_date ('21-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at           
to_date ('21-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at           
0--deleted 
);


INSERT INTO PRODUCT (product_id,name,product_desc,price,stock,stock_alert,brand,unit_id,product_status,created_at,updated_at,deleted)
VALUES(
'BB5801A8406F49CC953A277F918E770D',--product_id	        
'Neumáticos TURANZA ER33',--name                
'A los coches de lujo les vendría como anillo al dedo el Bridgestone ER33, un neumático creado especialmente para este tipo de automáviles gracias a su alto rendimiento.',--product_desc        
35000,--price
20,--stock   
12,--stock_alert            
'BRIDGESTONE',--brand               
'Uni',--unit_id             
'ACT',--product_status      
to_date ('24-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at           
to_date ('24-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at           
0--deleted 
);

INSERT INTO PRODUCT (product_id,name,product_desc,price,stock,stock_alert,brand,unit_id,product_status,created_at,updated_at,deleted)
VALUES(
'6CFE0D33D63A40ECB90BBBCA50E6E6EB',--product_id	        
'Correa de Distribucion ( 102R 25mm.)',--name                
'Las correas tienen por función transmitir fuerza generada por motor hacia aquellos componentes que la necesiten, por lo tanto el correcto funcionamiento de los componentes depende del buen estado de las correas.',--product_desc        
25000,--price
25,--stock   
10,--stock_alert            
'Continental',--brand               
'Uni',--unit_id             
'ACT',--product_status      
to_date ('24-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at           
to_date ('24-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at           
0--deleted 
);

INSERT INTO PRODUCT (product_id,name,product_desc,price,stock,stock_alert,brand,unit_id,product_status,created_at,updated_at,deleted)
VALUES(
'C4D20AB8F1114202A98156BFF9C6521E',--product_id	        
'Ampolleta Led Auto Cob 12000 Lm R11 Ml0847',--name                
'LED luz de faro de sueño, uso COB LED chip, poca superficie iluminada pero máxima eficiencia de la luz, mejor penetración y condensador más fuerte, impermeable y resistente al polvo, todo en uno, enchufar a juego, radiador de calor inteligente, inicio instantaneo.',--product_desc        
9000,--price
30,--stock   
15,--stock_alert            
'Auto Cob',--brand               
'Uni',--unit_id             
'ACT',--product_status      
to_date ('24-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at           
to_date ('24-04-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at           
0--deleted 
);
--producto Product_1(1 x ACT - deleted 1)
INSERT INTO PRODUCT (product_id,name,product_desc,price,stock,stock_alert,brand,unit_id,product_status,created_at,updated_at,deleted)
VALUES(
'031B59CCC04D42E283D509FA19D3A540',--product_id	        
'Product_1',--name                
'Producto 1 de prueba para Initial-D',--product_desc        
13000,--price
20,--stock   
5,--stock_alert            
'Testing',--brand               
'Uni',--unit_id             
'ACT',--product_status      
to_date ('12-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--create_at           
to_date ('12-06-2020 11:00 AM','DD-MM-YYYY HH:MI AM'),--update_at           
1--deleted 
);


--producto Product_2(1 x INA - deleted 1)
INSERT INTO PRODUCT (product_id,name,product_desc,price,stock,stock_alert,brand,unit_id,product_status,created_at,updated_at,deleted)
VALUES(
'22A778C4892D4E898325E2BD67060ED2',--product_id	        
'Product_1',--name                
'Producto 1 de prueba para Initial-D',--product_desc        
33000,--price
10,--stock   
4,--stock_alert            
'Testing_2',--brand               
'Kg',--unit_id             
'INA',--product_status      
to_date ('12-06-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--create_at           
to_date ('12-06-2020 12:00 AM','DD-MM-YYYY HH:MI AM'),--update_at           
1--deleted 
);

--producto Product_3(1 x INA - deleted 0)
INSERT INTO PRODUCT (product_id,name,product_desc,price,stock,stock_alert,brand,unit_id,product_status,created_at,updated_at,deleted)
VALUES(
'6EF98D4B1E4E42E7A345B70C3A1674B5',--product_id	        
'Product_1',--name                
'Producto 1 de prueba para Initial-D',--product_desc        
6000,--price
40,--stock   
15,--stock_alert            
'Testing_3',--brand               
'Lt',--unit_id             
'INA',--product_status      
to_date ('12-06-2020 10:00 AM','DD-MM-YYYY HH:MI AM'),--create_at           
to_date ('12-06-2020 10:00 AM','DD-MM-YYYY HH:MI AM'),--update_at           
0--deleted 
);

commit;