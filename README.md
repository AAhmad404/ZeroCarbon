# Planetze: Carbon Emissions Tracker

## About

Planetze project extensions by Ahmad.

## Original Project Members

- Aqeeb [Ahmad]
- Minki
- Adeeb
- Nethanel
- Andrey

## Code Documentation

LINKS

Original repository: https://github.com/A-Skvortsov/Planetze

EXISTING SIGN-IN CREDENTIALS:

Email: andreyskv49@gmail.com  
Password: hellohello1

USER FLOW/USE INSTRUCTIONS

 	1. user opens app  
	2. user is presented with a sign-up/login module  
		i. for sign-up:  
			a) user is prompted to enter account information  
			b) user is denied the ability to create an account with an email for which an account already exists  
			c) user must confirm password (password also has some basic restrictions i.e. no spaces)  
			d) if account information is valid, user is sent a verification email and taken to the login page where they can login once email is verified  
		ii. for login:  
			a) if user tries to login before having verified their email, they will be denied login  
			b) if user enters invalid credentials, they are prevented from logging in and told that credentials are invalid  
			c) if user enters valid credentials, proceeds to app (see 3.)  
	3. upon logging in:  
		i. if it is the user's first login, they are taken to the initialization/annual carbon emissions survey  
		ii. otherwise, they are taken directly to the app home page, set as Eco Tracker  
	4. initalization/annual carbon emissions survey  
		i. a bunch of questions are asked, each multiple choice  
		ii. "back" button exists so user can go back and redo answers to questions as they do the survey  
		iii. certain answers to certain questions will prevent related questions from being asked (i.e. if user answer "no" to "do you have a car?", no further questions about cars will be asked)  
		iv. default country and default car values are saved from inputs to this survey  
		v. upon completion of the survey, user is taken to survey results page  
	5. survey results page. User is presented with:  
		i. a total annual emissions bar  
		ii. a graph showing the breakdown of their annual emissions by emissions category (transportation, food, housing and consumption)  
		iii. a graph showing the comparison between the user's annual emissions and the annual emissions of a country selected from a dropdown list of countries (default country, as specified in 4iv, is set first)  
		iv. a "statistics" module showing statistical (percentage-based) comparisons of user's annual emissions to (1) the national average for the selected country and (2) the global target of 2 tons of co2 per year per person (2 tons global target was given on Piazza)  
		v. an option to retake the survey at the bottom of the page (button)  
		vi. an option to proceed to "Home" (Eco Tracker and central navigation area of the app)  
	6. Home/central navigation area of the app  
		i. user always has a navigation bar on the bottom of the screen from which they can access Eco Tracker, Eco Guage, Eco Balance and Eco Hub  
		ii. user always has a top bar (top of the screen) naming the page they are currently on (out of those mentioned previously) and having a settings option in the top right corner  
		iii. settings option clicked opens a new page with several extra options i.e. logout, stay logged in, Eco Guage chart modifications, etc.  

