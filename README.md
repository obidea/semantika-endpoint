Sesame meets Semantika
======================

Enabling Semantika SPARQL endpoint using Sesame Workbench.

Easy Installation
-----------------

If you are a new user of Sesame Workbench and never install one before then this guide is the best option for you.

* [Download and install Tomcat 7.x](http://tomcat.apache.org/download-70.cgi) into your local machine.
* Download and unzip the modified OpenRDF-Sesame WAR packages:
[semantika-sesame-1.0.zip](https://github.com/obidea/semantika-sesame/releases/download/v1.0/semantika-sesame-1.0.zip)
* Deploy the WAR packages to "$CATALINA_HOME/webapps/" where "$CATALINA_HOME" represents the location directory of your
Tomcat installation. In UNIX, the usual location is "/usr/share/tomcat7"
* Run Tomcat. The web container will unpack the WAR packages and run the servlets.
* Go to address `http://localhost:8080/openrdf-workbench` and the workbench page should appear.
* Try to create "New repository" using the left menu and select the "Type: Semantika Virtual RDF Store".


Manual Installation
-------------------

If you have already Sesame Workbench in your system then follow this manual configuration.

* Download and unzip the required files:
[manual-install.zip](https://github.com/obidea/semantika-sesame/releases/download/v1.0/manual-install.zip)

**Required library placement**:
* Place the Semantika Core library `semantika-core-x.x.jar` and Semantika Sesame library `semantika-sesame-x.x.jar`
to "$OPENRDF_SESAME/WEB-INF/lib/" and "$OPENRDF_WORKBENCH/WEB-INF/lib/" where "$OPENRDF_SESAME" represents the location
directory of the openrdf-sesame webapp and "$OPENRDF_WORKBENCH" represents the location of the openrdf-workbench webapp.
* Place other libraries in "$OPENRDF_SESAME/WEB-INF/lib/" only.
* In addition, you may need to place JDBC driver according to your database engine in "$OPENRDF_SESAME/WEB-INF/lib/"

**Form style sheets modification**:
* Place `create-semantika-vrepo.xsl` file in "$OPENRDF_WORKBENCH/transformations/".
* Go to "$OPENRDF_WORKBENCH/transformations/" and edit `create.xsl`. Add these lines below inside `<table class="dataentry">
section.
```xml
<option value="semantika-vrepo">
   Semantika Virtual RDF Store
</option>
```
* Go to "$OPENRDF_WORKBENCH/locale/" and edit `messages.xsl`. Add these lines below at the end of the `<stylesheet>`
section.
```xml
<!-- Semantika Fields -->
<variable name="semantika-config.label">Configuration path</variable>
```

**Repository config placement**:
* Place `semantika-vrepo.ttl` in "$OPENRDF_WORKBENCH/WEB-INF/classes/org/openrdf/repository/config".
Create this structure if the directories don't exist.


* Restart Tomcat.
* Go to address `http://localhost:8080/openrdf-workbench` and create "New repository".
* A new create entry "Type: Semantika Virtual RDF Store" should appear.
