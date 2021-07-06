# time-queue

Super simple time queue scheduler (time event notification)

# why?

A few times in the past Google Calender has left me hanging... this is my personal backup.

# setup a deployment

Every deployment is configured for every required source code branch (e.g. `main`).

Change `branch` by the branch name.

## Create database

```sql
# CREATE USER "timeq-branch-user" WITH PASSWORD 'whatever you want';
# CREATE DATABASE "timeq-branch-db" WITH OWNER "timeq-branch-user";
# GRANT CONNECT ON DATABASE "timeq-branch-db" TO "timeq-branch-user";
```

