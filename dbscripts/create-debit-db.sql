DROP TABLE IF EXISTS "transaction";
DROP SEQUENCE IF EXISTS "transaction_seq";

CREATE SEQUENCE "transaction_seq"
  START WITH     1
  INCREMENT BY   1;

CREATE TABLE "transaction"(
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
