# ElasticStack

---

## Tech

- logstash: consume data from kafka and load to elasticsearch
- elasticsearch: search engine
- kibana: data visualization with elasticsearch

### Features

### Pros

### Cons


---
## Demonstration

---
## Architecture


---
## [Installation](https://www.elastic.co/kr/elastic-stack/)

Enterprise or AWS elasticsearch를 사용하지 않고 도커 혹은 직접 설치하여 구성한다.

### [Logstash](./logstash/logstash-kafka.conf)

Docker
- https://www.elastic.co/guide/en/logstash/current/docker-config.html

직접 설치
-   `bin/logstash -f config/logstash-kafka.conf`
    - Input: Kafka `short-link.json` topic
    - Output: Elasticsearch data node

### ElasticSearch

### Kibana

---

## TroubleShoot

- 버전별 security 설정이 변함
  - 참고: https://www.elastic.co/guide/en/kibana/current/xpack-security.html
  - https://levelup.gitconnected.com/how-to-run-elasticsearch-8-on-docker-for-local-development-401fd3fff829
- Kafka,Logstash 연동
  - 참고: https://zkdlu.github.io/2021-01-22/ELK03-ELK/
  
