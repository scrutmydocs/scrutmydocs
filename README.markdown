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

##### Linux (Debian-like)
```sh
curl -L -C - -O https://github.com/downloads/elasticsearch/elasticsearch/elasticsearch-0.19.8.deb
dpkg -i elasticsearch-0.19.8.deb
```

##### OSX
With [Homebrew](http://mxcl.github.com/homebrew/)
```sh
brew install elasticsearch
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


Scrut My Docs REST API Resources
================================

Common based API
----------------

API are running at scrutmydocs point.

Each API provide help with the `_help` entry point.

```sh
curl 'localhost:8080/scrutmydocs/1/_help'    					 
```

REST Response are always based on the following JSON content:
```javascript
{
   "ok":true,
   "errors":null,
   "object":null
}
```

Ok indicates if there was something wrong during API execution.
If `ok` is `false`, you should find errors in `errors` property.

```javascript
{
   "ok":false,
   "errors":[
      "This is a first error",
      "This is a second error"
   ],
   "object":null
}
```

When needed, the API can return an object. For example, if you ask for a river detail, you should find
it in the `object` property:

```javascript
{
   "ok":true,
   "errors":null,
   "object":{
      "type":"fs",
      "analyzer":"standard",
      "url":"/tmp_es",
      "updateRate":30,
      "includes":null,
      "excludes":null,
      "name":"myfirstriver",
      "id":"myfirstriver",
      "start":false,
      "indexname":"docs",
      "typename":"doc"
   }
}
```

The returned object depends on the API you use.

Document
--------

You can manage your documents with the Document API.

### REST Resources

<table>
	<thead>
		<tr>
			<th>Resource</th>
			<th>Description</th>
		</tr>
	</thead>
	<tbody>
	    <tr>
			<td>GET 1/doc/_help</td>
			<td>Display help.</td>
	    </tr>
	    <tr>
			<td>POST 1/doc</td>
			<td>Add a document to the search engine (see Document object).</td>
	    </tr>
	    <tr>
			<td>DELETE 1/doc/{id}</td>
			<td>Delete a document in the default index/type (doc/docs).</td>
	    </tr>
	    <tr>
			<td>DELETE 1/doc/{index}/{id}</td>
			<td>Delete a document in the default type (doc).</td>
	    </tr>
	    <tr>
			<td>DELETE 1/doc/{index}/{type}/{id}</td>
			<td>Delete a document.</td>
	    </tr>
	    <tr>
			<td>GET 1/doc/{id}</td>
			<td>Get a document in the default index/type (doc/docs).</td>
	    </tr>
	    <tr>
			<td>GET 1/doc/{index}/{id}</td>
			<td>Get a document in a specific index with default type (docs).</td>
	    </tr>
	    <tr>
			<td>GET 1/doc/{index}/{type}/{id}</td>
			<td>Get a document in a specific index/type.</td>
	    </tr>
    </tbody>
</table>


### Document Object

A document object looks like:

```javascript
{
     "id" :"docid",
	 "index" :"docindex",
	 "type" : "doctype",
	 "name" : "documentname.pdf",
	 "contentType" : "application/pdf",
	 "content" : " BASE64 encoded file content "
}
```

When sending a document to Scrutmydocs, you can use a minimal structure:
```javascript
{
	 "name" : "documentname.pdf",
	 "contentType" : "application/pdf",
	 "content" : " BASE64 encoded file content "
}
```


### Examples


```sh
# Add a document to the search engine
curl -XPOST 'localhost:8080/scrutmydocs/1/doc/ -d '
{
     "id" :"myid1",
	 "name" : "mydocument",
	 "contentType" : "application/pdf",
	 "content" : " BASE64 encoded file content "
}
'    					 

# Add a document to the search engine
curl -XPOST 'localhost:8080/scrutmydocs/1/doc/ -d '
{
     "id" :"myid2",
	 "index" :"docs",
	 "type" : "doc",
	 "name" : "mydocument",
	 "contentType" : "application/pdf",
	 "content" : " BASE64 encoded file content "
}
'    					 

# Get a document in the default index/type  (docs/doc)
curl -XGET 'localhost:8080/scrutmydocs/1/doc/myid1/'
     	 
# Get a document in a specific index/type
curl -XGET 'localhost:8080/scrutmydocs/1/doc/docs/doc/myid2/'   			 	     

# DELETE a document in the default index/type  (docs/doc)
curl -XDELETE 'localhost:8080/scrutmydocs/1/doc/myid1/'         

# DELETE a document in a specific index/type
curl -XDELETE 'localhost:8080/scrutmydocs/1/doc/docs/doc/myid2/'    			 	    
```


Index
-----

You can manage your indices with the Index API.

### REST Resources

<table>
	<thead>
		<tr>
			<th>Resource</th>
			<th>Description</th>
		</tr>
	</thead>
	<tbody>
	    <tr>
			<td>GET 1/index/_help</td>
			<td>Display help.</td>
	    </tr>
	    <tr>
			<td>POST 1/index</td>
			<td>Create a new index (see Index Object).</td>
	    </tr>
	    <tr>
			<td>POST 1/index/{index}</td>
			<td>Create a new index named index with default settings. <b>deprecated</b></td>
	    </tr>
	    <tr>
			<td>POST 1/index/{index}/{type}</td>
			<td>Create a new index named index with a specific type (see Index Object). <b>deprecated</b></td>
	    </tr>
	    <tr>
			<td>DELETE 1/index/{index}</td>
			<td>Delete a full index. Use with caution !</td>
	    </tr>
    </tbody>
</table>

### Index Object

A index object looks like:

```javascript
{
	 "index" :"docs",
	 "type" : "doc",
	 "analyzer" : "default"
}
```


### Examples


```sh
# CREATE an index
curl -XPOST 'localhost:8080/scrutmydocs/1/index/ -d '
{
     "index" :"myindex",
	 "type" : "mytype",
	 "analyzer" : "french"
}
'    					 

# DELETE an index
curl -XDELETE 'localhost:8080/scrutmydocs/1/index/myindex'         
```


Search
------

You can search for documents.

### REST Resources

<table>
	<thead>
		<tr>
			<th>Resource</th>
			<th>Description</th>
		</tr>
	</thead>
	<tbody>
	    <tr>
			<td>GET 1/search/_help</td>
			<td>Display help.</td>
	    </tr>
	    <tr>
			<td>POST 1/search</td>
			<td>Search for a text and navigate in results (see SearchQuery Object).</td>
	    </tr>
	    <tr>
			<td>GET 1/search/{term}</td>
			<td>Search for a term.</td>
	    </tr>
    </tbody>
</table>

### SearchQuery Object

A search query object looks like:

```javascript
{
	 "search" :"apache",
	 "first" : 0,
	 "pageSize" : 10
}
```

Search is the text to search. You can use a Lucene syntax.
First is the page number (default to 0).
PageSize is the size of a page (aka number of results to fetch).

### SearchResponse Object

A search response object looks like:

```javascript
{
	 "took" : 123,
	 "totalHits" : 78,
	 "hits" : [
	      {
	         "id":"3fOybUdsTCWcNrCRRcMzKQ",
	         "type":"doc",
	         "index":"docs",
	         "contentType":"text/plain",
	         "source":null,
	         "highlights":[
	            "      <span class='badge badge-info'>Apache</span> License\n                           Version 2.0, January 2004\n                        http://www",
	            "apply the <span class='badge badge-info'>Apache</span> License to your work.\n\n      To apply the <span class='badge badge-info'>Apache</span> License to your work, attach the following",
	            "under the <span class='badge badge-info'>Apache</span> License, Version 2.0 (the \"License\");\n   you may not use this file except in compliance"
	         ],
	         "title":"LICENSE.txt"
	      },
	      {
	         "id":"nRYAgObDR6OAchE__Lwd_g",
	         "type":"doc",
	         "index":"docs",
	         "contentType":"text/plain",
	         "source":null,
	         "highlights":[
	            "      <span class='badge badge-info'>Apache</span> License\n                           Version 2.0, January 2004\n                        http://www",
	            "apply the <span class='badge badge-info'>Apache</span> License to your work.\n\n      To apply the <span class='badge badge-info'>Apache</span> License to your work, attach the following",
	            "under the <span class='badge badge-info'>Apache</span> License, Version 2.0 (the \"License\");\n   you may not use this file except in compliance"
	         ],
	         "title":"LICENSE.txt"
	      }
	 ]
}
```

Took is the time in milliseconds.
TotalHits is the total number of hits.
Hits contains an array of Hit objects.

### Hit Object

A hit object looks like:

```javascript
  {
     "id":"3fOybUdsTCWcNrCRRcMzKQ",
     "type":"doc",
     "index":"docs",
     "contentType":"text/plain",
     "source":null,
     "highlights":[
        "      <span class='badge badge-info'>Apache</span> License\n                           Version 2.0, January 2004\n                        http://www",
        "apply the <span class='badge badge-info'>Apache</span> License to your work.\n\n      To apply the <span class='badge badge-info'>Apache</span> License to your work, attach the following",
        "under the <span class='badge badge-info'>Apache</span> License, Version 2.0 (the \"License\");\n   you may not use this file except in compliance"
     ],
     "title":"LICENSE.txt"
  }
```

Id is the unique internal ID of the document.
Type is the object type (default to doc).
Index is the object index (default to docs)
ContentType is the document content type.
Source is always null as we don't provide content by now.
Title is the document filename.
Highlights may contain an array of String which highlights the document content with the searched terms. 


### Examples


```sh
# SEARCH for apache term
curl -XGET 'localhost:8080/scrutmydocs/1/search/apache'         

# SEARCH for apache term, starting from page 2 with 20 results
curl -XPOST 'localhost:8080/scrutmydocs/1/search/apache -d '
{
	 "search" :"apache",
	 "first" : 1,
	 "pageSize" : 20
}
'    					 
```


Rivers (Common API for all Rivers)
----------------------------------

You can manage get information on all rivers with the Rivers API.

### REST Resources

<table>
	<thead>
		<tr>
			<th>Resource</th>
			<th>Description</th>
		</tr>
	</thead>
	<tbody>
	    <tr>
			<td>GET 1/settings/rivers/_help</td>
			<td>Display help.</td>
	    </tr>
	    <tr>
			<td>GET 1/settings/rivers</td>
			<td>Get all existing rivers (it will provide an array of River objects).</td>
	    </tr>
    </tbody>
</table>

### River Object

A river object looks like:

```javascript
{
	 "id" : "mydummyriver",
	 "name" : "My Dummy River",
	 "indexname" : "docs",
	 "typename" : "doc",
	 "start" : false
	 //, ... plus some metadata depending on each river ...
}
```

Id is the unique name of your river. It will be used to get or delete the river.
Name is a fancy name for the river.
Indexname is where your documents will be send.
Typename is the type name under your documents will be indexed.
Start indicates if the river is running (true) or not (false).


### Examples


```sh
# GET all existing rivers
curl -XGET 'localhost:8080/scrutmydocs/1/settings/rivers/'
```



FileSystem Rivers (FSRivers)
----------------------------

You can manage your file system rivers with the FSRivers API.

### REST Resources

<table>
	<thead>
		<tr>
			<th>Resource</th>
			<th>Description</th>
		</tr>
	</thead>
	<tbody>
	    <tr>
			<td>GET 1/settings/rivers/fs/_help</td>
			<td>Display help.</td>
	    </tr>
	    <tr>
			<td>GET 1/settings/rivers/fs</td>
			<td>Get all existing Filesystem rivers (it will provide an array of FSRiver objects).</td>
	    </tr>
	    <tr>
			<td>GET 1/settings/rivers/fs/{name}</td>
			<td>Get one filesystem river (see FSRiver object).</td>
	    </tr>
	    <tr>
			<td>POST 1/settings/rivers/fs</td>
			<td>Create or update a FSRiver (see FSRiver Object). The river is not automatically started.</td>
	    </tr>
	    <tr>
			<td>PUT 1/settings/rivers/fs</td>
			<td>Same as POST.</td>
	    </tr>
	    <tr>
			<td>DELETE 1/settings/rivers/fs/{name}</td>
			<td>Remove a filesystem river.</td>
	    </tr>
	    <tr>
			<td>GET 1/settings/rivers/fs/{name}/start</td>
			<td>Start a river</td>
	    </tr>
	    <tr>
			<td>GET 1/settings/rivers/fs/{name}/stop</td>
			<td>Stop a river</td>
	    </tr>
    </tbody>
</table>

### FSRiver Object

A fsriver object looks like:

```javascript
{
	 "id" : "mydummyriver",
	 "name" : "My Dummy River",
	 "indexname" : "docs",
	 "typename" : "doc",
	 "start" : false,
	 "url" :"/tmp/docs",
	 "updateRate" : 300,
	 "includes" : "*.doc",
	 "excludes" : "resume*",
	 "analyzer" : "french"
}
```

Id is the unique name of your river. It will be used to get or delete the river.
Name is a fancy name for the river.
Indexname is where your documents will be send.
Typename is the type name under your documents will be indexed.
Start indicates if the river is running (true) or not (false).
Url is the root where FS River begins to crawl.
UpdateRate is the frequency (in seconds).
Includes is used when you want to index only some files (can be null aka every file is indexed).
Excludes is used when you want to exclude some files from the include list (can be null aka every file is indexed).
Analyzer is the analyzer to apply for this river ("default" or "french" by now).


### Examples


```sh
# CREATE a new river
curl -XPUT 'localhost:8080/scrutmydocs/1/settings/rivers/fs/' -d '
{
	 "id" : "mydummyriver",
	 "name" : "My Dummy River",
	 "indexname" : "docs",
	 "typename" : "doc",
	 "start" : false,
	 "url" :"/tmp/docs",
	 "updateRate" : 300,
	 "includes" : "*.doc",
	 "excludes" : "resume*",
	 "analyzer" : "french"
}
'    					 


# START a river
curl -XGET 'localhost:8080/scrutmydocs/1/settings/rivers/fs/mydummyriver/start'  

# STOP a river
curl -XGET 'localhost:8080/scrutmydocs/1/settings/rivers/fs/mydummyriver/stop'  

# DELETE a river
curl -XDELETE 'localhost:8080/scrutmydocs/1/settings/rivers/fs/mydummyriver'         
```

