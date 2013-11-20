VideoApp-Windows
================

Build Instructions:
...................

IntelliJIdea

Create Project_Name.iml file in project root folder.

Add the following:

<?xml version="1.0" encoding="UTF-8"?>
<module type="JAVA_MODULE" version="4">
  <component name="NewModuleRootManager" inherit-compiler-output="true">
    <exclude-output />
    <content url="file://$MODULE_DIR$">
      <sourceFolder url="file://$MODULE_DIR$/src" isTestSource="false" />
    </content>
    <orderEntry type="inheritedJdk" />
    <orderEntry type="sourceFolder" forTests="false" />
  </component>
</module>

Then go to .idea/modules.xml and add that Project_Name.iml like following:

<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="ProjectModuleManager">
    <modules>
      <module fileurl="file://$PROJECT_DIR$/Project_Name.iml" filepath="$PROJECT_DIR$/Project_Name.iml" />
    </modules>
  </component>
</project>

...................

Eclipse
___________________

What this program does:

It accepts clients, trying to connect and saves files they upload in specified folder.
___________________

How to work

Just enter the folder to save the file and then click the "Run" button.
___________________

