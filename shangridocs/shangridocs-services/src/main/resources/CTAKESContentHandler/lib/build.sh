CLASSPATH=""
for f in $(ls ${CTAKES_HOME}/lib/*.jar); do
    if [[ $f != *"cxf"* ]]
    then
        CLASSPATH+=$f
        CLASSPATH+=":"
    fi
done
export TIKA_HOME=/data/ducc/shangridocs/tika/ctakes/tika
CLASSPATH="/data/ducc/ctakes-config:$(ls ${TIKA_HOME}/tika-server/target/tika-server-*.jar | grep -v "tests"):${CTAKES_HOME}/desc:${CTAKES_HOME}/resources:${CLASSPATH}"
export CLASSPATH
echo "Using classpath: ${CLASSPATH}"
javac -cp ${CLASSPATH} -d $(dirname $0) $(dirname $0)/../src/org/apache/tika/sax/* $(dirname $0)/../src/CTAKESContentToMetadataHandler.java
