DropBear - SSH Server
=====================

Description
-----------

For Windows, MacOS and GNU/Linux systems.

This is a SSH server using DropBear, allows you to easily install, configure and run a Secured SHell server.

You will be able to SSH to your phone to gain shell access or copy files.

SSH is a secured (encrypted) protocol that gives you shell access on a remote UNIX-like system.

SSH can be used to browse, copy or execute commands.

This is similar to the Terminal app or `adb shell` but over the network.

Usage
-----

Transfert files with `scp`, WinSCP or Cyberduck.

Gain shell access with `ssh` from any terminal emulator (PuTTY/KiTTY under Windows).

You can also run `ssh` and `scp` from you phone.

License
-------

This software is released under GNU GPLv2+.

    This program comes with ABSOLUTELY NO WARRANTY.
    This is free software, and you are welcome to redistribute it
    under certain conditions.

* ic_launcher.png: Mfayas (Free), Johanna Nizard (Free)
* ic_market.png: DsynFLO Creations (Free)
* ic_rate.png: Andy Gongea (Free)
* ic_donate.png: Iconshock (Free)
* ic_web.png: Ever (Free)

* Binaries from <http://blog.mwmdev.com/tutorials/249/>

Some parts of this software are released under the Apache 2.0 LICENSE:

* Intents from d0lph1nk1ng <http://dolphinking-software.com>

Compatibility
-------------

* Minimal: Android 2.1+ (API 7+)
* Recommanded: Android 2.2+ (API 8+)
* Target: Android 2.3+ (API 10+)

* Density:
  * ldpi
  * mdpi
  * hdpi

* Screens:
  * small
  * normal
  * large

Build from sources
------------------

* Checkout the git repository
  [shell] git clone ...
* Checkout the git submodules
  [shell] git submodule update --init
* Check that submodules are all android library projects
  [eclipse] project / properties / android / is library
* Update submodules' android support library
  [eclipse] project / android tools / add support library

Thanks
------

* Miguel (for original binaries)
* Michael Almyros (for file chooser)
* Johan Nilsson (for android-actionbar)
* thiagolocatelli (for android-uitableview)
* Andreas Stutz (for android-viewpagertabs)
* Stephen 'Stericson' Erickson (for roottools)
* Adam 'ChainsDD' Shanks (for root)

Others thanks are placed as comments into the source code.
