# Capstone project on Mobile Cloud Computing Especialization

## Demostration 

Watch this video:

[![Social Gift Demostration](https://raw.githubusercontent.com/robfrut135/SocialGift/master/PotlachClient/res/drawable-xxxhdpi/ic_launcher.png)](https://drive.google.com/file/d/0BymCGWR0IjzkX3BWeTFhaWc2V3M/view?usp=sharing)

## Config 

Eclipse configuration

https://drive.google.com/file/d/0BymCGWR0IjzkM0VSUGtPa3RqMGM/view?usp=sharing

https://drive.google.com/file/d/0BymCGWR0IjzkZ3hPYTQzdFoyNDA/view?usp=sharing

## Requirements

The requirements to develop SocialGift were based on this description.

### Potlatch - A Sample Capstone Project 

“Happiness is a gift and the trick is not to expect it, but to delight in it when it comes." - Charles Dickens, Nicholas Nickleby

See a sunset that you just can't keep to yourself; a landscape that takes your breath away, an image that touches you personally? Then snap a picture, add some text or poetry to convey your thoughts, and give it to others with Potlatch. Can your thoughts and images touch people around the world? Who is the greatest giver of gifts? Find out with Potlatch. (The word potlatch comes from the Chinook jargon, meaning "to give away" or "a gift" and is a gift-giving feast practiced by indigenous peoples of the Pacific Northwest Coast of Canada and the United States) 

#### Basic Project Requirements 

- Any potential Capstone project must support multiple users and should leverage services running remotely in the cloud. Each project's specification clearly outlines the app's intended high-level behavior, yet leaves substantial room for individual creativity. Students will therefore need to flesh out many important design and implementation details. Basic requirements for all Capstone MOOC project specifications include: 
- Apps must support multiple users via individual user accounts. At least one user facing operation must be available only to authenticated users. 
- App implementations must comprise at least one instance of at least two of the following four fundamental Android components:  Activity, BroadcastReceiver, Service and ContentProvider. 
- Apps must interact with at least one remotely-hosted Java Spring-based service over the network via HTTP. 
- At runtime apps must allow users to navigate between at least three different user interface screens. (e.g., a hypothetical email reader app might have multiple screens, such as (1) a ListView showing all emails, (2) a detail View showing a single email, (3) a compose view for creating new emails, and (4) a Settings view for providing information about the user's email account.)
- Apps must use at least one advanced capability or API from the following list covered in the MoCCA Specialization: multimedia capture, multimedia playback, touch gestures, sensors, or animation. Experienced students are welcome to use other advanced capabilities not covered in the specialization, such as BlueTooth or Wifi-Direct networking, push notifications, or search. Moreover, projects that are specified by commercial organizations may require the use of additional organization-specific APIs or features not covered in the MoCCA Specialization. In these cases, relevant instructional material will be provided by the specifying organization. 
- Apps must support at least one operation that is performed off the UI Thread in one or more background Threads or a Thread pool. 
There may also be additional project-specific requirements (e.g., required use of a particular project-specific API or service). 

#### Basic Functional Description and App Requirements for Potlatch 

* A Gift is a unit of data containing an image, a title, and optional accompanying text.
* A User can create a Gift by taking a picture (or optionally by selecting an image already stored on the device), entering a title, and optionally typing in accompanying text.
* Once the Gift is complete the User can post the Gift to a Gift Chain (which is one or more related Gifts). Gift data is stored to and retrieved from a web-based service accessible in the cloud. The post operation requires an authenticated user account.
* Users can view Gifts that have been posted.
* Users can do text searches for Gifts. The search is performed only on the Gift's title. Gifts matching the search criterion are returned for user viewing.
* Users can indicate that they were touched by a Gift and can also flag Gifts as being obscene or inappropriate. Users can set a preference that prevents the display of Gifts flagged as obscene or inappropriate.
* Touched counts are displayed with each Gift. These counts are periodically updated in accordance with a user-specified preference (e.g., Touched counts are updated every 1, 5 or 60 minutes).
* Potlatch can display information about the top “Gift givers,” i.e., those whose Gifts have touched the most people.

#### Implementation Considerations  

The Potlatch project specification outlined above is intentionally underspecified to maximize opportunities for student creativity. Students must therefore consider and choose between a number of design and implementation issues and solution alternatives to produce their final product solution. For example, students should consider at least the following issues for this project: 
* How will Gift data be stored? In your device? In the remote service? 
* What will the user interface screens look like? How will the user navigate between different screens? 
* How and where will users attach Gifts to a Gift Chain? 
* How will users delete Gifts from a Gift Chain, as well as delete Gift Chains altogether? 
* How, when, and how often will the user enter their user account information? For example, will the user enter this information each time they run the app? Will they specify the information as part of a preference screen? 
* How will Gifts and Gift Chains be accessed? Will there be some initial default display? Will users have to enter search criteria? If so, how will the user get access to the search interface? 
* Will all search results be displayed at the same time? Or will only a subset of Gifts be shown at any one time? Will the Gifts be sorted in some way, such as by their Touched counts, creation time, or latest update time? 
* How will users indicate that they were touched by a Gift? Can they undo their decisions? How will they flag inappropriate or obscene Gifts? 
* What user preferences can the user set? How will the app be informed of changes to these user preferences? 
* How will the user navigate between viewing/searching for Gifts and viewing top Gift givers? How will the app handle concurrency issues, such as how will periodic updates occur - via server push or app pull? How will search queries and results be efficiently processed? Will the data be pulled from the server in multiple requests or all at one time? *  Will the server data include full-sized images, or thumbnails plus URLs pointing to the full-sized images? 
* Will the app cache information on the local device, e.g., in a Content Provider? 
* Will the app provide extensions and improvements that go beyond the minimum requirements? For example, if an app collects location information for each Gift, queries by location, and then uses Maps to display Gifts at their locations, then it may be necessary to modify the various app databases and query facilities. Also, using Maps may require students to have access to an actual device. Also, how will these enhancements affect the rest of the app? 
* Does your app really require two or more fundamental Android components? If so, which ones? For example, this app might benefit from using a ContentProvider or from using a background Service that synchronizes local and remote image data, only when the device is connected to a WiFi network
