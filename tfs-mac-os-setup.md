## How To Get TFS Working on macOS 🙁!

This guide is entirely intended for macOS users but could prove very useful for many Linux users and somewhat helpful for a few Windows users as well.

---

### Step 1: Convince your team to use Git 😀

This was the first solution I tried, and after well over a year of struggling to even get this to work it is the solution I recommend. It is still not an option for me and my team despite my lengthy efforts and thus the existence of this guide. But seriously, the (many) trials you will face are not absolutely not worth this effort if there is even a remote chance you can do the work to convert your project's source control over to Git. That said, if you are like me and a number of other voices I came across while researching this topic, read on...

### Step 2: Install Git.

Even if you failed at step 1, I recommend installing git to your machine (if it isn't already). We will be using Visual Studio Code as our editor and as our UI for source control and it will complain on launch if you do not have git installed (even if you only plan to work with TFS repos).
<https://sourceforge.net/projects/git-osx-installer/files/>

### Step 3: Install Visual Studio Code.

This is not technically necessary as everything you need to do can be done from the command line but the UI for handling your repo in VS Code is one of the only visual solutions out there for macOS and actually works pretty damn well (most of the time). The rest of this guide will be geared towards us using Visual Studio Code for most repository interaction.
<https://code.visualstudio.com/download>

### Step 4: Install Azure Repos extension.

In the extensions tab for VS Code search for Azure Repos (Microsoft's new 2019 ready name for TFS) and install that extension.

### Step 5: Install TFS extension.

In the extensions tab for VS Code search for TFS and install that extension. (Choose the most popularly downloaded one. There are one or two forks that have not been well maintained...)

### Step 6: Download and unzip Team Explorer Everywhere.

Microsoft has created version of its Team Foundation command line tools that works cross platform. They actually did this a few years ago, and there are a few YouTube videos they and others made about it, but I wouldn't say it was obvious what to do after this part from their articles and documentation. I think the home directory is a good place to put this unzipped folder.
<https://github.com/Microsoft/team-explorer-everywhere/releases>

### Step 7: Install Java Runtime Environment.

I highly recommend you use Java 8. I am currently using JRE version 1.8.0_131. This is very important. There were some library changes in versions 9 and 10 that the TEE-CLC tool will break on but will only silently log those errors in a hard to find Java error logs file. This was my biggest hurdle to getting through this whole process the first time. I read an article somewhere that explained how you could change the library call or something like that to account for the change in version 9/10 but ultimately that fix never worked for me even though a few people I saw that responded had it work for them. Your mileage may vary. If you already have Java installed you may need to reinstall altogether or point your PATH variable to the version 8 install.
<http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html>

### Step 8: Install Java SE Development Kit.

Similar notes to the previous step on what version to use. I'll keep this step simple and just include the link to the one I am currently using, JDK version 8u171. (I have seen variations on this export line, this is the one that worked for my setups.)
<http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html>

### Step 9: Set the JAVA_HOME variable on your shell profile.

Add the following line of code to the end of your shell profile.

    export JAVA_HOME=`/usr/libexec/java_home -v 1.8`

I am using zsh for my shell so I edited my .zshenv file.

    sudo nano ~/.zshenv

For many users this will simply be your bash profile.

    sudo nano ~/.bash_profile

Save your changes and move on.

### Step 10: Close and reopen your terminal.

Fully quit using Cmd + Q to reload your shell profile you added your path environment variable to in the previous step. You will likely get an error in the terminal if you did this wrong.

### Step 11: Change directory into the TEE-CLC folder and try to run tf.

Like this (subbed for the version you downloaded).

    cd TEE-CLC-14.134.0

Try to run the tf executable there.

    tf

At this point just running tf worked for me. I did try this on a second machine this week and had to run the executable from the proper directory (~/TEE-CLC-14.134.0/tf) everytime, even after the next step was succesful. Your mileage may vary. (Anyone have more insight on to what may have gone wrong or right for me in either siuation here?)

    ./tf

### Step 12: Run the tf EULA and accept.

Run ./tf eula and accept, accept, accept!

    ./tf eula

### Step 13: Create your local tf workspace.

In your terminal, do the following:

    tf workspace -new YourWorkspaceName -collection:https://tfs.YourTFSServerAddress.com/Whatever

This sets up the workspace locally. It will most likely ask for your username and password to authenticate with the TFS repo.

### Step 14: You may need a valid certificate to authenticate with your repo.

I do with my organization and my repos. You might be able to skip this step for your setup. This was a certificate the IT department puts on all of our machines. This was honestly one of the trickiest steps and you may need to do some research on your version of macOS and/or Java to do this correctly. You need to import certificate(s) to your Java cacerrts directory using the keytool application from the command line. I chose to export a copy of my certificate from my Keychain Access in macOS to my home folder to facilitate this step. After that, here is what worked for me:

    keytool import -alias YourCertNameHere -keystore YourCertNameHere.cer

Here is a link to the error you might see if you are failing somewhere on this step.
<https://stackoverflow.com/questions/21076179/pkix-path-building-failed-and-unable-to-find-valid-certification-path-to-requ>

### Step 15: Create a folder to sync your repo to.

I chose to do this is my home directory.

    cd ~/
    sudo mkdir YourRepoRootFolderName

Set ownership for the directory you just created for your user account.

    sudo chown -Rv YourUserName YourWorkspaceName

### Step 16: Map your workspace to the folder you created.

    tf workfold -map '$/TheNameOfTheRepoFolderYouWouldLikeToSync' ~/YourRepoRootFolderName -collection:https://tfs.YourTFSServerAddress.com/Whatever -workspace:YourWorkspaceName

### Step 17: Sync your local folder with the repo.

Change directory to the folder you created.

    cd YourRepoRootFolderName

And perform a tf get operation recursively in that folder. You may be prompted for your TFS username and password again. If successful you will see a long stream of logs in the terminal of all the files and folders it pulls down from your repo.

    tf get -recursive

### Step 18: Add the tf directory to your VS Code config.

There is a graphical interface for interacting with your global config for VS Code now to do this. But I just added the following line to my global settings.json file for VS code because I prefer the old way most of the time.

    "tfvc.location": "/users/YourUsername/TEE-CLC-14.134.0/tf"

### Step 19: Open VS code and edit a file in the folder you created.

Usually when VS Code loads, whether the first or and subsequent times, the source control tab on the left does not show any outstanding changes made (there will admittedly be none the first open). I usually solve this by making a quick edit to a file in the local folder for the repo and waiting a few seconds for it to update against the server. You should see the number of changes as a badge on the source control tab icon.

### Step 20: Make your first commit.

Make sure you have at least one outstanding change made and click the source control tab. Just like with git repos you can add a commit message up top. You will notice there a far less options than with Git repos but you can can sync and check in at least. You can even associate a work item or task number to the commit by using the following syntax in the commit message:

    #123456 - first commit from macOS for addison, woohoo

### Step 21+: ???

Supposing you've even made it this far somehow you are bound to encounter errors along the way. In full disclosure, TF sync errors are not uncommon, particularly through the VS Code source control UI. A quick restart of VS code will fix some of these errors. But, far more often than I would prefer, I find the only solution is to wipe my local repo folder (maybe manually putting aside my outstanding changes if necessary) then clear my local workspace like this:

    sudo rm -rf ~/YourRepoRootFolderName
    tf workspace -delete -collection:https://tfs.https://tfs.YourTFSServerAddress.com/Whatever YourWorkspaceName

Then repeat steps 15-17. This is probably overkill but I've gotten fast at it and almost always clears up my issues and allows me get back to work within a few minutes. Here's hoping your fare better on your journey with this process.

### Conclusion

Do I think all of this is really worth it? Yes, believe it or not. The ability to use VS Code to manage my work as opposed to Visual Studio proper and not having to boot a virtual machine that eats up half my CPU/RAM etc. makes my workday lightning fast compared to the alternative. Do I recommend this process for everyone? Probably not, but if you have some thick skin and a few (maybe more than few) hours to get ramped up here, it will pay off tremendously on the daily for you. That said, remind your manager about Git at least once a week. I remind my managaer's manager once a month. And my manager's manager's manager at least a few times a year.## How To Get TFS Working on macOS, in Just Twenty Steps or Less!

I have decided to write this article/how-to guide in a sad and desperate attempt to spare others from (some of) the pain I endured over the last year and a half. Let's start at the beginning of this journey. At the time of joining a team working on an old consumer facing AngularJS application (that's another article for another day), I was a daily Sublime user and came from a Linux/macOS background using cross platform source control tools (either Mercurial or Git). Day one, I had been blessed with a wonderful MacBook Pro. Day Two, I found out the project was using TFS for source control and I would need to run Microsoft Visual Studio inside of Windows 10 to check any code in or out. After just a few hours of running my Parallels VM and trying to create some monster mash workflow between the tools I know and love in macOS for my daily front-end needs, I was fed up. Not only was I constantly paradigm switching on any number of things between the two operating systems, but Visual Studio was a nightmare for this Sublime user looking for speed and simplicity in his coding experience. Ok it wasn't THAT bad. But there were buttons and features and clutter and slow, slow, slow everything everywhere... Ok, it WAS that bad.

How do people do this every day I thought? Well they didn't I was told. I had put myself in this situation by requesting a Mac and I would have to find the way out of it on my own. I furiously started to research any way possible to check in from the macOS side and avoid living my life split across two operating systems. Weeks passed. Then months. Occasionally I would come across yet another dying Stack Overflow thread or blog post about attempting to do the very thing I was. But everything guide I tried to follow or just jump off from hit some huge blocker halfway through. Every time I would ultimately give up yet again and return to my new weird split paradigm daily life.

I'm not sure what circumstances coalesced one fine day early this year but in one morning of perfect flow I broke through the last barrier. Fast forward to the present day. I currently use Visual Studio Code on macOS supplemented with some command line kung fu to do (most) of my check ins and required work on our project housed on an on premise TFS repository. I have distilled my struggles into a rough step by step guide on how to get TFS working on macOS. Be warned, it is long and in depth and by no means fool proof. I recommend using this guide as just that, a guide. You will likely have to troubleshoot on your own a few times along the way and I leave that up to you. I just sincerely hope these steps will help people through many of the hurdles I had to jump over many months in a much shorter amount of time. Here are the twenty steps I have "simplified" my process for my machines and new hires into. If you're lucky, you can skip a few and most are short at least... :|

(This guide is entirely intended for macOS users but could prove very useful for many Linux users and somewhat helpful for a few Windows users as well.)

---

### Step 1: Convince your team to use Git.

This was the first solution I tried, and after well over a year of struggling to even get this to work it is the solution I recommend. It is still not an option for me and my team despite my lengthy efforts and thus the existence of this guide. But seriously, the (many) trials you will face are not absolutely not worth this effort if there is even a remote chance you can do the work to convert your project's source control over to Git. That said, if you are like me and a number of other voices I came across while researching this topic, read on...

### Step 2: Install Git.

Even if you failed at step 1, I recommend installing git to your machine (if it isn't already). We will be using Visual Studio Code as our editor and as our UI for source control and it will complain on launch if you do not have git installed (even if you only plan to work with TFS repos).
<https://sourceforge.net/projects/git-osx-installer/files/>

### Step 3: Install Visual Studio Code.

This is not technically necessary as everything you need to do can be done from the command line but the UI for handling your repo in VS Code is one of the only visual solutions out there for macOS and actually works pretty damn well (most of the time). The rest of this guide will be geared towards us using Visual Studio Code for most repository interaction.
<https://code.visualstudio.com/download>

### Step 4: Install Azure Repos extension.

In the extensions tab for VS Code search for Azure Repos (Microsoft's new 2019 ready name for TFS) and install that extension.

### Step 5: Install TFS extension.

In the extensions tab for VS Code search for TFS and install that extension. (Choose the most popularly downloaded one. There are one or two forks that have not been well maintained...)

### Step 6: Download and unzip Team Explorer Everywhere.

Microsoft has created version of its Team Foundation command line tools that works cross platform. They actually did this a few years ago, and there are a few YouTube videos they and others made about it, but I wouldn't say it was obvious what to do after this part from their articles and documentation. I think the home directory is a good place to put this unzipped folder.
<https://github.com/Microsoft/team-explorer-everywhere/releases>

### Step 7: Install Java Runtime Environment.

I highly recommend you use Java 8. I am currently using JRE version 1.8.0_131. This is very important. There were some library changes in versions 9 and 10 that the TEE-CLC tool will break on but will only silently log those errors in a hard to find Java error logs file. This was my biggest hurdle to getting through this whole process the first time. I read an article somewhere that explained how you could change the library call or something like that to account for the change in version 9/10 but ultimately that fix never worked for me even though a few people I saw that responded had it work for them. Your mileage may vary. If you already have Java installed you may need to reinstall altogether or point your PATH variable to the version 8 install.
<http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html>

### Step 8: Install Java SE Development Kit.

Similar notes to the previous step on what version to use. I'll keep this step simple and just include the link to the one I am currently using, JDK version 8u171. (I have seen variations on this export line, this is the one that worked for my setups.)
<http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html>

### Step 9: Set the JAVA_HOME variable on your shell profile.

Add the following line of code to the end of your shell profile.

    export JAVA_HOME=`/usr/libexec/java_home -v 1.8`

I am using zsh for my shell so I edited my .zshenv file.

    sudo nano ~/.zshenv

For many users this will simply be your bash profile.

    sudo nano ~/.bash_profile

Save your changes and move on.

### Step 10: Close and reopen your terminal.

Fully quit using Cmd + Q to reload your shell profile you added your path environment variable to in the previous step. You will likely get an error in the terminal if you did this wrong.

### Step 11: Change directory into the TEE-CLC folder and try to run tf.

Like this (subbed for the version you downloaded).

    cd TEE-CLC-14.134.0

Try to run the tf executable there.

    tf

At this point just running tf worked for me. I did try this on a second machine this week and had to run the executable from the proper directory (~/TEE-CLC-14.134.0/tf) everytime, even after the next step was succesful. Your mileage may vary. (Anyone have more insight on to what may have gone wrong or right for me in either siuation here?)

    ./tf

### Step 12: Run the tf EULA and accept.

Run ./tf eula and accept, accept, accept!

    ./tf eula

### Step 13: Create your local tf workspace.

In your terminal, do the following:

    tf workspace -new YourWorkspaceName -collection:https://tfs.YourTFSServerAddress.com/Whatever

This sets up the workspace locally. It will most likely ask for your username and password to authenticate with the TFS repo.

### Step 14: You may need a valid certificate to authenticate with your repo.

I do with my organization and my repos. You might be able to skip this step for your setup. This was a certificate the IT department puts on all of our machines. This was honestly one of the trickiest steps and you may need to do some research on your version of macOS and/or Java to do this correctly. You need to import certificate(s) to your Java cacerrts directory using the keytool application from the command line. I chose to export a copy of my certificate from my Keychain Access in macOS to my home folder to facilitate this step. After that, here is what worked for me:

    keytool import -alias YourCertNameHere -keystore YourCertNameHere.cer

Here is a link to the error you might see if you are failing somewhere on this step.
<https://stackoverflow.com/questions/21076179/pkix-path-building-failed-and-unable-to-find-valid-certification-path-to-requ>

### Step 15: Create a folder to sync your repo to.

I chose to do this is my home directory.

    cd ~/
    sudo mkdir YourRepoRootFolderName

Set ownership for the directory you just created for your user account.

    sudo chown -Rv YourUserName YourWorkspaceName

### Step 16: Map your workspace to the folder you created.

    tf workfold -map '$/TheNameOfTheRepoFolderYouWouldLikeToSync' ~/YourRepoRootFolderName -collection:https://tfs.YourTFSServerAddress.com/Whatever -workspace:YourWorkspaceName

### Step 17: Sync your local folder with the repo.

Change directory to the folder you created.

    cd YourRepoRootFolderName

And perform a tf get operation recursively in that folder. You may be prompted for your TFS username and password again. If successful you will see a long stream of logs in the terminal of all the files and folders it pulls down from your repo.

    tf get -recursive

### Step 18: Add the tf directory to your VS Code config.

There is a graphical interface for interacting with your global config for VS Code now to do this. But I just added the following line to my global settings.json file for VS code because I prefer the old way most of the time.

    "tfvc.location": "/users/YourUsername/TEE-CLC-14.134.0/tf"

### Step 19: Open VS code and edit a file in the folder you created.

Usually when VS Code loads, whether the first or and subsequent times, the source control tab on the left does not show any outstanding changes made (there will admittedly be none the first open). I usually solve this by making a quick edit to a file in the local folder for the repo and waiting a few seconds for it to update against the server. You should see the number of changes as a badge on the source control tab icon.

### Step 20: Make your first commit.

Make sure you have at least one outstanding change made and click the source control tab. Just like with git repos you can add a commit message up top. You will notice there a far less options than with Git repos but you can can sync and check in at least. You can even associate a work item or task number to the commit by using the following syntax in the commit message:

    #123456 - first commit from macOS for addison, woohoo

### Step 21+: ???

Supposing you've even made it this far somehow you are bound to encounter errors along the way. In full disclosure, TF sync errors are not uncommon, particularly through the VS Code source control UI. A quick restart of VS code will fix some of these errors. But, far more often than I would prefer, I find the only solution is to wipe my local repo folder (maybe manually putting aside my outstanding changes if necessary) then clear my local workspace like this:

    sudo rm -rf ~/YourRepoRootFolderName
    tf workspace -delete -collection:https://tfs.https://tfs.YourTFSServerAddress.com/Whatever YourWorkspaceName

Then repeat steps 15-17. This is probably overkill but I've gotten fast at it and almost always clears up my issues and allows me get back to work within a few minutes. Here's hoping your fare better on your journey with this process.


Source https://gist.github.com/counterlogik


