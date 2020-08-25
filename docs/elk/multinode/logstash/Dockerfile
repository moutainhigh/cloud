FROM logstash:6.8.6

RUN rm -f       /usr/share/logstash/pipeline/logstash.conf
ADD pipeline/   /usr/share/logstash/pipeline/
ADD config/     /usr/share/logstash/config/