package uk.co.deanwild.materialshowcaseview.target;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;


public class ViewTarget implements Target {

    private final View mView;
    private boolean boundsContraintEnabled;

    public ViewTarget(View view) {
        this(view, false);
    }

    public ViewTarget(View view, boolean boundsContraintEnabled) {
        this.mView = view;
        this.boundsContraintEnabled = boundsContraintEnabled;
    }

    public ViewTarget(int viewId, Activity activity) {
        mView = activity.findViewById(viewId);
    }

    @Override
    public Point getPoint() {
        int[] location = getLocation();
        int x, y;
        x = location[0] + mView.getWidth() / 2;
        if (boundsContraintEnabled) {
            y = (int)(location[1] + mView.getHeight() / 3.5);
        } else {
            y = location[1] + mView.getHeight() / 2;
		}
        return new Point(x, y);
    }

    @Override
    public Rect getBounds() {
        int[] location = getLocation();
        int left, top, right, bottom;
        left = location[0];
        top = location[1];
        if (boundsContraintEnabled) {
            int minSize = Math.min(mView.getWidth(), mView.getHeight());
            right = (int)(location[0] + (minSize - minSize * 0.025));
            bottom = (int)(location[1] + (minSize - minSize * 0.025));
        } else {
            right = location[0] + mView.getMeasuredWidth();
            bottom = location[1] + mView.getMeasuredHeight();
        }
        return new Rect(left, top, right, bottom);
    }

    private int[] getLocation() {
        int[] location = new int[2];
        mView.getLocationInWindow(location);
        return location;
    }
}
