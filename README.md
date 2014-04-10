AntSim
======

A simple ant simulator.

Note that this project makes use of the LibGDX Framework which allows developers
to program for the desktop and Android platforms at the same time using the same
code (here located in the AntGame) src file.  To compile, run the main inside
AntGame-desktop if running from desktop or AntGame-android for Android.

Features v.0.2.3
----------------

* Ants wander randomly looking for food.
* When they find food, they return it to their ant hill using a path of nodes.
* New ants created, costing food from Anthill at regular intervals.
* User can zoom/pan across Ant world.


Screenshots
-----------
<img src="/Screenshot_2014-03-25-19-18-04.png" width="700px">
<img src="/Screenshot_2014-03-25-19-18-44.png" width="700px">

Todo
----

* Searching ants should "smell" the trail of other ants that lead to food and then "adopt" their path.
* Ability to click on ant and follow it - also display facts about ant.
* Ant hills should use more food depending on number of ants alive.  Should eventually run out and "die".
* Need to introduce combat ability - maybe a food source that moves and has to be killed before collected.
* Should include enemy ants that fight with player ants over food.

Contributions
=============

AntSim is completely open source and the contribution of others is absolutely open.  Please follow the style of the source files you add in and make commit requests in small, bite-size pieces so I don't have to search through multiple files to determine if the commit is safe.
