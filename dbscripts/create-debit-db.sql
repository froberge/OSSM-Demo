DROP TABLE IF EXISTS "transaction";

CREATE TABLE "transaction"(
  ID   SERIAL,
  CLIENT_ID VARCHAR(20) NOT NULL,
  TYPE VARCHAR(10)      NOT NULL,
  LOCATION VARCHAR(50)  NOT NULL,
  AMOUNT FLOAT8         NOT NULL,
  CREATE_DATE DATE,
  PRIMARY KEY (ID)
);

SET default_tablespace = '';
SET client_encoding = 'UTF8';
