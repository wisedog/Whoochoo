/**
 * 
 */
package net.wisedog.android.whooing.utils;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import android.util.Log;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class WhooingCurrency {
    private static final String TIMEZONE[] = { "America/Anchorage", "America/Phoenix",
            "America/Los_Angeles", "America/Denver", "Pacific/Honolulu", "America/Indianapolis",
            "America/Chicago", "America/New_York", "America/Edmonton", "America/Vancouver",
            "America/Winnipeg", "America/St_Johns", "America/Yellowknife", "America/Halifax",
            "America/Rankin_Inlet", "America/Rainy_River", "America/Montreal", "America/Regina",
            "America/Whitehorse", "Australia/Canberra", "Australia/NSW", "Australia/North",
            "Australia/Queensland", "Australia/South", "Australia/Tasmania", "Australia/Victoria",
            "Australia/West", "US/Samoa", "Africa/Abidjan", "Africa/Accra", "Africa/Algiers",
            "Africa/Asmera", "Africa/Bamako", "Africa/Bangui", "Africa/Banjul", "Africa/Bissau",
            "Africa/Brazzaville", "Africa/Bujumbura", "Africa/Cairo", "Africa/Casablanca",
            "Africa/Conakry", "Africa/Dakar", "Africa/Djibouti", "Africa/Freetown",
            "Africa/Gaborone", "Africa/Harare", "Africa/Johannesburg", "Africa/Kampala",
            "Africa/Khartoum", "Africa/Kigali", "Africa/Lagos", "Africa/Libreville", "Africa/Lome",
            "Africa/Luanda", "Africa/Lusaka", "Africa/Malabo", "Africa/Maputo", "Africa/Maseru",
            "Africa/Mbabane", "Africa/Mogadishu", "Africa/Monrovia", "Africa/Nairobi",
            "Africa/Ndjamena", "Africa/Niamey", "Africa/Nouakchott", "Africa/Ouagadougou",
            "Africa/Sao_Tome", "Africa/Tripoli", "Africa/Tunis", "America/Anguilla",
            "America/Antigua", "America/Aruba", "America/Barbados", "America/Belize",
            "America/Bogota", "America/Caracas", "America/Cayman", "America/Costa_Rica",
            "America/Dominica", "America/El_Salvador", "America/Grenada", "Europe/Paris",
            "America/Guadeloupe", "America/Guatemala", "America/Guyana", "America/Havana",
            "America/Jamaica", "America/La_Paz", "America/Lima", "America/Managua",
            "America/Martinique", "America/Montevideo", "America/Montserrat", "America/Nassau",
            "America/Panama", "America/Paramaribo", "America/Puerto_Rico", "America/St_Kitts",
            "America/St_Lucia", "America/St_Vincent", "America/Tegucigalpa", "Asia/Aden",
            "Asia/Amman", "Asia/Ashgabat", "Asia/Baghdad", "Asia/Bahrain", "Asia/Baku",
            "Asia/Bangkok", "Asia/Beirut", "Asia/Bishkek", "Asia/Brunei", "Asia/Calcutta",
            "Asia/Choibalsan", "Asia/Colombo", "Asia/Dhaka", "Asia/Dubai", "Asia/Dushanbe",
            "Asia/Hong_Kong", "Asia/Istanbul", "Asia/Jerusalem", "Asia/Kabul", "Asia/Karachi",
            "Asia/Katmandu", "Asia/Kuwait", "Asia/Macao", "Asia/Manila", "Asia/Muscat",
            "Asia/Nicosia", "Asia/Pyongyang", "Asia/Qatar", "Asia/Rangoon", "Asia/Riyadh",
            "Asia/Seoul", "Asia/Singapore", "Asia/Taipei", "Asia/Tbilisi", "Asia/Thimphu",
            "Asia/Tokyo", "Asia/Vientiane", "Asia/Yerevan", "Atlantic/Bermuda",
            "Atlantic/Cape_Verde", "Atlantic/Faeroe", "Atlantic/Reykjavik", "Atlantic/South_Georg",
            "Atlantic/St_Helena", "Chile/Continental", "Europe/Amsterdam", "Europe/Andorra",
            "Europe/Athens", "Europe/Belgrade", "Europe/Berlin", "Europe/Bratislava",
            "Europe/Brussels", "Europe/Bucharest", "Europe/Budapest", "Europe/Copenhagen",
            "Europe/Dublin", "Europe/Gibraltar", "Europe/Helsinki", "Europe/Ljubljana",
            "Europe/London", "Europe/Luxembourg", "Europe/Malta", "Europe/Minsk", "Europe/Monaco",
            "Europe/Oslo", "Europe/Prague", "Europe/Riga", "Europe/Rome", "Europe/San_Marino",
            "Europe/Sarajevo", "Europe/Skopje", "Europe/Sofia", "Europe/Stockholm",
            "Europe/Tallinn", "Europe/Tirane", "Europe/Vaduz", "Europe/Vatican", "Europe/Vienna",
            "Europe/Vilnius", "Europe/Warsaw", "Europe/Zagreb", "Asia/Tehran",
            "Indian/Antananarivo", "Indian/Christmas", "Indian/Cocos", "Indian/Comoro",
            "Indian/Maldives", "Indian/Mauritius", "Indian/Mayotte", "Indian/Reunion",
            "Pacific/Fiji", "Pacific/Funafuti", "Pacific/Guam", "Pacific/Nauru", "Pacific/Niue",
            "Pacific/Norfolk", "Pacific/Palau", "Pacific/Pitcairn", "Pacific/Rarotonga",
            "Pacific/Samoa", "Pacific/Tarawa", "Pacific/Tongatapu", "Pacific/Wallis",
            "Africa/Dar_es_Salaam", "Asia/Phnom_Penh", "Africa/Lagos", "America/Santo_Doming",
            "Africa/Addis_Ababa", "Europe/Paris", "America/Port-au-Prin", "Europe/Zurich",
            "Africa/Porto-Novo", "Africa/El_Aaiun", "Atlantic/Stanley", "America/Cayenne",
            "Indian/Chagos", "Europe/Chisinau", "Pacific/Saipan", "Africa/Blantyre",
            "Africa/Windhoek", "Pacific/Noumea", "Pacific/Port_Moresby", "America/Miquelon",
            "Asia/Gaza", "America/Asuncion", "Pacific/Guadalcanal", "Indian/Mahe",
            "Arctic/Longyearbyen", "Asia/Damascus", "America/Grand_Turk", "Indian/Kerguelen",
            "Pacific/Fakaofo", "America/Port_of_Spai", "America/Tortola", "America/St_Thomas",
            "Pacific/Efate", "Europe/Belgrade", "Europe/Podgorica", "Europe/Mariehamn",
            "Europe/Guernsey", "Europe/Isle_of_Man", "Europe/Jersey", "America/St_Barthelem",
            "America/Marigot", "America/Argentina/Ca", "America/Argentina/Ri",
            "America/Argentina/Co", "America/Argentina/Tu", "America/Argentina/Ju",
            "America/Argentina/La", "America/Argentina/Me", "America/Argentina/Sa",
            "America/Argentina/Ri", "America/Argentina/Bu", "America/Argentina/Ca",
            "America/Argentina/Us", "America/Rio_Branco", "America/Sao_Paulo", "America/Manaus",
            "America/Bahia", "America/Fortaleza", "America/Campo_Grande", "America/Belem",
            "America/Cuiaba", "America/Recife", "America/Porto_Velho", "America/Boa_Vista",
            "America/Maceio", "America/Araguaina", "Africa/Kinshasa", "Africa/Lubumbashi",
            "Africa/Kinshasa", "Africa/Kinshasa", "Asia/Harbin", "Asia/Shanghai", "Asia/Chongqing",
            "Asia/Urumqi", "Pacific/Galapagos", "America/Guayaquil", "Africa/Ceuta",
            "Atlantic/Canary", "Europe/Madrid", "America/Thule", "America/Godthab",
            "Asia/Jayapura", "Asia/Jakarta", "Asia/Makassar", "Asia/Pontianak", "Asia/Aqtobe",
            "Asia/Qyzylorda", "Asia/Aqtau", "Asia/Oral", "Asia/Almaty", "America/Tijuana",
            "America/Merida", "America/Chihuahua", "America/Mazatlan", "America/Chihuahua",
            "America/Cancun", "America/Hermosillo", "America/Monterrey", "America/Mexico_City",
            "Asia/Kuala_Lumpur", "Asia/Kuching", "Pacific/Auckland", "Pacific/Chatham",
            "Europe/Lisbon", "Atlantic/Madeira", "Asia/Yekaterinburg", "Europe/Kaliningrad",
            "Asia/Kamchatka", "Asia/Novokuznetsk", "Asia/Anadyr", "Asia/Krasnoyarsk",
            "Asia/Magadan", "Asia/Omsk", "Europe/Samara", "Asia/Vladivostok", "Asia/Yakutsk",
            "Asia/Sakhalin", "Europe/Volgograd", "Asia/Novosibirsk", "Asia/Irkutsk",
            "Europe/Moscow", "Europe/Simferopol", "Europe/Kiev", "Europe/Uzhgorod",
            "Europe/Zaporozhye", "Asia/Samarkand", "Asia/Tashkent", "Asia/Dili",
            "Pacific/Marquesas", "America/Curacao" };
    
    private static final String COUNTRY[] = { "Andorra", "United Arab Emirates", "Afghanistan",
            "Antigua & Barbuda", "Anguilla", "Albania", "Armenia", "Netherlands Antilles",
            "Angola", "Antarctica", "Argentina", "American Samoa", "Austria", "Australia", "Aruba",
            "Azerbaijan", "Bosnia and Herzegovina", "Barbados", "Bangladesh", "Belgium",
            "Burkina Faso", "Bulgaria", "Bahrain", "Burundi", "Benin", "Bermuda",
            "Brunei Darussalam", "Bolivia", "Brazil", "Bahama", "Bhutan", "Bouvet Island",
            "Botswana", "Belarus", "Belize", "Canada", "Cocos (Keeling) Islands",
            "Central African Republic", "Congo", "Switzerland", "Côte D'ivoire (Ivory Coast)",
            "Cook Iislands", "Chile", "Cameroon", "China", "Colombia", "Costa Rica", "Cuba",
            "Cape Verde", "Christmas Island", "Cyprus", "Czech Republic", "Germany", "Djibouti",
            "Denmark", "Dominica", "Dominican Republic", "Algeria", "Ecuador", "Estonia", "Egypt",
            "Western Sahara", "Eritrea", "Spain", "Ethiopia", "Finland", "Fiji",
            "Falkland Islands (Malvinas)", "Micronesia", "Faroe Islands", "France", "France",
            "Gabon", "United Kingdom (Great Britain)", "Grenada", "Georgia", "French Guiana",
            "Ghana", "Gibraltar", "Greenland", "Gambia", "Guinea", "Guadeloupe",
            "Equatorial Guinea", "Greece", "South Georgia and the South Sandwich Islands",
            "Guatemala", "Guam", "Guinea-Bissau", "Guyana", "Hong Kong",
            "Heard & McDonald Islands", "Honduras", "Croatia", "Haiti", "Hungary", "Indonesia",
            "Ireland", "Israel", "India", "British Indian Ocean Territory", "Iraq",
            "Islamic Republic of Iran", "Iceland", "Italy", "Jamaica", "Jordan", "Japan", "Kenya",
            "Kyrgyzstan", "Cambodia", "Kiribati", "Comoros", "St. Kitts and Nevis",
            "Democratic People's Republic of Korea(북한)", "Republic of Korea(대한민국)", "Kuwait",
            "Cayman Islands", "Kazakhstan", "Lao People's Democratic Republic", "Lebanon",
            "Saint Lucia", "Liechtenstein", "Sri Lanka", "Liberia", "Lesotho", "Lithuania",
            "Luxembourg", "Latvia", "Libyan Arab Jamahiriya", "Morocco", "Monaco", "Moldova",
            "Madagascar", "Marshall Islands", "Mali", "Mongolia", "Myanmar", "Macau",
            "Northern Mariana Islands", "Martinique", "Mauritania", "Monserrat", "Malta",
            "Mauritius", "Maldives", "Malawi", "Mexico", "Malaysia", "Mozambique", "Namibia",
            "New Caledonia", "Niger", "Norfolk Island", "Nigeria", "Nicaragua", "Netherlands",
            "Norway", "Nepal", "Nauru", "Niue", "New Zealand", "Oman", "Panama", "Peru",
            "French Polynesia", "Papua New Guinea", "Philippines", "Pakistan", "Poland",
            "St. Pierre & Miquelon", "Pitcairn", "Puerto Rico", "Portugal", "Palau", "Paraguay",
            "Qatar", "Réunion", "Romania", "Russian Federation", "Rwanda", "Saudi Arabia",
            "Solomon Islands", "Seychelles", "Sudan", "Sweden", "Singapore", "St. Helena",
            "Slovenia", "Svalbard & Jan Mayen Islands", "Slovakia", "Sierra Leone", "San Marino",
            "Senegal", "Somalia", "Suriname", "Sao Tome & Principe", "El Salvador",
            "Syrian Arab Republic", "Swaziland", "Turks & Caicos Islands", "Chad",
            "French Southern Territories", "Togo", "Thailand", "Tajikistan", "Tokelau",
            "Turkmenistan", "Tunisia", "Tonga", "East Timor", "Turkey", "Trinidad & Tobago",
            "Tuvalu", "Taiwan", "Tanzania", "Ukraine", "Uganda",
            "United States Minor Outlying Islands", "United States of America", "Uruguay",
            "Uzbekistan", "Vatican City State (Holy See)", "St. Vincent & the Grenadines",
            "Venezuela", "British Virgin Islands", "United States Virgin Islands", "Viet Nam",
            "Vanuatu", "Wallis & Futuna Islands", "Samoa", "Yemen", "Mayotte", "Yugoslavia",
            "South Africa", "Zambia", "Zaire", "Zimbabwe", "Unknown or unspecified country" };
    private static final String COUNTRY_CODE[] = { "AD", "AE", "AF", "AG", "AI", "AL", "AM", "AN",
            "AO", "AQ", "AR", "AS", "AT", "AU", "AW", "AZ", "BA", "BB", "BD", "BE", "BF", "BG",
            "BH", "BI", "BJ", "BM", "BN", "BO", "BR", "BS", "BT", "BV", "BW", "BY", "BZ", "CA",
            "CC", "CF", "CG", "CH", "CI", "CK", "CL", "CM", "CN", "CO", "CR", "CU", "CV", "CX",
            "CY", "CZ", "DE", "DJ", "DK", "DM", "DO", "DZ", "EC", "EE", "EG", "EH", "ER", "ES",
            "ET", "FI", "FJ", "FK", "FM", "FO", "FR", "FX", "GA", "GB", "GD", "GE", "GF", "GH",
            "GI", "GL", "GM", "GN", "GP", "GQ", "GR", "GS", "GT", "GU", "GW", "GY", "HK", "HM",
            "HN", "HR", "HT", "HU", "ID", "IE", "IL", "IN", "IO", "IQ", "IR", "IS", "IT", "JM",
            "JO", "JP", "KE", "KG", "KH", "KI", "KM", "KN", "KP", "KR", "KW", "KY", "KZ", "LA",
            "LB", "LC", "LI", "LK", "LR", "LS", "LT", "LU", "LV", "LY", "MA", "MC", "MD", "MG",
            "MH", "ML", "MN", "MM", "MO", "MP", "MQ", "MR", "MS", "MT", "MU", "MV", "MW", "MX",
            "MY", "MZ", "NA", "NC", "NE", "NF", "NG", "NI", "NL", "NO", "NP", "NR", "NU", "NZ",
            "OM", "PA", "PE", "PF", "PG", "PH", "PK", "PL", "PM", "PN", "PR", "PT", "PW", "PY",
            "QA", "RE", "RO", "RU", "RW", "SA", "SB", "SC", "SD", "SE", "SG", "SH", "SI", "SJ",
            "SK", "SL", "SM", "SN", "SO", "SR", "ST", "SV", "SY", "SZ", "TC", "TD", "TF", "TG",
            "TH", "TJ", "TK", "TM", "TN", "TO", "TP", "TR", "TT", "TV", "TW", "TZ", "UA", "UG",
            "UM", "US", "UY", "UZ", "VA", "VC", "VE", "VG", "VI", "VN", "VU", "WF", "WS", "YE",
            "YT", "YU", "ZA", "ZM", "ZR", "ZW", "ZZ" };
    private static final String CURRENCY[] = {};
    private static final int CURRENCY_E[] = {};
    
    private static String mCountryCode = null; 
    
    public static String getFormattedValue(double d){
        NumberFormat format = NumberFormat.getInstance();
        
        Currency currency = Currency.getInstance("KMF");
        format.setMaximumFractionDigits(currency.getDefaultFractionDigits());
        format.setCurrency(currency);
        Log.i("wisedog", "format1 : " + format.format(1234.23434));
        
        currency = Currency.getInstance("BIF");
        format.setMaximumFractionDigits(currency.getDefaultFractionDigits());
        format.setCurrency(currency);
        Log.i("wisedog", "format2 : " + format.format(1234.23434));
        return null;
    }
}
