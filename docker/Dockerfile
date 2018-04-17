FROM java:8u111-jdk

WORKDIR /app
RUN mkdir -p /app

RUN apt-get update && apt-get -y install cron
ADD broken-wf-cron /etc/cron.d/broken-wf-cron
RUN chmod 0644 /etc/cron.d/broken-wf-cron
RUN touch /var/log/cron.log

ADD genBrokenWF /usr/bin/genBrokenWF
RUN chmod +x /usr/bin/genBrokenWF

COPY copper-starter.zip /app
RUN unzip copper-starter.zip

WORKDIR /app/copper-starter
EXPOSE 1099


ENV COPPER_STARTER_OPTS "-Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false"

CMD cron && ./bin/copper-starter
