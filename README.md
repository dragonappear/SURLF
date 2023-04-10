# Tiny Url API and Statistics System

## Description

This is a tiny url system design project that accommodates 1 billion requests a day.

### Demonstration

---
## Tech

To see details on each layer, click on

- Web Server: [Nginx with Vue.js3 ](./nginx/README.md)
- WAS: [Java](./java/README.md)
- Caching: Redis
- Data Clustering: [Kafka](./kafka/README.md), [ElasticStack](./elasticstack/README.md)
- Infra: ECS

---
## System Architecture


---
## Assumption

---
## How to build and run project

You must install npm, java and gradlew

1. `docker-compose up -d`
2. `cd java && ./gradlew clean build && java -jar *.jar`
3. `cd nginx && npm install &&npm run dev`
4. enter -> localhost:5173


---

## DB

RDBMS: mariaDB

`ddl`
```sql
create table short_link
(
    id_short_link bigint auto_increment
        primary key,
    created_at    datetime(6)  not null,
    original_url  text         not null,
    short_id      varchar(255) not null,
    client_ip     varchar(255) not null,
    user_agent    varchar(255) null,
    constraint uk_short_id
        unique (short_id)
);

create index idx_client_info
    on short_link (user_agent, client_ip);
```


