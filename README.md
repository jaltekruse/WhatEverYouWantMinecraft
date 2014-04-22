WhatEverYouWantMinecraft
========================

<h1>Bug Reports</h1>
Use the <a href="https://github.com/jaltekruse/WhatEverYouWantMinecraft/issues">GitHub issue tracker</a> to report bugs.

<h1>Setting up the development environment</h1>
<ol>
	<li>Download <a href="http://www.oracle.com/technetwork/java/javase/downloads/index.html">Java JDK</a> if you don't already have it installed.</li>
	<li>Set your JAVA_HOME environment variable.  E.g. on my computer: JAVA_HOME = C:\Program Files\Java\jdk1.7.0_25</li>
	<li><a href="http://files.minecraftforge.net/">Download</a> the Minecraft-Forge <b>SRC</b> from this branch: 1.7.2 Recommended Version 10.12.0.1024</li>
	<li>Extract the folder then open</li>
	<li>Shift-right-click and select “open a new command window here”</li>
	<li>In the terminal run "gradlew.bat setupDevWorkspace --refresh-dependencies" (setupDecompWorkspace can be substituted for setupDevWorkspace to decompile the Minecraft source code but this will take much longer)</li>
	<li>In the terminal run "gradlew.bat eclipse"</li>
	<li>Go to the src folder and delete the main directory</li>
	<li>Run the following command in the src folder: "git clone git@github.com:jaltekruse/WhatEverYouWantMinecraft.git main"</li>
	<li>Open Eclipse, set the workspace to the Eclipse directory.  For me: C:\Users\Wesley\Git\forge\eclipse</li>
	<li>You can now run the program like normal from Eclipse</li>
	<li>If you get really stuck, check out this <a href="http://www.minecraftforge.net/wiki/Installation/Source">link</a></li>
	<li></li>
	<li></li>
</ol>

<h1>Commands to run once inside the game</h1>
Most of these items are also available in creative mode under the uncategorized tab.
<ul>
	<li>/summon Villager   then right click for dialogue, press k-key then right click for dialogue editor</li>
	<li>type ‘asdf’ then enter to find your player number, replace ### with the proper number in the following commands (you can use the up key to re-use previous commands):</li>
	<li>/give player### rpgmod:item.holy_hand_grenade 64</li>
	<li>/give player### rpgmod:item.axe_of_revenge 64</li>
	<li>/give player### rpgmod:item.lightning 64</li>
	<li>/give player### rpgmod:item.super_lightning 64</li>
	<li>/give player### rpgmod:item.heal_party 64</li>
	<li>/give player### rpgmod:item.super_lightning 64</li>
	<li>/give player### rpgmod:item.become_undead 64</li>
	<li>/give player### rpgmod:item.summon_zombie 64</li>
	<li>/give player### rpgmod:item.heal_self 64</li>
	<li>/give player### rpgmod:item.heal_mana 64/li>
	<li></li>
	<li></li>
</ul>











