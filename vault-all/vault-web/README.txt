===Part I: create module(for config properties) in jbossas 7===
1 . Create a new module for your configuration files
${JBOSSAS-7_HOME}/modules/com/redhat/tools/vault/conf/main/module.xml

<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="com.redhat.tools.vault.conf">
	<resources>
		<resource-root path="."/>
	</resources>
</module>

2 . Edit the properties file (the key=value pattern)

attachmentpath=/home/wguo/data/vault/
maxsizeKbyte=102400 

3 . Add the properties files to the module

jboss-as-7/
   modules/
      com/
         redhat/
            tools/
               vault/
                  conf/
                    main/
                      module.xml
                      config.properties
   
                      
4 . The developer will use the module name in the jboss-deployment-structure.xml file

<?xml version="1.0" encoding="UTF-8"?>
<jboss-deployment-structure>
  <deployment>
    <dependencies>
      <module name="com.redhat.tools.vault.conf" />
    </dependencies>
  </deployment>
</jboss-deployment-structure>
