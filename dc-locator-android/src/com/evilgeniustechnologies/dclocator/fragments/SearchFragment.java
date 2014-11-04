package com.evilgeniustechnologies.dclocator.fragments;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.evilgeniustechnologies.dclocator.R;

public class SearchFragment extends BaseFragment implements OnClickListener {
    private static final String TAG = "EGT.SearchFragment";
    TextView tvName, tvEx, TvCountry, Tvstate, tvLabel, tvCity;
    EditText etName, etState, etCity, et_ex;
    LinearLayout rl_name, rl_country, rl_ex, rl_State, rl_city;
    Spinner etEx, etCoutry, state;
    ImageView search;
    String name, stateString, city;
    String countryString, expertiseString;
    boolean isSpinnerTouch = false;
    boolean isExpertiseTouched = false;
    boolean isCountryTouched = false;
    public static String[] State = {"...", "Alberta", "British Columbia",
            "Manitoba", "New Brunswick", "Newfoundland",
            "Northwest Territories", "Nova Scotia", "Nunavut", "Ontario",
            "Prince Edward Island", "Quebec", "Saskatchewan",
            "Yukon Territory", "Alabama", "Alaska", "Arizona", "Arkansas",
            "California", "Colorado", "Connecticut", "Delaware,Florida",
            "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa",
            "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland",
            "Massachusetts", "Michigan", "Minnesota", "Mississippi",
            "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire",
            "New Jersey", "New Mexico", "New York", "North Carolina",
            "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania",
            "Rhode Island", "South Carolina", "South Dakota", "Tennessee",
            "Texas", "Utah", "Vermont", "Virginia", "Washington",
            "West Virginia", "Wisconsin", "Wyoming", "Other..."};
    public static String[] Expertise = {"...", "Wordpress",
            "Mobile App Development", "Graphic Design", "Copywriting",
            "Content Writing", "Content Marketing", "Paid SEO", "Paid Traffic",
            "AdSense/Niche Monetization", "Audio/Video Editing",
            "Manufacturing", "Content Editing", "Business Coach",
            "Internet Marketing", "Education", "Health/Fitness",
            "Virtual Assistants/Outsourcing", "Web Design", "Web Development",
            "UI/UX", "eCommerce", "Other..."};
    public static String[] arrCountryName = {"...", "United States", "Canada", "Afghanistan", "Albania",
            "Algeria", "Andorra", "Angola", "Anguilla",
            "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia",
            "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas",
            "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium",
            "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia",
            "Bosnia and Herzegovina", "Botswana", "Brazil",
            "British Indian Ocean Territory", "British Virgin Islands",
            "Brunei", "Bulgaria", "Burkina Faso", "Burma (Myanmar)", "Burundi",
            "Cambodia", "Cameroon", "Cape Verde", "Cayman Islands",
            "Central African Republic", "Chad", "Chile", "China",
            "Christmas Island", "Cocos (Keeling) Islands", "Colombia",
            "Comoros", "Cook Islands", "Costa Rica", "Croatia", "Cuba",
            "Cyprus", "Czech Republic", "Democratic Republic of the Congo",
            "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador",
            "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia",
            "Ethiopia", "Falkland Islands", "Faroe Islands", "Fiji", "Finland",
            "France", "French Polynesia", "Gabon", "Gambia", "Gaza Strip",
            "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland",
            "Grenada", "Guam", "Guatemala", "Guinea", "Guinea-Bissau",
            "Guyana", "Haiti", "Holy See (Vatican City)", "Honduras",
            "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran",
            "Iraq", "Ireland", "Isle of Man", "Israel", "Italy", "Ivory Coast",
            "Jamaica", "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya",
            "Kiribati", "Kosovo", "Kuwait", "Kyrgyzstan", "Laos", "Latvia",
            "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein",
            "Lithuania", "Luxembourg", "Macau", "Macedonia", "Madagascar",
            "Malawi", "Malaysia", "Maldives", "Mali", "Malta",
            "Marshall Islands", "Mauritania", "Mauritius", "Mayotte", "Mexico",
            "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro",
            "Montserrat", "Morocco", "Mozambique", "Namibia", "Nauru", "Nepal",
            "Netherlands", "Netherlands Antilles", "New Caledonia",
            "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue",
            "Norfolk Island", "North Korea", "Northern Mariana Islands",
            "Norway", "Oman", "Pakistan", "Palau", "Panama",
            "Papua New Guinea", "Paraguay", "Peru", "Philippines",
            "Pitcairn Islands", "Poland", "Portugal", "Puerto Rico", "Qatar",
            "Republic of the Congo", "Romania", "Russia", "Rwanda",
            "Saint Barthelemy", "Saint Helena", "Saint Kitts and Nevis",
            "Saint Lucia", "Saint Martin", "Saint Pierre and Miquelon",
            "Saint Vincent and the Grenadines", "Samoa", "San Marino",
            "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia",
            "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia",
            "Solomon Islands", "Somalia", "South Africa", "South Korea",
            "Spain", "Sri Lanka", "Sudan", "Suriname", "Svalbard", "Swaziland",
            "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan",
            "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tokelau", "Tonga",
            "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
            "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine",
            "United Arab Emirates", "United Kingdom", "United States",
            "Uruguay", "US Virgin Islands", "Uzbekistan", "Vanuatu",
            "Venezuela", "Vietnam", "Wallis and Futuna", "West Bank",
            "Western Sahara", "Yemen", "Zambia", "Zimbabwe"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getActionBar().setDisplayShowCustomEnabled(true);
        View v = inflater.inflate(R.layout.navigation_search_fragment, null);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);
        getActivity().getActionBar().setCustomView(v);
        getActivity().getActionBar().setDisplayShowHomeEnabled(false);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);

        View view = inflater.inflate(R.layout.search_fragment, container, false);
        tvName = (TextView) view.findViewById(R.id.tv_name_search);
        tvEx = (TextView) view.findViewById(R.id.tv_expertise_search);
        TvCountry = (TextView) view.findViewById(R.id.tv_country_search);
        tvCity = (TextView) view.findViewById(R.id.tv_city_search);
        etCity = (EditText) view.findViewById(R.id.et_city);
        rl_city = (LinearLayout) view.findViewById(R.id.rl_city);
        Tvstate = (TextView) view.findViewById(R.id.tv_state_search);
        tvLabel = (TextView) view.findViewById(R.id.tv_search_label);
        etCoutry = (Spinner) view.findViewById(R.id.et_country);
        etEx = (Spinner) view.findViewById(R.id.et_expertise);
        etState = (EditText) view.findViewById(R.id.et_state);
        etState.setVisibility(View.GONE);
        state = (Spinner) view.findViewById(R.id.spinner_state);
        etName = (EditText) view.findViewById(R.id.et_name);
        rl_State = (LinearLayout) view.findViewById(R.id.rl_state);
        rl_ex = (LinearLayout) view.findViewById(R.id.rl_expertise);
        rl_country = (LinearLayout) view.findViewById(R.id.rl_country);
        rl_name = (LinearLayout) view.findViewById(R.id.rl_name);
        search = (ImageView) view.findViewById(R.id.btn_search);
        search.setOnClickListener(this);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this.getActivity(), android.R.layout.simple_spinner_item,
                arrCountryName);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(
                this.getActivity(), android.R.layout.simple_spinner_item,
                Expertise);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(
                this.getActivity(), android.R.layout.simple_spinner_item,
                State);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etEx.setAdapter(arrayAdapter2);
        etEx.setPrompt("select...");
        state.setAdapter(arrayAdapter3);
        state.setPrompt("select...");
        state.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isSpinnerTouch = true;
                return false;
            }
        });
        state.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> ParentView,
                                       View selectItemView, int position, long arg3) {
                if (position == 0) {
                    stateString = null;
                } else {
                    if (ParentView.getItemAtPosition(position).toString()
                            .equalsIgnoreCase("Other...")) {
                        state.setVisibility(View.GONE);
                        etState.setVisibility(View.VISIBLE);
                        stateString = etState.getText().toString().trim();
                    } else {
                        stateString = ParentView.getItemAtPosition(position).toString();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                stateString = null;
            }
        });
        etEx.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isExpertiseTouched = true;
                return false;
            }
        });
        etEx.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long arg3) {
                if (position == 0) {
                    expertiseString = null;
                } else {
                    if (parentView.getItemAtPosition(position).toString()
                            .equalsIgnoreCase("Other...")) {
                        etEx.setVisibility(View.GONE);
                        et_ex.setVisibility(View.VISIBLE);
                        expertiseString = et_ex.getText().toString().trim();

                    } else {
                        expertiseString = parentView.getItemAtPosition(position)
                                .toString();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                expertiseString = null;
            }
        });
        etCoutry.setAdapter(arrayAdapter);
        etCoutry.setPrompt("Select...");
        etCoutry.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isCountryTouched = true;
                return false;
            }
        });
        etCoutry.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                if (position == 0) {
                    countryString = null;
                } else {
                    countryString = parentView.getItemAtPosition(position).toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                countryString = null;
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                name = etName.getText().toString().trim();
                city = etCity.getText().toString().trim();
                ListFragmentResult result = new ListFragmentResult();
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("expertise", expertiseString);
                bundle.putString("country", countryString);
                bundle.putString("city", city);
                result.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, result);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }
}