# hibernate-test

This project illustrates the problem with entityA-entityB insert order introduced in hibernate-core 5.4.

Project runs with Java 11.

## Usage

Clone project

Run the application with the following to get a working example (uses hibernate-core:5.3.15.Final)
```
./gradlew -Dhibernate.5_3=true bootRun
```

Run the application with the following to get a non working example (uses hibernate-core:5.4.12.Final)
```
./gradlew -Dhibernate.5_3=false bootRun
```

## Differences
In the working exemple that uses hibernate-core:5.3.15.Final we can see that entityA is inserted before entityB, and therefore honours the foreign key constraint.
```
org.hibernate.SQL : select entitya0_.a_id as a_id1_0_1_, entityb1_.a_id as a_id1_1_0_ from entitya entitya0_ left outer join entityb entityb1_ on entitya0_.a_id=entityb1_.a_id where entitya0_.a_id=?
org.hibernate.SQL : select entityb0_.a_id as a_id1_1_0_ from entityb entityb0_ where entityb0_.a_id=?
org.hibernate.SQL : insert into entitya (a_id) values (?)
org.hibernate.SQL : insert into entityb (a_id) values (?)
```

The non-working hibernate-core:5.4.12.Final example shows that this insert order is reversed and therefore violates this foreign key constraint.
```
org.hibernate.SQL                        : select entitya0_.a_id as a_id1_0_0_ from entitya entitya0_ where entitya0_.a_id=?
org.hibernate.SQL                        : select entityb0_.a_id as a_id1_1_0_ from entityb entityb0_ where entityb0_.a_id=?
org.hibernate.SQL                        : insert into entityb (a_id) values (?)
o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 23506, SQLState: 23506
o.h.engine.jdbc.spi.SqlExceptionHelper   : Referential integrity constraint violation: "A_B_FK: PUBLIC.ENTITYB FOREIGN KEY(A_ID) REFERENCES PUBLIC.ENTITYA(A_ID) ('a0976f37-c16c-4012-b2ee-223b7482fab7')"; SQL statement: insert into entityb (a_id) values (?) [23506-200]
```
