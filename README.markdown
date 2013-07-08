Scrut My Docs
=========

Introduction
------------

Scrut My Docs is a web application using elasticsearch.
With ScrutMyDocs you can Send, Look for and find all your documents.

Versions
--------

<table>
    <thead>
        <tr>
            <td>Scrutmydocs</td>
            <td>ElasticSearch</td>
            <td>FS River Plugin</td>
            <td>Dropbox River Plugin</td>
            <td>Jira River Plugin</td>
            <td>Attachment Plugin</td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>master (0.3.0-SNAPSHOT)</td>
            <td>0.90.3</td>
            <td>0.3.0</td>
            <td>0.2.0</td>
            <td>1.3.0</td>
            <td>1.8.0</td>
        </tr>
        <tr>
            <td>0.2.0</td>
            <td>0.19.9</td>
            <td>0.0.2</td>
            <td></td>
            <td></td>
            <td>1.4.0</td>
        </tr>
        <tr>
            <td>0.1.0</td>
            <td>0.19.8</td>
            <td>0.0.2</td>
            <td></td>
            <td></td>
            <td>1.4.0</td>
        </tr>
    </tbody>
</table>


Installation
------------

Download the Web Application aRchive (WAR) and drop it in the deploy folder of your favorite container (Tomcat, JBoss, ...).
In your browser, open <http://localhost:8080/scrutmydocs/>

### Deploy locally

If you don't have a Java container (like Tomcat, JBoss, ...) but want to test ScrutMyDocs into your local machine can try to run it with the following command (tested on Ubuntu).

Download jetty-runner from official repository:

 * http://repo2.maven.org/maven2/org/mortbay/jetty/jetty-runner/

currently latest version is 8.1.5.v20120716 so:

```sh
wget http://repo2.maven.org/maven2/org/mortbay/jetty/jetty-runner/8.1.5.v20120716/jetty-runner-8.1.5.v20120716.jar
```

install required SDK:

```sh
sudo apt-get install openjdk-6-jdk
```

download ScrutMyDocs:

```sh
wget https://github.com/downloads/scrutmydocs/scrutmydocs/scrutmydocs-0.3.0.war
```

and run application with:

```sh
java -jar jetty-runner-8.1.5.v20120716.jar scrutmydocs-0.3.0.war
```

now you can see ScrutMyDocs on your local machine, open the browser and open the page:

 * http://localhost:8080/

NOTE: please configure also _elasticsearch_, see next section.

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
curl -L -C - -O https://github.com/downloads/elasticsearch/elasticsearch/elasticsearch-0.90.0.deb
dpkg -i elasticsearch-0.90.0.deb
```

##### OSX
With [Homebrew](http://mxcl.github.com/homebrew/)
```sh
brew install elasticsearch
```

#### Add required plugins

* [mapper-attachment](https://github.com/elasticsearch/elasticsearch-mapper-attachments)
* [fsriver](https://github.com/dadoonet/fsriver)
* [dropbox](https://github.com/dadoonet/dropbox)
* [google-drive-river](https://github.com/lbroudoux/es-google-drive-river)
* [amazon-s3-river](https://github.com/lbroudoux/es-amazon-s3-river)
* [jira-river](https://github.com/jbossorg/elasticsearch-river-jira)

```sh
service elasticsearch stop
/usr/share/elasticsearch/bin/plugin -install elasticsearch/elasticsearch-mapper-attachments/1.8.0
/usr/share/elasticsearch/bin/plugin -install fr.pilato.elasticsearch.river/fsriver/0.3.0
/usr/share/elasticsearch/bin/plugin -install fr.pilato.elasticsearch.river/dropbox/0.2.0
/usr/share/elasticsearch/bin/plugin -install com.github.lbroudoux.elasticsearch/google-drive-river/0.0.1
/usr/share/elasticsearch/bin/plugin -install com.github.lbroudoux.elasticsearch/amazon-s3-river/0.0.1
/usr/share/elasticsearch/bin/plugin -url https://repository.jboss.org/nexus/content/groups/public-jboss/org/jboss/elasticsearch/elasticsearch-river-jira/1.4.1/elasticsearch-river-jira-1.4.1.zip -install elasticsearch-river-jira
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

