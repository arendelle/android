package org.arendelle.android;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Editor extends ActionBarActivity implements OnItemClickListener, OnClickListener {

    // left drawer
    private DrawerLayout drawerLayout;
    private ListView leftDrawerListView;
    private ActionBarDrawerToggle actionBarLeftDrawerToggle;
    private ImageButton leftDrawerButtonAdd;
    private ImageButton leftDrawerButtonSettings;
    private View leftDrawerLastSelection;
    private int leftDrawerInitialSelection;
	
	// gui objects
	private EditText textCode;
    private FloatingActionButton fabuttonRun;
	private Button keyLoopOpen,
		keyGrammarDivider, 
		keyLoopClose, 
		keySpaceOpen, 
		keySpaceClose, 
		keyConditionOpen, 
		keyConditionClose, 
		keySpaceSign, 
		keySourceSign, 
		keyTab,
		keyDivideSign, 
		keyEqualSign, 
		keyMinusSign, 
		keyPlusSign, 
		keyMultiplySign, 
		keyFunctionSign, 
		keyStoredSpaceSign, 
		keyKeySign, 
		keyPowSign,
		keyModuloSign, 
		keyFunctionHeaderOpen, 
		keyFunctionHeaderClose, 
		keyTitleSign, 
		keyStringSign;
	
	/** project folder */
	private File projectFolder;
	
	/** config file */
	private File configFile;
	
	/** main function */
	private File mainFunction;
	
	/** current function */
	private File currentFunction;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);

        // set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.editor_toolbar);
        setSupportActionBar(toolbar);
		
		// get gui objects
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		leftDrawerListView = (ListView) findViewById(R.id.left_drawer);
		textCode = (EditText) findViewById(R.id.text_code);
        fabuttonRun = (FloatingActionButton) findViewById(R.id.fabutton_run);

		keyLoopOpen = (Button) findViewById(R.id.key_loop_open);
		keyGrammarDivider = (Button) findViewById(R.id.key_grammar_divider);
		keyLoopClose = (Button) findViewById(R.id.key_loop_close);
		keySpaceOpen = (Button) findViewById(R.id.key_space_open);
		keySpaceClose = (Button) findViewById(R.id.key_space_close);
		keyConditionOpen = (Button) findViewById(R.id.key_condition_open);
		keyConditionClose = (Button) findViewById(R.id.key_condition_close);
		keySpaceSign = (Button) findViewById(R.id.key_space_sign);
		keySourceSign = (Button) findViewById(R.id.key_source_sign);
		keyTab = (Button) findViewById(R.id.key_tab);
		keyDivideSign = (Button) findViewById(R.id.key_divide_sign);
		keyEqualSign = (Button) findViewById(R.id.key_equal_sign);
		keyMinusSign = (Button) findViewById(R.id.key_minus_sign);
		keyPlusSign = (Button) findViewById(R.id.key_plus_sign);
		keyMultiplySign = (Button) findViewById(R.id.key_multiply_sign);
		keyFunctionSign = (Button) findViewById(R.id.key_function_sign);
		keyStoredSpaceSign = (Button) findViewById(R.id.key_stored_space_sign);
		keyKeySign = (Button) findViewById(R.id.key_key_sign);
		keyPowSign = (Button) findViewById(R.id.key_pow_sign);
		keyModuloSign = (Button) findViewById(R.id.key_modulo_sign);
		keyFunctionHeaderOpen = (Button) findViewById(R.id.key_function_header_open);
		keyFunctionHeaderClose = (Button) findViewById(R.id.key_function_header_close);
		keyTitleSign = (Button) findViewById(R.id.key_title_sign);
		keyStringSign = (Button) findViewById(R.id.key_string_sign);
		
		// activate left drawer toggle in action bar
		actionBarLeftDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                // highlight current function if needed
                if (leftDrawerLastSelection == null) {
                    leftDrawerLastSelection = leftDrawerListView.getChildAt(leftDrawerInitialSelection);
                    ((TextView) leftDrawerLastSelection).setTypeface(null, Typeface.BOLD);
                    ((TextView) leftDrawerLastSelection).setTextColor(getResources().getColor(android.R.color.white));
                }

            }
        };
		drawerLayout.setDrawerListener(actionBarLeftDrawerToggle);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		// setup font type of code area
		textCode.setTypeface(Typeface.MONOSPACE);
		
		// setup drawer list
		leftDrawerListView.setOnItemClickListener(this);

        // setup floating action button
        fabuttonRun.setOnClickListener(this);
		
		// setup keys
		keyLoopOpen.setOnClickListener(this);
		keyGrammarDivider.setOnClickListener(this);
		keyLoopClose.setOnClickListener(this);
		keySpaceOpen.setOnClickListener(this);
		keySpaceClose.setOnClickListener(this);
		keyConditionOpen.setOnClickListener(this);
		keyConditionClose.setOnClickListener(this);
		keySpaceSign.setOnClickListener(this);
		keySourceSign.setOnClickListener(this);
		keyTab.setOnClickListener(this);
		keyDivideSign.setOnClickListener(this);
		keyEqualSign.setOnClickListener(this);
		keyMinusSign.setOnClickListener(this);
		keyPlusSign.setOnClickListener(this);
		keyMultiplySign.setOnClickListener(this);
		keyFunctionSign.setOnClickListener(this);
		keyStoredSpaceSign.setOnClickListener(this);
		keyKeySign.setOnClickListener(this);
		keyPowSign.setOnClickListener(this);
		keyModuloSign.setOnClickListener(this);
		keyFunctionHeaderOpen.setOnClickListener(this);
		keyFunctionHeaderClose.setOnClickListener(this);
		keyTitleSign.setOnClickListener(this);
		keyStringSign.setOnClickListener(this);
		
		// get project folder
		projectFolder = new File(getIntent().getExtras().getString("projectFolder"));
		
		// get config file
		configFile = new File(projectFolder, "project.config");
		
		// get main function
		try {
			mainFunction = new File(projectFolder, Files.parseConfigFile(configFile).get("mainFunction"));
		} catch (Exception e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			finish();
		}
		
		// get current function
		try {
			currentFunction = new File(projectFolder, Files.parseConfigFile(configFile).get("currentFunction"));
		} catch (Exception e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			finish();
		}
		setTitle(currentFunction.getName());
		
		// read code
		try {
			textCode.setText(Files.read(currentFunction));
		} catch (Exception e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			finish();
		}

        // setup header in left drawer
        View header = getLayoutInflater().inflate(R.layout.drawer_listview_header, null);
        ((ImageView) header.findViewById(R.id.drawer_listview_header_preview)).setImageBitmap(BitmapFactory.decodeFile(new File(projectFolder, "preview.png").getAbsolutePath()));
        ((TextView) header.findViewById(R.id.drawer_listview_header_text)).setText(projectFolder.getName());
        leftDrawerListView.addHeaderView(header, null, false);

        // get buttons in left drawer
        leftDrawerButtonAdd = (ImageButton) header.findViewById(R.id.drawer_listview_header_button_add);
        leftDrawerButtonSettings = (ImageButton) header.findViewById(R.id.drawer_listview_header_button_settings);

        // setup buttons in left drawer
        leftDrawerButtonAdd.setOnClickListener(this);
        leftDrawerButtonSettings.setOnClickListener(this);
		
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		actionBarLeftDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

        // open or close left drawer
		if (actionBarLeftDrawerToggle.onOptionsItemSelected(item)) return true;

		switch (item.getItemId()) {
			
		default:
			return super.onOptionsItemSelected(item);
			
		}
		
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		actionBarLeftDrawerToggle.syncState();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // change colors in list
        if (leftDrawerLastSelection != null) {
            ((TextView) leftDrawerLastSelection).setTypeface(null, Typeface.NORMAL);
            ((TextView) leftDrawerLastSelection).setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
        leftDrawerLastSelection = view;
        ((TextView) view).setTypeface(null, Typeface.BOLD);
        ((TextView) view).setTextColor(getResources().getColor(android.R.color.white));

		// load selected file (/function)
		currentFunction = new File(projectFolder, ((TextView) view).getText().toString() + ".arendelle");
		setTitle(currentFunction.getName());
		
		// read code
		try {
			textCode.setText(Files.read(currentFunction));
		} catch (Exception e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			finish();
		}
		
		// close drawer
		drawerLayout.closeDrawer(leftDrawerListView);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.menu_editor, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onClick(View v) {

        // evaluate
        if (v == fabuttonRun) {

            try {

                // close keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textCode.getWindowToken(), 0);

                // save code
                Files.write(currentFunction, textCode.getText().toString());

                // execute code
                Intent intent = new Intent(this, Screen.class);
                intent.putExtra("code", Files.read(mainFunction));
                intent.putExtra("projectFolder", projectFolder.getAbsolutePath());
                startActivity(intent);

                drawerLayout.closeDrawer(leftDrawerListView);
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

        // add function
        else if(v == leftDrawerButtonAdd) {

            Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();

        }

        // open project settings
        else if(v == leftDrawerButtonSettings) {

            Toast.makeText(this, "Project settings", Toast.LENGTH_SHORT).show();

        }

        // add pressed key
        else {

            // get text of pressed key
            String keyText = "";
            if (v == keyTab) {
                keyText = "   ";
            } else {
                keyText = ((Button) v).getText().toString();
            }

            // insert text into code
            int start = Math.max(textCode.getSelectionStart(), 0);
            int end = Math.max(textCode.getSelectionEnd(), 0);
            textCode.getText().replace(Math.min(start, end), Math.max(start, end), keyText, 0, keyText.length());

        }
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		// save code
		try {
			Files.write(currentFunction, textCode.getText().toString());
		} catch (Exception e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();;
		}
		
		// save current function
		try {
			HashMap<String, String> properties = Files.parseConfigFile(configFile);
			properties.put("currentFunction", currentFunction.getName());
			Files.createConfigFile(configFile, properties);
		} catch (Exception e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();;
		}
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// get files in project
		ArrayList<String> filesList = new ArrayList<String>();
		File file[] = projectFolder.listFiles();
		for (File f : file) {
			if (f.getName().endsWith(".arendelle")) {
				filesList.add(f.getName().split(".arendelle")[0]);
			}
		}
		Collections.sort(filesList);
		
		// display files in the left drawer
		leftDrawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_listview_item, filesList));

        // store index (+1 because of the header) of current function for later
        leftDrawerInitialSelection = filesList.indexOf(currentFunction.getName().split(".arendelle")[0]) + 1;

	}

}