PhenoFront System Outline
==========
PhenoFront is a web-server front end to the LemnaTec Phenotyper platform.

As currently implemented, it allows for flexible large-scale data downloads from the LemnaTec database.
However it also serves as a base for extended functionality, such as being able to pass commands to the Phenotyper and metadata tracking in complement to the LemnaTec system.

A secondary (MySQL) database contains:
	User profiles and authentication credentials
	Metadata tags associated with user queries

The experiment data and related plant snapshots are pulled directly from the LemnaTec database.

The tool is built using the Spring 3.2 MVC Framework.



Building (For Production Use)
==========
Requirements and Recommendations:
For building and source editing, we recommend the Spring Tools Suite (http://spring.io/tools). The build instructions will be for STS.
To host the user profile database, either install a MySQL server, or ensure you have access and permission to add a database to one.

Instructions:
1) Download the source code and import it into a Spring 3.1 compatible IDE
	File>Import>File System and navigate to the root folder of wherever the downloaded project is located

2) Install or acquire access to a MySQL server (and ensure you have permission to add a database to it). Seed the user profile database with the file "authentication_and_metadata_seed.sql".
	From the workbench create a new connection (keeping track of the hostname, port, username, password, and connection name)
	Open the SQL editor on the new connection
	Open the file authentication_and_metadata_seed.sql
	Run the script (Query>Execute All)
	
NOTE: The default user added to the database is the user "admin" with the password "password". Change the password as soon as you get access to the server.

3) Before building the server, ensure the database connection configuration files in the server's classpath are filled in appropriately.
There are example files called "ltdatabase-example.conf" and "userdatabase-example.conf" that can be searched for to find the appropriate directory.
	ltdatabase.conf		LemnaTec Phenotyper database
	userdatabase.conf	User profile, authentication, metadata database (the MySQL database installed in (2))

NOTE: The classpath is in "<INSTALLED DIRECTORY>\src\main\webapp\WEB-INF\classes"

4) Now simply build the software on your favorite server and remember to change the default admin's password after logging into PhenoFront.



Building (For Development Use)
==========
To build for developing, do all the same steps as above, except instead of pointing ltdatabase.conf to the LemnaTec Phenotyper database, we'll make our own PostgreSQL test server.

Install or acquire access to a recent version of PostgreSQL (and ensure you have permission to add a database to it). Seed the fake LemnaTec database with the files "LTsystem_development.sql" and "lemnatest_development_seed.sql".

Ensure the configuration file "ltdatabase.conf" points to the development database just installed.

Now build.



Future Work
===========
Integration with the watering instruction tool.
Tracking user downloads to avoid repeated mass-downloads by a single user-group