FEATURE SUMMARY

	1. Eco Tracker  
  		i. displays user's total emissions (in kg of co2) for the current date at the top of the screen  
  		ii. displays current date in a calendar image along with a "view calendar" button which, when clicked, gives a calendar popup from which user can select a new date  
  			note: days with logged activities have indicators on them for user-friendliness  
  		iii. main part of the screen has a listview that (upon toggle) will show a breakdown of the user's logged activities (with CO2 for each activity) for the selected day (including habit logs) or user's current habits  
  		iv. add, edit and delete buttons exist for the activities, log and edit buttons exist for the habits  
  		v. user can access annual carbon emissions survey results page anytime from a button on the bottom of Eco Tracker  
  		vi. calendar notes:  
  			a) upon selection of a new day, Eco Tracker info updates to show data logged for the selected day  
  			b) user can select past and future dates to edit past activities (using the edit button, where add activity screen is displayed with pre-loaded settings corresponding to the logged activity) and plan future activities (or delete activities)  
  		vii. activity feature:  
  			a) selecting add, user is taken to a page where they can select an activity category, an activity and is prompted to manually input data for the activity before saving  
  		viii. habit feature:  
  			a) when habits are toggled, user can "edit" their habits which takes them to a new page displaying (upon toggle) all available habits (from a pre-defined habit list), user's current habits or recommended habits  
  			b) upon adopting a habit, user is reminded to log activities related to the habit using the "log" button  
  			c) habit progress is shown as logged habits appear as activities in the   "activities"-toggled listview so user can see when they complete the habit  
  			d) searching and filter: user has a searchbar above the habitlist in the edit habit screen as well as filter dropdowns which activate filters upon pressing the "apply" button  
  			e) personalized recommendations: user is recommended particular habits based on the category (transportation, food, etc.) of highest activity emissions over the past 30 days  
  	2. Eco guage  
  		i. user view their total co2 emissions for the selected time period at the top of the screen  
  		ii. user can select different time periods (i.e. 1 day, 1 week, etc.) at the bottom of the screen  
  		iii. Emissions trend graph: user is shown a trend graph showing their total emissions over the selected time period  
  		iv. Emissions breakdown by category: user is shown a pie chart showing the percentage breakdown of user emissions by emissions categories (transportation, food, etc.)  
  		v. Comparison to global/national averages: user is shown a bar graph comparing their emissions to the emissions of a selected country along with a percentage relation between the user emissions and the emissions of the selected country  
  	3. Eco Balance  
  		i. user is shown an introductory screen with a description of the purpose of Eco Balance along with a button to help them find emissions offset projects  
  		ii. upon clicking the button, user can explore offset projects each with a description, location and some statistics  
  		iii. selecting offset projects allows user to access links to external websites that provide further information and opportunities to explore the offset projects  
  	4. Eco Hub  
  		i. user is presented with an introductory screen showing learning resources and market trends buttons  
  		ii. selecting learning resources takes user to a screen with a list of informational YouTube videos on carbon emissions reduction  
  		iii. selecting market trends takes user to a screen showing recommended   websites/products/services related to emissions and carbon footprint reduction
  			a) linked websites involve latest market trends towards carbon emissions efforts  

