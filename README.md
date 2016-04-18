# Setting up ShangriDocs with DUCC and cTAKES

 This guide describes steps to set up the document exploration tool [ShangriDocs](https://github.com/chrismattmann/shangridocs) with [Apache UIMA DUCC](https://uima.apache.org/doc-uimaducc-whatitam.html) and [Apache cTAKES](http://ctakes.apache.org/).


This guide includes

* [Setting Up DUCC](#heading_ducc)
* [Setting Up cTAKES](#heading_ctakes)
* [Setting Up Tika Server](#heading_tika)
* [Setting Up ShangriDocs](#heading_shang)
* [Putting It All Together](#heading_together)
    * [Configuring cTAKES for DUCC](#heading_ctakes_ducc)
    * [Setting Up DUCC with cTAKES and ShangriDocs](#heading_ducc_ctakesshang)
    * [Starting ShangriDocs with DUCC+cTAKES](#heading_starting_shang)
    * [Running DUCC with Multiple Nodes](#heading_multinodes)
    * [Parallelism with Flow Controller (Brief explanation)](#heading_parallel)

#### Prerequisites

Before installing DUCC, create user **ducc** and enable **passwordless ssh** for user _ducc_.
Example for setting this up (for Red Hat Linux) can be found in
[DUCC Prerequisites](https://cwiki.apache.org/confluence/display/UIMA/DUCC#DUCC-t0)

#### References
[cTAKES Scale Out with UIMA DUCC](https://github.com/yiwenliuable/ctakes-scale-out-with-uima-ducc),
[Quick Start Tutorial](https://cwiki.apache.org/confluence/display/UIMA/DUCC),  [DUCC Documentation](https://uima.apache.org/d/uima-ducc-2.0.0/duccbook.html)
### <a name="heading_ducc">Setting Up DUCC

Download the binary installation file [uima-ducc-2.0.1-bin.tar.gz](http://www-us.apache.org/dist/uima/uima-ducc-2.0.1/uima-ducc-2.0.1-bin.tar.gz) to _/home/ducc/_

Then, from _/home/ducc/_

```$ tar -xvzf uima-ducc-2.0.1-bin.tar.gz```

Once the files are extracted, you need to configure DUCC to your system

```$ cd apache-uima-ducc-2.0.1/admin/```

```$ ./ducc_post_install```

##### <a name="heading_duccpost">Post install configuration
The **<em>ducc_post_install</em>** script sets up the default configuration in _ducc.properties_. This is where you can define:
* Hostname of the DUCC head node
* Full path to your Java executable (default is /usr/bin/java)

The default configuration file is located at _.../apache-uima-ducc-2.0.1/resources/default.ducc.properties_

Running <em>ducc_post_install</em> copies the parameters in **_default.ducc.properties_**, along with incorporating the hostname of head node and the java path to _ducc.properties_. Any changes made to _ducc.properties_ will be overwritten from running <em>ducc_post_install</em>. Refer to [Modifying ducc properties](#heading_configducc) for making changes to the properties.


#### Starting DUCC

From _.../apache-uima-ducc-2.0.1/admin_

```$ ./start_ducc```

**Note:** Wait at least a minute, after starting DUCC, before submitting any jobs. It takes DUCC a while for all initialization to be completed. If you submit a job before the initialization has been completed, it will return errors, such as _type=system error, text=job driver node unavailable_.


#### Checking status

From _.../apache-uima-ducc-2.0.1/admin_

```$ ./check_ducc```

The web interface to monitor the system and jobs can be accessed via a browser using

* ```http://[DUCC hostname]:42133/system.daemons.jsp```

* ```http://[DUCC hostname]:42133/jobs.jsp```

#### Testing DUCC
Submit a simple example job via command line

```$ /home/ducc/apache-uima-ducc-2.0.1/bin/ducc_submit -f /home/ducc/apache-uima-ducc-2.0.1/examples/simple/1.job```

#### Stopping DUCC
$ /home/ducc/apache-uima-ducc-2.0.1/admin/stop_ducc -a

####<a name="heading_configducc"></a> Further configuration to DUCC properties

Modification to DUCC's configuration should be performed in **_default.ducc.properties_** .  You can manually change the parameters in this file. Make sure to run the <em>ducc_post_install</em> script again after modification are made. Changes will take effect after DUCC is restarted.


## <a name="heading_ctakes">Setting Up cTAKES

Download the binary installation file [apache-ctakes-3.2.2-bin.tar.gz](http://www-us.apache.org/dist/ctakes/ctakes-3.2.2/apache-ctakes-3.2.2-bin.tar.gz) to _/home/ducc/_

If this site doesn’t work, other mirror sites can be found from [cTAKES Download Page](http://ctakes.apache.org/downloads.cgi), go to bottom of page “Current Download Mirror:”

Then, from _/home/ducc/_

```
$ tar -xvzf apache-ctakes-3.2.2-bin.tar.gz
$ curl -Lo ctakes-resources-3.2.1.1-bin.zip "http://downloads.sourceforge.net/project/ctakesresources/ctakes-resources-3.2.1.1-bin.zip?r=http%3A%2F%2Fsourceforge.net%2Fprojects%2Fctakesresources%2F%3Fsource%3Dtyp_redirect&ts=1433609725&use_mirror=softlayer-dal"
$ mv ctakes-resources-3.2.1.1-bin.zip /home/ducc/apache-ctakes-3.2.2
$ cd /home/ducc/apache-ctakes-3.2.2
$ unzip ctakes-resources-3.2.1.1-bin.zip
```

You can also follow the instructions in [cTAKESParser](https://wiki.apache.org/tika/cTAKESParser#Installing_cTAKES) for _Installing cTAKES_.
**Note:** This version of ShangriDocs DO NOT require Tika working with cTAKES as a server, like the finished product of [cTAKESParser](https://wiki.apache.org/tika/cTAKESParser).

### <a name="heading_umls"> Obtain UMLS license

The use of the analysis engine in ShangriDocs requires a UMLS license.

UMLS license (username and password) can be obtained from: [Obtain UMLS license here](https://uts.nlm.nih.gov//license.html)

This takes about 2 working days.

### Improving performance of cTAKES
Advanced  modification of cTAKES to improve performance and customize the annotated categories can be found in [Creating New Types](https://github.com/selinachu/CreateNewType). This requires the developer's version of cTAKES   ([apache-ctakes-3.2.2-src.tar.gz](http://www-us.apache.org/dist/ctakes/ctakes-3.2.2/apache-ctakes-3.2.2-src.tar.gz)). Please refer to the [Developer Install Guide]( https://cwiki.apache.org/confluence/display/CTAKES/cTAKES+3.2+Developer+Install+Guide).


## <a name="heading_shang"> Setting Up ShangriDocs

[ShangriDocs’s main site](https://github.com/chrismattmann/shangridocs)

For convenience, the code of the current version of ShangriDocs on AWS is at https://github.com/selinachu/DUCC-cTAKES-AWS.git


From _/home/ducc/_

```$ git clone https://github.com/selinachu/DUCC-cTAKES-AWS.git```


Add [UMLS username and password](#heading_umls) to **CTAKESConfig.properties**, located in
_/home/ducc/shangridocs/shangridocs-services/src/main/resources/CTAKESContentHandler/config/org/apache/tika/sax/_

```
UMLSUser=[your_username]
UMLPass=[your_password]
```

Also, add UMLS username and password to shell variables
```
export ctakes_umlsuser=‘your_username’
export ctakes_umlspw=‘your_password’
```

## <a name="heading_tika">Setting Up Tika Server


This is taken from [ShangriDocs Tika Server](https://github.com/chrismattmann/shangridocs#apache-tika-server), but skipping step 3.

**Note:** This version of Shangridocs, does not require the ctakes-tika server.

```
$ cd /home/ducc/DUCC-cTAKES-AWS/shangridocs
$ git clone https://github.com/apache/tika.git
```
##### <a name="heading_start_tika"> Starting Tika server
```
$ cd /home/ducc/DUCC-cTAKES-AWS/shangridocs/tika
$ java -jar tika-server/target/tika-server-1.11-SNAPSHOT.jar > ../tika-server.log 2>&1&
```
The Tika server will be on _port 9998_.

Now you are all set up to start **ShangriDocs**

## <a name="heading_together"> Putting It All Together

### <a name="heading_ctakes_ducc">Configuring cTAKES for DUCC

From _/home/ducc/apache-ctakes-3.2.2/desc/_

Change all descriptor files with ```<multipleDeploymentAllowed>``` tag from _false_ to _true_.  

**Note:** A simple way of accomplishing this is by searching for all descriptor files under _.../apache-ctakes-3.2.2/desc/_ with ```<multipleDeploymentAllowed>false``` and perform replacements to ```<multipleDeploymentAllowed>true```

Add type system information to **FilesInDirectoryCollectionReader.xml** in _.../apache-ctakes-3.2.2/desc/ctakes-core/desc/collection_reader/_

(Or [FilesInDirectoryCollectionReader.xml](https://github.com/selinachu/DUCC-cTAKES-AWS/blob/master/FilesInDirectoryCollectionReader.xml) from this repository)
```
<typeSystemDescription>
  <imports>
    <import name="org.apache.ctakes.typesystem.types.TypeSystem"/>
  </imports>
</typeSystemDescription>
<typePriorities/>
<fsIndexCollection/>
<capabilities>
  <capability>
    <inputs/>
    <outputs>
      <type allAnnotatorFeatures="true">org.apache.ctakes.typesystem.types.TypeSystem</type>
    </outputs>
```



Add type system information to **XCasWriterCasConsumer.xml** from _...pache-ctakes-3.2.2/desc/ctakes-core/desc/cas_consumer_
(Or [XCasWriterCasConsumer.xml](https://github.com/selinachu/DUCC-cTAKES-AWS/blob/master/XCasWriterCasConsumer.xml) from this repository)
```
<import name="org.apache.ctakes.typesystem.types.TypeSystem"/>
```


### <a name="heading_ducc_ctakesshang">Setting up DUCC with cTAKES and Shangridocs

Configurations are mainly defined in the DUCC properties file  [default.ducc.properties](https://github.com/selinachu/DUCC-cTAKES-AWS/blob/master/default.ducc.properties) and the job description file [ctakes.job](https://github.com/selinachu/DUCC-cTAKES-AWS/blob/master/shangridocs/shangridocs-services/src/main/resources/ctakes.job)

Replace the /home/ducc/apache-uima-ducc-2.0.1/resources/default.ducc.properties with this [default.ducc.properties](https://github.com/selinachu/DUCC-cTAKES-AWS/blob/master/default.ducc.properties) and run [ducc post install](#heading_duccpost) script again.

Set these environment variables
```
export DUCC_HOME=[path to ducc]
export CTAKES_HOME=[path to ctakes]
export SHANGRIDOCS_HOME=[path to shangridocs]
export TIKA_HOME=[path to tika]

```

If following the set up instructions above, then the paths would be
```
export DUCC_HOME=“/home/ducc/apache-uima-ducc-2.0.1“
export CTAKES_HOME=“/home/ducc/apache-ctakes-3.2.2”
export SHANGRIDOCS_HOME=“/home/ducc/DUCC-cTAKES-AWS/shangridocs”
export TIKA_HOME=“/home/ducc/DUCC-cTAKES-AWS/shangridocs/tika”
```

### <a name="heading_starting_shang">Starting ShangriDocs with DUCC and cTAKES
```
$ cd /home/ducc/apache-uima-ducc-2.0./admin
$ ./start_ducc
$ cd /home/ducc/DUCC-cTAKES-AWS/shangridocs/tika
$ ./run.bash
$ cd /home/ducc/DUCC-cTAKES-AWS/shangridocs/shangridocs-webapp
$ ./mvn clean tomcat7:run&
```
**Note:** [_run.bash_](https://github.com/selinachu/DUCC-cTAKES-AWS/blob/master/run-tika.bash) script starts the Tika server.   This can also be accomplished by [starting Tika server](#heading_start_tika).


### <a name="heading_multinodes">Running DUCC with multiple nodes

The multiple nodes needs to be defined in the following in the files: **ducc.nodes** and **jobdriver.nodes** under _...apache-uima-ducc-2.0.1/resources/_

```
ducc-1.aws-hostname.com
ducc-2.aws-hostname.com
ducc-3.aws-hostname.com
ducc-4.aws-hostname.com
```

This sets up DUCC to run on a cluster, or more specifically connecting the head node with the other three work nodes.  This allows the head node to send a job to one of its worker nodes.  

### <a name="heading_parallel">Parallelism with Flow Controller (Brief explanation)

DUCC does not automatically break up a large job to be run on multiple machines simultaneously. To accomplish this, it would require preprocessing of the document(s). The idea is to create separate set of CASes for each document and send them into the pipeline.
This would be done by incorporating a custom _Flow Controller_ to work inside the _Aggregate Analysis Engine_ containing a CAS Multiplier. By routing a CAS to a CAS Multiplier, it permits the creation of new CASes. The Cas Multiplier and Analysis Engine threads can then be run in parallel.

An example related to this topic can be  found from the [DUCC Documentation](https://uima.apache.org/d/uima-ducc-1.0.0/duccbook.html#x1-1380009). This explains the process to split a single text file, by using paragraphs as boundaries, to further segment the text into separate documents. Thus, breaking large files into multiple _Work_ items.   

Documentation related to DUCC's [Flow Controller](https://uima.apache.org/d/uima-ducc-1.0.0/duccbook.html#x1-1340008.5.2) and UIMA's [Flow Controllers with CAS Multipliers](https://uima.apache.org/d/uimaj-2.4.0/tutorials_and_users_guides.html#ugr.tug.fc.using_fc_with_cas_multipliers)


### <a name="heading_illegal_char">Known DUCC issue with _illegal_ characters

DUCC will cancel a job and related processes when encountering **illegal** characters. This issue arises from the CollectionReader when the Job Driver is putting _illegal_ characters in the work item CAS, which cannot be XML serialized. Produced error message will include ```Running Ducc Containerjava.lang.RuntimeException: JP Http Client Unable to Communicate with JD```.   
