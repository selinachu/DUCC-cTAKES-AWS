# ShangriDocs-DUCC-cTAKES

##Setting up DUCC
Before installing DUCC, create user ducc and enable passwordless ssh for user ducc

Then to Install DUCC

Download the binary file to /home/ducc
http://apache.cs.utah.edu//uima//uima-ducc-2.0.1/uima-ducc-2.0.1-bin.tar.gz

from /home/ducc
$ tar -xvzf uima-ducc-2.0.1-bin.tar.gz

###Configure DUCC
$ cd apache-uima-ducc-2.0.1/admin/

$ ./ducc_post_install

ducc_post_install runs the script that sets up the default configuration in ducc.properties

The default configuration file is here:

/home/ducc/apache-uima-ducc-2.0.1/resources/default.ducc.properties

Note: For advanced DUCC user, this default configuration file can be manually modified.  After changes are made, run the script apache-uima-ducc-2.0.1/admin/ducc_post_install again.

####DUCC Documentation
https://uima.apache.org/d/uima-ducc-2.0.0/duccbook.html

####Quick start tutorial
https://cwiki.apache.org/confluence/display/UIMA/DUCC

Starting DUCC
From /home/ducc/apache-uima-ducc-2.0.1/admin
$ ./start_ducc

To check status
From /home/ducc/apache-uima-ducc-2.0.1/admin
$ ./check_ducc

Web interface to monitor DUCC can be accessed via a browser

http://[DUCC hostname]:42133/system.daemons.jsp

http://[DUCC hostname]:42133/jobs.jsp

To test DUCC, submit a simple example job via command line

$ /home/ducc/apache-uima-ducc-2.0.1/bin/ducc_submit -f /Users/ducc/apache-uima-ducc-2.0.1/examples/simple/1.job

note: Wait at least a minute, after starting DUCC before submitting any jobs. It takes DUCC at least a minute before all initialization are done.

To stop DUCC completely
$ /home/ducc/apache-uima-ducc-2.0.1/admin/stop_ducc -a



###Setting up DUCC with cTAKES and Shangridocs

Replace the /home/ducc/apache-uima-ducc-2.0.1/resources/default.ducc.properties with current in this github repository

Then, run the script /home/ducc/apache-uima-ducc-2.0.1/admin/ducc_post_install again


##Setting up cTAKES 

You can follow the instructions in https://wiki.apache.org/tika/cTAKESParser for “Installing cTAKES”.

We DO NOT need Tika working with cTAKE. Thus, installing the cTAKESParser is unnecessary for this version of ShangriDocs


Summarizing the set up of cTAKES

The binary file can be downloaded from: http://www-us.apache.org/dist/ctakes/ctakes-3.2.2/apache-ctakes-3.2.2-bin.tar.gz

If this site doesn’t work, other mirror sites, go to http://ctakes.apache.org/downloads.cgi, go to bottom of page “Current Download Mirror:”

Under /home/ducc/

$ mkdir ctakes

$ cd ctakes

$ curl -O http://www-us.apache.org/dist/ctakes/ctakes-3.2.2/apache-ctakes-3.2.2-bin.tar.gz

$ tar -xvzf apache-ctakes-3.2.2-bin.tar.gz

$ curl -Lo ctakes-resources-3.2.1.1-bin.zip "http://downloads.sourceforge.net/project/ctakesresources/ctakes-resources-3.2.1.1-bin.zip?r=http%3A%2F%2Fsourceforge.net%2Fprojects%2Fctakesresources%2F%3Fsource%3Dtyp_redirect&ts=1433609725&use_mirror=softlayer-dal"

$ mv ctakes-resources-3.2.1.1-bin.zip /home/ducc/ctakes/apache-ctakes-3.2.2

$ cd /home/ducc/ctakes/apache-ctakes-3.2.2

$ unzip ctakes-resources-3.2.1.1-bin.zip


###Obtain UMLS license

UMLS license (username and password) can be obtain from: https://uts.nlm.nih.gov//license.html

This takes 2-3 days to processed.


###Configuring cTAKES for DUCC

From ${CTAKES_HOME}/desc/

Changed descriptor files that has a "multipleDeploymentAllowed" tag from “false” to “true”.  

One way is to: perform a search under ${CTAKES_HOME}/desc/ for all descriptor files with "multipleDeploymentAllowed" which are false and modify them.  

Replace the ${CTAKES_HOME}/desc/ctakes-core/desc/collection_reader/FilesInDirectoryCollectionReader.xml in this github repository


##Setting up ShangriDocs

ShangriDocs’s main site is at https://github.com/chrismattmann/shangridocs
Prior configuration DUCC with cTAKES+ShangriDocs can be found on: https://github.com/yiwenliuable/ctakes-scale-out-with-uima-ducc

For convenience, the codes of the current version of ShangriDocs on AWS is at https://github.com/selinachu/DUCC-cTAKES-AWS.git

For convenience,

From /home/ducc/

$ git clone https://github.com/selinachu/DUCC-cTAKES-AWS.git


Add UMLS username and password to CTAKESConfig.properties

/Users/ducc/shangridocs/shangridocs-services/src/main/resources/CTAKESContentHandler/config/org/apache/tika/sax/CTAKESConfig.properties

Also, need to add UMLS username and password to shell variables

export ctakes_umlsuser=‘username’

export ctakes_umlspw=‘password’


Set these environment variables

export DUCC_HOME=[path]	

export CTAKES_HOME=[path]

export SHANGRIDOCS_HOME=[path]

export TIKA_HOME=[path]

If following the instructions here, then the paths would be 

export DUCC_HOME=“/home/ducc/apache-uima-ducc-2.0.1“

export CTAKES_HOME=“/home/ducc/ctakes/apache-ctakes-3.2.2”

export SHANGRIDOCS_HOME=“/home/ducc/DUCC-cTAKES-AWS/shangridocs”

export TIKA_HOME=“/home/ducc/DUCC-cTAKES-AWS/shangridocs/tika”

###Setting up Tika Server 

(If not copying from from https://github.com/selinachu/DUCC-cTAKES-AWS.git)

https://github.com/chrismattmann/shangridocs#apache-tika-server
Note: For this version of Shangridocs, one do not need a ctakes-tika server. This can be accomplished by skipping step 3 (or ignoring ‘mkdir -p tika/ctakes’) from https://github.com/chrismattmann/shangridocs#apache-tika-server

Summarizing instructions to set up Tika server

$ cd /home/ducc/DUCC-cTAKES-AWS/shangridocs 

$ git clone https://github.com/apache/tika.git

$ cd tika

To start Tika server

$ java -jar tika-server/target/tika-server-1.11-SNAPSHOT.jar > ../tika-server.log 2>&1&



##To start ShangriDocs

$ cd /home/ducc/apache-uima-ducc-2.0./admin

$ ./start_ducc

$ cd /home/ducc/DUCC-cTAKES-AWS/shangridocs/tika

$ ./run.bash

$ cd /home/ducc/DUCC-cTAKES-AWS/shangridocs/shangridocs-webapp

$ ./mvn clean tomcat7:run&

