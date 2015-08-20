# FriendCaster
A simple Android app to browse, view, and stream / download episodes of the Super Best Friendcast.

## Features
* Material design
* Quickly browse through episodes of the Friendcast
* View detailed info / show notes
* Stream episodes to your player of choice
* Download episodes for later
* Light and Dark theme
* Gratis AND Free/Open Source

## TODO
* Color themes need to look REALLY good
  * Don't just slap material palette colors on it
  * Everything needs to compliment and flow
  * REALLY read the material design guidelines
* Set up linear progress bar when fetching feed
  * If they properly paginate, maybe we'll be fast enough to not need this...
* Add refresh button to action bar
  * Maybe we should do a pull-to-refresh?
* Implement downloading / broadcasting
  * Look into broadcasting to a Chromecast
* Review and clean up code (build your popsicle stick bridge before the Verrazano!)
* Release on Play

## Problems with the RSS Feed
* Not properly paginated
	1. Every time we load the feed, we have to load ALL EPISODES
	2. Not only does the server take a long time to respond because of this, but on our end it makes it that much slower to parse
	3. Not very friendly to mobile users with limited data 
	4. [VERY simple fix](http://wordpress.stackexchange.com/questions/108185/pagination-of-rss2-feed)
	5. No one really NEEDS every episode right away like that; it's easy enough to scroll to the bottom and send a request for the next page...
* Duration fields are left blank
	1. This means that, in order to get the length of each podcast we have to submit a HEAD request for EACH ONE
	2. Also not friendly to mobile users! (especially since it's not paginated properly!)
	3. Our method to calculate is also a little inaccurate as well (assumed bitrate and rounded up)
