PhenoFront
==========

LemnaTec front end.


This software package is designed for loading data from the LemnaTec database and in the future potentially contain data
analysis packages. 

The tool is built with the Spring 3.2 MVC framework.

Tests
==========
There is a set of Tests that go with this. They include unit tests and lightweight integration testing.


Building
==========
To build the database connector in spring-context.xml and in spring-security.xml need to be configured. SpringContext
should go to the LemnaTec database and SpringSecurity should go to the associated MySQL database. An SQL file for each
database will be provided, the LemnaTec.sql file is only useful for testing configuration. The MySQL file is required.


Future Work
===========
Database needs to load the images, they are stored as raw files on the data server, they will need to be retrieved somehow
and then converted into a compressed format. 
