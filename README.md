# Setting up ShangriDocs with DUCC and cTAKES

 This guide describes steps to set up the document exploration tool [ShangriDocs](https://github.com/chrismattmann/shangridocs) with [Apache UIMA DUCC](https://uima.apache.org/doc-uimaducc-whatitam.html) and [Apache cTAKES](http://ctakes.apache.org/).


This guide includes

* [Install DUCC](#heading_ducc)
* [Install cTAKES](#heading_ctakes)
* [Install Tika Server](#heading_tika)
* [Install ShangriDocs](#heading_shang)
* [Putting it all together](#heading_together)

## <a name="heading_ducc"></a>Setting up DUCC

#### Prerequisites

Before installing DUCC, create user **ducc** and enable **passwordless ssh** for user _ducc_.
Example for setting this up (for Red Hat Linux) can be found in
[DUCC Prerequisites](https://cwiki.apache.org/confluence/display/UIMA/DUCC#DUCC-t0)

##### References
[DUCC Documentation](https://uima.apache.org/d/uima-ducc-2.0.0/duccbook.html), [Quick Start Tutorial](https://cwiki.apache.org/confluence/display/UIMA/DUCC)


### Install DUCC

Download the binary installation file [uima-ducc-2.0.1-bin.tar.gz](http://www-us.apache.org/dist/uima/uima-ducc-2.0.1/uima-ducc-2.0.1-bin.tar.gz) to _/home/ducc/_

Then, from _/home/ducc/_

```$ tar -xvzf uima-ducc-2.0.1-bin.tar.gz```

Once the files are extracted, you need to configure DUCC to your system

```$ cd apache-uima-ducc-2.0.1/admin/```

```$ ./ducc_post_install```


The **<em>ducc_post_install</em>** script sets up the default configuration in _ducc.properties_. This is where you can define:
* Hostname of the DUCC head node
* Full path to your Java executable (default is /usr/bin/java)

The default configuration file is located at _.../apache-uima-ducc-2.0.1/resources/default.ducc.properties_

Running <em>ducc_post_install</em> copies the parameters in **_default.ducc.properties_**, along with incorporating the hostname of head node and the java path to _ducc.properties_. Any changes made to _ducc.properties_ will be overwritten from running <em>ducc_post_install</em>. [Modifying ducc properties](#heading_configducc)


#### Starting DUCC

From _.../apache-uima-ducc-2.0.1/admin_

```$ ./start_ducc```

**Note:** Wait at least a minute, after starting DUCC before submitting any jobs. It takes DUCC a while for all initialization to be completed. If you submit a job before the initialization has been completed, it will return errors, such as _type=system error, text=job driver node unavailable_.


#### Checking status

From _.../apache-uima-ducc-2.0.1/admin_

```$ ./check_ducc```

The web interface to monitor the system and jobs can be accessed via a browser using

* ```http://[DUCC hostname]:42133/system.daemons.jsp```

* ```http://[DUCC hostname]:42133/jobs.jsp```

To test your system out, you can submit a simple example job via command line

```$ /home/ducc/apache-uima-ducc-2.0.1/bin/ducc_submit -f /Users/ducc/apache-uima-ducc-2.0.1/examples/simple/1.job```

#### Stopping DUCC
$ /home/ducc/apache-uima-ducc-2.0.1/admin/stop_ducc -a

####<a name="heading_configducc"></a> Further configuration to DUCC properties

Modification to DUCC's configuration should be performed in **_default.ducc.properties_** .  You can manually change the parameters in this file. Make sure to run the <em>ducc_post_install</em> script again after modification are made. Changes will take effect after DUCC is restarted.


## <a name="heading_ctakes">Setting up cTAKES

Summarizing steps to set up cTAKES

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

You can also follow the instructions in [cTAKESParser](https://wiki.apache.org/tika/cTAKESParser#Installing_cTAKES) for only _Installing cTAKES_.
**Note:** This version of ShangriDocs DO NOT require Tika working with cTAKES as a server like the finished product of [cTAKESParser](https://wiki.apache.org/tika/cTAKESParser)

### Obtain UMLS license

The use of the analysis engine in ShangriDocs requires a UMLS license

UMLS license (username and password) can be obtain from: [Obtain UMLS license here](https://uts.nlm.nih.gov//license.html)

This takes about 2 working days.

### Improving performance of cTAKES
Advanced  modification of cTAKES to improve performance and customize the annotated categories can be found in [Creating New Types](https://github.com/selinachu/CreateNewType). This requires the developer's version of cTAKES   ([apache-ctakes-3.2.2-src.tar.gz](http://www-us.apache.org/dist/ctakes/ctakes-3.2.2/apache-ctakes-3.2.2-src.tar.gz)). Please refer to the [Developer Install Guide]( https://cwiki.apache.org/confluence/display/CTAKES/cTAKES+3.2+Developer+Install+Guide).


## <a name="heading_tika">Setting up Tika Server

(If not cloning from https://github.com/selinachu/DUCC-cTAKES-AWS.git)

This can be accomplished by skipping step 3 (or ignoring ‘mkdir -p tika/ctakes’) from [ShangriDocs-TikaServer](https://github.com/chrismattmann/shangridocs#apache-tika-server).
Note: This version of Shangridocs, one do not need a ctakes-tika server.

Sumarizing the steps:
```
$ cd /home/ducc/DUCC-cTAKES-AWS/shangridocs
$ git clone https://github.com/apache/tika.git
```
##### <a name="heading_start_tika"> Starting Tika server
```
$ cd tika
$ java -jar tika-server/target/tika-server-1.11-SNAPSHOT.jar > ../tika-server.log 2>&1&
```

## <a name="heading_shang"> Setting up ShangriDocs

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

Now you are all set up to start **ShangriDocs**

## <a name="heading_together"> Putting it all together

### Configuring cTAKES for DUCC

From _/home/ducc/apache-ctakes-3.2.2/desc/_

Change all descriptor files with ```<multipleDeploymentAllowed>``` tag from _false_ to _true_.  
**Note:** A simple way of accomplishing this is by searching for all descriptor files under _.../apache-ctakes-3.2.2/desc/_ with ```<multipleDeploymentAllowed>false``` and perform replacements to ```<multipleDeploymentAllowed>true```

Replace the FilesInDirectoryCollectionReader.xml in _.../apache-ctakes-3.2.2/desc/ctakes-core/desc/collection_reader/_ with the the [FilesInDirectoryCollectionReader.xml](https://github.com/selinachu/DUCC-cTAKES-AWS/blob/master/FilesInDirectoryCollectionReader.xml) in this repository


### Setting up DUCC with cTAKES and Shangridocs

Replace the /home/ducc/apache-uima-ducc-2.0.1/resources/default.ducc.properties with current in this github repository

Then, run the script /home/ducc/apache-uima-ducc-2.0.1/admin/ducc_post_install again

Set these environment variables

```
export DUCC_HOME=[path to ducc]
export CTAKES_HOME=[path to ctakes]
export SHANGRIDOCS_HOME=[path to shangridocs]
export TIKA_HOME=[path to tika]

```

If following the instructions here, then the paths would be
```
export DUCC_HOME=“/home/ducc/apache-uima-ducc-2.0.1“
export CTAKES_HOME=“/home/ducc/apache-ctakes-3.2.2”
export SHANGRIDOCS_HOME=“/home/ducc/DUCC-cTAKES-AWS/shangridocs”
export TIKA_HOME=“/home/ducc/DUCC-cTAKES-AWS/shangridocs/tika”
```

### Starting ShangriDocs with DUCC and cTAKES
```
$ cd /home/ducc/apache-uima-ducc-2.0./admin
$ ./start_ducc
$ cd /home/ducc/DUCC-cTAKES-AWS/shangridocs/tika
$ ./run.bash
$ cd /home/ducc/DUCC-cTAKES-AWS/shangridocs/shangridocs-webapp
$ ./mvn clean tomcat7:run&
```
**Note:** [_run.bash_](https://github.com/selinachu/DUCC-cTAKES-AWS/blob/master/run-tika.bash) script starts the Tika server.   This can also be accomplished by [starting Tika server](#heading_start_tika).


### Running DUCC with multiple nodes

The multiple nodes needs to be defined in the following in the files: **ducc.nodes** and **jobdriver.nodes** under _...apache-uima-ducc-2.0.1/resources/_

```
ducc-1.aws-hostname.com
ducc-2.aws-hostname.com
ducc-3.aws-hostname.com
ducc-4.aws-hostname.com
```