If you want to use Dropbox rivers, you must define first your dropbox credentials for your own
[Dropbox Application](https://www.dropbox.com/developers/apps):

```
# If you want to use Dropbox river, define your App key and Secret here
# You can create your application on https://www.dropbox.com/developers/apps
dropbox.app.key=yourkeyhere
dropbox.app.secret=yoursecrethere
```

You can now deploy the web application in your container.


Scrut My Docs REST API Resources
================================

Common based API
----------------

API are running at scrutmydocs/api point. It could be a nice idea to use a proxy to
rewrite urls.

Let say you are running scrutmydocs at http://scrutmydocs.org/scrutmydocs/, you should set your proxy to rewrite
* http://api.scrutmydocs.org to http://scrutmydocs.org/scrutmydocs/api/ and
* http://demo.scrutmydocs.org to http://scrutmydocs.org/scrutmydocs/

Then, the common base URL for API will be http://api.scrutmydocs.org


Each API provide help with the `_help` entry point.

```sh
curl 'localhost:8080/scrutmydocs/api/_help'    					 
```

The following command will give you existing APIs.

```sh
curl 'localhost:8080/scrutmydocs/api/'    					 
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
curl -XPOST 'localhost:8080/scrutmydocs/api/1/doc/ -d '
{
     "id" :"myid1",
	 "name" : "mydocument",
	 "contentType" : "application/pdf",
	 "content" : " BASE64 encoded file content "
}
'    					 

# Add a document to the search engine
curl -XPOST 'localhost:8080/scrutmydocs/api/1/doc/ -d '
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
curl -XGET 'localhost:8080/scrutmydocs/api/1/doc/myid1/'
     	 
# Get a document in a specific index/type
curl -XGET 'localhost:8080/scrutmydocs/api/1/doc/docs/doc/myid2/'   			 	     

# DELETE a document in the default index/type  (docs/doc)
curl -XDELETE 'localhost:8080/scrutmydocs/api/1/doc/myid1/'         

# DELETE a document in a specific index/type
curl -XDELETE 'localhost:8080/scrutmydocs/api/1/doc/docs/doc/myid2/'    			 	    
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
curl -XPOST 'localhost:8080/scrutmydocs/api/1/index/ -d '
{
     "index" :"myindex",
	 "type" : "mytype",
	 "analyzer" : "french"
}
'    					 

# DELETE an index
curl -XDELETE 'localhost:8080/scrutmydocs/api/1/index/myindex'         
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

* `search` is the text to search. You can use a Lucene syntax.
* `first` is the page number (default to 0).
* `pageSize` is the size of a page (aka number of results to fetch).

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

* `took` is the time in milliseconds.
* `totalHits` is the total number of hits.
* `hits` contains an array of Hit objects.

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

* `id` is the unique internal ID of the document.
* `type` is the object type (default to doc).
* `index` is the object index (default to docs)
* `contentType` is the document content type.
* `source` is always null as we don't provide content by now.
* `title` is the document filename.
* `highlights` may contain an array of String which highlights the document content with the searched terms. 


### Examples


```sh
# SEARCH for apache term
curl -XGET 'localhost:8080/scrutmydocs/api/1/search/apache'         

# SEARCH for apache term, starting from page 2 with 20 results
curl -XPOST 'localhost:8080/scrutmydocs/api/1/search/apache -d '
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

* `id` is the unique name of your river. It will be used to get or delete the river.
* `name` is a fancy name for the river.
* `indexname` is where your documents will be send.
* `typename` is the type name under your documents will be indexed.
* `start` indicates if the river is running (true) or not (false).


### Examples


```sh
# GET all existing rivers
curl -XGET 'localhost:8080/scrutmydocs/api/1/settings/rivers/'
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
	 "protocol" : "ssh",
	 "server" : "localhost",
	 "username" : "sshlogin",
	 "password" : "sshpassword",
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

* `id` is the unique name of your river. It will be used to get or delete the river.
* `name` is a fancy name for the river.
* `protocol` could be `local` (default) or `ssh`.
* `server` SSH server name when protocol is `ssh`.
* `username` SSH username when protocol is `ssh`.
* `password` SSH password when protocol is `ssh`.
* `indexname` is where your documents will be send.
* `typename` is the type name under your documents will be indexed.
* `start` indicates if the river is running (true) or not (false).
* `url` is the root where FS River begins to crawl.
* `updateRate` is the frequency (in seconds).
* `includes` is used when you want to index only some files (can be null aka every file is indexed).
* `excludes` is used when you want to exclude some files from the include list (can be null aka every file is indexed).
* `analyzer` is the analyzer to apply for this river ("default" or "french" by now).


### Examples


```sh
# CREATE a new river
curl -XPUT 'localhost:8080/scrutmydocs/api/1/settings/rivers/fs/' -d '
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
' -H "Content-Type: application/json" -H "Accept: application/json"


# START a river
curl -XGET 'localhost:8080/scrutmydocs/api/1/settings/rivers/fs/mydummyriver/start'

# STOP a river
curl -XGET 'localhost:8080/scrutmydocs/api/1/settings/rivers/fs/mydummyriver/stop'

# DELETE a river
curl -XDELETE 'localhost:8080/scrutmydocs/api/1/settings/rivers/fs/mydummyriver'         
```

Dropbox Rivers (DropboxRivers)
------------------------------

You can manage your Dropbox rivers with the DropboxRivers API.

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
			<td>GET 1/settings/rivers/dropbox/_help</td>
			<td>Display help.</td>
	    </tr>
	    <tr>
			<td>GET 1/settings/rivers/dropbox</td>
			<td>Get all existing Dropbox rivers (it will provide an array of DropboxRiver objects).</td>
	    </tr>
	    <tr>
			<td>GET 1/settings/rivers/dropbox/{name}</td>
			<td>Get one Dropbox river (see DropboxRiver object).</td>
	    </tr>
	    <tr>
			<td>POST 1/settings/rivers/dropbox</td>
			<td>Create or update a DropboxRiver (see DropboxRiver Object). The river is not automatically started.</td>
	    </tr>
	    <tr>
			<td>PUT 1/settings/rivers/dropbox</td>
			<td>Same as POST.</td>
	    </tr>
	    <tr>
			<td>DELETE 1/settings/rivers/dropbox/{name}</td>
			<td>Remove a Dropbox river.</td>
	    </tr>
	    <tr>
			<td>GET 1/settings/rivers/dropbox/{name}/start</td>
			<td>Start a river</td>
	    </tr>
	    <tr>
			<td>GET 1/settings/rivers/dropbox/{name}/stop</td>
			<td>Stop a river</td>
	    </tr>
    </tbody>
</table>

### DropboxRiver Object

A Dropboxriver object looks like:

```javascript
{
    "id" : "mydummyriver",
    "name" : "My Dummy River",
    "indexname" : "docs",
    "typename" : "doc",
    "start" : false,
    "url" :"/dbfolder",
    "token":"userdropboxtoken",
    "secret":"userdropboxsecret",
    "updateRate" : 300,
    "includes" : "*.doc",
    "excludes" : "resume*",
    "analyzer" : "french"
}
```

* `id` is the unique name of your river. It will be used to get or delete the river.
* `name` is a fancy name for the river.
* `indexname` is where your documents will be send.
* `typename` is the type name under your documents will be indexed.
* `start` indicates if the river is running (true) or not (false).
* `url` is the root where FS River begins to crawl.
* `token` is the user token you get from Dropbox.
* `secret` is the user secret you get from Dropbox.
* `updateRate` is the frequency (in seconds).
* `includes` is used when you want to index only some files (can be null aka every file is indexed).
* `excludes` is used when you want to exclude some files from the include list (can be null aka every file is indexed).
* `analyzer` is the analyzer to apply for this river ("default" or "french" by now).

> Note that you will have to conform to the OAuth process to get authorization token from the user.

### Examples


```sh
# CREATE a new river
curl -XPUT 'localhost:8080/scrutmydocs/api/1/settings/rivers/dropbox/' -d '
{
    "id" : "mydummyriver",
    "name" : "My Dummy River",
    "indexname" : "docs",
    "typename" : "doc",
    "start" : false,
    "url" :"/dbfolder",
    "token":"userdropboxtoken",
    "secret":"userdropboxsecret",
    "updateRate" : 300,
    "includes" : "*.doc",
    "excludes" : "resume*",
    "analyzer" : "french"
}
' -H "Content-Type: application/json" -H "Accept: application/json"


# START a river
curl -XGET 'localhost:8080/scrutmydocs/api/1/settings/rivers/dropbox/mydummyriver/start'

# STOP a river
curl -XGET 'localhost:8080/scrutmydocs/api/1/settings/rivers/dropbox/mydummyriver/stop'

# DELETE a river
curl -XDELETE 'localhost:8080/scrutmydocs/api/1/settings/rivers/dropbox/mydummyriver'
```

Google Drive Rivers (DriveRivers)
------------------------------

You can manage your Google Drive rivers with the DriveRivers API.

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
         <td>GET 1/settings/rivers/drive/_help</td>
         <td>Display help.</td>
       </tr>
       <tr>
         <td>GET 1/settings/rivers/drive</td>
         <td>Get all existing Google Drive rivers (it will provide an array of S3River objects).</td>
       </tr>
       <tr>
         <td>GET 1/settings/rivers/drive/{name}</td>
         <td>Get one Google Drive river (see DriveRiver object).</td>
       </tr>
       <tr>
         <td>POST 1/settings/rivers/drive</td>
         <td>Create or update a Google Drive river (see DriveRiver Object). The river is not automatically started.</td>
       </tr>
       <tr>
         <td>PUT 1/settings/rivers/drive</td>
         <td>Same as POST.</td>
       </tr>
       <tr>
         <td>DELETE 1/settings/rivers/drive/{name}</td>
         <td>Remove a Google Drive river.</td>
       </tr>
       <tr>
         <td>GET 1/settings/rivers/drive/{name}/start</td>
         <td>Start a river</td>
       </tr>
       <tr>
         <td>GET 1/settings/rivers/drive/{name}/stop</td>
         <td>Stop a river</td>
       </tr>
    </tbody>
</table>

### DriveRiver Object

A DriveRiver object looks like (see https://github.com/lbroudoux/es-google-drive-river for updated details):

```javascript
{
    "id" : "mydummyriver",
    "name" : "My Dummy River",
    "indexname" : "docs",
    "typename" : "doc",
    "start" : false,
    "url" :"Work",
    "folder" :"Work",
    "clientId":"GoogleDriveClientId",
    "clientSecret":"GoogleDriveClientSecret",
    "refreshToken":"GoogleDriveRefreshToken",
    "updateRate" : 300,
    "includes" : "*.doc",
    "excludes" : "resume*",
    "analyzer" : "french"
}
```

* `id` is the unique name of your river. It will be used to get or delete the river.
* `name` is a fancy name for the river.
* `indexname` is where your documents will be send.
* `typename` is the type name under your documents will be indexed.
* `start` indicates if the river is running (true) or not (false).
* `url` is the root folder where River begins to crawl within the Drive.
* `folder` is the root folder where River begins to crawl within the Drive (same as url).
* `clientId` is the client Id your Google Drive app is registred.
* `clientSecret` is the client secret your Google Drive app is registred.
* `refreshToken` is the refresh token you get when running the OAuth process (see https://github.com/lbroudoux/es-google-drive-river) 
* `updateRate` is the frequency (in seconds).
* `includes` is used when you want to index only some files (can be null aka every file is indexed).
* `excludes` is used when you want to exclude some files from the include list (can be null aka every file is indexed).
* `analyzer` is the analyzer to apply for this river ("default" or "french" by now).

> Note that you will have to conform to the OAuth process to get refresh token from the user.

### Examples


```sh
# CREATE a new river
curl -XPUT 'localhost:8080/scrutmydocs/api/1/settings/rivers/drive/' -d '
{
    "id" : "mydummyriver",
    "name" : "My Dummy River",
    "indexname" : "docs",
    "typename" : "doc",
    "start" : false,
    "url" :"MyFolder",
    "folder" :"MyFolder",
    "clientId":"AAAAAAAAAAAAAAAA",
    "clientSecret":"BBBBBBBBBBBBBB",
    "refreshToken":"CCCCCCCCCCCCCC",
    "updateRate" : 300,
    "includes" : "*.doc",
    "excludes" : "resume*",
    "analyzer" : "french"
}
' -H "Content-Type: application/json" -H "Accept: application/json"


# START a river
curl -XGET 'localhost:8080/scrutmydocs/api/1/settings/rivers/drive/mydummyriver/start'

# STOP a river
curl -XGET 'localhost:8080/scrutmydocs/api/1/settings/rivers/drive/mydummyriver/stop'

# DELETE a river
curl -XDELETE 'localhost:8080/scrutmydocs/api/1/settings/rivers/drive/mydummyriver'
```


Amazon S3 Rivers (S3Rivers)
------------------------------

You can manage your Amazon S3 rivers with the S3Rivers API.

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
         <td>GET 1/settings/rivers/s3/_help</td>
         <td>Display help.</td>
       </tr>
       <tr>
         <td>GET 1/settings/rivers/s3</td>
         <td>Get all existing Amazon S3 rivers (it will provide an array of S3River objects).</td>
       </tr>
       <tr>
         <td>GET 1/settings/rivers/s3/{name}</td>
         <td>Get one Amazon S3 river (see S3River object).</td>
       </tr>
       <tr>
         <td>POST 1/settings/rivers/s3</td>
         <td>Create or update an Amazon (see S3River Object). The river is not automatically started.</td>
       </tr>
       <tr>
         <td>PUT 1/settings/rivers/s3</td>
         <td>Same as POST.</td>
       </tr>
       <tr>
         <td>DELETE 1/settings/rivers/s3/{name}</td>
         <td>Remove an Amazon S3 river.</td>
       </tr>
       <tr>
         <td>GET 1/settings/rivers/s3/{name}/start</td>
         <td>Start a river</td>
       </tr>
       <tr>
         <td>GET 1/settings/rivers/s3/{name}/stop</td>
         <td>Stop a river</td>
       </tr>
    </tbody>
</table>

### S3River Object

A S3River object looks like (see https://github.com/lbroudoux/es-amazon-s3-river for updated details):

```javascript
{
    "id" : "mydummyriver",
    "name" : "My Dummy River",
    "indexname" : "docs",
    "typename" : "doc",
    "start" : false,
    "bucket" : "myownbucket"
    "url" :"Work/",
    "pathPrefix" :"Work/",
    "accessKey":"AWSAccountAccessKey",
    "secretKey":"AWSAccountSecretKey",
    "updateRate" : 300,
    "includes" : "*.doc",
    "excludes" : "resume*",
    "analyzer" : "french"
}
```

* `id` is the unique name of your river. It will be used to get or delete the river.
* `name` is a fancy name for the river.
* `indexname` is where your documents will be send.
* `typename` is the type name under your documents will be indexed.
* `start` indicates if the river is running (true) or not (false).
* `bucket` is the name of the Amazon S3 bucket being crawled.
* `url` is the path prefix of the S3 River begins to crawl within the bucket (note the trailing `/`).
* `pathPrefix` is the root where FS River begins to crawl within the bucket (same as url).
* `accessKey` is the access key of your Amazon Web Services account.
* `secretKey` is the secret key of your Amazon Web Services account.
* `updateRate` is the frequency (in seconds).
* `includes` is used when you want to index only some files (can be null aka every file is indexed).
* `excludes` is used when you want to exclude some files from the include list (can be null aka every file is indexed).
* `analyzer` is the analyzer to apply for this river ("default" or "french" by now).


### Examples


```sh
# CREATE a new river
curl -XPUT 'localhost:8080/scrutmydocs/api/1/settings/rivers/s3/' -d '
{
    "id" : "mydummyriver",
    "name" : "My Dummy River",
    "indexname" : "docs",
    "typename" : "doc",
    "start" : false,
    "bucket" : "MyBucket",
    "url" :"MyFolder/",
    "pathPrefix" :"MyFolder/",
    "accessKey":"AAAAAAAAAAAAAAAA",
    "secretKey":"BBBBBBBBBBBBBB",
    "updateRate" : 300,
    "includes" : "*.doc",
    "excludes" : "resume*",
    "analyzer" : "french"
}
' -H "Content-Type: application/json" -H "Accept: application/json"


# START a river
curl -XGET 'localhost:8080/scrutmydocs/api/1/settings/rivers/s3/mydummyriver/start'

# STOP a river
curl -XGET 'localhost:8080/scrutmydocs/api/1/settings/rivers/s3/mydummyriver/stop'

# DELETE a river
curl -XDELETE 'localhost:8080/scrutmydocs/api/1/settings/rivers/s3/mydummyriver'
```

JIRA Rivers
-----------

You can manage your JIRA rivers with the JIRA Rivers API.

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
         <td>GET 1/settings/rivers/jira/_help</td>
         <td>Display help.</td>
       </tr>
       <tr>
         <td>GET 1/settings/rivers/jira</td>
         <td>Get all existing JIRA rivers (it will provide an array of JiraRiver objects).</td>
       </tr>
       <tr>
         <td>GET 1/settings/rivers/jira/{id}</td>
         <td>Get one JIRA river (see JiraRiver object).</td>
       </tr>
       <tr>
         <td>POST 1/settings/rivers/jira</td>
         <td>Create or update a JIRA river (see JiraRiver Object). The river is not automatically started.</td>
       </tr>
       <tr>
         <td>PUT 1/settings/rivers/jira</td>
         <td>Same as POST.</td>
       </tr>
       <tr>
         <td>DELETE 1/settings/rivers/jira/{id}</td>
         <td>Remove a JIRA river.</td>
       </tr>
       <tr>
         <td>GET 1/settings/rivers/jira/{id}/start</td>
         <td>Start a river</td>
       </tr>
       <tr>
         <td>GET 1/settings/rivers/drive/{name}/stop</td>
         <td>Stop a river</td>
       </tr>
    </tbody>
</table>

### JiraRiver Object

A JiraRiver object looks like (see https://github.com/jbossorg/elasticsearch-river-jira for updated details):

```javascript
{
    "id": "mydummyriver",
    "name": "My Dummy River",
    "indexname": "my_jira_index",
    "typename": "jira_issue",
    "start": false,
    "type": "jira",
    "urlBase": "https://issues.jboss.org",
    "username": "jira_username",
    "pwd": "jira_user_password",
    "jqlTimeZone": "Europe/Paris",
    "timeout": "5s",
    "maxIssuesPerRequest": 50,
    "projectKeysIndexed": "TESTPROJECT",
    "indexUpdatePeriod": "5m",
    "indexFullUpdatePeriod": "1h",
    "maxIndexingThreads": 2,
    "analyzer": "keyword",
    "jiraIssueCommentType": "jira_issue_comment",
    "jiraRiverActivityIndexName": "jira_river_activity",
    "jiraRiverUpdateType": "jira_river_indexupdate"
}
```

* `id` is the unique name of your river. It is generated with a "trim()" on the river name parameter. Used to get or delete the river.
* `name` is a fancy name for the river.
* `indexname` is where JIRA components documents will be send
* `typename` is the type name under your JIRA compoents will be indexed. For the moment only jira_issue are processed.
* `start` indicates if the river is running (true) or not (false).
* `urlBase` is the base URL of the JIRA Instance
* `username` is the JIRA account user name
* `pwd` is the JIRA account user password
* `jqlTimeZone` is optional identifier of timezone used to format time values into JQL when requesting updated issues. Timezone of ElasticSearch JVM is used if not provided.
* `timeout` is time value, defines timeout for http/s REST request to the JIRA. Optional, 5s is default if not provided.
* `maxIssuesPerRequest` defines maximal number of updated issues requested from JIRA by one REST request. Optional, 50 used if not provided.
* `projectKeysIndexed` is a comma separated list of JIRA project keys to be indexed. If omitted all visible projects are fetched.
* `indexUpdatePeriod` is time value, defines how often is search index updated from JIRA instance. Optional, default 5 minutes.
* `maxIndexingThreads` defines maximal number of parallel indexing threads running for this river.
* `jiraIssueCommentType` defines the elasticsearch type used to index JIRA Issue Comments.
* `jiraRiverActivityIndexName` defines the index where JIRA River Activities are sent.
* `jiraRiverUpdateType` defines the elasticsearch type used to index JIRA River updates events.

> Some parameters are readonly for the moment in order to set JIRA River easily. Otherwise, there are required mapping to set before creating a JIRA River

### Examples


```sh

# START a river
curl -XGET 'localhost:8080/scrutmydocs/api/1/settings/rivers/jira/mydummyriver/start'

# STOP a river
curl -XGET 'localhost:8080/scrutmydocs/api/1/settings/rivers/jira/mydummyriver/stop'

# DELETE a river
curl -XDELETE 'localhost:8080/scrutmydocs/api/1/settings/rivers/jira/mydummyriver'

# GET a JIRA Issue
curl -XGET 'localhost:8080/scrutmydocs/api/1/jiraissue/{ISSUEKEY}'
```
