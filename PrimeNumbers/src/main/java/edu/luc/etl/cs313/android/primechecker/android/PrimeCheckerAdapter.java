package edu.luc.etl.cs313.android.primechecker.android;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Simple adapter for prime checker app.
 */
public class PrimeCheckerAdapter extends Activity {

	private TextView input;

	private final int NUM = 3;

	private final boolean[] workers = new boolean[3];

	private final boolean[] remotes = new boolean[3];

	private final ProgressBar[] progressBars = new ProgressBar[NUM];

	private final TextView[] urls = new TextView[NUM];

	private final List<AsyncTask<Long, Integer, Boolean>> localTasks = new ArrayList<AsyncTask<Long, Integer, Boolean>>(NUM);

	private final List<PrimeCheckerRemoteTask> remoteTasks = new ArrayList<PrimeCheckerRemoteTask>(NUM);

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prime_checker_adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_prime_checker_adapter, menu);
        return true;
    }

    @Override
    public void onResume() {
    	super.onResume();
    	input = (TextView) findViewById(R.id.inputCandidate);
    	progressBars[0] = (ProgressBar) findViewById(R.id.progressBar1);
    	progressBars[1] = (ProgressBar) findViewById(R.id.progressBar2);
    	progressBars[2] = (ProgressBar) findViewById(R.id.progressBar3);
    	urls[0] = (TextView) findViewById(R.id.inputUrl1);
    	urls[1] = (TextView) findViewById(R.id.inputUrl2);
    	urls[2] = (TextView) findViewById(R.id.inputUrl3);
    }

    @Override
    public void onDestroy() {
    	super.onDestroy();
    	onCancel(input);
    }

    public void onCheck(final View view) {
    	onCancel(view);
    	for (int i = 0; i < NUM; i += 1) {
    		progressBars[i].setProgress(0);
    		if (workers[i])
    			if (remotes[i]) {
        			final PrimeCheckerRemoteTask t = new PrimeCheckerRemoteTask(progressBars[i], input);
        			remoteTasks.add(t);
    				t.start(urls[i].getText().toString() + input.getText().toString());
    			} else {
    				final PrimeCheckerTask t = new PrimeCheckerTask(progressBars[i], input);
    				localTasks.add(t);
                    // execute this task in the background on a thread pool
    				t.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Long.parseLong(input.getText().toString()));
    			}
    	}
    }

    public void onCancel(final View view) {
    	for (final AsyncTask<?, ?, ?> t: localTasks) { t.cancel(true); }
    	for (final PrimeCheckerRemoteTask t: remoteTasks) { t.cancel(); }
    	localTasks.clear();
		remoteTasks.clear();
    }

    public void onWorker(final int number, final boolean enabled) {
    	workers[number] = enabled;
    }

    public void onRemote(final int number, final boolean enabled) {
    	remotes[number] = enabled;
    }

    public void onWorker1(final View view) { onWorker(0, ((ToggleButton) view).isChecked()); }
    public void onWorker2(final View view) { onWorker(1, ((ToggleButton) view).isChecked()); }
    public void onWorker3(final View view) { onWorker(2, ((ToggleButton) view).isChecked()); }
    public void onRemote1(final View view) { onRemote(0, ((ToggleButton) view).isChecked()); }
    public void onRemote2(final View view) { onRemote(1, ((ToggleButton) view).isChecked()); }
    public void onRemote3(final View view) { onRemote(2, ((ToggleButton) view).isChecked()); }
}
