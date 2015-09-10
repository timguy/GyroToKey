 **GyroToKey**
 * transforms gyrosensor data on mobile via WLAN into keypress on PC 

 **How to use**
 * install on your android from appstore: "sensor fusion" from lunds universitet
 * select "complement tilt", "complement yaw" on second screen and send it via WLAN to your ip adress of your pc
 * Start GyroToKey.jar:
 
 You have two options to start: 
 "java -jar GyroToKey.jar" and 
 "java -jar GyroToKey.jar g"
the last one starts with graphical chart output.

* Start with graphical output and move your phone. You should see the chart moving
*  Test if buttons are hit for your correctly. E.g. open SuperTuxKart directly or use an empty document or a site like https://scratch.mit.edu/projects/20966625/#fullscreen
* if the treshholds for moving the phone are correctly, fine. Otherwise you need to adapt the KeyStroker.java class
* Open SuperTuxKart game or whatever you want to use with the keypresses
* Mount your phone on a wobble board if you want

 **Issues**
 * awkward key presses
 	* Settings in robot class:
 		* robot.setAutoWaitForIdle(true); //make it more precise but slow
 	* Maybe build threads to be faster?
 * Ideas: configure treshholds in properties or via a GUI, more keystroker classes for other games
 