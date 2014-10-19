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

3) Before building the server, ensure the database connection configuration files in the server's classpath (\src\main\webapp\WEB-INF\classes) are filled in appropriately.
There are example files called "ltdatabase-example.conf" and "userdatabase-example.conf" that can be searched for to find the appropriate directory.
	ltdatabase.conf		LemnaTec Phenotyper database
	userdatabase.conf	User profile, authentication, metadata database (the MySQL database installed in (2))

NOTE: The classpath is in "<INSTALLED DIRECTORY>\src\main\webapp\WEB-INF\classes"

4) Now the server should build on your favorite server and remember to change the default admin's password after logging into PhenoFront.

NOTE: Prior to building the server, run the tests first. If you are building with Maven the tests should automatically run and the build aborted if the tests fail. However, if this is a development build in an IDE, run the JUnit tests. For more information on this see the section "Troubleshooting Setup Problems".



Building (For Development Use)
==========
To build for developing, do all the same steps as above, except instead of pointing ltdatabase.conf to the LemnaTec Phenotyper database, we'll make our own PostgreSQL test server.

Install or acquire access to a recent version of PostgreSQL (and ensure you have permission to add a database to it). Seed the fake LemnaTec database with the files "LTsystem_development.sql" and "lemnatest_development_seed.sql".

Using a standard Postgres install, with psql in the command line:

...\PhenoFront> psql -f LTsystem_development.sql -U <USERNAME>
...\PhenoFront> psql -f lemnatest_development_seed.sql -U <USERNAME>

Ensure the configuration file "ltdatabase.conf" points to the development database just installed.

Now build.


Troubleshooting Setup Problems
==========
Before compiling and running a server, run all the tests using JUnit 4. In Spring Tools Suite this amounts to:
	Run > Run Configurations... > JUnit (left panel) > New (icon in top left)
Select "Run all tests in selected project, package or source folder..."
Make sure JUnit 4 is the selected "Test Runner"
Click Apply
Click Run

If all the tests fail, this is likely because of a malformed configuration file (see: Building For Production Use (3)). Ensure the configuration files are formatted correctly. The following can be deduces from failure states of the test:

In the test class: LemnaTecServerConnectionTest
If			All fail							
Then		Config file (ltdatabase.config) is formatted incorrectly

If			Only LemnaTecDatabaseConfigurationFileConfiguredCorrectly passes
Then		The config file's connection settings are incorrect

If			Only LemnaTecDatabaseConfigurationFileConfiguredCorrectly AND LemnaTecDatabaseHasExperiments pass
Then		The LTSystem database exists, but the databases it references does not. In a production system this should never happen. In a development environment, see the method LemnaTecServerConnectionTest#GetDatabaseNames() for how to see which databases are listed by the LTSystem database that don't also exist in the Postgres system. Either find a script that adds the necessary databases, or modify the LTSystem database record.

Otherwise	All tests should pass. Any other failures represent a malformed database structure in the LemnaTec Postgres environment. In a development environment, this is probably called by an error during seeing from the step "Building (For Development Use)" above. In a production environment, debug the tests and see where exactly it fails. Then look at the live system to see what's gone wrong. You might have to call LemnaTec support.

The following classes can all be debugged in a very similar way as to the above LemnaTecServerConnectionTest troubleshooting:
	LemnaTecServerConnectionTest		- ltdatabase.config
	QueryServerConnectionTest 			- userdatabase.config
	TaggingServerConnectionTest			- userdatabase.config
	UserProfileServerConnectionTest		- userdatabase.config

All the above have a similar construction with analog test methods.


Future Work
===========
Integration with the watering instruction tool.
Tracking user downloads to avoid repeated mass-downloads by a single user-group

