# device-type.myneurio.groovy
My Ecobee Device:  Custom ecobee device to enable more smart thermostat's capabilities within Smartthings 

Author:             Yves Racine

linkedIn profile:   ca.linkedin.com/pub/yves-racine-m-sc-a/0/406/4b/

Date:               2014-03-31

Code: https://github.com/yracine/device-type.myecobee

**************************************************************************************************
If you like My Ecobee Device and related smartapps, please support the developer:


<br/> [![PayPal](https://www.paypalobjects.com/en_US/i/btn/btn_donate_SM.gif)](
https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=yracine%40yahoo%2ecom&lc=US&item_name=Maisons%20ecomatiq&no_note=0&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHostedGuest)

**************************************************************************************************

=====================
INSTALLATION STEPS
=====================

*************************************************
1) Create a new device type (My Neurio Device)
*************************************************


a) Go to https://graph.api.smartthings.com/ide/devices

b) Hit the "+New SmartDevice" at the top right corner

c) Hit the "From Code" tab on the left corner

d) Copy and paste the code from neurio.device-type.groovy
under https://github.com/yracine/device-type.myneurio.groovy

e) Hit the create button at the bottom

f) Hit the "publish/for me" button at the top right corner (in the code window)

*************************************************
2) Create a new smartapp (NeurioInit)
*************************************************

a) Go to https://graph.api.smartthings.com/ide/apps

b) Hit the "+New SmartApp" at the top right corner

c) Hit the "From Code" tab on the left corner

d) Copy and paste the code from NeurioInit
under https://github.com/yracine/device-type.myneurio/tree/master/smartapps

e) Hit the create button at the bottom

f) Make sure that enable OAuth in Smartapp is active 

* Goto app settings (top right corner), 
* Click on Oauth (middle of the page), and enable OAuth in Smart app
* Hit "Update" at the bottom

g) Go back to the code window, and hit the "publish/for me" button at the top right corner 

3) Create a new device (https://graph.api.smartthings.com/device/list)

  Name: Your Choice
  Device Network Id: Your Choice
  Type: My Neurio Device (should be the last option)
  Location: Choose the correct location
  Hub/Group: (optional) leave blank or set it up to your liking
4) Update device's preferences

    (a) <appKey> provided at the ecobee web portal in step 1 (no spaces)
    (b) <serial number> of your ecobee thermostat (no spaces)
    (c) <trace> when needed, set to true to get more tracing (no spaces)


