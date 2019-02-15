
## Steps to Setup the Spring Boot back end app

1. **Clone the application**

	```bash
	git clone https://github.com/vildibald/polls.git
	cd polls
	```

2. **Create PostgreSQL database**

	```sql
	CREATE DATABASE polling_app;
	CREATE USER hramsa WITH LOGIN PASSWORD 'hramsa';
	```

4. **Run the app**

	You can run the spring boot app by typing the following command.

	```bash
	# If gradle is already installed on your machine, then run 
	gradle bootRun
	
	# Otherwise on Windows machine run
	gradlew.bat bootRun
	
	# Otherwise on Linux machine run
	./gradlew bootRun
	```
	The server will start on port 8080.

5. **Default Roles**
	
	The spring boot app uses role based authorization powered by spring security. To add the default roles in the database, I have added the following sql queries in `src/main/resources/data-hsqldb.sql` file. Spring boot will automatically execute this script on startup

	```sql
	INSERT INTO public.roles(id, name)
    VALUES (1, 'ROLE_USER'),
           (2, 'ROLE_ADMIN')
    ON CONFLICT DO NOTHING;
	```

	Any new user who signs up to the app is assigned the `ROLE_USER` by default.
	
6. **(Optional) Integration tests**

    You may run integration tests explicitly using defined task
    
    ```bash
    gradle integrationTest
    ```

    Note that the unit and integration tests are separated using Spring profiles, therefore
    integration tests will not be performed during gradle's standard `build` or `test` tasks, 
    which is in general a preferred solution.
    
    Also integration tests use an embedded in-memory HSQLDB instead of PostgreSQL.  
   
