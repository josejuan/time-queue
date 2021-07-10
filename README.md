# time-queue

Super simple time queue scheduler (time event notification)

# why?

A few times in the past Google Calender has left me hanging... this is my personal backup.

# notifications

Every alarm is send to:

1. TODO mail
1. TODO https://betterprogramming.pub/how-to-send-push-notifications-to-your-phone-from-any-script-6b70e34748f6

# Setup (deploy) environments

You can deploy many environments as you want easilly.

Every environment is configured for every required source code branch (e.g. `main`).

The **environment name** will be the **branch name**.

The **environment domain name** will be the **branch name** as subdomain.

(In my case I add `-tq` as `https://{branch}-tq.{www_org_name}/` but no matter).

(In the following, change `branch` by the branch name)

## Configure your github secrets

A single time for all environments.

1. **DEV_DATABASE_USR**, database user.
1. **DEV_DATABASE_PWD**, database password.
1. **DOCKER_HUB_USR**, dockerhub image repository user.
1. **DOCKER_HUB_PWD**, dockerhub image repository password.
1. **K8S_SSH_HOST**, kubernetes bastion (configured client) machine.
1. **K8S_SSH_KEY**, private user ssh key.
1. **K8S_SSH_USER**, ssh user.
1. **WWW_ORG_NAME**, your main hosting domain name.

## Create database

Once per every new environment (`master`, `www`, `testing`, `benchmark`, ...).

```sql
# CREATE USER "timeq-branch-user" WITH PASSWORD 'whatever you want';
# CREATE DATABASE "timeq-branch-db" WITH OWNER "timeq-branch-user";
# GRANT CONNECT ON DATABASE "timeq-branch-db" TO "timeq-branch-user";
```

## Create associated branch

Once per every new environment.

```shell script
$ git checkout -b branch
```

## Update all deploy infraestructure

Every time do you want update/deploy one environment.

```shell script
$ git checkout branch
$ git merge whatever-branch-to-take-changes
$ git push
```