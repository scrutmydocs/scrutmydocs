ES-Search
=========

Introduction
------------

Es-search is an web application using elasticsearch.
With ScrutMyDocs you can Send, Look for and find all your documents.
 

Installation
------------



Setup
-----


RESTFul API
-----------

Each API provide help with the `_help` entry point.

```sh
curl 'localhost:8080/scrutmydocs/api/_help'    					 
```


### POST

```sh
# Add a document to the search engine
curl -XPOST 'localhost:8080/scrutmydocs/api/doc/{index}/{type}/{id}/'    					 
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

