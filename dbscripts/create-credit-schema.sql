DROP TABLE IF EXISTS cs_credit."transaction";
DROP SEQUENCE IF EXISTS cs_credit."transaction_seq";
DROP SCHEMA IF EXISTS cs_credit;

CREATE SCHEMA cs_credit;

CREATE SEQUENCE cs_credit."transaction_seq"
  START WITH     1
  INCREMENT BY   1;

CREATE TABLE cs_credit."transaction"(
  ID   INT              NOT NULL,
  CLIENT_ID VARCHAR(20) NOT NULL,
  TYPE VARCHAR(10)      NOT NULL,
  LOCATION VARCHAR(50)  NOT NULL,
  AMOUNT FLOAT8         NOT NULL,
  CREATE_DATE DATE,
  PRIMARY KEY (ID)
);

SET default_tablespace = '';
SET client_encoding = 'UTF8';
