##BakingApp

Notes for testing:

1- you should uninstall the app before running IdlingResouceMainActivityTest to force the app to fetching the data from the internet. If you don't, running thistest will be meaningless.

2- you have to make sure passing IdlingResouceMainActivityTest BEFORE running another test (RecipeGeneralActivityTest)

Notes for the app:

1- the app after fetching the recipes data from the api, It will store all these data into local database on the device

2- the widget displays random recipe every 30 minutes (excluding the last one displayed). The widget displays only the recipe name and its ingredients 
