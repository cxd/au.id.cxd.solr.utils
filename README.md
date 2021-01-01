# Solr Utils

This is a simple utility that can be used import files into an existing [Apache Solr](https://lucene.apache.org/solr/) core allowing the material to be indexed and searched with apache solr search engine.

The core is expected to have been configured for import of content via the tikka extract urls (/update/extract).

This utility is packaged via "universal:packageBin".

Once built and packaged the resulting program can be invoked with the command in the bin directory.

```aidl
bin/au-id-cxd-solr-utils --sourcePath /path/to/dir/to/import --configFile /path/to/custom/solr.conf
```

The two parameters above are:

### --sourcePath

The source path is the directory that may contain subfolders hosting the documents that are to be imported into the repository. The directory is filtered using the filter option that is specified in the configuration file.

### --configFile

The config file parameter defines the path to a configuration file such as ./solr.conf which contains the settings that are used for the import of resources into solr. Refer to the section on configuration below.

## Configuration

The configuration specifies where the solr server is located, which core to import into as well as the filter and mimetypes to use for the content to be imported.
It is in the HOCON format from the typesafe configuration library (https://github.com/lightbend/config).

An example listing is below:

```aidl
solr {
  host: "http://local:8983/solr",
  core: "gettingstarted",
  connectTimeout:10000,
  socketTimeout:60000,
  filter: "**/*.pdf",
  contentType: "application/pdf"
}
```
The properties are as follows.

### host
The host specifies the path to the solr instance which can be used to import the resources.

### core

The core defines the selected core that is configured to receive media such as word or pdf documents.

### connectTimeout

The timeout for the http library to make a request.

### socketTimeout

The timeout to keep the underlying socket connection alive when idle.

### filter

The filter specifies a pattern for file globbing from the source directory. In the example the path is specified as **, this allows recursive search below a tree of arbitrary depth, and the file component is *.pdf to wildcard match any filename ending in pdf.

### contentType

The content type corresponds to the mime type of the media when streamed to the /update/extract endpoint within the solr instance, in the example of pdf, it is "application/pdf".

## Overall usage.

As an example, lets say the file solr.conf is created in the same bin directory with the appropriate configuration for your environment and that the content to import may be located in ~/example/topic, then the command to import the content from that directory will be:

```aidl
bin/au-id-cxd-solr-utils --sourcePath ~/example/topc --configFile ./solr.conf
```

The program will output a list of files that it processes or otherwise will print an exception and terminate.


## Attributes of interest.

The resulting data set may be queried using a query such as:

http://localhost:8983/solr/gettingstarted/select?df=attr_content&fl=id%2C%20name%2Cattr_category%2Cattr_title%2Cattr_meta_author%2Cattr_created%2Cattr_date&hl.fl=attr_content&hl=on&q=*types*&rows=100


Of course make sure to replace the host and core with those from your environment.

Note that the query field "df" that is searched is __attr_content__.

However, if searching titles only it is possible to leverage either:

  - attr_title
  - attr_dc_title
  - attr_pdf_docinfo_title
    
The latter potentially being available on most documents, I have noticed that attr_title may not always be correct on documents. 

Additional fields of interest may include:

  - id
  - attr_name
  - attr_date 
  - attr_title
  - attr_pdf_docinfo_title 
  - attr_date
  - attr_created
  - attr_meta_author
  - attr_categories

Note however that the attr_name stores the name of the original file, also attr_categories is derived from the name of the directory containing the resource. These attributes may be used with a variety of open source search UI's when displaying search results from solr, or when developing your own custom search tools on top of solr. Each are derived from those created by the Cell content extraction with the exception of attr_name and attr_categories.

Note also the id is the MD5 of the byte contents of each document, and is unique even if the document has been duplicated below multiple subdirectories.



