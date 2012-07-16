Scrut My Docs
=========

Introduction
------------

Scrut My Docs is a web application using elasticsearch.
With ScrutMyDocs you can Send, Look for and find all your documents.
 

Installation
------------

Download the Web Application aRchive (WAR) and drop it in the deploy folder of your favorite container (Tomcat, JBoss, ...).
In your browser, open <http://localhost:8080/scrutmydocs/>


Setup
-----

### Embedded node

By default, ScrutMyDocs web application contain all you need to start. So Elasticsearch is embedded and
you have only to start your container to see it live!

### Connect to Elasticsearch Cluster

If you want to use an external ElasticSearch cluster, you will have to set it up.

#### Download and install Elasticsearch

```sh
curl -L -C - -O https://github.com/downloads/elasticsearch/elasticsearch/elasticsearch-0.19.8.deb
dpkg -i elasticsearch-0.19.8.deb
```

#### Add required plugins

[mapper-attachment](https://github.com/elasticsearch/elasticsearch-mapper-attachments), [fsriver](https://github.com/dadoonet/fsriver)

```sh
service elasticsearch stop
/usr/local/elasticsearch/bin/plugin -install elasticsearch/elasticsearch-mapper-attachments/1.4.0
/usr/local/elasticsearch/bin/plugin -install dadoonet/fsriver/0.0.2
```

#### Configure elasticsearch.yml node property file

```
# Mandatory cluster Name. You should be able to modify it in a future release.
cluster.name: scrutmydocs
# If you want to check plugins before starting
plugin.mandatory: mapper-attachments, river-fs
# If you want to disable multicast
discovery.zen.ping.multicast.enabled: false
```

#### Launch Elasticsearch nodes

```sh
service elasticsearch start
```

Then, you will have to configure ScrutMyDocs to connect to your cluster. By default, ScrutMyDocs will connect on
localhost:9300 node with cluster name scrutmydocs.

#### Modify scrutmydocs settings

The first time you launch scrutmydocs web application, it will create a file named `~/.scrutmydocs/config/scrutmydocs.properties`.
Just create or edit this file to adjust your parameters. Here is a 
[sample file](https://github.com/scrutmydocs/scrutmydocs/tree/master/src/main/resources/scrutmydocs/config/scrutmydocs.properties).

Modify the following settings:
```
# Set to false if you want to connect your webapp to an existing Elasticsearch cluster, default to true
node.embedded=false
# If false, you have to define your node(s) address(es), default to : localhost:9300,localhost:9301
node.addresses=localhost:9300
```

You can now deploy the web application in your container.


RESTFul API
-----------

Each API provide help with the `_help` entry point.

```sh
curl 'localhost:8080/scrutmydocs/api/_help'    					 
```


### POST

```sh
# Add a document to the search engine
curl -XPOST 'localhost:8080/scrutmydocs/api/doc/{index}/{type}/{id}/ -d '
{
     "id" :"myid",
	 "index" :"docs",
	 "type" : "doc",
	 "name" : "mydocument",
	 "contentType" : "application/pdf",
	 "content" : " BASE64 encoded file content "
}
'    					 
```

### GET
 
```sh
# Get a document in the default index/type  (doc/docs)
curl -XGET 'localhost:8080/scrutmydocs/api/doc/{id}/'     	 
# Get a document in a specific index with default type (docs)
curl -XGET 'localhost:8080/scrutmydocs/api/doc/{index}/{id}/' 
# Get a document in a specific index/type
curl -XGET 'localhost:8080/scrutmydocs/api/doc/{index}/{type}/{id}/'   			 	     
```

### DELETE

```sh
# DELETE a document in the default index/type  (doc/docs)
curl -XDELETE 'localhost:8080/scrutmydocs/api/doc/{id}/'         
# DELETE a document in a specific index with default type (docs)
curl -XDELETE 'localhost:8080/scrutmydocs/api/doc/{index}/{id}/'  
# DELETE a document in a specific index/type
curl -XDELETE 'localhost:8080/scrutmydocs/api/doc/{index}/{type}/{id}/'    			 	    
```