ASSUMPTIONS

	1. Individual activity emissions are displayed in kilograms (kg) of CO2
	2. Metric tons (referred to here as tons or tonnes or the unit (t); 1t = 1000kg) are used elsewhere, particularly in emissions total computations i.e. daily totals, annual emissions totals, etc.  
	3. Emissions measurement units (kg, t) and percentage computations (see statistics section of survey results page) are typically rounded to 1 or 2 decimal places to avoid floating point issues and for readability 
	4. Habit-related activities (i.e. activities that are logged as a result of selecting a habit in the "habits" section of Eco Tracker) cannot be edited as habits have default information settings including emissions computations preset based on assumptions (see further down)  
	5. Preset emissions values for habits are reduced versions of regular activity emissions presets. Note that all default habit information (including habit categories, habit names, impact levels) is stored in the Firebase Realtime Database (FRDB)  
	6. Habit impact levels are split into three categories by assumption; <= 1kgco2 per habit log, <= 5kgco2 per habit log, > 5kgco2 per habit log  
	7. Habit categories are as follows: transportation, food, housing, consumption 
	8. A reiteration of personalized habit recommendation: recommended habits are those in the category of highest user activity emissions over the past 30 days, excluding habits already logged by the user (example: if user activity emissions over 30 days are in the following descending order [transportation, food, energy, consumption], then habits in the transportation category are recommended first. If all those are adopted, habits in the second-highest emissions category (food, in this case) are adopted second. So on and so forth)  
	9. User can adopt or quit habits at any time. Habit-related activities that were logged previously are NOT automatically removed once a user quits a habit (i.e. record is kept on the days the user logged the habit)  
	10. Activity categories are as follows: transportation, food, consumption, energy (energy is synonymous in this application to monthly energy bills)  
	11. Monthly energy bill activities are logged as follows: when a user logs a monthly energy bill on day x, app automatically logs a single activity for each day from day x to day x-30 (inclusive), with the activity name and emissions being consistent for each day and based on preset values, replicating consistent user emissions throughout the past month leading to the monthly energy bill  
	12. If, in the annual emissions survey, user selects "I don't know" as their default car, default car is set to hybrid (as hybrid is the most neutral of all other settings i.e. gasoline/diesel are too high with emissions, electric is too low. Plus, many cars are hybrid nowadays so it is not unreasonable to assume they may have a hybrid car)  
	13. If, in the annual emissions survey, user selects "no" to whether they have a car, default car is set to none.
	14. In activity logging, if user selects drive personal vehicle activity, input automatically sets default car (which is taken from annual emissions survey) as the personal vehicle while user retains ability to change this (if default car is none, no automatic selection is performed)  
	15. Total annual emissions computations in annual emissions survey are taken exactly from "Formulas" excel file provided by teaching team  
		i. For "I don't know" selection of personal vehicle, emissions computations default to those of hybrid car (as this is most reasonable. We cannot assume 0 emissions since user must have answered "yes" to "do you have a car" question in order to reach this question)  
		ii. If, in the annual emissions survey, user selects "other" for "what type of energy do you use to heat your home?" or "what type of energy do you use to heat water in your home?", computations default to those of user selecting "electricity" since electricity is one of the most commonly used heating sources in our country of Canada, making it safe to assume that this is what user uses (as oppposed to making up numbers for some unspecified "other" form of energy)  
		iii. If, in the annual emissions survey, user selects either of the "yes" options for "do you use any renewable energy sources for electricity or heating (e.g., solar, wind)?", the provided values (in the Formula excel file) are subtracted from user total emissions. This may put user emissions into negative values. The survey results allow for and reflect this possibility  
	16. In the annual emissions survey, when prompted to input their country, user is NOT given the option to select certain arbitrary political regions that are included in the Global_Averages.csv file provided by the teaching team. These exclusions include but are not limited to options such as "Low-middle income countries", "Asia (excl. China and India)" and "European Union (27)" as these are not specific countries so it would be unreasonable to have these set as the user's default country. All concrete, individual countries (i.e. India, Russia, etc.) are included in the available options. Note that, for displaying comparison graphs between user emissions and national averages, ALL values specified in the Global_Averages.csv file are included  
	17. Activity emissions values are based closely on those provided in the Formulas excel file, but do vary since exact values for activity emissions are not provided  
		i. Emissions for "Cycling/Walking" activity are 0kgCO2 as cycling and walking does not emit any significant amount of carbon dioxide  
		ii. For flight activities, the emissions of a single short-haul flight is set to 225kgCO2 (as taken from Formulas file for 1-2 short-haul flights) and multiplied by number of short-haul flights taken. Same for long-haul flights, but one long-haul flight has 550kgCO2 emitted (double that of a short-haul)  
		iii. Food "meal" activity: emissions for a single serving are set, then multiplied by # of servings inputted by user. Single-serving emissions calculated from Formulas file as (emissions if you consume the selected meal daily, as given in Formulas file) divided by ~365 days per year  
		iv. Emissions for buying a single clothing item are computed as 6kgCO2: computed using formulas file:  
			a) monthly buyers of clothes get 360kgco2/year  
			b) suppose 5 clothing items per month @6kgco2 per clothing item. This gets the given 360kgco2/year number. So we use 6kgco2 per clothing item  
		v. For buying electronics, 300kgCO2 set as emissions for one smartphone (as in Formulas file; emissions for buying 1 electronic device), 600kg for one computer, 900kg for one T/V (computer & T/V emissions just taken as 2x and 3x those of a smartphone, respectively). These values are multiplied by # of electronics purchased  
		vi. For other purchases, 100kgCO2 set as emissions for furniture (by assumption) and 900kgCO2 for appliances (by assumption, as appliances are like big electronic devices, somewhat similar in size and materials to a T/V) then multiplied by # of items purchased  
		vii. Emissions for monthly energy bills are computed as (emissions for 2 occupants, detached house under 1000sqft) / 365 days in a year (this value is added to each day for past 30 days, as mentioned in assumption #11)  
			a) Since emissions for water bills are not mentioned in Formulas file (water HEATING is mentioned, not water use) emissions for water use are made to be the mean (average) of gas and electricity emissions  
	18: Eco Guage trendline graph: for any selected time period, if there exist days in the time period for which no activity data is logged by the user, it is assumed that  
		this is "missing data" and such days are omitted from the trendline graph (as per the advice of Professor Rawad)  

