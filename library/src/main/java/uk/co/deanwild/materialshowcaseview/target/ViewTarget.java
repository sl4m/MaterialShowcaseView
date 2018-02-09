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
        if (this.boundsContraintEnabled) {
            x = location[0] + 200;
            y = location[1] + mView.getHeight() - 200;
        } else {
            x = location[0] + mView.getWidth() / 2;
            y = location[1] + mView.getHeight() / 2;
        }
        return new Point(x, y);
    }

    @Override
    public Rect getBounds() {
        int[] location = getLocation();
        int right, bottom;
        if (boundsContraintEnabled) {
            right = location[0] + mView.getMeasuredWidth()/2;
            bottom = location[1] + mView.getMeasuredHeight()/2;
        } else {
            right = location[0] + mView.getMeasuredWidth();
            bottom = location[1] + mView.getMeasuredHeight();
        }
        return new Rect(location[0], location[1], right, bottom);
    }

    private int[] getLocation() {
        int[] location = new int[2];
        mView.getLocationInWindow(location);
        return location;
    }
}
