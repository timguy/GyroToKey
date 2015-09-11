 **GyroToKey**
 * transforms gyrosensor data on mobile via WLAN into keypress on PC 

 **How to use**
 * install on your android from appstore: "sensor fusion" from lunds universitet
 * select "complement tilt", "complement yaw" on second screen and send it via WLAN to your ip adress of your pc
 * Start GyroToKey.jar:

 "java -jar GyroToKey.jar" 

* Start with graphical output and move your phone. You should see the chart moving
*  Test if buttons are hit for your correctly. E.g. open SuperTuxKart directly or use an empty document or a site like https://scratch.mit.edu/projects/20966625/#fullscreen
* if the treshholds for moving the phone are correctly, fine. Otherwise you need to adapt the KeyStroker.java class
* Open SuperTuxKart game or whatever you want to use with the keypresses
* Mount your phone on a wobble board if you want

 **Issues**
 * awkward key presses
 	* Settings in robot class:
 		* robot.setAutoWaitForIdle(true); //make it more precise but slow
 	* Changed from Observer pattern to multi thread application communicating via queues. To slow processing of incoming UDP is not the reason :-/. Still hanging keys and delays while key presses
 		* Disadvantage now: No seperation in main now if gui should started or not. Now it's always started (now issue due to multi threaded)
	* tried also with xev on command line to see key presses. Could not find issues. 
 * Ideas: 
 	* configure treshholds in properties or via a GUI
 	* more keystroker classes for other games
 	* interfaces for everything to cope with different android applications or games
 
