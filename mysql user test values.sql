USE PhenoFront;

DESCRIBE users;

SELECT * FROM users;

INSERT  INTO users(USER_ID, USERNAME, PASSWORD, ENABLED, GROUP_ID, AUTHORITY)
VALUES(1, "cjmcentee", "cjmcenteepassword", 1, 1, "admin");

INSERT  INTO users(USER_ID, USERNAME, PASSWORD, ENABLED, GROUP_ID, AUTHORITY)
VALUES(3, "doug", "dougpassword", 1, 2, "admin");

INSERT  INTO users(USER_ID, USERNAME, PASSWORD, ENABLED, GROUP_ID, AUTHORITY)
VALUES(5, "steven", "stevenpassword", 1, 1, "admin");

INSERT  INTO users(USER_ID, USERNAME, PASSWORD, ENABLED, GROUP_ID, AUTHORITY)
VALUES(4, "johnsmith", "johnsmithpassword", 1, 3, "user");





DESCRIBE groups;

SELECT * FROM groups;

INSERT INTO groups(group_id, owner_id, group_name)
VALUES(2, 2, "test group");