package uk.co.deanwild.materialshowcaseviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class SequenceWithBoundConstraintExample extends AppCompatActivity implements View.OnClickListener {
	private Button mButtonOne;
	private Button mButtonReset;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sequence_bound_constraint_example);
		mButtonOne = (Button) findViewById(R.id.button_one);
		mButtonOne.setOnClickListener(this);

		mButtonReset = (Button) findViewById(R.id.button_reset);
		mButtonReset.setOnClickListener(this);

		presentShowcaseSequence(); // one second delay
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button_one) {
			presentShowcaseSequence();
		} else if (v.getId() == R.id.button_reset) {
			Toast.makeText(this, "Showcase reset", Toast.LENGTH_SHORT).show();
		}
	}

	private void presentShowcaseSequence() {
		ShowcaseConfig config = new ShowcaseConfig();
		config.setDelay(500); // half second between each showcase view

		MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);

		sequence.setConfig(config);

		sequence.addSequenceItem(
			new MaterialShowcaseView.Builder(this)
				.setTargetWithBoundsConstraint(mButtonOne)
				.setDismissOnTouch(true)
				.setDismissText("GOT IT")
				.setContentText("This is button one")
				.build()
		);

		sequence.start();
	}
}