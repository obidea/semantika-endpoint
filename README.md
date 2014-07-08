Semantika Sesame
================

This project aims to setup a Semantika SPARQL endpoint through Sesame Workbench. The endpoint uses the underlying database system to answer SPARQL queries from users.

Latest release: [1.2 (build 17) is available!](https://github.com/obidea/semantika-sesame/releases/tag/v1.2_17) (July 8, 2014)

Easy Installation
-----------------

* [Download and install Tomcat 7.x](http://tomcat.apache.org/download-70.cgi) into your local machine.
* [Download the latest release of Semantika Sesame project bundle](https://github.com/obidea/semantika-sesame/releases). The zip contains two WAR files, i.e., `openrdf-sesame.war` and `openrdf-workbench.war`
* Deploy the WAR packages to `$CATALINA_HOME/webapps/` where `$CATALINA_HOME` represents the location directory of your
Tomcat installation. In UNIX, the usual location is "/usr/share/tomcat7".
* Run Tomcat. The web container will unpack the WAR packages and run the servlets.
* Go to address "http://localhost:8080/openrdf-workbench" using a web browser.

**To test the installation:** In the workbench page, create "New repository" using the left menu. A "New Repository"
page appears with "Type: Semantika Virtual RDF Store" shows in the drop-down menu.


Creating an Endpoint
--------------------

Below is an example for creating an endpoint for our [Semantika-MusicBrainz](https://github.com/obidea/semantika-musicbrainz) project.

* To create an endpoint, you will need a configuration file that provides the application resources.
```
<semantika-configuration>
   <application-factory name="musicbrainz-endpoint">
      <data-source>
         <property name="connection.url">jdbc:h2:tcp://localhost/mbzdb</property>
         <property name="connection.driver_class">org.h2.Driver</property>
         <property name="connection.username">sa</property>
         <property name="connection.password"></property>
      </data-source>
      <mapping-source resource="file:///home/user/musicbrainz/res/artist.tml.xml" strict-parsing="false"/>
   </application-factory>
</semantika-configuration>
```
Notice that the URL protocol `file://` is used to locate the mapping resource at `/home/user/musicbrainz/res/artist.tml.xml`

* Complete the "New Repository" form by filling the ID, title and configuration file location.

![alt tag](https://raw.githubusercontent.com/obidea/semantika-sesame/master/img/create-endpoint-1.png)

* Once the endpoint was succesfully created, make a query using the "Query" menu item. The example asks for all artists that performed as a solo artist and each name.

![alt tag](https://raw.githubusercontent.com/obidea/semantika-sesame/master/img/create-endpoint-2.png)

* Check the returned results.

![alt tag](https://raw.githubusercontent.com/obidea/semantika-sesame/master/img/create-endpoint-3.png)


Need Help?
----------

Check [our Wikipage](https://github.com/obidea/semantika-api/wiki) for a brief introduction.
Need more help? Join [OBDA Semantika Forum](https://groups.google.com/forum/#!forum/obda-semantika).
