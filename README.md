# N6PDoubleTapToWake

An app to enable Double Tap To Wake on the Google Nexus 6P.

Once activated, it will run upon reboot. This app simply unlocks a built in feature within your nexus device and therefore has no effect on battery life.

````
echo 1 > /sys/devices/soc.0/f9924000.i2c/i2c-2/2-0070/input/input0/wake_gesture
````

***This app requires ROOT permissions and STOCK KERNAL to run***
