#!/bin/sh

export HADOOP_USER_NAME="jupyter"

jar_name=creditscore-1.0.SNAPSHOT.jar
MAIN_CLASS=vn.cicdata.encrypt.CDREncrypt


this="${BASH_SOURCE-$0}"
while [ -h "$this" ]; do
    ls=`ls -ld "$this"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '.*/.*' > /dev/null; then
	this="$link"
    else
	this=`dirname "$this"`/"$link"
    fi
done

# convert relative path to absolute path
bin=`dirname "$this"`
script=`basename "$this"`
bin=`cd "$bin">/dev/null; pwd`
this="$bin/$script"
cd $bin/..






export SPARK_2=/usr/bin/spark-submit

#--jars ${JARS_PATH} \
#--jars $(echo lib/*.jar | tr ' ', ',') \

${SPARK_2} \
--files $(echo config/* | tr ' ', ',') \
--class ${MAIN_CLASS}  \
--master yarn-cluster  \
 ${jar_name}

