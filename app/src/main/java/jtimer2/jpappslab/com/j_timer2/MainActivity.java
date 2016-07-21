package jtimer2.jpappslab.com.j_timer2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {


    private static final int PREFERENCES_ID = Menu.FIRST;

    private static final int ABOUT_ID = Menu.FIRST + 1;

    private static final int DIALOG_ABOUT = 0;

    private boolean runBack;

    private SharedPreferences prefs;

    public static long initialSettime;

    public static long currentTimeStopped;

    private boolean countUp = false;


    Button startCrono;
    Button pauseCrono;
    Chronometer crono;
    long time;
    Thread progress;
    private String sound = "2131165184";
    private final long startTime = 30 * 1000;
    private final long interval = 1 * 1000;
    private CountDownTimer countDownTimer;
    private CountUpTimer countUpTimer;
    public TextView textSecond;
    public TextView textMinute;
    public TextView textHour;
    public TextView timeSet;
    public EditText getHours;
    public EditText getMinutes;
    public EditText getSeconds;
    private boolean timerHasStarted = false;
    private ProgressBar progressBar;
    private int progressStatus = 100;
    private MediaPlayer mysound;
    private static boolean stopProgressBar = false;
    int sec = 0;
    int min = 0;
    int hour = 0;
    private long triggerTime = 0;
    int initialSeconds = 0;
    int initialMinutes = 0;
    int initialHours = 0;
    boolean restart = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    // public static boolean runbackground = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countUp = false;


        Typeface myTypeface = Typeface.createFromAsset(this.getAssets(),
                "digital.ttf");
        textHour = (TextView) this.findViewById(R.id.textView);
        textHour.setTypeface(myTypeface);
        textMinute = (TextView) this.findViewById(R.id.textView5);
        textMinute.setTypeface(myTypeface);
        textSecond = (TextView) this.findViewById(R.id.textView4);
        textSecond.setTypeface(myTypeface);
        timeSet = (TextView) this.findViewById(R.id.timeSet);
        timeSet.setTypeface(myTypeface);


        startCrono = (Button) this.findViewById(R.id.button);
        startCrono.setOnClickListener(this);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        getHours = (EditText) this.findViewById(R.id.editHours);
        getHours.setTypeface(myTypeface);
        getHours.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                restart = false;

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        getMinutes = (EditText) this.findViewById(R.id.editMinutes);
        getMinutes.setTypeface(myTypeface);
        getMinutes.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                restart = false;

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


        getSeconds = (EditText) this.findViewById(R.id.editSeconds);
        getSeconds.setTypeface(myTypeface);
        getSeconds.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                restart = false;

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        if (countUp) {
            countUpTimer = new CountUpTimer(startTime, interval);
        } else {
            countDownTimer = new CountDownTimer(startTime, interval);
        }


        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // register preference change listener
        prefs.registerOnSharedPreferenceChangeListener(this);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Preferences menu item
        MenuItem PrefMenuItem = menu.add(0, PREFERENCES_ID, 0, R.string.menu_preference);
        PrefMenuItem.setIcon(android.R.drawable.ic_menu_preferences);

        // About menu item
        MenuItem AboutMenuItem = menu.add(0, ABOUT_ID, 0, R.string.menu_about);
        AboutMenuItem.setIcon(android.R.drawable.ic_menu_info_details);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case PREFERENCES_ID:
                Intent prefIntent = new Intent(this, Preferences.class);
                startActivity(prefIntent);
                return true;
            case ABOUT_ID:
                showDialog(DIALOG_ABOUT);
                return true;
        }
        return true;
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_ABOUT:


                LayoutInflater factory = getLayoutInflater();
                final View aboutView = factory.inflate(R.layout.about, null);

                return new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("J-Timer")
                        .setView(aboutView)
                        .setPositiveButton(
                                android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                /* User clicked OK so do some stuff */
                                    }
                                })
                        .create();
        }
        return null;
    }


    public void onClick(View arg0) {

        int time = 0;

        //Validation

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage("Look at this dialog!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alertDialog = builder.create();

        // Setting Dialog Title
        alertDialog.setTitle("Alert");

        boolean passedValidation = true;

        try {
            sec = Integer.parseInt(getSeconds.getText().toString().trim());
            if (getMinutes.getText().toString().trim().equals("00")) {
                min = 0;
            } else {
                min = Integer.parseInt(getMinutes.getText().toString());
            }
            if (getHours.getText().toString().trim().equals("00")) {
                hour = 0;
            } else {
                hour = Integer.parseInt(getHours.getText().toString().trim());
            }
        } catch (Exception e) {
            // Setting Dialog Message
            alertDialog.setMessage("Count down HH MM SS must have numbers!");
            passedValidation = false;
        }

        if (sec > 60) {
            alertDialog.setMessage("Count down seconds should be no more than 60!");
            passedValidation = false;
        }

        if (min > 60) {
            alertDialog.setMessage("Count down minutes should be no more than 60!");
            passedValidation = false;
        }

        if (hour > 12) {
            alertDialog.setMessage("Count down hours should be no more than 12!");
            passedValidation = false;
        }

        if (sec == 0 && min == 0 && hour == 0 && mysound == null) {
            alertDialog.setMessage("Set at least one number above zero!");
            passedValidation = false;
        }

        if (!passedValidation) {
            alertDialog.show();
            return;
        }


        if (!timerHasStarted) {


            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String s = preferences.getString(getString(R.string.pref_alarm_sound), null);
            if (s != null) {
                sound = s;
            }

            String countType = preferences.getString(getString(R.string.pref_count), null);
            if (countType != null) {
                if (countType.equals("1")) {
                    countUp = true;
                } else {
                    countUp = false;
                }
            }


            mysound = MediaPlayer.create(MainActivity.this, Integer.parseInt(sound));
            mysound.setLooping(true);
            if (mysound.isPlaying()) {
                mysound.release();
            }
            initialSeconds = sec;
            initialMinutes = min;
            initialHours = hour;

            time = sec;
            time = time + min * 60;
            time = time + hour * 3600;
            triggerTime = time * 1000;
            if (!restart) {
                initialSettime = 0;
                timeSet.setText("Time Set: " + getHours.getText().toString() + " hrs " + getMinutes.getText().toString() + " mins " + getSeconds.getText().toString() + " secs");
                progressBar.setMax(time * 1000);
                progressStatus = time * 1000;
                progressBar.setProgress(progressStatus);
            }
            if (countUp) {
                countUpTimer = new CountUpTimer(time * 1000, interval);
                countUpTimer.Start();
            } else {
                countDownTimer = new CountDownTimer(time * 1000, interval);
                countDownTimer.Start();
            }
            timerHasStarted = true;
            startCrono.setText("PAUSE COUNT");
            stopProgressBar = false;

              /* progress = new Thread(new Runnable() {
                   public void run() {
                       while (stopProgressBar == false && progressStatus > 0) {
                               progressStatus -= 1000;
                           // Update the progress bar and display the
                           //current value in the text view
                           handler.post(new Runnable() {
                               public void run() {
                                   progressBar.setProgress(progressStatus);
                               }
                           });
                           try {
                               Thread.sleep(1000);
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }
                       }
                   }
               });
               progress.start();*/


        } else {
            if (countUp) {
                countUpTimer.cancel();
            } else {
                countDownTimer.cancel();
            }
            stopProgressBar = true;
            restart = true;
            mysound.release();
            mysound = null;
            timerHasStarted = false;
            startCrono.setText("START COUNT");
        }


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        sound = sharedPreferences.getString(getString(R.string.pref_alarm_sound), null);
        if (countUp) {
            countUpTimer.cancel();
        } else {
            countDownTimer.cancel();
        }
        progressBar.setProgress(0);
        progressStatus = 0;
        stopProgressBar = true;
        restart = true;
        if (mysound != null) {
            mysound.release();
        }
        mysound = null;
        timerHasStarted = false;
        startCrono.setText("START COUNT");
        getSeconds.setText("00");

        getMinutes.setText("00");

        getHours.setText("00");
    }


    public class CountDownTimer {
        private long millisInFuture;
        private long countDownInterval;
        private int hoursLeft;
        private int minsLeft;
        private int secsLeft;
        final Handler handler = new Handler();
        private boolean cancelled = false;


        public CountDownTimer(long pMillisInFuture, long pCountDownInterval) {
            this.millisInFuture = pMillisInFuture;
            this.countDownInterval = pCountDownInterval;
            if (!restart)
                initialSettime = pMillisInFuture;
        }

        public void Start() {

            final Runnable counter = new Runnable() {

                public void run() {

                    if (millisInFuture <= 0) {

                    } else {
                        long sec = millisInFuture / 1000;

                        millisInFuture -= countDownInterval;
                        handler.postDelayed(this, countDownInterval);
                        progressStatus -= 1000;
                        progressBar.setProgress(progressStatus);
                    }

                    refreshTimerVals();
                }
            };

            handler.postDelayed(counter, countDownInterval);

        }

        /**
         * Cancel the countdown.
         */
        public synchronized final void cancel() {
            cancelled = true;
            handler.removeCallbacksAndMessages(null);
        }


        public void refreshTimerVals() {


            // If timer expired
            if (millisInFuture < 0) {
                hoursLeft = 0;
                minsLeft = 0;
                secsLeft = 0;
                return;
            }

            // Otherwise calculate values
            long runTimeSecs = millisInFuture / 1000;
            // Log.v("Remaining time = " + remainingTime);
            // milliSecsLeft = (int) (remainingTime % 1000);
            secsLeft = (int) (runTimeSecs % 60);
            minsLeft = (int) ((runTimeSecs % 3600) / 60);
            hoursLeft = (int) (runTimeSecs / 3600);
            //String Hours = String.format("%02d", hours);
            getHours.setText("" + (hoursLeft < 10 ? "0" : "") + hoursLeft);
            //String Minutes = String.format("%02d", minutes);
            getMinutes.setText("" + (minsLeft < 10 ? "0" : "") + minsLeft);
            // String Seconds = String.format("%02d", seconds);
            getSeconds.setText("" + (secsLeft < 10 ? "0" : "") + secsLeft);
            if (hoursLeft == 0 && minsLeft == 0 && secsLeft == 0) {
                mysound.start();
                startCrono.setText("STOP SOUND");
            }

        }
    }

    public class CountUpTimer {
        private long millisInFuture;
        private long currentElapsedTime;
        private long countDownInterval;
        private int hoursLeft;
        private int minsLeft;
        private int secsLeft;
        final Handler handler = new Handler();
        private boolean cancelled = false;


        public CountUpTimer(long pMillisInFuture, long pCountDownInterval) {
            this.millisInFuture = pMillisInFuture;
            this.countDownInterval = pCountDownInterval;
            if (!restart)
                initialSettime = pMillisInFuture;
        }

        public void Start() {
            if (!restart) {
                progressBar.setProgress(0);
                progressStatus = 0;
                currentElapsedTime = 0;
            } else {
                millisInFuture = initialSettime;
                currentElapsedTime = currentTimeStopped;
            }
            final Runnable counter = new Runnable() {

                public void run() {

                    if (millisInFuture <= currentElapsedTime) {

                    } else {
                        //long sec = millisInFuture/1000;

                        currentElapsedTime += countDownInterval;
                        handler.postDelayed(this, countDownInterval);
                        progressStatus += 1000;
                        progressBar.setProgress(progressStatus);
                    }

                    refreshTimerVals();
                }
            };

            handler.postDelayed(counter, countDownInterval);

        }

        /**
         * Cancel the countdown.
         */
        public synchronized final void cancel() {
            cancelled = true;
            handler.removeCallbacksAndMessages(null);
        }


        public void refreshTimerVals() {
            currentTimeStopped = currentElapsedTime;

            // Otherwise calculate values
            long runTimeSecs = currentElapsedTime / 1000;
            // Log.v("Remaining time = " + remainingTime);
            // milliSecsLeft = (int) (remainingTime % 1000);
            secsLeft = (int) (runTimeSecs % 60);
            minsLeft = (int) ((runTimeSecs % 3600) / 60);
            hoursLeft = (int) (runTimeSecs / 3600);
            //String Hours = String.format("%02d", hours);
            getHours.setText("" + (hoursLeft < 10 ? "0" : "") + hoursLeft);
            //String Minutes = String.format("%02d", minutes);
            getMinutes.setText("" + (minsLeft < 10 ? "0" : "") + minsLeft);
            // String Seconds = String.format("%02d", seconds);
            getSeconds.setText("" + (secsLeft < 10 ? "0" : "") + secsLeft);
            if (millisInFuture <= currentElapsedTime) {
                mysound.start();
                startCrono.setText("STOP SOUND");
                return;
            }


        }
    }

    public void onBackPressed() {
        Process.killProcess(Process.myPid());
    }


}