IMPLEMENTATION DETAILS/DEVELOPER INFORMATION

	1. Libraries/Dependencies  
		i. MPAndroidChart for Eco Guage charts (https://github.com/PhilJay/MPAndroidChart)  
		ii. MaterialCalendarView, an extension of the built-in CalendarView library, for the Eco Tracker calendar (https://github.com/prolificinteractive/material-calendarview)  
		iii. OpenCSV, a csv file reader, for reading emissions of other countries in Eco Guage comparison chart (https://opencsv.sourceforge.net/)  
		iv. YouTubePlayer for Eco Hub learning resources videos (https://github.com/PierfrancescoSoffritti/android-youtube-player)  
		v. FirebaseAuth, to access firebase project for authentication and realtime database (https://firebase.google.com/docs/auth)  
		vi. Mockito, for unit testing of login module (https://site.mockito.org/)  
		vii. GoogleSignInClient, to sign in using google (https://developers.google.com/android/reference/com/google/android/gms/auth/api/identity/SignInClient)  
		
	2. Code Structure  
		i. Eco Tracker  
			a) Eco Tracker main page: EcoTrackerFragment.java (business logic) and fragment_eco_tracker.xml (UI)  
			b) Add/Edit activities feature: AddActivity.java and fragment_add_activity.xml  
			c) Add/Edit/Remove habits feature: AddHabit.java and fragment_add_habit.xml  
			d) Calendar feature: CalendarFragment.java and fragment_calendar.xml  
		ii. Eco Guage  
			a) Eco Guage main page: EcoGuageFragment.java and fragment_eco_guage.xml  
		iii. Eco Balance  
			a) Eco Balance main page: EcoBalanceFragment.java and fragment_eco_balance_fragment.xml  
			b) Eco balance offset projects page: EcoBalanceDestinationFragment.java and fragment_eco_balance_destination.xml  
			c) Individual offset project pages: Waste, Technology, Plant a Tree, (fossil) Fuels, Farmers, and Clean Air. Their associated files are easily identifiable  
		iv. Eco Hub  
			a) Eco Hub main page: EcoHubEntryFragment.java and fragment_eco_hub_entry.xml  
			b) Learning resources feature: LearningResourcesFragment.java and fragment_learning_resources.xml  
			c) Market trends feature: MarketTrendsFragment.java and fragment_market_trends.xml  
		v. Login and Sign-Up modules  
			a) (login folder): model-view-presenter structure. Files are easily identifiable  
			b) SignUpFragment, ForgotPasswordFragment and their associated xml files
		vi. Navigation  
			a) Primary (home) activity: HomeActivity.java and activity_home.xml  
			b) Navigation control files: menu/bottom_nav_menu.xml and navigation/nav_graph.xml  
		vii. Application startup files  
			a) AndroidManifest.xml, MainActivity.java and activity_main.xml  
		viii. Extra computation/logic files of utilities and customDataStructures folders  
			a) (utilities) Constants.java: constants pertaining to computations made in Eco Guage, Eco Tracker and annual carbon emissions survey features  
			b) (customDataStructures) EmissionNode.java and EmissionNodeCollection.java: used to retrieve user emissions over specified time periods (Eco Guage and Eco Tracker)  
