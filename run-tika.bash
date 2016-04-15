#!/bin/bash
export TIKA_HOME=$(dirname ${0})/tika/
CLASSPATH="$(ls ${TIKA_HOME}/tika-server/target/tika-server-*.jar | grep -v "tests")"
export CLASSPATH
java -classpath ${CLASSPATH} org.apache.tika.server.TikaServerCli --port 9998 1>./tika-server.log 2>&1 &
