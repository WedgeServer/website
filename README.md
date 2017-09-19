![Wedge site](https://lol768.com/i/cD.Df.XQ.qz.Zm.VB.vo.x6)

## Website

WIP Play-based website for the Wedge project.

Features being worked on:

* Tool for building binaries on demand, with user-specified plugins and architecture
  * Thanks to SirCmpwn for assistance with this - the backend here is mostly done.
  
Progress has been made on:

* Initial design, marketing information for the project.

## Running it locally

You'll need a reasonably recent version of the Scala build tool (`sbt`). v1.0.1 is known to work.

Clone the repository `git clone git@github.com:WedgeServer/website.git` and `cd website` followed by a `sbt run`.
This will take a while, but you'll eventually see a `--- (Running the application, auto-reloading is enabled) ---`
message. You should be able to then visit `http://localhost:9000`.

Need to change the port from the default of 9000? `sbt "run 1234"`.