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
* Add refresh button to action bar 
* Properly set up episode card content
  * Issue of what things to get, how to parse them, and where they go
  * Maybe consider other RSS libraries besides ROME? HorroRss looks good
  * Need  to come up with a card layout
* Nice animations for transition to and from EpisodeDetailFragment
  * Right now I'm thinking a simple slide in from the right
    * Maybe rotate in from bottom right? (Seamless does this)
* Start on EpisodeDetailFragment
  * Set up EpisodeDetail card content 
* Implement streaming / Downloading
  * Streaming is taking the enclosure url and mimetype and feeding it to an ACTION.VIEW intent
  * Downloading is a bit trickier; need to think of the details
  * Look into broadcasting to a Chromecast
* Review and clean up code (build your popsicle stick bridge before the Verrazano!)
* Release on Play
