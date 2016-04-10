CLASSPATH=""
for f in $(ls ${CTAKES_HOME}/lib/*.jar); do
    if [[ $f != *"cxf"* ]]
    then
        CLASSPATH+=$f
        CLASSPATH+=":"
    fi
done
export TIKA_HOME=/data/ducc/shangridocs/tika/ctakes/tika
CLASSPATH="/data/ducc/ctakes-config:$(ls ${TIKA_HOME}/tika-server/target/tika-server-*.jar | grep -v "tests"):${CTAKES_HOME}/desc:${CTAKES_HOME}/resources:${CLASSPATH}:$(dirname $0)"
export CLASSPATH
java -cp ${CLASSPATH} CTAKESContentToMetadataHandler $*
