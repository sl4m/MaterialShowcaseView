package uk.co.deanwild.materialshowcaseview.target;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;


public class ViewTarget implements Target {

    private final View mView;
    private double boundsContraint;

    public ViewTarget(View view) {
        this(view, 1);
    }

    public ViewTarget(View view, double boundsContraint) {
        this.mView = view;
        this.boundsContraint = normalizeBounds(boundsContraint);
    }

    public ViewTarget(int viewId, Activity activity) {
        mView = activity.findViewById(viewId);
    }

    @Override
    public Point getPoint() {
        int[] location = getLocation();
        int x, y;
        x = location[0] + mView.getWidth() / 2;
        y = location[1] + mView.getHeight() / 2;
        return new Point(x, y);
    }

    @Override
    public Rect getBounds() {
        int[] location = getLocation();
        int right, bottom;
        right = (int)(location[0] + mView.getMeasuredWidth() * boundsContraint);
        bottom = (int)(location[1] + mView.getMeasuredHeight() * boundsContraint);
        return new Rect(location[0], location[1], right, bottom);
    }

    private int[] getLocation() {
        int[] location = new int[2];
        mView.getLocationInWindow(location);
        return location;
    }

    private double normalizeBounds(double boundsContraint) {
        if (boundsContraint > 1) {
            return 1;
        } else if (boundsContraint < 0.1) {
            return 0.1;
        } else {
            return boundsContraint;
        }
    }
}
