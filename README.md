# pocketSomme

pocketSomme is an Android application I wrote to provide fast look-up capabilities for wine selection. It scrapes data from the public Snooth API, providing information on the wine's rating, region, and foods it pairs well with. The app also maintains a SQLite database of the history and favorited wines of the user, allowing for a passive accumulation of data to inform the user of his or her tastes in varietals, regions, etc.

Technologies Used:
* Java for backend/data treatment
* XML for front-end layout structuring
* SQLite for local data storage/management
* Android Studio as Development environment
* GenyMotion device emulator run on top of VirtualBox


**Splash Page**

<p align="center">
<img src="https://github.com/Nyzrus/pocketSomme/blob/master/app/src/main/res/drawable/pocketSomme_Home_size.png" width="150"><br>
</p>

The splash page is a typical click-through page to welcome the user and begin to define the style and functionality of the app itself, in this case through the minimal design elements. The user clicks on the centered image bringing him/her to the Search page, the main navigation Activity of the Android app.

**Search Page**

<p align="center">
<img src="https://github.com/Nyzrus/pocketSomme/blob/master/app/src/main/res/drawable/pocketSomme_Search.png" width="150"><br>
</p>

The Search page, again has a very simple layout with one immediately visible function, to enter a search query in the test box and enact it through the Search button.

The Search function is completed by scraping the relevant data from the Snooth API.

The scraping is done asynchronously by the jsonTask class so as not to affect the processes of the GUI or other parts of the application.
* A url is constructed by the UrlMaker class which uses a base url , my API key, and the query keywords to construct a custom url for each user query.
* Next, the getData() method is called to get an HTTP response for the device's connectivity vis a vis the url created
* The data is packed through a BufferedStream into a StringBuilder object(StringBuilder is used for memory conservation as the contents are expected to be continuously modified).
* The String return at the end of the StringBuilder construction is converted to json in the doInBackground() portion of the asynchronous jsonTask process.
* The json is then parsed and added to corresponding ArrayLists
* These arraylists are then packed in the Intent that opens the SearchResults activity


**SearchResults**

<p align="center">
<img src="https://github.com/Nyzrus/pocketSomme/blob/master/app/src/main/res/drawable/pocketSomme_Results.png" width="150"><br>
</p>

The SearchResults activity displays the results of the user query in a ListView format. The arraylists passed from the Search to the SearchResult activity are unpacked and the info passed to a ListView adapter that creates each row of the displayed ListView.

At the bottom of the activity are three buttons:

1. History button - Adds the selected(focused) wine from the listview to the user's history database. The same wine cannot be added twice. The user's history is visually accessible from the menu options on the Search Activity.

2. Favorite button - Adds the selected(focused) wine from the listview to the user's favorites database. The same wine cannot be added twice. The user's history is visually accessible from the menu options on the Search Activity.

3. GoTo button - Forwards the user directly to the Snooth website's entry for the specific wine which occasionally has more detailed information on the wine.

History and Favorite are supported by adapters which implement SQLite storage of the wines added to their lists. The adapters provide for Create, Read, and Delete operations within the databases.


**Features I would still like to implement**

One feature I would still like to implement is the ability to plot the User's favorites on GoogleMaps through the use of Android's GeoCoder class. Ideally, one would be able to see his or her favorite wines plotted cartographically which would give a nice visual representation of a user's natural tastes in wine. Through a semi-diligient cataloguing practice with the app, one would be able to check the map and see that many of their favorites come from Bordeaux, France giving them some context for future wine selection.
