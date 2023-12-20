1) C:\Users\vkopp\postgres>docker run --name postgresql -e POSTGRES_USER=vkoppara -e POSTGRES_PASSWORD=password -p 5432:5432 -v /c/Users/vkopp/postgres/data:/var/lib/postgresql/data -d postgres
2) change the file in C:\Users\vkopp\postgres\data\postgresql.conf 
under: # WRITE-AHEAD LOG

wal_level = logical
3) then restart postgres docker instance
4) create table customer (id int primary key, first_name varchar(256), last_name varchar(256), email varchar(256) )
5) hazelcast installation as in https://docs.hazelcast.com/hazelcast/5.3/getting-started/get-started-docker
6) start the spring boot application.
7) Insert records into the customer table:
insert into customer values(3, 'firstname-1', 'lastname','email@gmail.com')
8) use http://localhost:8080 to see the inserted records are appearing.
9) Also observe in the logs, the moment the records are inserted, it will update the cache too. 
 
   