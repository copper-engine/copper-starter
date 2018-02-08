FROM java:8u111-jdk

WORKDIR /app
RUN mkdir -p /app
COPY copper-starter.zip /app
RUN unzip copper-starter.zip

WORKDIR /app/copper-starter
EXPOSE 1099

RUN echo '#!/bin/bash\njava -cp /app/copper-starter/lib/copper-starter.jar org.copperengine.examples.orchestration.simulators.clients.OrchestrationServiceTestClient "http://localhost:9092/services/orchestration?wsdl" "491716677889" "sc00p" "100"' > /usr/bin/genBrokenWF && \
    chmod +x /usr/bin/genBrokenWF
ENV COPPER_STARTER_OPTS "-Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false"
CMD ["./bin/copper-starter"]