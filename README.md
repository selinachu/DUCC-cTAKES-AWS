# Setting up ShangriDocs with DUCC and cTAKES


This guide includes

* [Set up DUCC](#head_ducc)
* [Set up cTAKES](#head_ctakes)
* [Set up Tika Server](#head_tika)
* [Set up ShangriDocs UI](#head_shang)

## <a name="head_ducc"></a>Setting up DUCC

Before installing DUCC, create user ducc and enable passwordless ssh for user ducc

##### DUCC Documentation
https://uima.apache.org/d/uima-ducc-2.0.0/duccbook.html

##### Quick start tutorial
https://cwiki.apache.org/confluence/display/UIMA/DUCC


### Install

Download the binary installation file [uima-ducc-2.0.1-bin.tar.gz](http://apache.cs.utah.edu//uima//uima-ducc-2.0.1/uima-ducc-2.0.1-bin.tar.gz) to _/home/ducc/_

Then, from _/home/ducc/_

```$ tar -xvzf uima-ducc-2.0.1-bin.tar.gz```

Once the files are extracted, we need to configure DUCC to your system

### Configure
```$ cd apache-uima-ducc-2.0.1/admin/```

```$ ./ducc_post_install```

The <em>ducc_post_install</em> script sets up the default configuration in _ducc.properties_. This is where you can define:
* Hostname of the DUCC head node
* Full to your Java executable (default is /usr/bin/java)

The default configuration file is located at _/apache-uima-ducc-2.0.1/resources/default.ducc.properties_

Running <em>ducc_post_install</em> copies the parameters in _default.ducc.properties_ into _ducc.properties_, along with incorporating the hostname of head node and the java path in the configuration file.

Modification to DUCC's configuration should be performed in _default.ducc.properties_.  After changes are made, make sure to run the <em>ducc_post_install</em> script again.


#### Starting DUCC

From _.../apache-uima-ducc-2.0.1/admin_

```$ ./start_ducc```

**Note:** Wait at least a minute, after starting DUCC before submitting any jobs. It takes DUCC a while for all initialization to be completed. If you submit a job before the initialization has been completed, it will return errors regarding _type=system error, text=job driver node unavailable_


#### Check status

From _.../apache-uima-ducc-2.0.1/admin_

```$ ./check_ducc```

The web interface to monitor the system and jobs can be accessed via a browser using

* ```http://[DUCC hostname]:42133/system.daemons.jsp```

* ```http://[DUCC hostname]:42133/jobs.jsp```

To test your system out, you can submit a simple example job via command line

```$ /home/ducc/apache-uima-ducc-2.0.1/bin/ducc_submit -f /Users/ducc/apache-uima-ducc-2.0.1/examples/simple/1.job```

#### To stop DUCC completely
$ /home/ducc/apache-uima-ducc-2.0.1/admin/stop_ducc -a


### Setting up DUCC with cTAKES and Shangridocs

Replace the /home/ducc/apache-uima-ducc-2.0.1/resources/default.ducc.properties with current in this github repository

Then, run the script /home/ducc/apache-uima-ducc-2.0.1/admin/ducc_post_install again


## <a name="head_ctakes">Setting up cTAKES

You can follow the instructions in https://wiki.apache.org/tika/cTAKESParser for only “Installing cTAKES”.
**Note:** Installling cTAKESParser is unnecessary for this version of ShangriDocs. We DO NOT need Tika working with cTAKES as a server.


### Summarizing set up for cTAKES

Download the binary installation file [apache-ctakes-3.2.2-bin.tar.gz](http://www-us.apache.org/dist/ctakes/ctakes-3.2.2/apache-ctakes-3.2.2-bin.tar.gz) to _/home/ducc/_
If this site doesn’t work, other mirror sites can be found from [cTAKES] (http://ctakes.apache.org/downloads.cgi), go to bottom of page “Current Download Mirror:”

Then, from _/home/ducc/_

```
$ tar -xvzf apache-ctakes-3.2.2-bin.tar.gz
$ curl -Lo ctakes-resources-3.2.1.1-bin.zip "http://downloads.sourceforge.net/project/ctakesresources/ctakes-resources-3.2.1.1-bin.zip?r=http%3A%2F%2Fsourceforge.net%2Fprojects%2Fctakesresources%2F%3Fsource%3Dtyp_redirect&ts=1433609725&use_mirror=softlayer-dal"
$ mv ctakes-resources-3.2.1.1-bin.zip /home/ducc/apache-ctakes-3.2.2
$ cd /home/ducc/apache-ctakes-3.2.2
$ unzip ctakes-resources-3.2.1.1-bin.zip
```

### Obtain UMLS license

UMLS license (username and password) can be obtain from: [Obtain UMLS license here](https://uts.nlm.nih.gov//license.html)

This takes about 2 working days.


### Configuring cTAKES for DUCC

From _/home/ducc/apache-ctakes-3.2.2/desc/_

Change all descriptor files with ```<multipleDeploymentAllowed>``` tag from _false_ to _true_.  Note: Search for all descriptor files with ```<multipleDeploymentAllowed>false``` and make the replacement  

Replace the FilesInDirectoryCollectionReader.xml in _.../apache-ctakes-3.2.2/desc/ctakes-core/desc/collection_reader/_ with the one in this github repository

## <a name="head_shang"> Setting up ShangriDocs

ShangriDocs’s main site is at https://github.com/chrismattmann/shangridocs
Prior configuration DUCC with cTAKES+ShangriDocs can be found on: https://github.com/yiwenliuable/ctakes-scale-out-with-uima-ducc

For convenience, the code of the current version of ShangriDocs on AWS is at https://github.com/selinachu/DUCC-cTAKES-AWS.git

For convenience,

From _/home/ducc/_

```$ git clone https://github.com/selinachu/DUCC-cTAKES-AWS.git```


Add UMLS username and password to CTAKESConfig.properties

/Users/ducc/shangridocs/shangridocs-services/src/main/resources/CTAKESContentHandler/config/org/apache/tika/sax/CTAKESConfig.properties

Also, add UMLS username and password to shell variables

```
export ctakes_umlsuser=‘username’
export ctakes_umlspw=‘password’
```

Set these environment variables

```
export DUCC_HOME=[path to ducc]
export CTAKES_HOME=[path to ctakes]
export SHANGRIDOCS_HOME=[path to shangridocs]
export TIKA_HOME=[path to tiak]
```

If following the instructions here, then the paths would be
```
export DUCC_HOME=“/home/ducc/apache-uima-ducc-2.0.1“
export CTAKES_HOME=“/home/ducc/ctakes/apache-ctakes-3.2.2”
export SHANGRIDOCS_HOME=“/home/ducc/DUCC-cTAKES-AWS/shangridocs”
export TIKA_HOME=“/home/ducc/DUCC-cTAKES-AWS/shangridocs/tika”
```
## <a name="head_"tika>Setting up Tika Server

(If not copying from from https://github.com/selinachu/DUCC-cTAKES-AWS.git)

https://github.com/chrismattmann/shangridocs#apache-tika-server
Note: For this version of Shangridocs, one do not need a ctakes-tika server. This can be accomplished by skipping step 3 (or ignoring ‘mkdir -p tika/ctakes’) from https://github.com/chrismattmann/shangridocs#apache-tika-server

##### Summarizing instructions to set up Tika server

```
$ cd /home/ducc/DUCC-cTAKES-AWS/shangridocs
$ git clone https://github.com/apache/tika.git
```
##### To start Tika server
```
$ cd tika
$ java -jar tika-server/target/tika-server-1.11-SNAPSHOT.jar > ../tika-server.log 2>&1&
```

Now you are all set up to start **ShangriDocs**

### Start ShangriDocs
```
$ cd /home/ducc/apache-uima-ducc-2.0./admin
$ ./start_ducc
$ cd /home/ducc/DUCC-cTAKES-AWS/shangridocs/tika
$ ./run.bash
$ cd /home/ducc/DUCC-cTAKES-AWS/shangridocs/shangridocs-webapp
$ ./mvn clean tomcat7:run&
```
**Note:** _run.bash_ script is included in this repository.  It is used to start the Tika server
