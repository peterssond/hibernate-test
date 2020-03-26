# hibernate-test

This project illustrates the problem with parent-child insert order introduced in hibernate-core 5.4.

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
In the working exemple that uses hibernate-core:5.3.15.Final we can see that the parent is inserted before the child, and therefore honours the foreign key constraint.
```
org.hibernate.SQL : select parent0_.parent_id as parent_i1_1_1_, child1_.parent_id as parent_i1_0_0_ from parent parent0_ left outer join child child1_ on parent0_.parent_id=child1_.parent_id where parent0_.parent_id=?
org.hibernate.SQL : select child0_.parent_id as parent_i1_0_0_ from child child0_ where child0_.parent_id=?
org.hibernate.SQL : insert into parent (parent_id) values (?)
org.hibernate.SQL : insert into child (parent_id) values (?)

```

The non-working hibernate-core:5.4.12.Final example shows that this insert order is reversed and therefore violates this foreign key constraint.
```
org.hibernate.SQL                        : select parent0_.parent_id as parent_i1_1_0_ from parent parent0_ where parent0_.parent_id=?
org.hibernate.SQL                        : select child0_.parent_id as parent_i1_0_0_ from child child0_ where child0_.parent_id=?
org.hibernate.SQL                        : insert into child (parent_id) values (?)
o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 23506, SQLState: 23506
o.h.engine.jdbc.spi.SqlExceptionHelper   : Referential integrity constraint violation: "CHILD_PARENT_FK: PUBLIC.CHILD FOREIGN KEY(PARENT_ID) REFERENCES PUBLIC.PARENT(PARENT_ID) ('f5ee1981-faf4-4a06-be08-302d4ca1316e')"; SQL statement: insert into child (parent_id) values (?) [23506-200]

```
