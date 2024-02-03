# SMS-filtering
Hey eveyone! This repository and project board are what we're going to be using for version control (just keeping all of our files synced with each other) and progress tracking. I'll write a quick guide here for setting up your local machine to use this repository.

## Install Git
Go to the git for windows website and install wherever you please.

## Easy Setup
For an easier time setting up a local repository, you can use Github Desktop, Githubs GUI for repository management. I wouldn't really recommend this though since it's rather clunky and slow, but to each their own. Once you donwload it it's pretty simple to work with it, so I won't go into detail about it.

## Command-Line Setup
Once you have Git installed, navigate to the folder you want to contain the project files in your command prompt. For windows, here's an example of how to do so:

H:\>C:

C:\>cd C:\Projects\Senior Project

C:\Projects\Senior Project>

Type the drive letter followed by a colon to swap which drive the command prompt is on.
Then just type "cd (filepath)" to navigate to the folder you want.

### Initialization
Once at the desired folder in the cmd, type "git init" to set up a basic repository. You'll notice a hidden .git folder in your file browser if you have it set to see hidden items.

### Link Local to Cloud Repository
type "git remote add origin https://github.com/Lopezm0908/SMS-filtering" to link your local repository to the github one.
You can type "git remote" to confirm that it worked. It should return origin.

### Change Branch
type "git checkout main" to swap to the main branch. This should also automatically set up your local branch (main) to track the origin's main branch. This was the last step for setup, now we move to version control.



## Version Control
Now that everything is setup, I'll walk through how to use github for version control.

When you're ready to start working on the project, navigate to the project location in command line and type "git status". This will let you know if anyone else has done anything since you last worked on it. If it says you're up to date, you're good to start working. If you're not up to date, you'll need to type "git pull" to pull the changes from the cloud to your local machine, then you're good to start working.

Once you're finished working, type "git status" again. This will let you know what files you've changed (they'll be red). Type "git add ." to add all files you've altered to the next commit. Type "git status" again to ensure that the files are green now, meaning they're ready for a commit. Then, type "git commit -m '(describe here a very brief summary of what you did)' ". Once you've commited your changes, type "git push" to push your commits to the cloud.

That summarizes the overall workflow of github. Let me know if you have any questions (or you can use chatGPT for questions, it's very well documented on how to use Git and Github.


#### Final tip
Commits can be seen as checkpoints of sorts. If, while you're working on the project, you decide you want to try something a bit different and need to modify a good amount of your files to try it and aren't comfortable with that, make a quick commit and type as the message why you're committing and what you're fixing to try. You can bounce between commits as you please, though it's a bit more complex than what I talked about in this guide. This checkpoint hopping is the entire point of version control and allows you the comfort of knowing you won't screw something up somewhere and mess the whole project up since you hace commits to fall back on.