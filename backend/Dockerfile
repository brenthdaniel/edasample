FROM icr.io/appcafe/open-liberty:full-java11-openj9-ubi

COPY --chown=1001:0 src/main/liberty/config /config/
COPY --chown=1001:0 target/liberty/wlp/usr/shared/resources/derby-10.14.2.0.jar /opt/ol/wlp/usr/shared/resources
COPY --chown=1001:0 target/beer.war /config/apps
RUN configure.sh
