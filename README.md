# Choicetrax Client and Server code (circa 2007-2010)

This is the client and server code for my side project/startup Choicetrax, which was a DJ-oriented dance music search engine. It provided the ability to search [Beatport](http://beatport.com), [Traxsource](http://traxsource.com), [Juno Download](http://junodownload.com), Stompy, Primal Records and DJDownload. (Unfortunately, the unlinked stores no longer exist.) For more details on functionality and to see what the UI looked like at launch, see [this video](https://www.youtube.com/watch?v=rVc8BkVmHzE).

The whole project was almost 37k LOC. In this repo I'm just including the GWT client and Java server code for what was public facing. However, there were other codebases - the background screen scraping batch job to collect data from the various download stores, the private admin system, the self-service advertising system, and a collection of SQL scripts and XML Schema docs used to generate some of the code found here.

At some point I may do more of a post-mortem on the entire project. For now it's interesting for me to revisit this code and see things I like:

- a single GWT RPC endpoint using the command pattern
- independently arriving at the CQRS pattern (C = handlers, Q = loaders)
- writing code for the UI, as opposed to markup (HTML/JSX/etc)
- more to come...

And things I don't like:

- the complete lack of tests
- limited comments
- extensive string concatenation (notably in inline SQL)
- more to come...


## License

Note that at the moment I haven't included a license, which apparently indicates I'm maintaining copyright. I think most of this code is out of date enough that its primary purpose is historical curiosity. That said, I poured enough of my life into this over the course of several years that I'm not quite ready to slap an MIT license on it and let anyone have at it. (Not that my GitHub account is high traffic by any means.) My desire - for now at least - is it will do more good by making it public on GitHub than sitting in a backup system somewhere.