package uk.co.deanwild.materialshowcaseview;

import android.app.Activity;
import android.view.View;

import java.util.LinkedList;
import java.util.Queue;


public class MaterialShowcaseSequence implements IDetachedListener, MaterialShowcaseView.OnDismissedListener {

    PrefsManager mPrefsManager;
    Queue<MaterialShowcaseView> mShowcaseQueue;
    private boolean mSingleUse = false;
    Activity mActivity;
    private ShowcaseConfig mConfig;
    private int mSequencePosition = 0;

    private OnSequenceItemShownListener mOnItemShownListener = null;
    private OnSequenceItemDismissedListener mOnItemDismissedListener = null;
    private OnSequenceItemDismissedByButtonListener onItemDismissedByButtonListener = null;
    private OnSequenceItemDismissedByOverlayListener onItemDismissedByOverlayListener = null;

    public MaterialShowcaseSequence(Activity activity) {
        mActivity = activity;
        mShowcaseQueue = new LinkedList<>();
    }

    public MaterialShowcaseSequence(Activity activity, String sequenceID) {
        this(activity);
        this.singleUse(sequenceID);
    }

    public MaterialShowcaseSequence addSequenceItem(View targetView, String content, String dismissText) {
        addSequenceItem(targetView, "", content, dismissText);
        return this;
    }

    public MaterialShowcaseSequence addSequenceItem(View targetView, String title, String content, String dismissText) {

        MaterialShowcaseView sequenceItem = new MaterialShowcaseView.Builder(mActivity)
                .setTarget(targetView)
                .setTitleText(title)
                .setDismissText(dismissText)
                .setContentText(content)
                .build();

        if (mConfig != null) {
            sequenceItem.setConfig(mConfig);
        }

        mShowcaseQueue.add(sequenceItem);
        return this;
    }

    public MaterialShowcaseSequence addSequenceItem(MaterialShowcaseView sequenceItem) {
        mShowcaseQueue.add(sequenceItem);
        return this;
    }

    public MaterialShowcaseSequence singleUse(String sequenceID) {
        mSingleUse = true;
        mPrefsManager = new PrefsManager(mActivity, sequenceID);
        return this;
    }

    public void setOnItemShownListener(OnSequenceItemShownListener listener) {
        this.mOnItemShownListener = listener;
    }

    public void setOnItemDismissedListener(OnSequenceItemDismissedListener listener) {
        this.mOnItemDismissedListener = listener;
    }

    public void setOnItemDismissedByButtonListener(OnSequenceItemDismissedByButtonListener listener) {
        this.onItemDismissedByButtonListener = listener;
    }

    public void setOnItemDismissedByOverlayListener(OnSequenceItemDismissedByOverlayListener listener) {
        this.onItemDismissedByOverlayListener = listener;
    }

    public boolean hasFired() {

        if (mPrefsManager.getSequenceStatus() == PrefsManager.SEQUENCE_FINISHED) {
            return true;
        }

        return false;
    }

    public void start() {

        /**
         * Check if we've already shot our bolt and bail out if so         *
         */
        if (mSingleUse) {
            if (hasFired()) {
                return;
            }

            /**
             * See if we have started this sequence before, if so then skip to the point we reached before
             * instead of showing the user everything from the start
             */
            mSequencePosition = mPrefsManager.getSequenceStatus();

            if (mSequencePosition > 0) {
                for (int i = 0; i < mSequencePosition; i++) {
                    mShowcaseQueue.poll();
                }
            }
        }


        // do start
        if (mShowcaseQueue.size() > 0)
            showNextItem();
    }

    private void showNextItem() {

        if (mShowcaseQueue.size() > 0 && !mActivity.isFinishing()) {
            MaterialShowcaseView sequenceItem = mShowcaseQueue.remove();
            sequenceItem.setDetachedListener(this);
            sequenceItem.setOnDismissedListener(this);
            sequenceItem.show(mActivity);
            if (mOnItemShownListener != null) {
                mOnItemShownListener.onShow(sequenceItem, mSequencePosition);
            }
        } else {
            /**
             * We've reached the end of the sequence, save the fired state
             */
            if (mSingleUse) {
                mPrefsManager.setFired();
            }
        }
    }


    @Override
    public void onShowcaseDetached(MaterialShowcaseView showcaseView, boolean wasDismissed) {

        showcaseView.setDetachedListener(null);

        /**
         * We're only interested if the showcase was purposefully dismissed
         */
        if (wasDismissed) {

            if (mOnItemDismissedListener != null) {
                mOnItemDismissedListener.onDismiss(showcaseView, mSequencePosition);
            }

            mSequencePosition++;
            /**
             * If so, update the prefsManager so we can potentially resume this sequence in the future
             */
            if (mPrefsManager != null) {
                mPrefsManager.setSequenceStatus(mSequencePosition);
            }

            showNextItem();
        }
    }

    @Override
    public void onDismiss(MaterialShowcaseView view, DismissType dismissType) {
        if (this.onItemDismissedByButtonListener != null &&
            dismissType == DismissType.DISMISS_BUTTON) {
            this.onItemDismissedByButtonListener.onDismissedByButtonTouch(view, mSequencePosition);
        } else if (this.onItemDismissedByOverlayListener != null &&
            dismissType == DismissType.OVERLAY) {
            this.onItemDismissedByOverlayListener.onDismissedByOverlayTouch(view, mSequencePosition);
        }
    }

    public void setConfig(ShowcaseConfig config) {
        this.mConfig = config;
    }

    public interface OnSequenceItemShownListener {
        void onShow(MaterialShowcaseView itemView, int position);
    }

    public interface OnSequenceItemDismissedListener {
        void onDismiss(MaterialShowcaseView itemView, int position);
    }

    public interface OnSequenceItemDismissedByButtonListener {
        void onDismissedByButtonTouch(MaterialShowcaseView itemView, int position);
    }

    public interface OnSequenceItemDismissedByOverlayListener {
        void onDismissedByOverlayTouch(MaterialShowcaseView itemView, int position);
    }
}
