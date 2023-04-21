-- 
--  Author:  Anya
--  Created: 4 Mar 2023
-- 
--  Spring Boot will automatically pick up this file and run it against an embedded 
--  in-memory database, such as our configured H2 instance. 

CREATE TABLE todolists (
    id INT NOT NULL PRIMARY KEY, 
    item VARCHAR(32) NOT NULL DEFAULT NULL, 
    status VARCHAR(10) NOT NULL
);
