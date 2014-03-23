AntSim
======

A simple ant simulator.

Note that this project makes use of the LibGDX Framework which allows developers
to program for the desktop and Android platforms at the same time using the same
code (here located in the AntGame) src file.  To compile, please run the main inside
AntGame-desktop if running from desktop.

Features
--------

* Ants wander randomly looking for food.
* When they find food, they return it to their ant hill using a path of nodes.

Todo
----

* Searching ants should "smell" the trail of other ants that lead to food and then "adopt" their path.
* Ability to zoom in and out via touch.
* Ability to click on ant and follow it - also display facts about ant.
* Ant hills should create more ants as food is produced AND use more food depending on number of ants alive.  Should eventually run out and "die".
* Should include enemy ants that fight with player ants over food.

Contributions
=============

AntSim is completely open source and the contribution of others is absolutely open.  Please follow the style of the source files you add in and make commit requests in small, bite-size pieces so I don't have to search through multiple files to determine if the commit is safe.
