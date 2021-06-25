If this is your first time using the ThinkGear SDK for Android, please start
by reading the "ThinkGear Development Guide for Android" PDF.  It will
tell you everything you need to know to get started.

If you need further help, please visit http://developer.neurosky.com for the latest
additional information.

To contact NeuroSky for support, please visit http://support.neurosky.com, or
send email to support@neurosky.com.

For developer community support, please visit our community forum on
http://www.linkedin.com/groups/NeuroSky-Brain-Computer-Interface-Technology-3572341

Happy coding!


Version History
---------------
2013-09-12 - TGDevice version 29 - special test version using buffered stream read for testing
2013-07-25 - TGDevice version 28 - special test version using buffered stream read for testing
2013-04-05a - TGDevice version 26
    - one line correction to HelloEEG sample application
2013-04-05 - TGDevice version 26
    - add user callable methods to trigger the calculation of:
          taskDifficultyEnabled(true/false) "EEG only"
          taskFamiliarityEnabled(true/false) "EEG only"
    - add user callable methods to enable the calculation of:
          blinkDetectEnable(true/false) "EEG only"
          respirationRateEnabled(true/false) "EKG only"
    NOTE: some of the above consume significant CPU time and Memeory resources
    - operation while the Android Debugger is engaged will degrade performance
    NOTE: all of the above can be called at launch time, the configuration
    will be re-evaluated after a connection has been made to the NeuroSky device.
    if the device can not support the feature it will be automatically disabled.
    - add to EEG sample app, display of familiarity/difficulty as percent change
    - move difficulty and familiarity to lower priority threads so that 
         real time collection of data is possible.
    - the "lib" directory renamed to "libs" to avoid confusion
    - add reimplemented Zone algorithm, also added to HelloEEG sample app
    - minor improvements to sample applications
    - changes to respiration rate calculation thread to handle
        interference with the Android Debugger
    - improvements to the EKG personalization algorithm
    - improved Bluetooth connection start up, added a retry when the 
        a connection is established but no data is received.
    - correct protocol parsing bug that would cause many errors
        when an unexpected data is received from hardware.
    - correct an out-of-memory error seen when an EEG headset was connected
        for long periods (tens of minutes) with good signal.
    - reduced garbage collection needs

2012-12-14 - TGDevice version 22

