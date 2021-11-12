package com.example.sqlite_project.Features.ShowStudentList;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlite_project.Database.DatabaseQueryClass;
import com.example.sqlite_project.Features.CreateStudent.Student;
import com.example.sqlite_project.Features.CreateStudent.StudentCreateDialogFragment;
import com.example.sqlite_project.Features.CreateStudent.StudentCreateListener;
import com.example.sqlite_project.R;
import com.example.sqlite_project.Util.Config;
import com.google.android.material.navigation.NavigationView;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity implements StudentCreateListener, NavigationView.OnNavigationItemSelectedListener {

    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);
    private final Handler mDrawerHandler = new Handler();
    private List<Student> studentList = new ArrayList<>();

    private TextView studentListEmptyTextView;
    private RecyclerView recyclerView;
    private StudentListRecyclerViewAdapter studentListRecyclerViewAdapter;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private int mSelectedId;
    private static final String SELECTED_ITEM_ID = "SELECTED_ITEM_ID";
    private int mPrevSelectedId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Logger.addLogAdapter(new AndroidLogAdapter());

        mNavigationView = findViewById(R.id.navigation_view);
        assert mNavigationView != null;
        mNavigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                super.onDrawerSlide(drawerView, 0);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        recyclerView = (RecyclerView) findViewById(R.id.studentRecyclerView);
        studentListEmptyTextView = (TextView) findViewById(R.id.emptyStudentListTextView);

        studentList.addAll(databaseQueryClass.getAllStudent());

        studentListRecyclerViewAdapter = new StudentListRecyclerViewAdapter(this, studentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(studentListRecyclerViewAdapter);

        viewVisibility();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStudentCreateDialog();
            }
        });


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        mSelectedId = mNavigationView.getMenu().getItem(prefs.getInt("default_view", 0)).getItemId();

        mSelectedId = savedInstanceState == null ? mSelectedId : savedInstanceState.getInt(SELECTED_ITEM_ID);

        mPrevSelectedId = mSelectedId;

        mNavigationView.getMenu().findItem(mSelectedId).setChecked(true);

        if (savedInstanceState == null) {
            mDrawerHandler.removeCallbacksAndMessages(null);

            mDrawerHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigate(mSelectedId);
                }
            }, 250);

            boolean openDrawer = prefs.getBoolean("open_drawer", false);

            if (openDrawer)
                mDrawerLayout.openDrawer(GravityCompat.END);
            else
                mDrawerLayout.closeDrawers();
        }


    }

    private void navigate(final int itemId) {
        final View elevation = findViewById(R.id.elevation);
        Fragment navFragment = null;
        switch (itemId) {
            case R.id.nav_1:
                mPrevSelectedId = itemId;
//                navFragment = new DashboardFragment();
//                Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.nav_2:
//                mPrevSelectedId = itemId;
//
//                navFragment = new RegisterFamilyFragment();
//                break;
            case R.id.nav_3:
//                logout();
                new android.app.AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                break;
        }

//        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(4));

//        if (navFragment != null) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            //transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
//            try {
//                transaction.replace(R.id.content_frame, navFragment, "asasa").addToBackStack(null).commit();
//
//                if (elevation != null) {
//                    params.topMargin = navFragment instanceof DashboardFragment ? dp(0) : 0;
//
//                    Animation a = new Animation() {
//                        @Override
//                        protected void applyTransformation(float interpolatedTime, Transformation t) {
//                            elevation.setLayoutParams(params);
//                        }
//                    };
//                    a.setDuration(150);
//                    elevation.startAnimation(a);
//                }
//            } catch (IllegalStateException ignored) {
//            }
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_delete){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure, You wanted to delete all students?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            boolean isAllDeleted = databaseQueryClass.deleteAllStudents();
                            if(isAllDeleted){
                                studentList.clear();
                                studentListRecyclerViewAdapter.notifyDataSetChanged();
                                viewVisibility();
                            }
                        }
                    });

            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void viewVisibility() {
        if(studentList.isEmpty())
            studentListEmptyTextView.setVisibility(View.VISIBLE);
        else
            studentListEmptyTextView.setVisibility(View.GONE);
    }

    private void openStudentCreateDialog() {
        StudentCreateDialogFragment studentCreateDialogFragment = StudentCreateDialogFragment.newInstance("Create Student", this);
        studentCreateDialogFragment.show(getSupportFragmentManager(), Config.CREATE_STUDENT);
    }

    @Override
    public void onStudentCreated(Student student) {
        studentList.add(student);
        studentListRecyclerViewAdapter.notifyDataSetChanged();
        viewVisibility();
        Logger.d(student.getName());
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        mSelectedId = menuItem.getItemId();
        mDrawerHandler.removeCallbacksAndMessages(null);
        mDrawerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(mSelectedId);
            }
        }, 250);
        mDrawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }  else{
//            Fragment DFragment = new DashboardFragment();
//
//            Bundle args = new Bundle();
//
//
//            if (DFragment != null) {
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//                DFragment.setArguments(args);
//                try {
//                    transaction.replace(R.id.content_frame, DFragment, "Main").commit();
//                } catch (IllegalStateException ignored) {
//                }
//            }

            new android.app.AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();



        }

    }


}
