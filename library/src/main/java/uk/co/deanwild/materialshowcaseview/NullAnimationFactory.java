package uk.co.deanwild.materialshowcaseview;

import android.graphics.Point;
import android.view.View;

public class NullAnimationFactory implements IAnimationFactory {
	@Override public void animateInView(View target, Point point, long duration,
		AnimationStartListener listener) {
	}

	@Override public void animateOutView(View target, Point point, long duration,
		AnimationEndListener listener) {
	}

	@Override public void animateTargetToPoint(MaterialShowcaseView showcaseView, Point point) {
	}
}
