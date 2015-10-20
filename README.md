# Before you want to delete a function or modify it #

If you want to change its logic severely and you wish to remove that function, please add this right before the function declaration. For example,

@Deprecated

public void delete(){...} // method that you want to delete

public void delete(int newVariable){...} // new method

Then if other ppl still using depreciated function, eclipse will warn him.

It is better not to remove other ppl function as it will cause their copies not working.

# Git Dictionary #

COMMIT is always done to local repository.

FETCH is comparing with local repository with server copy. Therefore you need to commit to local repository to merge your local repository with server.

MERGE is adding your changes to server copy(which has been fetched)

CONFLICTS arise when eclipse fails to merge your local changes with server copy. You need to manually adding/remove/updating your copy. Remember to resolve all conflicts and do not remove your teammate functions.

PULL is FETCH + MERGE

PUSH is updating the server with your local repository in which all the conflicts are resolved.


# Commit Method #

Once you have completed the changes in your local repository, commit to your own repository by multi-selecting the files that you want to commit and right click, Team > Commit.

Do not select the whole project and commit.

Then you select your project, right click, Team > Fetch From Upstream.

Now conflicts arise. Go to Git Perspective, for each file which has conflicts, resolve the conflicts and save. Then commit it again to your local repository.

If you see files that were created by others, please add it to your local repository, or else it will be removed when you push to upstream.

After all conflict has been done, right click Project, Team > Push to Upstream.


# Commit Regulations #

After Fetch from Upstream, if you see files that were created by others, please add it to your local repository, or else it will be removed when you push to upstream.
 
Please push to upstream if and only if your part is syntax error-free and checked.

Please avoid removing methods of other people work
 
Please avoid renaming/removing methods unless it is needed
 
Adding new functions/methods are recommended


# Commit Frequency #

Model: Push once a day if needed

Logic: Push if and only if it is completed.

Data.json: Push if and only if it is syntax error-free, please check its syntax via online tools.
