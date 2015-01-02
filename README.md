Basic Computer Graphics Project 2
===========

Code for the second project of the Basic Computer Graphics class at UFRPE (Universidade Federal Rural de Pernambuco).

## How to configure a development environment
Clone the repository on a directory of your choice.

Then, download the JOGL library [here](http://jogamp.org/deployment/jogamp-current/archive/jogamp-all-platforms.7z "JOGAMP.ORG").
Extract it anywhere that is convenient to you.

On Eclipse, right-click on your project, select "Build Path" and click on "Configure Build Path...".
Select the "Libraries" tab and click to "Add External JARs...". Navigate to where you extracted the JOGL library and enter the "jar" folder.
Select "gluegen-rt.jar" and "jogl-all.jar", then click "OK". Go to the tab "Order and Export" and check both jars check boxes.
Hit "OK".

Now open the directory where you extracted the JOGL library. You need to navigate to "lib/linux-amd64" and copy it to the folder "lib-unix64/" inside the project's root folder.
Repeat it for the contents of the "lib/windows-amd64", copying them to the folder "lib-win64" of the project's root folder. You should now be OK to develop the project.
