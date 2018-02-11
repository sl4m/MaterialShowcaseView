package uk.co.deanwild.materialshowcaseviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class SequenceWithListenersExample extends AppCompatActivity implements
	View.OnClickListener,
	MaterialShowcaseSequence.OnSequenceItemDismissedByOverlayListener,
	MaterialShowcaseSequence.OnSequenceItemDismissedByButtonListener {
	private Button mButtonOne;
	private Button mButtonTwo;
	private Button mButtonThree;
	private Button mButtonReset;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sequence_example);
		mButtonOne = (Button) findViewById(R.id.btn_one);
		mButtonOne.setOnClickListener(this);

		mButtonTwo = (Button) findViewById(R.id.btn_two);
		mButtonTwo.setOnClickListener(this);

		mButtonThree = (Button) findViewById(R.id.btn_three);
		mButtonThree.setOnClickListener(this);

		mButtonReset = (Button) findViewById(R.id.btn_reset);
		mButtonReset.setOnClickListener(this);

		presentShowcaseSequence(); // one second delay
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_one || v.getId() == R.id.btn_two || v.getId() == R.id.btn_three) {
			presentShowcaseSequence();
		} else if (v.getId() == R.id.btn_reset) {
			Toast.makeText(this, "Showcase reset", Toast.LENGTH_SHORT).show();
		}
	}

	@Override public void onDismissedByButtonTouch(MaterialShowcaseView itemView, int position) {
		Toast.makeText(this, "Item #" + position + " was dismissed by button", Toast.LENGTH_SHORT).show();
	}

	@Override public void onDismissedByOverlayTouch(MaterialShowcaseView itemView, int position) {
		Toast.makeText(this, "Item #" + position + " was dismissed by overlay touch", Toast.LENGTH_SHORT).show();
	}

	private void presentShowcaseSequence() {
		ShowcaseConfig config = new ShowcaseConfig();
		config.setDelay(500); // half second between each showcase view

		MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);

		sequence.setOnItemDismissedByButtonListener(this);
		sequence.setOnItemDismissedByOverlayListener(this);

		sequence.setConfig(config);

		sequence.addSequenceItem(
			new MaterialShowcaseView.Builder(this)
				.setTarget(mButtonOne)
				.setDismissOnTouch(true)
				.setDismissText("GOT IT")
				.setContentText("This is button one")
				.build()
		);

		sequence.addSequenceItem(
			new MaterialShowcaseView.Builder(this)
				.setTarget(mButtonTwo)
				.setDismissOnTouch(true)
				.setDismissText("GOT IT")
				.setContentText("This is button two")
				.withRectangleShape(true)
				.build()
		);

		sequence.addSequenceItem(
			new MaterialShowcaseView.Builder(this)
				.setTarget(mButtonThree)
				.setDismissOnTouch(true)
				.setDismissText("GOT IT")
				.setContentText("This is button three")
				.withRectangleShape()
				.build()
		);

		sequence.start();
	}
}