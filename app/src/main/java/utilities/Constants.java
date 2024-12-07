package utilities;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constants {

    public static final String USER_DATA = "user data";

    public static final String UNVERIFIED_USERS = "unverified users";
    public static final String FIREBASE_LINK = "https://planetze-c3c95-default-rtdb.firebaseio.com/";
    public static final DatabaseReference USER_REFERENCE = FirebaseDatabase.getInstance(FIREBASE_LINK).getReference(USER_DATA);
    public static final DatabaseReference UNVERIFIED_USERS_REFERENCE = FirebaseDatabase.getInstance(FIREBASE_LINK).getReference(UNVERIFIED_USERS);
    public static final FirebaseAuth AUTH = FirebaseAuth.getInstance();

    public static final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "June",
            "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

    // All survey question categories
    public static final String[] categories = {"Transportation", "Food", "Housing", "Consumption"};
    //categories1 is used specifically because of the first survey question regarding country of residence
    public static final String[] categories1 = {"Area of Residence", "Transportation", "Food", "Housing", "Consumption"};
    public static final String[] impacts = {"Select an impact level", "Low Emissions (< 1kg CO2)",
            "Medium Emissions (< 5kg CO2)", "High Emissions (> 5kg CO2)"};


    public static final int DAILY = 1;
    public static final int WEEKLY = 7;
    public static final int MONTHLY = 30;
    public static final int YEARLY = 365;
    public static final int OVERALL = Integer.MAX_VALUE;

    public static final int EMISSION_TYPE_INDEX = 0;
    public static final int EMISSIONS_AMOUNT_INDEX = 2;

    public static final int transport_qs = 7;  //number of qs in transport category
    public static final int food_qs = 6;
    public static final int housing_qs = 7;
    public static final int consumption_qs = 4;

    // Firebase retrieval constants
    public static final String STAY_LOGGED_ON = "stay_logged_on";
    public static final String INTERPOLATE_EMISSIONS_DATA = "interpolate_emissions_data";
    public static final String HIDE_GRID_LINES = "hide_grid_lines";
    public static final String HIDE_TREND_LINE_POINTS = "show_trend_line_points";
    public static final String DEFAULT_COUNTRY = "default_country";
    public static final String DEFAULT_CAR = "default_car";
    public static final String EMAIL = "email";
    public static final String USERNAME = "name";

    // All survey questions. Each sub array is a question followed by all possible answer options
    public static final String[][] questions = {
            {"Please specify your current location.", "Afghanistan", "Africa", "Albania", "Algeria", "Andorra", "Angola", "Anguilla", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Asia", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bonaire Sint Eustatius and Saba", "Bosnia and Herzegovina", "Botswana", "Brazil", "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia", "Cuba", "Curacao", "Cyprus", "Czechia", "Democratic Republic of Congo", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia", "Europe", "Faroe Islands", "Fiji", "Finland", "France", "French Polynesia", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Greenland", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kosovo", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macao", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mexico", "Micronesia (country)", "Moldova", "Mongolia", "Montenegro", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "North America", "North Korea", "North Macedonia", "Norway", "Oceania", "Oman", "Pakistan", "Palau", "Palestine", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar", "Romania", "Russia", "Rwanda", "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia", "Saint Pierre and Miquelon", "Saint Vincent and the Grenadines", "Samoa", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Sint Maarten (Dutch)", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South America", "South Korea", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Wallis and Futuna", "Yemen", "Zambia", "Zimbabwe"},
            {"-"},
            {"Do you own or regularly use a car?", "Yes", "No"},
            {"What type of car do you drive?", "Gasoline", "Diesel", "Hybrid", "Electric", "I don't know"},
            {"How many kilometers/miles do you drive per year?", "Up to 5,000 km", "5,000–10,000 km", "10,000–15,000 km", "15,000–20,000 km", "20,000–25,000 km", "More than 25,000 km"},
            {"How often do you use public transportation?", "Never", "Occasionally (1-2 times/week)", "Frequently (3-4 times/week)", "Always (5+ times/week)"},
            {"How much time do you spend on public transport per week?", "Under 1 hour", "1-3 hours", "3-5 hours", "5-10 hours", "More than 10 hours"},
            {"How many short-haul flights have you taken in the past year?", "None", "1-2 flights", "3-5 flights", "6-10 flights", "More than 10 flights"},
            {"How many long-haul flights have you taken in the past year?", "None", "1-2 flights", "3-5 flights", "6-10 flights", "More than 10 flights"},
            {"-"},
            {"What best describes your diet?", "Vegetarian", "Vegan", "Pescatarian", "Meat-based"},
            {"How often do you eat beef?", "Daily", "Frequently (3-5 times/week)", "Occasionally (1-2 times/week)", "Never"},
            {"How often do you eat pork?", "Daily", "Frequently (3-5 times/week)", "Occasionally (1-2 times/week)", "Never"},
            {"How often do you eat chicken?", "Daily", "Frequently (3-5 times/week)", "Occasionally (1-2 times/week)", "Never"},
            {"How often do you eat fish?", "Daily", "Frequently (3-5 times/week)", "Occasionally (1-2 times/week)", "Never"},
            {"How often do you waste food or throw away uneaten leftovers?", "Never", "Rarely", "Occasionally", "Frequently"},
            {"-"},
            {"What type of home do you live in?", "Detached house", "Semi-detached house", "Townhouse", "Condo/Apartment", "Other"},
            {"How many people live in your household?", "1", "2", "3-4", "5 or more"},
            {"What is the size of your home?", "Under 1000 sq. ft.", "1000-2000 sq. ft.", "Over 2000 sq. ft."},
            {"What type of energy do you use to heat your home?", "Natural Gas", "Electricity", "Oil", "Propane", "Wood", "Other"},
            {"What is your average monthly electricity bill?", "Under $50", "$50-$100", "$100-$150", "$150-$200", "Over $200"},
            {"What type of energy do you use to heat water?", "Natural Gas", "Electricity", "Oil", "Propane", "Wood", "Other"},
            {"Do you use any renewable energy sources for electricity or heating?", "Yes, primarily", "Yes, partially", "No"},
            {"-"},
            {"How often do you buy new clothes?", "Monthly", "Quarterly", "Annually", "Rarely"},
            {"Do you buy second-hand or eco-friendly products?", "Yes, regularly", "Yes, occasionally", "No"},
            {"How many electronic devices have you purchased in the past year?", "None", "1", "2", "3", "4 or more"},
            {"How often do you recycle?", "Never", "Occasionally", "Frequently", "Always"}};

    public static final String[] activityCats = {"Select a Category",
            "Transportation", "Food", "Consumption", "Energy"};
    public static final String[][] activities = {
            {"Select an Activity", "Drive personal vehicle", "Take public transportation",
                    "Cycling/Walking", "Flight (< 1,500km)", "Flight (> 1,500km)"},
            {"Select an Activity", "Meal"},
            {"Select an Activity", "Buy new clothes", "Buy electronics", "Other purchases"},
            {"Select an Activity", "Electricity", "Gas", "Water"}};

    public static final String[] country = {
            "Afghanistan",
            "Africa",
            "Albania",
            "Algeria",
            "Andorra",
            "Angola",
            "Anguilla",
            "Antigua and Barbuda",
            "Argentina",
            "Armenia",
            "Aruba",
            "Asia",
            "Asia (excl. China and India)",
            "Australia",
            "Austria",
            "Azerbaijan",
            "Bahamas",
            "Bahrain",
            "Bangladesh",
            "Barbados",
            "Belarus",
            "Belgium",
            "Belize",
            "Benin",
            "Bermuda",
            "Bhutan",
            "Bolivia",
            "Bonaire Sint Eustatius and Saba",
            "Bosnia and Herzegovina",
            "Botswana",
            "Brazil",
            "British Virgin Islands",
            "Brunei",
            "Bulgaria",
            "Burkina Faso",
            "Burundi",
            "Cambodia",
            "Cameroon",
            "Canada",
            "Cape Verde",
            "Central African Republic",
            "Chad",
            "Chile",
            "China",
            "Colombia",
            "Comoros",
            "Congo",
            "Cook Islands",
            "Costa Rica",
            "Cote d'Ivoire",
            "Croatia",
            "Cuba",
            "Curacao",
            "Cyprus",
            "Czechia",
            "Democratic Republic of Congo",
            "Denmark",
            "Djibouti",
            "Dominica",
            "Dominican Republic",
            "East Timor",
            "Ecuador",
            "Egypt",
            "El Salvador",
            "Equatorial Guinea",
            "Eritrea",
            "Estonia",
            "Eswatini",
            "Ethiopia",
            "Europe",
            "Europe (excl. EU-27)",
            "Europe (excl. EU-28)",
           "European Union (27)",
            "European Union (28)",
            "Faroe Islands",
            "Fiji",
            "Finland",
            "France",
            "French Polynesia",
            "Gabon",
            "Gambia",
            "Georgia",
            "Germany",
            "Ghana",
            "Greece",
            "Greenland",
            "Grenada",
            "Guatemala",
            "Guinea",
            "Guinea-Bissau",
            "Guyana",
            "Haiti",
            "High-income countries",
            "Honduras",
            "Hong Kong",
            "Hungary",
            "Iceland",
            "India",
            "Indonesia",
            "Iran",
            "Iraq",
            "Ireland",
            "Israel",
            "Italy",
            "Jamaica",
            "Japan",
            "Jordan",
            "Kazakhstan",
            "Kenya",
            "Kiribati",
            "Kosovo",
            "Kuwait",
            "Kyrgyzstan",
            "Laos",
            "Latvia",
            "Lebanon",
            "Lesotho",
            "Liberia",
            "Libya",
            "Liechtenstein",
            "Lithuania",
            "Low-income countries",
            "Lower-middle-income countries",
            "Luxembourg",
            "Macao",
            "Madagascar",
            "Malawi",
            "Malaysia",
            "Maldives",
            "Mali",
            "Malta",
            "Marshall Islands",
            "Mauritania",
            "Mauritius",
            "Mexico",
            "Micronesia (country)",
            "Moldova",
            "Mongolia",
            "Montenegro",
            "Montserrat",
            "Morocco",
            "Mozambique",
            "Myanmar",
            "Namibia",
            "Nauru",
            "Nepal",
            "Netherlands",
            "New Caledonia",
            "New Zealand",
            "Nicaragua",
            "Niger",
            "Nigeria",
            "Niue",
            "North America",
            "North America (excl. USA)",
            "North Korea",
            "North Macedonia",
            "Norway",
            "Oceania",
            "Oman",
            "Pakistan",
            "Palau",
            "Palestine",
            "Panama",
            "Papua New Guinea",
            "Paraguay",
            "Peru",
            "Philippines",
            "Poland",
            "Portugal",
            "Qatar",
            "Romania",
            "Russia",
            "Rwanda",
            "Saint Helena",
            "Saint Kitts and Nevis",
            "Saint Lucia",
            "Saint Pierre and Miquelon",
            "Saint Vincent and the Grenadines",
            "Samoa",
            "Sao Tome and Principe",
            "Saudi Arabia",
            "Senegal",
            "Serbia",
            "Seychelles",
            "Sierra Leone",
            "Singapore",
            "Sint Maarten (Dutch)",
            "Slovakia",
            "Slovenia",
            "Solomon Islands",
            "Somalia",
            "South Africa",
            "South America",
            "South Korea",
            "South Sudan",
            "Spain",
            "Sri Lanka",
            "Sudan",
            "Suriname",
            "Sweden",
            "Switzerland",
            "Syria",
            "Taiwan",
            "Tajikistan",
            "Tanzania",
            "Thailand",
            "Togo",
            "Tonga",
            "Trinidad and Tobago",
            "Tunisia",
            "Turkey",
            "Turkmenistan",
            "Turks and Caicos Islands",
            "Tuvalu",
            "Uganda",
            "Ukraine",
            "United Arab Emirates",
            "United Kingdom",
            "United States",
            "Upper-middle-income countries",
            "Uruguay",
            "Uzbekistan",
            "Vanuatu",
            "Venezuela",
            "Vietnam",
            "Wallis and Futuna",
            "World",
            "Yemen",
            "Zambia",
            "Zimbabwe"
    };

    public static final double[] country_emissions = {
            0.29536375,
            0.99422127,
            1.7432004,
            3.9272263,
            4.6171236,
            0.45155162,
            8.752724,
            6.4218745,
            4.2378173,
            2.3045583,
            8.133404,
            4.611434,
            4.017375,
            14.985412,
            6.8781943,
            3.6746833,
            5.1708703,
            25.672274,
            0.5964455,
            4.3772573,
            6.1669006,
            7.6875386,
            1.7894346,
            0.631487,
            6.9370627,
            1.3489918,
            1.7583066,
            4.083284,
            6.1034565,
            2.838951,
            2.2454574,
            5.0039577,
            23.950201,
            6.8044534,
            0.26295447,
            0.06194545,
            1.1900775,
            0.34292704,
            14.249212,
            0.9588915,
            0.040548485,
            0.13367727,
            4.3041654,
            7.992761,
            1.9223082,
            0.49327007,
            1.2447897,
            3.9950094,
            1.5226681,
            0.41668788,
            4.348515,
            1.8659163,
            9.189007,
            5.616782,
            9.3357525,
            0.036375992,
            4.940161,
            0.40418932,
            2.1058853,
            2.1051137,
            0.49869007,
            2.3117273,
            2.333106,
            1.2174718,
            3.0307202,
            0.18914719,
            7.77628,
            1.0527312,
            0.15458965,
            6.8578663,
            7.886797,
            8.817789,
            6.1743994,
            5.983708,
            14.084624,
            1.1550449,
            6.5267396,
            4.603891,
            2.8509297,
            2.3882635,
            0.2847278,
            2.962545,
            7.9837584,
            0.6215505,
            5.7451057,
            10.473997,
            2.7133646,
            1.0756185,
            0.35742033,
            0.15518051,
            4.3736935,
            0.21119381,
            10.132565,
            1.0696708,
            4.081913,
            4.449911,
            9.499798,
            1.9966822,
            2.6456614,
            7.7993317,
            4.024638,
            7.7211185,
            6.208912,
            5.726825,
            2.2945588,
            8.501681,
            2.0301995,
            13.979704,
            0.45998666,
            0.5184742,
            4.830646,
            25.578102,
            1.4251612,
            3.0803475,
            3.561689,
            4.3543963,
            1.3594668,
            0.1653753,
            9.242238,
            3.8097827,
            4.606163,
            0.28005043,
            1.777996,
            11.618432,
            1.5127679,
            0.14871116,
            0.10262384,
            8.576508,
            3.2475724,
            0.31153768,
            3.1035979,
            3.6353714,
            0.957337,
            3.2697906,
            4.0153365,
            1.3243006,
            1.6565942,
            11.150772,
            3.6558185,
            4.8447766,
            1.8263615,
            0.24274588,
            0.6445672,
            1.5399038,
            4.1700416,
            0.5074035,
            7.1372175,
            17.641167,
            6.212154,
            0.79879653,
            0.116688,
            0.5891771,
            3.8729508,
            10.5346775,
            4.741475,
            1.9513915,
            3.6245701,
            7.5093055,
            9.85179,
            15.730261,
            0.84893465,
            12.123921,
            0.6660658,
            2.699258,
            0.77131313,
            1.3299496,
            1.7891879,
            1.3014648,
            8.106886,
            4.050785,
            37.601273,
            3.739777,
            11.416899,
            0.112346195,
            3.2986484,
            4.708081,
            2.6149206,
            10.293288,
            2.2964725,
            1.1218625,
            0.5816142,
            18.197495,
            0.6738352,
            6.024712,
            6.1495123,
            0.13124847,
            8.911513,
            14.352394,
            6.051555,
            5.9979916,
            0.41232163,
            0.03676208,
            6.7461643,
            2.4865332,
            11.598764,
            0.1680176,
            5.1644425,
            0.7936504,
            0.4696261,
            5.8029985,
            3.6069093,
            4.0478554,
            1.2490375,
            11.630868,
            1.0064901,
            0.23771806,
            3.7762568,
            0.2910665,
            1.7686282,
            22.423758,
            2.879285,
            5.1052055,
            11.03418,
            7.636793,
            1.0004411,
            0.12744623,
            3.5578535,
            25.833244,
            4.7201805,
            14.949616,
            6.2268133,
            2.3060381,
            3.4830604,
            0.6363055,
            2.7168686,
            3.4995174,
            2.2819076,
            4.658219,
            0.33701748,
            0.44570068,
            0.542628
    };

    public static final double[][] public_trans_emissions = {
            {  //never use public transport
                0,  //under 1 hour
                0,  //1-3 hours
                0,  //3-5 hours
                0,  //5-10 hours
                0  //10+ hours
            },
            {246, 819, 1638, 3071, 4095},  //occasionally use
            {573, 1911, 3822, 7166, 9555},  //frequently use
            {573, 1911, 3822, 7166, 9555}
    };  //always use


    public static final double[][][] recycling_reduction = {
            {  //no electronic devices
                    {  //monthly clothes
                            0,  //never recycle (always 0)
                            54,  //occasionally recycle, etc.
                            108,
                            180},
                    {  //quarterly clothes, etc.
                            0,
                            15,
                            30,
                            50},
                    {  //note that annually and quarterly are kept same since quarterly data omitted from given excel file
                            0,
                            15,
                            30,
                            50},
                    {
                            0,
                            0.75,
                            1.5,
                            2.5}
            },
            {  //1 electronic device, etc.
                    {0, 54 + 45, 108 + 60, 180 + 90},
                    {0, 15 + 45, 30 + 60, 50 + 90},
                    {0, 15 + 45, 30 + 60, 50 + 90},
                    {0, 0.75 + 45, 1.5 + 60, 2.5 + 90}
            },
            {
                    {0, 54 + 60, 108 + 120, 180 + 180},
                    {0, 15 + 60, 30 + 120, 50 + 180},
                    {0, 15 + 60, 30 + 120, 50 + 180},
                    {0, 0.75 + 60, 1.5 + 120, 2.5 + 180}
            },
            {
                    {0, 54 + 90, 108 + 180, 180 + 270},
                    {0, 15 + 90, 30 + 180, 50 + 270},
                    {0, 15 + 90, 30 + 180, 50 + 270},
                    {0, 0.75 + 90, 1.5 + 180, 2.5 + 270}
            },
            {
                    {0, 54 + 120, 108 + 240, 180 + 360},
                    {0, 15 + 120, 30 + 240, 50 + 360},
                    {0, 15 + 120, 30 + 240, 50 + 360},
                    {0, 0.75 + 120, 1.5 + 240, 2.5 + 360}
            }
    };


    public static final double[][][][][] housing_emissions = {
            {  //detached house
                    {  //1 person
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            2400,  //natural gas
                                            200,  //electricity
                                            2100,  //oil
                                            2870,  //propane
                                            2170  //wood
                                    },
                                    {  //$50-100/month
                                            2440,  //natural gas
                                            400,  //electricity
                                            5200,  //oil
                                            4400,  //propane
                                            2340  //wood
                                    },
                                    {  //$100-150/month
                                            2610,  //natural gas
                                            1200,  //electricity
                                            6100,  //oil
                                            5400,  //propane
                                            2510  //wood
                                    },
                                    {  //$150-200/month
                                            2780,  //natural gas
                                            1700,  //electricity
                                            7200,  //oil
                                            6400,  //propane
                                            2680  //wood
                                    },
                                    {  //>$200/month
                                            3100,  //natural gas
                                            2300,  //electricity
                                            8200,  //oil
                                            7400,  //propane
                                            3000  //wood
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            2800,  //natural gas
                                            300,  //electricity
                                            3800,  //oil
                                            3770,  //propane
                                            3670  //wood
                                    },
                                    {  //$50-100/month
                                            5900,  //natural gas
                                            600,  //electricity
                                            5300,  //oil
                                            3940,  //propane
                                            3840  //wood
                                    },
                                    {  //$100-150/month
                                            6500,  //natural gas
                                            1200,  //electricity
                                            6200,  //oil
                                            7100,  //propane
                                            4010  //wood
                                    },
                                    {  //$150-200/month
                                            7100,  //natural gas
                                            1800,  //electricity
                                            7200,  //oil
                                            4280,  //propane
                                            4180  //wood
                                    },
                                    {  //>$200/month
                                            8300,  //natural gas
                                            2400,  //electricity
                                            8200,  //oil
                                            4600,  //propane
                                            4500  //wood
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            2800,  //natural gas
                                            320,  //electricity
                                            5400,  //oil
                                            5570,  //propane
                                            4170  //wood
                                    },
                                    {  //$50-100/month
                                            3000,  //natural gas
                                            600,  //electricity
                                            10500,  //oil
                                            5740,  //propane
                                            4340  //wood
                                    },
                                    {  //$100-150/month
                                            3200,  //natural gas
                                            1800,  //electricity
                                            14000,  //oil
                                            5800,  //propane
                                            4510  //wood
                                    },
                                    {  //$150-200/month
                                            3400,  //natural gas
                                            2700,  //electricity
                                            17500,  //oil
                                            5852,  //propane
                                            4680  //wood
                                    },
                                    {  //>$200/month
                                            3600,  //natural gas
                                            3600,  //electricity
                                            21000,  //oil
                                            6100,  //propane
                                            5000  //wood
                                    }}},
                    {  //2 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            2600,  //natural gas
                                            250,  //electricity
                                            2650,  //oil
                                            3470,  //propane
                                            2370  //wood
                                    },
                                    {  //$50-100/month
                                            2640,  //natural gas
                                            500,  //electricity
                                            5400,  //oil
                                            4600,  //propane
                                            2540  //wood
                                    },
                                    {  //$100-150/month
                                            2810,  //natural gas
                                            1450,  //electricity
                                            6250,  //oil
                                            5600,  //propane
                                            2710  //wood
                                    },
                                    {  //$150-200/month
                                            2980,  //natural gas
                                            1900,  //electricity
                                            7400,  //oil
                                            6600,  //propane
                                            2880  //wood
                                    },
                                    {  //>$200/month
                                            3100,  //natural gas
                                            2500,  //electricity
                                            8400,  //oil
                                            7600,  //propane
                                            3200  //wood
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            3000,  //natural gas
                                            380,  //electricity
                                            4000,  //oil
                                            4470,  //propane
                                            4170  //wood
                                    },
                                    {  //$50-100/month
                                            6100,  //natural gas
                                            800,  //electricity
                                            5440,  //oil
                                            4640,  //propane
                                            4340  //wood
                                    },
                                    {  //$100-150/month
                                            6700,
                                            1450,
                                            6400,
                                            7230,
                                            4510
                                    },
                                    {  //$150-200/month
                                            7300,
                                            2000,
                                            7400,
                                            4980,
                                            4680
                                    },
                                    {  //>$200/month
                                            8500,
                                            2600,
                                            8400,
                                            5300,
                                            5000
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            2880,
                                            450,
                                            6200,
                                            6170,
                                            4670
                                    },
                                    {  //$50-100/month
                                            3200,
                                            900,
                                            11000,
                                            6340,
                                            4840
                                    },
                                    {  //$100-150/month
                                            3400,
                                            2100,
                                            15500,
                                            6410,
                                            5010
                                    },
                                    {  //$150-200/month
                                            3600,
                                            3100,
                                            18100,
                                            6560,
                                            5180
                                    },
                                    {  //>$200/month
                                            3800,
                                            3800,
                                            22000,
                                            6840,
                                            5500
                                    }}},
                    {  //3-4 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            2700,
                                            380,
                                            3200,
                                            4370,
                                            2670
                                    },
                                    {  //$50-100/month
                                            2940,
                                            550,
                                            5700,
                                            4900,
                                            2840
                                    },
                                    {  //$100-150/month
                                            3110,
                                            1600,
                                            6700,
                                            5900,
                                            3010
                                    },
                                    {  //$150-200/month
                                            3280,	2050,	7700,	6900,	3180
                                    },
                                    {  //>$200/month
                                            3600,	2700,	8700,	7900,	3500
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            3380,	500,	4700,	5670,	4870
                                    },
                                    {  //$50-100/month
                                            6400,	1050,	5800,	5740,	5040
                                    },
                                    {  //$100-150/month
                                            7000,	1600,	6700,	7400,	5210
                                    },
                                    {  //$150-200/month
                                            7600,	2250,	7700,	5985,	5380
                                    },
                                    {  //>$200/month
                                            8800,	280,	8700,	6350,	5750
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            3000,	520,	7000,	6970,	5270
                                    },
                                    {  //$50-100/month
                                            3500,	1500,	12500,	7240,	5640
                                    },
                                    {  //$100-150/month
                                            3700,	2300,	16250, 7300,	5710
                                    },
                                    {  //$150-200/month
                                            4100,	3400,	20000, 7600,	5980
                                    },
                                    {  //>$200/month
                                            4100,	4000,	23500, 7890,	6250
                                    }}},
                    {  //5 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            3000,	450,	3700,	5270,	2970
                                    },
                                    {  //$50-100/month
                                            3240,	600,	6000,	5200,	3140
                                    },
                                    {  //$100-150/month
                                            3410,	1800,	6950,	6200,	3310
                                    },
                                    {  //$150-200/month
                                            3580,	2200,	8000,	7200,	3480
                                    },
                                    {  //>$200/month
                                            3900,	3000,	9000,	8200,	3800
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            3860,	600,	5350,	6570,	5670
                                    },
                                    {  //$50-100/month
                                            6700,	1200,	6100,	6740,	5840
                                    },
                                    {  //$100-150/month
                                            7300,	1800,	7000,	7550,	6010
                                    },
                                    {  //$150-200/month
                                            7900,	2400,	8000,	7080,	6180
                                    },
                                    {  //>$200/month
                                            9100,	3100,	9000,	7400,	6500
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            3230,	675,	8900,	7970,	6170
                                    },
                                    {  //$50-100/month
                                            3800,	1800,	14000,	8140,	6340
                                    },
                                    {  //$100-150/month
                                            4000,	2700,	17500, 8230,	6510
                                    },
                                    {  //$150-200/month
                                            4400,	3600,	21000, 8300,	6680
                                    },
                                    {  //>$200/month
                                            4400,	4200,	25000, 8710,	7000
                                    }}}},
            {  //semi
                    {  //1 person
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            2160,	300,	2500,	2200,	2100
                                    },
                                    {  //$50-100/month
                                            2400,	410,	2800,	2600,	3000
                                    },
                                    {  //$100-150/month
                                            2600,	1210,	3000,	2800,	3200
                                    },
                                    {  //$150-200/month
                                            2800,	1700,	3200,	3000,	3400
                                    },
                                    {  //>$200/month
                                            3000,	2200,	3400,	3200,	3600
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            2443,	300,	3400,	4000,	1500
                                    },
                                    {  //$50-100/month
                                            4000,	600,	4000,	5000,	2500
                                    },
                                    {  //$100-150/month
                                            4500,	1200,	6100,	7000,	4100
                                    },
                                    {  //$150-200/month
                                            5000,	1800,	8000,	9000,	5500
                                    },
                                    {  //>$200/month
                                            6000,	2400,	10550,	12000,	7220
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            2821,	300,	3820,	4370,	3970
                                    },
                                    {  //$50-100/month
                                            7500,	1200,	5500,	4540,	4140
                                    },
                                    {  //$100-150/month
                                            10000,	1800,	8500,	4710,	4310
                                    },
                                    {  //$150-200/month
                                            12500,	2400,	11000,	4880,	4480
                                    },
                                    {  //>$200/month
                                            15000,	3000,	14000,	5200,	4800
                                    }}},
                    {  //2 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            2349,	410, 2592,	2300,	2450
                                    },
                                    {  //$50-100/month
                                            2600,	500,	3000,	2800,	3200
                                    },
                                    {  //$100-150/month
                                            2800,	1450,	3200,	3000,	3400
                                    },
                                    {  //$150-200/month
                                            3000,	1900,	3400,	3200,	3600
                                    },
                                    {  //>$200/month
                                            3200,	2500,	3600,	3400,	3800
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            2727,	410,	3499,	4300,	1800
                                    },
                                    {  //$50-100/month
                                            5200,	800,	4600,	6200,	2700
                                    },
                                    {  //$100-150/month
                                            6000,	1550,	6900,	8000,	4300
                                    },
                                    {  //$150-200/month
                                            6500,	2000,	8800,	10200,	6000
                                    },
                                    {  //>$200/month
                                            7800,	2500,	10900,	13200,	8000
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            3010,	560,	4000,	4870,	4470
                                    },
                                    {  //$50-100/month
                                            8800,	1400,	6000,	5040,	4640
                                    },
                                    {  //$100-150/month
                                            12000,	2000,	9200,	5210,	4810
                                    },
                                    {  //$150-200/month
                                            14200,	2600,	12000,	5380,	4980
                                    },
                                    {  //>$200/month
                                            16800,	3500,	14800,	5700,	5300
                                    }}},
                    {  //3-4 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            2732,	500, 2680,	2450,	2700
                                    },
                                    {  //$50-100/month
                                            2900,	560,	3300,	3100,	3500
                                    },
                                    {  //$100-150/month
                                            3100,	1620,	3500,	3300,	3700
                                    },
                                    {  //$150-200/month
                                            3300,	2000,	3700,	3500,	3900
                                    },
                                    {  //>$200/month
                                            3500,	2600,	3900,	3700,	4100
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            3151,	550,	3599,	4700,	2100
                                    },
                                    {  //$50-100/month
                                            6800,	1050,	5100,	7300,	3500
                                    },
                                    {  //$100-150/month
                                            7800,	1700,	7300,	9100,	4850
                                    },
                                    {  //$150-200/month
                                            8800,	2250,	9200,	11000,	6800
                                    },
                                    {  //>$200/month
                                            9800,	2800,	11200,	14100,	8600
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            3261,	890, 4307,	5670,	5270
                                    },
                                    {  //$50-100/month
                                            10800,	1650,	7200,	5840,	5340
                                    },
                                    {  //$100-150/month
                                            14000,	2300,	10200,	6010,	5610
                                    },
                                    {  //$150-200/month
                                            16000,	2820,	13500,	6180,	5780
                                    },
                                    {  //>$200/month
                                            18200,	4000,	15500,	6500,	6150
                                    }}},
                    {  //5 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            3199,	580, 2750,	2600,	3000
                                    },
                                    {  //$50-100/month
                                            3200,	600,	3600,	3400,	3800
                                    },
                                    {  //$100-150/month
                                            3400,	1820,	3800,	3600,	4000
                                    },
                                    {  //$150-200/month
                                            3600,	2200,	4000,	3800,	4200
                                    },
                                    {  //>$200/month
                                            3800,	3000,	4200,	4000,	4400
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            3578,	605,	3700,	4900,	2500
                                    },
                                    {  //$50-100/month
                                            7500,	1200,	6000,	8000,	4000
                                    },
                                    {  //$100-150/month
                                            8500,	1800,	8500,	10000,	5500
                                    },
                                    {  //$150-200/month
                                            10000,	2400,	10500,	12000,	7100
                                    },
                                    {  //>$200/month
                                            11000,	3200,	12000,	15000,	9100
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            3578,	1000,	4400,	6370,	5970
                                    },
                                    {  //$50-100/month
                                            12500,	1800,	8500,	6540,	6140
                                    },
                                    {  //$100-150/month
                                            15000,	2400,	11000,	6710,	6310
                                    },
                                    {  //$150-200/month
                                            17500,	3000,	14000,	6880,	6480
                                    },
                                    {  //>$200/month
                                            19000,	4500,	16000,	7200,	6800
                                    }}}},
            {  //townhouse/other
                    {  //1 person
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1971,	300,	2400,	1500,	2000
                                    },
                                    {  //$50-100/month
                                            2800,	500,	2800,	2500,	2800
                                    },
                                    {  //$100-150/month
                                            3000,	1000,	3600,	3000,	3000
                                    },
                                    {  //$150-200/month
                                            4000,	1600,	4500,	3700,	3330
                                    },
                                    {  //>$200/month
                                            8000,	2000,	5000,	5800,	3500
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            2443,	300, 2590,	3170,	1400
                                    },
                                    {  //$50-100/month
                                            4000,	550,	3800,	5600,	2400
                                    },
                                    {  //$100-150/month
                                            4300,	1200,	5000,	6000,	4000
                                    },
                                    {  //$150-200/month
                                            4800,	1700,	5350,	3680,	3800
                                    },
                                    {  //>$200/month
                                            9500,	2500,	5370,	6000,	4000
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            2822, 300,	2810,	3340,	3800
                                    },
                                    {  //$50-100/month
                                            3600,	1200,	4300,	5900,	3500
                                    },
                                    {  //$100-150/month
                                            5000,	1800,	5300,	3510,	4100
                                    },
                                    {  //$150-200/month
                                            8000,	2400,	5440,	3800,	4200
                                    },
                                    {  //>$200/month
                                            9500,	2800,	5670,	6200,	4300
                                    }}},
                    {  //2 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            2160,	410,	2523,	1850,	2250
                                    },
                                    {  //$50-100/month
                                            2910,	550,	3100,	2800,	3000
                                    },
                                    {  //$100-150/month
                                            3210,	1250,	3750,	3500,	3300
                                    },
                                    {  //$150-200/month
                                            5500,	1750,	4600,	4500,	3500
                                    },
                                    {  //>$200/month
                                            9500,	2100,	5200,	6800,	3700
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            2750	,380	,2620	,3770,	1560
                                    },
                                    {  //$50-100/month
                                            5000	,700	,4320	,5940	,2600
                                    },
                                    {  //$100-150/month
                                            5500,	1350,	5800	,6200	,4310
                                    },
                                    {  //$150-200/month
                                            6300,	1900,	5500	,4280,	3800
                                    },
                                    {  //>$200/month
                                            10100	,2780	,5500	,6600,	4640
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            3010,	560	,3000	,3940,	4070
                                    },
                                    {  //$50-100/month
                                            3840,	1380,	4900,	6330,	3930
                                    },
                                    {  //$100-150/month
                                            6200,	2000,	5690,	4110,	4500
                                    },
                                    {  //$150-200/month
                                            8300,	2500,	5600,	4500,	4640
                                    },
                                    {  //>$200/month
                                            10100,	3000,	5800,	6900,	4700
                                    }}},
                    {  //3-4 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            2500,	500,	2600,	2100,	2500
                                    },
                                    {  //$50-100/month
                                            3000	,580	,3250,	3400,	3300
                                    },
                                    {  //$100-150/month
                                            3500	,1320	,3900	,4100	,3520
                                    },
                                    {  //$150-200/month
                                            6200,	1900,	4800,	5200,	3720
                                    },
                                    {  //>$200/month
                                            10200,	2300	,5300	,7200,	4000
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            3111,	500,	2730	,4670	,1900
                                    },
                                    {  //$50-100/month
                                            6500	,950,	4800	,6140	,3300
                                    },
                                    {  //$100-150/month
                                            6800	,1520,	6200	,6420	,4600
                                    },
                                    {  //$150-200/month
                                            8500	,2150,	5720,	5180	,4220
                                    },
                                    {  //>$200/month
                                            11200	,3000,	5800,	6800,	5000
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            3300	,890	,3468,	4840	,5000
                                    },
                                    {  //$50-100/month
                                            3900	,1600	,5320	,6440,	4360
                                    },
                                    {  //$100-150/month
                                            7000,	2200,	6250,	5010	,4780
                                    },
                                    {  //$150-200/month
                                            9000	,2650,	5800	,5380,	5000
                                    },
                                    {  //>$200/month
                                            10300,	3800	,6100,	7500,	5100
                                    }}},
                    {  //5 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            2800	,550,	2720	,2500,	2600
                                    },
                                    {  //$50-100/month
                                            3200	,600	,3500	,3800	,3400
                                    },
                                    {  //$100-150/month
                                            3800	,1420,	4050,	5000	,3800
                                    },
                                    {  //$150-200/month
                                            8000	,2000	,5100	,5800,	4000
                                    },
                                    {  //>$200/month
                                            12000	,3000,	5600	,7800	,4300
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            3580,	590	,2800,	5570	,2200
                                    },
                                    {  //$50-100/month
                                            7300,	1100,	5500,	6340,	3800
                                    },
                                    {  //$100-150/month
                                            8340,	1700,	6100,	6500	,5100
                                    },
                                    {  //$150-200/month
                                            9000,	2400	,5900,	6080	,4400
                                    },
                                    {  //>$200/month
                                            14000,3500,	6200	,7400,	5430
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            3600	,1000	,3760	,5740,	5600
                                    },
                                    {  //$50-100/month
                                            5100,	1750,	5800,	6900,	5000
                                    },
                                    {  //$100-150/month
                                            8000,	2300,	6500	,5910,	5360
                                    },
                                    {  //$150-200/month
                                            9500,	2800	,6000,	6200	,5400
                                    },
                                    {  //>$200/month
                                            11000	,4300	,6350	,7850	,5500
                                    }}}},
            {  //condo
                    {  //1 person
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1680	,200	,0	,1320,	1600
                                    },
                                    {  //$50-100/month
                                            2240,	500,	0,	2100	,1800
                                    },
                                    {  //$100-150/month
                                            2800,	900	,0,	3000	,2800
                                    },
                                    {  //$150-200/month
                                            3700	,1400,	0	,3300	,3000
                                    },
                                    {  //>$200/month
                                            5000,	1900,	0	,5700	,3500
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            2060	,300,	0	,1500	,1800
                                    },
                                    {  //$50-100/month
                                            2500,	550	,0,	3000	,2200
                                    },
                                    {  //$100-150/month
                                            3100	,1250,	0	,4100	,3200
                                    },
                                    {  //$150-200/month
                                            4000	,1900	,0,	4550	,3100
                                    },
                                    {  //>$200/month
                                            5350,	2300	,0,	6000,	3900
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            2440,	350	,0,	1800,	2300
                                    },
                                    {  //$50-100/month
                                            2700	,610,	0	,3650	,2600
                                    },
                                    {  //$100-150/month
                                            3670,	1500,	0	,4500,	3500
                                    },
                                    {  //$150-200/month
                                            4250	,2150,	0	,5000	,3530
                                    },
                                    {  //>$200/month
                                            6000,	2600	,0	,6500,	4100
                                    }}},
                    {  //2 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1870	,250	,0	,1550	,1850
                                    },
                                    {  //$50-100/month
                                            2430	,550	,0	,2400	,2000
                                    },
                                    {  //$100-150/month
                                            3000,	1100	,0	,3300	,3000
                                    },
                                    {  //$150-200/month
                                            4100	,1600,	0	,3700	,3100
                                    },
                                    {  //>$200/month
                                            7200,	2100,	0	,6000	,3600
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            2260	,400	,0,	1700,	2200
                                    },
                                    {  //$50-100/month
                                            2880	,670	,0	,3400,	2500
                                    },
                                    {  //$100-150/month
                                            3300	,1450	,0	,4600	,3500
                                    },
                                    {  //$150-200/month
                                            4700,	2300,	0,	4950,	3300
                                    },
                                    {  //>$200/month
                                            7550	,2500,	0	,6300	,4200
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            2727	,570	,0	,2100,	2600
                                    },
                                    {  //$50-100/month
                                            3100	,690	,0,	4050	,2900
                                    },
                                    {  //$100-150/month
                                            3870	,1700,	0,	5000	,3700
                                    },
                                    {  //$150-200/month
                                            5050	,2550,	0	,5300	,3730
                                    },
                                    {  //>$200/month
                                            7800,	3100,	0	,6800	,4400
                                    }}},
                    {  //3-4 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            2170,	380,	0	,1800	,2000
                                    },
                                    {  //$50-100/month
                                            2719,	580	,0,	2800,	2300
                                    },
                                    {  //$100-150/month
                                            3200	,1200	,0,	3700,	3300
                                    },
                                    {  //$150-200/month
                                            4600,	1700	,0	,4400,	3500
                                    },
                                    {  //>$200/month
                                            8000,	2200	,0	,6600	,3900
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            2540	,500,	0,	2100,	2500
                                    },
                                    {  //$50-100/month
                                            3110,	780	,0,	3800,	2900
                                    },
                                    {  //$100-150/month
                                            3500,	1750	,0,	5000,	3600
                                    },
                                    {  //$150-200/month
                                            5200,	2700,	0	,5350,	3700
                                    },
                                    {  //>$200/month
                                            8200	,2700,	0	,7000,	4500
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            3010,	900	,0	,2450,	2900
                                    },
                                    {  //$50-100/month
                                            3300	,820,	0,	4650	,3300
                                    },
                                    {  //$100-150/month
                                            4100	,2000,	0	,5400,	4200
                                    },
                                    {  //$150-200/month
                                            5400,	2850,	0	,5600,	4200
                                    },
                                    {  //>$200/month
                                            8500,	3600,	0	,7400,	4900
                                    }}},
                    {  //5 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            2440,	450	,0,	2000,	2100
                                    },
                                    {  //$50-100/month
                                            2997,	600	,0,	3200,	2400
                                    },
                                    {  //$100-150/month
                                            3500	,1300,	0,	4300,	3500
                                    },
                                    {  //$150-200/month
                                            5000,	1900,	0	,5200	,3900
                                    },
                                    {  //>$200/month
                                            10000,	2600,	0	,7000,	4200
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            3010,	620,	0,	2300	,2700
                                    },
                                    {  //$50-100/month
                                            3320	,900	,0	,4200	,3300
                                    },
                                    {  //$100-150/month
                                            3900	,2100	,0,	5400,	4000
                                    },
                                    {  //$150-200/month
                                            5900,	3000,	0,	5650,	4100
                                    },
                                    {  //>$200/month
                                            10500,	3100,	0,	7400	,4700
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            3577	,980,	0	,2600,	3300
                                    },
                                    {  //$50-100/month
                                            3600	,980,	0	,5150	,3600
                                    },
                                    {  //$100-150/month
                                            4300	,2350,	0	,5700,	4600
                                    },
                                    {  //$150-200/month
                                            6200	,3150	,0	,6000,	4630
                                    },
                                    {  //>$200/month
                                            11100	,4000	,0	,7800,	5100
                                    }}}}};

}
