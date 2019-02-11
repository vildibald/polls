## This is a reimplementation of [Polls app backend](https://github.com/callicoder/spring-security-react-ant-design-polls-app) in Kotlin using PostgreSQL database. 

## Steps to Setup the Spring Boot Back end app (polling-app-server)

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
	
	# Othervise on Windows machine run
	gradlew.bat bootRun
	
	# Othervise on Linux machine run
	./gradlew bootRun
	```
	The server will start on port 8080.
	
5. **Default Roles**
	
	The spring boot app uses role based authorization powered by spring security. To add the default roles in the database, run the following commands or use graphical tool like PgAdmin or DBeaver.

    ```bash
	psql polling_app hramsa
	```
    Whether running from console or graphical tool, run these SQL statements. 

	```sql
	INSERT INTO public.roles(name) VALUES('ROLE_USER');
	INSERT INTO public.roles(name) VALUES('ROLE_ADMIN');
	```

	Any new user who signs up to the app is assigned the `ROLE_USER` by default.
   
